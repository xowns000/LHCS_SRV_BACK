package kr.co.hkcloud.palette3.chat.setting.app;


import java.sql.SQLException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageDAO;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingSystemMessageManageUtils;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("chatSettingSystemMessageManageService")
public class ChatSettingSystemMessageManageServiceImpl implements ChatSettingSystemMessageManageService
{
    private final TwbComDAO                           mobjDao;
    private final ChatSettingSystemMessageManageDAO   chatSettingSystemMessageDAO;
    private final ChatSettingSystemMessageManageUtils chatSettingSystemMessageManageUtils;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSystemMsgType(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "selectSystemMsgType", jsonParams);
    }


    /**
     * 시스템메시지 삭제 (캐시 적용)
     * 
     * @version 5.0
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public int deleteSystemMsg(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException
    {
        JSONArray jsonArray = jsonParams.getDataObject("MSG_INFO");
        log.info("??" + jsonArray);
        if(jsonArray != null) {
            int intMsgSize = jsonArray.size();
            for(int i = 0; i < intMsgSize; i++) {
                TelewebJSON jsonMsgParams = new TelewebJSON(jsonParams);
                jsonMsgParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));

                jsonMsgParams.setString("SYS_MSG_ID", jsonArray.getJSONObject(i).getString("SYS_MSG_ID"));
                jsonMsgParams.setString("MSG_HR", jsonArray.getJSONObject(i).getString("MSG_HR"));


                mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "deleteCustcoSystemMsg", jsonMsgParams);
            }
        }

        return 0;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSystemMsgList(TelewebJSON jsonParams) throws TelewebAppException
    {
    	log.info("@@#!!!@#!@#!@#!@" + jsonParams);
    	log.info("@@#!!!@#!@#!@#!@" + jsonParams.getString("PAGING"));
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "selectSystemMsgList", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSystemMsgLinkData(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "selectSystemMsgLinkData", jsonParams);
    }


    /**
     * 시스템메시지 등록 (캐시 적용)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public int insertNewSystemMsg(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException
    {
        int ErrorCode = 0;
        TelewebJSON linksParams = new TelewebJSON(jsonParams);
        objRetParams.setDataObject("DATA", jsonParams.getDataObject("DATA"));


        String sysMsgId = jsonParams.getString("BF_MSG_HR");
        if(sysMsgId.equals("-1")) {
	        // 시스템 메시지 등록
	        mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "insertNewSystemMsg", jsonParams);
        } else {
        	// 시스템 메시지 수정
	        mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "updateSystemMsg", jsonParams);
        }
        
        
        JSONObject jsonObj = (JSONObject) objRetParams.getDataObject("DATA").get(0);
        
        //링크메시지 일단 지원 안함
        if("LI".equals(jsonObj.get("NEW_MSG_TYPE"))) {

            if(null == jsonParams.getDataObject("LINKDATA") || jsonParams.getDataObject("LINKDATA").size() == 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", chatSettingSystemMessageManageUtils.getErrorMsg(20));
            }
            else {
                // validation check 2
                ErrorCode = chatSettingSystemMessageManageUtils.systemMsgValidationByLinkMsg(jsonParams.getDataObject("LINKDATA"));
                // 링크 메시지가 있을 경우, 링크메시지 등록.
                if(ErrorCode == 0) {
                    if(jsonParams.getDataObject("LINKDATA").size() > 0) {

                        for(int i = 0; i < jsonParams.getDataObject("LINKDATA").size(); i++) {
                            linksParams.setDataObject("DATA", (JSONObject) jsonParams.getDataObject("LINKDATA").get(i));
                            linksParams.setString("SYS_MSG_ID", jsonObj.getString("SYS_MSG_ID"));
                            linksParams.setString("REG_ID", jsonObj.getString("NEW_REG_ID"));
                            linksParams.setString("DEPT_CODE", jsonObj.getString("NEW_DEPT_CODE"));
                            linksParams.setString("SORT_ORD", Integer.toString((i + 1)));
                            linksParams.setString("ASP_NEWCUST_KEY", jsonObj.getString("ASP_NEWCUST_KEY"));

                            chatSettingSystemMessageDAO.insertNewSystemLinksMsg(linksParams);
                        }
                    }
                }
                else if(ErrorCode > 0) {
//                    String errorMsg = getErrorMsg(ErrorCode);
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", chatSettingSystemMessageManageUtils.getErrorMsg(ErrorCode));
                }
            }
        }
        return ErrorCode;
    }


    /**
     * 시스템메세지 수정 (캐시 적용)
     * 
     * @version 5.0
     */
    @Override
    @Transactional(readOnly = false)
    public void updateSystemMsgByText(TelewebJSON jsonParams) throws TelewebAppException
    {
        mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "modifySystemMsgByText", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON modifySystemMsgByLink(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "modifySystemMsgByLink", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertSystemLinksMsg(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "insertSystemLinksMsg", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateSystemLinksMsg(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "updateSystemLinksMsg", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON commonSysMsgList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "commonSysMsgList", jsonParams);
    }
    
    
    
}
