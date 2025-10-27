package kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.impl;

import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListService;
import kr.co.hkcloud.palette3.admin.layoutMng.tab.list.service.LayoutMngTabListSrchService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
@Service("srchService")
public class LayoutMngTabListSrchServiceImpl implements LayoutMngTabListSrchService {

    private final TwbComDAO twbComDao;
    private final LayoutMngTabListService layoutMngTabListService;

    @Override
    public TelewebJSON selectUsableParams(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        objRetParam = twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSrchMapper",
            "selectUsableParams", jsonParam);
        return objRetParam;
    }

    @Override
    public TelewebJSON selectSrch(TelewebJSON jsonParam) throws TelewebAppException {

        return new TelewebJSON();
    }
    @Override
    public TelewebJSON insertUpdateSrch(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON();
        String userId = jsonParam.getString("USER_ID");
        String tabId = jsonParam.getString("TAB_ID");

        JSONArray arrSrchList = jsonParam.getDataObject("regSrchParam");
        for(int i = 0; i < arrSrchList.size(); i++){
            JSONObject objSrch = (JSONObject) arrSrchList.get(i);
            objSrch.put("USER_ID", userId);
            objSrch.put("TAB_ID", tabId);
            // insert용 TelewebJSON객체
            TelewebJSON regParam = new TelewebJSON();
            regParam.setDataObject(TwbCmmnConst.G_DATA, objSrch);
            //LIST_ID없으면 생성해서 추가
            if(!objSrch.containsKey("LIST_ID")){
                objRetParam = insertSrch(regParam);
            }else{
                // 있으면 삭제하고 추가
                twbComDao.delete("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSrchMapper",
                    "deleteSrchById", regParam);
                layoutMngTabListService.deleteList(regParam);
                regParam.remove("LIST_ID");
                objRetParam = insertSrch(regParam);
            }
        }
        return objRetParam;
    };
    public TelewebJSON insertSrch(TelewebJSON jsonParam) throws TelewebAppException {

        TelewebJSON objRetParam = new TelewebJSON();
        objRetParam = layoutMngTabListService.insertList(jsonParam);
//        String listId = layoutMngTabListService.insertList(jsonParam).getString("LIST_ID");         // LIST_ID 생성 / PLT_LAYOUT_LIST 테이블 INSERT
        jsonParam.setString("LIST_ID", objRetParam.getString("LIST_ID"));     // 생성된 LIST_ID jsonParam에 set

        return twbComDao.insert("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSrchMapper",
            "insertSrch", jsonParam);
    };
}
