package kr.co.hkcloud.palette3.setting.system.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemAuthorityGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemAuthorityGroupRestController", description = "권한그룹 REST 컨트롤러")
public class SettingSystemAuthorityGroupRestController {

    private final SettingSystemAuthorityGroupService settingSystemAuthorityGroupService;


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-AUTHRT-MNG_DEL", note = "설정 메뉴관리 권한그룹 삭제")
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹삭제", notes = "설정시스템메뉴권한관리 권한그룹을 삭제한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group/delete")
    public Object deleteRtnAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityGroupService.deleteRtnAuthGroup(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹목록", notes = "설정시스템메뉴권한관리 권한그룹목록을 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group/inqire")
    public Object selectTwbBas05(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityGroupService.selectTwbBas05(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-AUTHRT-MNG_PROC", note = "설정 메뉴관리 권한그룹 변경(등록,수정)")
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹등록", notes = "설정시스템메뉴권한관리 권한그룹을 등록한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group/regist")
    public Object insertTwbBas05(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityGroupService.insertTwbBas05(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-AUTHRT-MNG_PROC", note = "설정 메뉴관리 권한그룹 변경(등록,수정)")
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹수정", notes = "설정시스템메뉴권한관리 권한그룹을 수정한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group/modify")
    public Object updateTwbBas05(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityGroupService.updateTwbBas05(mjsonParams);
    }
}
