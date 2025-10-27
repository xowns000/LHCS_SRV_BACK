package kr.co.hkcloud.palette3.setting.system.app;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * 메뉴권한관리 서비스 인터페이스 구현체
 * 
 * @author R&D
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemMenuAuthorityManageService")
public class SettingSystemMenuAuthorityManageServiceImpl implements SettingSystemMenuAuthorityManageService
{
    private final SettingSystemMenuButtonAuthorityService settingSystemMenuButtonAuthorityService;
    private final TwbComDAO                               twbComDAO;
    private final PaletteSecurityUtils                    paletteSecurityUtils;


    /**
     * 설정시스템메뉴권한관리 권한그룹코드를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAuthGroup(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnAuthGroup", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 사용자기준으로 권한그룹코드를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAtrtGroupCd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnAtrtGroupCd", jsonParams);
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
    public TelewebJSON processRtnAuth(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        
        List<String> arrStngMenu = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrStngMenu") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrStngMenu.add(strValue);
                }
            }
        }
        //신규설정메뉴가 있으면
        if(arrStngMenu.size() != 0) {
        	mjsonParams.setObject("arrStngMenu", 0, arrStngMenu);
        	
            //신규 설정메뉴 등록
            objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "INSERT_PLT_AUTH_PRG", mjsonParams);
        }
        
        List<String> arrUnStngMenu = new LinkedList<String>();
    	JSONArray jsonObj2 = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj2.size(); i++) {
        	JSONObject objData = jsonObj2.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrUnStngMenu") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrUnStngMenu.add(strValue);
                }
            }
        }
        //미설정 메뉴가 있으면
        if(arrUnStngMenu.size() != 0) {
        	mjsonParams.setObject("arrUnStngMenu", 0, arrUnStngMenu);
        	
            //설정된 버튼권한 삭제
        	settingSystemMenuButtonAuthorityService.deleteRtnBtnAuth(mjsonParams);
            //설정된 메뉴 삭제
            objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "deleteRtnAuthbyMenu", mjsonParams);
        }
        
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹별 할당메뉴를 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);		//DB Access 파라메터 생성

        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "DELETE_PLT_AUTH_PRG", objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }

//    /**
//     * 설정시스템메뉴권한관리 메뉴권한을 등록한다
//     * 
//     * @author        R&D
//     * @Transactional Auto Commit
//     * @return        TelewebJSON 형식의 처리 결과 데이터
//     * @since         2021.03.24
//     */
//    @Transactional(propagation = Propagation.REQUIRED,
//                   rollbackFor = {Exception.class, SQLException.class},
//                   readOnly = false)
//    public TelewebJSON processRtnBtnAuth(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성
//        TelewebJSON objParams = new TelewebJSON(mjsonParams);		//DB Access 파라메터 생성
//        JSONArray objArry = mjsonParams.getDataObject();
//        JSONObject objJson = new JSONObject();
//
//        //개인정보영향평가 : 업무처리 가능(VDI IP)여부 체크
//        boolean bVDI = paletteSecurityUtils.checkVDI(req);
//
//        if(bVDI) {
//            if(!objArry.isEmpty()) {
//                for(int i = 0; i < objArry.size(); i++) {
//                    objJson = objArry.getJSONObject(i);
//                    if(!objJson.isEmpty() && !objJson.isNullObject()) {
//                        objParams.setDataObject(JSONArray.fromObject(objJson));
//                        //DATA_FLAG값에 따라 삭제/추가 처리
//                        String strDataFlag = objParams.getString("DATA_FLAG");
//                        if(strDataFlag.equals(TwbCmmnConst.TRANS_DEL)) {  //삭제
//                            twbComDAO.insert("kr.co.hkcloud.palette.mng.auth.authbtn.dao.AuthBtnMapper", "DELETE_AUTH_BTN", objParams);
//                        }
//                        else if(strDataFlag.equals(TwbCmmnConst.TRANS_INS)) {    //추가
//                            twbComDAO.insert("kr.co.hkcloud.palette.mng.auth.authbtn.dao.AuthBtnMapper", "INSERT_AUTH_BTN", objParams);
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            objRetParams.setHeader("ERROR_FLAG", true);
//            objRetParams.setHeader("ERROR_MSG", "접근제한지역에서만 해당내용을 처리할 수 있습니다.");
//        }
//        //최종결과값 반환
//        return objRetParams;
//    }


    /**
     * 설정시스템메뉴권한관리 권한그룹별 할당메뉴를 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);		//DB Access 파라메터 생성

        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    //DATA_FLAG값에 따라 삭제 처리
                    String strDataFlag = objParams.getString("DATA_FLAG");
                    if(strDataFlag.equals(TwbCmmnConst.TRANS_DEL + "G")) {    //권한그룹
                        twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "DELETE_PLT_AUTH", objParams);
                    }
                    else if(strDataFlag.equals(TwbCmmnConst.TRANS_DEL + "P")) {  //권한그룹별프로그램
                        twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "DELETE_PLT_AUTH_PRG", objParams);
                    }
                    else if(strDataFlag.equals(TwbCmmnConst.TRANS_DEL + "U")) {  //사용자권한
                        twbComDAO.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "DELETE_PLT_USER_AUTH", objParams);
                    }
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹에 할당가능한 메뉴를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnchkEnableAuth(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnchkEnableAuth", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 미할당된 메뉴를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnNoAlloc(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnNoAlloc", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 할당된 메뉴를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAlloc(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnAlloc", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 미할당된 버튼을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnNoAllocBtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnNoAllocBtn", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 할당된 버튼을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAllocBtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnAllocBtn", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 메뉴권한트리목록을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMenuAuthTree(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnMenuAuthTree", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹명 중복을 체크한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectDupAuthGroup(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectDupAuthGroup", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 사용자별 메뉴권한 할당정보를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAuthGroupMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "selectRtnAuthGroupMng", jsonParams);
    }

}
