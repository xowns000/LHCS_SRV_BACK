package kr.co.hkcloud.palette3.setting.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "TwbBas03Controller",
     description = "설정시스템공통코드관리 컨트롤러")
public class SettingSystemCommonCodeManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템공통코드관리-화면",
                  notes = "설정시스템공통코드관리 화면으로 이동한다")
    @GetMapping("/setting/system/web/cmmn-code-manage")
    public String moveSettingSystemCommonCodeManage() throws TelewebWebException
    {
        log.debug("moveSettingSystemCommonCodeManage");
        return "setting/system/setting-system-common-code-manage";
    }
}
