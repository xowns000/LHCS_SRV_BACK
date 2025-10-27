package kr.co.hkcloud.palette3.cron.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.cron.enumer.CollectJobManageSttsCd;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("collectJobManageService")
public class CollectJobManageServiceImpl implements CollectJobManageService {
    private final PaletteUtils paletteCmmnUtils;
    private final TwbComDAO mobjDao;
    private String namespace = "kr.co.hkcloud.palette3.cron.dao.CollectJobManageMapper";
    private String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";
    
    
    /**
     * 수집_작업_관리 목록 조회
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCollectJobManageList(TelewebJSON paramJson) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCollectJobManageList", paramJson);
    }
    
    
    /**
     * 수집_작업_관리 저장
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCollectJobManage(TelewebJSON paramJson) throws TelewebAppException {
        return mobjDao.insert(namespace, "insertCollectJobManage", paramJson);
    }
    
    /**
     * 수집_작업_관리 수정
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateCollectJobManage(TelewebJSON paramJson) throws TelewebAppException {
        return mobjDao.update(namespace, "updateCollectJobManage", paramJson);
    }
    
    /**
     * 수집_작업_관리 실행 상태로 변경
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateCollectJobManageRun(TelewebJSON paramJson) throws TelewebAppException {
//        String logPrefix = logDevider + "___updateCollectJobManageRun___" + paramJson.getString("SNDER_KEY") + "___ ::: ";
        TelewebJSON jobMngRunParmas = new TelewebJSON();
        jobMngRunParmas.setString("JOB_MNG_ID", paramJson.getString("JOB_MNG_ID"));
        jobMngRunParmas.setString("STTS_CD", CollectJobManageSttsCd.RUN.toString());
        jobMngRunParmas.setString("JOB_BGNG_DT", paletteCmmnUtils.getShortString());
        jobMngRunParmas.setString("AFTR_JOB_BGNG_DT", paramJson.getString("AFTR_JOB_BGNG_DT"));
        
        return updateCollectJobManage(jobMngRunParmas);
    }

    @Override
    public TelewebJSON updateCollectJobManageRunLastSrchDt(TelewebJSON paramJson) throws TelewebAppException {
        TelewebJSON jobMngRunParmas = new TelewebJSON();
        jobMngRunParmas.setString("JOB_MNG_ID", paramJson.getString("JOB_MNG_ID"));
        jobMngRunParmas.setString("LAST_SRCH_DT", paramJson.getString("LAST_SRCH_DT"));
        return updateCollectJobManage(jobMngRunParmas);
    }

    /**
     * 수집_작업_관리 대기 상태로 변경
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateCollectJobManageWait(TelewebJSON jobMngParams, float jobHr, String waitYn, TelewebJSON collectRetObj) throws TelewebAppException {
        
        TelewebJSON jobMngWaitParmas = new TelewebJSON();
        jobMngWaitParmas.setString("JOB_MNG_ID", jobMngParams.getString("JOB_MNG_ID"));
        //대기 또는 종료
        jobMngWaitParmas.setString("STTS_CD", "Y".equals(waitYn) ? CollectJobManageSttsCd.WAIT.toString() : CollectJobManageSttsCd.STOP.toString());
        jobMngWaitParmas.setString("JOB_END_DT", paletteCmmnUtils.getShortString());
        jobMngWaitParmas.setString("JOB_HR", String.valueOf(jobHr));
        //실패 시 , 실패 여부 설정.
        if("N".equals(collectRetObj.getString("JOB_SCS_YN"))) {
            jobMngWaitParmas.setString("FAIL_CNT_YN", "Y");
        }
        
        return updateCollectJobManage(jobMngWaitParmas);
    }
    
    public TelewebJSON insertCollectJobHistory(TelewebJSON jobMngObj, TelewebJSON collectRetObj) throws TelewebAppException {
        String logPrefix = logDevider + "___insertCollectJobHistory___" + jobMngObj.getString("SNDER_KEY") + "___ ::: ";
        log.info(logPrefix + "jobMngObj ::: " + jobMngObj.toString());
        log.info(logPrefix + "collectRetObj ::: " + collectRetObj.toString());
        
        TelewebJSON jobMngParams = new TelewebJSON();
        jobMngParams.setString("JOB_MNG_ID", jobMngObj.getString("JOB_MNG_ID"));
        TelewebJSON jobMng = selectCollectJobManageList(jobMngParams);
        log.info(logPrefix + "jobMng ::: " + jobMng.toString());
        jobMng.setString("JOB_SCS_YN", collectRetObj.getString("JOB_SCS_YN"));
        jobMng.setString("JOB_RSLT_MSG", collectRetObj.getString("JOB_RSLT_MSG"));
        return mobjDao.insert(namespace, "insertCollectJobHistory", jobMng);
    }
    
    /**
     * 수집_작업_관리 - 이후_작업_시작_일시 계산
     * @return
     * @throws TelewebAppException
     */
    public String selectNextJobStartDateTime(String collectRepetition, int unit) throws TelewebAppException {
        String aftrJobBgngDt = null;
        
        Date date = new Date();
        
        // 기본 포맷 ( 년월일 시분초)
        String format = "yyyyMMddHHmmss";
        String addTime = "";
        if(unit == Calendar.MINUTE) {
            // 분단위일 시, 포맷 변경 ( 년월일 시분) - 마지막에 00초 추가함.
            format = "yyyyMMddHHmm";
            addTime = "00";
        } else if(unit == Calendar.HOUR) {
            // 시간 단위일 시, 포맷 변경 ( 년월일 시) - 마지막에 00분00초 추가함.
            format = "yyyyMMddHH";
            addTime = "0000";
        }
        
        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        // Java 시간 더하기
        Calendar cal = Calendar.getInstance();
         
        //지금
        cal.setTime(date);
        // collectRepetition unit(분, 시간) 더하기 - unit : Calendar.MINUTE, Calendar.HOUR
        cal.add(unit, Integer.parseInt(collectRepetition));
        //yyyyMMddHHmm 에 초 추가
        aftrJobBgngDt = sdformat.format(cal.getTime()) + addTime;
        
        return aftrJobBgngDt;
    }
}
