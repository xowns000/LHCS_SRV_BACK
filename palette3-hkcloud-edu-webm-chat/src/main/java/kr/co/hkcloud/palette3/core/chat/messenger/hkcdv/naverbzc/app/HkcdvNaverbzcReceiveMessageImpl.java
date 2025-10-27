package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.app;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.naverbzc.domain.NaverbzcOnMessageEvent;
import kr.co.hkcloud.palette3.core.chat.messenger.util.TeletalkReceiveUtils;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;  
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


/**
 * 메시지 수신 서비스
 * 
 * @author 이동욱
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvNaverbzcReceiveMessage")
public class HkcdvNaverbzcReceiveMessageImpl implements HkcdvNaverbzcReceiveMessage
{
    private static final String CALLED_API = "/message";

    private final FileRulePropertiesUtils               fileRulePropertiesUtils;
    private final TalkDataProcessService                talkDataProcessService;
    private final TalkMsgDataProcessService             talkMsgDataProcess;
    private final TalkBusyService                       talkBusyService;
    private final TransToKakaoService                   transToKakaoService;
    private final TalkRedisChatUtils                    talkRedisChatUtils;
    private final TeletalkReceiveUtils                  teletalkReceiveUtils;
    private final PaletteJsonUtils                      paletteJsonUtils;
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final InnbCreatCmmnService      innbCreatCmmnService;
    
    private final ChatProperties            chatProperties;
    
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 메시지 이벤트 수신
     * 
     * @param   NavertalktalkbzcOnMessageEvent messageEvent
     * @throws  Exception
     * @version                                5.0
     */
    @EventListener
    public void onMessage(final NaverbzcOnMessageEvent messageEvent) throws TelewebAppException
    {
        JSONObject messageJson = messageEvent.getMessageJson();
        JSONObject chatbotMessageJson = messageEvent.getMessageJson();
        
        String logPrefix = logDevider + ".onMessage" + "___" + messageJson.get("user_key") + "___NTT___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 네이버톡톡 메시지 이벤트 수신 start");
        log.info(logPrefix + (logNum++) + " ::: onMessage - {} ::: ", messageJson.toString());
        

        messageJson.put("response_yn", "Y");
        messageJson.put("IMAGE_TALK_PATH", "");
        messageJson.put("IMAGE_URL", "");
        messageJson.put("IMG_URL", "");

        messageJson.put("VIDEO_TALK_PATH", "");
        messageJson.put("VIDEO_URL", "");
        messageJson.put("VIDEO_THUMNAIL_PATH", "");

        String userKey = messageJson.getString("user_key");
        String senderKey = messageJson.getString("sndrKey");
        String serialNumber = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        log.info(logPrefix + (logNum++) + " ::: serialNumber ::: " + serialNumber);
        String type = messageJson.getString("type");
        String callTypCd = messageJson.getString("call_typ_cd");
        String custcoId = messageJson.getString("custco_id");
        String extra = messageJson.has("extra") ? messageJson.getString("extra") : "";

        //=========================================================
        //= 고객정보 ==============================================
        //=========================================================
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(userKey);
        customerVO.setTalkUserKey(userKey);
        customerVO.setCustcoId(custcoId);
        customerVO.setChnClsfCd(callTypCd);
        customerVO.setSndrKey(senderKey);
        log.info(logPrefix + (logNum++) + " ::: call before settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO) ::: "
            + "\ncustomerVO.getCustomerId :::  " + customerVO.getCustomerId()
            + "\ncustomerVO.getTalkUserKey :::  " + customerVO.getTalkUserKey()
            + "\ncustomerVO.getCustcoId :::  " + customerVO.getCustcoId()
            + "\ncustomerVO.getSndrKey :::  " + customerVO.getSndrKey()
            + "\ncustomerVO.getChnClsfCd :::  " + customerVO.getChnClsfCd()
        );
        settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);
        log.info(logPrefix + (logNum++) + " ::: call after settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO)");
        
        messageJson.put("CUSTCO_ID", custcoId);
        messageJson.put("SNDR_KEY", senderKey);
        messageJson.put("CHT_CUTT_DTL_ID", serialNumber);
        messageJson.put("TALK_SERIAL_NUMBER", serialNumber);

        TelewebJSON objParams = new TelewebJSON();
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("CHT_USER_KEY", userKey);
        objParams.setString("TALK_SERIAL_NUMBER", serialNumber);
        objParams.setString("CHT_CUTT_DTL_ID", serialNumber);
        objParams.setString("TYPE", type);
        objParams.setString("MSG_TYPE_CD", type);
        objParams.setString("PROC_ID", "SYSTEM");
        objParams.setString("USER_ID", "SYSTEM");
        objParams.setString("IMAGE_URL", ""); // 이미지 처리 기본 설정
        objParams.setString("IMG_URL", ""); // 이미지 처리 기본 설정
        objParams.setString("IMAGE_TALK_PATH", "");

        objParams.setString("VIDEO_TALK_PATH", "");
        objParams.setString("VIDEO_URL", "");
        objParams.setString("VIDEO_THUMNAIL_PATH", "");

        objParams.setString("CALL_TYP_CD", callTypCd);
        objParams.setString("CHN_CLSF_CD", callTypCd);
        objParams.setString("SNDR_KEY", senderKey);
        objParams.setString("CUSTCO_ID", custcoId);
        
        //네이버톡톡사용 시 해당 채널이 챗봇사용여부가 y일 때
        //인입된 네이버톡톡 채팅을 챗봇서버로 넘겨준다
        boolean custChtIng = transToKakaoService.custChtIng(userKey, senderKey);
        log.info(logPrefix + (logNum++) + " ::: transToKakaoService.custChtIng(userKey, senderKey) ::: 고객 상담중인지 체크 :::" + custChtIng);
        //인입고객이 상담을 진행중인지 체크(문의유형 선택중/대기중/전달/콜백 모두 포함)
        if(custChtIng) {
        	//고객이 상담중이라면 챗봇사용여부에 관계없이 그대로 상담 진행
        } else {
        	//고객이 상담중이 아니고 챗봇을 사용할때
            log.info(logPrefix + (logNum++) + " ::: transToKakaoService.chatbotYn(senderKey) ::: 챗봇사용여부 :::" + transToKakaoService.chatbotYn(senderKey));
        	if(transToKakaoService.chatbotYn(senderKey)){
        		//네이버톡톡인입이 동시에 된 경우(메타정보를 수신한 경우)
        		//챗봇이중복으로 등록된 상태라면
        		//첫 챗봇상태에서는 영향을 끼치지 않음
        		//네이버톡톡에서는 상담원연결을 하기 전 무조건 챗봇상태가 있음
        		int chbtCnt = transToKakaoService.chbtCnt(userKey, senderKey);
        		//고객의 챗봇상담이 중복으로 등록이 되어있다면
        		//나중에 인입된 건은 삭제
        		if(chbtCnt > 1) {
        			transToKakaoService.deleteChbtDupl(userKey, senderKey);
        		}
    			TelewebJSON inJson = new TelewebJSON();
                inJson.setString("TALK_USER_KEY", userKey);
                inJson.setString("CHT_USER_KEY", userKey);
                inJson.setString("TALK_SERIAL_NUMBER", messageJson.getString("CHT_CUTT_DTL_ID"));
                inJson.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
                inJson.setString("DSPTCH_PRF_KEY", senderKey);
                inJson.setString("TALK_API_CD", "/message");
                inJson.setString("SESSION_ID", "");
                inJson.setString("TYPE", messageJson.getString("type"));
                if("product".equals(messageJson.getString("type"))) {
                	String content = messageJson.getString("content");
                	content = content.replace("\"", "\"\"");
                	log.info("======챗봇======product content======" + content);
                	
                	inJson.setString("CONTENT", content);
                	log.info("======챗봇======objParams======" + inJson);
                } else {
                	inJson.setString("CONTENT", messageJson.getString("content"));
                }
                inJson.setString("IMAGE_URL", messageJson.getString("IMAGE_URL"));
                inJson.setString("IMG_URL", messageJson.getString("IMG_URL"));
                inJson.setString("IMAGE_TALK_PATH", messageJson.getString("IMAGE_TALK_PATH"));
                inJson.setString("CUSTCO_ID", custcoId);
                inJson.setString("SNDR_KEY", senderKey);
                inJson.setString("SYS_MSG_ID", objParams.getString("SYS_MSG_ID"));
                inJson.setString("MSG_TYPE_CD", objParams.getString("MSG_TYPE_CD"));
                inJson.setString("RCPTN_DSPTCH_CD", "RCV");

                inJson.setString("CALL_TYP_CD", callTypCd);
                inJson.setString("CHN_CLSF_CD", callTypCd);
                inJson.setString("CHN_TYPE_CD", callTypCd);
                //챗봇을 통해 상담사 연결을 클릭했는지 체크
                if(messageJson.getString("event").contains("open")) {
                	//상담테이블과 대기 테이블에 챗봇상담으로 insert
                    
                    inJson.setString("TALK_READY_CD", "CHATBOT");
                    inJson.setString("CUTT_STTS_CD", "CHATBOT");
                    inJson.setString("ALTMNT_STTS_CD", "CHATBOT");

                    inJson.setString("CUTT_TYPE", "insert");
                    
                    log.info(logPrefix + (logNum++) + " ::: 챗봇채팅정보-대기테이블 INSERT 전3 :::" + inJson);
                    // 배분 대기 및 상세 등록
                    talkDataProcessService.processInsertChatbotReady(inJson);

                	try {
                		//챗봇서버로 메시지 던지기
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        String dsptchPrfKey = chatbotMessageJson.getString("sender_key");
                        String chbtUrl = chatProperties.getChatbot().getSkill().getUrl().toString();
                        log.info("chbtUrl => " + chbtUrl);
                		
                		String url = chbtUrl + "/navertalktalk/" + dsptchPrfKey;
            			log.info("url = " + url);
                        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                        URI endUri = builder.build().toUri();
                        
            			ObjectMapper mapper = new ObjectMapper();
            			String paramString = mapper.writeValueAsString(chatbotMessageJson);
            			
            			log.info("paramString = " + paramString);
        		        RestTemplate restTemplate = new RestTemplate();
//        		        restTemplate.getMessageConverters().add(new MapHttpMessageConverter());
        		        
        		        HttpEntity<?> requestEntity = new HttpEntity<>(paramString, headers);
        		        //챗봇서버에서 결과코드를 안보내줌
        		        //결과가 "OK"라는 문자열임
        		        ResponseEntity<String> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, String.class);
//        		        Map<String, String> result = response.getBody();
//            			String chatbotResult = mapper.writeValueAsString(result); 
            			
            			log.info("response = " + response);
            			
            		} catch (JsonProcessingException e) {
            			e.printStackTrace();
            		}
        			
        			//챗봇 api보내고 결과 받은 뒤 정상코드면 return 정상코드 아니면 바로 상담원 연결하기
                	return;
                } else {
    		        JSONObject responseBodyObj = JSONObject.fromObject(JSONSerializer.toJSON(messageJson.getString("textContent")));
                    log.info(logPrefix + (logNum++) + " ::: responseBodyObj 데이터 :::" + responseBodyObj);

                	if(messageJson.getString("textContent").contains("counseling")) {
                		inJson.setString("CONTENT", responseBodyObj.getString("text"));
                		objParams.setString("CONTENT", responseBodyObj.getString("text"));
            			//테이블에 고객이 상담을 하고있는지 체크
                        log.info(logPrefix + (logNum++) + " ::: transToKakaoService.custChtbot(userKey, senderKey) ::: 인입고객 챗봇 상담중 여부 :::" + transToKakaoService.custChtbot(userKey, senderKey));


                        //고객문의유형 사용여부 ( 사용여부 y and 문의유형 체크 ) 
                        messageJson.put("INQRY_STATUS", "BEGIN");
                        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
                        boolean isUseInqry = "Y".equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(messageJson).getSize() > 0;

                        // 고객문의유형 사용할 때
                        log.info(logPrefix + (logNum++) + " ::: 챗봇이후 고객문의유형 사용할 때 ::: isUseInqry - " + isUseInqry);
                        TelewebJSON outJson = new TelewebJSON(); 
                        if(isUseInqry) {
                        	//문의유형 선택중
                            log.info(logPrefix + (logNum++) + " ::: 챗봇이후 문의유형 선택중 ");
                            inJson.setString("TALK_READY_CD", "QSTN_CHCING");
                            inJson.setString("CUTT_STTS_CD", "QSTN_CHCING");
                            inJson.setString("ALTMNT_STTS_CD", "QSTN_CHCING");
                        }
                        else {
                        	//배분대기
                            log.info(logPrefix + (logNum++) + " ::: 챗봇이후 배분대기 ");
                            inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
                            inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
                            inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
                            
                            if(transToKakaoService.custChtbot(userKey, senderKey)) {
                    			//챗봇으로 들어왔다면 상담테이블과 대기테이블 update
                                inJson.setString("CUTT_TYPE", "update");
                                
                                log.info(logPrefix + (logNum++) + " ::: 챗봇채팅정보-대기테이블 INSERT 전1 :::" + inJson);
                                
                                outJson = talkDataProcessService.processInsertChatbotReady(inJson);
                    		} else {
                    			//신규로 들어왔다면 상담테이블과 대기테이블 insert
                                inJson.setString("CUTT_TYPE", "insert");
                                
                                log.info(logPrefix + (logNum++) + " ::: 챗봇채팅정보-대기테이블 INSERT 전2 :::" + inJson);
                                
                                outJson = talkDataProcessService.processInsertChatbotReady(inJson);
                    		}
                        }
                		//시스템 메시지 발송
                        //현재 채팅가능한지 -> 문의유형 사용인지 -> 시스템 메시지(상담시작메시지) 발송

                        TelewebJSON telewebJSON = new TelewebJSON();
                        telewebJSON.setHeader("called_api", CALLED_API);
                        telewebJSON.setHeader("code", 0);
                        telewebJSON.setHeader("ERROR_FLAG", false);
                        telewebJSON.setHeader("ERROR_MSG", "");
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(0, messageJson);
                        telewebJSON.setDataObject(jsonArray);

                        //상담사에게 메시지 전달 또는 재배정
                        //고객 인입 채팅 저장
                        Boolean blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
                        log.info(logPrefix + (logNum++) + " ::: blnSendSocketToAgent ::: " + blnSendSocketToAgent);

                        //채팅이 가능하지 않은 상태인 지 체크한다 . 상담원 무응답으로 인한 재배정은 다른 상태관계 없이 무조건 배정 처리함. ( SJH 20181024 ) 
                        String contactID = objParams.getString("TALK_CONTACT_ID");
//                        if (!(contactID != null && !"".equals(contactID)) && talkBusyService.isChatDisable(userKey, senderKey, callTypCd))
                        log.info(logPrefix + (logNum++) + " ::: contactID ::: " + contactID);
                        log.info(logPrefix + (logNum++) + " ::: call before if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) ::: "
                            + "\nuserKey ::: " + userKey
                            + "\nsenderKey ::: " + senderKey
                            + "\ncallTypCd ::: " + callTypCd
                            + "\ncustcoId ::: " + custcoId
                            + "\nfalse"
                        );

                        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { 
                            log.info(logPrefix + (logNum++) + " ::: isChatDisable isShow :: ");
                            log.info(logPrefix + (logNum++) + " ::: isChatDisable userKey :: " + userKey);
                            log.info(logPrefix + (logNum++) + " ::: isChatDisable senderKey :: " + senderKey);
                            log.info(logPrefix + (logNum++) + " ::: isChatDisable callTypCd :: " + callTypCd);
                        	return; 
                        }
                        
                        //상담사에게 메시지 전달
                        log.info(logPrefix + (logNum++) + " ::: 상담사에게 메시지 전달 inJson ::: " + inJson);
                        log.info(logPrefix + (logNum++) + " ::: before call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: "
                            + "\nCALLED_API ::: " + CALLED_API
                            + "\nuserKey ::: " + userKey
                            + "\ntelewebJSON ::: " + telewebJSON
                            + "\nobjParams ::: " + objParams
                        );

                        log.info(logPrefix + (logNum++) + " ::: after call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: " + blnSendSocketToAgent);

                        // 기존의 상담원이 비정상 종료 되었으므로 , 신규배분 되나 고객문의 유형 사용 안함. 2018.10.10 SJH
                        String chtCuttId = objParams.getString("CHT_CUTT_ID");

                            // 고객문의유형 사용할 때  && (chtCuttId == null || "".equals(contactID))
                            log.info(logPrefix + (logNum++) + " ::: 챗봇 문의유형 사용 여부 ::: " + isUseInqry);
                            if(isUseInqry) {
                                String readyToTalk = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "14");
                                String ReqType = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "15");
                                String chatAgent = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "16");
                                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();

                                TelewebJSON msgInfoJson = new TelewebJSON();
                                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
                                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
                                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");

                                log.info(logPrefix + (logNum++) + " ::: true 고객문의유형 처리 ::: " + msgInfoJson);
                                log.info(logPrefix + (logNum++) + " ::: call before talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: "
                                    + "\nmsgInfoJson ::: " + msgInfoJson
                                    + "\ninJson ::: " + inJson
                                    + "\nmessageJson ::: " + messageJson
                                );
                                
                                //고객문의유형 처리
                                inJson.setString("CUTT_TYPE", "update");
                                talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson);
                                log.info(logPrefix + (logNum++) + " ::: call after talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: ");
                            }
                            else {
                                log.info("===>" + messageJson.get("user_key") + "::onMessage - false 고객문의유형 처리");
                                String systemMsgId = "14";

                                log.info(logPrefix + (logNum++) + " ::: call after talkMsgDataProcess.insertSndMsg(inJson) ::: ");
                                ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);

                                // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다
                                log.info(logPrefix + (logNum++) + " ::: call before transToKakaoService.sendSystemMsg  ::: "
                                    + "\ncustcoId ::: " + custcoId
                                    + "\nsystemMsgId ::: " + systemMsgId
                                    + "\nHcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId) ::: " + HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId)
                                    + "\ncallTypCd ::: " + callTypCd
                                );

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
                        return;
            		} else {
            			//상담테이블과 대기 테이블에 챗봇상담으로 insert
                        
                        inJson.setString("TALK_READY_CD", "CHATBOT");
                        inJson.setString("CUTT_STTS_CD", "CHATBOT");
                        inJson.setString("ALTMNT_STTS_CD", "CHATBOT");

                        inJson.setString("CUTT_TYPE", "insert");
                        
                        log.info(logPrefix + (logNum++) + " ::: 챗봇채팅정보-대기테이블 INSERT 전3 :::" + inJson);
                        // 배분 대기 및 상세 등록
                        talkDataProcessService.processInsertChatbotReady(inJson);

                    	try {
                    		//챗봇서버로 메시지 던지기
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            String dsptchPrfKey = chatbotMessageJson.getString("sender_key");
                            String chbtUrl = chatProperties.getChatbot().getSkill().getUrl().toString();
                            log.info("chbtUrl => " + chbtUrl);
                    		
                    		String url = chbtUrl + "/navertalktalk/" + dsptchPrfKey;
                			log.info("url = " + url);
                            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                            URI endUri = builder.build().toUri();
                            
                			ObjectMapper mapper = new ObjectMapper();
                			String paramString = mapper.writeValueAsString(chatbotMessageJson);
                			
                			log.info("paramString = " + paramString);
            		        RestTemplate restTemplate = new RestTemplate();
//            		        restTemplate.getMessageConverters().add(new MapHttpMessageConverter());
            		        
            		        HttpEntity<?> requestEntity = new HttpEntity<>(paramString, headers);
            		        //챗봇서버에서 결과코드를 안보내줌
            		        //결과가 "OK"라는 문자열임
            		        ResponseEntity<String> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, String.class);
//            		        Map<String, String> result = response.getBody();
//                			String chatbotResult = mapper.writeValueAsString(result); 
                			
                			log.info("response = " + response);
                			
                		} catch (JsonProcessingException e) {
                			e.printStackTrace();
                		}
            			
            			//챗봇 api보내고 결과 받은 뒤 정상코드면 return 정상코드 아니면 바로 상담원 연결하기
                    	return;
            		}
                }
        	}
        }

        // 챗봇연결 문의유형 선택 - 종료처리
        if(extra.startsWith("ENDWITHBOT_")) {
            transToKakaoService.endWithBot(userKey, senderKey, extra.replaceAll("ENDWITHBOT_", ""));
            return;
        }

        // 지원 가능/불가능 타입에 따라 return 처리함.
        if(teletalkReceiveUtils.isAvailableType(type, messageJson) == false) {
            teletalkReceiveUtils.noSendSystemMsg(type, objParams, messageJson, callTypCd);
            return;
        }
        RepositoryTrgtTypeCd targetType = null;
        log.info(logPrefix + (logNum++) + " ::: type ::: " + type);
        if("photo".equals(type)) {
            String imageTalkUrl = ((JSONObject) messageJson.get("imageContent")).getString("imageUrl");
            //imageTalkUrl = imageTalkUrl.replaceAll("https://shop-phinf.pstatic.net/", "https://lottechilsung.lottecrm.co.kr/naverimg/");
            messageJson.put("content", imageTalkUrl);
            messageJson.put("contents", imageTalkUrl);
            messageJson.put("IMAGE_URL", imageTalkUrl);
            messageJson.put("IMG_URL", imageTalkUrl);

            objParams.setString("CONTENT", imageTalkUrl);
            objParams.setString("CONTENTS", imageTalkUrl);
            objParams.setString("IMAGE_URL", imageTalkUrl);
            objParams.setString("IMG_URL", imageTalkUrl);

            log.info("nave photo >>> objParams : " + imageTalkUrl);
            log.info("nave photo >>> objParams : " + objParams);

            //이미지 팔레트서버 보관x
            //기존 서버에 있는 이미지 보여주는 로직에서 각 채널에서 받아오는 도메인 이미지 보여주므로 저장 필요 없음
            //이미지 제한 시스템 메시지도 발송 필요 없음
//            // [파일o] 메시지 수신(카카오톡): 채팅-이미지(고객)
//            final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
//            final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
//            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
//            log.debug("fileProperties>>>{}", fileProperties);
//
//            targetType = fileProperties.getTrgtTypeCd();
//            fileProperties.setCustcoId(custcoId);
//
//            //이미지 레파지토리 저장 
//            final JSONObject jsonFile = teletalkReceiveUtils.savePhototoRepository(messageJson, objParams, callTypCd, fileProperties);
//            messageJson.put("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
//            messageJson.put("FILE_KEY", jsonFile.getString("FILE_KEY"));
//            objParams.setString("FILE_GROUP_KEY", jsonFile.getString("FILE_GROUP_KEY"));
//            objParams.setString("FILE_KEY", jsonFile.getString("FILE_KEY"));

//            // 파일 BLOB 처리이면
//            switch(targetType)
//            {
//                case DB:
//                {
//                    //이미지 BLOB 저장 
//                    teletalkReceiveUtils.savePhototoBlob(messageJson, objParams, callTypCd, fileProperties);
//                    break;
//                }
//                default:
//                {
//                    //이미지 레파지토리 저장 
//                    teletalkReceiveUtils.savePhototoRepository(messageJson, objParams, callTypCd, fileProperties);
//                    break;
//                }
//            }
        }
        else if("video".equals(type)) {
            //지원 안함 - 20210615
        } 
        else if("product".equals(type)) {
        	String content = messageJson.getString("content");
        	content = content.replace("\"", "\"\"");
        	log.info("======챗봇x======product content======" + content);

        	//인입상담인 채팅에서 사용하는 파라미터
        	messageJson.put("content", content);
        	log.info("======챗봇x======objParams======" + messageJson);
        	//상담중인 채팅에서 사용하는 파라미터
        	objParams.setString("CONTENT", content);
        }
        else {
        	//type = "text"
            String content = messageJson.getString("content");
            //상담중일때 open이벤트를 받았을 경우 
            //type = "text"
            //content = ""
            //만약 open이벤트가 아니더라도 type="text"인데 content="" 라면 해당 채팅을 따로 처리하지 않음
        	if(content.equals("")) {
        		return;
        	}
            objParams.setString("CONTENT", paletteJsonUtils.valueToStringWithoutQutoes(content));
        }

        //장문 (1000 자이상)
//        if(objParams.getString("CONTENT").length() > 1000) {
//            OrgContentVO orgContentVO = new OrgContentVO();
//            orgContentVO.setCustcoId(custcoId);
//            teletalkReceiveUtils.insertOrgContentWithoutUrl(messageJson, objParams, orgContentVO);
//        }

        TelewebJSON telewebJSON = new TelewebJSON();
        telewebJSON.setHeader("called_api", CALLED_API);
        telewebJSON.setHeader("code", 0);
        telewebJSON.setHeader("ERROR_FLAG", false);
        telewebJSON.setHeader("ERROR_MSG", "");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, messageJson);
        telewebJSON.setDataObject(jsonArray);

        //상담사에게 메시지 전달 또는 재배정
        Boolean blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        if(blnSendSocketToAgent) { return; }

        //채팅이 가능하지 않은 상태인 지 체크한다 . 상담원 무응답으로 인한 재배정은 다른 상태관계 없이 무조건 배정 처리함. ( SJH 20181024 ) 
        String contactID = objParams.getString("TALK_CONTACT_ID");
//        if (!(contactID != null && !"".equals(contactID)) && talkBusyService.isChatDisable(userKey, senderKey, callTypCd))
        log.info(logPrefix + (logNum++) + " ::: contactID ::: " + contactID);
        log.info(logPrefix + (logNum++) + " ::: call before if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, false)) ::: "
            + "\nuserKey ::: " + userKey
            + "\nsenderKey ::: " + senderKey
            + "\ncallTypCd ::: " + callTypCd
            + "\ncustcoId ::: " + custcoId
            + "\nfalse"
        );

        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { 
            log.info(logPrefix + (logNum++) + " ::: isChatDisable isShow :: ");
            log.info(logPrefix + (logNum++) + " ::: isChatDisable userKey :: " + userKey);
            log.info(logPrefix + (logNum++) + " ::: isChatDisable senderKey :: " + senderKey);
            log.info(logPrefix + (logNum++) + " ::: isChatDisable callTypCd :: " + callTypCd);
        	return; 
        }
        
        // 배분 대기 등록
        TelewebJSON inJson = new TelewebJSON();
        inJson.setString("TALK_USER_KEY", userKey);
        inJson.setString("CHT_USER_KEY", userKey);
        inJson.setString("TALK_SERIAL_NUMBER", messageJson.getString("CHT_CUTT_DTL_ID"));
        inJson.setString("CHT_CUTT_DTL_ID", messageJson.getString("CHT_CUTT_DTL_ID"));
        inJson.setString("DSPTCH_PRF_KEY", senderKey);
        inJson.setString("TALK_API_CD", "/message");
        inJson.setString("SESSION_ID", "");
        inJson.setString("TYPE", messageJson.getString("type"));
        inJson.setString("CONTENT", messageJson.getString("content"));
        inJson.setString("IMAGE_URL", messageJson.getString("IMAGE_URL"));
        inJson.setString("IMG_URL", messageJson.getString("IMG_URL"));
        inJson.setString("IMAGE_TALK_PATH", messageJson.getString("IMAGE_TALK_PATH"));
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("SNDR_KEY", senderKey);
        inJson.setString("SYS_MSG_ID", objParams.getString("SYS_MSG_ID"));
        inJson.setString("MSG_TYPE_CD", objParams.getString("MSG_TYPE_CD"));
        inJson.setString("RCPTN_DSPTCH_CD", "RCV");

        //2018.12.26 KMG 동영상 정보 세팅
        inJson.setString("VIDEO_TALK_PATH", messageJson.getString("VIDEO_TALK_PATH"));
        inJson.setString("VIDEO_URL", messageJson.getString("VIDEO_URL"));
        inJson.setString("VIDEO_THUMNAIL_PATH", messageJson.getString("VIDEO_THUMNAIL_PATH"));

        if(messageJson.has("attachment")) {
            inJson.setString("LINKS", ((JSONObject) messageJson.get("attachment")).getString("url"));
            inJson.setString("ORG_CONT_ID", objParams.getString("ORG_CONT_ID"));
        }
        else {
            inJson.setString("LINKS", "");
            inJson.setString("ORG_CONT_ID", "");
        }

        if("photo".equals(type)) {
        	//파일을 따로 저장하지 않음
            inJson.setString("FILE_GROUP_KEY", "");
            inJson.setString("FILE_KEY", "");
        }

        //고객문의유형 사용여부 ( 사용여부 y and 문의유형 체크 ) 
        messageJson.put("INQRY_STATUS", "BEGIN");
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
        boolean isUseInqry = "Y".equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(messageJson).getSize() > 0;

        // 고객문의유형 사용할 때
        log.info(logPrefix + (logNum++) + " ::: 고객문의유형 사용할 때 ::: isUseInqry - " + isUseInqry);
        if(isUseInqry) {
        	//문의유형 선택중
            log.info(logPrefix + (logNum++) + " ::: 문의유형 선택중 ");
            inJson.setString("TALK_READY_CD", "QSTN_CHCING");
            inJson.setString("CUTT_STTS_CD", "QSTN_CHCING");
            inJson.setString("ALTMNT_STTS_CD", "QSTN_CHCING");
        }
        else {
        	//배분대기
            log.info(logPrefix + (logNum++) + " ::: 배분대기 ");
            inJson.setString("TALK_READY_CD", "ALTMNT_WAIT");
            inJson.setString("CUTT_STTS_CD", "ALTMNT_WAIT");
            inJson.setString("ALTMNT_STTS_CD", "ALTMNT_WAIT");
        }
        inJson.setString("CHATBOT_YN", "N");
        inJson.setString("CALL_TYP_CD", callTypCd);
        inJson.setString("CHN_CLSF_CD", callTypCd);
        inJson.setString("CHN_TYPE_CD", callTypCd);

        // 파일 BLOB 처리이면
        if(targetType != null && targetType == RepositoryTrgtTypeCd.DB) {
            //이미지 BLOB 저장 
            inJson.setString("ORG_FILE_ID", objParams.getString("ORG_FILE_ID"));
        }

        //상담사에게 메시지 전달
        log.info(logPrefix + (logNum++) + " ::: 상담사에게 메시지 전달 inJson ::: " + inJson);
        log.info(logPrefix + (logNum++) + " ::: before call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: "
            + "\nCALLED_API ::: " + CALLED_API
            + "\nuserKey ::: " + userKey
            + "\ntelewebJSON ::: " + telewebJSON
            + "\nobjParams ::: " + objParams
        );
        blnSendSocketToAgent = talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);

        log.info(logPrefix + (logNum++) + " ::: after call talkRedisChatUtils.isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams) ::: " + blnSendSocketToAgent);

        // 기존의 상담원이 비정상 종료 되었으므로 , 신규배분 되나 고객문의 유형 사용 안함. 2018.10.10 SJH
        //contactID = objParams.getString("TALK_CONTACT_ID");
        contactID = objParams.getString("CHT_CUTT_ID");

        if(!blnSendSocketToAgent) {
            // 고객문의유형 사용할 때  && (contactID == null || "".equals(contactID))
            if(isUseInqry && (contactID == null || "".equals(contactID))) {
                String readyToTalk = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "14");
                String ReqType = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "15");
                String chatAgent = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "16");
                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();

                TelewebJSON msgInfoJson = new TelewebJSON();
                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");

                log.info(logPrefix + (logNum++) + " ::: true 고객문의유형 처리 ::: " + msgInfoJson);
                log.info(logPrefix + (logNum++) + " ::: call before talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: "
                    + "\nmsgInfoJson ::: " + msgInfoJson
                    + "\ninJson ::: " + inJson
                    + "\nmessageJson ::: " + messageJson
                );
                
                //고객문의유형 처리
                talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson);
                log.info(logPrefix + (logNum++) + " ::: call after talkDataProcessService.processInqryType(msgInfoJson, inJson, messageJson) ::: ");
            }
            else {
                log.info("===>" + messageJson.get("user_key") + "::onMessage - false 고객문의유형 처리");
                String systemMsgId = "14";
                // 이전 채팅이 비정상 종료 되어 이전 채팅의 후처리가 필요한 경우 2018.10.10 SJH
                if(contactID != null && !"".equals(contactID)) {
                    //22 비정상 종료 코드 업데이트
                    log.info(logPrefix + (logNum++) + " ::: 비정상 종료 코드 업데이트 ::: ");
                    talkMsgDataProcess.updateTalkContactStatusCd(objParams); //22 비정상 종료 코드 업데이트

                    inJson.setString("TALK_READY_CD", "10");
                    inJson.setString("INQRY_CD", objParams.getString("TALK_INQRY_CD"));
                    inJson.setString("TALK_CONTACT_ID", contactID);

                    systemMsgId = "20";
                }

                log.info(logPrefix + (logNum++) + " ::: 배분 대기 및 상세 등록 ::: ");
                // 배분 대기 및 상세 등록
                TelewebJSON outJson = talkDataProcessService.processInsertTalkReady(inJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", inJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", messageJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", outJson);
                log.info(logPrefix + (logNum++) + ":!!!@@##!@#@#@#@#@#: {}", systemMsgId);
                log.info(logPrefix + (logNum++) + " ::: call before talkMsgDataProcess.insertSndMsg(inJson) ::: ");
                talkMsgDataProcess.insertSndMsg(inJson);
                log.info(logPrefix + (logNum++) + " ::: call after talkMsgDataProcess.insertSndMsg(inJson) ::: ");
                ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);

                int cnt2 = outJson.getInt("IS_UPDATE");
                log.info(logPrefix + (logNum++) + " ::: 메세지가 들어왔을경우에만 대기알람메세지를 전송한다 ::: cnt2 ::: " + cnt2);
                // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다
                if(cnt2 == 0) {
                    log.info(logPrefix + (logNum++) + " ::: call before transToKakaoService.sendSystemMsg  ::: "
                        + "\ncustcoId ::: " + custcoId
                        + "\nsystemMsgId ::: " + systemMsgId
                        + "\nHcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId) ::: " + HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(custcoId, systemMsgId)
                        + "\ncallTypCd ::: " + callTypCd
                    );

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
