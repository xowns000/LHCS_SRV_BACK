package kr.co.hkcloud.palette3.setting.holiday.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Slf4j
@RequiredArgsConstructor
@Service("settingHolidayManageService")
public class SettingHolidayManageServiceImpl implements SettingHolidayManageService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;


    /**
     * 설정휴일관리 목록을 조회한다
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageHoliday(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "selectRtnPageHoliday", jsonParams);
    }


    /**
     * 휴일 중복을 체크한다.
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHolidayDupChk(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "selectRtnHolidayDupChk", jsonParams);
    }


    @Override
    @Transactional
    public TelewebJSON insertRtnHoliday(TelewebJSON jsonParam, String holidayDate, String holidayName, String holidayGbCd) throws TelewebAppException
    {
        TelewebJSON objRetParam = new TelewebJSON();
        String strSeqKey = Integer.toString(innbCreatCmmnService.createSeqNo("HLDY_ID"));   //휴일 ID를 가져온다.

        jsonParam.setString("CUSTCO_ID", jsonParam.getString("CUSTCO_ID"));
        jsonParam.setString("HLDY_ID", strSeqKey);   //휴일 ID
        jsonParam.setString("HLDY_YMD", holidayDate); //휴일일자
        jsonParam.setString("HLDY_NM", holidayName); //휴일명
        jsonParam.setString("HLDY_SE_CD", holidayGbCd); //휴일구분
        jsonParam.setString("HLDY_EXPLN", holidayName); //휴일설명

        //휴일 중복 체크
        objRetParam = selectRtnHolidayDupChk(jsonParam);
        if("Y".equals(objRetParam.getString("DUP_YN"))) {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_CODE", "-700");
            objRetParam.setHeader("ERROR_MSG", "이미 등록되어 있는 휴일입니다. 확인 후 재시도바랍니다.");
        }
        else {
            objRetParam = insertRtnHoliday(jsonParam);
        }

        return objRetParam;
    }


    /**
     * 휴일을 등록한다.
     */
    @Override
    @Transactional
    public TelewebJSON insertRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objRetParam = new TelewebJSON();
    	
        String strSeqKey = jsonParam.getString("HLDY_ID");
        
        String strCase = jsonParam.getString("STR_CASE");
        if(strSeqKey.equals("")) {
        	strSeqKey = Integer.toString(innbCreatCmmnService.createSeqNo("HLDY_ID"));   //휴일 ID를 가져온다.
        	jsonParam.setString("HLDY_ID", strSeqKey);
        	objRetParam = mobjDao.insert("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "insertRtnHoliday", jsonParam);
        } else {
        	if(strCase.equals("")) {
            	objRetParam = mobjDao.update("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "updateRtnHoliday", jsonParam);
        	} else {
        		objRetParam = mobjDao.update("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "upsertRtnHoliday", jsonParam);
        	}
        }
        
        return objRetParam;
    }


    /**
     * 휴일을 수정한다.
     */
    @Override
    @Transactional
    public TelewebJSON updateRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException
    {
        String DT = jsonParam.getString("HOLIDAY_DT");
        String[] DTArray = DT.split("-");
        String HDY_DT = DTArray[0] + DTArray[1] + DTArray[2];
        jsonParam.setString("HOLIDAY_DT", HDY_DT);   //휴일일자
        return mobjDao.update("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "updateRtnHoliday", jsonParam);
    }


    /**
     * 휴일을 삭제한다.
     */
    @Override
    @Transactional
    public TelewebJSON deleteRtnHoliday(TelewebJSON jsonParam) throws TelewebAppException
    {
    	TelewebJSON objRetParam = new TelewebJSON();
    	
    	List<String> arrHldyId = new LinkedList<String>();
    	JSONArray jsonObj = jsonParam.getDataObject(TwbCmmnConst.G_DATA);
        for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrHldyId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrHldyId.add(strValue);
                }
            }
        }
        //신규설정메뉴가 있으면
        if(arrHldyId.size() != 0) {
        	jsonParam.setObject("arrHldyId", 0, arrHldyId);
		
        	objRetParam = mobjDao.delete("kr.co.hkcloud.palette3.setting.holiday.dao.SettingHolidayManageMapper", "deleteRtnHoliday", jsonParam);
		
		}
        
        return objRetParam;
    }

}
