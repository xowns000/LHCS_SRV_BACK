package kr.co.hkcloud.palette3.error.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "ExcpController",
     description = "예외 컨트롤러")
public class ExcpController
{
    /**
     * 공통 Excp01 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공통 Excp01 페이지",
                  notes = "공통 Excp01 페이지로 이동한다")
    @RequestMapping("/error/web/excp01")
    public String moveErrorExcp01() throws TelewebWebException
    {
        log.debug("moveErrorExcp01");
        return "error/excp01";
    }


    /**
     * 공통 Excp02 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공통 Excp02 페이지",
                  notes = "공통 Excp02 페이지로 이동한다")
    @RequestMapping("/error/web/excp02")
    public String moveErrorExcp02() throws TelewebWebException
    {
        log.debug("moveErrorExcp02");
        return "error/excp02";
    }


    /**
     * 공통 Excp03 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공통 Excp03 페이지",
                  notes = "공통 Excp03 페이지로 이동한다")
    @RequestMapping("/error/web/excp03")
    public String moveErrorExcp03() throws TelewebWebException
    {
        log.debug("moveErrorExcp03");
        return "error/excp03";
    }


    /**
     * 공통 Excp04 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공통 Excp04 페이지",
                  notes = "공통 Excp04 페이지로 이동한다")
    @RequestMapping("/error/web/excp04")
    public String moveErrorExcp04() throws TelewebWebException
    {
        log.debug("moveErrorExcp04");
        return "error/excp04";
    }


    /**
     * 공통 Excp05 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "공통 Excp05 페이지",
                  notes = "공통 Excp05 페이지로 이동한다")
    @RequestMapping("/error/web/excp05")
    public String moveErrorExcp05() throws TelewebWebException
    {
        log.debug("moveErrorExcp05");
        return "error/excp05";
    }
}
