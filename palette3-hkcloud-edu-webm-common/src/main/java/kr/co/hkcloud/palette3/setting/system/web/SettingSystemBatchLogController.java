package kr.co.hkcloud.palette3.setting.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingSystemBatchLogController",
     description = "설정시스템배치관리-화면 컨트롤러")
public class SettingSystemBatchLogController
{
    /**
     * 
     * 배치로그현황
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템배치로그-화면",
                  notes = "설정시스템배치관리 페이지로 이동한다")
    @GetMapping("/setting/system/web/batch-log")
    public String moveSettingSystemBatchLog() throws TelewebWebException
    {
        log.debug("moveSettingSystemBatchLog");
        return "setting/system/setting-system-batch-log";
    }
}
