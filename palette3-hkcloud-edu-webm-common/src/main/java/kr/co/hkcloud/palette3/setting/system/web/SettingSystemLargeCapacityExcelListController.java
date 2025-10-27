package kr.co.hkcloud.palette3.setting.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingSystemLargeCapacityExcelListController",
     description = "설정시스템대용량엑셀목록-화면 컨트롤러")
public class SettingSystemLargeCapacityExcelListController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템대용량엑셀목록-화면 페이지",
                  notes = "설정시스템대용량엑셀목록-화면로 이동한다")
    @GetMapping("/setting/system/web/lrge-cpcty-excel-list")
    public String moveSettingSystemLargeCapacityExcelList() throws TelewebWebException
    {
        log.debug("moveSettingSystemLargeCapacityExcelList");
        return "setting/system/setting-system-large-capacity-excel-list";
    }
}
