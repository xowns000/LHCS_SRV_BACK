package kr.co.hkcloud.palette3.phone.cutt.app;

import java.text.ParseException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CuttService {

    /**
     * 
     * 상담 내용 저장
     * 
     * @Method Name : cuttProc
     * @date : 2023. 6. 27.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     * @throws ParseException 
     */
    TelewebJSON cuttProc(TelewebJSON jsonParams) throws TelewebAppException, ParseException;

    /**
     * 
     * 상담 변경 이력 ID UPDATE
     * 
     * @Method Name : cuttChgHistIdUpdate
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttChgHistIdUpdate(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 상담 이력 목록
     * 
     * @Method Name : integCuttHistList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON integCuttHistList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 VOC 목록
     * 
     * @Method Name : vocList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON vocList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 문자발송 이력 목록
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON msgList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 예약 콜 목록
     * 
     * @Method Name : rsvtCallList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON rsvtCallList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 콜백 목록
     * 
     * @Method Name : callBackList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON callBackList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 캠페인 목록
     * 
     * @Method Name : cpiList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cpiList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * My 데스크 콜백, 예약콜, 캠페인 진행 현황
     * 
     * @Method Name : myDeskStat
     * @date : 2023. 7. 28.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON myDeskStat(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 상담원별 예약콜 처리 상태
     * 
     * @Method Name : cuslRsvtCallMonitor
     * @date : 2023. 10. 31.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslRsvtCallMonitor(TelewebJSON jsonParams) throws TelewebAppException;

    /* 예약콜이 있을 경우 상담직원이 인지할 수 있도록 우측 사이드바의 해당 아이콘에 에니메이션 효과를 적용 용도 */
    TelewebJSON rsvtCallNoCompletedCnt(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 통합 상담 이력 노출 확장속성 목록
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslExpsrExpsnAttrList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 이첩 처리 프로세스
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
	TelewebJSON trnsfCuttProc(TelewebJSON jsonParams) throws TelewebAppException;
}
