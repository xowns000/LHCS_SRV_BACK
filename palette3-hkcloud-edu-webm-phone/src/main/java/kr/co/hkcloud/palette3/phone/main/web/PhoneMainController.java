package kr.co.hkcloud.palette3.phone.main.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.properties.phone.PhoneProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "PhoneMainController",
     description = "전화메인 컨트롤러")
public class PhoneMainController
{
    private final PhoneProperties phoneProperties;


    /**
     * 전화메인 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "전화메인 페이지",
                  notes = "전화메인 페이지로 이동한다")
    @GetMapping("/phone/web/main")
    public String movePhoneMain(Model model) throws TelewebWebException
    {
        log.debug("movePhoneMain");
        
        model.addAttribute( "ctiServerIp" , phoneProperties.getCtiServer().getUrl().replace("http://", "") );
        model.addAttribute( "ctiServerPort" , phoneProperties.getCtiServer().getCtiPort());
        model.addAttribute( "ctiSecretPw" , phoneProperties.getCtiServer().getCtiSecretPw());
        model.addAttribute( "ctiOutboundNumber" , phoneProperties.getCtiServer().getCtiOutboundNumber());
        
        model.addAttribute("isMainResveCallEnabled", phoneProperties.isMainResveCallEnabled());
        model.addAttribute("isMainCheckBoardEnabled", phoneProperties.isMainCheckBoardEnabled());
        return "phone/main/phone-main";
    }


    @ApiOperation(value = "소프트폰 페이지",
                  notes = "소프트폰 페이지")
    @GetMapping("/phone/web/main/soft-phone-manage")
    public String moveInfraCti() throws TelewebWebException
    {
        log.debug("move-soft-phone-manage");
        return "phone/main/soft-phone-manage";
    }


    @ApiOperation(value = "고객검색팝업",
                  notes = "고객검색팝업")
    @GetMapping("/phone/main/web/cstmrinfo-popup")
    public String movePhoneMainCstmrinfoPopup() throws TelewebWebException
    {
        log.debug("move-soft-phone-manage");
        return "phone/main/cstmrinfo-popup";
    }


    @ApiOperation(value = "호전환 페이지",
                  notes = "호전환 페이지")
    @GetMapping("/phone/web/main/phone-cnvrs-popup")
    public String movePhoneMainphoneCnvrsPopup() throws TelewebWebException
    {
        log.debug("move-soft-phone-manage");
        return "phone/main/phone-cnvrs-popup";
    }


    @ApiOperation(value = "아웃바운드상세페이지",
                  notes = "아웃바운드상세페이지")
    @GetMapping("/phone/main/web/outbound-detail-popup")
    public String moveOutboundDetailPopup() throws TelewebWebException
    {
        log.debug("move-outbound-detail-popup");
        return "phone/main/outbound-detail-popup";
    }


    @ApiOperation(value = "콜백상세페이지",
                  notes = "콜백상세페이지")
    @GetMapping("/phone/main/web/callback-detail-popup")
    public String moveCallbackDetailPopup() throws TelewebWebException
    {
        log.debug("callback-detail-popup");
        return "phone/main/callback-detail-popup";
    }


    @ApiOperation(value = "스케줄상세페이지",
                  notes = "스케줄상세페이지")
    @GetMapping("/phone/main/web/schedule-detail-popup")
    public String moveScheduleDetailPopup() throws TelewebWebException
    {
        log.debug("move-schedule-detail-popup");
        return "phone/main/schedule-detail-popup";
    }


    @ApiOperation(value = "설문지조사팝업",
                  notes = "설문지조사팝업")
    @GetMapping("/phone/main/web/qestnr-regist-popup")
    public String moveQestnrRegistPopup() throws TelewebWebException
    {
        log.debug("move-qestnr-regist-popup");
        return "phone/main/qestnr-regist-popup";
    }


    // 테스트용
    @ApiOperation(value = "설문지조사팝업",
                  notes = "설문지조사팝업")
    @GetMapping("/phone/main/web/soft-phone-manage")
    public String movesoftphonemanage() throws TelewebWebException
    {
        log.debug("move-qestnr-regist-popup");
        return "phone/main/soft-phone-manage";
    }
}
