package kr.co.hkcloud.palette3.config.aspect;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.twb.util.TwbCheckParameterUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectLoggerUtils;
import kr.co.hkcloud.palette3.config.aspect.util.AspectNameUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@RequiredArgsConstructor
@Aspect
//@RequiredArgsConstructor
@Component
@Order(300)
public class TelewebJsonAspect {

//    private final PaletteProperties paletteProperties;

    private final TwbCheckParameterUtils checkParameter;

    private AspectLoggerUtils aspectLoggerUtils;

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.token.reissue}")
    private String JWT_TOKEN_REISSUE;

    @Value("${jwt.token.nm}")
    private String JWT_TOKEN_NM;

    private static final String X_V3_AUTHORIZATION_HEADER = "X-V3-Authorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "bearer";

    private AspectNameUtils aspectNameUtils;

    @Autowired
    public void setAspectLoggerUtils(AspectLoggerUtils aspectLoggerUtils) {
        this.aspectLoggerUtils = aspectLoggerUtils;
    }

    @Autowired
    public void setAspectNameUtils(AspectNameUtils aspectNameUtils) {
        this.aspectNameUtils = aspectNameUtils;
    }

    /**
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution (* kr.co.hkcloud.palette3..api.*Controller.*(.., @TelewebJsonParam (*), ..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object telewebJsonParamAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        log.trace("\n===============================================================================\n<!--T E L E W E B J S O N  P A R A M  START\n{}\n\n", methodpath);

        StopWatch stopWatch = new StopWatch(methodpath);
        /////////////////////////////////////////////////////////////////////////////////
        //BEFORE
        stopWatch.start("before");
        aspectLoggerUtils.telewebJsonParamBeforeLog(proceedingJoinPoint);

        String strIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        String strRequestUserID = "";
        String message = "";
        Object result = "";

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {

            HttpServletRequest objRequest = requestAttributes.getRequest();

            try {
                //Request 객체의 파라메터 정보를 TelewebJSON으로 Convert 한다.
                TelewebJSON jsonParams = new TelewebJSON();
                TelewebJSON objJsonParams = jsonParams.cnvtTelewebJson(objRequest, true);

                objJsonParams.setHeader("TELEWEB_IDENTIFIER", strIdentifier);
                objJsonParams.setHeader("X-V3-MODE", objRequest.getHeader("X-V3-MODE"));

                /////////////////////////////////////////////////////////////////////////////////
                //Token
                //Header에 accessToken 생성

                String accessToken = "";
                String reissueToken = "";

                //Request Header에서accessToken 추출
                String xV3BearerToken = objRequest.getHeader(X_V3_AUTHORIZATION_HEADER);    //v3 api 인증토큰
                String bearerToken = objRequest.getHeader(AUTHORIZATION_HEADER);    // 로그인 인증토큰
                if( StringUtils.isEmpty(xV3BearerToken) ) {
                    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
                        accessToken = bearerToken.substring(7);
                    }
                    try {
                        if (!StringUtils.isEmpty(accessToken) && !accessToken.equals("null")) {
                            reissueToken = String.valueOf(stringRedisTemplate.opsForValue().get("palette:token:reissue:" + accessToken));
                            // 리턴 Json Header에 accessToken 생성
                            if (reissueToken != null && !reissueToken.isEmpty() && !reissueToken.equals("null")) {
                                objJsonParams.setHeader(JWT_TOKEN_NM, reissueToken);
                            } else {
                                objJsonParams.setHeader(JWT_TOKEN_NM, accessToken);
                            }
                        }
                    } catch (NullPointerException ex) {
                        objJsonParams.setHeader(JWT_TOKEN_NM, accessToken);
                    }
                }

                /*
                 * //TelewebJSON에 CUSTCO_ID 정보를 설정한다. JSONObject jsonSuper =
                 * objJsonParams.getJsonSuper(); Iterator<?> jsonkey = jsonSuper.keys();
                 * while(jsonkey.hasNext()) { String key = (String) jsonkey.next();
                 * if("HEADER".equals(key)) { continue; } JSONArray jsonarray = (JSONArray)
                 * jsonSuper.get(key); int jarrSize = jsonarray.size(); for(int i = 0; i <
                 * jarrSize; i++) { jsonarray.getJSONObject(i).put("CUSTCO_ID", custcoId);
                 * jsonarray.getJSONObject(i).put("ASP_USER_ID", strRequestUserID); } }
                 */

                stopWatch.stop();
                /////////////////////////////////////////////////////////////////////////////////
                //MAP-TO-TELEWEBJSON
                stopWatch.start("map-to-telewebjson");
                Object[] arguments = proceedingJoinPoint.getArgs();
                Object[] args = Arrays.stream(arguments).map(data -> {
                    if (data instanceof TelewebJSON) {
                        data = objJsonParams;
                    }
                    return data;
                }).toArray();

                stopWatch.stop();
                /////////////////////////////////////////////////////////////////////////////////
                //CHECK-PARAMETER
                stopWatch.start("check-parameter");
                message = checkParameter.checkParameter(objJsonParams, objRequest);
                stopWatch.stop();
                if ("".equals(message)) {
                    /////////////////////////////////////////////////////////////////////////////////
                    //PROCEED
                    stopWatch.start("proceed");
                    result = proceedingJoinPoint.proceed(args);
                    stopWatch.stop();
                    /////////////////////////////////////////////////////////////////////////////////
                    //TELEWEBJSON
                    stopWatch.start("telewebjson");
                    if (result instanceof TelewebJSON) {
                        TelewebJSON objRetJson = (TelewebJSON) result;
                        objRetJson.removeHeader("TWB_SQL_ID");
                        objRetJson.removeHeader("TWB_SQL_NAME_SPACE");

                        message = objRetJson.toString();
                    }
                    stopWatch.stop();
                }
                /////////////////////////////////////////////////////////////////////////////////
                //CONVERT-STRING
                stopWatch.start("json-to-string");
                JsonObject covertedObject = new Gson().fromJson(message, JsonObject.class);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(covertedObject);

                result = new ResponseEntity<String>(json, HttpStatus.OK);

                stopWatch.stop();
                /////////////////////////////////////////////////////////////////////////////////
                //AFTER
                stopWatch.start("after");
                aspectLoggerUtils.telewebJsonParamAfterLog(stopWatch, result);
            } catch (Exception ex) {
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }
                /////////////////////////////////////////////////////////////////////////////////
                //ERROR
                stopWatch.start("error");
                String mstrExceptMsg;

                Throwable cause = ex;
                while (cause.getCause() != null) {
                    cause = cause.getCause();
                }

                mstrExceptMsg = cause.getMessage();

                //VALIDATION 익셉션 처리
                if (ex instanceof TelewebValidationException) {
                    List<ObjectError> listObjError = ((TelewebValidationException) ex).getListObjError();
                    mstrExceptMsg = listObjError.get(0).getDefaultMessage();
                }
                JSONObject errorJson = new JSONObject();
                errorJson.put("Uri", objRequest.getRequestURI());
                errorJson.put("IDENTIFIER", strIdentifier);
                errorJson.put("USER_ID", strRequestUserID);
                errorJson.put("RESPONSE CLASS", methodpath);
                errorJson.put("ERROR MSG", mstrExceptMsg);
                aspectLoggerUtils.telewebJsonParamErrorLog(errorJson, ex, proceedingJoinPoint);

                message = "{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"" + mstrExceptMsg + "\"},\"DATA\":[{}]}";
                log.debug("message ->\n{}", message);

                result = new ResponseEntity<String>(message, HttpStatus.OK);

                stopWatch.stop();
                /////////////////////////////////////////////////////////////////////////////////
                //AFTER
                stopWatch.start("after");
                aspectLoggerUtils.telewebJsonParamAfterLog(stopWatch, result);
            }
        }
        log.trace("\n\n{}\nT E L E W E B J S O N  P A R A M  END-->\n===============================================================================", methodpath);
        return result;
    }
    /**
     *
     * @param  proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    /** 채팅 메인 Around */
    @Around("execution (* kr.co.hkcloud.palette3..api.ChatMainRestController.*(..)) && !@annotation(kr.co.hkcloud.palette3.config.aspect.NoAspectAround)")
    public Object chatAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        /** API첫번째(최초) 수신 */
        String methodpath = aspectNameUtils.getMehodPath(proceedingJoinPoint);
        String message = "";
        Object result = new Object();
        String strIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        String strRequestUserID = "";

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            HttpServletRequest objRequest = requestAttributes.getRequest();

            try {
                //PROCEED
                result = proceedingJoinPoint.proceed();

                if(result instanceof ResponseEntity) {
                    ResponseEntity<String> objRetJson = (ResponseEntity<String>) result;
                    String objRetBody = objRetJson.getBody();

                    if(objRetBody != null){
                        JSONObject jsonObject = JSONObject.fromObject(objRetBody);

                        // header가 없으면 생성
                        if (!jsonObject.containsKey("header")) {
                            jsonObject.put("header", new JSONObject());
                        }

                        JSONObject header = jsonObject.getJSONObject("header");

                        //Token
                        String accessToken  = "";
                        String reissueToken = "";

                        //Request Header에서 accessToken 추출
                        String xV3BearerToken = objRequest.getHeader(X_V3_AUTHORIZATION_HEADER);    //v3 api 인증토큰
                        String bearerToken = objRequest.getHeader(AUTHORIZATION_HEADER);    // 로그인 인증토큰
                        if( StringUtils.isEmpty(xV3BearerToken) ) {
                            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
                                accessToken = bearerToken.substring(7);
                            }
                            try {
                                if (!StringUtils.isEmpty(accessToken) && !accessToken.equals("null")) {
                                    reissueToken = String.valueOf(stringRedisTemplate.opsForValue().get("palette:token:reissue:" + accessToken));
                                    // 리턴 Json Header에 accessToken 생성
                                    if (reissueToken != null && !reissueToken.isEmpty() && !reissueToken.equals("null")) {
                                        header.put(JWT_TOKEN_NM, reissueToken);
                                    } else {
                                        header.put(JWT_TOKEN_NM, accessToken);
                                    }
                                }
                            } catch (NullPointerException ex) {
                                header.put(JWT_TOKEN_NM, accessToken);
                            }
                        }

                        result = new ResponseEntity<String>(jsonObject.toString(), objRetJson.getHeaders() ,HttpStatus.OK);
                    }
                }
            }
            catch(Exception ex) {
                String mstrExceptMsg;

                Throwable cause = ex;
                while (cause.getCause() != null) {
                    cause = cause.getCause();
                }

                mstrExceptMsg = cause.getMessage();

                //VALIDATION 익셉션 처리
                if (ex instanceof TelewebValidationException) {
                    List<ObjectError> listObjError = ((TelewebValidationException) ex).getListObjError();
                    mstrExceptMsg = listObjError.get(0).getDefaultMessage();
                }
                JSONObject errorJson = new JSONObject();
                errorJson.put("Uri", objRequest.getRequestURI());
                errorJson.put("IDENTIFIER", strIdentifier);
                errorJson.put("USER_ID", strRequestUserID);
                errorJson.put("RESPONSE CLASS", methodpath);
                errorJson.put("ERROR MSG", mstrExceptMsg);
                aspectLoggerUtils.telewebJsonParamErrorLog(errorJson, ex, proceedingJoinPoint);

                message = "{\"header\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\"" + mstrExceptMsg + "\"},\"data\":[{}]}";
                log.debug("message ->\n{}", message);

                result = new ResponseEntity<String>(message, HttpStatus.OK);
            }
        }

        /** Aspect 두번째(최종) 리턴 */
        return result;
    }
}
