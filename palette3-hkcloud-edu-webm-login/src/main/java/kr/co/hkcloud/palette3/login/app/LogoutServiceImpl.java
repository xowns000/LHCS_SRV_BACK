package kr.co.hkcloud.palette3.login.app;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.dao.LoginMapper;
import kr.co.hkcloud.palette3.login.dao.LogoutMapper;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Orange
 */
@Slf4j
@RequiredArgsConstructor
@Service("logoutService")
public class LogoutServiceImpl implements LogoutService {

	private final InnbCreatCmmnService innbCreatCmmnService;
	private final TwbComDAO mobjDao;
	private final LoginMapper loginDao;
	private final LogoutMapper logoutDao;

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${jwt.token.info}")
	private String JWT_TOKEN_INFO;

	@Value("${jwt.token.refresh}")
	private String JWT_TOKEN_REFRESH;

	@Value("${jwt.token.logout}")
	private String JWT_TOKEN_LOGOUT;

	@Value("${jwt.token.nm}")
	private String JWT_TOKEN_NM;


	/**
	 * 로그 아웃
	 *
	 * @param logout
	 * @param req
	 * @return
	 * @throws TelewebAppException
	 * @throws AccountExpiredException
	 */
	public TelewebJSON logout(LoginDTO.Logout logout, HttpServletRequest req) throws AccountExpiredException {
		TelewebJSON objRetParams = new TelewebJSON();

		String accessToken = "";
		String userRefreshToken = "";
		String refreshToken = "";
		String loginId = "";
		int userId;

		String jessionid = "";

		accessToken = String.valueOf(logout.getAccessToken());
		jessionid = String.valueOf(logout.getJessionid());
		userId = logout.getUserid();

		/* Access Token 검증 */
		ErrorCode errorCode = jwtTokenProvider.validateToken(logout.getAccessToken());
		if (errorCode != ErrorCode.OK) {
			throw new BadCredentialsException(errorCode.getMessage());
		}

		/* Access Token 에서 User eml 을 가져옵니다. */
		Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

		loginId = String.valueOf(authentication.getName());
		userRefreshToken = String.valueOf(redisTemplate.opsForValue().get(
			JWT_TOKEN_INFO + ":" + loginId + ":" + accessToken));
		refreshToken = String.valueOf(redisTemplate.opsForValue().get(
			JWT_TOKEN_REFRESH + ":" + loginId));

		/* Redis 에서 해당 User Id 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다. */
		if (refreshToken != null && !refreshToken.isEmpty() && !refreshToken.equals("null")) {

			/* Redis 에서 해당 User Id + AccessToken = Refresh Token 이 있는지 여부를 확인 */
			if (userRefreshToken != null && !userRefreshToken.isEmpty()
				&& !userRefreshToken.equals("null")) {

				/* User Id + AccessToken == Refresh Token 같은 경우 로그아웃 처리 */
				if (userRefreshToken.equals(refreshToken)) {

					/* Redis Delete */
					redisTemplate.delete(JWT_TOKEN_INFO + ":" + authentication.getName() + ":"
						+ logout.getAccessToken());        //사용자_AccessToken 기준 Refresh Token 삭제
					redisTemplate.delete(JWT_TOKEN_REFRESH + ":"
						+ authentication.getName());                                    //Refresh Token Delete

					/* 로그인시 생성된 세션정보 레디스 삭제 */
					if (jessionid != null && !jessionid.isEmpty() && !jessionid.equals("null")
						&& !jessionid.equals("")) {
						redisTemplate.delete("palette:session:sessions:expires:" + jessionid);
						redisTemplate.delete("palette:session:sessions:" + jessionid);
					}

					HttpSession session = req.getSession();

					LoginLogDTO loginLogDTO = new LoginLogDTO();
					loginLogDTO.setUserId(userId);                            //userId
					loginLogDTO.setTaskSeCd("LOGOUT");
					loginLogDTO.setCntnIp(PaletteServletRequestSupport.getClientIp(req));
					loginLogDTO.setEtcInfo01(authentication.getName());                        //userId
					//                    loginLogDTO.setWrtrId(authentication.getName());
					loginLogDTO.setPrstLgnYn("N");

					//로그아웃 일괄 처리
					processLogout(loginLogDTO, session);
				}

			}

		}

		/* JSON Header JWT_TOKEN_NM 초기화 */
		objRetParams.setHeader(JWT_TOKEN_NM, "");
		objRetParams.setHeader("ERROR_FLAG", "false");
		objRetParams.setHeader("ERROR_MSG", "정상적으로 로그아웃 되었습니다.");

		/* 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기 */
		Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());

		redisTemplate.opsForValue().set(logout.getAccessToken(), JWT_TOKEN_LOGOUT, expiration, TimeUnit.MILLISECONDS);

		return objRetParams;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void processLogout(LoginLogDTO loginLogDTO, HttpSession session) throws TelewebAppException {
		String logId = innbCreatCmmnService.getLoginNextStringId();

		loginLogDTO.setUserLogId(logId);

		/* 접속 로그를 기록한다. */
		loginDao.insertLog(loginLogDTO);

		/* 사용자정보를 업데이트 한다. */
		logoutDao.updateLogoutSuccessInfo(loginLogDTO);

		/* 2018.11.15 kmg 이석상태에서 로그오프 시, 이석상태 기록 */
		TelewebJSON jsonParams = new TelewebJSON();
		String telIdentifier = String.format("%s-%s", DateFormatUtils.format((new Date()), "HH:mm:ss"), UUID.randomUUID().toString().substring(0, 8));

		jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
		jsonParams.setInt("USER_ID", (Integer) loginLogDTO.getUserId());

		/* 로그아웃 시 고객사키 정보가 없음.sjh */
		// String custcoId =  (String)session.getAttribute("ASP_CUST_INFO");
		// jsonParams.setString("CUSTCO_ID", custcoId);

		/* DAO검색 메서드 호출 */
		TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId", jsonParams);

		if (objRetParams.getInt("CNT") > 0) {

			/* 1. 채팅OFF 종료시간 업데이트 */
			mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);

			/* 2. 채팅OFF 히스토리 기록 */
			jsonParams.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));
			mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);

			/* 3. 채팅OFF 초기화 (고객사 키 제거 로그아웃시 없음 ) */
			mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);

			/* 4. 채팅ON 초기화 */
			mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", jsonParams);
		}

		/**
		 * HYG :: TELETALK 동시접속 동시계정 채팅on 버그 패치 적용 로그아웃 시 REDIS 대기에서 삭제, LIY 20200301
		 * redisChatReadyRepository.removeUserId(loginLogDTO.getUserId());
		 */
		log.trace("session={}\n getUserId={}\n TWB_SESSION_INFO={}", session, loginLogDTO.getUserId(), session.getAttribute("TWB_SESSION_INFO"));
	}
}
