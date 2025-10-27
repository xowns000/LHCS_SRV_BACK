package kr.co.hkcloud.palette3.setting.ipExt.app;


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
 * Description : IP 내선번호 설정 Impl
 * package  : kr.co.hkcloud.palette3.setting.ipExt.app
 * filename : IpExtServiceImpl.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("ipExtService")
public class IpExtServiceImpl implements IpExtService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;


    /**
     * IP 내선번호 관리-목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON ipExtList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "ipExtList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON extNotEmptyCuslList(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return mobjDao.select("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "extNotEmptyCuslList", mjsonParams);
    }


    /**
     * IP 내선번호 관리-등록, 수정
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON ipExtProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //등록
        if(StringUtils.isBlank(mjsonParams.getString("PHN_IP_EXT_ID"))) {
        	int PHN_IP_EXT_ID = innbCreatCmmnService.createSeqNo("PHN_IP_EXT_ID");
        	mjsonParams.setInt("PHN_IP_EXT_ID", PHN_IP_EXT_ID);
        	
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "INSERT_IP_EXT", mjsonParams);
        }else{ //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "UPDATE_IP_EXT", mjsonParams);
        }

        return objRetParams;
    }
    
    /**
     * 내선번호 중복 체크
     */
    @Transactional(readOnly = true)
    public TelewebJSON extNoDuplCheck(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "extNoDuplCheck", mjsonParams);
    }


    /**
     * IP 내선번호 관리-삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON ipExtDel(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	List<String> arrPhnIpExtId = new LinkedList<String>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
        	JSONObject objData = jsonObj.getJSONObject(i);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrPhnIpExtId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrPhnIpExtId.add(strValue);
                }
            }
        }
        
        if(arrPhnIpExtId.size() != 0) {
            mjsonParams.setObject("arrPhnIpExtId", 0, arrPhnIpExtId);
        }
    	
        return mobjDao.update("kr.co.hkcloud.palette3.setting.ipExt.dao.IpExtMapper", "DELETE_IP_EXT", mjsonParams);
    }
}
