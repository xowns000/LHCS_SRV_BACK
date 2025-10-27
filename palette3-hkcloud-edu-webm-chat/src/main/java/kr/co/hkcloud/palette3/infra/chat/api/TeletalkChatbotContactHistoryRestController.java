package kr.co.hkcloud.palette3.infra.chat.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.infra.chat.app.TeletalkChatbotContactHistoryService;
import kr.co.hkcloud.palette3.infra.chat.util.TeletalkChatbotContactHistoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 챗봇 대회아력을 텔레톡에 저장한다. 상담이력ID가 있으면, 해당 상담이력ID로 저장하며, 없는경우에는 신규 상담이력ID를 생성하여 새로운 이력으로 생성하여 저장한 뒤 상담이력ID를 반환한다.
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "TeletalkChatbotContactHistoryRestController",
        description = "챗봇 대화이력 서비스  REST 컨트롤러")
public class TeletalkChatbotContactHistoryRestController {
    private final TeletalkChatbotContactHistoryValidator teletalkChatbotContactHistoryValidator;
    private final TeletalkChatbotContactHistoryService teletalkChatbotContactHistoryService;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 챗봇 대화이력 저장
     *
     * @param jsonBody 챗봇 대화이력 JSON 파라미터 talkUserKey : 카카오유저키 TALK_CONTACT_ID : 상담이력ID content_type : 타입 content : 챗봇 대화내용 TALK_TITLE CUSTCO_ID SNDR_KEY CUST_ID
     **/
    @ApiOperation(value = "챗봇 대화이력 저장 API",
            notes = "챗봇 대화이력 저장 API")
    @PostMapping(value = "/infra/hkcdv/chat-apibotContactHistory")
    public JSONObject insertChatbotContactHistory(@RequestBody JSONObject jsonBody, HttpServletRequest request, BindingResult result) throws TelewebApiException {
        JSONObject jsonRtn = new JSONObject();
        JSONObject data = new JSONObject();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON jsonReturn = new TelewebJSON();

        log.debug("insertChatbotContactHistory jsonBody : {}", jsonBody);

        //카카오유저키
        if (jsonBody.containsKey("talkUserKey") && !jsonBody.getString("talkUserKey").isEmpty()) {
            jsonParams.setString("user_key", jsonBody.getString("talkUserKey"));
        }
        //상담이력ID
        if (jsonBody.containsKey("TALK_CONTACT_ID") && !jsonBody.getString("TALK_CONTACT_ID").isEmpty()) {
            jsonParams.setString("TALK_CONTACT_ID", jsonBody.getString("TALK_CONTACT_ID"));
        }
        //메시지타입
        if (jsonBody.containsKey("content_type") && !jsonBody.getString("content_type").isEmpty()) {
            jsonParams.setString("TYPE", jsonBody.getString("content_type"));
        }
        //수신발신코드
        if (jsonBody.containsKey("SNDRCV_CD") && !jsonBody.getString("SNDRCV_CD").isEmpty()) {
            jsonParams.setString("SNDRCV_CD", jsonBody.getString("SNDRCV_CD"));
        }
        //수발신메시지
        if (jsonBody.containsKey("content") && !jsonBody.getString("content").isEmpty()) {
            jsonParams.setString("CONTENT", jsonBody.getString("content"));
        }
        //업무명
        if (jsonBody.containsKey("TALK_TITLE") && !jsonBody.getString("TALK_TITLE").isEmpty()) {
            jsonParams.setString("TALK_TITLE", jsonBody.getString("TALK_TITLE"));
            jsonParams.setString("TALK_QST", jsonBody.getString("TALK_TITLE"));
            jsonParams.setString("TALK_ANS", jsonBody.getString("TALK_TITLE"));
        }
        //고객사키
        if (jsonBody.containsKey("CUSTCO_ID") && !jsonBody.getString("SNDR_KEY").isEmpty()) {
            jsonParams.setString("CUSTCO_ID", jsonBody.getString("CUSTCO_ID"));
        } else {
            jsonParams.setString("CUSTCO_ID", "HKCTALKMNG");
        }
        //채널SENDER KEY
        if (jsonBody.containsKey("SNDR_KEY") && !jsonBody.getString("SNDR_KEY").isEmpty()) {
            jsonParams.setString("SNDR_KEY", jsonBody.getString("SNDR_KEY"));
            StringBuffer sb = new StringBuffer();
            sb.append(jsonBody.getString("SNDR_KEY"));
            sb.append("_");
            sb.append(jsonBody.getString("talkUserKey"));
            jsonParams.setString("TALK_USER_KEY", sb.toString());    //유저키 SenderKey_UserKey
            jsonParams.setString("CUSTOMER_ID", sb.toString());        //고객ID SenderKey_UserKey
        }

        //고객ID - 챗봇 인증시 고객ID가 전달된다. 고객ID가 있는 경우에는 고객테이블에 업데이트 한다.
        if (jsonBody.containsKey("CUST_ID") && !jsonBody.getString("CUST_ID").isEmpty()) {
            String custId = jsonBody.getString("CUST_ID");
            jsonParams.setString("CUSTOMER_SEQ", custId);
            teletalkChatbotContactHistoryService.insertCustInfo(jsonParams);
        }

        //Validation 체크 
        teletalkChatbotContactHistoryValidator.validate(jsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonRtn.put("success", false);    //챗봇으로 반환할 성공여부 초기세팅
        jsonRtn.put("code", "400");        //챗봇으로 반환할 코드 초기세팅

        //상담아력ID 있는경우
        if (jsonParams.containsKey("TALK_CONTACT_ID") && jsonParams.getString("TALK_CONTACT_ID") != "") {
            //상담이력상세에 데이터 삽입
            jsonRtn.put("TALK_CONTACT_ID", jsonParams.getString("TALK_CONTACT_ID"));        //챗봇으로 반환할 상담이력ID
            jsonRtn.put("success", true);                                                    //챗봇으로 반환할 성공여부
            jsonRtn.put("code", "200");                                                        //챗봇으로 반환할 코드
            jsonParams.setString("TALK_SERIAL_NUMBER", innbCreatCmmnService.getSeqNo(jsonParams, "BOT"));    //시리얼번호
            teletalkChatbotContactHistoryService.insertChatbotContactDetail(jsonParams);            //상담이력 상세 저장

        }
        //상담이력ID 없는 경우
        else {
            //신규 상담이력ID 생성 후 상담이력 삽입 (상담완료), 상담이력상세 데이터 삽입
            String strBizCase = "BT";
            String strContactId = innbCreatCmmnService.getSeqNo(jsonParams, strBizCase);                        //신규 상담이력ID 생성
            jsonParams.setString("TALK_CONTACT_ID", strContactId);
            jsonRtn.put("TALK_CONTACT_ID", strContactId);                                        //챗봇으로 반환할 상담이력ID
            jsonRtn.put("success", true);                                                        //챗봇으로 반환할 성공여부
            jsonRtn.put("code", "200");                                                            //챗봇으로 반환할 코드
            jsonParams.setString("TALK_SERIAL_NUMBER", innbCreatCmmnService.getSeqNo(jsonParams, "BOT"));        //시리얼번호

            teletalkChatbotContactHistoryService.insertChatbotContact(jsonParams);                    //상담이력 신규 삽입
            teletalkChatbotContactHistoryService.insertChatbotContactDetail(jsonParams);                //상담이력 상세 저장

            //NGNS로 상담이력 이관
            TelewebJSON jsonEaiParams = new TelewebJSON();
            TelewebJSON jsonTemp = new TelewebJSON();

            //DB조회가 필요한 부분은 조회하여 정보 세팅
            jsonTemp = teletalkChatbotContactHistoryService.selectCustInfo(jsonParams);                //DB조회후에 세팅

            if (jsonTemp.getDataObject("CUST_INFO") != null || jsonTemp.getDataObject("CONTACT_INFO") != null) {

                if (jsonTemp.getDataObject("CONTACT_INFO") != null && jsonTemp.getDataObject("CONTACT_INFO").size() > 0) {
                    log.debug("insertChatbotContactHistory 2");
                    JSONObject jsonContact = jsonTemp.getDataObject("CONTACT_INFO").getJSONObject(0);
                    jsonEaiParams.setString("TALK_CONTACT_DT", jsonContact.getString("TALK_CONTACT_DT"));    //상담일시 (채팅 상담시작일시 YYYY-MM-DD 00:00:00)
                    jsonEaiParams.setString("TALK_DIST_DT", jsonContact.getString("TALK_DIST_DT"));            //상담인입일시 (라이브랫 접속일시 YYYY-MM-DD 00:00:00)
                    jsonEaiParams.setString("TALK_END_DT", jsonContact.getString("TALK_END_DT"));            //상담종료일시 (채팅 종료일시(저장일시)  YYYY-MM-DD 00:00:00)
                }

                if (jsonTemp.getDataObject("CUST_INFO") != null && jsonTemp.getDataObject("CUST_INFO").size() > 0) {

                    JSONObject jsonCust = jsonTemp.getDataObject("CUST_INFO").getJSONObject(0);
                    jsonEaiParams.setString("CUST_NAME", jsonCust.getString("CUSTOMER_NM"));    //고객명 => DB조회후헤 세팅
                    jsonEaiParams.setString("CUST_ID", jsonCust.getString("CUSTOMER_SEQ"));        //고객ID

                    jsonEaiParams.setString("TALK_CONTACT_ID", jsonParams.getString("TALK_CONTACT_ID"));            //상담이력ID
                }
            }
        }

        return jsonRtn;
    }
}
