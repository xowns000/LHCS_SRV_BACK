package kr.co.hkcloud.palette3.setting.agent.api;


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
import kr.co.hkcloud.palette3.setting.agent.app.SettingAgentBelongItemManageService;
import kr.co.hkcloud.palette3.setting.agent.util.SettingAgentBelongItemManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingAgentBelongItemManageServiceCallRestController",
     description = "조직관리 서비스콜 REST 컨트롤러")
public class SettingAgentBelongItemManageServiceCallRestController
{
    private final SettingAgentBelongItemManageService   settingAgentBelongItemManageService;
    private final SettingAgentBelongItemManageValidator settingAgentBelongItemManageValidator;


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-목록",
                  notes = "설정사용자소속항목관리 목록을 조회한다")
    @PostMapping("/api/setting/agent/psitn-iem-manage/inqire")
    public Object selectAttrView(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        settingAgentBelongItemManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingAgentBelongItemManageService.selectAttrView(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-등록",
                  notes = "설정사용자소속항목관리 항목을 등록한다")
    @PostMapping("/api/setting/agent/psitn-iem-manage/regist")
    public Object insertAttr(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        settingAgentBelongItemManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingAgentBelongItemManageService.insertAttr(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-수정",
                  notes = "설정사용자소속항목관리 항목을 수정한다")
    @PostMapping("/api/setting/agent/psitn-iem-manage/modify")
    public Object updateAttr(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        settingAgentBelongItemManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingAgentBelongItemManageService.updateAttr(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-삭제",
                  notes = "설정사용자소속항목관리 항목을 삭제한다")
    @PostMapping("/api/setting/agent/psitn-iem-manage/delete")
    public Object deleteAttr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingAgentBelongItemManageService.deleteAttr(jsonParams);

        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자소속항목관리-중복체크",
                  notes = "설정사용자소속항목관리 항목이 중복되는지 체크한다")
    @PostMapping("/api/setting/agent/psitn-iem-manage/dplct-ceck/inqire")
    public Object selectChkDeptCode(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingAgentBelongItemManageService.selectChkDeptCode(jsonParams);

        return objRetParams;
    }


    /**
     * 부서정보 조회(wfms)
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "부서정보 조회",
                  notes = "부서정보 조회")
    @PostMapping("/api/setting/agent/psitn-iem-manage/dept-info1/inqire")
    public Object selectRtnUserDeptCdInfo1(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        objRetParams = settingAgentBelongItemManageService.selectRtnUserDeptCdInfo1(mjsonParams);

        //최종결과값 반환 
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "부서정보 조회",
                  notes = "부서정보 조회")
    @PostMapping("/api/setting/agent/psitn-iem-manage/dept-info2/inqire")
    public Object selectRtnUserDeptCdInfo2(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        objRetParams = settingAgentBelongItemManageService.selectRtnUserDeptCdInfo2(mjsonParams);

        //최종결과값 반환 
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "부서정보 조회",
                  notes = "부서정보 조회")
    @PostMapping("/api/setting/agent/psitn-iem-manage/dept-info3/inqire")
    public Object selectRtnUserDeptCdInfo3(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        objRetParams = settingAgentBelongItemManageService.selectRtnUserDeptCdInfo3(mjsonParams);

        //최종결과값 반환 
        return objRetParams;
    }
}
