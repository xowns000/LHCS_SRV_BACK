package kr.co.hkcloud.palette3.setting.system.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemCommonCodeManageService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemCommonCodeManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemCommonCodeManageRestController", description = "공통코드관리 REST 컨트롤러")
public class SettingSystemCommonCodeManageRestController {

    private final SettingSystemCommonCodeManageService settingSystemCommonCodeManageService;
    private final SettingSystemCommonCodeManageValidator settingSystemCommonCodeManageValidator;


    /**
     * /twb/TwbBas03/selectRtnCodeType
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템공통코드관리-목록", notes = "설정시스템공통코드관리 목록을 조회한다")
    @PostMapping("/api/setting/system/cmmn-code-manage/list")
    public Object selectRtnCodeType(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemCommonCodeManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemCommonCodeManageService.selectRtnCodeType(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템공통코드관리-상세", notes = "설정시스템공통코드관리 상세를 조회한다")
    @PostMapping("/api/setting/system/cmmn-code-manage/detail")
    public Object selectRtnCodeDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemCommonCodeManageService.selectRtnCodeDetail(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_CMMN-CODE-MNG_PROC", note = "설정 공통코드관리 변경(등록,수정)")
    @ApiOperation(value = "설정시스템공통코드관리-등록", notes = "설정시스템공통코드관리 공통코드를 등록한다")
    @PostMapping("/api/setting/system/cmmn-code-manage/regist")
    public Object insertTwbBas03(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemCommonCodeManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingSystemCommonCodeManageService.insertTwbBas03(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_CMMN-CODE-MNG_PROC", note = "설정 공통코드관리 변경(등록,수정)")
    @ApiOperation(value = "설정시스템공통코드관리-수정", notes = "설정시스템공통코드관리 공통코드를 수정한다")
    @PostMapping("/api/setting/system/cmmn-code-manage/modify")
    public Object UPDATE_PLT_COMN_CD(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemCommonCodeManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingSystemCommonCodeManageService.updateTwbBse03(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템공통코드관리-중복체크", notes = "설정시스템공통코드관리 공통코드 중복체크한다")
    @PostMapping("/api/setting/system/cmmn-code-manage/dpcnCheck")
    public Object dpcnCheck(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemCommonCodeManageService.dpcnCheck(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 삭제결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CMMN-CODE-MNG_DEL", note = "설정 공통코드관리 삭제")
    @ApiOperation(value = "설정시스템공통코드관리-삭제", notes = "설정시스템공통코드관리 공통코드 삭제한다(비활성화 처리)")
    @PostMapping("/api/setting/system/cmmn-code-manage/delete")
    public Object deleteRtnCodeInfo(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        //Validation 체크 
        //        settingSystemCommonCodeManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        if (!objArry.isEmpty()) {
            for (int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if (!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    settingSystemCommonCodeManageService.deleteRtnCodeInfo(objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }

}
