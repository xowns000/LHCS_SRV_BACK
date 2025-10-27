package kr.co.hkcloud.palette3.phone.center.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : 알림톡템플릿관리
 * 파 일 명 : PhoneCenterNtcnTalkTmplatManageService.java
 * 작 성 자 : 현은지
 * 작 성 일 : 2021.04.29
 * 설    명 : 알림톡템플릿관리 서비스 인퍼페이스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
public interface PhoneCenterNtcnTalkTmplatManageService
{
    /**
     * 알림톡템플릿관리 목록을 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectNtcnTalkMngList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 알림톡템플릿관리 정보를 등록한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 알림톡템플릿관리 정보를 수정한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 알림톡템플릿관리 정보를 삭제한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 알림톡템플릿관리 건수를 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectNtcnTalkMngCnt(TelewebJSON mjsonParams) throws TelewebAppException;

}
