package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingMessageManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingMessageManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingMessageManageRestController", description = "채팅설정메시지관리 REST 컨트롤러")
public class ChatSettingMessageManageRestController {

    private final ChatSettingMessageManageService chatSettingMessageManageService;
    private final ChatSettingMessageManageValidator chatSettingMessageManageValidator;


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메시지관리-조회", notes = "채팅설정메시지관리 조회한다")
    @PostMapping("/chat-api/setting/message-manage/list")
    public Object selectRtnCnslMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        //        chatSettingMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //시스템메시지 조회
        objRetParams = chatSettingMessageManageService.selectRtnSystemMsgByMsgID(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메시지관리-메시지 분류별 시스템 메시지 조회", notes = "채팅설정메시지관리- 메시지 분류별로 시스템 메시지를 조회한다")
    @PostMapping("/chat-api/setting/message-manage/by-se-cd/inqire")
    public Object selectRtnCnslMsgBySeCd(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        //        chatSettingMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessageManageService.selectRtnSystemMsgByMsgID(jsonParams);

        if (mjsonParams.getString("SYS_MSG_ID").equals("6")) {
            //고객무응답 종료 설정 값 조회
            objRetParams.setDataObject("CHAT_END", chatSettingMessageManageService.selectRtnCnslProp(jsonParams));
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메시지관리-응대지연조회", notes = "채팅설정메시지관리 응대지연 조회한다")
    @PostMapping("/chat-api/setting/message-manage/rspons-delay/inqire")
    public Object selectRtnLateRouteMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //Validation 체크 
        //        chatSettingMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //상담준비 메시지 조회
        objRetParams.setDataObject("READY_INFORM_MSG", chatSettingMessageManageService.selectRtnSystemMsgByMsgID(jsonParams));

        //응대지연 메시지 조회
        objRetParams.setDataObject("LATE_ROUTE_MSG", chatSettingMessageManageService.selectRtnCustMsg(jsonParams));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메시지관리-고객무응답조회", notes = "채팅설정메시지관리 고객무응답 조회한다")
    @PostMapping("/chat-api/setting/message-manage/cstmr-nthg-rspons/inqire")
    public Object selectRtnCnslMsgCustNoResp(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        System.out.println(mjsonParams);
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //Validation 체크 
        //        chatSettingMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //고객무응답 종료 설정 값 조회
        objRetParams.setDataObject("CHAT_END", chatSettingMessageManageService.selectRtnCnslProp(jsonParams));

        //고객무응답 메시지 조회
        objRetParams.setDataObject("CUST_NO_RESP_MSG", chatSettingMessageManageService.selectRtnCustMsg(jsonParams));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSG-MNG_PROC", note = "채팅설정메시지관리  변경(등록,수정)")
    @ApiOperation(value = "채팅설정메시지관리-등록", notes = "채팅설정메시지관리 등록한다")
    @PostMapping("/chat-api/setting/message-manage/regist")
    public Object insertRtnCnslMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = chatSettingMessageManageService.insertRtnCnslMsg(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSG-MNG_DEL", note = "채팅설정메시지관리  삭제")
    @ApiOperation(value = "채팅설정메시지관리-삭제", notes = "채팅설정메시지관리 삭제한다")
    @PostMapping("/chat-api/setting/message-manage/delete")
    public Object deleteRtnCnslMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        //        chatSettingMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //메시지 삭제
        objRetParams = chatSettingMessageManageService.deleteRtnCnslMsg(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객무응답 종료 메시지 ON/OFF 클릭 이벤트 정의 TwbTalkMngCnslMsgRestController.java가 아닌 TwbTalkPropMgmtRestController.java에 있는 함수를 호출하기에 함수만 따로 추가함 상담설정 정보 업데이트
     *
     * @param                                  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 처리 결과 데이터
     *
     * @ApiOperation(value="채팅설정메시지관리-설정정보갱신", notes="채팅설정메시지관리 설정정보 갱신한다") @PostMapping("/chat-api/setting/message-manage/estbs-info-updt") public Object updateRtn(@TelewebJsonParam TelewebJSON
     *                                                                mjsonParams, BindingResult result) throws TelewebApiException { //필수객체정의 TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //mjsonParams 해더에
     *                                                                UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
     *
     *                                                                twbTalkPropMgmtValidator.validate(mjsonParams, result); //VALIDATION 체크 if(result.hasErrors()) { throw new ValidationException(result.getAllErrors());
     *                                                                }
     *
     *                                                                //전송된 파라메터 반환 TelewebJSON jsonParams = new TelewebJSON(mjsonParams); jsonParams.setDataObject(mjsonParams.getDataObject(TelewebConst.G_DATA));
     *
     *                                                                objRetParams = chatSettingMessageManageService.updateRtnCnslProp(objRetParams, jsonParams);
     *
     *                                                                // JSONArray objArry = jsonParams.getDataObject(); // String strEnvCd = ""; //환경설정코드 // String strEnvSetVal = ""; //환경설정값 // String strEnvCdNm = "";
     *                                                                //환경설정값 설명 // int objArrSize = objArry.size(); // // // 상담가능 허용 일괄 건수 , 여부 // String contChatagentYn = null; // String contChatagentCnt = null; // //
     *                                                                //배열 사이즈만큼 처리 // if (!objArry.isEmpty()) // { // for (int i = 0; i < objArrSize; i++) // { // //환경설정코드, 환경설정값을 가져온다. // strEnvCd =
     *                                                                objArry.getJSONObject(i).getString("STNG_CD"); // strEnvSetVal = objArry.getJSONObject(i).getString("STNG_VL"); // strEnvCdNm =
     *                                                                objArry.getJSONObject(i).getString("STNG_CD_NM"); // // if (strEnvCd.equals("CONT_CHATAGENT_YN")) contChatagentYn = strEnvSetVal; // if
     *                                                                (strEnvCd.equals("CONT_CHATAGENT_CNT")) contChatagentCnt = strEnvSetVal; // // //환경설정코드, 환경설정값을 세팅한다. // jsonParams.setString("STNG_CD", strEnvCd); //
     *                                                                jsonParams.setString("STNG_VL", strEnvSetVal); // jsonParams.setString("STNG_CD_NM", strEnvCdNm != null ? strEnvCdNm : ""); // // objRetParams =
     *                                                                twbTalkPropService.updateRtnCnslProp(jsonParams); // } // } // // // 상담가능허용수 일괄적용 // if (contChatagentYn != null && contChatagentYn.equals("Y")) // {
     *                                                                // jsonParams.setString("MAX_CHAT_AGENT", contChatagentCnt); // objRetParams =
     *                                                                mobjDao.update("com.hcteletalk.teletalk.mng.mypage.dao.TalkMngMypageMapper", "updateAllMaxAgent", jsonParams); // }
     *
     *                                                                //상담설정 정보 update 후 다시 채우자~ - 20200521 liy //텔레톡 상담 설정 TelewebJSON outJson = routingToAgentManager.selectTalkEnv();
     *                                                                HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);
     *
     *                                                                //최종결과값 반환 return objRetParams; }
     */
}
