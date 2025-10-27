package kr.co.hkcloud.palette3.excel.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.excel.app.LrgeCpctyExcelCmmnServcie;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "LrgeCpctyExcelCmmnRestController",
     description = "대용량엑셀공통 REST 컨트롤러")
public class LrgeCpctyExcelCmmnRestController
{
    private final LrgeCpctyExcelCmmnServcie lrgeCpctyExcelCmmnServcie;


    /**
     * 대용량 엑셀 다운로드 요청
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "대용량 엑셀 다운로드 요청",
                  notes = "대용량 엑셀 다운로드를 요청한다.")
    @PostMapping("/api/excel/lrge-cpcty-excel/dwld")
    public Object requestExportExcelHugeData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        if(!mjsonParams.containsHeaderKey("TWB_SQL_NAME_SPACE") || !mjsonParams.containsHeaderKey("TWB_SQL_ID")) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "SQL 정보가 누락되었습니다.");
        }
        else {
            objRetParams = lrgeCpctyExcelCmmnServcie.requestLargeExcelDown(mjsonParams.getHeaderString("TWB_SQL_NAME_SPACE"), mjsonParams.getHeaderString("TWB_SQL_ID"), mjsonParams);
        }

        return objRetParams;
    }

}
