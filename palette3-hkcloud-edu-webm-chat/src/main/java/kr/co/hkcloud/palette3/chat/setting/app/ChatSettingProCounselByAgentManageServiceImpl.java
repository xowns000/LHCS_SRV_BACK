package kr.co.hkcloud.palette3.chat.setting.app;


import java.sql.SQLException;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatSettingProCounselByAgentManageService")
public class ChatSettingProCounselByAgentManageServiceImpl implements ChatSettingProCounselByAgentManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnInqryCd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "selectRtnInqryCd", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserInqry(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "selectRtnUserInqry", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserAllocInqry(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "selectRtnUserAllocInqry", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserInqryByUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "selectRtnUserInqryByUser", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateUserSkillRank(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "updateUserSkillRank", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnUserInqryByUser(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "deleteRtnUserInqryByUser", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnUserInqryByInqry(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "deleteRtnUserInqryByInqry", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON SELECT_USER_MAX_RMK(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "SELECT_USER_MAX_RMK", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON SELECT_TALK_USER_INQRY_LEVEL2(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "SELECT_TALK_USER_INQRY_LEVEL2", jsonParams);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON INSERT_TALK_USER_INQRY(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON mjsonParams = new TelewebJSON(jsonParams);      //DB Access 파라메터 생성
        mjsonParams.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //하위 문의유형 저장
        JSONArray jsonArray = jsonParams.getDataObject("USER_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intUserSize = jsonArray.size();
            for(int i = 0; i < intUserSize; i++) {
                TelewebJSON jsonQstnParams = new TelewebJSON(jsonParams);
                jsonQstnParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));					//고객사ID
                jsonQstnParams.setString("USER_ID", jsonParams.getString("USER_ID"));						//사용자명
                
                jsonQstnParams.setString("QSTN_TYPE_ID", jsonParams.getString("QSTN_TYPE_ID"));			//문의유형ID
                jsonQstnParams.setString("STNG", jsonParams.getString("STNG"));			//문의유형 할당,미할당 체크
                
                jsonQstnParams.setString("STNG_USER_ID", jsonArray.getJSONObject(i).getString("USER_ID"));	//할당 사용자ID
                jsonQstnParams.setString("DEPT_ID", jsonArray.getJSONObject(i).getString("DEPT_ID"));	//할당 사용자ID

                if("STNG".equals(jsonQstnParams.getString("STNG"))) {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "INSERT_TALK_USER_INQRY", jsonQstnParams));
                } else {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "DELETE_TALK_USER_INQRY", jsonQstnParams));
                }
            }
            objRetParams.setDataObject("QSTN_INFO", returnJsonArray);
        }

        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON STNG_CHT_USER_SKILL(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON mjsonParams = new TelewebJSON(jsonParams);      //DB Access 파라메터 생성
        mjsonParams.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //하위 문의유형 저장
        JSONArray jsonArray = jsonParams.getDataObject("USER_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intUserSize = jsonArray.size();
            for(int i = 0; i < intUserSize; i++) {
                TelewebJSON jsonQstnParams = new TelewebJSON(jsonParams);
                jsonQstnParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));					//고객사ID
                jsonQstnParams.setString("USER_ID", jsonParams.getString("USER_ID"));						//사용자명
                
                jsonQstnParams.setString("QSTN_TYPE_ID", jsonParams.getString("QSTN_TYPE_ID"));			//문의유형ID
                jsonQstnParams.setString("STNG", jsonParams.getString("STNG"));			//문의유형 할당,미할당 체크
                
                jsonQstnParams.setString("STNG_USER_ID", jsonArray.getJSONObject(i).getString("USER_ID"));	//할당 사용자ID
                jsonQstnParams.setString("DEPT_ID", jsonArray.getJSONObject(i).getString("DEPT_ID"));	//할당 사용자ID

                if("STNG".equals(jsonQstnParams.getString("STNG"))) {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "INSERT_TALK_USER_INQRY", jsonQstnParams));
                } else {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "DELETE_TALK_USER_INQRY", jsonQstnParams));
                }
            }
            objRetParams.setDataObject("QSTN_INFO", returnJsonArray);
        }

        return objRetParams;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON STNG_CHT_SKILL_USER(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON mjsonParams = new TelewebJSON(jsonParams);      //DB Access 파라메터 생성
        mjsonParams.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //하위 문의유형 저장
        JSONArray jsonArray = jsonParams.getDataObject("QSTN_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intUserSize = jsonArray.size();
            for(int i = 0; i < intUserSize; i++) {
                TelewebJSON jsonQstnParams = new TelewebJSON(jsonParams);
                jsonQstnParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));					//고객사ID
                jsonQstnParams.setString("USER_ID", jsonParams.getString("USER_ID"));						//사용자명
                
                jsonQstnParams.setString("STNG_USER_ID", jsonParams.getString("SRCH_USER_ID"));			//문의유형ID
                jsonQstnParams.setString("DEPT_ID", jsonParams.getString("DEPT_ID"));			//문의유형ID
                jsonQstnParams.setString("STNG", jsonParams.getString("STNG"));			//문의유형 할당,미할당 체크
                
                jsonQstnParams.setString("QSTN_TYPE_ID", jsonArray.getJSONObject(i).getString("QSTN_TYPE_ID"));	//할당 사용자ID

                if("STNG".equals(jsonQstnParams.getString("STNG"))) {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "INSERT_TALK_USER_INQRY", jsonQstnParams));
                } else {
                	returnJsonArray.add(i, mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "DELETE_TALK_USER_INQRY", jsonQstnParams));
                }
            }
            objRetParams.setDataObject("QSTN_INFO", returnJsonArray);
        }

        return objRetParams;
    }
    


    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON DELETE_TALK_USER_INQRY(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON mjsonParams = new TelewebJSON(jsonParams);      //DB Access 파라메터 생성
        mjsonParams.setDataObject(jsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //하위 문의유형 저장
        JSONArray jsonArray = jsonParams.getDataObject("USER_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intUserSize = jsonArray.size();
            for(int i = 0; i < intUserSize; i++) {
                TelewebJSON jsonQstnParams = new TelewebJSON(jsonParams);
                jsonQstnParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));					//고객사ID
                jsonQstnParams.setString("USER_ID", jsonParams.getString("USER_ID"));						//사용자명
                
                jsonQstnParams.setString("QSTN_TYPE_ID", jsonParams.getString("QSTN_TYPE_ID"));			//문의유형ID
                
                jsonQstnParams.setString("STNG_USER_ID", jsonArray.getJSONObject(i).getString("USER_ID"));	//할당 사용자ID
                jsonQstnParams.setString("DEPT_ID", jsonArray.getJSONObject(i).getString("DEPT_ID"));	//할당 사용자ID

                returnJsonArray.add(i, mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "DELETE_TALK_USER_INQRY", jsonQstnParams));
            }
            objRetParams.setDataObject("QSTN_INFO", returnJsonArray);
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserPage_new(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingProCounselByAgentManageMapper", "selectRtnUserPage_new", jsonParams);
    }
}
