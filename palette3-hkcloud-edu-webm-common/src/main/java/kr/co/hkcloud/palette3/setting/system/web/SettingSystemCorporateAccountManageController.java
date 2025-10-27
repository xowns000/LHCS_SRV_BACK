package kr.co.hkcloud.palette3.setting.system.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingSystemCorporateAccountManageController",
     description = "설정시스템기업계정관리 컨트롤러")
public class SettingSystemCorporateAccountManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템기업계정관리-화면",
                  notes = "설정시스템기업계정관리 화면으로 이동한다")
    @GetMapping("/setting/system/web/entrprs-acnt-manage")
    public String moveSettingSystemCorporateAccountManage() throws TelewebWebException
    {
        log.debug("moveSettingSystemCorporateAccountManage");
        return "setting/system/setting-system-corporate-account-manage";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템기업계정관리-상세팝업",
                  notes = "설정시스템기업계정관리 상세팝업화면으로 이동한다")
    @GetMapping("/setting/system/web/entrprs-acnt-manage/ctmmny-queue-popup")
    public String moveSettingSystemCorporateAccountManageQueuePopup() throws TelewebWebException
    {
        log.debug("moveSettingSystemCorporateAccountManageDetailPopup");
        return "setting/system/setting-system-corporate-account-manage-queue-popup";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템기업계정관리-상세팝업",
                  notes = "설정시스템기업계정관리 상세팝업화면으로 이동한다")
    @GetMapping("/setting/system/web/entrprs-acnt-manage/ctmmny-detail-popup")
    public String moveSettingSystemCorporateAccountManageDetailPopup() throws TelewebWebException
    {
        log.debug("moveSettingSystemCorporateAccountManageDetailPopup");
        return "setting/system/setting-system-corporate-account-manage-detail-popup";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템기업계정관리-등록팝업",
                  notes = "설정시스템기업계정관리 등록팝업화면으로 이동한다")
    @GetMapping("/setting/system/web/entrprs-acnt-manage/ctmmny-regist-popup")
    public String moveSettingSystemCorporateAccountManageRegistPopup() throws TelewebWebException
    {
        log.debug("moveSettingSystemCorporateAccountManageRegistPopup");
        return "setting/system/setting-system-corporate-account-manage-regist-popup";
    }


    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정시스템기업계정관리-기업정보수정팝업",
                  notes = "설정시스템기업계정관리 기업정보수정팝업으로 이동한다")
    @GetMapping("/setting/system/web/entrprs-acnt-manage/entrprs-info-modify-popup")
    public String moveSettingSystemCorporateAccountManageCorpInfoModifyPopup() throws TelewebWebException
    {
        log.debug("moveSettingSystemCorporateAccountManageCorpInfoModifyPopup");
        return "setting/system/setting-system-corporate-account-manage-corp-info-modify-popup";
    }
}
