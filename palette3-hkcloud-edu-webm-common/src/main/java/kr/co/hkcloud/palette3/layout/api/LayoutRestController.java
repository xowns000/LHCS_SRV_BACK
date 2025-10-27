package kr.co.hkcloud.palette3.layout.api;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.layout.api
 * fileName       :
 * author         : njy
 * date           : 2024-04-17
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-17           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LayoutRestController {
    private final LayoutService layoutService;
    @ApiOperation(value = "레이아웃 조회", notes = "레이아웃을 조회한다.")
    @PostMapping("/api/common/layout/selectLayout")
    public Object selectLayout(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = layoutService.selectLayout(jsonParams);
        return objRetParams;
    }

}
