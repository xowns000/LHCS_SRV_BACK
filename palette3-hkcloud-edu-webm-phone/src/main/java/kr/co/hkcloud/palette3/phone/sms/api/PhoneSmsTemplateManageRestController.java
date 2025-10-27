package kr.co.hkcloud.palette3.phone.sms.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sms.app.PhoneSmsTemplateManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneSmsTemplateManageRestController",
     description = "문자 템플릿 관리 REST 컨트롤러")
public class PhoneSmsTemplateManageRestController
{
    private final PhoneSmsTemplateManageService phoneSmsTemplateManageService;

    /**
     * SMS 템플릿 분류 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 분류 조회",
                  notes = "SMS 템플릿 분류 조회")
    @PostMapping("/phone-api/sms/tmpl/manage/clsf")
    public Object selectRtnSmsTree(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneSmsTemplateManageService.selectRtnSmsTree(mjsonParams);
    }
    

    /**
     * SMS템플릿 분류 추가 가능 여부 조회
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
//    @ApiOperation(value = "SMS템플릿 분류 트리 노드 추가 가능 여부 조회",
//            notes = "해당 분류 노드에 SMS템플릿이 존재하면 화면에서 하위 노드 생성 불가하도록 제어하기 위함")
//    @PostMapping("/phone-api/sms/tmpl/manage/isRegTmplClsf")
//    public Object isRegTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsTemplateManageService.isRegTmplClsf(mjsonParams);
//    }

    /**
     * SMS템플릿 분류 추가
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS템플릿 분류 추가",
            notes = "SMS템플릿 분류 추가")
    @PostMapping("/phone-api/sms/tmpl/manage/insertTmplClsf")
    public Object insertTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneSmsTemplateManageService.insertTmplClsf(mjsonParams);
    }
    
    /**
     * SMS템플릿 분류 수정
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS템플릿 분류 수정",
    		notes = "SMS템플릿 분류 수정")
    @PostMapping("/phone-api/sms/tmpl/manage/modifyTmplClsf")
    public Object modifyTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.modifyTmplClsf(mjsonParams);
    }

    /**
     * SMS템플릿 분류 추가
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS템플릿 분류 추가",
    		notes = "SMS템플릿 분류 추가")
    @PostMapping("/phone-api/sms/tmpl/manage/deleteTmplClsf")
    public Object deleteTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.deleteTmplClsf(mjsonParams);
    }

    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
            notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/sms/tmpl/manage/list")
    public Object selectSmsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneSmsTemplateManageService.selectSmsList(mjsonParams);
    }

    /**
     * 템플릿 삭제
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "템플릿 삭제",
            notes = "템플릿을 삭제한다")
    @PostMapping("/phone-api/sms/tmpl/manage/deleteSmsTmpl")
    public Object deleteSmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.deleteSmsTmpl(mjsonParams);
    }

    /**
     * SMS 저장
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 저장",
                  notes = "SMS 저장")
    @PostMapping("/phone-api/sms/tmpl/manage/insertSmsTmpl")
    public Object insertSmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneSmsTemplateManageService.insertSmsTmpl(mjsonParams);
    }
    
    /**
     * SMS 수정
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 수정",
    		notes = "SMS 수정")
    @PostMapping("/phone-api/sms/tmpl/manage/modifySmsTmpl")
    public Object modifySmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.modifySmsTmpl(mjsonParams);	
    }

    /**
     * mms 그룹키 업데이트
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
    @ApiOperation(value = "mms 그룹키 업데이트",
    		notes = "mms 그룹키 업데이트")
    @PostMapping("/phone-api/sms/tmpl/manage/fileKeyUnity")
    public Object fileKeyUnity(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.fileKeyUnity(mjsonParams);
    }
    
    /**
     * MMS 업로드 파일 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS리스트 조회",
            notes = "SMS리스트 조회")
    @PostMapping("/phone-api/sms/tmpl/manage/uploadFiles")
    public Object selectMmsUploadFileList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneSmsTemplateManageService.selectMmsUploadFileList(mjsonParams);
    }
    
    /**
     * 템플릿 첨부파일 삭제
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
    @ApiOperation(value = "mms 그룹키 업데이트",
    		notes = "mms 그룹키 업데이트")
    @PostMapping("/phone-api/sms/tmpl/manage/deleteSmsTmplFileByFileKey")
    public Object deleteSmsTmplFileByFileKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.deleteSmsTmplFileByFileKey(mjsonParams);
    }
    
    /**
     * 템플릿유형 Tree 순서 변경
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "템플릿유형 Tree 순서 변경",
            notes = "템플릿유형 Tree Node 순서를 변경 한다")
    @PostMapping("/phone-api/sms/tmpl/manage/changeTmplClsfSortOrder")
    public Object changeTmplClsfSortOrder(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneSmsTemplateManageService.changeTmplClsfSortOrder(mjsonParams);
    }
    
}
