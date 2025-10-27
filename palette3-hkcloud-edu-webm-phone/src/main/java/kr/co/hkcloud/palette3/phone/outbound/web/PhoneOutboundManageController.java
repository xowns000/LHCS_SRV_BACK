package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneOutboundManageController",
     description = "아웃바운드관리 컨트롤러")
public class PhoneOutboundManageController
{
    /**
     * 아웃바운드관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드관리 페이지",
                  notes = "아웃바운드관리 이동한다")
    @GetMapping("/phone/outbound/web/manage")
    public String movePhoneOutboundManage() throws TelewebWebException
    {
        log.debug("movePhoneOutboundManage");

        return "phone/outbound/phone-outbound-manage";
    }
}
