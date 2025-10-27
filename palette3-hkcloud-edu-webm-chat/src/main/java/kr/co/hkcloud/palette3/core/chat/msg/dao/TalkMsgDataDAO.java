package kr.co.hkcloud.palette3.core.chat.msg.dao;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Repository
public class TalkMsgDataDAO
{
    private final TwbComDAO mobjDao;
    private final ChatSettingBannedWordUtils chatSettingBannedWordUtils;

    private final InnbCreatCmmnService      innbCreatCmmnService;


    /**
     * 고객접수, 대기 건을 조회한다
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON selectTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "SELECT_PLT_CHT_USER_RDY", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담이력 TALK_CONTACT_ID 조회
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON selectTalkContactId(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "SELECT_TALK_CONTACT_ID", mjsonParams);

        return objRetParams;
    }


    /**
     * 수신메세지 저장
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON insertCntRcvMsg(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.info("insertCntRcvMsg mjsonParams ===> " + mjsonParams);
        log.info("insertCntRcvMsg mjsonParams.getString(MSG_TYPE_CD) ===> " + mjsonParams.getString("MSG_TYPE_CD"));
        //다중이미지 처리
        if(mjsonParams.getString("MSG_TYPE_CD").equals("photo")) {
        	//이미지를 받았을 때
        	String imagesUrlString = mjsonParams.getString("CONTENTS");
        	String[] imagesUrlArray = imagesUrlString.split(",");
            
            // 배열을 순회하면서 for문 실행
        	int count = 0;
            for (String url : imagesUrlArray) {
                log.info("CONTENT ===> " + url);
                mjsonParams.setString("CONTENT",url);
                String msg = chatSettingBannedWordUtils.parseContent_3(url, mjsonParams.getString("CUSTCO_ID"));
                mjsonParams.setString("CHG_CONTENT",msg);
                
                // 첫 번째 반복이 아닐 때만 serialNumber 생성
                if (count > 0) {
                    mjsonParams.setString("CHT_CUTT_DTL_ID",Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
                }
                log.info("insert mjsonParams ===> " + mjsonParams);
                
                objRetParams = mobjDao
                    .insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", mjsonParams);
                
                count++;
            }
        } else {
            //금칙어 변환
            String msg = chatSettingBannedWordUtils.parseContent_3(mjsonParams.getString("CONTENT"), mjsonParams.getString("CUSTCO_ID"));
            mjsonParams.setString("CHG_CONTENT",msg);
            
            objRetParams = mobjDao
                .insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 상담이력의 상담중 건을 후처리 상태로 업데이트
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON updateTalkContactPost(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "UPDATE_TALK_CONTACT_POST", mjsonParams);

        return objRetParams;
    }


    /**
     * user_key가 상담 대기가 없고 문의유형 선택중인지 체크한다
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON selectTalkUserCutt(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "selectTalkUserCutt", mjsonParams);

        return objRetParams;
    }


    /**
     * user_key가 상담 대기가 없고 문의유형 선택중인지 체크한다
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON updadateExpiredSessionCutt(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "updadateExpiredSessionCutt", mjsonParams);

        return objRetParams;
    }
    
}
