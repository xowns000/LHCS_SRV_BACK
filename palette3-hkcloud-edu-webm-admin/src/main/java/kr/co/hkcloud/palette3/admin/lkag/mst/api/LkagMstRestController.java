package kr.co.hkcloud.palette3.admin.lkag.mst.api;

import kr.co.hkcloud.palette3.admin.lkag.mst.app.LkagMstService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.api
 * fileName       : LkagMstRestController
 * author         : 연동_마스터
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LkagMstRestController {

    private final LkagMstService LkagMstService;

    @PostMapping("/admin-api/lkag/mst/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.selectList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mst/selectBox")
    public Object selectBox(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.selectBox(mjsonParams);
        return objRetParams;
    }

    @PostMapping("/admin-api/lkag/mst/insert")
    public Object insert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.insert(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mst/update")
    public Object update(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.update(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mst/delete")
    public Object delete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.delete(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mst/dpcn-chk")
    public Object dpcnChkMstNm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMstService.dpcnChkMstNm(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

}
