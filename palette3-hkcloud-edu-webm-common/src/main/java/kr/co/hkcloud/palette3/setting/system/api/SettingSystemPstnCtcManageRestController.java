package kr.co.hkcloud.palette3.setting.system.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemPstnCtcManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemPstnCtcManageRestController", description = "설정시스템 위치 및 연락처정보 관리 REST 컨트롤러")
public class SettingSystemPstnCtcManageRestController {

    private final SettingSystemPstnCtcManageService settingSystemPstnCtcManageService;


    /**
     *
     * 메서드 설명		: 위치정보 리스트 조회
     * @Method Name    : selectPstn
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템 위치정보관리 - 위치정보 리스트 조회", notes = "설정시스템 위치정보관리 - 위치정보 리스트를 조회한다")
    @PostMapping("/api/setting/system/pstn/selectPstn")
    public Object selectPstn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.selectPstn(mjsonParams);

        return objRetParams;
    }

    /**
     * 메서드 설명		: 위치정보 등록 및 수정
     * @Method Name    : upsertPstn
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_PSTN_PROC", note = "설정 위치정보관리 변경(등록,수정)")
    @ApiOperation(value = "설정시스템 위치정보관리 - 위치정보 등록 및 수정", notes = "설정시스템 위치정보관리 - 위치정보를 등록 및 수정한다")
    @PostMapping("/api/setting/system/pstn/upsertPstn")
    public Object upsertPstn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.upsertPstn(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * 메서드 설명		: 위치정보 삭제 
     * @Method Name    : deletePstn
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_PSTN_DEL", note = "설정 위치정보관리 삭제")
    @ApiOperation(value = "설정시스템 위치정보관리 - 위치정보 삭제", notes = "설정시스템 위치정보관리 - 위치정보 삭제한다")
    @PostMapping("/api/setting/system/pstn/deletePstn")
    public Object deletePstn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.deletePstn(mjsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 위치정보 리스트 조회
     * @Method Name    : selectCtc
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템 연락처정보관리 - 연락처정보 리스트 조회", notes = "설정시스템 연락처정보관리 - 연락처정보 리스트를 조회한다")
    @PostMapping("/api/setting/system/ctc/selectCtc")
    public Object selectCtc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.selectCtc(mjsonParams);

        return objRetParams;
    }


    /**
     * 메서드 설명		: 연락처정보 등록 및 수정
     * @Method Name    : upsertCtc
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CTC_PROC", note = "설정 연락처정보관리 변경(등록,수정)")
    @ApiOperation(value = "설정시스템 연락처정보관리 - 연락처정보 등록 및 수정", notes = "설정시스템 연락처정보관리 - 연락처정보를 등록 및 수정한다")
    @PostMapping("/api/setting/system/ctc/upsertCtc")
    public Object upsertCtc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.upsertCtc(mjsonParams);

        return objRetParams;
    }


    /**
     *
     * 메서드 설명		: 연락처정보 삭제 
     * @Method Name    : deleteCtc
     * @date            : 2023. 6. 19
     * @author        : 김성태
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CTC_DEL", note = "설정 연락처정보관리 삭제")
    @ApiOperation(value = "설정시스템 연락처정보관리 - 연락처정보 삭제", notes = "설정시스템 연락처정보관리 - 연락처정보를 삭제한다")
    @PostMapping("/api/setting/system/ctc/deleteCtc")
    public Object deleteCtc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = settingSystemPstnCtcManageService.deleteCtc(mjsonParams);

        return objRetParams;
    }

}
