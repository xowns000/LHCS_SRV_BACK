package kr.co.hkcloud.palette3.core.chat.stomp.api;


import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.main.app.ChatMainService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.stomp.provider.TalkStompJwtTokenProvider;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingSystemMessageManageService;
import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatPublisherService;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompSendToAgentService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.StompChatJwtToken;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
public class TalkStompChatRestController
{
    private final TalkStompJwtTokenProvider     talkStompJwtTokenProvider;
    private final TalkStompSendToAgentService   talkStompSendToAgentService;
    private final TalkDataProcessService        talkDataProcessService;
    private final TalkRedisChatPublisherService talkRedisChatPublisher;
    private final TalkRedisChatReadyRepository  redisChatReadyRepository;
    private final TalkRedisChatInoutRepository  redisChatInoutRepository;
    private final PaletteFilterUtils            paletteFilterUtils;
    private final ChatMainService               chatMainService;
    private final InnbCreatCmmnService          innbCreatCmmnService;
    
    private final ChatSettingSystemMessageManageService   chatSettingSystemMessageManageService;


    /**
     * 
     * @return
     */
    @NoBizLog
    @PostMapping("/api/stomp/chat/userinfo")
    public StompChatJwtToken getUserInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        String userId = "";
        String userName = "";

        //userId = PaletteUserContextSupport.getCurrentUser() != null ? PaletteUserContextSupport.getCurrentUser().getName() : "test01";
        //userName = PaletteUserContextSupport.getCurrentUser().getUsrname();

        //if(PaletteUserContextSupport.getCurrentUser() != null) {
        //    return StompChatJwtToken.builder().name(userName).id(userId).token(talkStompJwtTokenProvider.generateToken(userId)).error("").build();
        //}
        //else {
        //    return StompChatJwtToken.builder().error("-999").build();
        //}

        userId = bodyobj.getString("USER_ID");
        userName = bodyobj.getString("USER_NM");

        if(userId == null || userId.isEmpty() || userId.equals("null") || userId.equals("")) {
            return StompChatJwtToken.builder().error("-999").build();
        }
        else {
            return StompChatJwtToken.builder().name(userName).id(userId).token(talkStompJwtTokenProvider.generateToken(userId)).error("").build();
        }
    }


    /**
     * 채팅 전달
     * 
     * @param  name
     * @return
     * @throws TelewebApiException
     */
    @PostMapping("/api/stomp/chat/forwarding")
    public Object stompChatForwarding(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON responseJson = new TelewebJSON();
        String inOut = mjsonParams.getHeaderString("in_out");

        //상담 전달이면
        if("trans".equals(inOut)) {
            String transId = mjsonParams.getHeaderString("agent_id");

            boolean isReadyTransUserId = redisChatReadyRepository.hasKey(transId);
            if(isReadyTransUserId) {
                // 전달대기 등록
                String cnt = talkDataProcessService.processTransTalkReady(mjsonParams); //서비스에서 처리 by liy - 20180228

                // 전달 받을 사람한테 가는 json
                responseJson.setHeader("called_api", "/readyAlram");
                responseJson.setHeader("ready_count", cnt);
                responseJson.setHeader("code", 0);
                responseJson.setHeader("ERROR_FLAG", false);
                responseJson.setHeader("ERROR_MSG", "전달 건이 존재합니다.");

                //READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 즉시 전달배정알람
                log.trace(">>> READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 즉시 전달배정알람");
                talkRedisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.READY).chatEvent(ChatEvent.TRANS_READY_ALRAM).messageEvent(MessageEvent.SYSTEM).userId(transId)
                    .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).custcoId(mjsonParams.getString("CUSTCO_ID")).build());

                // 전달한 사람한테 가는 json
                String talkContactId = mjsonParams.getHeaderString("TALK_CONTACT_ID");
                responseJson.setHeader("code", 0);
                responseJson.setHeader("TRANS_YN", "Y");
                responseJson.setHeader("TALK_CONTACT_ID", talkContactId);
                responseJson.setHeader("ERROR_FLAG", false);
                responseJson.setHeader("ERROR_MSG", "정상 전달 되었습니다.");
//                sendToAgentLocal(responseJson); => return responseJson;

                //3자채팅 종료
                String userKey = mjsonParams.getHeaderString("TALK_USER_KEY");
                boolean isInoutUserKey = redisChatInoutRepository.hasKey(userKey);
                if(isInoutUserKey) {
                    //3자채팅 상담사에게 메시지 전송
                    String message = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId("20180411175458193MSG34897", ((JSONObject) (mjsonParams.getDataObject().get(0))).getString("CUSTCO_ID"));
                    talkStompSendToAgentService.sendConsultEndToAgent(ChatMessage.builder().userKey(userKey).message(message).custcoId(mjsonParams.getString("CUSTCO_ID")).build());
                }
            }
            else {
                String message = String.format(" %s 님은 채팅 ON 상태가 아닙니다.", transId);
                responseJson.setHeader("code", -999);
                responseJson.setHeader("ERROR_FLAG", true);
                responseJson.setHeader("ERROR_MSG", message);
//                sendToAgentLocal(responseJson); => return responseJson;
            }
        }
        return responseJson;
    }


    /**
     * 채팅 전달 (vue )
     * 
     * @param  name
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담전달",
                  notes = "상담메인 상담전달",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/api/stomp/chat/agent/forwarding",
                    method = RequestMethod.POST)
    public @ResponseBody JSONObject stompChatForwardingwithEnd(@RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON mjsonParams = new TelewebJSON();

        //반환정보 세팅
        JSONObject result = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);

        TelewebJSON responseJson = new TelewebJSON();

        String transId = bodyobj.getString("TRAN_USER");
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("CUSTCO_ID");
        String userId = bodyobj.getString("USER_ID");
        String chtUserKey = bodyobj.getString("CHT_USER_KEY");
        String chtCuttId = bodyobj.getString("CHT_CUTT_ID");

        jsonParams.setString("AGENT_ID", transId);
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("CHT_CUTT_ID", chtCuttId);
        //상담원 채팅대기 상태 조회
        objRetParams = chatMainService.isReadyUser(jsonParams);

        // 로그인 여부 db 체크로 변경 
        boolean isReadyTransUserId = false;
        if(objRetParams != null)
        	//상담원 채팅대기상태 체크하지 않고 전달
//            isReadyTransUserId = objRetParams.getString("IS_READY").equals("1") ? true : false;
        	isReadyTransUserId = true;
//        boolean isReadyTransUserId = redisChatReadyRepository.hasKey(transId); redis 체크 제외 , 일시적 단절 될수도 있음.
        if(isReadyTransUserId) {
            // 컨텐츠 정보 검색
            TelewebJSON contactJson = chatMainService.selectRtnTalkHistInfo(jsonParams);
            jsonParams.setHeader("CUST_ID", contactJson.getString("CUST_ID"));
            jsonParams.setHeader("DSPTCH_PRF_KEY", contactJson.getString("DSPTCH_PRF_KEY"));
            jsonParams.setHeader("SNDR_KEY", contactJson.getString("SNDR_KEY"));
            jsonParams.setHeader("CHT_CUTT_ID", contactJson.getString("CHT_CUTT_ID"));
            jsonParams.setHeader("CUTT_TYPE_ID", bodyobj.getString("CUTT_TYPE_ID"));
            jsonParams.setHeader("agent_id", transId);
            jsonParams.setHeader("agent_nick", transId);
            jsonParams.setHeader("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
            jsonParams.setHeader("PRCS_RSLT_CD", bodyobj.getString("PRCS_RSLT_CD"));

            // 콜백 정보 셋팅 
            jsonParams.setString("CLBK_YN", bodyobj.getString("CLBK_YN"));
            jsonParams.setString("CLBK_YMD", bodyobj.getString("CLBK_YMD"));
            jsonParams.setString("CLBK_BGNG_DT", bodyobj.getString("CLBK_BGNG_DT"));
            jsonParams.setString("QSTN_CHC_RDY_DT", contactJson.getString("QSTN_CHC_RDY_DT"));
            jsonParams.setString("CHN_CLSF_CD", contactJson.getString("CHN_CLSF_CD"));
            jsonParams.setString("CUTT_STTS_CD", bodyobj.getString("CUTT_STTS_CD"));
            jsonParams.setString("CUTT_CN", bodyobj.getString("CUTT_CN"));
            jsonParams.setString("CUTT_TYPE_ID", bodyobj.getString("CUTT_TYPE_ID"));

            // 전달대기 등록
            String cnt = talkDataProcessService.processTransTalkReady(jsonParams); //서비스에서 처리 by liy - 20180228

            // 전달 받을 사람한테 가는 json
            responseJson.setHeader("called_api", "/readyAlram");
            responseJson.setHeader("ready_count", cnt);
            responseJson.setHeader("code", 0);
            responseJson.setHeader("ERROR_FLAG", false);
            responseJson.setHeader("ERROR_MSG", "전달 건이 존재합니다.");
            responseJson.setString("CUST_ID", contactJson.getString("CUST_ID"));
            responseJson.setString("DSPTCH_PRF_KEY", contactJson.getString("DSPTCH_PRF_KEY"));
            responseJson.setString("SNDR_KEY", contactJson.getString("SNDR_KEY"));
            responseJson.setString("CUTT_STTS_CD", bodyobj.getString("CUTT_STTS_CD"));
            responseJson.setString("CHN_CLSF_CD", contactJson.getString("CHN_CLSF_CD"));
            responseJson.setString("SNDR_KEY", contactJson.getString("SNDR_KEY"));
            responseJson.setString("CUTT_CN", bodyobj.getString("CUTT_CN"));
            responseJson.setString("CUST_ID", bodyobj.getString("CUST_ID"));
            responseJson.setString("CUST_NM", bodyobj.getString("CUST_NM"));
            responseJson.setString("CUSTCO_ID", custcoId);
            
            //READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 즉시 전달배정알람
            log.info("전달알림 시작"+ responseJson);
            log.info("전달알림"+ChatMessage.builder().chatType(ChatType.READY).chatEvent(ChatEvent.TRANS_READY_ALRAM).messageEvent(MessageEvent.SYSTEM).userId(transId).custcoId(custcoId)
            .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).build());
            log.trace(">>> READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 즉시 전달배정알람");
            talkRedisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.READY).chatEvent(ChatEvent.TRANS_READY_ALRAM).messageEvent(MessageEvent.SYSTEM).userKey(chtUserKey).userId(transId).custcoId(custcoId)
                .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).build());

            // 전달한 사람한테 가는 json
            String talkContactId = contactJson.getString("CHT_CUTT_ID");
//            responseJson.setHeader("code"           , 0);
//            responseJson.setHeader("TRANS_YN"       , "Y");
//            responseJson.setHeader("TALK_CONTACT_ID", talkContactId);
//            responseJson.setHeader("ERROR_FLAG"     , false);
//            responseJson.setHeader("ERROR_MSG"      , "정상 전달 되었습니다.");
//
//            
            // 구독자 알림 
            String userKey = contactJson.getString("CUST_ID");
            boolean isInoutUserKey = redisChatInoutRepository.hasKey(userKey);
            if(isInoutUserKey) {
                //3자채팅 상담사에게 메시지 전송,전달자 동시 전송 => 기존 redis조회에서 db조회로 변경
                //String message = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, "10");
                jsonParams.setString("SYS_MSG_ID", "10");	//시스템 메시지 업무구분 10 : 채팅전달 시 메시지
                jsonParams.setString("MSG_HR", "0");	//시스템 메시지 업무구분 10 : 채팅전달 시 메시지
                objRetParams = chatSettingSystemMessageManageService.selectSystemMsgList(jsonParams);
                
                String message = objRetParams.getString("MSG_CN");

                responseJson.setHeader("cust_noresponse_message_yn", "Y");
                responseJson.setHeader("code", 0);
                responseJson.setHeader("ERROR_FLAG", false);
                responseJson.setHeader("ERROR_MSG", message);
                responseJson.setString("user_key", userKey);
                responseJson.setHeader("from_who", "fromweb");
                responseJson.setHeader("called_api", "/expired_agent_session");
                responseJson.setString("CHT_CUTT_ID", talkContactId);
                log.trace("sendToAgentLocal ::: {}", responseJson.toString());

                log.trace(">>> CONSULT:EXPIRED_AGENT_SESSION:SYSTEM [PUB] - 3자 상담사,전달자세션만료");
                talkRedisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.CONSULT).chatEvent(ChatEvent.EXPIRED_AGENT_SESSION).messageEvent(MessageEvent.SYSTEM).userKey(userKey).custcoId(custcoId)
                    .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).build());
            }

            result.put("code", "0");			// 정상
            result.put("error", "");			// error
        }
        else {
            String message = String.format(" %s 님은 채팅 ON 상태가 아닙니다.", transId);
            result.put("code", -555);
            result.put("error", message);

        }

        if("0".equals(result.getString("code"))) {
            // 상담이력 저장
        	log.info("!~~~~~~~~~!!!!!!"+jsonParams);
            objRetParams = chatMainService.updateRtnTalkHistByTrans(jsonParams);
        }

        //최종결과값 반환
        return result;
    }

//    @GetMapping("/api/stomp/chat/rooms")
//    public List<ChatRoom> rooms() throws TelewebApiException
//    {
//        List<ChatRoom> chatRooms = redisChatRepository.findAllRoom();
//        chatRooms.stream().forEach(room -> {
//            try
//            {
//                room.setUserCount(redisChatRepository.getInoutCount(room.getRoomId()));
//            }
//            catch (Exception e)
//            {
//                log.error(e.getLocalizedMessage(), e);
//            }
//        });
//        return chatRooms;
//    }

//    @PostMapping("/api/stomp/chat/room")
//    public ChatRoom createRoom(@RequestParam String name) throws TelewebApiException
//    {
//        return redisChatRepository.createChatRoom(name);
//    }
//    
//    @GetMapping("/api/stomp/chat/room/{userKey}")
//    public ChatRoom roomInfo(@PathVariable String userKey) throws TelewebApiException
//    {
//        return redisChatRepository.finByUserKey(userKey);
//    }
}
