package kr.co.hkcloud.palette3.phone.dashboard.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
// import kr.co.hkcloud.palette.core.util.TelewebCommUtil;
import kr.co.hkcloud.palette3.phone.dashboard.app.PhoneDashboardCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneDashboardCallbackRestController",
     description = "아웃바운드 진행 현황 REST 컨트롤러")
public class PhoneDashboardCallbackRestController
{
    private final PhoneDashboardCallbackService phoneDashboardCallbackService;


    /**
     * 아웃바운드 진행 현황를 조회한다.
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "콜백모니터링",
                  notes = "콜백모니터링")
    @PostMapping("/phone-api/dashboard/callback/inqire")
    public Object selectClbkMntrList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneDashboardCallbackService.selectClbkMntrList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
