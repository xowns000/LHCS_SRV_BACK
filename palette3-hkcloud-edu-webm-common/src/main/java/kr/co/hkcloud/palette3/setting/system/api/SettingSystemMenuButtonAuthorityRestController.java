package kr.co.hkcloud.palette3.setting.system.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemMenuButtonAuthorityService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemMenuButtonAuthorityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemMenuButtonAuthorityRestController",
     description = "메뉴버튼권한 REST 컨트롤러")
public class SettingSystemMenuButtonAuthorityRestController
{
    private final SettingSystemMenuButtonAuthorityService   settingSystemMenuButtonAuthorityService;
    private final SettingSystemMenuButtonAuthorityValidator settingSystemMenuButtonAuthorityValidator;


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴권한관리-버튼권한 조회",
                  notes = "설정시스템메뉴권한관리 버튼권한을 조회한다.")
    @PostMapping("/api/setting/system/menu-author-manage/menu-button-author/inqire")
    public Object selectRtnMenuBtnRole(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuButtonAuthorityService.selectRtnMenuBtnRole(mjsonParams);
    }


    /**
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-버튼권한등록",
                  notes = "설정시스템메뉴권한관리 버튼권한을 등록한다")
    @PostMapping("/api/setting/system/menu-author-manage/button-author/regist")
    public Object processRtnBtnAuth(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        settingSystemMenuButtonAuthorityValidator.validate(mjsonParams, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingSystemMenuButtonAuthorityService.processRtnBtnAuth(mjsonParams);
    }
}
