package kr.co.hkcloud.palette3.setting.agent.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "SettingAgentManageController", description = "사용자관리 컨트롤러")
public class SettingAgentManageController
{
    private final PaletteProperties paletteProperties;


    /**
     * 사용자관리 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "사용자관리 페이지", notes = "사용자관리 페이지로 이동한다")
    @GetMapping("/setting/agent/web/manage")
    public String moveSettingAgentManageController(Model model) throws TelewebWebException
    {
        log.debug("moveSettingAgentManageController");

        final boolean useAsp = paletteProperties.getAsp().isEnabled();
        model.addAttribute("useAsp", useAsp);
        return "setting/agent/setting-agent-manage";
    }


    /**
     * 사용자관리 사용자정보조회 팝업 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "사용자관리 팝업 페이지", notes = "사용자관리 팝업 페이지로 이동한다")
    @GetMapping("/setting/agent/web/manage/user-info-inqire-popup")
    public String moveSettingAgentManageUserInfoInqirePopupController(Model model) throws TelewebWebException
    {
        log.debug("moveSettingAgentManageUserInfoInqirePopupController");

        final boolean useAsp = paletteProperties.getAsp().isEnabled();
        model.addAttribute("useAsp", useAsp);
        return "setting/agent/setting-agent-manage-user-info-inqire-popup";
    }


    /**
     * 사용자관리 상담원정보조회 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "상담원정보조회 팝업 페이지", notes = "상담원정보조회 팝업 페이지로 이동한다")
    @GetMapping("/setting/agent/web/manage/agent-info-inqire-popup")
    public String moveSettingAgentManageAgentInfoInqirePopup(Model model) throws TelewebWebException
    {
        log.debug("moveSettingAgentManageAgentInfoInqirePopup");

        final boolean useAsp = paletteProperties.getAsp().isEnabled();
        model.addAttribute("useAsp", useAsp);
        return "setting/agent/setting-agent-manage-agent-info-inqire-popup";
    }
}
