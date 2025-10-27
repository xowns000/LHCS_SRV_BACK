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
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemLargeCapacityExcelListService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemLargeCapacityExcelListValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemLargeCapacityExcelListRestController",
     description = "설정시스템대용량엑셀목록 REST 컨트롤러")
public class SettingSystemLargeCapacityExcelListRestController
{
    private final SettingSystemLargeCapacityExcelListValidator settingSystemLargeCapacityExcelListValidator;
    private final SettingSystemLargeCapacityExcelListService   settingSystemLargeCapacityExcelListService;


    /**
     * 
     * @param  mjsonParams
     * @param  result
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "대용량엑셀 요청 현황 조회",
                  notes = "대용량엑셀 요청 현황 조회")
    @PostMapping("/api/setting/system/lrge-cpcty-excel-list/requst-sttus/list")
    public Object selectRtnReqList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//	        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemLargeCapacityExcelListValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //사용자 공통Biz를 이용하여 기본정보를 검색한다.
        objRetParams = settingSystemLargeCapacityExcelListService.selectRtnReqList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }
}
