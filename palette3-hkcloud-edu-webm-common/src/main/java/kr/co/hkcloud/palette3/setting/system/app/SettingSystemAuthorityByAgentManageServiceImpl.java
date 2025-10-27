package kr.co.hkcloud.palette3.setting.system.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * @author 61000216
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemAuthorityByAgentManageService")
public class SettingSystemAuthorityByAgentManageServiceImpl implements SettingSystemAuthorityByAgentManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 설정시스템사용자별권한관리 사용자별 할당정보를 조회한다(페이징검색)
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAuthAlloc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //할당 미할당에 따라 변경
        if("IS NULL".equals(mjsonParams.getString("NULLABLE"))) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "selectRtnNotAuthAllocAll", mjsonParams);
        }
        else {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "selectRtnAuthAllocAll", mjsonParams);
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     * 사용자관리 - 사용자 권한 단건 조회
     * @Method Name  	: selectRtnAuthrtById
     * @date   			: 2023. 6. 21.
     * @author   		: NJY
     * @version     	: 1.0
     * ----------------------------------------
     * @param  jsonParams	삽입할 사용자 정보
     * @return 
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAuthrtById(TelewebJSON jsonParams) throws TelewebAppException
    {
    	
    	return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "selectRtnAuthByUserId", jsonParams);
    }
    /**
	 * 
	 * 사용자관리 - 사용자 권한 삽입
	 * @Method Name  	: insertAtrtGroupIdByUser
	 * @date   			: 2023. 6. 16.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  jsonParams	삽입할 사용자 정보
	 * @return 
	 * @throws TelewebAppException
	 */
    @Override
    @ApiOperation(value = "사용자의 권한 그룹 단 건 삽입",
                  notes = "사용자의 권한 그룹 단 건 삽입")
    @Transactional(readOnly = false)
    public TelewebJSON insertAtrtGroupIdByUser(TelewebJSON jsonParams) throws TelewebAppException
    {
    	
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "INSERT_PLT_USER_AUTHRT", jsonParams);
    }


    /**
	 * 
	 * 사용자관리 - 사용자 권한 수정
	 * @Method Name  	: updateAtrtGroupIdByUser
	 * @date   			: 2023. 6. 16.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  jsonParams	수정할 사용자 정보
	 * @return objRetParam
	 * @throws TelewebAppException
	 */
    @Override
    @ApiOperation(value = "사용자의 권한 그룹 단 건 수정",
                  notes = "사용자의 권한 그룹 단 건 수정")
    //  @Secured(TeletalkAuthority.ROLES.ADMIN)
    @Transactional(readOnly = false)
    public TelewebJSON updateAtrtGroupIdByUser(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParam = new TelewebJSON(); // 반환 파라메터 생성
    	TelewebJSON objSrchParam = new TelewebJSON(); // 검색 파라메터 생성
    	
    	JSONArray objArry = new JSONArray();
    	
    	objSrchParam = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "selectRtnAuthByUserId", jsonParams);
    	
    	objArry = objSrchParam.getDataObject();
    	
    	if(!objArry.isEmpty()) {
    		
    		objRetParam = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "UPDATE_PLT_USER_AUTH_USER", jsonParams);   
    		
    	}else {
    		
    		objRetParam = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "INSERT_PLT_USER_AUTHRT_DELETED_AUTHRT", jsonParams);    
    		
    	}
    	return objRetParam;
    			
    }


    /**
     * 설정시스템사용자별권한관리 권한그룹별 사용자를 등록한다(권한그룹별 할당사용자 저장)
     */
    @Override
//    @Secured(TeletalkAuthority.ROLES.ADMIN)
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException
    {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        //DB 검색 파라메터 생성
        TelewebJSON objSrchParam = new TelewebJSON(mjsonParams);

        
        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();
        
        objSrchParam = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "selectRtnAuthByUserId", mjsonParams);
    
        
        JSONArray objSrchArry = objSrchParam.getDataObject();
        JSONObject objSrchJson = new JSONObject();
        
        String USER_ID = mjsonParams.getString("USER_ID");
        String CUSTCO_ID = mjsonParams.getString("USER_ID");
        String AUTHRT_GROUP_ID = mjsonParams.getString("AUTHRT_GROUP_ID");
        
        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    
                    if(!objParams.containsKey("USER_ID")) {
                    	objParams.setString("USER_ID", USER_ID);
                    }
                    if(!objParams.containsKey("CUSTCO_ID")) {
                    	objParams.setString("CUSTCO_ID", CUSTCO_ID);
                    }
                    if(!objParams.containsKey("AUTHRT_GROUP_ID")) {
                    	objParams.setString("AUTHRT_GROUP_ID", AUTHRT_GROUP_ID);
                    }
                    
                    if(objSrchArry.isEmpty()) {
      
                    	mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "INSERT_PLT_USER_AUTHRT_DELETED_AUTHRT", objParams);
                    
                    }else {
                    	// 
                    	mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "UPDATE_PLT_USER_AUTH_USER", objParams);
                    	
                    }
                    
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 설정시스템사용자별권한관리 사용자별 권한그룹을 등록한다(사용자별 권한그룹 저장)
     */
    @Override
//    @Secured(TeletalkAuthority.ROLES.ADMIN)
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnAuthGroup(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        JSONArray objArry = mjsonParams.getDataObject();
        
        TelewebJSON objSrchRetParams = selectRtnAuthrtById(mjsonParams);
        
        JSONArray arrRetParams = objSrchRetParams.getDataObject();
        
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    //DATA_FLAG값에 따라 삭제/추가 처리
                    String strDataFlag = objParams.getString("DATA_FLAG");
                    if(strDataFlag.equals(TwbCmmnConst.TRANS_DEL)) {
                        //USER_ID에 해당하는 데이터 전체 삭제
                    	
                    	objParams.setString("DEL_USER_ID", mjsonParams.getString("REG_USER_ID"));
                    	
                        mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "DELETE_PLT_USER_AUTHRT_USER", objParams);
                    }
                    else if(strDataFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                        //권한그룹 추가
                    	if(arrRetParams.isEmpty()) {
                    		mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "INSERT_PLT_USER_AUTHRT_DELETED_AUTHRT", objParams);                    		
                    	}else {
                    		mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "UPDATE_PLT_USER_AUTH_USER", objParams);
                    	}
                    }
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 설정시스템사용자별권한관리 권한그룹별 할당된 사용자를 삭제한다(권한그룹별 할당사용자 삭제)
     */
    @Override
//    @Secured(TeletalkAuthority.ROLES.ADMIN)
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnAuthGroupUser(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        
        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();
        
        String AUTHRT_GROUP_ID = mjsonParams.getString("AUTHRT_GROUP_ID");
        
        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    if(!objParams.containsKey("AUTHRT_GROUP_ID")) {
                    	objParams.setString("AUTHRT_GROUP_ID", AUTHRT_GROUP_ID);
                    }                    
                    log.debug("objParams==========================================="+objParams);
                    mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemAuthorityByAgentManageMapper", "DELETE_PLT_USER_AUTHRT_USER", objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }

}
