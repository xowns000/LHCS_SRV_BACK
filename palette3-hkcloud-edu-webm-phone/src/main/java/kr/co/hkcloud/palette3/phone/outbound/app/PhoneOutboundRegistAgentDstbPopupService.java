package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneOutboundRegistAgentDstbPopupService
{
    /**
     * 상담원 배분 팝업 에서 아웃바운드 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCsltDivObndList(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     * 상담원 배분 팝업 에서 아웃바운드 상세조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCsltDivObndDetail(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     * 아웃바운드 상담원 배분정보 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCsltDivInfo(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     * 상담원배분_아웃바운드 고객 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndDivCustList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 배분 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndDivList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 배분 수정 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 배분 등록 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertObndDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드고객 배분여부 수정 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndDivYn(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 상담원배분미완료 조회 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCsltDivNotEnd(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 배분삭제 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteObndDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 고객을가지고있는 사원조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndHavingCust(TelewebJSON jsonParams) throws TelewebAppException;

}
