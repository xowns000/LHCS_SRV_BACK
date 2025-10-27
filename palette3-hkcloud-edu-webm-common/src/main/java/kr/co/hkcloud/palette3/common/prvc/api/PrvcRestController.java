package kr.co.hkcloud.palette3.common.prvc.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.prvc.app.PrvcService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description : 개인정보 조회 이력
 * package  : kr.co.hkcloud.palette3.common.prvc.api
 * filename : PrvcRestController.java
 * Date : 2023. 9. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 9. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PrvcRestController",
     description = "Prvc REST 컨트롤러")
public class PrvcRestController
{
    private final PrvcService   prvcService;


    /**
     * 
     * 개인정보 조회 이력 -목록
     * @Method Name  	: prvcInqHistList
     * @date   			: 2023. 5. 15.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "개인정보 조회 이력-목록",
                  notes = "개인정보 조회 이력 목록을 조회한다")
    @PostMapping("/api/common/prvc/prvcInqHistList")
    public Object prvcInqHistList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return prvcService.prvcInqHistList(mjsonParams);
    }
}
