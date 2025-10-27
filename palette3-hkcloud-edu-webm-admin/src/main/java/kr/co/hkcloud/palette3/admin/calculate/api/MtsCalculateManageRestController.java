package kr.co.hkcloud.palette3.admin.calculate.api;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.custco.app.AdminCertCustcoManageService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.cron.app.CronService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.admin.calculate.app.MtsCalculateManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "MtsCalculateManageRestController", description = "정산관리 - mts문자 통계")
public class MtsCalculateManageRestController {

    @Autowired
    protected MtsCalculateManageService mtsCalculateManageService;

    /**
     * 메서드 설명 : 기업고객관리 - 목록 조회
     */
    @ApiOperation(value = "admin-정산관리 - mts발송 통계 가져오기", notes = "admin-정산관리 - mts발송 통계 가져오기")
    @PostMapping("/admin-api/mtsCalculate/sndngStat")
    public Object sndngStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //        log.debug("LOG확인 >>>>>>>>>>>>>>>>>");
        //        log.debug("<<<<<<<<<<<<<<<" + mjsonParams);

        objRetParams = mtsCalculateManageService.sndngStat(mjsonParams);

        return objRetParams;
    }
}
