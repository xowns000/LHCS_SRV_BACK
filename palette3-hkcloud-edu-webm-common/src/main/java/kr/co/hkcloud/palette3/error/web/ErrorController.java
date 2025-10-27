package kr.co.hkcloud.palette3.error.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Api(value = "ErrorController", description = "오류 컨트롤러")
public class ErrorController
{
    /**
     * 오류 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "오류 페이지", notes = "오류 페이지로 이동한다")
    @GetMapping("/error/web/error")
    @PostMapping("/error/web/error")
    public String moveErrorError() throws TelewebWebException
    {
        log.debug("moveErrorError");
        return "error/error";
    }

    @GetMapping("/error")
    public String redirectRoot(){
        return null;
    }
}
