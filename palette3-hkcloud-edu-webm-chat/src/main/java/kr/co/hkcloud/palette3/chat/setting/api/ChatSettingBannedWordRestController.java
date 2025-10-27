package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingBannedWordService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordValidator;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
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
@Api(value = "ChatSettingBannedWordRestController", description = "채팅설정금칙어관리 REST 컨트롤러")
public class ChatSettingBannedWordRestController {

    private final ChatSettingBannedWordService chatSettingBannedWordService;
    private final ChatSettingBannedWordValidator chatSettingBannedWordValidator;


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정금칙어관리-목록", notes = "채팅설정금칙어관리 목록 조회한다.")
    @PostMapping("/chat-api/setting/banned-word/list")
    public Object getProhibiteWordList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));

        //validation 체크
        //        chatSettingBannedWordValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }
        objRetParams = chatSettingBannedWordService.getProhibiteWordList(objRetParams);
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정금칙어관리-상세", notes = "채팅설정금칙어관리 상세 조회한다.")
    @PostMapping("/chat-api/setting/banned-word/detail")
    public Object getProhibiteWordDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));
        objRetParams = chatSettingBannedWordService.getProhibiteWordDetail(objRetParams);
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_BANNED-WORD_PROC", note = "채팅설정금칙어 변경(등록,수정)")
    @ApiOperation(value = "채팅설정금칙어관리-등록", notes = "채팅설정금칙어관리 등록한다.")
    @PostMapping("/chat-api/setting/banned-word/regist")
    public Object insertProhibiteWord(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));

        //validation 체크
        //        chatSettingBannedWordValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return chatSettingBannedWordService.insertProhibiteWord(objRetParams);
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_BANNED-WORD_PROC", note = "채팅설정금칙어 변경(등록,수정)")
    @ApiOperation(value = "채팅설정금칙어관리-수정", notes = "채팅설정금칙어관리 수정한다.")
    @PostMapping("/chat-api/setting/banned-word/modify")
    public Object putProhibiteWord(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //validation 체크
        chatSettingBannedWordValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));
        objRetParams = chatSettingBannedWordService.putProhibiteWord(objRetParams);
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_BANNED-WORD_DEL", note = "채팅설정금칙어 삭제")
    @ApiOperation(value = "채팅설정금칙어관리-삭제", notes = "채팅설정금칙어관리 삭제한다.")
    @PostMapping("/chat-api/setting/banned-word/delete")
    public Object deleteProhibiteWord(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //validation 체크
        //        chatSettingBannedWordValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        for (int i = 0; i < mjsonParams.getDataObject("DATA").size(); i++) {
            objRetParams.setDataObject("DATA", (JSONObject) mjsonParams.getDataObject("DATA").get(i));
            chatSettingBannedWordService.deleteProhibiteWord(objRetParams);
        }
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "금칙어 중복체크", notes = "채팅설정금칙어관리 금칙어 중복 체크.")
    @PostMapping("/chat-api/setting/banned-word/dupl/check")
    public Object fbdwdDuplCheck(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return chatSettingBannedWordService.fbdwdDuplCheck(mjsonParams);
    }
}
