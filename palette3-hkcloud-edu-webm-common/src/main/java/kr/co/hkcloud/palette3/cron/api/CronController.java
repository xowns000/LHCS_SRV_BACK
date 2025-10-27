package kr.co.hkcloud.palette3.cron.api;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.cron.app.CronService;
import kr.co.hkcloud.palette3.email.app.EmailService;
import kr.co.hkcloud.palette3.mts.app.MtsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * Description : cron 작업 스케쥴러
 * package : kr.co.hkcloud.palette3.cron.api
 * filename : CronController.java
 * Date : 2023. 8. 16.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 8. 16., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@Component
@RestController
@RequiredArgsConstructor
public class CronController {

    private final CronService cronService;
    private final EmailService emailService;
    private final MtsService mtsService;

    @NotBlank
    @Value("${k8s.pod.name}")
    private String podName;

    //첫번째 부터 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    @Scheduled(cron = "0 0 1 * * *")
    public void executeItgrtStatisticsRowData() {

        if (podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("api-0") > -1) {
            //			TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
            log.info("[" + podName + "]-------------------------------------------executeItgrtStatisticsRowData Start... ");
            TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT_CODE);
            cronService.executeItgrtStatisticsRowData();
        }

    }

    //첫번째 부터 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    @Scheduled(cron = "0 5 1 * * *")
    public void executeItgrtChatStatisticsRowData() {
        if (podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("api-0") > -1) {
            //			TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
            log.info("[" + podName + "]-------------------------------------------executeItgrtChatStatisticsRowData Start... ");
            TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT_CODE);
            cronService.executeItgrtChatStatisticsRowData();
        }

    }

    // 애플리케이션 시작 후 10초 후에 첫 실행, 그 후 매 60초마다 주기적으로 실행한다.
    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void otherJob() {
        log.info("-------------------------------------------[" + podName + "]-------------------------------------------");
        if (podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("api-0") > -1) {
            log.info("[" + podName + "]-------------------------------------------otherJob Start... ");
            log.info("[" + podName + "]-------------------------------------------설문, QA, 캠페인 상태 변경 START... ");
            //			TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
            TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT_CODE);
            cronService.executePlanStatChange();
            log.info("[" + podName + "]-------------------------------------------설문, QA, 캠페인 상태 변경 END... ");
        }
    }



    // 애플리케이션 시작 후 10초 후에 첫 실행, 그 후 매 60초마다 주기적으로 실행한다.
    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void sendReservationEmail() {
        log.info("-------------------------------------------[" + podName + "]-------------------------------------------");
        if (podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("api-0") > -1) {
            log.info("[" + podName + "]------------------------------------------- 이메일 예약 발송 스케줄러 Start... ");

            JSONArray tenantArray = selectBatchTenantArray();
            for(int i = 0; i < tenantArray.size(); i++) {
                JSONObject tenant = tenantArray.getJSONObject(i);
                String schema = tenant.getString("SCHEMA_ID");

                //테넌트 설정
                TenantContext.setCurrentTenant(schema);

                emailService.sendReservationEmail();
            }

            log.info("[" + podName + "]-------------------------------------------이메일 예약 발송 스케줄러 End... ");
        }
    }
    
    
    //MTS 발송 결과 업데이트
    @Scheduled(cron = "0 0 3 * * *")
    @PostMapping("/api/crontest/updateMtsSendingResult")
    public void updateMtsSendingResult() {

    	if (podName.lastIndexOf("all-0") > -1 || podName.lastIndexOf("api-0") > -1) {
            log.info("[" + podName + "]------------------------------------------- MTS 발송 결과 업데이트 스케줄러 Start... ");

            JSONArray tenantArray = selectBatchTenantArray();
            for(int i = 0; i < tenantArray.size(); i++) {
                JSONObject tenant = tenantArray.getJSONObject(i);
                String schema = tenant.getString("SCHEMA_ID");

                //테넌트 설정
                TenantContext.setCurrentTenant(schema);

                mtsService.cronMtsSendingResult();
            }

            log.info("[" + podName + "]-------------------------------------------MTS 발송 결과 업데이트 스케줄러 End... ");
        }

    }


    /**
     * 배치 테넌트 목록 조회
     * @return
     */
    private JSONArray selectBatchTenantArray() {
        JSONArray tenantArray = new JSONArray();
        TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT_CODE);
        TelewebJSON tenantObj = cronService.selectBatchTenantList();
        if(tenantObj.getHeaderInt("COUNT") > 0) {
            tenantArray = tenantObj.getDataObject();
        }
        return tenantArray;
    }
}