package kr.co.hkcloud.palette3.setting.system.app;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.TeletalkAuthority;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 권한그룹 서비스 인터페이스 구현체
 * 
 * @author R&D
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemAuthorityGroupService")
public class SettingSystemAuthorityGroupServiceImpl implements SettingSystemAuthorityGroupService
{
    private final TwbComDAO mobjDao;
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final String    TRANS_DEL = "D";


    /**
     * 설정시스템메뉴권한관리 권한그룹을 삭제한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	log.info("mjsonParams11" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성
        
        List<String> arrAuthrtId = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrAuthrtId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrAuthrtId.add(strValue);
                }
            }
        }
    	log.info("mjsonParams22" + mjsonParams);
        
        if(arrAuthrtId.size() != 0) {
        	mjsonParams.setObject("arrAuthrtId", 0, arrAuthrtId);
        }
    	log.info("mjsonParams33" + mjsonParams);
        
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "DELETE_PLT_BTN_AUTHRT", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuAuthorityManageMapper", "DELETE_PLT_AUTH_PRG", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "DELETE_PLT_USER_AUTH", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "DELETE_PLT_AUTH", mjsonParams);
    	log.info("mjsonParams44" + mjsonParams);

        return objRetParams;
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹목록을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTwbBas05(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "SELECT_PLT_AUTH", jsonParams);
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹을 등록한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON insertTwbBas05(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String authrtGroupId = mjsonParams.getString("AUTHRT_GROUP_ID");
        if(authrtGroupId.equals("")) {
        	authrtGroupId = Integer.toString(innbCreatCmmnService.createSeqNo("AUTHRT_GROUP_ID"));
        	mjsonParams.setString("AUTHRT_GROUP_ID", authrtGroupId);
            //추가
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "INSERT_PLT_AUTH", mjsonParams);
        }
        else {
            //수정
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "UPDATE_PLT_AUTH", mjsonParams);
        }
        objRetParams.setString("AUTHRT_GROUP_ID", authrtGroupId);

        return objRetParams;
    }


    /**
     * 설정시스템메뉴권한관리 권한그룹을 수정한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.24
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = false)
    public TelewebJSON updateTwbBas05(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityGroupMapper", "UPDATE_PLT_AUTH", mjsonParams);
    }

}
