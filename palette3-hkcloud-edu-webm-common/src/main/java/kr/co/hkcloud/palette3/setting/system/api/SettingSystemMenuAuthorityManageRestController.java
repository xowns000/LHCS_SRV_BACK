package kr.co.hkcloud.palette3.setting.system.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemMenuAuthorityManageService;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemMenuManageService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemMenuAuthorityManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemMenuAuthorityManageRestController",
     description = "메뉴권한관리 REST 컨트롤러")
public class SettingSystemMenuAuthorityManageRestController
{
    private final SettingSystemMenuAuthorityManageService   settingSystemMenuAuthorityManageService;
    private final SettingSystemMenuManageService            settingSystemMenuManageService;
    private final SettingSystemMenuAuthorityManageValidator settingSystemMenuAuthorityManageValidator;


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 등록결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-등록",
                  notes = "설정시스템메뉴권한관리 메뉴권한을 등록한다")
    @PostMapping("/api/setting/system/menu-author-manage/regist")
    public Object processRtnAuth(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        settingSystemMenuAuthorityManageValidator.validate(mjsonParams, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemMenuAuthorityManageService.processRtnAuth(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹코드",
                  notes = "설정시스템메뉴권한관리 권한그룹코드를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group-code/inqire")
    public Object selectRtnAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnAuthGroup(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹코드(사용자)",
                  notes = "설정시스템메뉴권한관리 사용자기준으로 권한그룹코드를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group-code-by-agent/inqire")
    public Object selectRtnAtrtGroupCd(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnAtrtGroupCd(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 삭제결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-할당메뉴삭제(그룹별)",
                  notes = "설정시스템메뉴권한관리 권한그룹별 할당메뉴를 삭제한다")
    @PostMapping("/api/setting/system/menu-author-manage/asgn-menu-by-group/delete")
    public Object deleteRtnAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.deleteRtnAuthGroup(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-할당가능메뉴조회",
                  notes = "설정시스템메뉴권한관리 권한그룹에 할당가능한 메뉴를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/asgn-posbl-menu/inqire")
    public Object selectRtnchkEnableAuth(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnchkEnableAuth(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-미할당메뉴조회",
                  notes = "설정시스템메뉴권한관리 미할당된 메뉴를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/un-asgn-menu/inqire")
    public Object selectRtnNoAlloc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
        settingSystemMenuAuthorityManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingSystemMenuAuthorityManageService.selectRtnNoAlloc(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-할당메뉴조회",
                  notes = "설정시스템메뉴권한관리 할당된 메뉴를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/asgn-menu/inqire")
    public Object selectRtnAlloc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
//        settingSystemMenuAuthorityManageValidator.validate(mjsonParams, result);
//        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingSystemMenuAuthorityManageService.selectRtnAlloc(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-미할당버튼조회",
                  notes = "설정시스템메뉴권한관리 미할당된 버튼을 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/un-asgn-button/inqire")
    public Object selectRtnNoAllocBtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnNoAllocBtn(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-할당버튼조회",
                  notes = "설정시스템메뉴권한관리 할당된 버튼을 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/asgn-button/inqire")
    public Object selectRtnAllocBtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnAllocBtn(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-그룹목록",
                  notes = "설정시스템메뉴권한관리 그룹목록을 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/group/inqire")
    public Object selectRtnMenuGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuManageService.selectRtnMenuGroup(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-메뉴권한트리목록",
                  notes = "설정시스템메뉴권한관리 메뉴권한트리목록을 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/menu-author-tree/inqire")
    public Object selectRtnMenuAuthTree(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectRtnMenuAuthTree(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-권한그룹명중복체크",
                  notes = "설정시스템메뉴권한관리 권한그룹명 중복을 체크한다")
    @PostMapping("/api/setting/system/menu-author-manage/author-group-nm-dplct-ceck/inqire")
    public Object selectDupAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemMenuAuthorityManageService.selectDupAuthGroup(mjsonParams);
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴권한관리-사용자별할당정보조회",
                  notes = "설정시스템메뉴권한관리 사용자별 메뉴권한 할당/미할당 정보를 조회한다")
    @PostMapping("/api/setting/system/menu-author-manage/asgn-info-by-agent/inqire")
    public Object selectRtnAuthGroupMng(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
        settingSystemMenuAuthorityManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingSystemMenuAuthorityManageService.selectRtnAuthGroupMng(mjsonParams);
    }
}
