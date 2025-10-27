package kr.co.hkcloud.palette3.admin.layoutMng.tab.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.tab.service.LayoutMngTabService;
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

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.service.impl
 * fileName       : LayoutMngTabServiceImpl
 * author         : njy
 * date           : 2024-03-25
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-25           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("LayoutMngDwnListService")
public class LayoutMngTabServiceImpl implements LayoutMngTabService {

    private final TwbComDAO twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    @Override
    public TelewebJSON selectTabList(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "selectTabList", jsonParam);

        return objRetParams;
    }

    @Override
    public TelewebJSON insertUpdateTab(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        JSONArray arrRegTabs = jsonParam.getDataObject(TwbCmmnConst.G_DATA);

        for (int i = 0; i < arrRegTabs.size(); i++) {
            JSONObject objRegTab = (JSONObject) arrRegTabs.get(i);
            TelewebJSON regParam = new TelewebJSON();
            regParam.setDataObject(TwbCmmnConst.G_DATA, objRegTab);
            regParam.setString("USER_ID", jsonParam.getString("USER_ID"));

            TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
            if (StringUtils.isEmpty(regParam.getString("TAB_ID"))) {
                if (!dpcnChkTab(regParam).getHeaderBoolean("ERROR_FLAG")) {
                    regParam.setInt("TAB_ID", innbCreatCmmnService.createSeqNo("TAB_ID")); // TAB_ID 생성
                    objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "insertTab",
                        regParam);
                } else {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "이미 사용중인 이름입니다.");
                    return objRetParams;
                }
            } else {
                objRetParams = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "updateTab", regParam);
            }
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON deleteTab(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "selectListCnt", jsonParam);
        if (objRetParams.getInt("COUNT") > 0) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "하위 목록이 존재하는 탭은 삭제할 수 없습니다.");
        } else {

            twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSrchMapper",
                "deleteSrch", jsonParam);
            objRetParams = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "deleteListInSrch", jsonParam);
            objRetParams = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "deleteTab", jsonParam);
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChkTab(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParam);
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.dao.LayoutMngTabMapper", "dpcnChkTab", jsonParam);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
        }
        return objRetParams;
    }
}
