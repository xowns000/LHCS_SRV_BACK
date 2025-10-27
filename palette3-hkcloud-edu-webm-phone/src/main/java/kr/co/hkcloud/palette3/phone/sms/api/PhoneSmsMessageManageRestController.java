package kr.co.hkcloud.palette3.phone.sms.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sms.app.PhoneSmsMessageManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneSmsMessageManageRestController",
     description = "문자 템플릿 관리 REST 컨트롤러")
public class PhoneSmsMessageManageRestController
{
	private final PhoneSmsMessageManageService phoneSmsMessageManageService;
	
	/**
	 * SMS 템플릿 리스트 조회
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "SMS 템플릿 분류 리스트 조회",
			notes = "SMS 템플릿 분류 리스트 조회")
	@PostMapping("/phone-api/sms/message/manage/tmplClsfList")
	public Object selectSmsTmplClsfList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneSmsMessageManageService.selectSmsTmplClsfList(mjsonParams);
	}
	
    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS 템플릿 리스트 조회",
            notes = "SMS 템플릿 리스트 조회")
    @PostMapping("/phone-api/sms/message/manage/tmplList")
    public Object selectSmsTmplList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
    {
    	return phoneSmsMessageManageService.selectSmsTmplList(mjsonParams);
    }
    
    /**
     * SMS 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "SMS리스트 조회",
            notes = "SMS리스트 조회")
    @PostMapping("/phone-api/sms/message/manage/sendHistory")
    public Object selectSmsSendHistory(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
    {
        return phoneSmsMessageManageService.selectSmsSendHistory(mjsonParams);
    }
    
}
