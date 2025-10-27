package kr.co.hkcloud.palette3.km.conts.api;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.conts.app.KmContsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmContsRestController", description = "지식 콘텐츠  REST 컨트롤러")
public class KmContsRestController {

    private final KmContsService kmContsService;

    @ApiOperation(value = "지식컨텐츠 목록 조회", notes = "지식컨텐츠 목록을 조회한다")
    @PostMapping("/api/km/conts/selectContentList")
    public Object selectContentList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.selectContentList(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "지식컨텐츠 단건 조회", notes = "지식컨텐츠 단건을 조회한다")
    @PostMapping("/api/km/conts/selectContent")
    public Object selectContent(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.selectContent(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "지식컨텐츠 등록 OR 수정", notes = "지식컨텐츠를 등록, 수정 한다")
    @PostMapping("/api/km/conts/mergeContent")
    public Object mergeContent(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return kmContsService.mergeContent(mjsonParams);
    }
    
    
    @ApiOperation(value = "지식컨텐츠 수정", notes = "지식컨텐츠를 수정 한다")
    @PostMapping("/api/km/conts/updateContent")
    public Object updateContent(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return kmContsService.updateContent(mjsonParams);
    }

    @ApiOperation(value = "지식 컨텐츠 상태 변경", notes = "지식 컨텐츠 상태를 변경 한다")
    @PostMapping("/api/km/conts/contsProcStts")
    public Object contsProcStts(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return kmContsService.contsProcStts(mjsonParams);
    }

    @ApiOperation(value = "지식컨텐츠 승인관리 목록 조회", notes = "지식컨텐츠 승인관리 목록을 조회한다")
    @PostMapping("/api/km/conts/kmContsApprManageList")
    public Object kmContsApprManageList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.kmContsApprManageList(mjsonParams);
        return objRetParams;
    }
    
    @ApiOperation(value = "지식 콘텐츠 표시 순서 재정렬", notes = "선택된 지식 콘텐츠 이후의 콘텐츠들의 표시 순서를 업데이트한다.")
    @PostMapping("/api/km/conts/updateContentSortOrd")
    public Object updateContentSortOrd(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.updateContentSortOrd(mjsonParams);
        return objRetParams;
    }
    
    @ApiOperation(value = "지식 콘텐츠 검토 이력 목록 조회", notes = "선택된 지식 콘텐츠의 검토 이력 목록을 조회한다.")
    @PostMapping("/api/km/conts/selectReviewHistoryList")
    public Object selectReviewHistoryList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.selectReviewHistoryList(mjsonParams);
        return objRetParams;
    }
    
    
    @ApiOperation(value = "새 표시 순서 값 조회", notes = "지식 콘텐츠 등록 시 표시 순서 자동 배정을 위해 새 표시 순서 값을 조회한다.")
    @PostMapping("/api/km/conts/selectNewSortOrd")
    public Object selectNewSortOrd(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmContsService.selectNewSortOrd(mjsonParams);
        return objRetParams;
    }

}
