package kr.co.hkcloud.palette3.admin.lkag.mng.response.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mng.app
 * fileName       : LkagMngServiceImpl
 * author         : KJD
 * date           : 2024-03-25
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-25        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("LkagMngResponseService")
public class LkagMngResponseServiceImpl implements LkagMngResponseService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository;
    private final PaletteJsonUtils paletteJsonUtils;

    HashMap<String, Integer> testInsertKeys = new HashMap<String,Integer>();  //upArtclId

    @Override
    public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "selectList", jsonParams);
    }
    @Override
    public TelewebJSON treeList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = this.selectList(jsonParams);
        List<Map<String, Object>> tree = null;
        if (objRetParams.getDataObject("DATA").size() > 0) {
            String upId = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_ARTCL_ID");
            tree = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), upId, "RSPNS_ARTCL_ID",
                "UP_ARTCL_ID", "DSPLY_NM", "SORT");
            objRetParams.setDataObject("TREE", paletteJsonUtils.getJsonArrayFromList(tree));
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        jsonParams.setInt("RSPNS_ARTCL_ID", innbCreatCmmnService.createSeqNo("RSPNS_ARTCL_ID"));
        jsonParams.setInt("__KEY_ID", jsonParams.getInt("RSPNS_ARTCL_ID"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "insert", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON insertTestJson(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        testInsertKeys = new HashMap<String,Integer>();

        String rspnsData = jsonParams.getString("rspnsData").replaceAll("&#91;", "[").replaceAll("&#93;", "]");
        JSONArray rspnsDataObj = JSONArray.fromObject(rspnsData);

        if( rspnsDataObj.size() > 0){
            for (Object item : rspnsDataObj) {
                JSONObject obj = (JSONObject) item;
                obj.put("ARTCL_NM", obj.getString("COL_NM"));
                obj.put("ENCPT_YN", "N");

                TelewebJSON insertWebJson = new TelewebJSON();
                JSONArray tmpArray = (new JSONArray());
                tmpArray.add(0, obj);
                insertWebJson.setDataObject( tmpArray );
                objRetParams = this.insert( insertWebJson );
                testInsertKeys.put(obj.getString("COL_NM"),  objRetParams.getHeaderInt("__KEY_ID") );
                try {
                    if ("LPIT_OBJECT".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_ARRAY".equals(obj.getString("DATA_TYPE_CD"))) {
                        this.insertTestJsonArray(obj);
                    }
                }catch(net.sf.json.JSONException e) {}
            }
        }

        return objRetParams;
    }

    public void insertTestJsonArray(JSONObject obj) {
        TelewebJSON objRetParams = new TelewebJSON();
        JSONArray jsonArray = (JSONArray)obj.get("children");
        if( jsonArray != null && jsonArray.size() > 0 ){
            for (Object subItem : jsonArray) {
                JSONObject subObj = (JSONObject) subItem;
                subObj.put("ARTCL_NM", subObj.getString("COL_NM"));
                subObj.put("UP_ARTCL_ID", testInsertKeys.get( obj.getString("COL_NM") ) );

                TelewebJSON insertWebJson = new TelewebJSON();
                JSONArray tmpArray = (new JSONArray());
                tmpArray.add(0, subObj);
                insertWebJson.setDataObject( tmpArray );
                objRetParams = this.insert( insertWebJson );
                testInsertKeys.put(subObj.getString("COL_NM"),  objRetParams.getHeaderInt("__KEY_ID") );

                try {
                    if ("LPIT_OBJECT".equals(subObj.getString("DATA_TYPE_CD")) || "LPIT_ARRAY".equals(subObj.getString("DATA_TYPE_CD"))) {
                        this.insertTestJsonArray(subObj);
                    }
                }catch(net.sf.json.JSONException je) {}
            }
        }
    }

    @Override
    public TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "update", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "delete", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON deleteReal(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "deleteReal", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "dpcnChk", jsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;
    }
    /**
     * 순서변경.
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON orderUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        if ("UP".equals(jsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(jsonParams.getString("SORT_ORD")) > 1) {
                jsonParams.setInt("ADD_NUM", 1);
                jsonParams.setInt("SORT_ORD", Integer.parseInt(jsonParams.getString("SORT_ORD")) - 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "UPDATE_OTHER_SORT_ORDER", jsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "UPDATE_SORT_ORDER", jsonParams);
            }
        } else if ("DOWN".equals(jsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(jsonParams.getString("SORT_ORD")) < Integer.parseInt(jsonParams.getString("MAX_SORT_ORD"))) {
                jsonParams.setInt("ADD_NUM", -1);
                jsonParams.setInt("SORT_ORD", Integer.parseInt(jsonParams.getString("SORT_ORD")) + 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "UPDATE_OTHER_SORT_ORDER", jsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "UPDATE_SORT_ORDER", jsonParams);
            }
        }
        return objRetParams;
    }
}
