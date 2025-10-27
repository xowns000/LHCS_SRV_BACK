package kr.co.hkcloud.palette3.admin.layoutMng.tab.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.service.LayoutMngTabService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.api
 * fileName       : LayoutMngTabRestController
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
@Api(value = "LayoutMngTabRestController", description = "레이아웃 하위목록 관리 REST 컨트롤러")
public class LayoutMngTabRestController {
    private final LayoutMngTabService layoutMngTabService;

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 조회")
    @PostMapping("/admin-api/layoutMng/tab/selectTabList")
    public Object selectTabs(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngTabService.selectTabList(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 등록 또는 수정")
    @PostMapping("/admin-api/layoutMng/tab/insertUpdateTab")
    public Object insertUpdateTab(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngTabService.insertUpdateTab(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 등록")
    @PostMapping("/admin-api/layoutMng/tab/deleteTab")
    public Object deleteTab(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = layoutMngTabService.deleteTab(jsonParam);
        return objRetParam;
    }
}
