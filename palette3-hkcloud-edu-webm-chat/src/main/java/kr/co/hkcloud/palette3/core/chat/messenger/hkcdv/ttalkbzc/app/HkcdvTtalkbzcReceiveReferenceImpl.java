package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.app;


import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.ttalkbzc.domain.TtalkOnReferenceEvent;
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


/**
 * 레퍼런스 수신 인터페이스 구현체
 * 
 * @author User
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvTtalkbzcReceiveReference")
public class HkcdvTtalkbzcReceiveReferenceImpl implements HkcdvTtalkbzcReceiveReference
{
    private static final String                         CALLED_API = "/reference";
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final TalkBusyService                       talkBusyService;
    private final TransToKakaoService                   transToKakaoService;
    private final TalkDataProcessService                       talkDataProcessService;
    private final InnbCreatCmmnService                  innbCreatCmmnService;
    private final TalkRedisChatUtils                    talkRedisChatUtils;
    private final TeletalkReceiveUtils                  teletalkReceiveUtils;
    private final PaletteProperties                     paletteProperties;


    /**
     * 레퍼런스 이벤트 수신
     * 
     * @param   TtalkOnReferenceEvent referenceEvent
     * @throws  Exception
     * @version                       5.0
     */
    @EventListener
    public void onReference(final TtalkOnReferenceEvent referenceEvent) throws TelewebAppException
    {
        String telIdentifier = UUID.randomUUID().toString();
        JSONObject referenceJson = referenceEvent.getReferenceJson();
        log.info("[{}] onReference - {}", telIdentifier, referenceJson.toString());

        String partnerId = paletteProperties.getPartnerId();
        if(StringUtils.isEmpty(partnerId)) {
            log.error("teletalk.properties partnerId is empty ::: {}", partnerId);
            throw new TelewebAppException("teletalk.properties partnerId is empty");
        }
        String userKey = referenceJson.getString("user_key");
        String orgUserKey = referenceJson.getString("org_user_key");
        String senderKey = referenceJson.getString("asp_sndrKey");
        String callTypCd = referenceJson.getString("call_typ_cd");
        String custcoId = referenceJson.getString("custco_id");

        referenceJson.put("CUSTCO_ID", custcoId);
        referenceJson.put("SNDR_KEY", senderKey);

//		String mode = env.getString("realDevMode");

        //path : /reference
        //사용자가 전송한 메시지 상세
        //"reference":{"extra":"고객사에서 관리되는 커스텀한 메타 정보가 전달"} 
        //"reference":{"extra":"", "lastText":"채팅방에서 상담톡으로 전환 시 버튼으로부터 전달된 메타정보", "lastTextDate":"메타정보가 생성된 최신 시각"}}
        // (메타정보는 고객사가 커스텀)
        JSONObject first = referenceJson.getJSONObject("reference");

        // 카카오 정책 변경으로 파라미터명 text -> extra 로 변경 2019.08.12 LSM
        String content = new String();
        try {
            if(first != null && first.containsKey("extra")) {
                JSONArray txtJsonArray = null;
                if(!"".equals(first.getString("extra"))) {
                    txtJsonArray = first.getJSONArray("extra");
                    content = txtJsonArray.toString();
                }
                else {
                    content = "";
                }
            }
            else {
                content = "";
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            content = "";
        }

        String orderNumber = null;
        Integer custSeq = 0;
        String memberYn = null;

        //채팅이 가능하지 않은 상태인 지 체크한다
        if(talkBusyService.isChatDisable(userKey, senderKey, callTypCd, custcoId, true)) { return; }

        //=========================================================
        //= 고객정보 ==============================================
        //=========================================================
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(userKey);
        customerVO.setTalkUserKey(userKey);
        customerVO.setCustcoId(custcoId);
        customerVO.setSndrKey(senderKey);
        customerVO.setChnClsfCd(callTypCd);
        settingCustomerInformationListService.mergeCustomerBaseInfo(customerVO);

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
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "INQRY_TYPE_YN");
        referenceJson.put("INQRY_STATUS", "BEGIN");
        boolean isUseInqry = "Y"
            .equals(inqryTypeYn) && talkDataProcessService.selectInqryLevelType(referenceJson).getSize() > 0;

        //저장하기 위해 치환
        String replaceContent = content.replaceAll("\"", "\\\\\"");

        TelewebJSON inJson = new TelewebJSON();
        String serial = innbCreatCmmnService.getSeqNo(new TelewebJSON(), partnerId);
        log.info("----------------------------------" + serial);
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

        // 고객문의유형 사용할 때
        if(isUseInqry) {
            inJson.setString("TALK_READY_CD", "09");
        }
        else {
            inJson.setString("TALK_READY_CD", "10");
        }
        inJson.setString("CHATBOT_YN", "N");
        inJson.setString("CALL_TYP_CD", callTypCd);

        //재배정
        Boolean blnSendSocketToAgent = talkRedisChatUtils
            .isSendSocketToAgent(CALLED_API, userKey, telewebJSON, objParams);
        if(!blnSendSocketToAgent) {
            // 고객문의유형 사용할 때
            if(isUseInqry) {
                String readyToTalk = HcTeletalkDbSystemMessage.getInstance()
                    .getStringBySystemMsgId(custcoId, "14");
                String ReqType = HcTeletalkDbSystemMessage.getInstance()
                    .getStringBySystemMsgId(custcoId, "15");
                String chatAgent = HcTeletalkDbSystemMessage.getInstance()
                    .getStringBySystemMsgId(custcoId, "16");
                String inqryTypeMsg = new StringBuffer(ReqType).append(" ").append(chatAgent).toString();

                TelewebJSON msgInfoJson = new TelewebJSON();
                msgInfoJson.setString("MSG_READY_TO_TALK", readyToTalk);
                msgInfoJson.setString("MSG_INQRY_TYPE_MSG", inqryTypeMsg);
                msgInfoJson.setString("MSG_READY_TO_TALK_ID", "14");

                //고객문의유형 처리
                talkDataProcessService.processInqryType(msgInfoJson, inJson, referenceJson);
            }
            else {
                //reference는 여러번 날라올 수 있으므로 체크한다
                TelewebJSON selectReadyJson = talkDataProcessService.selectCntTalkUserReadyKey(inJson);
                int cnt = ((JSONObject) (selectReadyJson.getDataObject().get(0))).getInt("CNT");
                if(cnt == 0) {
                    // 배분 대기 및 상세 등록
                    String systemMsgId = "14";
                    ((JSONObject) (inJson.getDataObject().get(0))).put("SYS_MSG_ID", systemMsgId);

                    talkDataProcessService.processInsertTalkReady(inJson);

                    // 메세지가 들어왔을경우에만 대기알람메세지를 전송한다, 챗봇일 경우 알람 메시지를 전송하지 않는다. 수정 이건철 20180313 - 기존 로직

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
