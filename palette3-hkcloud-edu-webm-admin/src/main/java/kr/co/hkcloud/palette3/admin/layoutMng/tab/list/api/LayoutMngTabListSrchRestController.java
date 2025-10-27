package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.api;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListSrchService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.list.api
 * fileName       :
 * author         : njy
 * date           : 2024-03-28
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-28           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LayoutMngTabListSrchRestController {
    private final LayoutMngTabListSrchService layoutMngTabListSrchService;
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 탭 - 검색 목록 조회")
    @PostMapping("/admin-api/layoutMng/tab/list/srch/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID"));
        TelewebJSON objRetParam = layoutMngTabListSrchService.selectSrch(jsonParam);
        return objRetParam;
    }

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 탭 - 사용 가능 검색 파라미터 조회")
    @PostMapping("/admin-api/layoutMng/tab/list/srch/selectUsableParam")
    public Object selectUsableParam(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID"));
        TelewebJSON objRetParam = layoutMngTabListSrchService.selectUsableParams(jsonParam);
        return objRetParam;
    }


    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 -  탭 - 검색등록/업데이트")
    @PostMapping("/admin-api/layoutMng/tab/list/srch/insertUpdateSrch")
    public Object insertUpdateSrch(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 고객사 테넌트 설정
        TelewebJSON objRetParam = layoutMngTabListSrchService.insertUpdateSrch(jsonParam);
        return objRetParam;
    }

}
