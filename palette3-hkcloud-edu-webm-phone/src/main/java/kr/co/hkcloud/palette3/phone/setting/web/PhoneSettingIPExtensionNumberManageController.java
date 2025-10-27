package kr.co.hkcloud.palette3.phone.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "PhoneSettingIPExtensionNumberManageController",
     description = "ip내선번호관리 컨트롤러")
public class PhoneSettingIPExtensionNumberManageController
{
    /**
     * ip내선번호관리 페이지 이동
     * 
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 페이지",
                  notes = "ip내선번호관리 페이지로 이동한다")
    @GetMapping("/phone/setting/web/ip-lxtn-no-manage")
    public String movePhoneSettingIPExtensionNumberManage() throws TelewebWebException
    {
        log.debug("movePhoneSettingIPExtensionNumberManage");
        return "phone/setting/ip-lxtn-no-manage";
    }
}
