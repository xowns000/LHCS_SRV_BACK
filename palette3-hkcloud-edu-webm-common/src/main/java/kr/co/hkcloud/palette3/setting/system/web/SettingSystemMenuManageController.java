package kr.co.hkcloud.palette3.setting.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingSystemMenuManageController",
     description = "설정시스템메뉴관리 화면 컨트롤러")
public class SettingSystemMenuManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴관리-화면",
                  notes = "설정시스템메뉴관리 화면으로 이동한다")
    @GetMapping("/setting/system/web/menu-manage")
    public String moveSettingSystemMenuManage() throws TelewebWebException
    {
        log.debug("moveSettingSystemMenuManage");
        return "setting/system/setting-system-menu-manage";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "프로그램경로조회 팝업 페이지",
                  notes = "프로그램경로조회 팝업 페이지로 이동한다")
    @GetMapping("/setting/system/web/menu-manage/progrm-path-inqire-popup")
    public String moveSettingSystemMenuManageProgramPathInqirePopup() throws TelewebWebException
    {
        log.debug("moveSettingSystemMenuManageProgramPathInqirePopup");
        return "setting/system/setting-system-menu-manage-progrm-path-inqire-popup";
    }
}
