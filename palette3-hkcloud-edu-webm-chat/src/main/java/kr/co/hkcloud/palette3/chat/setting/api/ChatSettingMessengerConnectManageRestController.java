package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingMessengerConnectManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingMessengerConnectManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingMessengerConnectManageRestController", description = "채팅설정메신저연동관리(구 채널연동목록) REST 컨트롤러")
public class ChatSettingMessengerConnectManageRestController {

    private final ChatSettingMessengerConnectManageService chatSettingMessengerConnectManageService;
    private final TransToKakaoService transToKakaoService;
    private final PaletteFilterUtils paletteFilterUtils;
    private final ChatSettingMessengerConnectManageValidator chatSettingMessengerConnectManageValidator;

    private final RoutingToAgentManagerService routingToAgentManagerService;


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메신저연동관리-조회(구 채널연동목록)", notes = "채팅설정메신저연동관리 조회(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/inqire")
    public Object selectRtnPageAspCustChannelList(@TelewebJsonParam TelewebJSON mjsonParams,
        BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /*
         * twbTalkMngAspCustChannelSelectValidator.validate(mjsonParams, result); //VALIDATION 체크 if(result.hasErrors()) { throw new ValidationException(result.getAllErrors()); }
         */

        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.selectRtnPageAspCustChannelList(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메신저연동관리-상세팝업-상세(구 채널연동목록)", notes = "채팅설정메신저연동관리-상세팝업-상세(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/detail-popup/inqire")
    public Object selectRtnPageAspCustChannelDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.selectRtnPageAspCustChannelDetail(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메신저연동관리-카카오톡연동결과조회(구 채널연동목록)", notes = "채팅설정메신저연동관리 카카오톡연동결과조회(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/kakao-intrlck-result/inqire")
    public Object selectRtnKaKaoConnStatus(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.selectRtnKaKaoConnStatus(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSGER-CNT-MNG_PROC", note = "채팅 메신저연동 변경(등록,수정)")
    @ApiOperation(value = "채팅설정메신저연동관리-등록팝업-등록(구 채널연동목록)", notes = "채팅설정메신저연동관리-등록팝업-등록(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/regist-popup/regist")
    public Object insertRtnAspCustChannel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //ValidatiON 체크
        //        chatSettingMessengerConnectManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        // 티톡 자동 채널 생성 
        if (jsonParams.getString("CHN_CLSF_CD") != null && jsonParams.getString("CHN_CLSF_CD").equals("TTALK")) {
            //          {"custco_id": "HKCTALKMNG" , "profile_name" : "이건철_클라우드", "start_dt" : "2020-01-14", "end_dt":"2020-12-31"}

            //티톡 DB 수정 여부
            //티톡 수정
            JSONObject sendKakao = new JSONObject();
            sendKakao.put("custco_id", jsonParams.getString("CUSTCO_ID"));
            sendKakao.put("profile_name", paletteFilterUtils.filter5(jsonParams.getString("DSPTCH_PRF_NM")));
            sendKakao.put("profile_status", jsonParams.getString("SRVC_MAINT_YN"));
            sendKakao.put("CHT_CUTT_DTL_ID", "99999999999");
            sendKakao.put("sndrKey", "");        // 티톡에서 생성
            sendKakao.put("uuid", "");            // 티톡에서 생성
            sendKakao.put("user_key", "");        // 티톡에서 생성
            sendKakao.put("CHT_USER_KEY", "");    // 티톡에서 생성

            JSONObject ret = transToKakaoService.transToKakao("profile", sendKakao, jsonParams.getString("CHN_CLSF_CD")); //이미지 처리

            if (ret != null && ret.getString("code").equals("0")) {
                jsonParams.setString("DSPTCH_PRF_KEY", ret.getString("sndrKey"));
                jsonParams.setString("UUID", ret.getString("uuid"));
            } else {
                // 티톡 등록 실패 오류 던짐.
                objRetParams.setHeader("ERROR_FLAG", true);
                return objRetParams;
            }
        }

        objRetParams = chatSettingMessengerConnectManageService.insertRtnAspCustChannel(jsonParams);

        //텔레톡 상담 설정
        TelewebJSON outJson = routingToAgentManagerService.selectTalkEnv();
        HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);
        log.info("=================================");
        log.info("Chat Application outJson : ", outJson.toString());
        log.info(outJson.toString());
        log.info("=================================");

        //시스템메시지 설정
        TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
        HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);
        log.info("=================================");
        log.info("Chat Application sysMsgJson : ", sysMsgJson.toString());
        log.info("=================================");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSGER-CNT-MNG_PROC", note = "채팅 메신저연동 변경(등록,수정)")
    @ApiOperation(value = "채팅설정메신저연동관리-상세팝업-수정(구 채널연동목록)", notes = "채팅설정메신저연동관리-상세팝업-수정(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/detail-popup/modify")
    public Object updateRtnAspCustChannelDetail(@TelewebJsonParam TelewebJSON mjsonParams,
        BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        chatSettingMessengerConnectManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        // 티톡  api 호출 
        if (jsonParams.getString("CHN_CLSF_CD") != null && jsonParams.getString("CHN_CLSF_CD").equals("TTALK")) {
            //          {"custco_id": "HKCTALKMNG" , "profile_name" : "이건철_클라우드", "start_dt" : "2020-01-14", "end_dt":"2020-12-31"}

            JSONObject sendKakao = new JSONObject();
            sendKakao.put("custco_id", jsonParams.getString("CUSTCO_ID"));
            sendKakao.put("profile_name", jsonParams.getString("DSPTCH_PRF_NM"));
            sendKakao.put("profile_status", jsonParams.getString("SRVC_MAINT_YN"));
            sendKakao.put("CHT_CUTT_DTL_ID", "99999999999");
            sendKakao.put("sndrKey", jsonParams.getString("DSPTCH_PRF_KEY"));    // 티톡에서 생성
            sendKakao.put("uuid", jsonParams.getString("UUID"));            // 티톡에서 생성

            JSONObject ret = transToKakaoService.transToKakao("profile", sendKakao, jsonParams.getString("CHN_CLSF_CD")); //이미지 처리

            if (ret != null && !ret.getString("code").equals("0")) {
                // 티톡 등록 실패 오류 던짐.
                objRetParams.setHeader("ERROR_FLAG", true);
                return objRetParams;
            }
        }

        objRetParams = chatSettingMessengerConnectManageService.updateRtnAspCustChannelDetail(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSGER-CNT-MNG_DEL", note = "채팅 메신저연동 삭제")
    @ApiOperation(value = "채팅설정메신저연동관리-삭제(구 채널연동목록)", notes = "채팅설정메신저연동관리 삭제(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/delete")
    public Object deleteRtnAspCustChannelItem(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        chatSettingMessengerConnectManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        // 티톡  api 호출 
        if (jsonParams.getString("CHN_CLSF_CD") != null && jsonParams.getString("CHN_CLSF_CD").equals("TTALK")) {
            //          {"custco_id": "HKCTALKMNG" , "profile_name" : "이건철_클라우드", "start_dt" : "2020-01-14", "end_dt":"2020-12-31"}

            JSONObject sendKakao = new JSONObject();
            sendKakao.put("custco_id", jsonParams.getString("CUSTCO_ID"));
            sendKakao.put("profile_name", jsonParams.getString("DSPTCH_PRF_NM"));
            sendKakao.put("profile_status", "N");
            sendKakao.put("CHT_CUTT_DTL_ID", "99999999999");
            sendKakao.put("sndrKey", jsonParams.getString("DSPTCH_PRF_KEY"));    // 티톡에서 생성
            sendKakao.put("uuid", jsonParams.getString("UUID"));            // 티톡에서 생성

            JSONObject ret = transToKakaoService.transToKakao("profile", sendKakao, jsonParams.getString("CHN_CLSF_CD")); //이미지 처리

            if (ret != null && !ret.getString("code").equals("0")) {
                // 티톡 등록 실패 오류 던짐.
                objRetParams.setHeader("ERROR_FLAG", true);
                return objRetParams;
            }
        }

        objRetParams = chatSettingMessengerConnectManageService.deleteRtnAspCustChannelItem(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅설정메신저연동관리-검색(구 채널연동목록)", notes = "채팅설정메신저연동관리 채널 정보검색(구 채널연동목록)")
    @PostMapping("/chat-api/setting/messenger-connect-manage/search/inqire")
    public Object selectRtnAspCustChannelInUse(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.selectRtnAspCustChannelInUse(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASP비즈채널 콤보데이터 조회
     *
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "ASP비즈채널 콤보데이터 조회", notes = "ASP비즈채널 콤보데이터 조회.")
    @PostMapping("/chat-api/setting/messenger-connect-manage/biz-combo-data/inqire")
    public Object selectRtnAspBizServiceComboData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.selectRtnAspBizChannelComboData(mjsonParams);
        log.info("temp : {}", objRetParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 채팅채널 사용여부 업데이트
     *
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSGER-CNT-MNG_STTS_CHG", note = "채팅 메신저연동 사용여부변경")
    @ApiOperation(value = "채널 서비스 사용 미사용처리", notes = "채널 서비스 사용 미사용처리")
    @PostMapping("/chat-api/setting/messenger-connect-manage/channel-status/change")
    public Object chnStatChange(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.chnStatChange(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     *
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_MSGER-CNT-MNG_STTS_CHG", note = "채팅 챗봇 사용여부변경")
    @ApiOperation(value = "채널 챗봇 사용 미사용처리", notes = "채널 챗봇 사용 미사용처리")
    @PostMapping("/chat-api/setting/messenger-connect-manage/chbt-yn/change")
    public Object chnChbtStatChange(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.chnChbtStatChange(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 게시판 문의 채널 설정
     *
     *
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "게시판 문의 채널 설정", notes = "게시판 문의 채널 설정")
    @PostMapping("/chat-api/setting/messenger-connect-manage/pst-channel/setting")
    public Object pstChnSet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingMessengerConnectManageService.pstChnSet(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }

    @PostMapping("/chat-api/setting/messenger-connect-manage/detail-popup/selectBbsParamStngList")
    public Object selectBbsParamStngList(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = chatSettingMessengerConnectManageService.selectBbsParamStngList(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }
    @PostMapping("/chat-api/setting/messenger-connect-manage/detail-popup/updateBbsParamStng")
    public Object updateBbsParamStng(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = chatSettingMessengerConnectManageService.updateBbsParamStng(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
