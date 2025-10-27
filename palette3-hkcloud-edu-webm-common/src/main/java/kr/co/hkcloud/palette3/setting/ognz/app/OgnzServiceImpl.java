package kr.co.hkcloud.palette3.setting.ognz.app;


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

/**
 * 
 * Description : 조직 관리
 * package  : kr.co.hkcloud.palette3.setting.ognz.app
 * filename : OgnzServiceImpl.java
 * Date : 2023. 6. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("ognzService")
public class OgnzServiceImpl implements OgnzService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;

    @Transactional(readOnly = true)
    public TelewebJSON ognzTreeList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "ognzTreeList", mjsonParams);
    }
    
    /**
     * 조직 관리-등록, 수정
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON ognzProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        String RGN_LIST = "";
        if(!StringUtils.isEmpty(mjsonParams.getString("RGN_LIST"))) {
	        RGN_LIST = mjsonParams.getString("RGN_LIST").toString();
	        RGN_LIST = RGN_LIST.replace("&#91;", "[").replace("&#93;", "]");
	        mjsonParams.setString("RGN_LIST", RGN_LIST);
        }

        //등록
        if(StringUtils.isBlank(mjsonParams.getString("DEPT_ID"))) {
        	int DEPT_ID = innbCreatCmmnService.createSeqNo("DEPT_ID");
        	mjsonParams.setInt("DEPT_ID", DEPT_ID);
        	
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "INSERT_OGNZ", mjsonParams);

            if(!StringUtils.isEmpty(mjsonParams.getString("RGN_LIST"))) {
            	if(!"[]".equals(RGN_LIST)) objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "ognzRgnReg", mjsonParams);
            }
        }else{ //수정
        	if(!StringUtils.isEmpty(mjsonParams.getString("RGN_LIST"))) {
        		objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "ognzRgnDel", mjsonParams);
        	}
        	
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "UPDATE_OGNZ", mjsonParams);
            
            if(!StringUtils.isEmpty(mjsonParams.getString("RGN_LIST"))) {
            	if(!"[]".equals(RGN_LIST)) objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "ognzRgnReg", mjsonParams);
            }
        }

        return objRetParams;
    }


    /**
     * 조직 관리-삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON ognzDel(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	List<String> arrDeptId = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrDeptId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrDeptId.add(strValue);
                }
            }
        }
        
        if(arrDeptId.size() != 0) {
            mjsonParams.setObject("arrDeptId", 0, arrDeptId);
        }
    	
        return mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "DELETE_OGNZ", mjsonParams);
    }

    /**
     * 조직 순서 재정의
     */
    @Transactional(propagation = Propagation.REQUIRED,
    		rollbackFor = {Exception.class, SQLException.class},
    		readOnly = false)
    public TelewebJSON ognzOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
    	
    	if("UP".equals(mjsonParams.getString("ORDER_TYPE"))) {
    		if(Integer.parseInt(mjsonParams.getString("SORT_ORD")) > 1) {
    			mjsonParams.setInt("ADD_NUM", 1);
    			mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD"))-1);
    		}
    	}else if("DOWN".equals(mjsonParams.getString("ORDER_TYPE"))) {
    		if(Integer.parseInt(mjsonParams.getString("SORT_ORD")) < Integer.parseInt(mjsonParams.getString("MAX_SORT_ORD"))) {
    			mjsonParams.setInt("ADD_NUM", -1);
    			mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD"))+1);
    		}
    	}
    	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "UPDATE_OGNZ_OTHER_SORT_ORDER", mjsonParams);
    	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "UPDATE_OGNZ_SORT_ORDER", mjsonParams);
    	
    	return objRetParams;
    }
    
    /**
     * 지역 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rgnList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.ognz.dao.OgnzMapper", "rgnList", mjsonParams);
    }
}
