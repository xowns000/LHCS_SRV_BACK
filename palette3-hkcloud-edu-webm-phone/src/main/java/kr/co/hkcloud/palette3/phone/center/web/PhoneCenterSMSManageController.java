package kr.co.hkcloud.palette3.phone.center.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : SMS관리
 * 파 일 명 : SmsMngController.java
 * 작 성 자 : 김대찬
 * 작 성 일 : 2020.01.06
 * 설    명 : SMS관리 컨트롤러 클래스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
@Slf4j
@Controller
@Api(value = "phoneCenterSMSManageController",
     description = "SMS관리 컨트롤러")
public class PhoneCenterSMSManageController
{
    /**
     * SMS관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "SMS관리 페이지",
                  notes = "SMS관리 이동한다")
    @GetMapping("/phone/center/web/sms-manage")
    public String moveSmsMng() throws TelewebWebException
    {
        log.debug("phoneCenterSMSManage");
        return "phone/center/phone-center-sms-manage";
    }

}
