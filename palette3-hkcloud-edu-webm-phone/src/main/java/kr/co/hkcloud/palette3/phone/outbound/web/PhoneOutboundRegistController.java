package kr.co.hkcloud.palette3.phone.outbound.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.excel.domain.ExcelResponse.ExcelPropertiesResponse;
import kr.co.hkcloud.palette3.excel.enumer.ExcelBusiType;
import kr.co.hkcloud.palette3.excel.enumer.ExcelPathType;
import kr.co.hkcloud.palette3.excel.util.ExcelRulePropertiesUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "phoneOutboundRegistController",
     description = "아웃바운드등록 컨트롤러")
public class PhoneOutboundRegistController
{
    private final ExcelRulePropertiesUtils excelRulePropertiesUtils;


    /**
     * 아웃바운드등록 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "아웃바운드등록 페이지",
                  notes = "아웃바운드등록 이동한다")
    @GetMapping("/phone/outbound/web/regist")
    public String movePhoneOutboundRegist(Model model) throws TelewebWebException
    {
        log.debug("movePhoneOutboundRegist");

        final ExcelBusiType taskTypeCd = ExcelBusiType.phone;  //전화
        final ExcelPathType pathTypeCd = ExcelPathType.outbound;  //파일
        final ExcelPropertiesResponse excelProperties = excelRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        excelProperties.setUploadUri(new StringBuffer(excelProperties.getUploadUri()).append("/regist/upload").toString());
        log.debug("excelProperties>>>{}", excelProperties);
        model.addAttribute("excelProperties", excelProperties);

        return "phone/outbound/phone-outbound-regist";
    }

}
