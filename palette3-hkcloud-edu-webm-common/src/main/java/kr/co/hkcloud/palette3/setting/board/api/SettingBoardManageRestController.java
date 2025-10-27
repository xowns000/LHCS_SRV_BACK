package kr.co.hkcloud.palette3.setting.board.api;


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
import kr.co.hkcloud.palette3.setting.board.app.SettingBoardManageService;
import kr.co.hkcloud.palette3.setting.board.util.SettingBoardManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingBoardManageRestController",
     description = "설정게시판관리  REST 컨트롤러")
public class SettingBoardManageRestController
{
    private final SettingBoardManageService   settingBoardManageService;
    private final SettingBoardManageValidator settingBoardManageValidator;


    /**
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "설정게시판관리-목록",
                  notes = "설정게시판관리 목록을 조회한다")
    @PostMapping("/api/setting/board/manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //Validation 체크 
        settingBoardManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingBoardManageService.selectRtn(mjsonParams);
    }


    /**
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "설정게시판관리-등록",
                  notes = "설정게시판관리 게시판을 등록한다")
    @PostMapping("/api/setting/board/manage/regist")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //데이터 체크가 통과할 경우만 처리
        if(validationProcess(mjsonParams, objRetParams)) {
            //Validation 체크 
            settingBoardManageValidator.validate(mjsonParams, result);
            if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

            objRetParams = settingBoardManageService.processRtn(mjsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "설정게시판관리-삭제",
                  notes = "설정게시판관리 게시판을 삭제한다")
    @PostMapping("/api/setting/board/manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //Validation 체크 
        settingBoardManageValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //데이터 체크가 통과할 경우만 처리
        if(validationDelete(mjsonParams, objRetParams)) {
            objRetParams = settingBoardManageService.deleteRtn(mjsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 처리시 데이터체크 템플릿
     * 
     * @param jsonParams   전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     */
    private boolean validationProcess(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException
    {
        boolean blnRetVal = true;
        String strTransFlag = jsonParams.getString("DATA_FLAG");    //화면에서 전송된 플래그 설정
        TelewebJSON objParams = new TelewebJSON(jsonParams);

        //신규/수정 상태에 따른 다른 체크가 필요할 경우 추가 조건 설정
        if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
            // 중복체크
            objParams = settingBoardManageService.selectRtnDupBrdMaster(jsonParams);

            //공통 항목에 대한 데이터 체크
            if(objParams.getInt("CNT") > 0) {
                //조건이 맞지 않을 경우 objRetParams 반환파라메터에 상태와 메시지를 설정하여 반환한다.
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "같은 ID값이 존재합니다.");
                blnRetVal = false;
            }
        }
        else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            //체크없음
        }

        return blnRetVal;
    }


    /**
     * 데이터 삭제시 데이터체크 템플릿
     * 
     * @param  jsonParams   전송된 파라메터 데이터
     * @param  objRetParams 반환파라메터
     * @return              true:처리가능상태, false:처리불가능상태
     */
    private boolean validationDelete(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebApiException
    {
        boolean blnRetVal = true;

        TelewebJSON objParams = new TelewebJSON(jsonParams);
        objParams = settingBoardManageService.selectRtnBrdDataCnt(jsonParams);

        //삭제 시 필요한 로직 처리
        if(objParams.getInt("CNT") > 0) {
            //조건이 맞지 않을 경우 objRetParams 반환파라메터에 상태와 메시지를 설정하여 반환한다.
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "데이터가 존재하여 삭제가 불가능 합니다.");
            blnRetVal = false;
        }

        return blnRetVal;
    }


    /**
     * 
     * @author R&D
     * @since  2021.03.26
     */
    @ApiOperation(value = "설정게시판관리-실시간공지사용여부조회",
                  notes = "설정게시판관리 실시간공지 사용여부를 조회한다")
    @PostMapping("/api/setting/board/manage/rltm-notice-use-at/inqire")
    public Object selectRtNotiRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return settingBoardManageService.selectRtNotiRtn(mjsonParams);
    }
}
