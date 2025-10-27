package kr.co.hkcloud.palette3.statistics.chat.api;


import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.statistics.chat.app.StatisticsChatCounselByInquiryTypeService;
import kr.co.hkcloud.palette3.statistics.chat.util.StatisticsChatCounselByInquiryTypeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatCounselByInquiryTypeRestController",
     description = "통계채팅상담(문의유형별) REST 컨트롤러")
public class StatisticsChatCounselByInquiryTypeRestController
{
    private final StatisticsChatCounselByInquiryTypeService   statisticsChatCounselByInquiryTypeService;

//    /**
//     *
//     * @param  mjsonParams
//     * @param  result
//     * @return
//     * @throws TelewebApiException
//     *
//     */
//    @ApiOperation(value = "통계채팅상담(문의유형별)-조회",
//                  notes = "통계채팅상담(문의유형별) 정보를 조회한다.")
//    @PostMapping("/api/statistics/chat/counsel-by-inqire-type/selectByInqryType")
//    public Object selectStatisticsByInqryType(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams.setDataObject("CNT_DATA",statisticsChatCounselByInquiryTypeService.selectStatisticsByInqryType(mjsonParams).getDataObject("DATA"));
//        objRetParams.setDataObject("QSTN_DATA",statisticsChatCounselByInquiryTypeService.selectInqryTypeTree(mjsonParams).getDataObject("DATA"));
//
//        log.info("objRetParams ================>" + objRetParams);
//        return objRetParams;
//    }
}
