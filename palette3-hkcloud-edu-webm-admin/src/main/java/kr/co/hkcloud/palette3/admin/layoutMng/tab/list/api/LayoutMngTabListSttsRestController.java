package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.api;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListSttsService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
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
public class LayoutMngTabListSttsRestController {
    private final LayoutMngTabListSttsService sttsService;

    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 썸네일 조회")
    @PostMapping("/admin-api/layoutMng/tab/list/stts/selectStts")
    public Object selectThumnail(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = sttsService.selectStts(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 썸네일 생성/수정")
    @PostMapping("/admin-api/layoutMng/tab/list/stts/insertUpdateStts")
    public Object insertUpdateThumnail(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = sttsService.insertUpdateStts(jsonParam);
        return objRetParam;
    }
    @ApiOperation(value = "", notes = "서비스 연동관리 - 레이아웃 관리 - 레이아웃 하위목록 설정 - 탭 - 썸네일 삭제")
    @PostMapping("/admin-api/layoutMng/tab/list/stts/deleteStts")
    public Object deleteThumnail(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParam = sttsService.deleteStts(jsonParam);
        return objRetParam;
    }

}
