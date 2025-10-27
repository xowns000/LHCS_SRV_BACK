package kr.co.hkcloud.palette3.admin.layoutMng.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.service.LayoutMngBscInfoService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jcodec.common.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.app
 * fileName       :
 * author         : njy
 * date           : 2024-03-20
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("LayoutManageService")
public class LayoutMngBscInfoServiceImpl implements LayoutMngBscInfoService {

    private final TwbComDAO twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;

    @Override
    public TelewebJSON selectLayoutList(TelewebJSON jsonParam) throws TelewebAppException {
        //        JSONArray arrListItems = new JSONArray();
        TelewebJSON objCustcoSchema = selectCertCustcoInfo4Layout(jsonParam); // 고객사별(스키마별) 조회를 위한 스키마 조회용 TelewebJSON 객체
        TelewebJSON objRetParam = new TelewebJSON(); // 반환 파라미터

        // TODO 스키마별로 테이블 생성 후 진행
        JSONArray arrayJson = new JSONArray();
        for (int i = 0; i < objCustcoSchema.getSize(); i++) { //
            JSONObject obj = (JSONObject) objCustcoSchema.getDataObject("DATA").get(i);
            TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
            jsonParam.setString("CUSTCO_ID", obj.getString("CUSTCO_ID"));
            jsonParam.setString("SCHEMA_ID", obj.getString("SCHEMA_ID"));
            if (!StringUtils.isEmpty(jsonParam.getString("CUSTCO_ID"))) {
                TelewebJSON arrList = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper",
                    "selectLayoutList", jsonParam);
                if (arrList.getSize() > 0) {
                    for (int j = 0; j < arrList.getSize(); j++) {
                        if( arrList.getDataObject("DATA").size() > 0 ) {
                            arrayJson.add( arrList.getDataObject("DATA").get(j) );
                        }
                    }
                }
            }
        }
        objRetParam.setDataObject( arrayJson );
        //        objRetParam.setDataObject("DATA", arrListItems);
        objRetParam.setHeader("ERROR_FLAG", false);
        return objRetParam;
    }

    @Override
    public TelewebJSON selectLayoutClsfList(TelewebJSON jsonParam) throws TelewebAppException {
        //레이아웃 분류 테이블은 CUSTCO 스키마로 이동
        TelewebJSON objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper",
            "selectLayoutClsfList", jsonParam);
        return objRetParam;
    }

    @Override
    public TelewebJSON selectCertCustcoInfo4Layout(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper",
            "selectCertCustcoInfo4Layout", jsonParam);
        return objRetParam;
    }

    @Override
    public TelewebJSON selectLkagByCertCustco(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper",
            "selectLkagByCertCustco", jsonParam);
        return objRetParam;
    }

    @Override
    public TelewebJSON insertUpdateBscInfo(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        if (!StringUtils.isEmpty(jsonParam.getString("SCHEMA_ID"))) {
            TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
            if (StringUtils.isEmpty(jsonParam.getString("LAYOUT_ID"))) {
                int layoutId = innbCreatCmmnService.createSeqNo("LAYOUT_ID"); // 레이아웃 ID 생성
                jsonParam.setInt("LAYOUT_ID", layoutId);                              // 레이아웃 ID 세팅
                objRetParam = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper", "insertBscInfo",
                    jsonParam);
            } else {
                objRetParam = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper", "updateBscInfo",
                    jsonParam);
            }
            objRetParam.setInt("LAYOUT_ID", jsonParam.getInt("LAYOUT_ID"));
            objRetParam.setString("CERT_CUSTCO_ID", jsonParam.getString("CERT_CUSTCO_ID"));
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteBscInfo(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        JSONArray arr = (JSONArray) jsonParam.getDataObject("DATA");
        for (int i = 0; i < arr.size(); i++) {
            TelewebJSON delParam = new TelewebJSON();
            delParam.setDataObject("DATA", (JSONObject) arr.get(i));
            TenantContext.setCurrentTenant(delParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
            log.info("===> TenantContext.getCurrentTenant(): " + TenantContext.getCurrentTenant());
            objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper", "selectTabCnt", delParam);
            if (objRetParam.getInt("COUNT") > 0) {
                objRetParam.setHeader("ERROR_FLAG", true);
                objRetParam.setHeader("ERROR_MSG", "하위 탭이 존재하여 삭제할 수 없습니다.");
            } else {
                objRetParam = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper", "deleteBscInfo",
                    delParam);
            }
        }

        return objRetParam;
    }

    @Override
    public TelewebJSON updateUseLayout(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        JSONArray arrUseLayoutList = jsonParam.getDataObject("setUseLayoutList");
        for (int i = 0; i < arrUseLayoutList.size(); i++) {
            JSONObject objUseLayout = (JSONObject) arrUseLayoutList.get(i);
            TenantContext.setCurrentTenant(objUseLayout.getString("SCHEMA_ID"));
            TelewebJSON useLayout = new TelewebJSON();
            useLayout.setDataObject(TwbCmmnConst.G_DATA, objUseLayout);

            objRetParam = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.dao.LayoutMngBscInfoMapper", "updateUseLayout",
                useLayout);
        }
        return objRetParam;
    }

}
