package kr.co.hkcloud.palette3.setting.system.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemBatchLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemBatchLogRestController",
     description = "설정시스템배치관리 REST 컨트롤러")
public class SettingSystemBatchLogRestController
{
    private final SettingSystemBatchLogService settingSystemBatchLogService;


    /**
     * 
     * @return              mjsonParams
     * @throws objRetParams
     * 
     */
    @ApiOperation(value = "설정시스템배치로그-목록",
                  notes = "설정시스템배치로그 리스트를 조회한다.")
    @PostMapping("/api/setting/system/batch-log/list")
    public Object selectRtnLogList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//      TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //데이터 조회
        objRetParams = settingSystemBatchLogService.selectRtnBatchLog(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * 
     */
    @ApiOperation(value = "설정시스템배치로그-조회",
                  notes = "배치관리 마스터 테이블을 조회하여 콤보박스 데이터를 조회하고 생성한다.")
    @PostMapping("/api/setting/system/batch-log/inqire")
    public Object selectDupAuthGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = settingSystemBatchLogService.selectBatchList(mjsonParams);

        return objRetParams;
    }
}
