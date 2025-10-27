package kr.co.hkcloud.palette3.qa.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAEvlRsltManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController("QAEvlRsltManageRestController")
@Api(value = "QAEvlRsltManageRestController",
description = "QA 평가 결과 관리 REST 컨트롤러")
public class QAEvlRsltManageRestController {

	private final QAEvlRsltManageService qaEvlRsltManageService;

	@ApiOperation(value = "QA평가결과관리",
				  notes = "QA평가관리자목록")
	@PostMapping("/api/qa/evlrslt/mngrlist")
	Object getEvlMngrList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlRsltManageService.selectQaEvlRsltMngrList(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "QA평가결과관리",
			  	  notes = "QA평가대상자목록")
	@PostMapping("/api/qa/evlrslt/trgtlist")
	Object getEvlTrgtList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlRsltManageService.selectQaEvlRsltTrgtList(mjsonParams);
		return objRetParams;
	}
}