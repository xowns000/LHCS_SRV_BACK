package kr.co.hkcloud.palette3.common.innb.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "InnbCreatCmmnRestController",
     description = "고유번호생성 공통 REST 컨트롤러")
public class InnbCreatCmmnRestController
{
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 업무별 고유키정보를 생성하여 반환한다.
     * 
     * @return                     업무별 고유키(YYYYMMDDHHMISSMSC + 업무구분(대문자3자리) + 00000)
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "업무별 고유키정보를 생성",
                  notes = "업무별 고유키정보를 생성하여 반환한다.")
    @PostMapping("/api/innb/common/generate-unique-no/inqry")
    public Object selectBizSeq(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //업무구분
        String strBizCase = mjsonParams.getString("BIZ_CASE");
        //고유키 정보 세팅
        objRetParams.setString("RET_VAL", innbCreatCmmnService.getSeqNo(mjsonParams, strBizCase));
        //최종결과값 반환
        return objRetParams;
    }

}
