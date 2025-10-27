package kr.co.hkcloud.palette3.config.security.util;


import java.util.Iterator;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@Component
public class TeletalkSecurityLoggerUtils
{
    /**
     * 
     * @param  authLogJson
     * @throws TelewebUtilException
     */
    public void menuAuthenticationLog(JSONObject authLogInfoJson) throws TelewebUtilException
    {
        if(log.isDebugEnabled()) {
            String strKeyName, strValue, print;
            StringBuffer sbLog = new StringBuffer("\n");
            sbLog.append("---------------------------------------------\n");
            sbLog.append("MENU AUTHENTICATION INFORMATION\n");
            sbLog.append("---------------------------------------------\n");

            @SuppressWarnings("unchecked")
            Iterator<String> objIterator = authLogInfoJson.keys();
            while(objIterator.hasNext()) {
                strKeyName = objIterator.next();
                strValue = authLogInfoJson.getString(strKeyName);
                print = String.format("%-25s :%s", strKeyName, strValue);
                sbLog.append("").append(print).append("\n");
            }
            log.debug("{}", sbLog.toString());
        }
    }


    /**
     * 
     * @param  stopWatch
     * @throws TelewebUtilException
     */
    public void menuAuthenticationLog(StopWatch stopWatch) throws TelewebUtilException
    {
        stopWatch.stop();
        if(log.isInfoEnabled()) {
            log.info("\n{}", stopWatch.prettyPrint());
        }
    }
}
