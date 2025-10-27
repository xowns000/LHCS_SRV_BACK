package kr.co.hkcloud.palette3.qa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAEvlEnforceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("QAEvlMEnforceRestController")
@Api(value = "QAEvlEnforceRestController",
description = "QA 평가 시행 REST 컨트롤러")
public class QAEvlEnforceRestController {

	private final QAEvlEnforceService qaEvlEnforceService;

	@ApiOperation(value = "QA평가시행",
				  notes = "QA평가대상자 목록")
	@PostMapping("/api/qa/evlefc/list")
	Object getEvlTrgtList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlEnforceService.selectQaEnforceList(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "QA평가시행",
			  	  notes = "QA평가표")
	@PostMapping("/api/qa/evlefc/getsheet")
	Object getEvlSheet(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlEnforceService.selectQaEvlSheet(mjsonParams);
		return objRetParams;
	}
	@ApiOperation(value = "QA평가시행",
		  	  	  notes = "QA 품질 평가 저장")
	@PostMapping("/api/qa/evlefc/setrslt")
	Object setEvlRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlEnforceService.setEvlRslt(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "QA평가시행",
	  	  	  	  notes = "QA 품질 평가 완료 (특정차수)")
	@PostMapping("/api/qa/evlefc/evlcomplete")
	Object setEvlComplete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qaEvlEnforceService.setCyclEvlComplete(mjsonParams);
		return objRetParams;
	}
}