package kr.co.hkcloud.palette3.admin.lkag.custco.api;

import kr.co.hkcloud.palette3.admin.lkag.custco.app.LkagCustcoService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.custco.api
 * fileName       : LkagMstRestController
 * author         : 고객사별 연동
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
public class LkagCustcoRestController {
    private final LkagCustcoService lkagCustcoService;

    @PostMapping("/admin-api/lkag/custco/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.selectList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/selectTblList")
    public Object selectTblList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.selectTblList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/treeHeadersList")
    public Object selectHeadersList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.treeHeadersList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/treeParamsList")
    public Object treeParamsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.treeParamsList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/insert")
    public Object insert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.insert(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/custco/update")
    public Object update(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.update(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/insertParamsTbl")
    public Object insertParamsTbl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.insertParamsTbl(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/insertHeaders")
    public Object insertHeaders(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.insertHeaders(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/updateHeaders")
    public Object updateHeaders(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.updateHeaders(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/delete")
    public Object delete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.delete(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/custco/deleteHeaders")
    public Object deleteHeaders(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.deleteHeaders(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/custco/dpcn-chk")
    public Object dpcnChkMstNm(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = lkagCustcoService.dpcnChk(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

}
