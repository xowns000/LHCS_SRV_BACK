package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnReferenceEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.util.TeletalkReceiveUtils;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * 카카오톡 상담톡 사용자 메타 정보 수신(path: /reference)
 * 가이드 문서 : https://bzm-center.kakao.com/guide/document/bizchat
 *  
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvKakaobzcReceiveReference")
public class HkcdvKakaobzcReceiveReferenceImpl implements HkcdvKakaobzcReceiveReference
{
    private static final String                         CALLED_API = "/reference";
    private final InnbCreatCmmnService                  innbCreatCmmnService;
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final TalkBusyService                       talkBusyService;
    private final TransToKakaoService                   transToKakaoService;
    private final TalkRedisChatUtils                    redisChatUtils;
    private final TalkDataProcessService                talkDataProcessService;
    private final TeletalkReceiveUtils                  teletalkReceiveUtils;
    private final PaletteProperties                     paletteProperties;
    
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";


    /**
     * 카카오 상담톡 레퍼런스 이벤트 수신
     * @param   KakaobzcOnReferenceEvent referenceEvent
     * @throws  Exception
     * @version                          5.0
     */
    @EventListener
    public void onReference(final KakaobzcOnReferenceEvent referenceEvent) throws TelewebAppException
    {
        JSONObject referenceJson = referenceEvent.getReferenceJson();
        
        String logPrefix = logDevider + ".onReference" + "___" + referenceJson.get("user_key") + "___KAKAO___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 레퍼런스 이벤트(사용자 메타 정보) 수신 start");
        log.info(logPrefix + (logNum++) + " ::: onReference - {} ::: ", referenceJson.toString());
        
        //onMessage에 있던 sleep를 onReference로 옮김. message가 먼저 오며, message은 실제 메세지를 주고 받아서 sleep 걸기 안맞아 보임. by hjh.
        try {
            Thread.sleep(300);
            log.info(logPrefix + (logNum++) + " ::: Thread.sleep(300)");
        }
        catch(InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        
        String partnerId = paletteProperties.getPartnerId();
        log.info(logPrefix + (logNum++) + " ::: partnerId ::: " + partnerId);
        if(StringUtils.isEmpty(partnerId)) {
            log.error("teletalk.properties partnerId is empty ::: {}", partnerId);
            throw new TelewebAppException("teletalk.properties partnerId is empty");
        }

        String userKey = referenceJson.getString("user_key");
        String orgUserKey = referenceJson.getString("org_user_key");
        String senderKey = referenceJson.getString("asp_sndrKey");
        String callTypCd = referenceJson.getString("call_typ_cd");
        String custcoId = referenceJson.getString("custco_id");

        //=========================================================
        //= 고객정보 ==============================================
        //=========================================================
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(userKey);
        customerVO.setTalkUserKey(userKey);
        customerVO.setCustcoId(custcoId);
        customerVO.setSndrKey(senderKey);
        customerVO.setChnClsfCd(callTypCd);
        log.info(logPrefix + (logNum++) + " ::: 고객기본정보 DB에 삽입 - call before settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO) ::: "
            + "\ncustomerVO.getCustomerId :::  " + customerVO.getCustomerId()
            + "\ncustomerVO.getTalkUserKey :::  " + customerVO.getTalkUserKey()
            + "\ncustomerVO.getCustcoId :::  " + customerVO.getCustcoId()
            + "\ncustomerVO.getSndrKey :::  " + customerVO.getSndrKey()
            + "\ncustomerVO.getChnClsfCd :::  " + customerVO.getChnClsfCd()
        );
        //고객기본정보 DB에 삽입
        settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);

        referenceJson.put("CUSTCO_ID", custcoId);
        referenceJson.put("SNDR_KEY", senderKey);

        // 사용하는 곳이 없어 주석 처리 - by hjh.
//        //고객의 상담 가능상태를 체크한다.
//        TelewebJSON jsonParams = new TelewebJSON();
//        jsonParams.setString("TALK_USER_KEY", userKey);
//        jsonParams.setString("CHT_USER_KEY", userKey);
//        jsonParams.setString("ORG_TALK_USER_KEY", orgUserKey);
//        jsonParams.setString("SNDR_KEY", senderKey);
//        jsonParams.setString("CALL_TYPE_CD", callTypCd);
//        jsonParams.setString("CUSTCO_ID", custcoId);

//        if (!checkIsValidConsultService.isValid(jsonParams))
//        {
//        	transToKakaoService.endWithBot(userKey, senderKey, "noConsult");	//"상담톡 연결불가"블록으로 이동, main이라는 값은 오픈빌더에서 블록에 이벤트 설정해야 함
//        	return;
//        }

//		String mode = env.getString("realDevMode");

        //path : /reference
        //사용자가 전송한 메시지 상세
        //"reference":{"extra":"고객사에서 관리되는 커스텀한 메타 정보가 전달"} 
        //"reference":{"extra":"", "lastText":"채팅방에서 상담톡으로 전환 시 버튼으로부터 전달된 메타정보", "lastTextDate":"메타정보가 생성된 최신 시각"}}
        // (메타정보는 고객사가 커스텀)
        JSONObject first = referenceJson.getJSONObject("reference");
        log.info(logPrefix + (logNum++) + " ::: first ::: " + first);

        // 카카오 정책 변경으로 파라미터명 text -> extra 로 변경 2019.08.12 LSM
        String content = new String();

        JSONObject contentParam = new JSONObject();
        boolean isUseGetInqry = false;
        String routerTarget = new String();
        boolean noCheckBusy = false;

        JSONObject extraJson = null;
        if(referenceEvent.getExtraJson() != null && !referenceEvent.getExtraJson().isEmpty()) {
            extraJson = teletalkReceiveUtils.postExtraParamMapping(referenceEvent.getExtraJson());	// post ( array ) 처리는 별도 0 번째만 
        }
        else {
            extraJson = teletalkReceiveUtils.getExtraParamMapping(first);							// get ( string ) 
        }

        log.info(logPrefix + (logNum++) + " ::: extraJson.toString() ::: " + extraJson.toString());

        // 지정상담사 
        if(extraJson != null && extraJson.containsKey("ROUTERTARGET")) {
            log.info(logPrefix + (logNum++) + " ::: 지정상담사 - extraJson.containsKey(\"ROUTERTARGET\") ::: " + extraJson.containsKey("ROUTERTARGET"));
            routerTarget = (String) extraJson.get("ROUTERTARGET");
            noCheckBusy = true;
        }
        // 선택된 문의유형 들어올때  
        else if(extraJson != null && extraJson.containsKey("INQRY")) {
            log.info(logPrefix + (logNum++) + " ::: 선택된 문의유형 들어올때 - extraJson.containsKey(\"INQRY\") ::: " + extraJson.containsKey("INQRY"));
            isUseGetInqry = true;
            contentParam.put("INQRY", (String) extraJson.get("INQRY"));
        }
        else { // 그외 파라미터 
            content = (String) extraJson.get("content");
            log.info(logPrefix + (logNum++) + " ::: 그외 파라미터 content ::: " + content);
        }

        String orderNumber = null;
        Integer custSeq = 0;
        String memberYn = null;

        //채팅이 가능하지 않은 상태인 지 체크한다 ( 지정상담사로 들어온 경우 busy 영향 받지 않는다. 무조건 통과 )
        //상담사가 채팅이 가능한 상태인지 체크함(점심시간, 상담 미운영 시간 등)
        log.info(logPrefix + (logNum++) + " ::: 채팅이 가능하지 않은 상태인 지 체크한다 ( 지정상담사로 들어온 경우 busy 영향 받지 않는다. 무조건 통과 ) ::: ");
        if(!noCheckBusy && talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { 
            log.info(logPrefix + (logNum++) + " ::: !noCheckBusy && talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true) - true ::: ");
            return; 
        } else {
             log.info(logPrefix + (logNum++) + " ::: !noCheckBusy && talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true) - false ::: ");
        }

        TelewebJSON objParams = new TelewebJSON();
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("CHT_USER_KEY", userKey);
        objParams.setString("CALL_TYP_CD", callTypCd);
        objParams.setString("CNH_TYP_CD", callTypCd);

        TelewebJSON telewebJSON = new TelewebJSON();
        telewebJSON.setHeader("called_api", CALLED_API);
        telewebJSON.setHeader("code", 0);
        telewebJSON.setHeader("ERROR_FLAG", false);
        telewebJSON.setHeader("ERROR_MSG", "");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, referenceJson);
        telewebJSON.setDataObject(jsonArray);

        //고객문의유형 사용여부
        log.info(logPrefix + (logNum++) + " ::: 고객문의유형 사용여부 ::: ");
        referenceJson.put("INQRY_STATUS", "BEGIN");
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
        boolean isUseInqry = "Y".equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(referenceJson).getSize() > 0;
        log.info(logPrefix + (logNum++) + " ::: inqryTypeYn ::: " + inqryTypeYn);
        log.info(logPrefix + (logNum++) + " ::: isUseInqry ::: " + isUseInqry);

        // 지정상담사 인입시 문의유형 사용유무 
        String fixedRouteInqryUseYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "FIXED_ROUTE_INQRY_USE_YN");
        log.info(logPrefix + (logNum++) + " ::: fixedRouteInqryUseYn ::: " + fixedRouteInqryUseYn);
        if(routerTarget != null && !routerTarget.equals("")) {
            if("Y".equals(fixedRouteInqryUseYn))
                isUseInqry = true;
            else
                isUseInqry = false;
        }

        //저장하기 위해 치환
        String replaceContent = content.replaceAll("\"", "\\\\\"");
        log.info(logPrefix + (logNum++) + " ::: replaceContent ::: " + replaceContent);

        TelewebJSON inJson = new TelewebJSON();
        String serial = innbCreatCmmnService.getSeqNo(new TelewebJSON(), partnerId);
        log.info(logPrefix + (logNum++) + " ::: serial ::: " + serial);
        inJson.setString("TALK_USER_KEY", userKey);
        inJson.setString("CHT_USER_KEY", userKey);
        inJson.setString("TALK_SERIAL_NUMBER", serial);
        inJson.setString("DSPTCH_PRF_KEY", senderKey);
        inJson.setString("TALK_API_CD", "/reference");
        inJson.setString("SESSION_ID", "");
        inJson.setString("TYPE", "reference");
        inJson.setString("CONTENT", replaceContent);
        inJson.setString("ORDER_NUMBER", orderNumber);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("USER_ID", "2");

        // 20180620 강명구
        // 채팅별 고객정보 요청을 하기 위하여 추가.
        inJson.setString("MEMBER_YN", memberYn);
        inJson.setInt("CUSTOMER_SEQ", custSeq);
        // 지정상담사로 인입 된경우 ( SJH ) 
        inJson.setString("DESIGNATED_USER_ID", routerTarget);

        //파라미터로 문의유형을 전달 받았을 때
        log.info(logPrefix + (logNum++) + " ::: 파라미터로 문의유형을 전달 받았을 때 ::: if(isUseInqry && isUseGetInqry) :::"
             + "\nisUseInqry ::: " + isUseInqry
             + "\nisUseGetInqry ::: " + isUseGetInqry
        );
        if(isUseInqry && isUseGetInqry) {
            log.info(logPrefix + (logNum++) + " ::: if(isUseInqry && isUseGetInqry) ::: true");
            inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
            inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
            inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
            inJson.setString("INQRY_CD", contentParam.getString("INQRY"));
        }
        //고객문의유형 사용할 때
        else if(isUseInqry) {
            log.info(logPrefix + (logNum++) + " ::: 고객문의유형 사용할 때 ::: isUseInqry ::: " + isUseInqry);
            inJson.setString("TALK_READY_CD", "QSTN_CHCING");
            inJson.setString("ALTMNT_STTS_CD", "QSTN_CHCING");
            inJson.setString("CUTT_STTS_CD", "QSTN_CHCING");
        }
        else {
            log.info(logPrefix + (logNum++) + " ::: else ::: " + isUseInqry);
            inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
            inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
            inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
        }

        
        //챗봇을 사용할때
        log.info(logPrefix + (logNum++) + " ::: transToKakaoService.chatbotYn(senderKey) ::: 챗봇사용여부 :::" + transToKakaoService.chatbotYn(senderKey));
    	if(transToKakaoService.chatbotYn(senderKey)){
            inJson.setString("CHBT_YN", "Y");
            
            try {
        		//챗봇서버로 메시지 던지기
                HttpHeaders headers = new HttpHeaders();
        		headers.add("Content-Type", "application/json");
                String dsptchPrfKey = referenceJson.getString("sender_key");
//                String chbtUrl = chatProperties.getChatbot().getSkill().getUrl().toString();
//                log.info("chbtUrl => " + chbtUrl);
        		
//        		String url = chbtUrl + "/navertalktalk/" + dsptchPrfKey;
//    			log.info("url = " + url);
                String url = "https://bzc-api.kakao.com/v2/0ee0d8c660525467124e8ce3c7464f0ad98dd0e0/chatbot/messages";
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                URI endUri = builder.build().toUri();
		        
		        log.info(logPrefix + (logNum++) + " ::: 챗봇조회api url ::: " + url);
                
                JSONObject chatbotDataParam = new JSONObject();
                chatbotDataParam.put("user_key", userKey);
                chatbotDataParam.put("sender_key", dsptchPrfKey);
                
    			ObjectMapper mapper = new ObjectMapper();
    			String paramString = mapper.writeValueAsString(chatbotDataParam);
		        
		        log.info(logPrefix + (logNum++) + " ::: 챗봇조회api 파라미터 ::: " + paramString);
		        RestTemplate restTemplate = new RestTemplate();
//		        restTemplate.getMessageConverters().add(new MapHttpMessageConverter());
		        
		        HttpEntity<?> requestEntity = new HttpEntity<>(paramString, headers);
		        //챗봇서버에서 결과코드를 안보내줌
		        //결과가 "OK"라는 문자열임
		        ResponseEntity<Map> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, Map.class);
//		        Map<String, String> result = response.getBody();
//    			String chatbotResult = mapper.writeValueAsString(result); 
		        
		        Map<String, Object> responseBody = null;
		        responseBody = response.getBody();
				
				String responseBodyToString = mapper.writeValueAsString(responseBody);
		        JSONObject responseBodyObj = JSONObject.fromObject(JSONSerializer.toJSON(responseBodyToString));
		        JSONArray chatbotMsgArr = (JSONArray) responseBodyObj.get("chatbot_messages");
                for(int i=0 ; i<chatbotMsgArr.size() ; i++){
                    JSONObject tempObj = (JSONObject) chatbotMsgArr.get(i);
                    tempObj.put("CHT_USER_KEY", userKey);
                    
                    transToKakaoService.insertKakaoChatbotConents(tempObj);
				}
		        
    		} catch (JsonProcessingException e) {
		        log.info(logPrefix + (logNum++) + " ::: 챗봇조회api error ::: " + e);
    		}
    	} else {
            inJson.setString("CHBT_YN", "N");
    	}
        inJson.setString("CALL_TYP_CD", callTypCd);

        //재배정
        Boolean blnSendSocketToAgent = redisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        log.info(logPrefix + (logNum++) + " ::: 재배정 ::: blnSendSocketToAgent ::: " + blnSendSocketToAgent);
        if(!blnSendSocketToAgent) {
            //파라미터로 문의유형을 전달 받았을 때
            if(isUseInqry && isUseGetInqry) {
                log.info(logPrefix + (logNum++) + " ::: 파라미터로 문의유형을 전달 받았을 때 ::: if(isUseInqry && isUseGetInqry) :::"
                    + "\nisUseInqry ::: " + isUseInqry
                    + "\nisUseGetInqry ::: " + isUseGetInqry
                );
                //reference는 여러번 날라올 수 있으므로 체크한다
                TelewebJSON selectReadyJson = talkDataProcessService.selectCntTalkUserReadyKey(inJson);
                int cnt = ((JSONObject) (selectReadyJson.getDataObject().get(0))).getInt("CNT");
                log.info(logPrefix + (logNum++) + " ::: reference는 여러번 날라올 수 있으므로 체크한다 ::: cnt ::: " + cnt);
                if(cnt == 0) {
                    log.info(logPrefix + (logNum++) + " ::: 배분 대기 및 상세 등록 - 채팅 상담 등록 ::: cnt ::: " + cnt);
                    // !!!!!!!!!!!!!!!배분 대기 및 상세 등록 - 채팅 상담 등록!!!!!!!!!!!!!
                    String systemMsgId = "14";
                    ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);

                    TelewebJSON outJson = talkDataProcessService.processInsertTalkReady(inJson);

                    log.info(logPrefix + (logNum++) + " ::: 배분 대기 및 상세 등록 ::: inJson ::: " + inJson);
                    log.info(logPrefix + (logNum++) + " ::: call after talkDataProcessService.processInsertTalkReady(inJson) ::: outJson ::: " + outJson);
                    int cnt2 = outJson.getInt("IS_UPDATE");
                    String cnt3 = outJson.getString("CUTT_CNT");

                    if(cnt2 == 0 && cnt3.equals("1")) {
                        // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다, 챗봇일 경우 알람 메시지를 전송하지 않는다. 수정 이건철 20180313 - 기존 로직
                        transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
                        log.info(logPrefix + (logNum++) + " ::: sytemsg ::: " + HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId));
                    }
                }
            }
            // 고객문의유형 사용할 때
            else if(isUseInqry) {
                log.info(logPrefix + (logNum++) + " ::: 고객문의유형 사용할 때 ::: isUseInqry ::: " + isUseInqry);
                String readyToTalk = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "14");
                String ReqType = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "15");
                String chatAgent = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "16");
                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();

                TelewebJSON msgInfoJson = new TelewebJSON();
                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");

                //고객문의유형 처리
                log.info(logPrefix + (logNum++) + " ::: 고객문의유형 처리 ::: call before talkDataProcessService.processInqryType(msgInfoJson, inJson, referenceJson) ::: "
                    + "\nmsgInfoJson, inJson, referenceJson ::: " + msgInfoJson
                    + "\ninJson ::: " + inJson
                    + "\nreferenceJson ::: " + referenceJson
                );
                talkDataProcessService.processInqryType(msgInfoJson, inJson, referenceJson);
            }
            else {
                log.info(logPrefix + (logNum++) + " ::: reference는 여러번 날라올 수 있으므로 체크한다 ::: ");
                //reference는 여러번 날라올 수 있으므로 체크한다
                TelewebJSON selectReadyJson = talkDataProcessService.selectCntTalkUserReadyKey(inJson);
                int cnt = ((JSONObject) (selectReadyJson.getDataObject().get(0))).getInt("CNT");
                log.info(logPrefix + (logNum++) + " ::: cnt ::: " + cnt);
                if(cnt == 0) {
                    
                    log.info(logPrefix + (logNum++) + " ::: 배분 대기 및 상세 등록 - 채팅 상담 등록 - cnt ::: " + cnt);
                    // !!!!!!!!!!!!!!!배분 대기 및 상세 등록 - 채팅 상담 등록!!!!!!!!!!!!!
                    String systemMsgId = "14";
                    ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);
                    //배분 대기 및 상세 등록 - 채팅 상담 등록
                    TelewebJSON outJson = talkDataProcessService.processInsertTalkReady(inJson);
                    int cnt2 = outJson.getInt("IS_UPDATE");
                    String cnt3 = outJson.getString("CUTT_CNT");
 
                    if(cnt2 == 0 && cnt3.equals("1")) {
                        // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다, 챗봇일 경우 알람 메시지를 전송하지 않는다. 수정 이건철 20180313 - 기존 로직
                        log.info(logPrefix + (logNum++) + " ::: 메세지가 들어왔을경우에만 대기알람메세지를 전송한다, 챗봇일 경우 알람 메시지를 전송하지 않는다. ::: "
                         + "\ncnt2 ::: "+ cnt2
                         + "\ncnt3 ::: " + cnt3);
                        log.info(logPrefix + (logNum++) + " ::: call beform transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd) ::: "
                            + "\nsenderKey ::: "+ senderKey
                            + "\nHcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId) ::: " + HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId)
                            + "\ncallTypCd ::: " + callTypCd);
                        if(systemMsgId.equals("14")) {
                        	//상담 대기메시지 발송 여부 체크
                            String custWaitMsgYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "CUST_WAIT_MSG_YN");
                            if(custWaitMsgYn.equals("Y")) {
                            	transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
                            }
                        } else {
                        	transToKakaoService.sendSystemMsg(userKey, senderKey, HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId), callTypCd);
                        }
                    }
                }
            }
        }
    }
}
