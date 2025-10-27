package kr.co.hkcloud.palette3.phone.center.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneCenterScheduleManageController",
     description = "스케쥴관리 컨트롤러")
public class PhoneCenterScheduleManageController
{
    /**
     * 스케쥴관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "스케쥴관리 페이지",
                  notes = "스케쥴관리 이동한다")
    @GetMapping("/phone/center/web/schedule-manage")
    public String moveSchedulMng() throws TelewebWebException
    {
        log.debug("moveSchedulMng");
        return "phone/center/phone-center-schedule-manage";
    }
}
