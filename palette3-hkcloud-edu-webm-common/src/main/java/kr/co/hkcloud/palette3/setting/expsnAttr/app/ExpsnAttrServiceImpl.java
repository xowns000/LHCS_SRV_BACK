package kr.co.hkcloud.palette3.setting.expsnAttr.app;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("expsnAttrService")
public class ExpsnAttrServiceImpl implements ExpsnAttrService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;


    /**
     * 확장속성관리-목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON expsnAttrList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "expsnAttrList", mjsonParams);
    }


    /**
     * 확장속성관리-등록, 수정
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON expsnAttrProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //등록
        if(StringUtils.isBlank(mjsonParams.getString("ATTR_ID"))) {
        	int ATTR_ID = innbCreatCmmnService.createSeqNo("ATTR_ID");
        	mjsonParams.setInt("ATTR_ID", ATTR_ID);
        	
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "INSERT_EXPSN_ATTR", mjsonParams);
        }else{ //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "UPDATE_EXPSN_ATTR", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 확장속성관리-삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON expsnAttrDel(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	List<String> arrAttrId = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrAttrId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrAttrId.add(strValue);
                }
            }
        }
        
        if(arrAttrId.size() != 0) {
            mjsonParams.setObject("arrAttrId", 0, arrAttrId);
        }
    	
        return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "DELETE_EXPSN_ATTR", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
    rollbackFor = {Exception.class, SQLException.class},
    readOnly = false)
    public TelewebJSON expsnAttrReOrder(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	List<String> arrAttrId = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int i = 0; i < jsonObj.size(); i++) {
    		JSONObject objData = jsonObj.getJSONObject(i);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if(strKey.indexOf("arrAttrId") > -1 && StringUtils.isNotEmpty(strValue)) {
    				arrAttrId.add(strValue);
    			}
    		}
    	}
    	
    	if(arrAttrId.size() != 0) {
    		mjsonParams.setObject("arrAttrId", 0, arrAttrId);
    	}
    	
    	return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "UPDATE_EXPSN_ATTR_RE_ORD", mjsonParams);
    }
    
    /**
     * 확장속성관리-항목ID 중복 체크
     */
    @Transactional(readOnly = true)
    public boolean expsnAttrColIdDupleChk(TelewebJSON jsonParams) throws TelewebAppException {
    	try {
    		
    		TelewebJSON jsonUserData = mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.ExpsnAttrMapper", "expsnAttrColIdDupleChk", jsonParams);
    		
    		if(jsonUserData.getHeaderInt("COUNT") == 0) {
    			return false;
    		}
    		else {
    			return true;
    		}
    	}
    	catch(Exception ex) {
    		return false;
    	}
    }
}
