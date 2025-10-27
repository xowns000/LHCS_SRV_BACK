package kr.co.hkcloud.palette3.chat.setting.util;


import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerProhibitService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class ChatSettingBannedWordUtils
{
    private final RoutingToAgentManagerProhibitService routingToAgentManager;


    /**
     * 
     * @param  objRetParams
     * @param  adminChk
     * @param  custcoId
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray parseContents(TelewebJSON objRetParams, String adminChk, @NotEmpty String custcoId) throws TelewebUtilException
    {
        JSONArray talkJsonArray = objRetParams.getDataObject("DATA");
        //String prohApplyYN = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "PROHIBITE_APPLY_YN");
        //if(prohApplyYN.equals("APPLY")) {
        //금칙어 설정 (Mehod Caching)
        TelewebJSON custcoIdJson = new TelewebJSON();
        custcoIdJson.setString("CUSTCO_ID", custcoId);
        TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords(custcoIdJson);
        JSONArray retProhibiteWords = prohibiteJson.getDataObject();
        if(retProhibiteWords.size() > 0 && talkJsonArray.size() > 0) {
            JSONObject prohJsonObj = new JSONObject();
            JSONObject talkJsonObj = new JSONObject();
            for(int i = 0; i < retProhibiteWords.size(); i++) {
                prohJsonObj = retProhibiteWords.getJSONObject(i);
                for(int j = 0; j < talkJsonArray.size(); j++) {
                    talkJsonObj = talkJsonArray.getJSONObject(j);
                    if(talkJsonObj.get("RCPTN_DSPTCH_CD").toString().contains(prohJsonObj.get("FBDWD").toString())) {
                        talkJsonObj.replace("RCPTN_DSPTCH_CD", talkJsonObj.get("RCPTN_DSPTCH_CD").toString().replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString()));
                    }
                }
            }
        }
        //}
        return talkJsonArray;
    }


    /**
     * 
     * @param  objRetParams
     * @param  adminChk
     * @param  custcoId
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray parseContentTalkTile(TelewebJSON objRetParams, String adminChk, @NotEmpty String custcoId) throws TelewebUtilException
    {
        JSONArray talkJsonArray = objRetParams.getDataObject("DATA");
        //String prohApplyYN = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "PROHIBITE_APPLY_YN");
        //if(prohApplyYN.equals("APPLY")) {
        //금칙어 설정 (Mehod Caching)
        TelewebJSON custcoIdJson = new TelewebJSON();
        custcoIdJson.setString("CUSTCO_ID", custcoId);
        TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords(custcoIdJson);
        JSONArray retProhibiteWords = prohibiteJson.getDataObject();
        log.info(">>>>" + retProhibiteWords);
        log.info(">>>>" + retProhibiteWords.size());

        if(retProhibiteWords.size() > 0 && talkJsonArray.size() > 0) {
            JSONObject prohJsonObj = new JSONObject();
            JSONObject talkJsonObj = new JSONObject();
            for(int i = 0; i < retProhibiteWords.size(); i++) {
                prohJsonObj = retProhibiteWords.getJSONObject(i);
                for(int j = 0; j < talkJsonArray.size(); j++) {
                    talkJsonObj = talkJsonArray.getJSONObject(j);
                    if(talkJsonObj.get("RCPTN_DSPTCH_MSG").toString().contains(prohJsonObj.get("FBDWD").toString())) {
                        talkJsonObj.replace("RCPTN_DSPTCH_MSG", talkJsonObj.get("RCPTN_DSPTCH_MSG").toString().replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString()));
                    }
                }
            }
        }
        //}
        return talkJsonArray;
    }


    /**
     * 
     * @param  objRetParams
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray parseContent_2(TelewebJSON objRetParams, @NotEmpty String custcoId) throws TelewebUtilException
    {
        JSONArray talkJsonArray = objRetParams.getDataObject("DATA");
        //String prohApplyYN = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "PROHIBITE_APPLY_YN");
        //if(prohApplyYN.equals("APPLY")) {
        //금칙어 설정 (Mehod Caching)
        TelewebJSON custcoIdJson = new TelewebJSON();
        custcoIdJson.setString("CUSTCO_ID", custcoId);
        TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords(custcoIdJson);
        JSONArray retProhibiteWords = prohibiteJson.getDataObject();
        if(retProhibiteWords.size() > 0) {
            if(talkJsonArray.size() > 0) {
                JSONObject talkJsonObj = new JSONObject();
                JSONObject prohJsonObj = new JSONObject();
                for(int i = 0; i < retProhibiteWords.size(); i++) {
                    prohJsonObj = retProhibiteWords.getJSONObject(i);
                    for(int j = 0; j < talkJsonArray.size(); j++) {
                        talkJsonObj = talkJsonArray.getJSONObject(j);
                        if(talkJsonObj.get("content") != null) {
                            if(talkJsonObj.get("content").toString().contains(prohJsonObj.get("FBDWD").toString())) {
                                talkJsonObj.replace("content", talkJsonObj.get("content").toString().replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString()));
                            }
                        }
                        if(talkJsonObj.get("message") != null) {
                            if(talkJsonObj.get("message").toString().contains(prohJsonObj.get("FBDWD").toString())) {
                                talkJsonObj.replace("message", talkJsonObj.get("message").toString().replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString()));
                            }
                        }
                    }
                }
            }
        }
        //}
        return talkJsonArray;
    }


    /**
     * 
     * @param  message
     * @return
     */
    public String parseContent_3(String message, @NotEmpty String custcoId) throws TelewebUtilException
    {
        //String prohApplyYN = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "PROHIBITE_APPLY_YN");
        //if(prohApplyYN.equals("APPLY")) {
        //금칙어 설정 (Mehod Caching)
        TelewebJSON custcoIdJson = new TelewebJSON();
        custcoIdJson.setString("CUSTCO_ID", custcoId);
        TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords(custcoIdJson);
        JSONArray retProhibiteWords = prohibiteJson.getDataObject();
        if(retProhibiteWords.size() > 0) {
            JSONObject prohJsonObj = new JSONObject();
            for(int i = 0; i < retProhibiteWords.size(); i++) {
                prohJsonObj = retProhibiteWords.getJSONObject(i);
                if(message.contains(prohJsonObj.get("FBDWD").toString())) {
                    message = message.replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString());
                }
            }
        }
        //}
        return message;
    }


    /**
     * 
     * @param  objRetParams
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONArray parseContents_4(TelewebJSON objRetParams, @NotEmpty String custcoId) throws TelewebUtilException
    {
        JSONArray arrayMessageword = objRetParams.getDataObject("DATA");
        //String prohApplyYN = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "PROHIBITE_APPLY_YN");
        //if(prohApplyYN.equals("APPLY")) {
        //금칙어 설정 (Mehod Caching)
        TelewebJSON custcoIdJson = new TelewebJSON();
        custcoIdJson.setString("CUSTCO_ID", custcoId);
        TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords(custcoIdJson);
        JSONArray retProhibiteWords = prohibiteJson.getDataObject();
        if(retProhibiteWords.size() > 0) {
            if(arrayMessageword.size() > 0) {
                JSONObject prohJsonObj = new JSONObject();
                JSONObject retMessageword = new JSONObject();
                for(int i = 0; i < retProhibiteWords.size(); i++) {
                    prohJsonObj = retProhibiteWords.getJSONObject(i);
                    for(int j = 0; j < arrayMessageword.size(); j++) {
                        retMessageword = arrayMessageword.getJSONObject(j);
                        if(retMessageword.get("MESSAGE").toString().contains(prohJsonObj.get("FBDWD").toString())) {
                            retMessageword.replace("MESSAGE", retMessageword.get("MESSAGE").toString().replaceAll(prohJsonObj.get("FBDWD").toString(), prohJsonObj.get("SSTTT").toString()));
                        }
                    }
                }
            }
        }
        //}
        return arrayMessageword;
    }
}
