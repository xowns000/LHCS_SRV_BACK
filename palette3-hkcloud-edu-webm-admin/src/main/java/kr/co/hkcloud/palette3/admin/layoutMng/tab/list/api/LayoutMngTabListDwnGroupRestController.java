package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.api;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListDwnGroupService;
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
 * date           : 2024-04-01
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-01           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LayoutMngTabListDwnGroupRestController {
    private final LayoutMngTabListDwnGroupService dwnGroupService;

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 하위그룹 추가")
    @PostMapping("/admin-api/layoutMng/tab/list/dwnGroup/insertUpdateListGroup")
    public Object insertUpdateListGroup(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 고객사 테넌트 설정
        TelewebJSON objRetParam = dwnGroupService.insertUpdateDwnGroup(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 하위그룹 삭제")
    @PostMapping("/admin-api/layoutMng/tab/list/dwnGroup/deleteListGroup")
    public Object deleteListGroup(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 고객사 테넌트 설정
        TelewebJSON objRetParam = dwnGroupService.deleteDwnGroup(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 하위그룹 상세 단건 삭제")
    @PostMapping("/admin-api/layoutMng/tab/list/dwnGroup/deleteListGroupDtl")
    public Object deleteListGroupDtl(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 고객사 테넌트 설정
        TelewebJSON objRetParam = dwnGroupService.deleteDwnGroupDtl(jsonParam);
        return objRetParam;
    }
}
