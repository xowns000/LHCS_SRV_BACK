package kr.co.hkcloud.palette3.phone.history.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "PhoneHistoryListController",
     description = "상담이력관리 컨트롤러")
public class PhoneHistoryInqireController
{

    /**
     * 상담이력관리 페이지 이동(공제지원)
     * 
     * @return
     */
    @ApiOperation(value = "상담이력관리 페이지",
                  notes = "상담이력관리 이동한다")
    @GetMapping("/phone/history/web/manage")
    public String MoveCnslHistMngDe() throws TelewebWebException
    {
        log.debug("MoveCnslHistMngDe");
        return "phone/history/phone-history-manage";
    }


    /**
     * 상담이력조회 페이지 이동(공제지원)
     * 
     * @return
     */
    @ApiOperation(value = "상담이력조회 페이지",
                  notes = "상담이력조회 이동한다")
    @GetMapping("/phone/history/web/inqire")
    public String MoveCnslHistSearchDe() throws TelewebWebException
    {
        log.debug("MoveCnslHistSearchDe");
        return "phone/history/phone-history-inqire";
    }


    /**
     * 조합검색 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "조합검색 페이지",
                  notes = "조합검색 페이지로 이동한다")
    @GetMapping("/phone/history/web/phone-history-cnfm-no-popup")
    public String moveComOrgzFind() throws TelewebWebException
    {
        log.debug("moveComOrgzFind");
        return "phone/history/phone-history-cnfm-no-popup";
    }

}
