package kr.co.hkcloud.palette3.cron.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CollectJobManageService {
    /**
     * 수집_작업_관리 목록 조회
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectCollectJobManageList(TelewebJSON paramJson) throws TelewebAppException;
    
    
    /**
     * 수집_작업_관리 저장
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertCollectJobManage(TelewebJSON paramJson) throws TelewebAppException;
    
    /**
     * 수집_작업_관리 수정
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateCollectJobManage(TelewebJSON paramJson) throws TelewebAppException;
    
    
    /**
     * 수집_작업_관리 실행 상태로 변경
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateCollectJobManageRun(TelewebJSON paramJson) throws TelewebAppException;
    public TelewebJSON updateCollectJobManageRunLastSrchDt(TelewebJSON paramJson) throws TelewebAppException;

    
    /**
     * 수집_작업_관리 대기 상태로 변경
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateCollectJobManageWait(TelewebJSON jobMngParams, float jobHr, String waitYn, TelewebJSON collectRetObj) throws TelewebAppException;
    
    
    /**
     * 수집_작업_이력 저장
     * @param jobMng
     * @param collectRetObj
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertCollectJobHistory(TelewebJSON jobMngParams, TelewebJSON collectRetParams) throws TelewebAppException;
    
    /**
     * 수집_작업_관리 - 이후_작업_시작_일시 계산
     * @return
     * @throws TelewebAppException
     */
    public String selectNextJobStartDateTime(String collectRepetition, int unit) throws TelewebAppException;
}
