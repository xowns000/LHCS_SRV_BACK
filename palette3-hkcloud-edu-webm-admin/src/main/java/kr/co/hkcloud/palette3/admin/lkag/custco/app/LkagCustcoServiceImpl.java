package kr.co.hkcloud.palette3.admin.lkag.custco.app;

import java.util.List;
import java.util.Map;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.app
 * fileName       : LkagMstServiceImpl
 * author         : KJD
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
@Service("lkagCustcoService")
public class LkagCustcoServiceImpl implements LkagCustcoService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository;
    private final PaletteJsonUtils paletteJsonUtils;

    @Override
    public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "selectList", jsonParams);
    }

    @Override
    public TelewebJSON selectTblList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "selectTblList", jsonParams);
    }

    @Override
    public TelewebJSON selectHeadersList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "selectHeadersList", jsonParams);
    }

    @Override
    public TelewebJSON treeHeadersList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = this.selectHeadersList(jsonParams);
        List<Map<String, Object>> tree = null;
        if (objRetParams.getDataObject("DATA").size() > 0) {
            String upId = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_ID");
            tree = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), upId, "HEAD_ID", "UP_ID", "PARAM_COL_NM", "HEAD_ID");
            objRetParams.setDataObject("TREE", paletteJsonUtils.getJsonArrayFromList(tree));
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON treeParamsList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "treeParamsList", jsonParams);
        List<Map<String, Object>> tree = null;
        if (objRetParams.getDataObject("DATA").size() > 0) {
            String upId = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_ARTCL_ID");
            tree = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), upId, "PARAM_ARTCL_ID", "UP_ARTCL_ID", "COL_NM", "SORT");
            objRetParams.setDataObject("TREE", paletteJsonUtils.getJsonArrayFromList(tree));
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        jsonParams.setInt("CERT_CUSTCO_LKAG_ID", innbCreatCmmnService.createSeqNo("CERT_CUSTCO_LKAG_ID"));
        jsonParams.setInt("__KEY_ID", jsonParams.getInt("CERT_CUSTCO_LKAG_ID"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "insert", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));

        return objRetParams;
    }

    @Override
    public TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "update", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        mobjDao.delete("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "deleteHeadersInit", jsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "delete", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON insertParamsTbl(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "deleteParamsTbl", jsonParams);
        if (StringUtils.isNotEmpty(jsonParams.getString("LNKG_TBL_ID"))) {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "insertParamsTbl", jsonParams);
        }
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON insertHeaders(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        jsonParams.setInt("HEAD_ID", innbCreatCmmnService.createSeqNo("HEAD_ID"));
        jsonParams.setInt("__KEY_ID", jsonParams.getInt("HEAD_ID"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "insertHeaders", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON updateHeaders(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "updateHeaders", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON deleteHeaders(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        if (jsonParams.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("ITEM_LIST", jsonParams.getDataObject(TwbCmmnConst.G_DATA));
            JSONArray arrParam = new JSONArray();
            arrParam.add(jsonObj);
            jsonParams.setDataObject(arrParam);
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "deleteHeaders", jsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "선택된 항목이 없습니다.");
        }
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "dpcnChk", jsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;
    }
}
