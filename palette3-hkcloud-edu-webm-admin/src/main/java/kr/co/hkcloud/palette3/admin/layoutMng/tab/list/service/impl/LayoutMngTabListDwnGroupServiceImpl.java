package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListDwnGroupService;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
@Service("dwnGroupService")
public class LayoutMngTabListDwnGroupServiceImpl implements LayoutMngTabListDwnGroupService {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO twbComDao;
    private final LayoutMngTabListService layoutMngTabListService;

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertUpdateDwnGroup(TelewebJSON jsonParam) throws TelewebAppException {
        log.info("jsonParam>>>>>>>>>" + jsonParam);
        TelewebJSON objRetParam = new TelewebJSON();
        String tabId = jsonParam.getString("TAB_ID");
        String userId = jsonParam.getString("USER_ID");

        // 1. listDwnGroupParams에 list_ID와 list_dwn_group_id가 있는지 구분 하여 생성, 수정 분기
        JSONArray arrDwnGroupParams = jsonParam.getDataObject("listDwnGroupParams");

        for (int i = 0; i < arrDwnGroupParams.size(); i++) {
            JSONObject objDwnGroupParam = (JSONObject) arrDwnGroupParams.get(i);
            objDwnGroupParam.put("TAB_ID", tabId);
            objDwnGroupParam.put("USER_ID", userId);
            TelewebJSON dwnGroupParam = new TelewebJSON();
            // 등록,수정 분기
            if (objDwnGroupParam.getString("LIST_ID") == "null" || StringUtils.isEmpty(objDwnGroupParam.getString("LIST_ID"))
                || objDwnGroupParam.getString("LIST_DWN_GROUP_ID") == "null" || StringUtils.isEmpty(
                objDwnGroupParam.getString("LIST_DWN_GROUP_ID"))) {
                // insert
                dwnGroupParam.setDataObject(TwbCmmnConst.G_DATA, objDwnGroupParam);
                // 하위그룹 저장
                dwnGroupParam = insertDwnGroup(dwnGroupParam);
                // 하위그룹상세 저장 시작
                if (!ObjectUtils.isEmpty(dwnGroupParam.getDataObject(TwbCmmnConst.G_DATA))) {
                    TelewebJSON insertDwnGroupDtlParam = new TelewebJSON();
                    insertDwnGroupDtlParam.setDataObject("listDwnGroupDtlParams", jsonParam.getDataObject("listDwnGroupDtlParams"));
                    insertDwnGroupDtlParam.setString("LIST_DWN_GROUP_ID", dwnGroupParam.getString("LIST_DWN_GROUP_ID"));
                    insertDwnGroupDtlParam.setString("USER_ID", dwnGroupParam.getString("USER_ID"));
                    insertDwnGroupDtlParam.setString("GROUP_SORT_ORD", dwnGroupParam.getString("GROUP_SORT_ORD"));
                    // 하위그룹상세 삭제, 등록
                    objRetParam = deleteInsertDwnGroupDtl(insertDwnGroupDtlParam);
                    if (objRetParam.getHeaderBoolean("ERROR_FLAG")) {
                        objRetParam.setHeader("ERROR_FLAG", true);
                        objRetParam.setHeader("ERROR_MSG", "하위그룹 상세 등록에 실패했습니다.");
                    } else {
                        objRetParam.setHeader("ERROR_FLAG", false);
                        objRetParam.setHeader("ERROR_MSG", "하위그룹 상세 등록에 성공했습니다.");
                    }
                } else {
                    objRetParam.setHeader("ERROR_FLAG", true);
                    objRetParam.setHeader("ERROR_MSG", "하위그룹 등록에 실패했습니다.");
                }
            } else {
                // update
                dwnGroupParam.setDataObject(TwbCmmnConst.G_DATA, objDwnGroupParam);
                objRetParam = updateDwnGroup(dwnGroupParam);
                if (!objRetParam.getHeaderBoolean("ERROR_FLAG")) {
                    TelewebJSON updateDwnGroupDtlParam = new TelewebJSON();
                    updateDwnGroupDtlParam.setDataObject("listDwnGroupDtlParams", jsonParam.getDataObject("listDwnGroupDtlParams"));
                    updateDwnGroupDtlParam.setString("LIST_DWN_GROUP_ID", dwnGroupParam.getString("LIST_DWN_GROUP_ID"));
                    updateDwnGroupDtlParam.setString("USER_ID", dwnGroupParam.getString("USER_ID"));
                    updateDwnGroupDtlParam.setString("GROUP_SORT_ORD", dwnGroupParam.getString("GROUP_SORT_ORD"));
                    objRetParam = deleteInsertDwnGroupDtl(updateDwnGroupDtlParam);
                }
            }
        }

        return objRetParam;
    }

    public TelewebJSON insertDwnGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        objRetParam.setHeader("ERROR_FLAG", false);

        objRetParam = layoutMngTabListService.insertList(jsonParam);         // LIST_ID 생성 / PLT_LAYOUT_LIST 테이블 INSERT
        jsonParam.setString("LIST_ID", objRetParam.getString("LIST_ID"));
        String listDwnGroupId = String.valueOf(innbCreatCmmnService.createSeqNo("LIST_DWN_GROUP_ID"));
        jsonParam.setString("LIST_DWN_GROUP_ID", listDwnGroupId);
        //PLT_LAYOUT_LIST_GROUP 테이블 INSERT
        twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper", "insertDwnGroup", jsonParam);
        // 리턴 파라미터에 LIST_ID, LIST_GROUP_ID 세팅
        objRetParam.setString("LIST_ID", jsonParam.getString("LIST_ID"));
        objRetParam.setString("LIST_DWN_GROUP_ID", jsonParam.getString("LIST_DWN_GROUP_ID"));
        objRetParam.setString("USER_ID", jsonParam.getString("USER_ID"));
        objRetParam.setString("GROUP_SORT_ORD", jsonParam.getString("GROUP_SORT_ORD"));

        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID")) || StringUtils.isEmpty(jsonParam.getString("LIST_DWN_GROUP_ID"))) {
            objRetParam.setHeader("ERROR_FLAG", false);
            objRetParam.setHeader("ERROR_MSG", "LIST_ID or LIST_DWN_GROUP_ID is null");
        }
        return objRetParam;
    }

    public TelewebJSON updateDwnGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        objRetParam.setHeader("ERROR_FLAG", false);

        //PLT_LAYOUT_LIST_DWN_GROUP 테이블 update
        twbComDao.update("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper", "updateListDwnGroup",
            jsonParam);
        // 리턴 파라미터에 LIST_ID, LIST_GROUP_ID 세팅

        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID")) || StringUtils.isEmpty(jsonParam.getString("LIST_DWN_GROUP_ID"))) {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "LIST_ID or LIST_DWN_GROUP_ID is null");
        } else {
            objRetParam.setHeader("ERROR_FLAG", false);
        }
        return objRetParam;
    }

    public TelewebJSON deleteInsertDwnGroupDtl(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        objRetParam.setHeader("ERROR_FLAG", false);
        JSONArray arrDwnGroupDtlParams = jsonParam.getDataObject("listDwnGroupDtlParams");
        String listDwnGroupId = jsonParam.getString("LIST_DWN_GROUP_ID");
        String groupSortOrd = jsonParam.getString("GROUP_SORT_ORD");
        String userId = jsonParam.getString("USER_ID");

        if (!StringUtils.isEmpty(jsonParam.getString("LIST_DWN_GROUP_ID"))) { // 최초 한번만 디테일 전체 삭제
            twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper", "deleteListDwnGroupDtl",
                jsonParam);
        }

        for (int i = 0; i < arrDwnGroupDtlParams.size(); i++) {
            TelewebJSON insertDtlParam = new TelewebJSON();
            JSONObject objDtlParam = (JSONObject) arrDwnGroupDtlParams.get(i);
            if (groupSortOrd.equals(objDtlParam.getString("GROUP_SORT_ORD"))) {
                objDtlParam.put("LIST_DWN_GROUP_ID", listDwnGroupId);
                objDtlParam.put("USER_ID", userId);
                insertDtlParam.setDataObject(TwbCmmnConst.G_DATA, objDtlParam);

                insertDtlParam.setInt("LIST_DWN_GROUP_DTL_ID", innbCreatCmmnService.createSeqNo("LIST_DWN_GROUP_DTL_ID"));
                objRetParam = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper",
                    "insertListDwnGroupDtl", insertDtlParam);
            }
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteDwnGroup(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        if((!StringUtils.isEmpty(jsonParam.getString("LIST_DWN_GROUP_ID")) || !"null".equals(jsonParam.getString("LIST_DWN_GROUP_ID")))
        && (!StringUtils.isEmpty(jsonParam.getString("LIST_ID")) || !"null".equals(jsonParam.getString("LIST_ID")))){
            objRetParam = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper",
                "deleteListDwnGroupDtl", jsonParam);
            objRetParam = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper",
                "deleteListDwnGroup", jsonParam);
            objRetParam = layoutMngTabListService.deleteList(jsonParam);
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteDwnGroupDtl(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper", "deleteListDwnGroupDtl",
            jsonParam);
        return new TelewebJSON();
    }

}
