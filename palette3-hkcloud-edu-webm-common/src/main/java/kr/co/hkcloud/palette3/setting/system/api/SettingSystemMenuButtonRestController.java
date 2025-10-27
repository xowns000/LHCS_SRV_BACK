package kr.co.hkcloud.palette3.setting.system.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemMenuButtonService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemMenuButtonValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemMenuButtonRestController", description = "메뉴버튼 REST 컨트롤러")
public class SettingSystemMenuButtonRestController {

    private final SettingSystemMenuButtonService settingSystemMenuButtonService;
    private final SettingSystemMenuButtonValidator settingSystemMenuButtonValidator;


    /**
     * /twb/tables/BTN/SELECT_BTN
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "메뉴버튼-목록", notes = "메뉴버튼 목록을 조회한다")
    @PostMapping("/api/setting/system/menu-manage/button/inqire")
    public Object selectBtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuButtonService.selectBtn(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_BTN_PROC", note = "설정 메뉴관리 메뉴버튼 변경(등록,수정)")
    @ApiOperation(value = "메뉴버튼-등록/수정 통합", notes = "메뉴버튼을 등록또는수정한다")
    @PostMapping("/api/setting/system/menu-manage/button/regist")
    public Object insertBtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        //        settingSystemMenuButtonValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }
        //
        //        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuButtonService.insertBtn(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_BTN_PROC", note = "설정 메뉴관리 메뉴버튼 변경(등록,수정)")
    @ApiOperation(value = "메뉴버튼-수정", notes = "메뉴버튼을 수정한다")
    @PostMapping("/api/setting/system/menu-manage/button/modify")
    public Object updateBtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemMenuButtonValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuButtonService.updateBtn(jsonParams);
    }


    /**
     *
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return TelewebJSON 형식의 조회결과 데이터
     */

    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_BTN_DEL", note = "설정 메뉴관리 메뉴버튼 삭제")
    @ApiOperation(value = "메뉴버튼-삭제", notes = "메뉴버튼을 삭제한다")
    @PostMapping("/api/setting/system/menu-manage/button/delete")
    public Object deleteRtnBtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        //        settingSystemMenuButtonValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemMenuButtonService.deleteRtnBtn(mjsonParams);
    }

}
