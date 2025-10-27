package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl;

import java.util.LinkedList;
import java.util.List;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jcodec.common.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
@Service("layoutMngTabListService")
public class LayoutMngTabListServiceImpl implements LayoutMngTabListService {

    private final TwbComDAO twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;

    @Override
    public TelewebJSON selectWholList(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam); // 반환 파라미터
        // TAB_ID에 연결된 LIST_ID 조회 - jsonParam에 LIST_ID와 LSIT_TYPE이 존재하면 전체/개별 분기
        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID"))) {
            objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListMapper", "selectList",
                jsonParam);
        }
        if (objRetParam.getDataObject(TwbCmmnConst.G_DATA).size() > 0) { // LIST_ID가 존재 할 때
            // 최초 로드 시 전체 조회 . 등록/수정,삭제 시 개별 조회
            if (!StringUtils.isEmpty(jsonParam.getString("LIST_TYPE"))) {
                String listType = jsonParam.getString("LIST_TYPE");

                switch (listType) {
                    case "srch":
                        objRetParam.setDataObject("srch", setParams("LayoutMngTabListSrchMapper", "selectSrch",
                            StringUtils.isEmpty(jsonParam.getString("LIST_ID")) ? objRetParam : jsonParam));
                        break;
                    case "thumbnail":
                        objRetParam.setDataObject("thumnail", setParams("LayoutMngTabListThumnailMapper", "selectThumnail",
                            StringUtils.isEmpty(jsonParam.getString("LIST_ID")) ? objRetParam : jsonParam));
                        break;
                    case "stts":
                        objRetParam.setDataObject("stts", setParams("LayoutMngTabListSttsMapper", "selectStts",
                            StringUtils.isEmpty(jsonParam.getString("LIST_ID")) ? objRetParam : jsonParam));
                        break;
                    case "listGroup":
                        objRetParam.setDataObject("listGroup", setParams("LayoutMngTabListGroupMapper", "selectListGroup",
                            StringUtils.isEmpty(jsonParam.getString("LIST_ID")) ? objRetParam : jsonParam));
                        break;
                    case "dwnGroup":
                        objRetParam.setDataObject("dwnGroup", setParams("LayoutMngTabListDwnGroupMapper", "selectDwnGroup",
                            StringUtils.isEmpty(jsonParam.getString("LIST_ID")) ? objRetParam : jsonParam));
                        break;
                }
            } else {
                objRetParam.setDataObject("srch", setParams("LayoutMngTabListSrchMapper", "selectSrch", objRetParam));
                objRetParam.setDataObject("thumnail", setParams("LayoutMngTabListThumnailMapper", "selectThumnail", objRetParam));
                objRetParam.setDataObject("stts", setParams("LayoutMngTabListSttsMapper", "selectStts", objRetParam));
                objRetParam.setDataObject("dwnGroup", setParams("LayoutMngTabListDwnGroupMapper", "selectDwnGroup", objRetParam));
                objRetParam.setDataObject("listGroup", setParams("LayoutMngTabListGroupMapper", "selectListGroup", objRetParam));
            }
        } else {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "해당 탭에 존재하는 하위 목록이 없습니다.");
        }
        return objRetParam;
    }

    private TelewebJSON setParams(String mapperName, String sqlName, TelewebJSON jsonParam) throws TelewebAppException {
        // mapperName, sqlName, jsonParam을 파라미터로 받아 각 쿼리를 실행하여 결과값을 TelewebJSON 리턴
        String sqlNameSpace = "kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao." + mapperName;
        List<String> arrListId = new LinkedList<String>();
        TelewebJSON selectParam = new TelewebJSON();
        for (int i = 0; i < jsonParam.getDataObject(TwbCmmnConst.G_DATA).size(); i++) {
            arrListId.add(jsonParam.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(i).getString("LIST_ID"));
        }
        if (arrListId.size() != 0) {
            selectParam.setObject("arrListId", 0, arrListId);
        }
        selectParam = twbComDao.select(sqlNameSpace, sqlName, selectParam);
        return selectParam;
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertList(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID")) || "null".equals(jsonParam.getString("LIST_ID"))) {
            int listId = innbCreatCmmnService.createSeqNo("LIST_ID");
            jsonParam.setInt("LIST_ID", listId);
            objRetParam = twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListMapper", "insertList",
                jsonParam);
            objRetParam.setInt("LIST_ID", listId);
        }
        return objRetParam;
    }

    @Override
    public TelewebJSON deleteList(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParam);
        if (StringUtils.isEmpty(jsonParam.getString("LIST_ID"))) {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "삭제할 항목이 지정되지 않습니다");
        } else {
            objRetParam = twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListMapper", "deleteList",
                jsonParam);
        }

        return objRetParam;
    }

}
