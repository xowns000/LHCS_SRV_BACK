package kr.co.hkcloud.palette3.login.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.config.security.util.PaletteSecurityUtils;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.dao.LoginMapper;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;
import kr.co.hkcloud.palette3.login.exception.AdministratorResetsPasswordException;
import kr.co.hkcloud.palette3.login.exception.FirstLoginException;
import kr.co.hkcloud.palette3.login.exception.RejectClientIPException;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Orange
 */
@Slf4j
@RequiredArgsConstructor
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    private final InnbCreatCmmnService innbCreatCmmnService;

    private final LoginMapper loginDao;
    private final TwbComDAO mobjDao;

    private final UserDetailsService userDetailsService;
    private final PaletteSecurityUtils paletteSecurityUtils;

    private final PaletteSecurityProperties paletteSecurityProperties;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    private final TwbUserBizServiceImpl twbUserBizServiceImpl;

    private final SseService sseService;
    private final PaletteUtils telewebCommUtil;

    @Value("${jwt.token.shc256}")
    private String JWT_TOKEN_SHC256;

    @Value("${jwt.token.info}")
    private String JWT_TOKEN_INFO;

    @Value("${jwt.token.refresh}")
    private String JWT_TOKEN_REFRESH;

    @Value("${jwt.token.nm}")
    private String JWT_TOKEN_NM;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON login(LoginDTO.Login login, HttpServletRequest req) throws AccountExpiredException {

        TelewebJSON objRetParams = new TelewebJSON();

        /**
         * Login ID/PW 를 기반으로 Authentication 객체 생성 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
         */
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        String loginId = "";
        String accessToken = "";
        String refreshToken = "";

        /* ASP trial 기간 체크, 서비스 유지 여부 체크 */
        TelewebJSON aspParams = new TelewebJSON();
        TelewebJSON retTrial = new TelewebJSON();
        TelewebJSON retService = new TelewebJSON();

        String telIdentifier = "";

        String credentials = "";

        String shaPwd = "";
        String encryptKey = "";

        String decCredentials = null;
        String decryptKey = null;
        String decPwd = null;
        String keyVal = "";        // Redis Sha256 암호화 키값 추출

        /* VID 체크 : 허용된 IP에서만 접근 가능 */
        if (!paletteSecurityUtils.checkVDI(req)) {
            throw new RejectClientIPException("Invalid Client IP :::");
        }

        /* 사용자 체크 */
        loginId = authenticationToken.getName();

        if (StringUtils.isEmpty(loginId)) {
            throw new BadCredentialsException("Invalid username or password :::");
        }

        /**
         * 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분 authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행 Authentication
         * authentication =
         * authenticationManagerBuilder.getObject().authenticate(authenticationToken);
         */
        PaletteUserDetailsVO userDetailsVO = (PaletteUserDetailsVO) userDetailsService.loadUserByUsername(loginId);

        /* 사용자 체크 */
        if (ObjectUtils.isEmpty(userDetailsVO)) {
            throw new BadCredentialsException("Invalid username or password :::");
        }

        /**
         * accountNonLocked - 계정 잠김 (비밀번호실패회수 초과여부) 관리자가 비밀번호를 초기화하여 해제함
         */
        if (!userDetailsVO.isAccountNonLocked()) {
            throw new LockedException("Account Locked :::");
        }

        /* enabled - 계정 비활성 (사용허가상태 여부 체크) */
        if (!userDetailsVO.isEnabled()) {
            throw new DisabledException("Account Disabled :::");
        }

        /* accountNonExpired - 계정만료 (사용기간이 있는 계정인 경우) */
        if (!userDetailsVO.isAccountNonExpired()) {
            throw new AccountExpiredException("Account Expired :::");
        }

        /* credentialsNonExpired - 비밀번호 만료여부 */
        if (!userDetailsVO.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials Expired :::");
        }

        /* FirstLogin - 최초 로그인 여부 (비밀번호 변경) */
        if (paletteSecurityProperties.getPwdUserTerm().isFirstLogin() && (StringUtils.isEmpty(userDetailsVO.getPwdStatus()) || "FIRST"
            .equalsIgnoreCase(userDetailsVO.getPwdStatus()))) {
            throw new FirstLoginException("First Login :::");
        }

        /* AdministratorResetsPassword - 관리자가 비밀번호를 초기화 했는지 체크 */
        if (paletteSecurityProperties.getPwdUserTerm().isReset() && "RESET".equalsIgnoreCase(userDetailsVO.getPwdStatus())) {
            throw new AdministratorResetsPasswordException("Administrator Resets Password :::");
        }

        credentials = authenticationToken.getCredentials().toString();

        shaPwd = userDetailsVO.getPassword();
        encryptKey = userDetailsVO.getEncryptKey();

        keyVal = String.valueOf(redisTemplate.opsForValue().get(JWT_TOKEN_SHC256 + ":" + loginId));        // Redis Sha256 암호화 키값 추출

        try {

            decCredentials = twbUserBizServiceImpl.pwdDecString(credentials, keyVal);

            decryptKey = twbUserBizServiceImpl.keyDecString(encryptKey);
            decPwd = twbUserBizServiceImpl.pwdDecString(shaPwd, decryptKey);
        } catch (Exception e) {
            log.error("userBiz.password decyript fail :::", e);
            throw new BadCredentialsException("Invalid username or password :::");
        }

        /* 비밀번호 체크 */
        log.trace("입력된 패스워드 = {} 토큰 패스워드(SHA256) = {}", userDetailsVO.getPassword(), authenticationToken.getCredentials().toString());

        if (StringUtils.equals(decPwd, decCredentials)) {

            try {
                /* 로그인 성공 처리 */
                String cntnIp = PaletteServletRequestSupport.getClientIp(req);
                LoginLogDTO loginLogDTO = new LoginLogDTO();
                loginLogDTO.setUserId(userDetailsVO.getUserId());  //userId
                loginLogDTO.setTaskSeCd("LOGIN");
                loginLogDTO.setCntnIp(cntnIp);
                loginLogDTO.setEtcInfo01(userDetailsVO.getName());
                loginLogDTO.setWrtrId(userDetailsVO.getUserId());
                loginLogDTO.setPrstLgnYn("Y");
                loginLogDTO.setPswdFailCnt("0");
                loginLogDTO.setCustcoId(Integer.parseInt(userDetailsVO.getCustcoId()));

                /* 로그인 일괄 처리 */
                //LinkedHashMap<String, String> selectLoginSession = processLogin(loginLogDTO);
                //String mapAsString = selectLoginSession.keySet().stream().map(key -> selectLoginSession.get(key)).collect(Collectors.joining("|"));
                //log.debug("mapAsString={}", mapAsString);

                objRetParams = loginProcess(loginLogDTO);
                log.info("cntnIp={}", cntnIp);
                log.info("duplicateLogin={}", paletteSecurityProperties.isDuplicateLogin());
                log.info("duplicateLoginAllowip={}", paletteSecurityProperties.getDuplicateLoginAllowip());

                // 중복로그인 여부설정체크
                if (!paletteSecurityProperties.isDuplicateLogin() && !this.isDuplicateLoginAccessible(cntnIp)) {

                    if ("N".equals(userDetailsVO.getDpcnLgnPmYn())) { //중복 로그인 허용이 아닌 경우
                        String roomId = userDetailsVO.getTenantId() + "_" + userDetailsVO.getCustcoId();
                        List<String> loginUserList = hashOpsEnterInfo.values("SSE_ROOM_ENTER_INFO_" + roomId);
                        log.info("loginUserList.size={}", loginUserList.size());
                        for (String loginUserId : loginUserList) {
                            if (loginUserId.equals(String.valueOf(userDetailsVO.getUserId()))) {
                                // 중복로그인 인경우. 로그아웃 전송.
                                SseMessage sseMessage = new SseMessage();
                                sseMessage.setType(SseMessage.MessageType.SYSTEM_LOGOUT);
                                sseMessage.setSender("2");
                                sseMessage.setReceiver(loginUserId);    // ALL은 전체 , userId 개인별
                                sseMessage.setRoomId(roomId);
                                sseMessage.setSecond(-1); //필수 아님. 기본이 5초
                                sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
                                sseMessage.setMessage("보안↔" + cntnIp + " IP에서 동일계정으로 로그인되었습니다.\n 자동 로그아웃 되었습니다.(" + telewebCommUtil
                                    .getFormatString("yyyy-MM-dd HH:mm:ss") + ")");    //메시지
                                try {
                                    sseService.sendMessage(sseMessage);
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    log.error("sseMessage is exception : " + e.getMessage());
                                }
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                log.error("userBiz.processLogin fail :::", e);
            }

            /* 인증 정보를 기반으로 JWT 토큰 생성 */
            TokenDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userDetailsVO);

            accessToken = String.valueOf(tokenInfo.getAccessToken());
            refreshToken = String.valueOf(tokenInfo.getRefreshToken());

            /* Redis Delete */
            redisTemplate.delete(JWT_TOKEN_SHC256 + ":" + loginId);    //Redis SHA256 ENC 키값 삭제
            redisTemplate.delete(JWT_TOKEN_REFRESH + ":" + loginId);    //리프레쉬 토큰 삭제

            /* Redis Set */
            redisTemplate.opsForValue().set(JWT_TOKEN_INFO + ":" + loginId + ":" + accessToken, refreshToken, tokenInfo
                .getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS); //사용자_AccessToken 기준 Refresh Token
            redisTemplate.opsForValue().set(JWT_TOKEN_REFRESH + ":" + loginId, refreshToken, tokenInfo.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS); // 리프레쉬 토큰 저장

            objRetParams.setHeader(JWT_TOKEN_NM, tokenInfo.getAccessToken());

        } else {
            try {
                TelewebJSON jsonParams = new TelewebJSON();
                twbUserBizServiceImpl.updateRtnFailPwd(jsonParams, userDetailsVO.getUserId(), userDetailsVO.getPwdFailFreq());
            } catch (Exception e) {
                log.error("userBiz.updateRtnFailPwd fail :::", e);
            }
            throw new BadCredentialsException("Invalid username or password :::" + (userDetailsVO.getPwdFailFreq() + 1));
        }

        log.debug("mapAsString={}", objRetParams);

        /** 로그아웃 시 레디스의 세션 정보를 삭제하기 위한 값 */
        HttpSession session = req.getSession();

        objRetParams.setHeader("JSESSIONID", session.getId());
        //        log.info("objRetParamobjRetParamobjRetParam"+objRetParams.getDataObject("DATA").toString());

        return objRetParams;
    }

    @Transactional(readOnly = false)
    public TelewebJSON loginProcess(LoginLogDTO loginLogDTO) throws TelewebAppException {
        String logId = innbCreatCmmnService.getLoginNextStringId();
        loginLogDTO.setUserLogId(logId);

        //접속 로그를 기록한다.
        loginDao.insertLog(loginLogDTO);

        //사용자정보를 업데이트 한다.
        loginDao.updateLoginSuccessInfo(loginLogDTO);

        //DELETE TALK_READY USER_ID
        //TelewebJSON inJson = new TelewebJSON();
        //inJson.setString("USER_ID", loginLogDTO.getUserId());
        //routingToAgentReadyDAO.deleteTalkReady(inJson);

        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = String.format("%s-%s", DateFormatUtils.format((new Date()), "HH:mm:ss"), UUID.randomUUID().toString()
            .substring(0, 8));
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setInt("USER_ID", loginLogDTO.getUserId());

        //DAO검색 메서드 호출 , 중복 로그인 시 상담원 상태에 대한 초기화가 필요함 ( 로그아웃에 초기화 로직이 걸려있으므로 중복로그인은 로그아웃 처리를 거치지 않기때문임 )
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId",
            jsonParams);

        if (objRetParams.getInt("CNT") > 0) {

            //1. 채팅OFF 종료시간 업데이트
            mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);

            //2. 채팅OFF 히스토리 기록
            jsonParams.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));    // idgen 수정 sjh 20190429
            mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);

            //3. 채팅OFF 초기화 (고객사 키 제거 로그아웃시 없음 )
            mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);

            //4. 채팅ON 초기화
            mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", jsonParams);
        }

        //로그인정보 조회
        TelewebJSON paramParams = new TelewebJSON();
        TelewebJSON objParams = new TelewebJSON();

        paramParams.setInt("USER_ID", loginLogDTO.getUserId());
        paramParams.setInt("CUSTCO_ID", loginLogDTO.getCustcoId());

        objParams = mobjDao.select("kr.co.hkcloud.palette3.login.dao.LoginMapper", "selectLoginSuccessLoginInfo", paramParams);

        // 시스템 ID (2, 3, 4) 일 때는 2차인증여부 "N"
        if ("2".equals(objParams.getString("USER_ID")) || "3".equals(objParams.getString("USER_ID")) || "4".equals(objParams.getString(
            "USER_ID"))) {
            objParams.setString("USER_LGN_SMS_CERT_YN", "N");
        }
        return objParams;
    }

    @Override
    @Transactional(readOnly = false)
    public LinkedHashMap<String, String> processLogin(LoginLogDTO loginLogDTO) throws TelewebAppException {
        String logId = innbCreatCmmnService.getLoginNextStringId();
        loginLogDTO.setUserLogId(logId);

        //접속 로그를 기록한다.
        loginDao.insertLog(loginLogDTO);

        //사용자정보를 업데이트 한다.
        loginDao.updateLoginSuccessInfo(loginLogDTO);

        //DELETE TALK_READY USER_ID
        //TelewebJSON inJson = new TelewebJSON();
        //inJson.setString("USER_ID", loginLogDTO.getUserId());
        //routingToAgentReadyDAO.deleteTalkReady(inJson);

        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = String.format("%s-%s", DateFormatUtils.format((new Date()), "HH:mm:ss"), UUID.randomUUID().toString()
            .substring(0, 8));
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setInt("USER_ID", loginLogDTO.getUserId());

        //DAO검색 메서드 호출 , 중복 로그인 시 상담원 상태에 대한 초기화가 필요함 ( 로그아웃에 초기화 로직이 걸려있으므로 중복로그인은 로그아웃 처리를 거치지 않기때문임 )
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId",
            jsonParams);

        if (objRetParams.getInt("CNT") > 0) {

            //1. 채팅OFF 종료시간 업데이트
            mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);

            //2. 채팅OFF 히스토리 기록
            jsonParams.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));    // idgen 수정 sjh 20190429
            mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);

            //3. 채팅OFF 초기화 (고객사 키 제거 로그아웃시 없음 )
            mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);

            //4. 채팅ON 초기화
            mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", jsonParams);
        }

        //세션정보 조회
        return loginDao.selectLoginSuccessSessionInfo(loginLogDTO);
    }

    /**
     * 중복로그인허용 아이피여부조회.
     * 
     * @param ipAddress
     * @return
     */
    public boolean isDuplicateLoginAccessible(String ipAddress) {
        List<IpAddressMatcher> ipAddressMatchers = new ArrayList<>();
        String allowIp = paletteSecurityProperties.getDuplicateLoginAllowip();
        String[] splitStr = allowIp.split(",");
        for (String s : splitStr) {
            if (StringUtils.isNotEmpty(s)) {
                ipAddressMatchers.add(new IpAddressMatcher(s.trim())); // Spring Security에서 제공하는 IP또는 IP 대역여부를 판단하기 위한 객체
            }
        }
        return ipAddressMatchers.stream().anyMatch(matcher -> matcher.matches(ipAddress));
    }
}
