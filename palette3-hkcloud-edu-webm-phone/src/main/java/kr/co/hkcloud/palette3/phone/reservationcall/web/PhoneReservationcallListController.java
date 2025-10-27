package kr.co.hkcloud.palette3.phone.reservationcall.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "phoneReservationcallListController",
     description = "예약콜조회 컨트롤러")
public class PhoneReservationcallListController
{
    /**
     * 예약콜조회 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "예약콜조회 페이지",
                  notes = "예약콜조회 이동한다")
    @GetMapping("/phone/reservationcall/web/inqire")
    public String movePhoneReservationcallList() throws TelewebWebException
    {
        log.debug("movePhoneReservationcallList");
        return "phone/reservationcall/phone-reservationcall-list";
    }
}
