package kr.co.hkcloud.palette3.setting.system.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemAuthorityByAgentManageService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemAuthorityByAgentManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemAuthorityByAgentManageRestController", description = "사용자별권한관리 REST 컨트롤러")
public class SettingSystemAuthorityByAgentManageRestController {

    private final SettingSystemAuthorityByAgentManageService settingSystemAuthorityByAgentManageService;
    private final SettingSystemAuthorityByAgentManageValidator settingSystemAuthorityByAgentManageValidator;

    //    @Autowired
    //    public TwbBas07RestController(TelewebComDAO mobjDao)
    //    {
    //        this.mobjDao = mobjDao;
    //    }


    /**
     *
     * 사용자관리 - 사용자 권한 단건 조회
     * @Method Name    : selectRtnAuthrtById
     * @date            : 2023. 6. 21.
     * @author        : NJY
     * @version        : 1.0
     * ----------------------------------------
     * @param  mjsonParams    조회할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @ApiOperation(value = "설정시스템사용자별권한관리-사용자별할당정보단건조회", notes = "설정시스템사용자별권한관리 사용자별 할당정보를 조회한다")
    @PostMapping("/api/setting/system/author-by-agent-manage/asgn-info-by-id/inqire")
    public Object selectRtnAuthrtById(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //최종결과값 반환
        return settingSystemAuthorityByAgentManageService.selectRtnAuthrtById(mjsonParams);

    }

    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템사용자별권한관리-사용자별할당정보조회", notes = "설정시스템사용자별권한관리 사용자별 할당정보를 조회한다(페이징검색)")
    @PostMapping("/api/setting/system/author-by-agent-manage/asgn-info-by-agent/inqire")
    public Object selectRtnAuthAlloc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //VALIDATION 체크
        //        settingSystemAuthorityByAgentManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemAuthorityByAgentManageService.selectRtnAuthAlloc(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_AUTHRT-MNG_GROUP_PROC", note = "설정 사용자별권한관리-권한그룹별사용자등록")
    @ApiOperation(value = "설정시스템사용자별권한관리-권한그룹별사용자등록", notes = "설정시스템사용자별권한관리 권한그룹별 사용자를 등록한다(권한그룹별 할당사용자 저장)")
    @PostMapping("/api/setting/system/author-by-agent-manage/agent-by-author-group/regist")
    public Object processRtnAuthGroupUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("regist start=-===============================================");
        return settingSystemAuthorityByAgentManageService.processRtnAuthGroupUser(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_AUTHRT-MNG_AGENT_PROC", note = "설정 사용자별권한관리-사용자별권한그룹등록")
    @ApiOperation(value = "설정시스템사용자별권한관리-사용자별권한그룹등록", notes = "설정시스템사용자별권한관리 사용자별 권한그룹을 등록한다(사용자별 권한그룹 저장)")
    @PostMapping("/api/setting/system/author-by-agent-manage/author-group-by-agent/regist")
    public Object processRtnAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityByAgentManageService.processRtnAuthGroup(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_AUTHRT-MNG_GROUP_DEL", note = "설정 사용자별권한관리-권한그룹별사용자삭제")
    @ApiOperation(value = "설정시스템사용자별권한관리-권한그룹별사용자삭제", notes = "설정시스템사용자별권한관리 권한그룹별 할당된 사용자를 삭제한다(권한그룹별 할당사용자 삭제)")
    @PostMapping("/api/setting/system/author-by-agent-manage/agent-by-author-group/delete")
    public Object deleteRtnAuthGroupUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingSystemAuthorityByAgentManageService.deleteRtnAuthGroupUser(mjsonParams);
    }
}
