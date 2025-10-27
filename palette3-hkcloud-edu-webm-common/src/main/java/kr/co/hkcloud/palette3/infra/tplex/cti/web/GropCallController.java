package kr.co.hkcloud.palette3.infra.tplex.cti.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "GropCallController",
     description = "전화받기")
public class GropCallController
{
    /**
     * 그룹전환로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "그룹전환 페이지",
                  notes = "그룹전환으로 이동한다.")
    @GetMapping("/infra/ics/cti/web/GropCall")
    public String movePhoneCall() throws TelewebWebException
    {
        log.debug("movegropCall");

        return "infra/ics/cti/GroupCall";
    }
}
