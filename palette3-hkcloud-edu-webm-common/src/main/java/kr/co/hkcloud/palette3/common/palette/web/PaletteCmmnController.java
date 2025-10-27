package kr.co.hkcloud.palette3.common.palette.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "PaletteCmmnController",
     description = "팔레트공통 컨트롤러")
public class PaletteCmmnController
{
    /**
     * 공란 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공란 페이지",
                  notes = "공란 페이지로 이동한다")
    @GetMapping("/palette/common/web/palette-cmmn-blnk")
    public String moveBlnk() throws TelewebWebException
    {
        log.debug("moveBlnk");
        return "palette/common/palette-cmmn-blnk";
    }
}
