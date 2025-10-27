package kr.co.hkcloud.palette3.setting.customer.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingCustomerInformationListController",
     description = "설정고객정보목록 컨트롤러")
public class SettingCustomerInformationListController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정고객정보목록-화면",
                  notes = "설정고객정보목록 화면으로 이동한다")
    @GetMapping("/setting/customer/web/info-list")
    public String moveSettingCustomerInformationList() throws TelewebWebException
    {
        log.debug("moveSettingCustomerInformationList");
        return "setting/customer/setting-customer-information-list";
    }


    /**
     * 고객정보조회 팝업 페이지 이동
     * 
     * @return
     */
    @ApiOperation(value = "설정고객정보목록-고객정보조회-팝업-화면",
                  notes = "설정고객정보목록-고객정보조회-팝업 화면으로 이동한다")
    @GetMapping("/setting/customer/web/info-list/cstmrinfo-inqire-popup")
    public String moveSettingCustomerInformationListCstmInfoPopup() throws TelewebWebException
    {
        log.debug("moveSettingCustomerInformationListCstmInfoPopup");
        return "setting/customer/setting-customer-information-list-cstmrinfo-inqire-popup";
    }


    @ApiOperation(value = "설정고객정보조회팝업",
                  notes = "설정고객정보조회 팝업 화면으로 이동한다")
    @GetMapping("/setting/customer/web/info-inqire-popup")
    public String moveSettingCustomerInformationInqirePopup() throws TelewebWebException
    {
        log.debug("moveSettingCustomerInformationInqirePopup");
        return "setting/customer/setting-customer-information-inqire-popup";
    }
}
