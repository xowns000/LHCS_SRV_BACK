package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingInquiryTypeManageService;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingMessageManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingInquiryTypeManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingInquiryTypeManageRestController", description = "채팅설정문의유형관리 REST 컨트롤러")
public class ChatSettingInquiryTypeManageRestController {

    private final ChatSettingInquiryTypeManageService chatSettingInquiryTypeManageService;
    private final ChatSettingMessageManageService chatSettingMessageManageService;
    private final FileDbMngService fileDbMngService;
    private final ChatSettingInquiryTypeManageValidator chatSettingInquiryTypeManageValidator;
    private final PaletteJsonUtils paletteJsonUtils;


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-채널-트리조회", notes = "채팅설정문의유형관리 채널별 트리조회한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/chnnl-tr/inqire")
    public Object selectRtnTalkMngInqryTypeTreeViewByChannel(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingInquiryTypeManageService.selectRtnTalkMngInqryTypeTreeViewByChannel(jsonParams);

        List<Map<String, Object>> menuList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_QSTN_TYPE_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_QSTN_TYPE_ID");
            menuList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_QSTN_TYPE_ID, "QSTN_TYPE_ID",
                "UP_QSTN_TYPE_ID", "QSTN_TYPE_NM", "SORT");
            objRetParams.setDataObject("QSTN_TYPE_TREE", paletteJsonUtils.getJsonArrayFromList(menuList));
        }

        return objRetParams;    //최종결과값 반환
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-트리-초기화", notes = "채팅설정문의유형관리 트리를 초기화한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/tr-initl/process")
    public Object selectRtnTalkMngInqryTypeTreeClear(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingInquiryTypeManageService.selectRtnTwbTalkInqryTypTreeClear(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_INQRY-TY-MNG_DEL", note = "채팅설정문의유형관리 삭제")
    @ApiOperation(value = "채팅설정문의유형관리-삭제", notes = "채팅설정문의유형관리 삭제한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/delete")
    public Object deleteRtnInqryTyp(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        //validation 체크
        //        chatSettingInquiryTypeManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = chatSettingInquiryTypeManageService.deleteQstnType(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_INQRY-TY-MNG_PROC", note = "채팅설정문의유형관리 변경(등록,수정)")
    @ApiOperation(value = "채팅설정문의유형관리-저장", notes = "채팅설정문의유형관리 저장한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/regist")
    public Object processRtnInqryType(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        //validation 체크
        //        chatSettingInquiryTypeManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        objRetParams = chatSettingInquiryTypeManageService.processRtnInqryType(mjsonParams);
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-채널-상세", notes = "채팅설정문의유형관리 채널 상세조회한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/chnnl/detail")
    public Object selectRtnChannelNodeDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        objRetParams = chatSettingInquiryTypeManageService.selectRtnChannelNodeDetailMst(mjsonParams);

        return objRetParams;    //최종결과값 반환
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-상세", notes = "채팅설정문의유형관리 상세조회한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/detail")
    public Object selectRtnNodeDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        objRetParams = chatSettingInquiryTypeManageService.selectRtnNodeDetailMst(mjsonParams);

        return objRetParams;    //최종결과값 반환
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-추가-필요정보조회", notes = "채팅설정문의유형관리 추가시 필요정보 조회한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/adit-need-info/inqire")
    public Object selectRtnNewInqryCdInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //문의유형 하위 노드 중 가장 마지막 정렬순서 조회
        objRetParams.setDataObject("SORT_INFO", chatSettingInquiryTypeManageService.selectRtnInqryCdLastSortOrd(jsonParams));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @param  fileDeleteRequest
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "CHT_INQRY-TY-MNG_IMG_DEL", note = "채팅설정문의유형관리 이미지 삭제")
    @ApiOperation(value = "채팅설정문의유형관리-이미지-삭제", notes = "채팅설정문의유형관리 이미지 삭제한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/image/delete")
    public Object deleteRtnImage(@TelewebJsonParam TelewebJSON mjsonParams,
        @Valid @RequestBody final FileDeleteRequest fileDeleteRequest) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //문의유형관리 이미지 삭제
        fileDbMngService.delete(fileDeleteRequest);

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상적으로 삭제되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 엑셀에 매핑된 js가 아닌 다른 곳에서 호출
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정문의유형관리-조회", notes = "채팅설정문의유형관리 조회한다")
    @PostMapping("/chat-api/setting/inqry-ty-manage/list")
    public Object selectRtnInqryCd(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingInquiryTypeManageService.selectRtnInqryCd(jsonParams);

        return objRetParams;
    }


    /**
     * 안내메시지 조회
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "안내메시지 조회", notes = "안내메시지 조회")
    @PostMapping("/chat-api/setting/inqry-ty-manage/selectInfoMsg")
    public Object selectInfoMsg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingInquiryTypeManageService.selectInfoMsg(jsonParams);

        return objRetParams;
    }


    /**
     * 안내메시지 저장
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "안내메시지 저장", notes = "안내메시지 저장")
    @PostMapping("/chat-api/setting/inqry-ty-manage/infoMsgRegist")
    public Object infoMsgRegist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);        //DB Access 파라메터 생성
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingInquiryTypeManageService.infoMsgRegist(jsonParams);

        return objRetParams;
    }

}