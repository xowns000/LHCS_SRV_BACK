package kr.co.hkcloud.palette3.phone.script.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneScriptChangeManageController",
     description = "스크립트변경관리 컨트롤러")
public class PhoneScriptChangeManageController
{
    /**
     * 스크립트변경관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "스크립트변경관리 페이지",
                  notes = "스크립트변경관리 이동한다")
    @GetMapping("/phone/script/web/change-manage")
    public String moveScrtChngMng() throws TelewebWebException
    {
        log.debug("moveScrtChngMng");
        return "phone/script/phone-script-change-manage";
    }
}
