package kr.co.hkcloud.palette3.common.twb.app;


import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("twbBizService")
public class TwbBizServiceImpl implements TwbBizService
{
    /**
     * 서비스 클래스를 호출한다.
     * 
     * @param  req
     * @param  res
     * @param  session
     * @param  acJson
     * @return
     * @throws TelewebAppException
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON callService(HttpServletRequest req, HttpServletResponse res, HttpSession session, TelewebJSON acJson) throws TelewebAppException
    {
        String serviceId = String.format("%s%s", acJson.getHeaderString("SERVICE"), "RestController");
        String methodName = acJson.getHeaderString("METHOD");
        String serviceClass = serviceId;

        if(!serviceClass.equals("") && serviceClass != null) {
            try {
                WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(req);
                Object obj = webApplicationContext.getBean(serviceId);

                /*
                 * Method method = (obj.getClass()).getDeclaredMethod(methodName, TelewebJSON.class);
                 * 
                 * if(method!=null) { // log.debug("=====>{}", method.invoke(obj, acJson));
                 * 
                 * @SuppressWarnings("unchecked") TelewebJSON result = new TelewebJSON(((ResponseEntity<String>) method.invoke(obj, acJson)).getBody()); acJson = result;
                 * 
                 * } else { acJson.setHeader("ERROR_FLAG", true); acJson.setHeader("ERROR_MSG", String.format("%s.%s::해당 함수를 찾을 수 없습니다.", serviceId, methodName)); }
                 */

                //SE팀 패치사항 - 엑셀다운로드시  NoSuchMethodException 발생보완
                Method method;
                int tmpCheck = 0;

                try {
                    method = (obj.getClass()).getDeclaredMethod(methodName, TelewebJSON.class, BindingResult.class);
                    tmpCheck = 2;
                }
                catch(NoSuchMethodException exp) {
                    method = (obj.getClass()).getDeclaredMethod(methodName, TelewebJSON.class);
                    tmpCheck = 1;
                }

                if(method != null) {
                    if(tmpCheck == 2) {
                        //log.debug("=====>{}", method.invoke(obj, acJson, new MapBindingResult(new HashMap(), "")));
                        @SuppressWarnings("unchecked")
                        TelewebJSON result = new TelewebJSON(((ResponseEntity<String>) method.invoke(obj, acJson, new MapBindingResult(new HashMap(), ""))).getBody());
                        acJson = result;
                    }
                    else {
                        //log.debug("=====>{}", method.invoke(obj, acJson));
                        @SuppressWarnings("unchecked")
                        TelewebJSON result = new TelewebJSON(((ResponseEntity<String>) method.invoke(obj, acJson)).getBody());
                        acJson = result;
                    }
                }
                else {
                    acJson.setHeader("ERROR_FLAG", true);
                    acJson.setHeader("ERROR_MSG", String.format("%s.%s::해당 함수를 찾을 수 없습니다.", serviceId, methodName));
                }

            }
            catch(Exception ex) {
                Throwable cause = ex;
                while(cause.getCause() != null) {
                    cause = cause.getCause();
                }

                String strSessionInfo = "";
                String strRequestUserID = "";
                try {
                    strSessionInfo = (String) req.getSession().getAttribute("TWB_SESSION_INFO");
                    if(strSessionInfo != null && !"".equals(strSessionInfo)) {
                        strRequestUserID = strSessionInfo.split("\\|")[0];
                    }
                }
                catch(NullPointerException Nex) {
                    strRequestUserID = "";
                }

                acJson.setHeader("ERROR_FLAG", true);
                acJson.setHeader("ERROR_MSG", serviceId + "." + methodName + ":처리 중 오류가 발생하였습니다.\n" + cause.getMessage());

                String strLog = "\n";
                strLog += "IDENTIFIER     : " + acJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
                strLog += "USER_ID        : " + strRequestUserID + "\n";
                strLog += "CALL_TYPE      : " + "BIZ_SERVICE" + "\n";
                strLog += "SERVICE        : " + serviceId + "\n";
                strLog += "METHOD         : " + methodName + "\n";
                strLog += "ERROR_MSG      : " + "처리 중 오류가 발생하였습니다.\n" + cause.getMessage() + "\n";

                log.error("{} ::: {}", strLog, ex);
            }
        }
        else {
            acJson.setHeader("ERROR_FLAG", true);
            acJson.setHeader("ERROR_MSG", "서비스가 정의되지 않았습니다.");
        }
        return acJson;
    }

}
