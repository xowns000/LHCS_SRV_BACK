package kr.co.hkcloud.palette3.admin.lkag.mng.request.api;

import java.util.List;
import java.util.Map;
import kr.co.hkcloud.palette3.admin.lkag.mng.request.app.LkagMngRequestService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
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
public class LkagMngRequestRestController {

    private final PaletteJsonUtils paletteJsonUtils;
    private final LkagMngRequestService LkagMngRequestService;

    @PostMapping("/admin-api/lkag/mng/request/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.selectList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/request/treeList")
    public Object treeList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.selectList(mjsonParams);

        List<Map<String, Object>> tree = null;
        if (objRetParams.getDataObject("DATA").size() > 0) {
            String upId = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_ARTCL_ID");
            tree = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), upId, "PARAM_ARTCL_ID",
                "UP_ARTCL_ID", "DSPLY_NM", "SORT");
            objRetParams.setDataObject("TREE", paletteJsonUtils.getJsonArrayFromList(tree));
        }

        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/request/insert")
    public Object insert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.insert(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/request/update")
    public Object update(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.update(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/request/delete")
    public Object delete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.delete(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mng/request/dpcn-chk")
    public Object dpcnChk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.dpcnChk(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    /**
     * 순서 변경
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @PostMapping("/admin-api/lkag/mng/request/orderUpdate")
    public Object orderUpdate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngRequestService.orderUpdate(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

}
