package kr.co.hkcloud.palette3.setting.consulttype.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.consulttype.app.SettingConsulttypeManageService;
import kr.co.hkcloud.palette3.setting.consulttype.util.SettingConsulttypeManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingConsulttypeManageRestController",
     description = "설정상담유형관리 REST 컨트롤러")
public class SettingConsulttypeManageRestController
{
    private final SettingConsulttypeManageService   settingConsulttypeManageService;
    private final SettingConsulttypeManageValidator settingConsulttypeManageValidator;


    /**
     * 
     * @param  mjsonParams
     * @author             정성현
     * @return
     */
    @ApiOperation(value = "회사이름을 검색",
                  notes = "회사이름 조회")
    @PostMapping("/api/setting/consulttype/manage/companyname/inqire")
    public Object selectCompanyName(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//        //Validation 체크 
//        settingConsulttypeManageValidator.validate(mjsonParams, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }
//
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingConsulttypeManageService.selectCompanyName(mjsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정상담유형관리-1레벨목록",
                  notes = "설정상담유형관리 1레벨목록을 조회한다")
    @PostMapping("/api/setting/consulttype/manage/level1/inqire")
    public Object selectCnslTypLevelByTopLevel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        //Validation 체크 
        settingConsulttypeManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingConsulttypeManageService.selectCnslTypLevelByTopLevel(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정상담유형관리-목록",
                  notes = "설정상담유형관리 목록(1레벨제외)을 조회한다.")
    @PostMapping("/api/setting/consulttype/manage/list")
    public Object selectCnslTypByNotTopLevel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingConsulttypeManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = settingConsulttypeManageService.selectCnslTypByNotTopLevel(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정상담유형관리-등록",
                  notes = "설정상담유형관리 상담유형을 등록한다.")
    @PostMapping("/api/setting/consulttype/manage/regist")
    public Object processRtnTwbTalkCnslTyp(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);	//전송된 파라메터 반환

        //Validation 체크 
        settingConsulttypeManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingConsulttypeManageService.processRtnTwbTalkCnslTyp(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형 삭제",
                  notes = "상담유형 삭제")
    @PostMapping("/api/setting/consulttype/manage/delete")
    public Object deleteRtnCnslTyp(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);	//전송된 파라메터 반환

        //Validation 체크 
        settingConsulttypeManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingConsulttypeManageService.deleteRtnCnslTyp(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "상담유형정보 조회",
                  notes = "상담유형 정보를 가져와 콤보를 생성한다.")
    @PostMapping("/api/setting/consulttype/manage/inqire")
    public Object selectRtnNodeDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("mjsonParams 상담유형 =============" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingConsulttypeManageService.selectRtnNodeDetail(jsonParams);

        return objRetParams;
    }

}
