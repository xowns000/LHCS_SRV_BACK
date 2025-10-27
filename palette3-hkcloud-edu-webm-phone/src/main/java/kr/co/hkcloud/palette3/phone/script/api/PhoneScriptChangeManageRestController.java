package kr.co.hkcloud.palette3.phone.script.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.script.app.PhoneScriptChangeManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneScriptChangeManageRestController",
     description = "스크립트변경이력관리 서비스콜 REST 컨트롤러")
public class PhoneScriptChangeManageRestController
{
    private final PhoneScriptChangeManageService phoneScriptChangeManageService;


    /**
     * 스크립트변경이력 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트변경이력 조회",
                  notes = "스크립트변경이력 조회")
    @PostMapping("/phone-api/script/change-manage/list")
    public Object selectScrtChngList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptChangeManageService.selectScrtChngList(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트변경이력 상세조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트변경이력조회상세",
                  notes = "스크립트변경이력조회상세")
    @PostMapping("/phone-api/script/change-manage/detail")
    public Object selectScrtChngDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptChangeManageService.selectScrtChngDetail(mjsonParams);

        return objRetParams;
    }

}
