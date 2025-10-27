package kr.co.hkcloud.palette3.chat.setting.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("chatSettingManageService")
public class ChatSettingManageServiceImpl implements ChatSettingManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectRtnHistoryOfWorkTime", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHistoryOfWorkTimeLast(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectRtnHistoryOfWorkTimeLast", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHistoryOfWorkTimeBefore(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectRtnHistoryOfWorkTimeBefore", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnYesterday(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectRtnYesterday", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertRtnHistoryOfWorkTime", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnCnslProp(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException
    {
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateRtnCnslProp", jsonParams);
        //상담혀용수 일괄적용 일때
        if(jsonParams.getString("STNG_CD").equals("CONT_CHATAGENT_YN")) {
        	if(jsonParams.getString("STNG_VL").equals("Y")) {
	        	//상담 허용수 체크
	        	objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectCutPmCnt", jsonParams);
	        	jsonParams.setString("CHT_LMT_CNT", objRetParams.getString("STNG_VL"));
            	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateCutPm", jsonParams);
        	}
        } else if(jsonParams.getString("STNG_CD").equals("CONT_CHATAGENT_CNT")){		//상담 허용 수 일때
        	//상담 허용 여부 체크
        	objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectCutPmYn", jsonParams);
        	if(objRetParams.getString("STNG_VL").equals("Y")) {
	        	jsonParams.setString("CHT_LMT_CNT", jsonParams.getString("STNG_VL"));
            	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateCutPm", jsonParams);
        	}
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnHistoryOfWorkTime(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateRtnHistoryOfWorkTime", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnMsg(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "deleteRtnMsg", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectRtnCnslProp", jsonParams);
    }


    /**
     * 파레트메인 - 상담허용수를 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnsltPermCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectUserInformation", jsonParams);
    }


    /**
     * 파레트메인 - 상담허용수를 저장한다.
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateMaxAgentAndViewBaseScriptYnByUserId(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateMaxAgentAndViewBaseScriptYnByUserId", jsonParams);
    }



    /**
     * 상담 허용 수 개별 설정
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateChtLmtCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateChtLmtCnt", jsonParams);
    }


    /**
     * 상담원 채팅 상태 변경
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateUserStat(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON retParams = new TelewebJSON();
    	retParams = mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "selectUserStat", jsonParams);
    	
    	if(retParams.getString("CNT").equals("0")) {
    		retParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertUserStat", jsonParams);
    	} else {
    		retParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "updateUserStat", jsonParams);
    	}
        return retParams;
    }

}
