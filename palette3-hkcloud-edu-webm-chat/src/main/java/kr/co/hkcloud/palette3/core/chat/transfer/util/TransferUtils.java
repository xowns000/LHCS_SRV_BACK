package kr.co.hkcloud.palette3.core.chat.transfer.util;


import java.net.URI;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyDAO;
import kr.co.hkcloud.palette3.core.palette.asp.app.TalkAspCustChannelService;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCustChannel;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class TransferUtils
{
    private final ChatProperties            chatProperties;
    private final TalkAspCustChannelService aspCustChannelService;
    private final PaletteFilterUtils        paletteFilterUtils;
    private final TalkBusyDAO               busyDAO;


    /**
     * kakao or 자체 메신저 url 분기
     * 
     * @return
     */
    public URI getTargetUrl(String active, String callTypCd) throws TelewebUtilException
    {
        URI url = null;
        switch(callTypCd)
        {

            case "TTALK":  // ttalk 

                if("write".equals(active)) {        //일반메세지
                    url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getChatWrite().toString());

                }
                else if("image".equals(active)) {   //이미지
                    url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getImageUpload().toString());

                }
                else if("links".equals(active)) { // 링크메세지 (일반메세지와 KAKAO path 동일하다.)
                    url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getChatEnd().toString());

                }
                else if("endtalk".equals(active)) { //상담종료
                    url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getChatEnd().toString());
                }
                else if("profile".equals(active)) { //상담종료
                    url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getProfile().toString());
                }
                else {
                    return null;
                }

                break;
            case "KAKAO":  // kakao
                if("write".equals(active) || "file".equals(active) || "audio".equals(active)) {        //일반메세지
                    url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getChatWrite().toString());

                }
                else if("image".equals(active)) {   //이미지
                    url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getImageUpload().toString());

                }
                else if("links".equals(active)) { // 링크메세지 (일반메세지와 KAKAO path 동일하다.)
                    url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getChatWrite().toString());

                }
                else if("endtalk".equals(active)) { //상담종료
                    url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getChatEnd().toString());
                }
                else if("botevent".equals(active)) { //봇연결
                    url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getChatEndWithBot().toString());
                }
                else {
                    return null;
                }
                System.out.println("~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~");
                System.out.println("url" + url);
                break;
            case "LINE":  // line
                if("write".equals(active)) {        //일반메세지
                    url = URI.create(chatProperties.getMessenger().getLine().getUrls().getBotMessagePush().toString());

                }
                else if("image".equals(active)) {   //이미지
//                    url = URI.create(env.getString("writeUrl_line"));		 image properties bot-message-content를 사용하는 것으로 수정했지만 확인 필요
                    url = URI.create(chatProperties.getMessenger().getLine().getUrls().getBotMessageContent().toString());

                }
                else if("links".equals(active)) { // 링크메세지 (일반메세지와 KAKAO path 동일하다.)
                    url = URI.create(chatProperties.getMessenger().getLine().getUrls().getBotMessagePush().toString());

                }
                else if("endtalk".equals(active)) { //상담종료
                    url = URI.create(chatProperties.getMessenger().getLine().getUrls().getExpiredSession().toString());
                }
                else {
                    return null;
                }
                break;
            case "NTT":  // navertalktalk
                // 네이버톡톡은 하나의 url에서 body 데이터의 값에 따라 유형이 달라짐 ex) 텍스트, 이미지, ...
                url = URI.create(chatProperties.getMessenger().getNavertalktalk().getBaseUrl().toString());

                if("endtalk".equals(active)) { //상담종료
                    url = URI.create(chatProperties.getMessenger().getNavertalktalk().getUrls().getExpiredSession().toString());
                }
                break;
            default:
                log.info("TransferUtils getTargetUrl ::: no target URL");
                break;
        }
        log.trace("TransferUtils getTargetUrl return url ::: {}", url);
        return url;
    }


    /**
     * kakao or 자체 메신저 url 분기
     * 
     * @return
     */
    public URI getTargetWriteUrl(String callTypCd) throws TelewebUtilException
    {
        URI url = null;
        switch(callTypCd)
        {

            case "TTALK":  // ttalk 
                url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getChatWrite().toString());
                break;
            case "KAKAO":  // kakao
                url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getChatWrite().toString());
                break;
            case "LINE":  // LINE
                url = URI.create(chatProperties.getMessenger().getLine().getUrls().getBotMessagePush().toString());
                break;
            default:
                log.info("TransferUtils getTargetWriteUrl ::: no target write URL");
                break;
        }

        log.trace("TransferUtils getTargetWriteUrl return url ::: {}", url);
        return url;
    }


    /**
     * 이미지 URL kakao or 자체 메신저 url 분기
     * 
     * @return
     */
    public URI getTargetImageUrl(String callTypCd) throws TelewebUtilException
    {
        URI url = null;
        switch(callTypCd)
        {

            case "TTALK":  // ttalk 
                url = URI.create(chatProperties.getMessenger().getTtalk().getUrls().getImageUpload().toString());
                break;
            case "KAKAO":  // kakao
                url = URI.create(chatProperties.getMessenger().getKakaotalk().getUrls().getImageUpload().toString());
                break;
            case "LINE":  // LINE
                url = URI.create(chatProperties.getMessenger().getLine().getUrls().getBotMessageContent().toString());
                break;
            default:
                log.info("TransferUtils getTargetImageUrl ::: no target image URL");
                break;
        }

        log.trace("TransferUtils getTargetImageUrl return url ::: {}", url);
        return url;
    }


    /**
     * SNDR_KEY로 DSPTCH_PRF_KEY 반환
     * 
     * @param  sndrKey
     * @return                      dsptchPrfKey
     * @throws TelewebUtilException
     */
    public JSONObject getDsptchPrfKeyByAspSenderKey(JSONObject writeData) throws TelewebUtilException
    {
        String dsptchPrfKey = "";
        String custcoId = "";
        String uuid = "";

        JSONObject resultData = new JSONObject();

        String sndrKey = writeData.getString("sndrKey");
        TwbAspCustChannel twbAspCustChannel = aspCustChannelService.findBySndrKey(sndrKey);

        dsptchPrfKey = twbAspCustChannel.getDsptchPrfKey();
//        talkAccessToken = twbAspCustChannel.getTalkAccessToken();
        custcoId = Integer.toString(twbAspCustChannel.getCustcoId());
        uuid = twbAspCustChannel.getUuid();

        log.info("TransferUtils getDsptchPrfKeyByAspSenderKey return dsptchPrfKey ::: {}", dsptchPrfKey);
        log.info("TransferUtils getDsptchPrfKeyByAspSenderKey return dsptchPrfKey ::: {}", dsptchPrfKey);

        resultData.put("sndrKey", sndrKey);
        resultData.put("dsptchPrfKey", dsptchPrfKey);
        resultData.put("talk_access_token", uuid);
        resultData.put("uuid", uuid);
        resultData.put("custco_id", custcoId);

        return resultData;
    }


    /**
     * TALK_USER_KEY에서 USER_KEY부분 가져온다. SNDR_KEY + '.' + TALK_USER_KEY 구조로 되어 있음
     * 
     * @param  talkUserKey
     * @return                      retTalkUserKey
     * @throws TelewebUtilException
     */
    public String getUserKeyByTalkUserKey(String talkUserKey) throws TelewebUtilException
    {
        String retTalkUserKey = "";

        if(talkUserKey.indexOf("_") > -1) {
            retTalkUserKey = talkUserKey.substring(talkUserKey.indexOf("_") + 1);
        }
        else {
            log.trace("talkUserKey.indexOf(_) is fault ::: {}", talkUserKey);
        }

        log.trace("TransferUtils getUserKeyByTalkUserKey return dsptchPrfKey retTalkUserKey ::: {}", talkUserKey, retTalkUserKey);
        return retTalkUserKey;
    }


    /**
     * 메신저별 전송할 데이터 포맷 생성
     * 
     * @param  talkUserKey
     * @return                      retTalkUserKey
     * @throws TelewebUtilException
     */
    public JSONObject tranToFormat(String active, JSONObject params, String callTypeCd) throws TelewebUtilException
    {
    	log.info("tranToFormat"+params);
    	log.info("active"+active);
    	log.info("callTypeCd"+callTypeCd);
        JSONObject formattedObj = params;
        switch(callTypeCd)
        {

            case "TTALK":  	// ttalk 
            case "KAKAO":  	// kakao
                formattedObj = params;
                break;
            case "LINE":  // LINE

                String writeData = "{" + "\"talk_access_token\": \"%s\"," + "\"to\": \"%s\"," + "\"CHT_CUTT_DTL_ID\": \"%s\"," + "\"CHT_USER_KEY\": \"%s\"," + "\"sndrKey\": \"%s\"," + "\"messages\":[" + "{" + "}," + "]" + "}";
                
                //LINE,NTT는 메시지 발신 시 Authorization 필요

                String sendString = String.format(writeData, params.getString("talk_access_token"), params.getString("user_key"), params.getString("CHT_CUTT_DTL_ID"), params.getString("user_key"), params.getString("sndrKey"));

                formattedObj = JSONObject.fromObject(sendString);

                // text
                if(active.equals("text")) {

                    String filter2Msg = "";
                    if(params.containsKey("message"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("message"));
                    else if(params.containsKey("CONTENT"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("CONTENT"));

                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("type", active);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("text", filter2Msg);
                }
                // links
                else if(active.equals("links")) {

                    String orgContentUrl = params.getString("image_url");	// 이미지 

                    String filter2Msg = "";
                    if(params.containsKey("message"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("message"));
                    else if(params.containsKey("CONTENT"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("CONTENT"));

                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("type", active);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("text", filter2Msg);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("originalContentUrl", orgContentUrl);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("previewImageUrl", orgContentUrl);
                }
                else {
                    // 이미지 
                    String orgContentUrl = params.getString("image_url");

                    log.info("TransferUtils tranToFormat ::: image = {}", orgContentUrl);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("type", "image");
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("originalContentUrl", orgContentUrl);
                    ((JSONArray) formattedObj.getJSONArray("messages")).getJSONObject(0).put("previewImageUrl", orgContentUrl);
                }

                break;
            case "NTT":

                if(active.equals("text")) {
                    String writeData_text = "{" + "\"event\": \"%s\"," + "\"user\": \"%s\"," + "\"call_typ_cd\": \"%s\"," + "\"send_api_token\": \"%s\"," + "\"CHT_CUTT_DTL_ID\": \"%s\"," + "\"CHT_USER_KEY\": \"%s\"," + "\"sndrKey\": \"%s\"," + "\"textContent\":" + "{" + "}" + "}";
                    
                    String sendString_text = String.format(writeData_text, "send", params.getString("user_key"), params.getString("call_typ_cd"), params.getString("talk_access_token"), params.getString("CHT_CUTT_DTL_ID"), params.getString("user_key"), params.getString("sndrKey"));

                    formattedObj = JSONObject.fromObject(sendString_text);

                    String filter2Msg = "";
                    if(params.containsKey("message"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("message"));
                    else if(params.containsKey("CONTENT"))
                        filter2Msg = paletteFilterUtils.filter5(params.getString("CONTENT"));

                    (formattedObj.getJSONObject("textContent")).put("text", filter2Msg);
                }
                else if(active.equals("composite")) {
                    String writeData_composite = "{" + "\"event\": \"%s\"," + "\"user\": \"%s\"," + "\"send_api_token\": \"%s\"," + "\"CHT_CUTT_DTL_ID\": \"%s\"," + "\"CHT_USER_KEY\": \"%s\"," + "\"sndrKey\": \"%s\"," + "\"compositeContent\":" + "{" + "}" + "}";

                    String sendString_composite = String.format(writeData_composite, "send", params.getString("user_key"), params.getString("talk_access_token"), params.getString("CHT_CUTT_DTL_ID"), params.getString("user_key"), params.getString("sndrKey"));

                    formattedObj = JSONObject.fromObject(sendString_composite);

                    (formattedObj.getJSONObject("compositeContent")).put("compositeList", params.getJSONArray("compositeList"));
                }
                else {
                    // 이미지
                    String writeData_image = "{" + "\"event\": \"%s\"," + "\"user\": \"%s\"," + "\"send_api_token\": \"%s\"," + "\"CHT_CUTT_DTL_ID\": \"%s\"," + "\"CHT_USER_KEY\": \"%s\"," + "\"sndrKey\": \"%s\"," + "\"imageContent\":" + "{" + "}" + "}";

                    String sendString_image = String.format(writeData_image, "send", params.getString("user_key"), params.getString("talk_access_token"), params.getString("CHT_CUTT_DTL_ID"), params.getString("user_key"), params.getString("sndrKey"));

                    formattedObj = JSONObject.fromObject(sendString_image);

                    log.info("TransferUtils tranToFormat ::: image = {}", params.getString("image"));
                    (formattedObj.getJSONObject("imageContent")).put("imageUrl", params.getString("image"));
                }

                break;
            default:
                log.info("TransferUtils getSendString ::: wrong callTypeCd");
                break;
        }

        return formattedObj;
    }


    /**
     * 업무시간 set
     * 
     * @param  talkUserKey
     * @return                      retTalkUserKey
     * @throws TelewebUtilException
     */
    public void setWorkingTime(JSONObject writeData, String custcoId) throws TelewebUtilException
    {

        String workStartTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_START_TIME");
        String workEndTime = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "WORK_END_TIME");

        writeData.put("wst", workStartTime);
        writeData.put("wet", workEndTime);

        // 4. 휴일 체크
        String holidayYn = busyDAO.selectTalkHoliday(workStartTime, workEndTime, custcoId);
        writeData.put("holidayYn", holidayYn);
    }

//    /**
//     * 공개 레포지토리로 복사
//     * 
//     * @param  talkUserKey
//     * @return                      retTalkUserKey
//     * @throws TelewebUtilException
//     */
//    public String copyPublicRepositoryPath(String imagePath, String serialNum) throws TelewebUtilException
//    {
//
//        String strRepositoryPath = paletteProperties.getRepository().getRootDir().toString();			//Repository Path
//        String strRepositoryPublicPath = chatProperties.getMessenger().getTtalk().getRepository().getPublicDir().toString();	//Repository Public Path
//
//        String targetPath = "/" + serialNum + "_" + imagePath.substring(1);
//        String orgFilePath = strRepositoryPath + imagePath;
//        String strFilePath = strRepositoryPublicPath + targetPath;
//
//        fileUtils.copyFile(orgFilePath, strFilePath);
//
//        String orgContentUrl = paletteProperties.getAsp().getSingnuoEmailActivationUrl() + "/RepositoryPublic" + targetPath;	// host url 
//
//        return orgContentUrl;
//    }


    /**
     * json 객체 string 변환시 "" 감싸도록 수정
     * 
     * @param  talkUserKey
     * @return                      retTalkUserKey
     * @throws TelewebUtilException
     */
    public String checkWellFormedJSON(JSONObject writeData) throws TelewebUtilException
    {

        String result = JSONObject.fromObject(writeData.toString()).toString();

        return result;
    }

}
