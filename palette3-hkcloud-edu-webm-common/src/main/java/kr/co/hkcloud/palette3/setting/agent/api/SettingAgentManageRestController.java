package kr.co.hkcloud.palette3.setting.agent.api;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.agent.app.SettingAgentManageService;
import kr.co.hkcloud.palette3.setting.agent.util.SettingAgentManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingAgentManageRestController", description = "사용자관리 REST 컨트롤러")
public class SettingAgentManageRestController {

    private final SettingAgentManageService settingAgentManageService;
    private final SettingAgentManageValidator settingAgentManageValidator;

    /**
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * @author 정성현
     */
    @ApiOperation(value = "호분배 할때 등록된 IP 내선 체크", notes = "사용자 업데이트시 사용")
    @PostMapping("/api/setting/agent/manage/inlineCheck")
    public Object InlineCheck(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("mjsonParams2 =========11============" + mjsonParams);
        return settingAgentManageService.UserBatchInterface(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * @author 정성현
     */
    @ApiOperation(value = "외부 Api 인터페이스 태우기 호분배", notes = "사용자 업데이트시 사용")
    @PostMapping("/api/setting/agent/manage/userBatchImpl")
    public Object UserBatchInterface(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("mjsonParams2 =========11============" + mjsonParams);
        return settingAgentManageService.UserBatchInterface(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "외부 Api 인터페이스 태우기 로그아웃", notes = "사용자 업데이트시 사용")
    @PostMapping("/api/setting/agent/manage/userLogoutImpl")
    public Object UserLogoutInterface(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        log.debug("mjsonParams2 =======11==============" + mjsonParams);
        return settingAgentManageService.UserLogoutInterface(mjsonParams);
    }

    /**
     * 
     * 사용자관리 - 사용자 조회
     * 
     * @Method Name : processRtn
     * @date : 2023. 6. 15.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 조회할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @PrvcAspectAnotation(value = "USER_LIST") //개인정보 Logging관련
    @ApiOperation(value = "설정사용자관리-목록", notes = "설정사용자관리 목록을 조회한다")
    @PostMapping("/api/setting/agent/manage/list")
    public Object selectRtnUserInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //        OkHttpClient client = new OkHttpClient().newBuilder().build();
        //        MediaType mediaType = MediaType.parse("text/plain");
        //        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("tplexParam", "{\"SP_CODE\":\"2000\",\"REQ\":\"force_logout\",\"AGENT_ID\":\"AGENT_1003\",\"EXTENSION_NO\":\"1003\"}")
        //            .build();
        //        Request request = new Request.Builder().url("http://121.67.187.236:60080/API/").post(body).build();
        //        try {
        //            Response response = client.newCall(request).execute();
        //            try {
        //                JSONObject json = new JSONObject(response.body().string());
        //                log.debug("aaaaaaa =========" + json.getString("result_code"));
        //            }
        //            catch(JSONException e) {
        //                e.printStackTrace();
        //            }
        //
        //        }
        //        catch(IOException e) {
        //            e.printStackTrace();
        //        }

        return settingAgentManageService.selectRtnUserInfo(mjsonParams);
        //        return settingAgentManageService.selectRtnUserInfo_new(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     * @author 정성현
     */
    @ApiOperation(value = "설정사용자관리-소속등록", notes = "설정사용자관리 사용자 소속을 등록한다.")
    @PostMapping("/api/setting/agent/manage/registAttr")
    public Object processRtnAttr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("mjsonParams ================" + mjsonParams);
        return settingAgentManageService.processRtnAttr(mjsonParams);
    }

    /**
     * 
     * 사용자관리 - 사용자 등록 - 사용자 ID 중복체크
     * 
     * @Method Name : UserDpncChk
     * @date : 2023. 7. 26.
     * @author : NJY
     * @version : 1.1
     * ----------------------------------------
     * @param mjsonParams 조회할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @ApiOperation(value = "설정사용자관리-사용자 등록-사용자ID 중복체크 ", notes = "설정사용자관리 사용자 등록에서 사용자 ID를 중복체크 한다.")
    @PostMapping("/api/setting/agent/manage/regist-user/dpnc-chk/inqire")
    public Object UserDpncChk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.UserDpncChk(mjsonParams);
    }

    /**
     * 
     * 사용자관리 - 사용자 등록 및 수정
     * 
     * @Method Name : processRtn
     * @date : 2023. 6. 16.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 등록,수정할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @PrvcAspectAnotation(value = "USER_REG,USER_MOD") //개인정보 Logging관련
    @ApiOperation(value = "설정사용자관리-등록", notes = "설정사용자관리 사용자를 등록 및 수정한다")
    @PostMapping("/api/setting/agent/manage/regist")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //        settingAgentManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingAgentManageService.processRtn(mjsonParams);
    }

    /**
     * 
     * @author R&D
     * @since 2021.03.24
     * @param TelewebJSON
     * @return TelewebJSON 형식의 처리결과 데이터
     */
    @ApiOperation(value = "설정사용자관리-비밀번호초기화", notes = "설정사용자관리 사용자 비밀번호를 초기화한다")
    @PostMapping("/api/setting/agent/manage/password-initl/process")
    public Object updatePasswordReset(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.updatePasswordReset(mjsonParams);
    }

    /**
     * 
     * @author lsm
     * @since 2019.04.15
     * @param TelewebJSON
     * @return TelewebJSON 형식의 처리결과 데이터
     */
    @ApiOperation(value = "설정사용자관리-비밀번호잠금초기화", notes = "설정사용자관리 사용자 비밀번호잠금을 초기화한다")
    @PostMapping("/api/setting/agent/manage/password-lock-initl/process")
    public Object updatePasswordUnLock(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.updatePasswordUnLock(mjsonParams);
    }

    /**
     * (사용안함)
     * 
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정사용자관리-팝업-페이지목록", notes = "설정사용자관리-팝업-페이지목록(사용안함)")
    @PostMapping("/api/setting/agent/manage/popup-page-list")
    public Object selectRtnUserPage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.selectRtnUserPage(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @param result
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @PrvcAspectAnotation(value = "USER_LIST")   //개인정보 Logging관련
    @ApiOperation(value = "설정사용자관리/사용자정보-조회-팝업-조회", notes = "설정사용자관리 사용자정보 목록을 조회한다")
    @PostMapping("/api/setting/agent/manage/user-info-inqire-popup/inqire")
    public Object selectRtnUserPage_new(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //        settingAgentManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingAgentManageService.selectRtnUserPage_new(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자관리-사용자정보조회", notes = "설정사용자관리 사용자정보를 조회한다")
    @PostMapping("/api/setting/agent/manage/agent-info/inqire")
    public Object selectRtnUserBaseInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.selectRtnUserBaseInfo(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자관리-사용자목록", notes = "설정사용자관리 사용자목록을 조회한다")
    @PostMapping("/api/setting/agent/manage/agent/inqire")
    public Object selectTwbBas01(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.selectTwbBas01(mjsonParams);
    }

    /**
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "설정사용자관리-ID중복체크", notes = "설정사용자관리 ID가 중복하는지 확인한다")
    @PostMapping("/api/setting/agent/manage/id-dpict-ceck/inqire")
    public Object selectTwbBas01pWD(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.selectTwbBas01pWD(mjsonParams);
    }

    /**
     * 상담원 팝업 조회
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정사용자관리/상담원정보-조회-팝업/조회", notes = "설정사용자관리/상담원정보-조회-팝업/조회")
    @PostMapping("/api/setting/agent/manage/agent-info-inqire-popup/inqire")
    public Object selectRtnUserInfoPop(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = settingAgentManageService.selectRtnUserInfoPop(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 사용자 삭제
     * 사용자 삭제시 상담이력등의 문제가 발생할 수 있음
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "사용자 삭제", notes = "사용자 삭제")
    @PostMapping("/api/setting/agent/manage/delete")
    public Object deleteUser(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = settingAgentManageService.deleteUser(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 사용자 ID 수정
     * 사용자 ID 수정시 상담이력등의 문제가 발생할 수 있음
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "사용자 ID 수정", notes = "사용자 ID 수정")
    @PostMapping("/api/setting/agent/manage/modify")
    public Object updateUserId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = settingAgentManageService.updateUserId(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * 메인화면 - 사용자 프로필 사진 등록
     * 
     * @Method Name : updateProfile
     * @date : 2023. 7. 13.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 등록,수정할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @ApiOperation(value = "설정사용자관리-등록", notes = "설정사용자관리 사용자를 등록 및 수정한다")
    @PostMapping("/api/setting/agent/profile/updateProfile")
    public Object updateProfile(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return settingAgentManageService.updateProfile(mjsonParams);
    }

    /**
     * 
     * 강제 비밀번호 변경
     * 
     * @Method Name : forceUpdatePassword
     * @date : 2023. 9. 20.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "강제 비밀번호 변경", notes = "강제로 비밀번호를 변경한다.")
    @PostMapping("/api/setting/agent/forceUpdatePassword")
    public Object forceUpdatePassword(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.forceUpdatePassword(mjsonParams);
    }
    
    /**
     * 
     * 전체 스키마별 고객사 목록
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전체 스키마별 고객사 목록", notes = "전체 스키마별 고객사 목록")
    @PostMapping("/api/setting/agent/schemaCustcoList")
    public Object schemaCustcoList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingAgentManageService.schemaCustcoList(mjsonParams);
    }

    /**
     * 
     * 고객사별 상담원 목록
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객사별 상담원 목록", notes = "전체 스키마별 고객사 목록")
    @PostMapping("/api/setting/agent/custcoCuslList")
    public Object custcoCuslList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
    	return settingAgentManageService.custcoCuslList(mjsonParams);
    }
}