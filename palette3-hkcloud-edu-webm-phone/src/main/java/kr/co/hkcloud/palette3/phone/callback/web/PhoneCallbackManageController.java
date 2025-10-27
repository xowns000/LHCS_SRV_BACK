package kr.co.hkcloud.palette3.phone.callback.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "PhoneCallbackManageController",
     description = "콜백 관리 컨트롤러")
public class PhoneCallbackManageController
{
    /**
     * 콜백 관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "콜백 관리 페이지",
                  notes = "콜백 관리 페이지로 이동한다")
    @GetMapping("/phone/callback/web/manage")
    public String moveClbkMng() throws TelewebWebException
    {
        log.debug("movePhoneCallbackList");
        return "phone/callback/phone-callback-manage";
    }

}
