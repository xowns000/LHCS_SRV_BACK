package kr.co.hkcloud.palette3.palette.main.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "MainController",
     description = "메인 컨트롤러")
public class PaletteMainController
{
    private final PaletteProperties paletteProperties;


    /**
     * /main/hkcdv/web/TwbMain moveTwbMain
     * 
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "메인 페이지",
                  notes = "메인으로 이동한다")
    @GetMapping("/palette/web/main")
    public String moveMain(Model model) throws TelewebWebException
    {
//        final String mainPage = palettePropertyConfig.getThymleafMainPath();
        final String mainPage = "palette/main/palette-main";
        log.debug("moveMain ::: {}", mainPage);

        model.addAttribute("useAsp", paletteProperties.getAsp().isEnabled());
        model.addAttribute("isMainNoticeEnabled", paletteProperties.isMainNoticeEnabled());
        model.addAttribute("timestamp", DateCmmnUtils.toEpochMilli());
        return mainPage;
    }
}
