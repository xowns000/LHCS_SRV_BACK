package kr.co.hkcloud.palette3.qa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAEvlManageService;
import kr.co.hkcloud.palette3.qa.app.QAQltyClsfManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("QAEvlManageRestController")
@Api(value = "QAEvlManageRestController", description = "QA 평가 시행 관리 REST 컨트롤러")
public class QAEvlManageRestController {

    private final QAEvlManageService qaEvlManageService;
    private final QAQltyClsfManageService qAQltyClsfManageService;

    @SystemEventLogAspectAnotation(value = "COM_QA-EVLMNG_PROC", note = "QA평가시행관리 평가표 변경(등록,수정)", isSaveParameter = false)
    @ApiOperation(value = "QA평가시행관리", notes = "QA평가표 저장")
    @PostMapping("/api/qa/evlmng/save")
    Object insertQaMngr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaEvlManageService.insertQaPlanQlty(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가시행관리", notes = "평가지표 불러오기")
    @PostMapping("/api/qa/evlmng/artcllist")
    Object selectQAQltyEvlArtclListWithClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qAQltyClsfManageService.selectQaQltyEvlArtclListWithClsf(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가시행관리", notes = "평가표불러오기")
    @PostMapping("/api/qa/evlmng/planqltylist")
    Object selectQAPlanQltyList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qAQltyClsfManageService.selectQaPlanQltyList(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가시행관리", notes = "평가대상선정-상담이력불러오기")
    @PostMapping("/api/qa/evlmng/cuttlist")
    Object selectQaTrgtSlctnList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

		if (mjsonParams.getString("DATE_FROM") != null && !mjsonParams.getString("DATE_FROM").isEmpty()) {
			mjsonParams.setString("DATE_FROM", mjsonParams.getString("DATE_FROM").replaceAll("-", ""));
		}
		if (mjsonParams.getString("DATE_TO") != null && !mjsonParams.getString("DATE_TO").isEmpty()) {
			mjsonParams.setString("DATE_TO", mjsonParams.getString("DATE_TO").replaceAll("-", ""));
		}
		if (mjsonParams.getString("PHN_HR") != null && !mjsonParams.getString("PHN_HR").isEmpty()) {
			mjsonParams.setString("PHN_HR", String.valueOf(Integer.valueOf(mjsonParams.getString("PHN_HR")) * 60));
		}

        objRetParams = qaEvlManageService.selectQaTrgtSlctnList(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가시행관리", notes = "평가대상할당-할당내역불러오기")
    @PostMapping("/api/qa/evlmng/almntlist")
    Object selectQaTrgtAlmntList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaEvlManageService.selectQaTrgtAlmntList(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가시행관리", notes = "평가대상할당-대상자 할당")
    @PostMapping("/api/qa/evlmng/execalmnt")
    Object setQaTrgtAlnmnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //objRetParams = qaEvlManageService.selectQaTrgtAlmntList(mjsonParams);
        log.debug("mjsonParams {}", mjsonParams.toString());
        //
        //		String assignType = mjsonParams.getString("TYPE");
        //
        //	    switch(assignType) {
        //	    	case "EQUAL":
        //	        	log.debug("assignType.switch1 {}", assignType);
        //	        	break;
        //	        case "ADD":
        //	        	log.debug("assignType.switch2 {}", assignType);
        //	        	break;
        //	        case "WITHDRAW":
        //	        	log.debug("assignType.switch3 {}", assignType);
        //	        	break;
        //	        case "WITHDRAWALL":
        //	        	log.debug("assignType.switch4 {}", assignType);
        //	        	break;
        //	        default :
        //	        	log.debug("assignType.switch5 {}", assignType);
        //	      };
        //
        //		return mjsonParams;
        return qaEvlManageService.execQaTrgtAlmnt(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-EVLMNG_EXESLCTN", note = "QA평가시행관리 평가대상할당-대상자 선정")
    @ApiOperation(value = "QA평가시행관리", notes = "평가대상할당-대상자 선정")
    @PostMapping("/api/qa/evlmng/execslctn")
    Object setQaTrgtSlctn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("mjsonParams %s", mjsonParams.toString());
        return qaEvlManageService.insertQaTrgtSlctn(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-EVLMNG_SLCTN_DEL", note = "QA평가시행관리 평가대상할당-대상자 삭제")
    @ApiOperation(value = "QA평가시행관리", notes = "평가대상할당-대상자 삭제")
    @PostMapping("/api/qa/evlmng/delslctn")
    Object removeQaTrgtSlctn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("mjsonParams %s", mjsonParams.toString());
        return qaEvlManageService.deleteQaTrgtSlctn(mjsonParams);
    }
}