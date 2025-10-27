package kr.co.hkcloud.palette3.qa.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAQltyClsfManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController("QAQltyClsfManageRestController")
@Api(value = "QAQltyClsManageRestController",
description = "QA평가지표관리 REST 컨트롤러")
public class QAQltyClsfManageRestController {

	private final QAQltyClsfManageService qAQltyClsfManageService;

	@ApiOperation(value = "QA평가지표관리",
			notes = "QA평가지표 트리 조회")
	@PostMapping("/api/qa/qltyclsf/tree-list")
	Object selectQAQltyClsfTree(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.selectQaQltyClsfTree(mjsonParams);
		return objRetParams;
	}

	@ApiOperation(value = "QA평가지표관리",
			notes = "QA 품질지표 분류 등록")
	@PostMapping("/api/qa/qltyclsf/regist")
	Object insertQAQltyClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.insertQaQltyClsf(mjsonParams);
		return objRetParams;
	}

	@ApiOperation(value = "QA평가지표관리",
			notes = "QA 품질지표 분류 삭제")
	@PostMapping("/api/qa/qltyclsf/remove")
	Object deleteQAQltyClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.deleteQaQltyClsf(mjsonParams);
		return objRetParams;
	}	

	@ApiOperation(value = "QA평가지표관리",
			notes = "품질 지표 목록")
	@PostMapping("/api/qa/qltyevlartcl/list")
	Object selectQAQltyEvlArtclList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.selectQaQltyEvlArtcl(mjsonParams);
		return objRetParams;
	}

	@ApiOperation(value = "QA평가지표관리",
			notes = "품질 지표 등록")
	@PostMapping("/api/qa/qltyevlartcl/regist")
	Object insertQAQltyEvlArtcl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.insertQaQltyEvlArtcl(mjsonParams);
		return objRetParams;
	}

	@ApiOperation(value = "QA평가지표관리",
			notes = "품질 지표 삭제")
	@PostMapping("/api/qa/qltyevlartcl/remove")
	Object deleteQAQltyEvlArtcl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.deleteQaQltyEvlArtcl(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "QA평가지표관리",
			notes = "QA 시행 구분 코드 조회")
	@PostMapping("/api/qa/qltyclsf/code")
	Object selectQAQltyClsfCode(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.selectQaQltyClsfCode(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "QA평가지표관리",
			notes = "QA품질기준 순서 변경")
	@PostMapping("/api/qa/qltyclsf/clsfreorder")
	Object reOrderQltyClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = qAQltyClsfManageService.reOrderQltyClsf(mjsonParams);
		return objRetParams;
	}
}
