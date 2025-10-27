package kr.co.hkcloud.palette3.config.aspect.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserLogBizServiceImpl;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class AspectLoggerUtils
{
    private final ObjectMapper jsonObjectMapper;

    private final TwbUserLogBizServiceImpl twbUserLogBizServiceImpl;


    /**
     *
     * @param  proceedingJoinPoint
     * @throws TelewebUtilException
     */
    public void webControllerBeforeLog(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Optional.ofNullable(requestAttributes).ifPresent(ra -> {
                StringBuffer sbLog = new StringBuffer("\n");
                sbLog.append("---------------------------------------------\n");
                sbLog.append("SESSION INFORMATION\n");
                sbLog.append("---------------------------------------------\n");

                HttpServletRequest request = ra.getRequest();
                HttpSession session = request.getSession();

                String strKeyName, strValue, print;
                JSONObject infoJson = new JSONObject();
                infoJson.put("Uri", String.format("%s    (%s)", request.getRequestURI(), request.getMethod()));
                infoJson.put("Id", session.getId());
                infoJson.put("CreationTime", DateCmmnUtils.getTimestampToDate(session.getCreationTime()));
                infoJson.put("LastAccessedTime", DateCmmnUtils.getTimestampToDate(session.getLastAccessedTime()));
                infoJson.put("MaxInactiveInterval", session.getMaxInactiveInterval());

                @SuppressWarnings("unchecked")
                Iterator<String> objIterator = infoJson.keys();
                while(objIterator.hasNext()) {
                    strKeyName = objIterator.next();
                    strValue = infoJson.getString(strKeyName);
                    print = String.format("%-25s :%s", strKeyName, strValue);
                    sbLog.append("").append(print).append("\n");
                }
                log.debug("{}", sbLog.toString());
            });
        }
    }


    /**
     *
     * @param  stopWatch
     * @param  result
     * @throws TelewebUtilException
     */
    public void webControllerAfterLog(StopWatch stopWatch, Object result) throws TelewebUtilException
    {
        stopWatch.stop();
        if(log.isInfoEnabled()) {
            log.info("\n{}", stopWatch.prettyPrint());
        }
    }


    /**
     *
     * @param  proceedingJoinPoint
     * @throws TelewebUtilException
     */
    public void restControllerBeforeLog(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Optional.ofNullable(requestAttributes).ifPresent(ra -> {
                HttpServletRequest objRequest = ra.getRequest();

                String strKeyName, strValue, print;
                StringBuffer sbLog = new StringBuffer("\n");
                sbLog.append("---------------------------------------------\n");
                sbLog.append("Request Uri :").append(objRequest.getRequestURI()).append("    (").append(objRequest.getMethod()).append(")\n");
                sbLog.append("---------------------------------------------\n");
                sbLog.append("Parameter Key                    Value\n");
                sbLog.append("---------------------------------------------\n");

                for(Enumeration<?> e = objRequest.getParameterNames(); e.hasMoreElements();) {
                    strKeyName = (String) e.nextElement();
                    
                    if(strKeyName != null && !"DATA[0][PP_KEY_PP]".equals(strKeyName) && !"DATA[0][PP_ALG_PP]".equals(strKeyName) && objRequest.getParameterValues(strKeyName) != null) {
                        for(int i = 0; i < objRequest.getParameterValues(strKeyName).length; i++) {
                            strValue = objRequest.getParameterValues(strKeyName)[i];
                            print = String.format("%-30s =%s", strKeyName, strValue);
                            sbLog.append("").append(print).append("\n");
                        }
                    }
                }

                sbLog.append("---------------------------------------------\n");
                sbLog.append("Body Data\n");
                sbLog.append("---------------------------------------------\n");

                MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
                Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
                int index = -1;
                for(Annotation[] annotations : annotationMatrix) {
                    index++;
                    for(Annotation annotation : annotations) {
                        if(!(annotation instanceof RequestBody)) {
                            continue;
                        }
                        sbLog.append("[").append(index).append("] ").append(proceedingJoinPoint).append("\n");

                        String prettyPrintJSONString = "";
                        Object requestBody = proceedingJoinPoint.getArgs()[index];
                        try {
                            //JSON 문자열 변환 시도
                            prettyPrintJSONString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
                        }
                        catch(JsonProcessingException e) {
                            try {
                                Object object = jsonObjectMapper.readValue(requestBody.toString(), Object.class);
                                prettyPrintJSONString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                            }
                            catch(JsonProcessingException e1) {
                                log.warn("restControllerBeforeLog.JsonProcessingException\ne ::: {}\ne1 ::: {}\n{}", e, e1.getLocalizedMessage(), requestBody);
                            }
                        }
                        sbLog.append(prettyPrintJSONString).append("\n");
                    }
                }
                log.debug("{}", sbLog.toString());
            });
        }
    }


    /**
     *
     * @param  stopWatch
     * @param  result
     * @throws TelewebUtilException
     */
    public void restControllerAfterLog(StopWatch stopWatch, Object result) throws TelewebUtilException
    {
        stopWatch.stop();
        if(log.isInfoEnabled()) {
            log.info("\n{}", stopWatch.prettyPrint());
        }
    }


    /**
     *
     * @param  proceedingJoinPoint
     * @throws TelewebUtilException
     */
    public void serviceBeforeLog(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            ;
        }
    }


    /**
     *
     * @param  stopWatch
     * @param  result
     * @throws TelewebUtilException
     */
    public void serviceAfterLog(StopWatch stopWatch, Object result) throws TelewebUtilException
    {
        stopWatch.stop();
        if(log.isInfoEnabled()) {
            ;
        }
    }


    /**
     *
     * @param  proceedingJoinPoint
     * @throws TelewebUtilException
     */
    public void teletalkReceiverUtilsBeforeLog(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            if(log.isDebugEnabled()) {
                Object[] arguments = proceedingJoinPoint.getArgs();
                for(Object argument : arguments) {
                    if(argument instanceof JSONObject) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Object object;
                        try {
                            object = objectMapper.readValue(argument.toString(), Object.class);
                            String prettyPrintJSONString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

                            StringBuffer sbLog = new StringBuffer("\n");
                            sbLog.append("---------------------------------------------\n");
                            sbLog.append("RECEIVER INPUT INFORMATION\n");
                            sbLog.append("---------------------------------------------\n");
                            sbLog.append(prettyPrintJSONString).append("\n");
                            log.debug(sbLog.toString());
                        }
                        catch(JsonProcessingException e) {
                            throw new TelewebUtilException(e);
                        }

                    }
                }
            }
        }
    }


    /**
     *
     * @param  stopWatch
     * @param  result
     * @throws TelewebUtilException
     */
    public void teletalkReceiverUtilsAfterLog(StopWatch stopWatch, Object result) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object object;
            try {
                object = objectMapper.readValue(result.toString(), Object.class);
                String prettyPrintJSONString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

                StringBuffer sbLog = new StringBuffer("\n");
                sbLog.append("---------------------------------------------\n");
                sbLog.append("RECEIVER OUPUT INFORMATION\n");
                sbLog.append("---------------------------------------------\n");
                sbLog.append(prettyPrintJSONString).append("\n");
                log.debug(sbLog.toString());
            }
            catch(JsonProcessingException e) {
                throw new TelewebUtilException(e);
            }
        }
        stopWatch.stop();

        if(log.isInfoEnabled()) {
            log.info("\n{}", stopWatch.prettyPrint());
        }
    }


    /**
     *
     * @param  proceedingJoinPoint
     * @throws TelewebUtilException
     */
    public void telewebJsonParamBeforeLog(ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            ;
        }
    }


    /**
     *
     * @param  errorJson
     * @throws TelewebUtilException
     */
    public void telewebJsonParamErrorLog(JSONObject errorJson, Exception ex, ProceedingJoinPoint proceedingJoinPoint) throws TelewebUtilException
    {
        if(log.isErrorEnabled()) {
            String strKeyName, strValue, print;
            StringBuffer sbLog = new StringBuffer("\n");
            sbLog.append("---------------------------------------------\n");
            sbLog.append("ERROR INFORMATION\n");
            sbLog.append("---------------------------------------------\n");

            @SuppressWarnings("unchecked")
            Iterator<String> objIterator = errorJson.keys();
            while(objIterator.hasNext()) {
                strKeyName = objIterator.next();
                strValue = errorJson.getString(strKeyName);
                print = String.format("%-25s :%s", strKeyName, strValue);
                sbLog.append("").append(print).append("\n");
            }
            log.error("{}", sbLog.toString());
            log.error(ex.getMessage(), ex);

            // api parameter log
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                Optional.ofNullable(requestAttributes).ifPresent(ra -> {
                    HttpServletRequest objRequest = ra.getRequest();

                    String strKeyNameApi, strValueApi, printApi;
                    StringBuffer sbLogApi = new StringBuffer("\n");
                    sbLogApi.append("---------------------------------------------\n");
                    sbLogApi.append("ERROR INFORMATION API PARAMETER\n");
                    sbLogApi.append("---------------------------------------------\n");
                    sbLogApi.append("Parameter Key                    Value\n");
                    sbLogApi.append("---------------------------------------------\n");

                    for(Enumeration<?> e = objRequest.getParameterNames(); e.hasMoreElements();) {
                        strKeyNameApi = (String) e.nextElement();

                        if(strKeyNameApi != null && !"DATA[0][PP_KEY_PP]".equals(strKeyNameApi) && !"DATA[0][PP_ALG_PP]".equals(strKeyNameApi) && objRequest.getParameterValues(strKeyNameApi) != null) {
                            for(int i = 0; i < objRequest.getParameterValues(strKeyNameApi).length; i++) {
                                strValueApi = objRequest.getParameterValues(strKeyNameApi)[i];
                                printApi = String.format("%-30s =%s", strKeyNameApi, strValueApi);
                                sbLogApi.append("").append(printApi).append("\n");
                            }
                        }
                    }

                    sbLogApi.append("---------------------------------------------\n");
                    sbLogApi.append("Body Data\n");
                    sbLogApi.append("---------------------------------------------\n");

                    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
                    Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
                    int index = -1;
                    for(Annotation[] annotations : annotationMatrix) {
                        index++;
                        for(Annotation annotation : annotations) {
                            if(!(annotation instanceof RequestBody)) {
                                continue;
                            }
                            sbLogApi.append("[").append(index).append("] ").append(proceedingJoinPoint).append("\n");

                            String prettyPrintJSONString = "";
                            Object requestBody = proceedingJoinPoint.getArgs()[index];
                            try {
                                //JSON 문자열 변환 시도
                                prettyPrintJSONString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
                            }
                            catch(JsonProcessingException e) {
                                try {
                                    Object object = jsonObjectMapper.readValue(requestBody.toString(), Object.class);
                                    prettyPrintJSONString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                                }
                                catch(JsonProcessingException e1) {
                                    log.warn("restControllerBeforeLog.JsonProcessingException\ne ::: {}\ne1 ::: {}\n{}", e, e1.getLocalizedMessage(), requestBody);
                                }
                            }
                            sbLogApi.append(prettyPrintJSONString).append("\n");
                        }
                    }
                    log.error("{}", sbLogApi.toString());
                });
         }
    }


    /**
     *
     * @param  stopWatch
     * @param  result
     * @throws TelewebUtilException
     */
    public void telewebJsonParamAfterLog(StopWatch stopWatch, Object result) throws TelewebUtilException
    {
        stopWatch.stop();
        if(log.isInfoEnabled()) {
            ;
        }
    }
}
