package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;

import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.cache.app.TalkRedisCacheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("routingToAgentManagerService")
public class RoutingToAgentManagerServiceImpl implements RoutingToAgentManagerService
{
    private final RoutingToAgentDAO routingToAgentDAO;
    private final RouterInformationMessageService routerInformationMessageService;
    private final RouterNoCustomerResponseService routerNoCustomerResponseService;
    private final RouterInquiryTypeNoneService routerInquiryTypeNoneService;
    private final RoutingToAgentService routingToAgentService;
    private final TalkRedisCacheService talkRedisCacheService;

    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 상담초기화
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public void consultationIitialization() throws TelewebAppException
    {
        TelewebJSON inJson = new TelewebJSON();
        routingToAgentDAO.deleteAllTalkReady(inJson);
        routingToAgentDAO.deleteAllTalkIng(inJson);
        routingToAgentDAO.deleteTalkStack(inJson);
    }


    /**
     * 시스템 컨피그 추출
     *
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTalkEnv() throws TelewebAppException
    {
        return routingToAgentDAO.selectTalkEnv();
    }


    /**
     * 시스템메시지 추출
     *
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSystemMessage() throws TelewebAppException
    {
        return routingToAgentDAO.selectSystemMessage();
    }


    /**
     * 안내메세지 처리 (배정지연메시지) throw 처리하면 안됨
     */
    @Override
    public void processInfoMsg()
    {
        try
        {
            //고객별 안내메시지 조회 - 이메일, 게시판은 채팅이 아니므로 제외한다.
            TelewebJSON outJson = routerInformationMessageService.selectInfoMsg();
            JSONArray talkInfoMsgLoop = outJson.getDataObject();
            if(talkInfoMsgLoop.size() == 0)
            {
                return;
            }

            for(int i = 0; i < talkInfoMsgLoop.size(); i++)
            {
                try
                {
                    routerInformationMessageService.processInfoMsg(talkInfoMsgLoop.getJSONObject(i));   // 1건마다 커밋( 트랜잭션 불필요 ) 
                    Thread.sleep(1);
                } catch(Exception e)
                {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * 고객 문의유형 선택안함 처리 Throw 처리하면 안됨
     */
    @Override
    public void processInqryTypeNoContact() throws TelewebAppException
    {
        try
        {
            //이메일, 게시판은 채팅이 아니므로 제외한다.
            TelewebJSON outJson = routerInquiryTypeNoneService.selectInqryTypeNoContact();
            JSONArray inqryTypeNoContactList = outJson.getDataObject();

            for(int i = 0; i < inqryTypeNoContactList.size(); i++)
            {
                try
                {
                    routerInquiryTypeNoneService.processInqryTypeNoContact(inqryTypeNoContactList.getJSONObject(i));    // 1건마다 커밋( 트랜잭션 불필요 )
                    Thread.sleep(1);
                } catch(Exception e)
                {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * 고객 무응답 메시지 처리 throw 처리하면 안됨
     */
    @Override
    public void processCustNoResponseMsg() throws TelewebAppException
    {
        try
        {
            //고객별 안내메시지 조회 - 이메일, 게시판은 채팅이 아니므로 제외한다.
            TelewebJSON outJson = routerNoCustomerResponseService.selectCustNoResponseMsg();
            JSONArray talkCustNoResponseMsgLoop = outJson.getDataObject();
            if(talkCustNoResponseMsgLoop.size() == 0)
            {
                return;
            }

            for(int i = 0; i < talkCustNoResponseMsgLoop.size(); i++)
            {
                try
                {
                    routerNoCustomerResponseService.processCustNoResponseMsg(talkCustNoResponseMsgLoop.getJSONObject(i));
                    Thread.sleep(1);
                } catch(Exception e)
                {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }


    /**
     * 상담원 배분 처리 => 대기중인 상담원이 있는 경우만 처리되야 한다 throw 처리하면 안됨
     *
     * @param maxAgentChat
     */
    @Override
    public void processRoutingToAgent() throws TelewebAppException
    {
        try
        {
            TelewebJSON inJson = new TelewebJSON();
            TelewebJSON outJson = new TelewebJSON();
            TelewebJSON chkJson = new TelewebJSON();

            //고객접수건이 있는지 조회한다.
            //전달 건보다 신규 접수 고객 건이 우선한다

            // 지정상담사 배분 ( 람다 스레드 또는 이벤트로 처리 예정) 
            TelewebJSON outJson2 = routingToAgentDAO.selectDesignatedTalkUserReady(inJson);
            JSONArray designatedTalkUserKeyLoop = outJson2.getDataObject();
            log.trace("designatedTalkUserKeyLoop.size() == {}", designatedTalkUserKeyLoop.size());
            for(int i = 0; i < designatedTalkUserKeyLoop.size(); i++)
            {
                try
                {
                    routingToAgentService.processRoutingToDesignatedAgent(designatedTalkUserKeyLoop.getJSONObject(i));
                    Thread.sleep(1);
                } catch(Exception e)
                {
                    log.error(e.getLocalizedMessage(), e);
                }
            }

            //2. 고객접수건(10)이 있을 때
            outJson = routingToAgentDAO.selectTalkUserReady(inJson);
            JSONArray talkUserKeyLoop = outJson.getDataObject();
            log.info("talkUserKeyLoop.size() == {}", talkUserKeyLoop.size());

            //2. 고객접수건(10)이 있을 때
            for(int i = 0; i < talkUserKeyLoop.size(); i++)
            {
                try
                {
                    log.info("talkUserKeyLoop in == {}", talkUserKeyLoop.getJSONObject(i));
                    routingToAgentService.processRoutingToAgent(talkUserKeyLoop.getJSONObject(i));
                    Thread.sleep(1);
                } catch(Exception e)
                {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 스키마.custco 별 TELETALK 레디스데이터 체크
     *
     * @throws TelewebAppException
     */
    @Override
    public void executeTeletalkRedisSettingJob() throws TelewebAppException {
        try
        {
            TelewebJSON mjsonParams = new TelewebJSON();
            TenantContext.setCurrentTenant( TenantContext.DEFAULT_TENANT_CODE );
            TelewebJSON schemaCuscoObj = routingToAgentDAO.getAllSchemaCustco(mjsonParams);
            JSONArray schemaCuscoObjArry = schemaCuscoObj.getDataObject();
            if(!schemaCuscoObjArry.isEmpty()) {
                for (int i = 0; i < schemaCuscoObjArry.size(); i++) {
                    TenantContext.setCurrentTenant( schemaCuscoObj.getString("SCHEMA_ID", i) );
                    TenantContext.setCurrentCustco( schemaCuscoObj.getString("CUSTCO_ID", i) );
                    long size = talkRedisCacheService.getSizeDbEnv();
                    if( size == 0 ) {
                        log.info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- executeTeletalkRedisSettingJob HcTeletalkDbEnvironment.setDbEnvironment() !!! ::: size ::: " + size + 
                            ", tenant ::: " + schemaCuscoObj.getString("SCHEMA_ID", i) + 
                            ", custco_id ::: " + schemaCuscoObj.getString("CUSTCO_ID", i));
                        //텔레톡 상담 설정
                        TelewebJSON outJson = this.selectTalkEnv();
                        HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);
                        log.info("=================================");
                        log.info("Chat Application outJson : ", outJson.toString());
                        log.info(outJson.toString());
                        log.info("=================================");

                        //시스템메시지 설정
                        TelewebJSON sysMsgJson = this.selectSystemMessage();
                        HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);
                        log.info("=================================");
                        log.info("Chat Application sysMsgJson : ", sysMsgJson.toString());
                        log.info("=================================");
                    }
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 배정되지 않은 채팅 대기테이블에 없을 시 대기 테이블에 다시 insert (이메일상담 / 게시판상담)
     *
     * @throws TelewebAppException
     */
    @Override
    public void cuttRdyReInsert() throws TelewebAppException {
        try
        {
            //채팅상담 중 대기상태인 상담중 대기테이블에 없는 상담이 있는지 체크
        	TelewebJSON inJson = new TelewebJSON();
	        TelewebJSON outJson = routingToAgentDAO.selectCuttRdyReInsert(inJson);
	        
	        JSONArray selectCuttRdyReInsertArry = outJson.getDataObject();
	        
	        if(!selectCuttRdyReInsertArry.isEmpty()) {
                for (int i = 0; i < selectCuttRdyReInsertArry.size(); i++) {
                	if(selectCuttRdyReInsertArry.getJSONObject(i).getString("CHT_RDY_ID").equals("-1")) {
	                	inJson.setString("CUST_ID", selectCuttRdyReInsertArry.getJSONObject(i).getString("CUST_ID"));
	                	inJson.setString("CHT_CUTT_ID", selectCuttRdyReInsertArry.getJSONObject(i).getString("CHT_CUTT_ID"));
	                	String chtRdyId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_ID"));
	                	inJson.setString("CHT_RDY_ID", chtRdyId);
	                	//배정되지 않은 채팅 대기테이블에 없을 시 대기 테이블에 다시 insert (이메일상담 / 게시판상담)
	                	routingToAgentDAO.cuttRdyReInsert(inJson);
                	}
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 대기테이블에서는 배정되었지만 상담테이블에서는 배정처리되지 않은경우
     *
     * @throws TelewebAppException
     */
    @Override
    public void cuttReInsert() throws TelewebAppException {
        try
        {
            //채팅상담 중 대기상태인 상담중 대기테이블에 없는 상담이 있는지 체크
        	TelewebJSON inJson = new TelewebJSON();
	        TelewebJSON outJson = routingToAgentDAO.selectCuttReInsert(inJson);
	        
	        JSONArray selectCuttReInsertArry = outJson.getDataObject();
	        
	        if(!selectCuttReInsertArry.isEmpty()) {
                for (int i = 0; i < selectCuttReInsertArry.size(); i++) {
                	if(!selectCuttReInsertArry.getJSONObject(i).getString("CHT_CUTT_ID").equals("-1")) {
	                	inJson.setString("CHT_CUTT_ID", selectCuttReInsertArry.getJSONObject(i).getString("CHT_CUTT_ID"));
	                	inJson.setString("CUSL_ID", selectCuttReInsertArry.getJSONObject(i).getString("CUSL_ID"));
	                	inJson.setString("CUTT_RDY_REG_DT", selectCuttReInsertArry.getJSONObject(i).getString("CUTT_RDY_REG_DT"));
	                	//배정되지 않은 채팅 대기테이블에 없을 시 대기 테이블에 다시 insert (이메일상담 / 게시판상담)
	                	routingToAgentDAO.cuttReInsert(inJson);
                	}
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 중복으로 저장된 상담 삭제처리
     *
     * @throws TelewebAppException
     */
    @Override
    public void duplCuttDelete() throws TelewebAppException {
        try
        {
            //채팅상담 중 대기상태인 상담중 대기테이블에 없는 상담이 있는지 체크
        	TelewebJSON inJson = new TelewebJSON();
	        TelewebJSON outJson = routingToAgentDAO.selectDuplCutt(inJson);
	        
	        JSONArray selectCuttReInsertArry = outJson.getDataObject();
	        
	        if(!selectCuttReInsertArry.isEmpty()) {
                for (int i = 0; i < selectCuttReInsertArry.size(); i++) {
                	inJson.setString("CHT_CUTT_ID", selectCuttReInsertArry.getJSONObject(i).getString("CHT_CUTT_ID"));
                	inJson.setString("CUST_ID", selectCuttReInsertArry.getJSONObject(i).getString("CUST_ID"));
                	inJson.setString("SNDR_KEY", selectCuttReInsertArry.getJSONObject(i).getString("SNDR_KEY"));
                	inJson.setString("REG_DT", selectCuttReInsertArry.getJSONObject(i).getString("REG_DT"));
                	//PLT_CHT_RDY 테이블에서 중복상담 삭제
                	//PLT_CHT_CUTT 테이블에서 중복상담 처리
                	//PLT_CHT_CUTT_DTL테이블에서 중복상담 병합
                	routingToAgentDAO.duplCuttRdyDelete(inJson);
                	routingToAgentDAO.duplCuttProcess(inJson);
                	routingToAgentDAO.duplCuttDtlMerge(inJson);
                }
            } else {
            	//cutt테이블에는 중복이 없지만 rdy 테이블에는 중복이 있는경우
    	        outJson = routingToAgentDAO.selectDuplCuttRdy(inJson);
    	        
    	        JSONArray selectCuttRdyArry = outJson.getDataObject();
    	        
    	        if(!selectCuttRdyArry.isEmpty()) {
	                for (int i = 0; i < selectCuttRdyArry.size(); i++) {
	                	inJson.setString("CUST_ID", selectCuttRdyArry.getJSONObject(i).getString("CUST_ID"));
	                	inJson.setString("SNDR_KEY", selectCuttRdyArry.getJSONObject(i).getString("SNDR_KEY"));
	                	inJson.setString("REG_DT", selectCuttRdyArry.getJSONObject(i).getString("REG_DT"));
	                	
	                	//PLT_CHT_RDY 테이블에서 중복상담 삭제
	                	routingToAgentDAO.duplCuttRdyDelete(inJson);
	                }
                }
            }
        } catch(Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    
}
