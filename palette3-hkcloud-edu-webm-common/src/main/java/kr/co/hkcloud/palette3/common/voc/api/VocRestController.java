package kr.co.hkcloud.palette3.common.voc.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.voc.app.VocService;
import kr.co.hkcloud.palette3.common.voc.util.VocValidator;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description : VOC Rest Controller
 * package  : kr.co.hkcloud.palette3.common.voc.api
 * filename : VocRestController.java
 * Date : 2023. 6. 14.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 14., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "VocRestController",
     description = "VOC REST 컨트롤러")
public class VocRestController
{
    private final VocService   vocService;
    private final VocValidator vocValidator;


    /**
     * 
     * VOC -목록
     * @Method Name  	: vocList
     * @date   			: 2023. 5. 15.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "VOC-목록",
                  notes = "VOC 목록을 조회한다")
    @PostMapping("/api/common/voc/vocList")
    public Object vocList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return vocService.vocList(mjsonParams);
    }


    /**
     * 
     * VOC-등록, 수정
     * @Method Name  	: vocProc
     * @date   			: 2023. 5. 16.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "VOC-등록, 수정",
                  notes = "VOC 등록 및 수정을 한다")
    @PostMapping("/api/common/voc/vocProc")
    public Object vocProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return vocService.vocProc(mjsonParams);
    }
}
