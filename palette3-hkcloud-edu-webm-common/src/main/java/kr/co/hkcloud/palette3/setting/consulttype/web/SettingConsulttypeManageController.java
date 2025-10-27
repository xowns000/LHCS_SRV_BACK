package kr.co.hkcloud.palette3.setting.consulttype.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingConsulttypeManageController",
     description = "상담유형관리 컨트롤러")
public class SettingConsulttypeManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정상담유형관리-화면",
                  notes = "설정상담유형관리 화면으로 이동한다")
    @GetMapping("/setting/consulttype/web/manage")
    public String moveSettingConsulttypeManage() throws TelewebWebException
    {
        log.debug("moveSettingConsulttypeManage");
        return "setting/consulttype/setting-consulttype-manage";
    }
}
