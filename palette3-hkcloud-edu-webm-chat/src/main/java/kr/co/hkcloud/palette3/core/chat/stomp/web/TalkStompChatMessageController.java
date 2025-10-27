package kr.co.hkcloud.palette3.core.chat.stomp.web;


import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.validation.constraints.NotEmpty;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.config.stomp.listeners.TalkWebSocketConstants;
import kr.co.hkcloud.palette3.config.stomp.provider.TalkStompJwtTokenProvider;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompMessageIncomingService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import kr.co.hkcloud.palette3.file.app.FileStorageService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileConvertUtils;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Controller
public class TalkStompChatMessageController
{
    private final ApplicationEventPublisher       eventPublisher;
    private final TalkStompJwtTokenProvider       stompJwtTokenProvider;
    private final PaletteProperties               paletteProperties;
    private final TalkStompMessageIncomingService stompMessageIncomingService;
    private final TransToKakaoService             transToKakaoService;
    private final TalkMsgDataProcessService       msgDataProcess;
    private final InnbCreatCmmnService            innbCreatCmmnService;
    private final FileStorageService              fileStorageService;
    private final TalkRedisChatInoutRepository    redisChatInoutRepository;
    private final PaletteFilterUtils              paletteFilterUtils;
    private final PaletteJsonUtils                paletteJsonUtils;
    private final FileDownloadUtils               fileDownloadUtils;
    private final FileConvertUtils                fileConvertUtils;
    private final FileRuleUtils                   fileRuleUtils;
    private final FileRulePropertiesUtils         fileRulePropertiesUtils;

//    /**
//     * 상담대기 "/app/ready/chat/message"로 들어오는 메시징을 처리한다.
//     * @param chatMessage
//     */
//    @MessageMapping("/ready/chat/message")
//    public void onReadyMessage(ChatMessage chatMessage, @Header("token") String token) throws TelewebWebException
//    {
//        log.trace("onReadyMessage :::");
//        
//        String userId = stompJwtTokenProvider.getUserNameFromJwt(token);
//        
//        //로그인 회원 정보로 대화명 설정
//        chatMessage.setUserId(userId);
//        
//        //Websocket에 발행된 메시지를 redis로 발행(publish)
//        stompRedisPublisher.sendPubMessage(chatMessage);
//    }


    /**
     * 상담중 "/app/inout/chat/message"로 들어오는 메시징을 처리한다.
     * 
     * @param chatMessage
     */
    @MessageMapping("/inout/chat/message")
    public void onInoutMessage(Message<?> inMessage, ChatMessage chatMessage, @NotEmpty String sendData, @Header("token") String token, @Header("userKey") String userKey, @Header("CERT-CUSTCO-TENANT-ID") String tenantCode) throws TelewebWebException
    {
        log.debug("onInoutMessage :::");
        log.debug("sendData ::: {}", sendData);
        log.info("onInoutMessage :::");
        log.info("sendData ::: {}", sendData);
        log.info("22nowCustco "+TenantContext.getCurrentCustco());
        log.info("22nowTenant "+TenantContext.getCurrentTenant());
        log.info("TenantCode1 " + tenantCode);
        TenantContext.setCurrentTenant(tenantCode);
        log.info("TenantCode2 " + TenantContext.getCurrentTenant());

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(inMessage);
        String custcoId = (String) accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_CUSTCO_ID);

        String nickname = stompJwtTokenProvider.getUserNameFromJwt(token);

        //로그인 회원 정보로 대화명 설정
        chatMessage.setUserId(nickname);

        TelewebJSON objParams = new TelewebJSON(sendData);
        
//        chatMessage.setUserId(objParams.getString("USER_ID"));
        
        objParams.setString("CHT_CUTT_ID", objParams.getString("CHT_CUTT_ID"));
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("SNDR_KEY", objParams.getString("sndrKey"));
        objParams.setString("CUSTCO_ID", objParams.getString("CUSTCO_ID"));

        // 시리얼 넘버 서버 검색으로 수정 20200420 SJH 
        if(objParams.getString("CHT_CUTT_DTL_ID") == null || ("").equals(objParams.getString("CHT_CUTT_DTL_ID"))) {
            objParams.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
        }
        else {
            objParams.setString("CHT_CUTT_DTL_ID", objParams.getString("CHT_CUTT_DTL_ID"));
        }

        objParams.setString("TYPE", objParams.getString("message_type"));
        objParams.setString("CHN_CLSF_CD", objParams.getString("chnClsfCd"));
        //objParams.setString("CUSTCO_ID", custcoId);
        objParams.setString("CUSTCO_ID", objParams.getString("CUSTCO_ID"));

        String calledApi = objParams.containsHeaderKey("called_api") ? objParams.getHeaderString("called_api") : "/inOut";

        // 상담종료를 클릭하여 넘어왔을 경우,
        if("Y".equals(objParams.getString("consultEnd"))) {
            JSONObject sendJson = null;
        	String endMsg = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(objParams.getString("CUSTCO_ID"), "2");
            objParams.setString("SYS_MSG_ID","2");
            objParams.setString("CONTENT", endMsg);

            boolean isInoutUserKey = redisChatInoutRepository.hasKey(userKey);
            if(isInoutUserKey) {
            }

            String sendString = String.format("{\"user_key\":\"%s\",\"sndrKey\":\"%s\",\"CHT_CUTT_DTL_ID\":\"%s\",\"message_type\":\"TX\",\"message\":\"%s\"}", userKey, objParams.getString("sndrKey"), objParams
                .getString("CHT_CUTT_DTL_ID"), endMsg);

            log.debug("consultEnd sendString ::: {}", sendString);
            sendJson = JSONObject.fromObject(sendString);
            objParams.setString("CONTENT", endMsg);
            
            JSONObject ret = transToKakaoService.transToKakao("write", sendJson, objParams.getString("chnClsfCd"));
            
            objParams.setString("MSG_TYPE_CD", objParams.getString("message_type"));
            objParams.setString("RCPTN_DSPTCH_CD", objParams.getString("SNDRCV_CD"));
            if(objParams.getString("RCPTN_DSPTCH_CD").equals("SND")){
                objParams.setString("RCPTN_SNDPTY_ID", objParams.getString("USER_ID"));
            } else if(objParams.getString("RCPTN_DSPTCH_CD").equals("RCV")) {
                objParams.setString("RCPTN_SNDPTY_ID", objParams.getString("CUST_ID"));
            }
            
            //이메일은 종료 메세지를 남기지 않음.
            if(!"EMAIL".equals(objParams.getString("chnClsfCd"))) {
                msgDataProcess.insertSndMsg(objParams);
            }

            JSONObject obj = new JSONObject();
            obj.put("user_key", userKey);
            obj.put("sndrKey", objParams.getString("sndrKey"));
            obj.put("CHT_CUTT_ID", objParams.getString("CHT_CUTT_ID"));
            obj.put("CHT_CUTT_DTL_ID", objParams.getString("CHT_CUTT_DTL_ID"));
            obj.put("CHT_USER_KEY", objParams.getString("CHT_USER_KEY"));
            
            eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj).callTypCd(objParams.getString("chnClsfCd")).build());
        }
        else {
            if("links".equals(objParams.getString("type"))) {
                objParams.setString("CONTENT", paletteFilterUtils.filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(objParams.getString("CUSTCO_ID"), "26")));

            }
            else if("LI_UNTACT".equals(objParams.getString("type"))) {
                // 비대면 링크 
                String content = objParams.getString("message");
                objParams.setString("CONTENT", paletteJsonUtils.valueToStringWithoutQutoes(content.toString()));
            }
            else if("TX".equals(objParams.getString("TYPE"))) {
                String content = objParams.getString("message");
                objParams.setString("CONTENT", paletteJsonUtils.valueToStringWithoutQutoes(content.toString()));
            }
            else if("chatClose".equals(objParams.getString("type"))){
            	String endBtnMsg = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(objParams.getString("CUSTCO_ID"), "27");
            	objParams.setString("SYS_MSG_ID", "27");
                objParams.setString("CONTENT", endBtnMsg);
            }
            else {
                objParams.setString("CONTENT", objParams.getString("message"));
            }


//          //채팅방 인원수 세팅
//          chatMessage.setUserCount(redisChatInoutRepository.getCount(chatMessage.getUserKey()));

            // HYG :: TELETALK 고객무응답 시간체크 버그 패치 적용
            // HYG 20190828 :: 메시지 길이 제한 오류 1000자 초과 
            // 카카오 API 에 보낼 때 큰따옴표의 경우, 두글자로 체크하여 1000자 초과로 계산되고 MessageLengthOverLimitException 발생함
            // 카카오 api에 전달되기 전에 자바단에서 데이터 유효성을 체크해준다.
            // , LIY 20200301
            int rtnCode = stompMessageIncomingService.chkValidationData(objParams);
            if(rtnCode < 0) // 실패 
            {
                //stompMessageIncomingService.sendToAgentMessageError(userKey, rtnCode, objParams.getString("USER_ID"), custcoId);
                stompMessageIncomingService.sendToAgentMessageError(userKey, rtnCode, objParams.getString("USER_ID"), objParams.getString("CUSTCO_ID"));
                return;
            }

            //시스템 메시지가 아닌, 실제 메시지 인경우만 초기화 처리를 함. SJH
            if("text".equals(objParams.getString("TYPE")) || "photo".equals(objParams.getString("TYPE")) || "TX".equals(objParams.getString("TYPE"))) {
                msgDataProcess.updateCustNoRespIs0(objParams);  //상담사가 메시지를 보냈을 경우 고객 무응답 메시지 시간 0으로 초기화
            }

//            // line은 public 경로로 이미지 경로 변경, 같은 파일명은 라인에서 수신이 안되므로 serialnum 으로 파일명 변경함.
//            if(objParams.getString("CALL_TYP_CD").equals("LINE") && !"".equals(objParams.getString("IMAGE_TALK_PATH"))) {
//
//                // [파일] 상담중 메시지 처리: 라인 public 복사 관련 확인 필요
//                //https://test.hkcloud.co.kr/infra/api/file/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey}
//                String imageUrl = transferUtils.copyPublicRepositoryPath(objParams.getString("IMAGE_TALK_PATH"), objParams.getString("TALK_SERIAL_NUMBER"));
//
//                objParams.setString("IMAGE_URL", imageUrl);
//
//            }

            //[파일] 상담중 메시지 처리: 채팅-이미지(고객)
            if("photo".equals(objParams.getString("TYPE"))) {
                // @formatter:off
                final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
                final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
                final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
                log.debug("fileProperties>>>{}", fileProperties);
                
                FileAccessType fileAcsTypeCd = FileAccessType.PRIVATE; //비공개:기본값
                
                //라인인 경우 PUBLIC
                if(objParams.getString("chnClsfCd").equals("LINE")) {
                    fileAcsTypeCd = FileAccessType.PUBLIC; //공개
                }

                //이미지관리에 있는 이미지 복사하여 상담이력상세에 저장한다.
                FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                                      .fileGroupKey(objParams.getString("FILE_GROUP_KEY"))
                                                                                      .fileKey(objParams.getString("FILE_KEY"))
                                                                                      .build();
                
                FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(fileProperties, fileDbMngSelectRequest);

                //그룹키 생성
                String fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(new FileUploadRequests());
                File file;
                try {
                    file = fileDownloadResponse.getResource().getFile();
                }
                catch(IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                    throw new TelewebUtilException(e.getLocalizedMessage(), e);
                }
                
                FileUploadRequest fileUploadRequest = FileUploadRequest.builder()
                                                                       .file(fileConvertUtils.toMultipartFile(file))
                                                                       .fileGroupKey(fileGroupKey)
                                                                       .fileAcsTypeCd(fileAcsTypeCd)
                                                                       .build();

                //파일 저장
                FileUploadResponse fileUploadResponse = fileStorageService.store(fileProperties, fileUploadRequest);

                //파일정보 세팅
                objParams.setString("TASK_TYPE_CD", taskTypeCd.toString());
                objParams.setString("PATH_TYPE_CD", pathTypeCd.toString());
                objParams.setString("FILE_GROUP_KEY", fileUploadResponse.getFileGroupKey());
                objParams.setString("FILE_KEY", fileUploadResponse.getFileKey());
                
                //라인인 경우 PUBLIC URL 생성
                if(objParams.getString("chnClsfCd").equals("LINE") && fileAcsTypeCd == FileAccessType.PUBLIC) {
                    
                    //https://test.hkcloud.co.kr/infra/api/file/{custcoId}/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey}
                    final URI imageUri = paletteProperties.getPublicUrl()
                                                          .resolve("/infra/api/file")
                                                          //.resolve("/").resolve(custcoId)
                                                          .resolve("/").resolve(objParams.getString("CUSTCO_ID"))
                                                          .resolve("/").resolve(taskTypeCd.name())
                                                          .resolve("/").resolve(pathTypeCd.name())
                                                          .resolve("/").resolve(fileUploadResponse.getFileGroupKey())
                                                          .resolve("/").resolve(fileUploadResponse.getFileKey());
                    
                    log.debug(">>>obj imageUri: {}", imageUri);
                    log.debug(">>>str imageUri: {}", imageUri.toString());
                    objParams.setString("IMAGE_URL", imageUri.toString());
                }
                // @formatter:on
            }

            
            objParams.setString("MSG_TYPE_CD", objParams.getString("message_type"));
            objParams.setString("RCPTN_DSPTCH_CD", objParams.getString("SNDRCV_CD"));
            if(objParams.getString("RCPTN_DSPTCH_CD").equals("SND")){
                objParams.setString("RCPTN_SNDPTY_ID", objParams.getString("USER_ID"));
            } else if(objParams.getString("RCPTN_DSPTCH_CD").equals("RCV")) {
                objParams.setString("RCPTN_SNDPTY_ID", objParams.getString("CUST_ID"));
            }
            
            msgDataProcess.insertSndMsg(objParams);
            stompMessageIncomingService.messageIncoming(userKey, calledApi, sendData, objParams);    // 시리얼키 넘김, SJH 20190107
        }
    }

}
