package kr.co.hkcloud.palette3.admin.lkag.mng.response.api;

import kr.co.hkcloud.palette3.admin.lkag.mng.response.app.LkagMngResponseService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.api
 * fileName       : LkagMstRestController
 * author         : 연동_인터페이스관리
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
public class LkagMngResponseRestController {

    private final PaletteJsonUtils paletteJsonUtils;
    private final LkagMngResponseService LkagMngResponseService;

    @PostMapping("/admin-api/lkag/mng/response/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.selectList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mng/response/treeList")
    public Object treeList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.treeList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/response/insert")
    public Object insert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.insert(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/response/insertTestJson")
    public Object insertTestJson(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.insertTestJson(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/response/update")
    public Object update(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.update(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/response/delete")
    public Object delete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.delete(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mng/response/deleteReal")
    public Object deleteReal(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.deleteReal(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mng/response/dpcn-chk")
    public Object dpcnChk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.dpcnChk(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    /**
     * 순서 변경
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @PostMapping("/admin-api/lkag/mng/response/orderUpdate")
    public Object orderUpdate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngResponseService.orderUpdate(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
}
