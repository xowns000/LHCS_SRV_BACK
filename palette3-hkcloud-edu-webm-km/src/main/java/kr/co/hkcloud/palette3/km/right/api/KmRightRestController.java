package kr.co.hkcloud.palette3.km.right.api;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.right.app.KmRightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmRightRestController", description = "우측 메뉴 지식정보 조회 REST 컨트롤러")
public class KmRightRestController {

    private final PaletteJsonUtils paletteJsonUtils;
    //    private final InnbCreatCmmnService innbCreatCmmnService;
    private final KmRightService kmRightService;

    @ApiOperation(value = "지식,스크립트 목록 조회", notes = "지식,스크립트 목록을 조회 한다")
    @PostMapping("/api/km/right/rightContsList")
    public Object rightContsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmRightService.rightContsList(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "지식컨텐츠 트리 조회", notes = "우측 지식컨텐츠 트리를 조회한다")
    @PostMapping("/api/km/right/rightContsTreeList")
    public Object rightContsTreeList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmRightService.rightContsTreeList(mjsonParams);

        List<Map<String, Object>> clsfList = null;
        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_KMS_CLSF_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_KMS_CLSF_ID");
            clsfList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_KMS_CLSF_ID, "KMS_CLSF_ID",
                "UP_KMS_CLSF_ID", "KMS_CLSF_NM", "SORT_ARR");
            objRetParams.setDataObject("KM_CLSF_TREE", paletteJsonUtils.getJsonArrayFromList(clsfList));
        }

        return objRetParams;
    }



    @ApiOperation(value = "사용자 컨텐츠 등록", notes = "사용자 컨텐츠를 등록 한다")
    @PostMapping("/api/km/right/rightRegUserConts")
    public Object rightRegUserConts(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return kmRightService.rightRegUserConts(mjsonParams);
    }

    @ApiOperation(value = "사용자 컨텐츠 삭제", notes = "사용자 컨텐츠를 삭제 한다")
    @PostMapping("/api/km/right/rightDelUserConts")
    public Object rightDelUserConts(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return kmRightService.rightDelUserConts(mjsonParams);
    }

    @ApiOperation(value = "지식 Badge Count 조회", notes = "지식  Badge Count를 조회 한다")
    @PostMapping("/api/km/right/rightContsBadgeConut")
    public Object rightContsBadgeConut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = kmRightService.rightContsBadgeConut(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "선택한 안내(가이드) 정보 조회", notes = "선택한 안내(가이드) 정보를 조회 한다")
    @PostMapping("/api/km/right/searchKeyword")
    public Object searchKeyword(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = kmRightService.searchKeyword(mjsonParams);
        return objRetParams;
    }

    @ApiOperation(value = "지식 콘텐츠 정보 조회", notes = "선택한 지식 콘텐츠 정보(콘텐츠, 관련 콘텐츠, 관련 링크, 첨부 파일을 조회 한다")
    @PostMapping("/api/km/right/rightContsInfo")
    public Object rightContsInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return kmRightService.rightContsInfo(mjsonParams);      // 안내 상담 목록
    }
    
    @ApiOperation(value = "지식스크립트 단축키 merge", notes = "지식스크립트 단축키를 저장/수정/삭제 조회한다")
    @PostMapping("/api/km/right/mergeShortcutKmsScript")
    public Object mergeShortcutKmsScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = kmRightService.mergeShortcutKmsScript(mjsonParams);
        return objRetParams;
    }

}
