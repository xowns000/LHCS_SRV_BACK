package kr.co.hkcloud.palette3.common.twb.util;


import java.util.Iterator;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class TwbCmmnLoggerUtils
{
    /**
     * 
     * @param  keyName
     * @param  value
     * @throws TelewebUtilException
     */
    public void checkParameterLog(JSONObject infoJson) throws TelewebUtilException
    {
        if(log.isErrorEnabled()) {
            String strKeyName, strValue, print;
            StringBuffer sbLog = new StringBuffer("\n");
            sbLog.append("---------------------------------------------\n");
            sbLog.append("CHECK PARAMETER INFORMATION\n");
            sbLog.append("---------------------------------------------\n");

            @SuppressWarnings("unchecked")
            Iterator<String> objIterator = infoJson.keys();
            while(objIterator.hasNext()) {
                strKeyName = objIterator.next();
                strValue = infoJson.getString(strKeyName);
                print = String.format("%-25s :%s", strKeyName, strValue);
                sbLog.append("").append(print).append("\n");
            }
            log.error("{}", sbLog.toString());
        }
    }
}
