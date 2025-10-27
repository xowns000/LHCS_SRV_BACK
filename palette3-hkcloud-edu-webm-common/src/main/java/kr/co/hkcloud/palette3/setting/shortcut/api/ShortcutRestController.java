package kr.co.hkcloud.palette3.setting.shortcut.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.shortcut.app.ShortcutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이용자 단축키 설정 및 조회
 * 단축키는 로그인 시, userStore.AC_USER_SHORT_KEY 에서 조회함
 *
 * @author HJH
 * @since 2024-01-10
 * @version 1.0
 * <pre>
 * ===================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023-11-22 KJD 최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ShortcutRestController", description = "사용자 단축키 REST 컨트롤러")
public class ShortcutRestController {
    private final ShortcutService shortcutService;
    
    @ApiOperation(value = "사용자 단축키 목록 조회", notes = "사용자 단축키를 조회한다")
    @PostMapping("/api/system/shortcut/selectShortcutList")
    public Object selectShortcutList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = shortcutService.selectShortcutList(mjsonParams);
        return objRetParams;
    }
    
    
    @ApiOperation(value = "단축키 키 목록 조회", notes = "단축키 키 목록을 조회한다")
    @PostMapping("/api/system/shortcut/selectShortcutKeyList")
    public Object selectShortcutKeyList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = shortcutService.selectShortcutKeyList(mjsonParams);
        return objRetParams;
    }
    
    
    @ApiOperation(value = "단축키 merge", notes = "단축키를 저장/수정/삭제 조회한다")
    @PostMapping("/api/system/shortcut/mergeShortcut")
    public Object mergeShortcut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = shortcutService.mergeShortcut(mjsonParams);
        return objRetParams;
    }
    
    
    @ApiOperation(value = "단축키 삭제", notes = "단축키를 삭제 조회한다")
    @PostMapping("/api/system/shortcut/deleteShortcut")
    public Object deleteShortcut(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = shortcutService.deleteShortcut(mjsonParams);
        return objRetParams;
    }
}
