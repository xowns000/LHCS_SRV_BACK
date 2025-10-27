package kr.co.hkcloud.palette3.setting.holiday.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingHolidayManageController",
     description = "휴일관리 컨트롤러")
public class SettingHolidayManageController
{
    /**
     * 설정휴일관리 화면으로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "설정휴일관리-화면",
                  notes = "설정휴일관리 화면으로 이동한다")
    @GetMapping("/setting/holiday/web/manage")
    public String moveSettingHolidayManage() throws TelewebWebException
    {
        log.debug("moveSettingHolidayManage");
        return "setting/holiday/setting-holiday-manage";
    }
}
