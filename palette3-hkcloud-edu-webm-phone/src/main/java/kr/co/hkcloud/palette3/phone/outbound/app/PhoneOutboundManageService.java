package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundManageService
{
    /**
     * 아웃바운드 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 완전판매 아웃바운드조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndEndFcntList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 진행조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndPrceList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 상세조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndDtail(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 배분정보 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndDivInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드마감 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndEnd(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 정보 수정한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 사용여부 수정한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndUseYn(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드(캠페인고객)삭제한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteObndCust(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담사별 아웃바운드를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectUserObndList(TelewebJSON jsonParams) throws TelewebAppException;
}
