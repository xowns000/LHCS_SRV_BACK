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
import kr.co.hkcloud.palette3.phone.sms.app.PhoneAtalkMessageManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneAtalkMessageManageRestController",
     description = "알림톡 메시지 관리 REST 컨트롤러")
public class PhoneAtalkMessageManageRestController
{
	private final PhoneAtalkMessageManageService phoneAtalkMessageManageService;
	
	/**
	 * 알림톡 발송프로필 목록 조회
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 발송 이력 조회",
			notes = "알림톡 발송 이력 조회")
	@PostMapping("/phone-api/atalk/message/manage/atalkSendHistory")
	public Object atalkSendHistory(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkMessageManageService.atalkSendHistory(mjsonParams);
	}
	
	/**
	 * 알림톡 발송프로필 목록 조회
	 *
	 * @param  mjsonParams
	 * @return
	 */
	@ApiOperation(value = "알림톡 템플릿 목록 조회",
			notes = "알림톡 템플릿 목록 조회")
	@PostMapping("/phone-api/atalk/message/manage/atalkTmpls")
	public Object atalkTmpls(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, URISyntaxException, UnsupportedEncodingException 
	{
		return phoneAtalkMessageManageService.atalkTmpls(mjsonParams);
	}
	
    
}
