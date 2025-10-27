package kr.co.hkcloud.palette3.setting.customer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCautionCustomerListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingAgentManageRestController",
    description = "설정주의고객정보목록  REST 컨트롤러")
public class SettingCautionCustomerListRestController {
    private final SettingCautionCustomerListService settingCautionCustomerListService;

    @PrvcAspectAnotation(value = "CAUTION_CUST_LIST") //개인정보 Logging관련
    @ApiOperation(value = "설정주의고객목록", notes = "설정 주의고객 목록을 조회한다")
    @PostMapping("/api/setting/customer/cautionInfo/selectCautionCustList")
    public Object selectCautionCustList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingCautionCustomerListService.selectCautionCustList(mjsonParams);
    }
}
