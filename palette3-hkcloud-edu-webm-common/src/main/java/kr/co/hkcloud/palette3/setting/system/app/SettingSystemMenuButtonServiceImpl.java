package kr.co.hkcloud.palette3.setting.system.app;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 버튼 서비스 인터페이스 구현체
 *
 * @author R&D
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemMenuButtonService")
public class SettingSystemMenuButtonServiceImpl implements SettingSystemMenuButtonService {

    private final TwbComDAO mobjDao;
    private final SettingSystemMenuButtonAuthorityService settingSystemMenuButtonAuthorityService;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 메뉴버튼 목록을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectBtn(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "SELECT_BTN", jsonParams);
    }


    /**
     * 메뉴버튼을 등록한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON insertBtn(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        String btnId = jsonParams.getString("BTN_ID");
        if (btnId.equals("")) {
            btnId = Integer.toString(innbCreatCmmnService.createSeqNo("BTN_ID"));
            jsonParams.setString("BTN_ID", btnId);
            //추가
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "INSERT_BTN", jsonParams);
        } else {
            //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "UPDATE_BTN", jsonParams);
        }
        objRetParams.setString("BTN_ID", btnId);

        return objRetParams;
    }


    /**
     * 메뉴버튼을 수정한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateBtn(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "UPDATE_BTN", jsonParams);
    }


    /**
     * 메뉴관리 버튼을 삭제한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnBtn(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        List<String> arrBtnId = new LinkedList<String>();
        JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
            while (it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if (strKey.indexOf("arrBtnId") > -1 && StringUtils.isNotEmpty(strValue)) {
                    arrBtnId.add(strValue);
                }
            }
        }

        if (arrBtnId.size() != 0) {
            jsonParams.setObject("arrBtnId", 0, arrBtnId);
        }

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "DELETE_BTN",
            jsonParams);                        //BTN(버튼자원)
        objRetParams = settingSystemMenuButtonAuthorityService.deleteRtnBtnAuthByBtnID(jsonParams);

        return objRetParams;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnBtnByMeneId(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        String MENU_ID = jsonParams.getString("LOWRNK_MENU_ID");
        if (StringUtils.isEmpty(MENU_ID)) {
            throw new IllegalArgumentException("LOWRNK_MENU_ID is necessary");
        }

        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setString("MENU_ID", jsonParams.getString("LOWRNK_MENU_ID"));

        TelewebJSON jsonBtnId = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "selectRtnBtnIdByMenuId", jsonSelect);
        for (int idx = 0, iTimes = jsonBtnId.getHeaderInt("TOT_COUNT"); idx < iTimes; ++idx) {
            TelewebJSON jsonDelete = new TelewebJSON(jsonParams);
            jsonDelete.setString("BTN_ID", jsonBtnId.getString("BTN_ID", idx));

            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "deleteRtnBtnByMeneId", jsonDelete);
        }

        return objRetParams;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnAuthBtnByMeneId(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        String MENU_ID = jsonParams.getString("LOWRNK_MENU_ID");

        if (StringUtils.isEmpty(MENU_ID)) {
            throw new IllegalArgumentException("LOWRNK_MENU_ID is necessary");
        }

        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setString("MENU_ID", jsonParams.getString("LOWRNK_MENU_ID"));

        TelewebJSON jsonBtnId = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "selectRtnAuthBtnIdByMenuId",
            jsonSelect);
        for (int idx = 0, iTimes = jsonBtnId.getHeaderInt("TOT_COUNT"); idx < iTimes; ++idx) {
            TelewebJSON jsonDelete = new TelewebJSON(jsonParams);
            jsonDelete.setString("ATRT_GROUP_ID", jsonBtnId.getString("ATRT_GROUP_ID", idx));
            jsonDelete.setString("MENU_ID", jsonBtnId.getString("MENU_ID", idx));
            jsonDelete.setString("BTN_ID", jsonBtnId.getString("BTN_ID", idx));

            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "deleteRtnAuthBtnByMeneId", jsonDelete);
        }

        return objRetParams;
    }

}
