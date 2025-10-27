package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListGroupService;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl
 * fileName       :
 * author         : njy
 * date           : 2024-04-01
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-01           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("listGroupService")
public class LayoutMngTabListGroupServiceImpl implements LayoutMngTabListGroupService {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO twbComDao;
    private final LayoutMngTabListService layoutMngTabListService;


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertUpdateListGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
//        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID")); // 고객사 테넌트 설정

        JSONArray arrParam = jsonParam.getDataObject(TwbCmmnConst.G_DATA); //jsonArray

        // 같은 그룹끼리 묶어줌
        String userId = jsonParam.getString("USER_ID");
        JSONArray arrGroupParam = new JSONArray();
        JSONArray tempArr = new JSONArray();
        for (int i = 0; i < arrParam.size(); i++) { // arr 길이 만큼 순회
            JSONObject objDtlParam = (JSONObject) arrParam.get(i); // dtl JSON객체
            objDtlParam.put("USER_ID", userId); // USER_ID 추가
            if (ObjectUtils.isEmpty(tempArr)) { //tempArr가 비어있으면 최초 값 추가
                tempArr.add(objDtlParam);
            } else {
                if (objDtlParam.getString("GROUP_SORT_ORD").equals(tempArr.getJSONObject(0).getString("GROUP_SORT_ORD"))) {
                    tempArr.add(objDtlParam);
                } else {
                    arrGroupParam.add(tempArr);
                    tempArr.clear();
                    tempArr.add(objDtlParam);
                }
            }
        }
        arrGroupParam.add(tempArr);

        for (int i = 0; i < arrGroupParam.size(); i++) {
            JSONArray arrSameGroup = arrGroupParam.getJSONArray(i);
            TelewebJSON insertGroupParam = new TelewebJSON();
            insertGroupParam.setDataObject(TwbCmmnConst.G_DATA, arrSameGroup);
            if (arrSameGroup.getJSONObject(0).containsKey("LIST_GROUP_ID")) {
                String listGroupId = arrSameGroup.getJSONObject(0).getString("LIST_GROUP_ID");
                objRetParam = updateGroup(insertGroupParam);
                if (!objRetParam.getHeaderBoolean("ERROR_FLAG")) {
                    objRetParam = deleteInsertGroupDtl(insertGroupParam);
                } else {
                    objRetParam.setHeader("ERROR_FLAG", true);
                    objRetParam.setHeader("ERROR_MSG", insertGroupParam.getHeaderString("ERROR_MSG"));
                    return objRetParam;
                }
            } else {
                String listGroupId = String.valueOf(innbCreatCmmnService.createSeqNo("LIST_GROUP_ID"));
                insertGroupParam.setString("LIST_GROUP_ID", listGroupId);

                objRetParam = insertGroup(insertGroupParam);
                insertGroupParam.setString("LIST_ID", objRetParam.getString("LIST_ID"));
                insertGroupParam.setString("TAB_ID", objRetParam.getString("TAB_ID"));
                // 하위 테이블 delete/insert
                if (!objRetParam.getHeaderBoolean("ERROR_FLAG")) {
                    objRetParam = deleteInsertGroupDtl(insertGroupParam);
                } else {
                    objRetParam.setHeader("ERROR_FLAG", true);
                    objRetParam.setHeader("ERROR_MSG", insertGroupParam.getHeaderString("ERROR_MSG"));
                    return objRetParam;
                }
            }
        }
        return objRetParam;
    }

    public TelewebJSON insertGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        objRetParam.setHeader("ERROR_FLAG", false);
        jsonParam.setString("TAB_ID", jsonParam.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(0).getString("TAB_ID"));

        objRetParam = layoutMngTabListService.insertList(jsonParam);         // LIST_ID 생성 / PLT_LAYOUT_LIST 테이블 INSERT
        jsonParam.setInt("LIST_ID", objRetParam.getInt("LIST_ID"));
        //PLT_LAYOUT_LIST_GROUP 테이블 INSERT
        twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper", "insertListGroup", jsonParam);
        // 리턴 파라미터에 LIST_ID, LIST_GROUP_ID 세팅
        objRetParam.setInt("LIST_ID", jsonParam.getInt("LIST_ID"));

        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID")) || StringUtils.isEmpty(jsonParam.getString("LIST_GROUP_ID"))) {
            objRetParam.setHeader("ERROR_FLAG", false);
            objRetParam.setHeader("ERROR_MSG", "LIST_ID or LIST_GROUP_ID is null");
        }
        return objRetParam;
    }

    public TelewebJSON updateGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        objRetParam.setHeader("ERROR_FLAG", false);
        objRetParam = twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper", "updateListGroup",
            jsonParam);
        return objRetParam;
    }

    public TelewebJSON deleteInsertGroupDtl(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        for (int i = 0; i < jsonParam.getDataObject(TwbCmmnConst.G_DATA).size(); i++) {
            TelewebJSON insertDeleteParam = new TelewebJSON();
            insertDeleteParam.setDataObject(TwbCmmnConst.G_DATA, jsonParam.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(i));
            insertDeleteParam.setString("LIST_ID", jsonParam.getString("LIST_ID"));
            insertDeleteParam.setString("LIST_GROUP_ID", jsonParam.getString("LIST_GROUP_ID"));

            if (!StringUtils.isEmpty(insertDeleteParam.getString("LIST_GROUP_DTL_ID")) && i == 0) { // 최초 한번만 디테일 삭제
                twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper", "deleteListGroupDtl",
                    insertDeleteParam);
            }
            insertDeleteParam.setInt("LIST_GROUP_DTL_ID", innbCreatCmmnService.createSeqNo("LIST_GROUP_DTL_ID"));
            objRetParam = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper",
                "insertListGroupDtl", insertDeleteParam);
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteListGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        objRetParam.setHeader("ERROR_FLAG", false);
//        TenantContext.setCurrentTenant(jsonParam.getString("SCHEMA_ID"));
        // 삭제 순서 - PLT_LAYOUT_LIST_GROUP_DTL > PLT_LAYOUT_LIST_GROUP > PLT_LAYOUT_LIST
        objRetParam = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper",
            "deleteListGroupDtl", jsonParam);
        objRetParam = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper", "deleteListGroup",
            jsonParam);
        objRetParam = layoutMngTabListService.deleteList(jsonParam);
        return objRetParam;
    }
}
