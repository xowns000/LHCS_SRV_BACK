package kr.co.hkcloud.palette3.setting.system.app;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.util.PaletteSecurityUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 버튼 서비스 인터페이스 구현체
 * 
 * @author R&D
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemMenuButtonAuthorityService")
public class SettingSystemMenuButtonAuthorityServiceImpl implements SettingSystemMenuButtonAuthorityService
{
    private final TwbComDAO            twbComDAO;
    private final PaletteSecurityUtils PaletteSecurityUtils;

    @Autowired
    private HttpServletRequest request;


    /**
     * 메뉴버튼권한을 조회한다.
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMenuBtnRole(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "selectRtnMenuBtnRole", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 메뉴권한을 등록한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnBtnAuth(TelewebJSON mjsonParams) throws TelewebAppException
    {	
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        
        List<String> arrStngBtn = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrStngBtn") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrStngBtn.add(strValue);
                }
            }
        }
        //신규설정메뉴가 있으면
        if(arrStngBtn.size() != 0) {
        	mjsonParams.setObject("arrStngBtn", 0, arrStngBtn);
        	
            //신규 설정메뉴 등록
            objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "INSERT_AUTH_BTN", mjsonParams);
        }
        
        List<String> arrUnStngBtn = new LinkedList<String>();
    	JSONArray jsonObj2 = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj2.size(); i++) {
        	JSONObject objData = jsonObj2.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrUnStngBtn") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrUnStngBtn.add(strValue);
                }
            }
        }
        //미설정 메뉴가 있으면
        if(arrUnStngBtn.size() != 0) {
        	mjsonParams.setObject("arrUnStngBtn", 0, arrUnStngBtn);
            //설정된 버튼 삭제
            objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "DELETE_AUTH_BTN_exceptDataToRegister", mjsonParams);
        }
        
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 메뉴관리 버튼을 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnBtnAuth(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "DELETE_AUTH_BTN", jsonParams);	//AUTH_BTN(버튼권한)

        return objRetParams;
    }


    /**
     * 메뉴관리 버튼을 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnBtnAuthExceptDataToRegister(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "DELETE_AUTH_BTN_exceptDataToRegister", jsonParams);	//AUTH_BTN(버튼권한)

        return objRetParams;
    }


    /**
     * 버튼ID로 메뉴관리 버튼을 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnBtnAuthByBtnID(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper", "DELETE_AUTH_BTN_BYBTN", jsonParams);	//AUTH_BTN(버튼권한)

        return objRetParams;
    }

}