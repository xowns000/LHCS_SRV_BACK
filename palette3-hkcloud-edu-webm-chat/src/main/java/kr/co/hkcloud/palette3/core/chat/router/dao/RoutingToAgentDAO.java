package kr.co.hkcloud.palette3.core.chat.router.dao;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Repository
public class RoutingToAgentDAO
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final ChatProperties       chatProperties;
    private final TwbComDAO            twbComDAO;


    /**
     * 대기중 상담 전체 삭제
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteAllTalkReady(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteAllTalkReady", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담중 상담 전체 삭제
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteAllTalkIng(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteAllTalkIng", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담 건수 삭제
     * 
     * @Transactional         Auto Commit
     * @param         mjsonParams
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteTalkStack(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkStack", mjsonParams);

        return objRetParams;
    }


    /**
     * 시스템 컨피그 추출
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkEnv() throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkEnv", jsonParams);

        return objRetParams;
    }


    /**
     * 금칙어 추출
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectProhibiteWords(@NotEmpty String custcoId) throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("CUSTCO_ID", custcoId);
        log.info("><><><><"+ jsonParams);

        //TelewebJSON objRetParams = twbComDAO.select("com.hcteletalk.teletalk.mng.prohibiteword.dao.TalkMngProhibiteWordMapper", "getProhibiteWordListByServiceable", jsonParams);
        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingBannedWordMapper", "getProhibiteWordListByServiceable", jsonParams);

        return objRetParams;
    }


    /**
     * 시스템메시지 추출
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSystemMessage() throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "getSystemMessageListByServiceable", jsonParams);

        return objRetParams;
    }


    /**
     * 안내메시지 발송 메시지 조회
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectInfoMsg() throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectInfoMsg", jsonParams);

        return objRetParams;
    }


    /**
     * 안내메시지 발송후 발송time update
     * 
     * @Transactional             Auto Commit
     * @param         TelewebJSON
     * @return                    TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateTalkSetTime(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkSetTime", mjsonParams);

        return objRetParams;
    }


    /**
     * 2018.10.15 lsm 문의유형 선택 후, 상담사 배정이 지연될 경우 데이터 이관
     * 
     * @param jsonParams
     */
    @Transactional(readOnly = false)
    public void delayByClose(JSONObject jsonParams) throws TelewebDaoException
    {
        TelewebJSON inJson = new TelewebJSON();
        String strBizCase = "TALK";


        //상담이력 ID를 가져온다.
        String strContactId = innbCreatCmmnService.getSeqNo(inJson, strBizCase);
        inJson.setString("TALK_CONTACT_ID", strContactId);
        inJson.setString("TALK_USER_KEY", jsonParams.getString("CHT_USER_KEY"));
        inJson.setString("CHT_USER_KEY", jsonParams.getString("CHT_USER_KEY"));
        inJson.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        inJson.setString("SNDR_KEY", jsonParams.getString("SNDR_KEY"));

        //고객대기에서 대기이력테이블에 데이터 저장
        inJson.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
        twbComDAO.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", inJson);

        //고객대기에서 해당 데이터 삭제
        twbComDAO.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", inJson);

        //상담이력에서 해당 데이터 상태 업데이트
        twbComDAO.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkReadyInfo", inJson);
    }


    /**
     * 고객 무응답 메시지 조회
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCustNoResponseMsg() throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectCustNoResponseMsg", jsonParams);

        return objRetParams;
    }


    /**
     * 고객 무응답 메시지 발송후 발송 후 발송시간 수정
     * 
     * @param  mjsonParams
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateCustNoResponeTime(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateContactCustMsgTime", mjsonParams);

        return objRetParams;
    }


    /**
     * 고객 문의유형 선택 안하는 고객 리스트
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectInqryTypeNoContact() throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();

        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectInqryTypeNoContact", jsonParams);

        return objRetParams;

    }


    /**
     * 시스템 메시지 시간 업데이트
     * 
     * @param msgJson
     */
    @Transactional(readOnly = false)
    public void updateMsgTimeByTalkReady(TelewebJSON msgJson) throws TelewebDaoException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject("DATA", msgJson);

        twbComDAO.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateMsgTimeByTalkReady", jsonParams);
    }


    /**
     * 문의유형 선택안한 데이터 이관
     * 
     * @param jsonParams
     */
    @Transactional(readOnly = false)
    public boolean inqryTypeNoContactByClose(JSONObject jsonParams) throws TelewebDaoException
    {
        TelewebJSON inJson = new TelewebJSON();
        String strBizCase = "TALK";
        boolean result = false;


        //상담이력 ID를 가져온다.
        String strContactId = innbCreatCmmnService.getSeqNo(inJson, strBizCase);
        inJson.setString("TALK_CONTACT_ID", strContactId);
        inJson.setString("CHT_USER_KEY", jsonParams.getString("CHT_USER_KEY"));
        inJson.setString("SNDR_KEY", jsonParams.getString("SNDR_KEY"));

        //문의유형 선택 안함 데이터 이관
        //인입 시 상담 테이블에 바로 들어간 상태 = > 상태 변경
//        twbComDAO.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertNoInqryToContact", inJson);
        twbComDAO.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateNoInqryToContact", inJson);

        //고객대기에서 대기이력테이블에 데이터 저장
//        twbComDAO.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", inJson);

        //고객대기에서 해당 데이터 삭제
        twbComDAO.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyQstnInfo", inJson);

        //상담이력상세에 저장
//        twbComDAO.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", inJson);

        //고객대기상세에서 해당 데이터 삭제
//        twbComDAO
//            .delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyDetailInfoNotExistMaster", inJson);

        //정상 종료(삭제) 여부 확인 
        TelewebJSON objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "existsTalkReadyByTalkUserKey", inJson);
        JSONArray userIdLoop = objRetParams.getDataObject();
        if(userIdLoop.size() == 0)		// 정상 삭제 
        {
            result = true;
        }

        return result;
    }


    /**
     * 전문상담 라우팅 조회 및 상담사 선점 ( ready update ) , stack 테이블이 아닌 당일 채팅 대기로 배정 판단.
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON readyTalkRouteToSpecAgent(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        twbComDAO
            .update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "readyTalkRouteToSpecAgent", mjsonParams);

        // 배정 사용자 반환 처리 
        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectReadyAgent", mjsonParams);
        JSONArray userIdLoop = objRetParams.getDataObject();
        if(userIdLoop.size() != 0) {
            //고객대기에서 대기이력테이블에 데이터 저장
        	mjsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
            twbComDAO
                .insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 배분 대기 조회
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReady", mjsonParams);

        return objRetParams;
    }


    /**
     * 배분 대기 조회
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectDesignatedTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectDesignatedTalkUserReady", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담대기 ID 조회
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkReadyUserId(TelewebJSON mjsonParams) throws TelewebDaoException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setInt("MAX_AGENT_CHAT", chatProperties.getMaxAgentChat());  //Integer.toString(env.getInt("maxAgentChat", 3)));
        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkReadyUserId", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담 대기건수 조회 (11제외)
     * 
     * @param  HashMap
     * @return         TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkUserReadyAgentNot11(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReadyAgentNot11", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담 대기 상세조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebDaoException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkUserReadyInfo(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReadyInfo", mjsonParams);

        return objRetParams;
    }


    /**
     * 라우팅 조회 및 상담사 선점 ( ready update ) , stack 테이블이 아닌 당일 채팅 대기로 배정 판단.
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON readyTalkRouteToAgent(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        twbComDAO
            .update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "readyTalkRouteToAgent", mjsonParams);

        // 배정 사용자 반환 처리 
        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectReadyAgent", mjsonParams);
        JSONArray userIdLoop = objRetParams.getDataObject();
        if(userIdLoop.size() != 0) {
            //고객대기에서 대기이력테이블에 데이터 저장
        	mjsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
            twbComDAO
                .insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 라우팅 조회 및 지정 상담사 업데이트 ( ready update )
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON readyTalkRouteToDesignatedAgent(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        twbComDAO
            .update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "readyTalkRouteToDesignatedAgent", mjsonParams);

        // 배정 사용자 반환 처리 
        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectReadyAgent", mjsonParams);
        JSONArray userIdLoop = objRetParams.getDataObject();
        if(userIdLoop.size() != 0) {
            //고객대기에서 대기이력테이블에 데이터 저장
        	mjsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
            twbComDAO
                .insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 라우팅 조회 및 직전상담사 선점 ( ready update )
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON readyTalkRouteToBeforeAgent(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        twbComDAO
            .update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "readyTalkRouteToBeforeAgent", mjsonParams);

        // 배정 사용자 반환 처리 
        objRetParams = twbComDAO
            .select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectReadyAgent", mjsonParams);
        JSONArray userIdLoop = objRetParams.getDataObject();
        if(userIdLoop.size() != 0) {
            //고객대기에서 대기이력테이블에 데이터 저장
        	mjsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
            twbComDAO
                .insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", mjsonParams);
        }

        return objRetParams;
    }
    
    
    /**
     * 상담 테이블에 대기중인 상담을 배분처리 ( ready update )
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRoutingCutt(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        
        // 사용자 배분여부
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectReadyAgent", mjsonParams);

        log.info("objRetParams 배분 있는지 체크 == {}", objRetParams);
        JSONArray userIdLoop = objRetParams.getDataObject();

        log.info("objRetParams 배분 있는지 체크 == {}", userIdLoop.size());
        if(userIdLoop.size() != 0) {
            log.info("=================배분 시작=================");
            //배분대기 고객 상담 시작
            twbComDAO.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateRoutingCutt", mjsonParams);
        }

        return objRetParams;
    }

    /* 전체 스키마별(tenant) custco아이디 조회  */
    @Transactional(readOnly = false)
    public TelewebJSON getAllSchemaCustco(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "getAllSchemaCustco", mjsonParams);

        return objRetParams;
    }

    /* 채팅상담 중 대기상태인 상담중 대기테이블에 없는 상담이 있는지 체크 */
    @Transactional(readOnly = false)
    public TelewebJSON selectCuttRdyReInsert(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectCuttRdyReInsert", mjsonParams);

        return objRetParams;
    }


    /* 배정되지 않은 채팅 대기테이블에 없을 시 대기 테이블에 다시 insert (이메일상담 / 게시판상담) */
    @Transactional(readOnly = false)
    public void cuttRdyReInsert(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "cuttRdyReInsert", mjsonParams);
    }

    /* 채팅상담 중 대기상태인 상담중 대기테이블에 없는 상담이 있는지 체크 */
    @Transactional(readOnly = false)
    public TelewebJSON selectCuttReInsert(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectCuttReInsert", mjsonParams);

        return objRetParams;
    }


    /* 대기테이블에서 배정되었지만 상담테이블에서 배정처리되지 않은경우 */
    @Transactional(readOnly = false)
    public void cuttReInsert(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "cuttReInsert", mjsonParams);
    }

    /* 중복된 상담이력이 있는지 조회(CUTT테이블) */
    @Transactional(readOnly = false)
    public TelewebJSON selectDuplCutt(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectDuplCutt", mjsonParams);

        return objRetParams;
    }

    /* 중복된 상담이력이 있는지 조회(RDY테이블) */
    @Transactional(readOnly = false)
    public TelewebJSON selectDuplCuttRdy(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectDuplCuttRdy", mjsonParams);

        return objRetParams;
    }

    /* PLT_CHT_RDY 테이블에서 중복상담 삭제 */
    @Transactional(readOnly = false)
    public TelewebJSON duplCuttRdyDelete(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "duplCuttRdyDelete", mjsonParams);

        return objRetParams;
    }

    /* PLT_CHT_CUTT 테이블에서 중복상담 처리 */
    @Transactional(readOnly = false)
    public TelewebJSON duplCuttProcess(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "duplCuttProcess", mjsonParams);

        return objRetParams;
    }

    /* PLT_CHT_CUTT_DTL 테이블에서 중복상담 병합 */
    @Transactional(readOnly = false)
    public TelewebJSON duplCuttDtlMerge(TelewebJSON mjsonParams) throws TelewebDaoException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "duplCuttDtlMerge", mjsonParams);

        return objRetParams;
    }
}
