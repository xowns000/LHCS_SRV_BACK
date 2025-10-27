package kr.co.hkcloud.palette3.phone.dashboard.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.dashboard.app.PhoneDashboardOutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneDashboardOutboundRestController",
     description = "아웃바운드 진행 현황 REST 컨트롤러")
public class PhoneDashboardOutboundRestController
{
    private final PhoneDashboardOutboundService phoneDashboardOutboundService;


    // 인터페이스 호출방식 변경으로 인한 수정
    /**
     * 아웃바운드 모니터링를 조회한다.
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드 모니터링",
                  notes = "아웃바운드 모니터링")
    @PostMapping("/phone-api/dashboard/outbound/inqire")
    public Object selectObndCallList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성 
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneDashboardOutboundService.selectObndCallList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }
}
