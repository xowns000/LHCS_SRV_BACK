package kr.co.hkcloud.palette3.chat.status.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.status.app.ChatStatusCounselingListService;
import kr.co.hkcloud.palette3.chat.status.util.ChatStatusCounselingListValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatStatusCounselingListRestController",
     description = "채팅현황상담중현황및3차채팅 REST 컨트롤러")
public class ChatStatusCounselingListRestController
{
    private final ChatStatusCounselingListValidator chatStatusCounselingListValidator;
    private final ChatStatusCounselingListService   chatStatusCounselingListService;


    /**
     * 
     * @return TeleWebJson 형식의 처리 결과 데이터
     * @author Kang Myoung Gu
     * @since  2018. 05. 08
     */
    @ApiOperation(value = "채팅현황상담중현황및3차채팅-조회",
                  notes = "채팅현황상담중현황및3차채팅 조회한다")
    @PostMapping("/chat-api/status/cnslt-list/inqire")
    public Object selectRtnPageCnslMgmt(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        chatStatusCounselingListValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        jsonParams.setString("TALK_DIST_DT_START", getCorrected(jsonParams));

        objRetParams = chatStatusCounselingListService.selectRtnPageCnslMgmtByKaom_new(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams 전송된 파라메터 데이터
     * @return
     * 
     */
    private String getCorrected(TelewebJSON jsonParams) throws TelewebApiException
    {
        String strCorrectedDate = chatStatusCounselingListService.selectCorrectedDate(jsonParams).getString("CORRECTED_DATE");

        String strWorkStartTime = HcTeletalkDbEnvironment.getInstance().getString(((JSONObject) (jsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"), "WORK_START_TIME");

        return strCorrectedDate + strWorkStartTime + "00";
    }
}
