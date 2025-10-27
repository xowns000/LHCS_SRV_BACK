package kr.co.hkcloud.palette3.phone.history.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "PhoneHistoryChangeManageController",
     description = "상담이력변경관리 컨트롤러")
public class PhoneHistoryChangeManageController
{
    /**
     * 상담이력변경관리 페이지 이동(공제지원)
     * 
     * @return
     */
    @ApiOperation(value = "상담이력변경관리 페이지",
                  notes = "상담이력변경관리 이동한다")
    @GetMapping("/phone/history/web/change-manage")
    public String MoveCnslHistChngMngDe() throws TelewebWebException
    {
        log.debug("MoveCnslHistChngMngDe");
        return "phone/history/phone-history-change-manage";
    }
}
