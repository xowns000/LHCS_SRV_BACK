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
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemCorporateAccountManageService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemCorporateAccountManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemCorporateAccountManageRestController",
     description = "설정시스템기업계정관리 컨트롤러")
public class SettingSystemCorporateAccountManageRestController
{
    private final SettingSystemCorporateAccountManageService   settingSystemCorporateAccountManageService;
    private final SettingSystemCorporateAccountManageValidator settingSystemCorporateAccountManageValidator;


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리에서 큐리스트 조회.",
                  notes = "설정시스템기업계정관리에 큐를 등록한다.")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/queue/delete")
    public Object deleteQueue(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.deleteQueue(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리에서 큐리스트 조회.",
                  notes = "설정시스템기업계정관리에 큐를 등록한다.")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/queue/selectlist")
    public Object selectQueueList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectQueueList(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리에서 큐를 등록한다.",
                  notes = "설정시스템기업계정관리에 큐를 등록한다.")
    @PostMapping("/api/setting/system/queue/regist")
    public Object insertAspQueue(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.insertAspQueue(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리에서 큐 등록시 다이얼로그를 띄워주기 위함.",
                  notes = "설정시스템기업계정관리에 다이얼로그를 띄워주기 위함.")
    @PostMapping("/api/setting/system/queue/detail")
    public Object selectAspQueue(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectAspQueue(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-목록",
                  notes = "설정시스템기업계정관리 목록을 조회한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/list")
    public Object selectRtnPageAspCustList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectRtnPageAspCustList(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-고객사상세팝업-상세조회",
                  notes = "설정시스템기업계정관리  고객사상세팝업 상세를 조회한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/ctmmny-detail-popup/detail")
    public Object selectRtnAspCustDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectRtnAspCustDetail(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템기업계정관리-기업계정코드",
                  notes = "설정시스템기업계정관리 기업계정코드를 조회한다(comboData)")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/entrprs-acnt-code/inqire")
    public Object selectRtnAspCustComboData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectRtnAspCustComboData(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-기업정보조회",
                  notes = "설정시스템기업계정관리 기업정보를 조회한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/entrprs-info/inqire")
    public Object selectAspCustInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.selectAspCustInfo(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-등록",
                  notes = "설정시스템기업계정관리 기업계정을 등록한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/ctmmny-regist-popup/regist")
    public Object insertRtnAspCust(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.insertRtnAspCust(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-고객사상세팝업-상세수정",
                  notes = "설정시스템기업계정관리 고객사상세팝업 상세정보를 수정한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/ctmmny-detail-popup/modify")
    public Object updateRtnAspCustDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.updateRtnAspCustDetail(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-삭제",
                  notes = "설정시스템기업계정관리 기업계정을 삭제한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/delete")
    public Object deleteRtnAspCust(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        settingSystemCorporateAccountManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemCorporateAccountManageService.deleteRtnAspCust(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-봇톡스 로그인",
                  notes = "설정시스템기업계정관리  봇톡스에 로그인한다")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/bot-setting")
    public Object botalkProcess(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("come here ===== mjsonParams" + mjsonParams);
        return settingSystemCorporateAccountManageService.botalkProcess(mjsonParams);
    }


    /**
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정시스템기업계정관리-기업계정 존재여부",
                  notes = "설정시스템기업계정관리 신규 기업계정이 존재하는지 조회한다.")
    @PostMapping("/api/setting/system/entrprs-acnt-manage/ctmmny-regist-popup/exist")
    public Object selectCustExist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("hereis" + mjsonParams);
        return settingSystemCorporateAccountManageService.selectCustExist(mjsonParams);
    }

    /**
     * CUSTCO_ID 변경하기
     * 
     * @param  없음(DefaultService에 mjsonParams 전역변수에 정의되어 있음)
     * @return                    TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "CUSTCO_ID 변경하기",
                  notes = "CUSTCO_ID 변경하기")
    @PostMapping("/api/setting/system/CUSTCO_ID/modify")
    public Object updateCustcoId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingSystemCorporateAccountManageService.updateCustcoId(mjsonParams);
    }
}
