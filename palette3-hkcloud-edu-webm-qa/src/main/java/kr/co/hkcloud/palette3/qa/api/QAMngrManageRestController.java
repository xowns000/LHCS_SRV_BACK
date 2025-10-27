package kr.co.hkcloud.palette3.qa.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.qa.app.QAMngrManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("QAMngrManageRestController")
@Api(value = "QAMngrManageRestController", description = "QA 평가 관리자 지정 REST 컨트롤러")
public class QAMngrManageRestController {

    private final QAMngrManageService qaMngrManageService;

    @ApiOperation(value = "QA평가관리자지정", notes = "관리자 지정을 위한 사용자 목록")
    @PostMapping("/api/qa/mngrmng/userlist")
    Object selectUserList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaMngrManageService.selectUserList(mjsonParams);
        return objRetParams;
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-MNGRMNG_PROC", note = "QA평가시행관리 관리자 변경(등록,수정)")
    @ApiOperation(value = "QA평가관리자지정", notes = "QA평가 관리자 등록")
    @PostMapping("/api/qa/mngrmng/regist")
    Object insertQaMngr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaMngrManageService.insertQaMngr(mjsonParams);
        return objRetParams;
    }

    @SystemEventLogAspectAnotation(value = "COM_QA-MNGRMNG_DEL", note = "QA평가시행관리 관리자 삭제")
    @ApiOperation(value = "QA평가관리자지정", notes = "QA평가 관리자 삭제")
    @PostMapping("/api/qa/mngrmng/remove")
    Object deleteQaMngr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaMngrManageService.deleteQaMngr(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "QA평가관리자지정", notes = "QA평가 관리자 목록")
    @PostMapping("/api/qa/mngrmng/mngrlist")
    Object selectQaMngrList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = qaMngrManageService.selectMngrList(mjsonParams);
        return objRetParams;
    }
}