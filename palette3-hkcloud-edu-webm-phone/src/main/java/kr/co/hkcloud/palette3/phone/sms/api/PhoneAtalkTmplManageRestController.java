package kr.co.hkcloud.palette3.phone.sms.api;


import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sms.app.PhoneAtalkTmplManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneAtalkTmplManageRestController",
     description = "문자 템플릿 관리 REST 컨트롤러")
public class PhoneAtalkTmplManageRestController
{
	private final PhoneAtalkTmplManageService phoneAtalkTmplManageService;
	
	/**
	 * 알림톡 발송프로필 목록 조회
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 발송프로필 목록 조회",
			notes = "알림톡 발송프로필 목록 조회")
	@PostMapping("/phone-api/atalk/tmpl/manage/sendProfileKeys")
	public Object selectSendProfileKeys(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkTmplManageService.selectSendProfileKeys(mjsonParams);
	}
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 발송프로필 목록 조회",
			notes = "알림톡 발송프로필 목록 조회")
	@PostMapping("/phone-api/atalk/tmpl/manage/checktmplCd")
	public Object checktmplCd(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkTmplManageService.checktmplCd(mjsonParams);
	}
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 발송프로필 목록 조회",
			notes = "알림톡 발송프로필 목록 조회")
	@PostMapping("/phone-api/atalk/tmpl/manage/save")
	public Object saveAtalkTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkTmplManageService.saveAtalkTmpl(mjsonParams);
	}
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 수정",
			notes = "알림톡 수정")
	@PostMapping("/phone-api/atalk/tmpl/manage/modify")
	public Object modifyAtalkTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkTmplManageService.modifyAtalkTmpl(mjsonParams);
	}
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 수정",
			notes = "알림톡 수정")
	@PostMapping("/phone-api/atalk/tmpl/manage/delete")
	public Object deleteAtalkTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkTmplManageService.deleteAtalkTmpl(mjsonParams);
	}
	
	/**
     * 알림톡 템플릿 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
            notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/list")
    public Object selectAtalkTmplList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneAtalkTmplManageService.selectAtalkTmplList(mjsonParams);
    }
	
	/**
     * 알림톡 템플릿 상태 업데이트 후 상태 리스트 가져오기
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "알림톡 템플릿 상태 업데이트 후 상태 리스트 가져오기",
            notes = "알림톡 템플릿 상태 업데이트 후 상태 리스트 가져오기")
    @PostMapping("/phone-api/atalk/tmpl/manage/stts-list")
    public Object atalkTmplSttsUpdateAndSelect(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return phoneAtalkTmplManageService.atalkTmplSttsUpdateAndSelect(mjsonParams);
    }
    
    
    /**
     * 알림톡 템플릿 MTS 검수요청
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "업로드 이미지 정보 조회",
    		notes = "업로드 이미지 정보 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/images")
    public Object selectImageList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.selectImageList(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 등록
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsRegister")
    public Object mtsRegister(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsRegister(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 검수요청
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsRequest")
    public Object mtsRequest(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsRequest(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 검수요청 취소
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsCancelRequest")
    public Object mtsCancelRequest(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsCancelRequest(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 승인 취소
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsCancelApproval")
    public Object mtsCancelApproval(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsCancelApproval(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 승인 취소
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsStop")
    public Object mtsStop(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsStop(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 승인 취소
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsReuse")
    public Object mtsReuse(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsReuse(mjsonParams);
    }
    
    /**
     * 알림톡 템플릿 MTS 승인 취소
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "휴면 해제",
    		notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/atalk/tmpl/manage/mtsRelease")
    public Object mtsRelease(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.mtsRelease(mjsonParams);
    }
    
    /**
     * 알림톡 채널 카테고리 리스트 가져오기
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "알림톡 채널 카테고리 리스트 가져오기",
    		notes = "알림톡 채널 카테고리 리스트 가져오기")
    @PostMapping("/phone-api/atalk/getAtalkCat")
    public Object getAtalkCat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.getAtalkCat(mjsonParams);
    }
    
    /**
     * 알림톡 채널 관리자 인증코드 발급
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "알림톡 채널 관리자 인증코드 발급",
    		notes = "알림톡 채널 관리자 인증코드 발급")
    @PostMapping("/phone-api/atalk/getToken")
    public Object getToken(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.getToken(mjsonParams);
    }
    
    /**
     * 알림톡 채널 발급
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "알림톡 채널 발급",
    		notes = "알림톡 채널 발급")
    @PostMapping("/phone-api/atalk/getAtalkChn")
    public Object getAtalkChn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
    	return phoneAtalkTmplManageService.getAtalkChn(mjsonParams); 
    }
}
