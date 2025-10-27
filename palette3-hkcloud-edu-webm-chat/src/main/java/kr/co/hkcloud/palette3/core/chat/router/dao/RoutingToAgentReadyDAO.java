package kr.co.hkcloud.palette3.core.chat.router.dao;

import java.sql.SQLException;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RoutingToAgentReadyDAO {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;

    /**
     * 배분 대기 등록 - 채팅 상담 생성
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON insertTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException {
        log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + "::insertTalkUserReady - 배분 대기 등록 : {}", mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + ":1@#!@#!" + mjsonParams);
        log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + ":444444441111" + mjsonParams.getString("CHT_USER_KEY"));
        objRetParams.setString("DLVR_CUTT_HSTRY_ID", mjsonParams.getString("CHT_CUTT_ID"));
        String chtRdyId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_ID"));
        mjsonParams.setString("CHT_RDY_ID", chtRdyId);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkUserReady", mjsonParams);
        log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + ":444444442222" + mjsonParams.getString("CHT_USER_KEY"));

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!채팅_상담 생성!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String chtCuttId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID"));
        mjsonParams.setString("CHT_CUTT_ID", chtCuttId);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttReady", mjsonParams);
        log.info("===>chtCuttId ::: " + mjsonParams.getString("CHT_USER_KEY") + "::: " + chtCuttId);

        if (mjsonParams.getString("ALTMNT_STTS_CD")
                       .equals("QSTN_CHCING")) {
            String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
            mjsonParams.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttDtlReady", mjsonParams);
            log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + ":444444444444" + mjsonParams.getString("CHT_USER_KEY"));
        }

        //상담 인사 메시지 중복으로 나가지 않도록 체크 (새로운 상담 이력이 생겼는지 체크)
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "newCuttCheck", mjsonParams);

        log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + ":44444444" + mjsonParams.getString("CHT_USER_KEY"));
        objRetParams.setString("CHT_CUTT_ID", chtCuttId);
        objRetParams.setString("CHT_RDY_ID", chtRdyId);

        objRetParams.setInt("IS_UPDATE", 0); //해당 필드가 어디에까지 쓰이는지 확인 후 필요 없을 시 삭제 (기존 중복 확인 쿼리 결과의 count 업데이트 해주던 필드)
        return objRetParams;
    }

    /**
     * 챗봇상담 상담 및 대기 등록
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON insertChbtCutt(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        mjsonParams.setString("CHBT_YN", "Y");
        if (mjsonParams.getString("CUTT_TYPE").equals("insert")) {

            String chtRdyId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_ID"));
            mjsonParams.setString("CHT_RDY_ID", chtRdyId);
            log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + "::insertChbtCutt - 챗봇상담 대기 등록 : {}", chtRdyId);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkUserReady",
                mjsonParams);

            String chtCuttId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID"));
            mjsonParams.setString("CHT_CUTT_ID", chtCuttId);
            log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + "::insertChbtCutt - 챗봇상담 상담 등록 : {}", chtCuttId);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttReady", mjsonParams);
        } else if (mjsonParams.getString("CUTT_TYPE").equals("update")) {
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateChbtCuttRdy", mjsonParams);
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateChbtCutt", mjsonParams);
        }

        if (mjsonParams.getString("TYPE").equals("product")) {
            //상품문의를 클릭했을 경우
            //어떤상품 문의인지 저장하기위함
            log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + "::INSERT_PLT_CHT_CUTT_DTL - 문의상품정보 등록 : {}", mjsonParams.getString(
                "CHT_CUTT_ID"));
            log.info("===>" + mjsonParams.getString("CHT_USER_KEY") + "::INSERT_PLT_CHT_CUTT_DTL - 문의상품정보 등록 파라미터 : {}", mjsonParams);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", mjsonParams);
        }

        return objRetParams;
    }

    /**
     * 배분 상세 등록
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON insertTalkUserReadyDetail(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkUserReadyDetail",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 전달 완료 후 기존 상담 중이던 이력 데이터의 상태값을 전달대기 완료로 업데이트 처리함
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkContactStat(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkTrans", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담 대기건수 조회
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectTalkUserReadyAgent(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReadyAgent",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 상담이력데이타 조회
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON selectRtnTalkHistInfo(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRtnTalkHistInfo", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담 대기 저장
     * 
     * @Transactional Auto Commit
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON insertTalkReady(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkReady", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담 대기 저장
     * 
     * @Transactional Auto Commit
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON insertTalkReadyByUserId(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkReadyByUserId",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 진행 중 상담 삭제
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON deleteTalkReady(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", mjsonParams);

        return objRetParams;
    }

    /**
     * 배분 상세 병합
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON mergeTalkUserReadyDetail(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //        String strDbms = env.getString("defaultDBMS");
        //        if (strDbms != null && "MYSQL".equals(strDbms))
        //        {
        // insert / update 수정 
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserReadyDetail",
            mjsonParams);
        int cnt = objRetParams.getHeaderInt("COUNT");
        if (cnt == 0)   // 동일한 값이 없다면 update
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkUserReadyDetail",
                mjsonParams);
        //        }
        //        else
        //        {
        //            objRetParams = mobjDao.insert("kr.co.hkcloud.palette.core.chat.router.dao.TalkRouteMapper", "mergeTalkUserReadyDetail", mjsonParams);
        //        }
        return objRetParams;
    }

    /**
     * 분배대기 고객의 상태코드 조회
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON selectTalkUserReadyCode(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectTalkUserReadyCode",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 인입된 고객의 상담이 안내메시지 입력 확인상태일 때
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON selectInfoMsgJson(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectInfoMsgJson",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 인입된 고객의 상담이 예약 확인상태일 때
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = {Exception.class,
        SQLException.class}, readOnly = false)
    public TelewebJSON selectRsvtCutt(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "selectRsvtCutt",
            mjsonParams);

        return objRetParams;
    }

    /**
     * 이전 문의유형 으로 업데이트
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkUserReadyInqryCd(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateTalkUserReadyInqryCd", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateChtQstn", mjsonParams);

        String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        mjsonParams.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttDtlReady", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담 대기등록 (10)
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkUserReady10(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserReady10", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateCuttRdy", mjsonParams);

        String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));
        mjsonParams.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertCuttDtlReady", mjsonParams);

        return objRetParams;
    }

    /**
     * 상담 대기등록 - 상담사배정 (11)
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON updateTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateTalkUserReady", mjsonParams);

        //고객대기에서 대기이력테이블에 데이터 저장
        mjsonParams.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
        mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", mjsonParams);

        return objRetParams;
    }

    //    /**
    //     * 배분 대기 건 수 조회
    //     *  - 호출하는 곳이 없음 20191014
    //     * @param HashMap
    //     * @return TelewebJSON 형식의 처리 결과 데이터
    //     */
    //    public TelewebJSON selectCntTalkUserReady(TelewebJSON mjsonParams) throws TelewebDaoException
    //    {
    //        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    //
    //        objRetParams = mobjDao.select("kr.co.hkcloud.palette.core.chat.router.dao.TalkRouteMapper", "selectCntTalkUserReady", mjsonParams);
    //
    //        return objRetParams;
    //    }

    /**
     * 진행 중 상담 삭제
     * 
     * @param HashMap
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    public TelewebJSON deleteTalkReadySession(TelewebJSON mjsonParams) throws TelewebDaoException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReadySessionId",
            mjsonParams);

        return objRetParams;
    }
    
}
