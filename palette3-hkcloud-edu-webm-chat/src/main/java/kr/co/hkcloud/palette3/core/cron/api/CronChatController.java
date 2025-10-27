package kr.co.hkcloud.palette3.core.cron.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.busy.app.TalkBusyService;
import kr.co.hkcloud.palette3.core.cron.app.CronChatBoardService;
import kr.co.hkcloud.palette3.core.cron.app.CronChatEmailService;
import kr.co.hkcloud.palette3.core.cron.app.CronChatBatchService;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.cron.app.CollectJobManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Component
@RequiredArgsConstructor
@RestController
public class CronChatController {

    private final ChatProperties chatProperties;
    private final CronChatEmailService cronChatEmailService;
    private final CronChatBoardService cronChatBoardService;
    private final CronChatBatchService cronChatBatchService;
    private final TalkBusyService busyService;
    private final CollectJobManageService collectJobManageService;
    private final PaletteUtils paletteUtils;

    @NotBlank
    @Value("${k8s.pod.name}")
    private String podName;

    @Value("${palette.security.private.alg}")
    private String P_ALG;

    @Value("${palette.security.private.key}")
    private String P_KEY;

    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 채팅_상담_이메일 수집(IMAP/IMAPS)
     * 동작 안함 - 개발중. by hjh.
     */
    //    @PostMapping("/chat-api/cron/collectEmail")
    //    public void collectEmail() throws Exception {
    //        String logPrefix = logDevider + "___collectEmailExecutor___";
    //        long startTime = System.currentTimeMillis();
    //        log.info(logPrefix + "채팅_상담_이메일 수집 Start ::: " + startTime);
    //        JSONArray tenantArray = selectBatchTenantArray();
    //        for(int i = 0; i<tenantArray.size(); i ++) {
    //            JSONObject tenant = tenantArray.getJSONObject(i);
    //            String schema = tenant.getString("SCHEMA_ID");
    //            log.info(logPrefix + schema);
    //            //TODO : 임시로 public schema만 수행하도록 함 - 타 schema엔 관련 테이블이 없음.
    //            if("public".equals(schema)) {
    //                TenantContext.setCurrentTenant(tenant.getString("SCHEMA_ID"));
    //                //이메일 수집
    ////                cronChatEmailService.collectChatCuttEmail();
    //            }
    //
    //
    //        }
    //        long endTime = System.currentTimeMillis();
    //        double runTime = (endTime - startTime) / 1000.0;
    //        log.info(logPrefix + "채팅_상담_이메일 수집 End :::  " + endTime);
    //        log.info(logPrefix + "채팅_상담_이메일 수집 실행 시간 :::  " + String.format("%,.3f", runTime) + "초(Sec)");
    //    }


    /**
     * 수집_작업_관리 - 채팅_채널(이메일, 게시판) 수집 스케줄러
     * 매 분 스케줄러가 돌면서 다음 작업 시간이 지나고 대기 상태인 수집 작업을 수행한다.
     * @throws Exception
     */
    @Scheduled(cron ="0 * * * * ?")
    public void clctJobMngChatChannel() throws Exception {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //all-0 또는 chat-0 pod이고, application-chat.yml의 chat:router-enabled: true 일때 사용.
        if ((podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("chat-0") > -1) && chatProperties.isRouterEnabled()) {
        //if ((podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("chat-0") > -1) ) {
            //if (false) {

            String logPrefix = logDevider + "___clctJobMngChatChannel___" + podName + "___ ::: ";
            //        log.info(logPrefix + "chatProperties.isRouterEnabled() : " + chatProperties.isRouterEnabled() + ", isRun : " + !((podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("chat-0") > -1) && chatProperties.isRouterEnabled()));


            long startTime = System.currentTimeMillis();
            log.info(logPrefix + "수집_작업_관리 - 채팅_채널(이메일, 게시판) 수집 스케줄러 Start ::: " + startTime);
            JSONArray tenantArray = selectBatchTenantArray();
            for(int i = 0; i < tenantArray.size(); i++) {
                JSONObject tenant = tenantArray.getJSONObject(i);
                String schema = tenant.getString("SCHEMA_ID");
                String certCustcoId = tenant.getString("CERT_CUSTCO_ID");
                log.info(logPrefix + "schema ::: " + schema);

                //테넌트 설정
                TenantContext.setCurrentTenant(tenant.getString("SCHEMA_ID"));

                //수집_작업_관리 목록 조회(채팅 채널 - 이메일, 게시판에 한함)
                TelewebJSON jobMngParmas = new TelewebJSON();
                jobMngParmas.setString("CLCT_JOB", "CHT_CHN");
                jobMngParmas.setString("STTS_CD", "WAIT");
                TelewebJSON jobMngRetObj = collectJobManageService.selectCollectJobManageList(jobMngParmas);
                int cnt = jobMngRetObj.getHeaderInt("COUNT");
                log.info(logPrefix + tenant.getString("SCHEMA_ID") + " ::: JOB_MNG COUNT ::: " + cnt);

                if(cnt > 0) {
                    JSONArray jobMngArray = jobMngRetObj.getDataObject();
                    for(int j = 0; j < jobMngArray.size(); j++) {

                        JSONArray dataArray = new JSONArray();
                        dataArray.add(jobMngArray.getJSONObject(j));
                        TelewebJSON jobMng = new TelewebJSON();
                        jobMng.setDataObject(dataArray);
                        TelewebJSON custcoChnParmas = new TelewebJSON();
                        custcoChnParmas.setString("CERT_CUSTCO_ID", certCustcoId);
                        custcoChnParmas.setString("SNDR_KEY", jobMng.getString("SNDR_KEY"));
                        custcoChnParmas.setString("PP_KEY_PP", P_KEY);
                        custcoChnParmas.setString("PP_ALG_PP", P_ALG);


                        log.info(logPrefix + tenant.getString("SCHEMA_ID") + " ::: custcoChnParmas ::: " + custcoChnParmas.toString());
                        log.info(logPrefix + tenant.getString("SCHEMA_ID") + " ::: jobMng ::: " + jobMng.toString());

                        long startJobTime = System.currentTimeMillis();

                        //고객사_채널_설정(게시판 또는 이메일) 정보
                        TelewebJSON custcoChnRetObj = null;
                        //수집 결과 Obj
                        TelewebJSON collectRetObj = null;
                        if("CHT_CHN_BATCH".equals(jobMng.getString("CLCT_JOB_CD"))) {
                            custcoChnRetObj = cronChatBoardService.selectCustcoChannelBbsSettingList(custcoChnParmas);
                            if("Y".equals(custcoChnRetObj.getString("SRVC_MAINT_YN"))) {
                                //현재시간이 이후_작업_시작_일시보다 큰 경우 작업 시작 한다.
                                String currentDateTime = paletteUtils.getShortString(); //toDate에 사용예정.
                                if( Long.parseLong(jobMng.getString("AFTR_JOB_BGNG_DT")) <= Long.parseLong( currentDateTime ) ) {

                                    //수집_작업_관리 상태 변경 - 상태코드: 실행중, 작업_시작_시간, 다음 시작 시간,
                                    jobMng.setString("AFTR_JOB_BGNG_DT", collectJobManageService.selectNextJobStartDateTime(custcoChnRetObj.getString("CLCT_RPTT"), Calendar.MINUTE));
                                    collectJobManageService.updateCollectJobManageRun(jobMng);

                                    custcoChnRetObj.setString("LAST_SRCH_DT",jobMng.getString("LAST_SRCH_DT")); // fromDate에 사용예정
                                    custcoChnRetObj.setString("currentDateTime", currentDateTime);

                                	collectRetObj = cronChatBatchService.getSwRsvt(custcoChnRetObj);
                                    if ( !collectRetObj.getHeaderBoolean("ERROR_FLAG")) {

                                        // 수집_작업_관리 상태 변경 - 상태코드: 실행중, 작업_시작_시간, 다음 시작 시간,
                                        // BBS에서는 최종_검색_일시를 fromDate에 사용예정으로 현재시간을 세팅한다.
                                        Calendar fromCal = Calendar.getInstance();
                                        SimpleDateFormat fromFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                                        Date fromDate = fromFmt.parse( currentDateTime );
                                        fromCal.setTime( fromDate );
                                        fromCal.add(Calendar.HOUR, -2);  //2시간전
                                        jobMng.setString("LAST_SRCH_DT", fromFmt.format(fromCal.getTime()) );
                                        collectJobManageService.updateCollectJobManageRunLastSrchDt(jobMng);
                                    }
                                }
                            }

                        } else {
	                        if("CHT_CHN_BBS".equals(jobMng.getString("CLCT_JOB_CD"))) {
	                            custcoChnRetObj = cronChatBoardService.selectCustcoChannelBbsSettingList(custcoChnParmas);
	
	                            log.info(logPrefix + tenant.getString("SCHEMA_ID") + " ::: custcoChnRetObj ::: " + custcoChnRetObj.toString());
	
	                            if("Y".equals(custcoChnRetObj.getString("SRVC_MAINT_YN"))) {
	                                //게시판 수집
	                                //현재시간이 이후_작업_시작_일시보다 큰 경우 작업 시작 한다.
	                                String currentDateTime = paletteUtils.getShortString(); //toDate에 사용예정.
	                                if( Long.parseLong(jobMng.getString("AFTR_JOB_BGNG_DT")) <= Long.parseLong( currentDateTime ) ) {
	
	                                    //수집_작업_관리 상태 변경 - 상태코드: 실행중, 작업_시작_시간, 다음 시작 시간,
	                                    jobMng.setString("AFTR_JOB_BGNG_DT", collectJobManageService.selectNextJobStartDateTime(custcoChnRetObj.getString("CLCT_RPTT"), Calendar.MINUTE));
	                                    collectJobManageService.updateCollectJobManageRun(jobMng);
	
	                                    custcoChnRetObj.setString("LAST_SRCH_DT",jobMng.getString("LAST_SRCH_DT")); // fromDate에 사용예정
	                                    custcoChnRetObj.setString("currentDateTime", currentDateTime);
	
	                                    collectRetObj = cronChatBoardService.collectChatCuttBbs(custcoChnRetObj);
	                                    if ( !collectRetObj.getHeaderBoolean("ERROR_FLAG")) {
	
	                                        // 수집_작업_관리 상태 변경 - 상태코드: 실행중, 작업_시작_시간, 다음 시작 시간,
	                                        // BBS에서는 최종_검색_일시를 fromDate에 사용예정으로 현재시간을 세팅한다.
	                                        Calendar fromCal = Calendar.getInstance();
	                                        SimpleDateFormat fromFmt = new SimpleDateFormat("yyyyMMddHHmmss");
	                                        Date fromDate = fromFmt.parse( currentDateTime );
	                                        fromCal.setTime( fromDate );
	                                        fromCal.add(Calendar.HOUR, -2);  //2시간전
	                                        jobMng.setString("LAST_SRCH_DT", fromFmt.format(fromCal.getTime()) );
	                                        collectJobManageService.updateCollectJobManageRunLastSrchDt(jobMng);
	                                    }
	                                }
	
	                            } else {
	                                collectRetObj = new TelewebJSON();
	                                collectRetObj.setString("JOB_SCS_YN", "Y");
	                                collectRetObj.setString("JOB_RSLT_MSG", "미수집 - 채널 사용안함 처리됨");
	                            }
	                        } else if("CHT_CHN_EMAIL".equals(jobMng.getString("CLCT_JOB_CD"))) {
	                            //고객사_채널_이메일_설정 정보 조회
	                            custcoChnRetObj = cronChatEmailService.selectCustcoChannelEmailSettingList(custcoChnParmas);
	
	                            //수집_작업_관리 상태 변경 - 상태코드: 실행중, 작업_시작_시간, 다음 시작 시간,
	                            jobMng.setString("AFTR_JOB_BGNG_DT", collectJobManageService.selectNextJobStartDateTime(custcoChnRetObj.getString("CLCT_RPTT"), Calendar.MINUTE));
	                            collectJobManageService.updateCollectJobManageRun(jobMng);
	                            if("Y".equals(custcoChnRetObj.getString("SRVC_MAINT_YN"))) {
	                                //이메일 수집
	                                collectRetObj = cronChatEmailService.collectChatCuttEmail(custcoChnRetObj);
	                            } else {
	                                collectRetObj = new TelewebJSON();
	                                collectRetObj.setString("JOB_SCS_YN", "Y");
	                                collectRetObj.setString("JOB_RSLT_MSG", "미수집 - 채널 사용안함 처리됨");
	                            }
	
	                        }
	                    }
                    	
                        long endJobTime = System.currentTimeMillis();
                        float jobHr = (float)((endJobTime - startJobTime) / 1000.0);
                        //수집_작업_관리 상태 변경 - 대기
                        collectJobManageService.updateCollectJobManageWait(jobMng, jobHr, custcoChnRetObj.getString("SRVC_MAINT_YN"), collectRetObj);
                        // 수집_작업_이력 저장
                        collectJobManageService.insertCollectJobHistory(jobMng, collectRetObj);
                    }
                }

            }
            long endTime = System.currentTimeMillis();
            double runTime = (endTime - startTime) / 1000.0;
            log.info(logPrefix + "수집_작업_관리 - 채팅_채널(이메일, 게시판) 수집 스케줄러 End :::  " + endTime);
            log.info(logPrefix + "수집_작업_관리 - 채팅_채널(이메일, 게시판) 수집 스케줄러 실행 시간 :::  " + String.format("%,.3f", runTime) + "초(Sec)");
        }
    }

    /**
     * 배치 테넌트 목록 조회
     * @return
     */
    private JSONArray selectBatchTenantArray() {
        JSONArray tenantArray = new JSONArray();
        TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT_CODE);
        TelewebJSON tenantObj = busyService.selectBatchTenantList();
        if(tenantObj.getHeaderInt("COUNT") > 0) {
            tenantArray = tenantObj.getDataObject();
        }
        return tenantArray;
    }
}
