package kr.co.hkcloud.palette3.admin.layoutMng.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.layoutMng.service.LayoutMngBscInfoService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.api
 * fileName       : LayoutMngBscInfoRestController
 * author         : njy
 * date           : 2024-03-20
 * description    : 레이아웃 관리 REST 컨트롤러
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20           njy            최초 생성
 *
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "LayoutMngBscInfoRestController", description = "레이아웃 관리 REST 컨트롤러")
public class LayoutMngBscInfoRestController {

    private final LayoutMngBscInfoService layoutMngBscInfoService;

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 리스트 조회")
    @PostMapping("/admin-api/layoutMng/bscInfo/getLayoutList")
    public Object selectLayoutList(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.selectLayoutList(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 분류 조회")
    @PostMapping("/admin-api/layoutMng/bscInfo/getLayoutClsfList")
    public Object selectLayoutClsfList(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.selectLayoutClsfList(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 고객사 정보 조회")
    @PostMapping("/admin-api/layoutMng/bscInfo/getCertCustcoInfo")
    public Object selectCertCustcoInfo4Layout(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.selectCertCustcoInfo4Layout(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 고객사별 연동정보 조회")
    @PostMapping("/admin-api/layoutMng/bscInfo/selectLkagByCertCustco")
    public Object selectLkagByCertCustco(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.selectLkagByCertCustco(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 등록")
    @PostMapping("/admin-api/layoutMng/bscInfo/insertUpdateBscInfo")
    public Object insertUpdateBscInfo(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.insertUpdateBscInfo(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 수정")
    @PostMapping("/admin-api/layoutMng/bscInfo/updateBscInfo")
    public Object updateBscInfo(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        //TODO 하위 기능 완성 후 레이아웃 수정, 삭제 기능 구현
        TelewebJSON objRetParam = layoutMngBscInfoService.insertUpdateBscInfo(jsonParams);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 삭제")
    @PostMapping("/admin-api/layoutMng/bscInfo/deleteLayout")
    public Object deleteLayout(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.deleteBscInfo(jsonParams);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 사용 설정")
    @PostMapping("/admin-api/layoutMng/bscInfo/updateUseLayout")
    public Object updateUseLayout(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngBscInfoService.updateUseLayout(jsonParams);
        return objRetParam;
    }
}
