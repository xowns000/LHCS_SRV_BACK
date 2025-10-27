package kr.co.hkcloud.palette3.chat.setting.app;


import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("chatSettingBannedWordService")
public class ChatSettingBannedWordServiceImpl implements ChatSettingBannedWordService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;


    /**
     * 금칙어 리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getProhibiteWordList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "getProhibiteWordList", jsonParams);
    }


    /**
     * 금칙어 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getProhibiteWordDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "getProhibiteWordDetail", jsonParams);
    }


    /**
     * 금칙어 신규 등록 / 수정 (캐싱 처리)
     * 
     * @version 5.0
     * @see     com.RoutingToAgentManagerService.teletalk.core.router.app.RoutingToAgentManager.selectProhibiteWords
     */
    @Override
    @CacheEvict(value = "palette:cache:prohibite-words",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheProhibiteWordsGenerate).generate(#jsonParams)")
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException
    {
        String fbdwdId = jsonParams.getString("FBDWD_ID");
        if(fbdwdId.equals("")) {
        	fbdwdId = Integer.toString(innbCreatCmmnService.createSeqNo("FBDWD_ID"));
        	jsonParams.setString("FBDWD_ID", fbdwdId);
        	
        	return mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "postProhibiteWord", jsonParams);
        } else {
        	return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "putProhibiteWord", jsonParams);
        }
    }


    /**
     * 금칙어 수정 (캐싱 처리)
     * 
     * @version 5.0
     */
    @Override
    @CacheEvict(value = "palette:cache:prohibite-words",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheProhibiteWordsGenerate).generate(#jsonParams)")
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON putProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "putProhibiteWord", jsonParams);
    }


    /**
     * 금칙어 삭제 (캐싱 처리)
     * 
     * @version 5.0
     */
    @Override
    @CacheEvict(value = "palette:cache:prohibite-words",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheProhibiteWordsGenerate).generate(#jsonParams)")
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objParams = new TelewebJSON(jsonParams);
    	
    	List<String> arrFbdwdId = new LinkedList<String>();
    	JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrFbdwdId") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrFbdwdId.add(strValue);
                }
            }
        }
        //신규설정메뉴가 있으면
        if(arrFbdwdId.size() != 0) {
        	jsonParams.setObject("arrFbdwdId", 0, arrFbdwdId);
		
        	objParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "deleteProhibiteWord", jsonParams);
		}
        
        return objParams;
    }

    
    /**
     * 금칙어 중복체크
     * 
     * @version 5.0
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON fbdwdDuplCheck(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "fbdwdDuplCheck", jsonParams);
    }
}
