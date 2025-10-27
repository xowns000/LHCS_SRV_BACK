package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListService;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListSttsService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl
 * fileName       :
 * author         : njy
 * date           : 2024-03-28
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-28           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("sttsService")
public class LayoutMngTabListSttsServiceImpl implements LayoutMngTabListSttsService {
    private final TwbComDAO twbComDao;
    private final LayoutMngTabListService layoutMngTabListService;
    @Override
    public TelewebJSON selectStts(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID"));
        objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSttsMapper",
            "selectStts", jsonParam);
        return objRetParam;
    }

    @Override
    public TelewebJSON insertUpdateStts(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
        if (StringUtils.isEmpty(jsonParam.getString("RSPNS_ARTCL_ID"))) {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "응답 항목은 필수 입니다.");
        } else {
            if (StringUtils.isEmpty(jsonParam.getString("LIST_ID"))) {
                objRetParam = layoutMngTabListService.insertList(jsonParam);         // LIST_ID 생성 / PLT_LAYOUT_LIST 테이블 INSERT
                jsonParam.setInt("LIST_ID", objRetParam.getInt("LIST_ID"));     // 생성된 LIST_ID jsonParam에 set
                objRetParam = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSttsMapper",
                    "insertStts", jsonParam);                             // LIST_ID로 PLT_LAYOUT_LIST_STTS INSERT
                objRetParam.setInt("__KEY_ID", jsonParam.getInt("LIST_ID"));
            } else {
                objRetParam = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSttsMapper",
                    "updateStts", jsonParam);
                objRetParam.setInt("__KEY_ID", jsonParam.getInt("LIST_ID"));
            }
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteStts(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 선택한 고객사의 SCHEMA
        if (!StringUtils.isEmpty(jsonParam.getString("LIST_ID")) && !StringUtils.isEmpty(jsonParam.getString("TAB_ID"))) {
            twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSttsMapper", "deleteStts",
                jsonParam);
            layoutMngTabListService.deleteList(jsonParam);
        }
        return objRetParam;
    }

}
