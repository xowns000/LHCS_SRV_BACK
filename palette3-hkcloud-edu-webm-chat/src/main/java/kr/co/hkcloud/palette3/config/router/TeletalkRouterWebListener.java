package kr.co.hkcloud.palette3.config.router;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.validation.constraints.NotBlank;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisRouterRepository;
import kr.co.hkcloud.palette3.core.chat.redis.domain.RedundancyStatus;
import kr.co.hkcloud.palette3.core.chat.redis.domain.RedundancyStatusVO;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * packageName    : kr.co.hkcloud.palette3.config.router
 * fileName       : TeletalkRouterWebListener.java
 * author         : USER
 * date           : 2023-10-12
 * description    : 텔레톡 라우터 웹리스너 - for router
 * <p>
 * 텔레톡 기동 시 상담초기화 (X)
 * <p>
 * 텔레톡 환경설정 자동반영 (必)
 * <p>
 * 상담사 배정 (必)
 * <p>
 * 시스템안내메세지 발신 (必)
 * <p>
 * 고객 무응답 메세지 발신 (必)
 * <p>
 * 고객 문의유형 선택 안함 발신 (必)
 * ACTIVE : 활성 ROUTER STANBY : 대기 ROUTER
 *
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-12       USER       최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@WebListener
@Service("teletalkRouterWebListener")
public class TeletalkRouterWebListener implements ServletContextListener {

    private static ScheduledExecutorService prohibiteExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService routeExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService infoMsgExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService sysMsgExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService noSelectByInqryTypeExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService cuttRdyReInsertExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService cuttReInsertExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService teletalkRedisSettingExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledExecutorService duplCuttDeleteExecutor = Executors.newSingleThreadScheduledExecutor();

    private static RedundancyStatus redundancyStatus = RedundancyStatus.STANDBY; //초기값은 STANDBY ㅡㅡ;

    private final ChatProperties chatProperties;
    private final TalkRedisChatReadyRepository redisChatReadyRepository;
    private final TalkRedisRouterRepository redisRouterRepository;
    private final RoutingToAgentManagerService routingToAgentManagerService;
    private final TalkBusyService busyService;
    
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
    
    String betchStatus = "STOP";

    @NotBlank
    @Value("${k8s.pod.name}")
    private String podName;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        if(podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("chat-0") > -1 || podName.equals("podname")) {
            String logPrefix = logDevider + ".contextInitialized" + "___" + podName + "___";
            int logNum = 1;
            log.info(logPrefix + (logNum++) + " ::: contextInitialized start");
            /**
             * 애플리케이션 시작 후 60초 후에 첫 실행, 그 후 매 60초마다 주기적으로 실행한다.
             * 레디스에 스키마.custco 별 TELETALK 레디스데이터 체크하여 생성시켜준다.
             */
            teletalkRedisSettingExecutor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    routingToAgentManagerService.executeTeletalkRedisSettingJob();
                }
            }, 60, 60, TimeUnit.SECONDS);

            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            TelewebJSON tenantObj = new TelewebJSON();

            //채팅 테넌트 아이디 검색
            tenantObj = busyService.selectBatchTenantList();
            if(tenantObj.getHeaderInt("COUNT") > 0) {
                JSONArray arr = new JSONArray();
                arr = tenantObj.getDataObject();
                for(int i = 0; i < arr.size(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    //라우터 활성화 여부
                    log.info(logPrefix + (logNum++) + " ::: 라우터 활성화 여부 chatProperties.isRouterEnabled() ::: " + chatProperties.isRouterEnabled());
                    if(chatProperties.isRouterEnabled()) {
                    	betchStatus = "ING";

                        //                //=======================================================================
                        //                // 텔레톡 환경설정 자동반영 (60s FixedDelay) => ready event에서 처리함
                        //                //=======================================================================
                        //                configExecutor.scheduleWithFixedDelay(new Runnable()
                        //                {
                        //                    @Override
                        //                    public void run()
                        //                    {
                        //                        //ROUTER:RUN - 텔레톡 환경설정 자동반영
                        //                        try
                        //                        {
                        ////                            //텔레톡 상담 설정
                        ////                            TelewebJSON outJson = routingToAgentManager.selectTalkEnv();
                        ////                            HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);
                        //
                        //                            //금칙어 설정
                        ////                            TelewebJSON prohibiteJson = routingToAgentManager.selectProhibiteWords();
                        ////                            JSONArray dbPrhoibiteWordsLoop = prohibiteJson.getDataObject();
                        ////                            if (dbPrhoibiteWordsLoop.size() > 0)
                        ////                            {
                        ////                                HcTeletalkDbProhibiteWords.getInstance().setDbProhibiteWords(dbPrhoibiteWordsLoop);
                        ////                            }
                        ////                            else
                        ////                            {
                        ////                                log.error("ProhibiteWords size 0? prhoibiteWordsLoop.size()={}", dbPrhoibiteWordsLoop.size());
                        ////                            }
                        //
                        ////                            //시스템메시지 설정
                        ////                            TelewebJSON sysMsgJson = routingToAgentManager.selectSystemMessage();
                        ////                            HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);
                        //                        }
                        //                        catch (Exception e)
                        //                        {
                        //                            log.error(e.getLocalizedMessage(), e);
                        //                        }
                        //                    }
                        //                }, 1, 60, TimeUnit.SECONDS);

                        //=======================================================================
                        // 상담사 배정 (2s FixedDelay)
                        //=======================================================================
                        routeExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
                                log.info("Tenant what!??" + obj.getString("SCHEMA_ID"));
                                //ROUTER:RUN - 상담사 배정
                                try {
                                    //REDIS에 ACTIVE 값이 있는 지 체크
                                    boolean isRedundancyStatusActive = redisRouterRepository.hasKey(RedundancyStatus.ACTIVE);
                                    log.info("local redundancyStatus={}, isRedundancyStatusActive={}", redundancyStatus, isRedundancyStatusActive);

                                    if(isRedundancyStatusActive) {
                                        log.info("local redundancyStatus={} 0", redundancyStatus);
                                        switch(redundancyStatus) {
                                            case ACTIVE: {
                                                //수행 후
                                                //BUSY(상담불가) 체크 - 이메일, 게시판은 채팅이 아니므로 제외한다.
                                                busyService.checkBusy();

                                                //대기 상담원이 있는 경우에만 라우팅을 수행한다.
                                                long readySize = redisChatReadyRepository.getSize();
                                                log.info("ACTIVE readySize={}", readySize);
                                                if(readySize > 0L) {
                                                    //상담사 배정 처리
                                                    routingToAgentManagerService.processRoutingToAgent();
                                                }

                                                //REDIS 현재 시간으로 업데이트
                                                redisRouterRepository.setRedundancyStatusVO(
                                                    RedundancyStatusVO.builder().redundancyStatus(RedundancyStatus.ACTIVE)
                                                        .lastRunTime(DateCmmnUtils.toEpochMilli()).build());
                                                //무조건 돈 놈이 ACTIVE다....
                                                redundancyStatus = RedundancyStatus.ACTIVE;
                                                log.info("ACTIVE local redundancyStatus={} 1", redundancyStatus);
                                                break;
                                            }
                                            case STANDBY: {
                                                //STANBY이면 REDIS ACTIVE 값 get 2분 체크
                                                RedundancyStatusVO redundancyStatusVO = redisRouterRepository.getRedundancyStatusVO(RedundancyStatus.ACTIVE);

                                                Long lastRunTimeActive = redundancyStatusVO.getLastRunTime();
                                                Long wowtime = DateCmmnUtils.toEpochMilli() - lastRunTimeActive;
                                                log.info("STANDBY wowtime={}\n\n\n\n\n\n\n", wowtime);

                                                //2분 이상이면
//                                                if(wowtime > 120000) {
                                                    //REDIS 현재 시간으로 업데이트
                                                    redisRouterRepository.setRedundancyStatusVO(
                                                        RedundancyStatusVO.builder().redundancyStatus(RedundancyStatus.ACTIVE)
                                                            .lastRunTime(DateCmmnUtils.toEpochMilli()).build());
                                                    //내가 ACTIVE가 된다...
                                                    redundancyStatus = RedundancyStatus.ACTIVE;
                                                    log.info("STANDBY change local redundancyStatus={} 2", redundancyStatus);

                                                    //수행
                                                    //BUSY(상담불가) 체크
                                                    busyService.checkBusy();

                                                    //대기 상담원이 있는 경우에만 라우팅을 수행한다.
                                                    long readySize = redisChatReadyRepository.getSize();
//                                                    log.info("STANDBY readySize={}", readySize);
                                                    if(readySize > 0L) {
                                                        //상담사 배정 처리
                                                        routingToAgentManagerService.processRoutingToAgent();
                                                        return;
                                                    }
//                                                }
                                                //PASS
//                                                else {
//                                                    //PASS 하면 STANDBY 다....
//                                                    redundancyStatus = RedundancyStatus.STANDBY;
//                                                    log.info("local redundancyStatus={} 3", redundancyStatus);
//                                                }
                                                break;
                                            }
                                            default: {
                                                throw new Exception("RedundancyStatus => ACTIVE/STANDBY only!!");
                                            }
                                        }
                                    } else {
                                        //내가 ACTIVE가 된다...
                                        redundancyStatus = RedundancyStatus.ACTIVE;
                                        log.info("ACTIVE local redundancyStatus={} 4", redundancyStatus);

                                        //REDIS 현재 시간으로 업데이트
                                        redisRouterRepository.setRedundancyStatusVO(
                                            RedundancyStatusVO.builder().redundancyStatus(RedundancyStatus.ACTIVE).lastRunTime(DateCmmnUtils.toEpochMilli())
                                                .build());
                                    }
                                } catch(Exception e) {
                                    log.error(e.getLocalizedMessage(), e);
                                }
                            }
                        }, 10, 2, TimeUnit.SECONDS);

                        //=======================================================================
                        // 시스템안내메세지 발신 (10s FixedDelay)
                        //=======================================================================
                        infoMsgExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                //ROUTER:RUN - 시스템안내메세지 발신
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
//                                    log.info("Tenant what!??" + obj.getString("SCHEMA_ID"));

                                    //안내메세지 처리 (2,4,6...)
                                    //TODO 채팅 상세 insert 오류 발생 시, 분석 시작점
                                    routingToAgentManagerService.processInfoMsg();
                                }
                            }
                        }, 20, 10, TimeUnit.SECONDS);

                        //=======================================================================
                        // 고객 무응답 메세지 발신 (10s FixedDelay)
                        //=======================================================================
                        sysMsgExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                //상담설정을 조회쿼리에서 가져오는 로직으로 수정함
                                //                        String strCustNoRespYn = HcTeletalkDbEnvironment.getInstance().getString("CUSTCO_ID", "CUST_NORESP_USE_YN");
                                //                        if ("Y".equals(strCustNoRespYn))
                                //                        {
                                //ROUTER:RUN - 고객 무응답 메세지 발신
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
//                                    log.info("Tenant what!??" + obj.getString("SCHEMA_ID"));

                                    //고객무응답메시지 처리
                                    routingToAgentManagerService.processCustNoResponseMsg();
                                }
                                //                        }
                            }
                        }, 21, 10, TimeUnit.SECONDS);

                        //=======================================================================
                        // 고객 문의유형 선택안함 발신 (10s FixedDelay)
                        //=======================================================================
                        noSelectByInqryTypeExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
//                                    log.info("Tenant what!??" + obj.getString("SCHEMA_ID"));

                                    //고객문의유형 선택안함 처리
                                    log.info(logPrefix + " ::: 고객문의유형 선택안함 처리 ::: " + chatProperties.isRouterEnabled());
                                    routingToAgentManagerService.processInqryTypeNoContact();
                                }
                            }
                        }, 22, 10, TimeUnit.SECONDS);


                        //=======================================================================
                        // 배정되지 않은 채팅 대기테이블에 없을 시 대기 테이블에 다시 insert (이메일상담 / 게시판상담)
                        //=======================================================================
                        cuttRdyReInsertExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
                                    routingToAgentManagerService.cuttRdyReInsert();
                                }
                            }
                        }, 23, 10, TimeUnit.SECONDS);


                        //=======================================================================
                        // 대기테이블에서는 배정되었지만 상담테이블에 배정처리가 되지 않은경우
                        //=======================================================================
                        cuttReInsertExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
                                    routingToAgentManagerService.cuttReInsert();
                                }
                            }
                        }, 23, 10, TimeUnit.SECONDS);


                        //=======================================================================
                        // 중복으로 저장된 상담 삭제처리
                        //=======================================================================
                        duplCuttDeleteExecutor.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                if(redundancyStatus == RedundancyStatus.ACTIVE) {
                                    TenantContext.setCurrentTenant(obj.getString("SCHEMA_ID"));
                                    routingToAgentManagerService.duplCuttDelete();
                                }
                            }
                        }, 10, 2, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //라우터 활성화 여부
        if((podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("chat-0") > -1 || podName.equals("podname")) && chatProperties.isRouterEnabled()) {
        	betchStatus = "STOP";
            try {
                prohibiteExecutor.shutdown();
                prohibiteExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*prohibiteExecutor tasks interrupted fail ::: ", e);
            } finally {
                if(!prohibiteExecutor.isTerminated()) {
                    log.info("*prohibiteExecutor tasks terminated fail :::");
                }
                prohibiteExecutor.shutdownNow();
            }

            //infoMsgExecutor shutdown
            try {
                infoMsgExecutor.shutdown();
                infoMsgExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*infoMsgExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!infoMsgExecutor.isTerminated()) {
                    log.trace("*infoMsgExecutor tasks terminated fail :::");
                }
                infoMsgExecutor.shutdownNow();
            }

            //sysMsgExecutor shutdown
            try {
                sysMsgExecutor.shutdown();
                sysMsgExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*sysMsgExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!sysMsgExecutor.isTerminated()) {
                    log.trace("*sysMsgExecutor tasks terminated fail :::");
                }
                sysMsgExecutor.shutdownNow();
            }

            //routeExecutor shutdown
            try {
                routeExecutor.shutdown();
                routeExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*routeExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!routeExecutor.isTerminated()) {
                    log.trace("*routeExecutor tasks terminated fail :::");
                }
                routeExecutor.shutdownNow();
            }

            //sysMsgExecutor shutdown
            try {
                noSelectByInqryTypeExecutor.shutdown();
                noSelectByInqryTypeExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*noSelectByInqryTypeExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!noSelectByInqryTypeExecutor.isTerminated()) {
                    log.trace("*noSelectByInqryTypeExecutor tasks terminated fail :::");
                }
                noSelectByInqryTypeExecutor.shutdownNow();
            }

            //cuttRdyReInsertExecutor shutdown
            try {
            	cuttRdyReInsertExecutor.shutdown();
            	cuttRdyReInsertExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*cuttRdyReInsertExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!cuttRdyReInsertExecutor.isTerminated()) {
                    log.trace("*cuttRdyReInsertExecutor tasks terminated fail :::");
                }
                cuttRdyReInsertExecutor.shutdownNow();
            }

            //cuttReInsertExecutor shutdown
            try {
            	cuttReInsertExecutor.shutdown();
            	cuttReInsertExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*cuttReInsertExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!cuttReInsertExecutor.isTerminated()) {
                    log.trace("*cuttReInsertExecutor tasks terminated fail :::");
                }
                cuttReInsertExecutor.shutdownNow();
            }

            //duplCuttDeleteExecutor shutdown
            try {
            	duplCuttDeleteExecutor.shutdown();
            	duplCuttDeleteExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*duplCuttDeleteExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!duplCuttDeleteExecutor.isTerminated()) {
                    log.trace("*duplCuttDeleteExecutor tasks terminated fail :::");
                }
                duplCuttDeleteExecutor.shutdownNow();
            }

            try {
                teletalkRedisSettingExecutor.shutdown();
                teletalkRedisSettingExecutor.awaitTermination(10, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                log.error("*teletalkRedisSettingExecutor tasks interrupted fail  ::: ", e);
            } finally {
                if(!teletalkRedisSettingExecutor.isTerminated()) {
                    log.trace("*teletalkRedisSettingExecutor tasks terminated fail :::");
                }
                teletalkRedisSettingExecutor.shutdownNow();
            }
        }
    }

    /**
     *
     */
    public void stopBetch() {
    	betchStatus = "STOP";
        prohibiteExecutor.shutdown();
        try {
            if(!prohibiteExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        routeExecutor.shutdown();
        try {
            if(!routeExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        infoMsgExecutor.shutdown();
        try {
            if(!infoMsgExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        sysMsgExecutor.shutdown();
        try {
            if(!sysMsgExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        noSelectByInqryTypeExecutor.shutdown();
        try {
            if(!noSelectByInqryTypeExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        cuttRdyReInsertExecutor.shutdown();
        try {
            if(!cuttRdyReInsertExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        cuttReInsertExecutor.shutdown();
        try {
            if(!cuttReInsertExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        duplCuttDeleteExecutor.shutdown();
        try {
            if(!duplCuttDeleteExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
            }
        } catch(InterruptedException e) {
        }
        
    }

    /**
     *
     */
    public void startBetch() {
    	betchStatus = "RESTART";
    	
    	prohibiteExecutor = Executors.newSingleThreadScheduledExecutor();
        routeExecutor = Executors.newSingleThreadScheduledExecutor();
        infoMsgExecutor = Executors.newSingleThreadScheduledExecutor();
        sysMsgExecutor = Executors.newSingleThreadScheduledExecutor();
        noSelectByInqryTypeExecutor = Executors.newSingleThreadScheduledExecutor();
        cuttRdyReInsertExecutor = Executors.newSingleThreadScheduledExecutor();
        cuttReInsertExecutor = Executors.newSingleThreadScheduledExecutor();
        teletalkRedisSettingExecutor = Executors.newSingleThreadScheduledExecutor();
        duplCuttDeleteExecutor = Executors.newSingleThreadScheduledExecutor();
      
        contextInitialized(null);
    }

    /**
     *
     */
    public String betchStatus() {
        return betchStatus;
    }
}
