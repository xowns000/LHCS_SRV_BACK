package kr.co.hkcloud.palette3.setting.env.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.env.app.SettingEnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName : kr.co.hkcloud.palette3.setting.env.api
 * fileName : SettingEnvRestController
 * author : USER
 * date : 2023-11-01
 * description : 고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등) 세팅 처리용도.
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023-11-01 USER 최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingEnvRestController", description = "고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등) REST 컨트롤러")
public class SettingEnvRestController {

    private final SettingEnvService settingEnvService;

    /**
     *
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "고객-환경설정정보 조회", notes = "고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등)을 조회한다")
    @PostMapping("/api/setting/env/custcoSettingList")
    public Object custcoSettingList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingEnvService.custcoSettingList(mjsonParams);
    }

    @ApiOperation(value = "고객-환경설정정보 조회", notes = "고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등)을 조회한다")
    @PostMapping("/api/setting/env/selectSettingEnv")
    public Object selectSettingEnv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingEnvService.selectSettingEnv(mjsonParams);
    }

    @SystemEventLogAspectAnotation(value = "COM_CUSTCO_ENV_PROC", note = "고객사별 환경설정 정보 변경(수정)")
    @ApiOperation(value = "고객-환경설정정보 변경", notes = "고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등)을 변경한다")
    @PostMapping("/api/setting/env/updateSettingEnv")
    public Object updateSettingEnv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingEnvService.updateSettingEnv(mjsonParams);
    }

}
