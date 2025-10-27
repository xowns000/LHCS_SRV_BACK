package kr.co.hkcloud.palette3.core.chat.stomp.app;


import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("talkStompSendToKakaoService")
public class TalkStompSendToKakaoServiceImpl implements TalkStompSendToKakaoService
{
    private final TwbComDAO                  twbComDAO;
    private final ChatProperties             chatProperties;
    private final PaletteFilterUtils         paletteFilterUtils;
    private final ChatSettingBannedWordUtils chatSettingBannedWordUtils;
    private final TransToKakaoService        transToKakaoService;


    /**
     * 카카오로 전송
     */
    @Override
    public JSONObject sendToKakao(String userKey, TelewebJSON sendJson, String callTypCd) throws TelewebAppException
    {
        JSONObject sendKakao = JSONObject.fromObject(sendJson.getDataObject().getJSONObject(0));

        log.info("[sendKakao] ::: {}", sendKakao);
        log.info("[sendKakao] ::: {}", sendJson);

        JSONObject ret = new JSONObject();
        try {
            String strType = sendJson.getString("type");
//          sendKakao.put("message", filter2Msg);
            if(strType.equals("image")) {
                String filter2Msg = paletteFilterUtils.filter2(sendKakao.getString(strType));
                filter2Msg = filter2Msg.replaceAll("/Repository", ""); //물리경로를 사용
                sendKakao.put("user_key", userKey);
                sendKakao.put(strType, filter2Msg);
                ret = transToKakaoService.transToKakao("image", sendKakao, callTypCd); //이미지 처리
            }
            else if(strType.equals("links")) {

                // 개인정보 요청 기능이 활성화일 경우에만 link 주소를 보냄.
                if("Y".equals(HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (sendJson.getDataObject().get(0))).getString("CUSTCO_ID"), "REQ_USER_INFO_YN"))) {
                    sendKakao.put("user_key", userKey);
                    //sendKakao.put("message", utils.filter2(TelewebTalkInOut.getInstance().getTalkConfig().getString("consultLinkMessage")));  //이건철 20180403 시스템 메시지로 변경
                    sendKakao.put("message", paletteFilterUtils
                        .filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(((JSONObject) (sendJson.getDataObject().get(0))).getString("CUSTCO_ID"), "26")));

                    // 추후에 link url을 변경해주도록 하자.
                    String linkUrl_Mobile = "";
                    String linkUrl_Pc = "";

                    linkUrl_Mobile = paletteFilterUtils.filter2(chatProperties.getSupport().getUrls().getMediationAddressMobile().toString());
                    linkUrl_Mobile += sendKakao.getString("TALK_USER_KEY");

                    linkUrl_Pc = paletteFilterUtils.filter2(chatProperties.getSupport().getUrls().getMediationAddressWeb().toString());
                    linkUrl_Pc += sendKakao.getString("TALK_USER_KEY");

                    linkUrl_Mobile += "&adviceUser=" + sendKakao.getString("USER_ID");;
                    linkUrl_Pc += "&adviceUser=" + sendKakao.getString("USER_ID");;

                    JSONArray linksJson = new JSONArray();
                    JSONObject linkJson = new JSONObject();
                    linkJson.put("name", "개인정보 입력");
                    linkJson.put("type", "WL");
                    linkJson.put("url_mobile", linkUrl_Mobile);
                    linkJson.put("url_pc", linkUrl_Pc);
                    linksJson.add(linkJson);

                    sendKakao.put("links", linksJson);
                    ret = transToKakaoService.transToKakao("links", sendKakao, callTypCd); //메세지 처리
                }
                else {
                    ret.put("code", -999);
                    ret.put("message", "개인정보 요청 비활성");
                }
            }
            else if(strType.equals("chatClose")) {
                sendKakao.put("user_key", userKey);
                sendKakao.put("message", sendJson.getString("message"));

                // 네이버톡톡인 경우 데이터 포멧 상이해 따로 지정
                if(callTypCd.equals("NTT")) {
                    String compositeList = "[{" + "\"description\": \"%s\"," + "\"buttonList\":[{\"type\":\"TEXT\"," + "\"data\":{\"title\":\"!종료\", \"code\": \"\"}}]}]";
                    compositeList = String.format(compositeList, sendKakao.getString("message"));

                    JSONArray jsonData = JSONArray.fromObject(compositeList);
                    sendKakao.put("compositeList", jsonData);
                    ret = transToKakaoService.transToKakao("composite", sendKakao, callTypCd); //메세지 처리
                } else {
	                JSONArray linksJson = new JSONArray();
	                JSONObject linkJson = new JSONObject();
	                linkJson.put("name", "!종료");
	                linkJson.put("type", "BK");
	                linkJson.put("extra", "!종료");
	
	                linksJson.add(linkJson);
	                sendKakao.put("links", linksJson);
	                ret = transToKakaoService.transToKakao("links", sendKakao, callTypCd); //메세지 처리
                }
            }
            else if(strType.equals("LI_UNTACT")) {
                TelewebJSON objRetParams = new TelewebJSON();
                TelewebJSON jsonParams = new TelewebJSON();

                jsonParams.setString("CUSTCO_ID", ((JSONObject) (sendJson.getDataObject().get(0))).getString("CUSTCO_ID"));
                jsonParams.setString("UNTACT_ID", ((JSONObject) (sendJson.getDataObject().get(0))).getString("UNTACT_ID"));

                objRetParams = twbComDAO.select("com.hcteletalk.teletalk.mng.untact.dao.TalkMngUntactUrlMapper", "selectUntactUrl", jsonParams);

                if(objRetParams.getHeaderInt("COUNT") > 0) {
                    // 비대면 URL 전송
                    sendKakao.put("user_key", userKey);
                    sendKakao.put("message", paletteFilterUtils.filter2(objRetParams.getString("MSG_CN")));
                    sendKakao.put("type", "LI");
                    sendKakao.put("message_type", "LI");

                    JSONArray linksJson = new JSONArray();
                    JSONObject linkJson = new JSONObject();
                    linkJson.put("name", paletteFilterUtils.filter2(objRetParams.getString("BTN_NM")));
                    linkJson.put("type", "WL");
                    linkJson.put("url_mobile", paletteFilterUtils.filter2(objRetParams.getString("URL_MOBILE")));
//                    linkJson.put("url_pc", "");
                    linksJson.add(linkJson);

                    sendKakao.put("links", linksJson);
                    ret = transToKakaoService.transToKakao("links", sendKakao, callTypCd); //메세지 처리
                }
            }
            else if(strType.equals("file")) {
            	int fileSize = Integer.parseInt(sendJson.getString("file_size"));
                sendKakao.put("user_key", userKey);
                sendKakao.put("file_size", fileSize);
                ret = transToKakaoService.transToKakao("file", sendKakao, callTypCd); //이미지 처리
            }
            else if(strType.equals("audio")) {
                sendKakao.put("user_key", userKey);
                ret = transToKakaoService.transToKakao("audio", sendKakao, callTypCd); //이미지 처리
            }
            else {
                String msg = paletteFilterUtils.filter5(chatSettingBannedWordUtils.parseContent_3(sendKakao.getString("message"), ((JSONObject) (sendJson.getDataObject().get(0))).getString("CUSTCO_ID")));

                sendKakao.put("user_key", userKey);
//                sendKakao.put("message", jsonUtils.valueToStringWithoutQutoes(msg));
                sendKakao.put("message", msg);

                ret = transToKakaoService.transToKakao("write", sendKakao, callTypCd); //메세지 처리
            }

        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            ret.put("code", -999);
            ret.put("message", "상담 톡 전송 오류.");
        }
        return ret;
    }

}
