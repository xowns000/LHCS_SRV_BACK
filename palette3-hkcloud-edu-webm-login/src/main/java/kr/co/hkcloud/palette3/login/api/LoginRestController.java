package kr.co.hkcloud.palette3.login.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.security.SecureRandom;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserLogBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.app.LoginService;
import kr.co.hkcloud.palette3.login.domain.LoginDTO.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;



@Slf4j
@RequiredArgsConstructor
@RestController("LoginRestController")
@Api(value = "LoginRestController", description = "텔레웹 로그인 컨트롤러")
public class LoginRestController
{
    private final TwbUserBizServiceImpl    twbUserBizServiceImpl;
    private final TwbUserLogBizServiceImpl twbUserLogBizServiceImpl;

    private final LoginService loginService;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.token.shc256}")
    private String JWT_TOKEN_SHC256;

    @Value("${jwt.token.nm}")
    private String JWT_TOKEN_NM;


    /**
     * 로그인
     *
     * @param  mjsonParams
     * @param  mobjRequest
     * @param  result
     * @return                         TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebAppException
     * @throws AccountExpiredException
     * @throws TelewebApiException
     */
    @ApiOperation(value = "로그인",
        notes = "로그인 처리")
    @PostMapping("/auth-api/login")
    //public Object login(@Validated Login login, Errors errors, HttpServletRequest req) throws AccountExpiredException, TelewebAppException
    public Object login(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest req) throws AccountExpiredException, TelewebAppException
    {

        Login login = new Login();

        /* 반환 파라메터 생성 */
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String username = "";
        String userpassword = "";

        username = mjsonParams.getString("username");
        userpassword = mjsonParams.getString("password");

        // validation check
        //if (errors.hasErrors()) {
        //	throw new BadCredentialsException(String.valueOf(Helper.refineErrors(errors)));
        //}

        if(username.isEmpty() || username == null || userpassword.isEmpty() || userpassword == null) { throw new BadCredentialsException("필수 입력값을 입력하세요"); }

        login.setUsername(username);
        login.setUserpassword(userpassword);
        login.toAuthentication();

        //최종결과값 반환
        objRetParams = loginService.login(login, req);

        return objRetParams;
    }


    /**
     * 비밀번호 수정
     *
     * @param  mjsonParams
     * @param  mobjRequest
     * @param  result
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "비밀번호 수정",
        notes = "비밀번호를 수정한다.")
    @PostMapping("/auth-api/password/modify")
    public Object passwordUpdt(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest, BindingResult result) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String userId =  mjsonParams.getString("USER_ID");
        /*
         * loginValidator.validate(mjsonParams, result); //VALIDATION 체크 if(result.hasErrors()) { throw new ValidationException(result.getAllErrors()); }
         */

        //비밀번호변경 가능 여부 체크
        if(!validationUpdatePwd(mjsonParams, objRetParams)) { return objRetParams; }

        //데이터 체크가 통과할 경우만 처리
        objRetParams = twbUserBizServiceImpl.updateRtnPwd(mjsonParams);

        mjsonParams.setString("RESET_USER_ID", userId); // 화면에서 넘겨주는 USER_ID 떄문에 비밀번호 초기화 쿼리에서 구별하기위한 변수로 변경
        //비밀번호 변경 로그를 기록한다.
        twbUserLogBizServiceImpl.insertRtnChangePwdInfo(mjsonParams, mobjRequest.getRemoteAddr());

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 비밀번호 수정시 데이터 체크
     *
     * @param  jsonParams   전송된 파라메터 데이터
     * @param  objRetParams 반환파라메터
     * @return              true:처리가능상태, false:처리불가능상태
     */
    private boolean validationUpdatePwd(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException
    {

        //상태초기값 설정
        objRetParams.setHeader("ERROR_FLAG", true);

        //회원정보를 검색한 후 비밀번호 업데이트에 필요한 체크를 수행한다.  LGN_ID로 체크
        TelewebJSON jsonUserInfo = twbUserBizServiceImpl.selectRtnForID(jsonParams);

        //ID가 존재하는지 체크
        if(jsonUserInfo.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_MSG", "등록되지 않은 ID 입니다.");
            return false;
        }

        if(jsonParams.getString("CHK_OLD_FLAG").equals("Y")) {
            //기존 비밀번호가 일치하는지 체크
            //서블릿 컨텍스트에서 세션 추출
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest objRequest = requestAttributes.getRequest();
            HttpSession mobjSession = objRequest.getSession();

            String shaPwd = jsonUserInfo.getString("PSWD");									// 사용자 테이블을 조회 한 해당 사용자의 기존 비밀번호
//            log.debug("shaPwd ===================>>>>>>>>>>>>>>>>>>" + shaPwd);
            String credentials = jsonParams.getString("PASSWORD_OLD");						// 암호화 되어 넘어온 기존 비밀번호
//            log.debug("credentials ===================>>>>>>>>>>>>>>>>>>" + credentials);
            String encryptKey = jsonUserInfo.getString("PSWD_ENCPT_CD");					// 기존 비밀번호 암호화 키
//            log.debug("encryptKey ===================>>>>>>>>>>>>>>>>>>" + encryptKey);
            String decCredentials = null;
            String decryptKey = null;
            String decPwd = null;
            //String keyVal = (String) mobjSession.getAttribute("ENCRYPT_KEY");
            //String.valueOf(redisTemplate.opsForValue().get(JWT_TOKEN_SHC256 + ":" + userId));	
            String userId = jsonParams.getString("LGN_ID");							// 사용자의 로그인 ID
            String keyVal = String.valueOf(redisTemplate.opsForValue().get(JWT_TOKEN_SHC256 + ":" + userId));

//            log.debug("keyVal ===================>>>>>>>>>>>>>>>>>>" + keyVal);
            jsonParams.setString("SESSION_ENCRYPT_KEY", keyVal);
            mobjSession.setAttribute("ENCRYPT_KEY", "");
            jsonParams.setString("ENCRYPT_KEY", encryptKey);

            decCredentials = twbUserBizServiceImpl.pwdDecString(credentials, keyVal);
//            log.debug("decCredentials ===================>>>>>>>>>>>>>>>>>>" + decCredentials);
            decryptKey = twbUserBizServiceImpl.keyDecString(encryptKey);
//            log.debug("decryptKey ===================>>>>>>>>>>>>>>>>>>" + decryptKey);
            decPwd = twbUserBizServiceImpl.pwdDecString(shaPwd, decryptKey);
//            log.debug("decPwd ===================>>>>>>>>>>>>>>>>>>" + decPwd);

            if(!decCredentials.equals(decPwd)) {
                objRetParams.setHeader("ERROR_MSG", "현재 비밀번호가 일치하지 않습니다.");
                return false;
            }
        }

        //초기값 설정
        objRetParams.setHeader("ERROR_FLAG", false);

        return true;
    }


    /**
     * 비밀번호 암호화 랜덤 값 생성
     *
     * @param  (TelewebJSON) mjsonParams
     * @return               TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "비밀번호 암호화",
        notes = "비밀번호 암호화 랜덤값을 생성한다.")
    @PostMapping("/auth-api/password-encpt/process")
    public Object createEncryptKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest objRequest = requestAttributes.getRequest();
        HttpSession mobjSession = objRequest.getSession();

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String username = mjsonParams.getString("username");
        String strEncryptKey = twbUserBizServiceImpl.createEncryptKey();

        //세션정보에 생성된 키 정보를 삽입
        mobjSession.setAttribute("ENCRYPT_KEY", strEncryptKey);

        //Redis 삭제 
        redisTemplate.delete(JWT_TOKEN_SHC256 + ":" + username);

        //Redis 저장 
        redisTemplate.opsForValue().set(JWT_TOKEN_SHC256 + ":" + username, strEncryptKey);
        redisTemplate.expire(JWT_TOKEN_SHC256 + ":" + username, 3, TimeUnit.MINUTES);					// SHA256 암호화 키값 Redis 유효시간 설정

        //테스트 용도 zIWaCJlseaXN6IP4gT8jsk6Ibz5KBzWf/QIxN8QQooAySytpGEm6uxY/bfskmqsmZhE5MgWu2YSPk4TMDn7wh5F1Ozn7qjPkOi5S8RZ2G0g=   /// ZzM0NW1yb3c5Y3E1OXh2YmczNDVtcm93
        //redisTemplate.opsForValue().set("palette:sha256enc:" + "teleweb", "ZzM0NW1yb3c5Y3E1OXh2YmczNDVtcm93");

        //최종 반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setString("ENCRYPT_KEY", 0, strEncryptKey);

        return objRetParams;
    }

}
