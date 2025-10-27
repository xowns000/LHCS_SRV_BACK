package kr.co.hkcloud.palette3.setting.agent.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingAgentBelongItemManageController",
     description = "설정사용자소속항목관리 화면 컨트롤러")
public class SettingAgentBelongItemManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-화면",
                  notes = "설정사용자소속항목관리 화면으로 이동한다")
    @GetMapping("/setting/agent/web/psitn-iem-manage")
    public String moveSettingAgentBelongItemManage() throws TelewebWebException
    {
        log.debug("moveSettingAgentBelongItemManage");
        return "setting/agent/setting-agent-belong-item-manage";
    }
}
