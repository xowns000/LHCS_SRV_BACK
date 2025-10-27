package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnProgrsSttusService
{

    /**
     * 캠페인 기본 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnComboBox(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 현황조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnStep01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 담당자별현황리스트
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnStep02(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 데이타 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnData(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 미할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnUser(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 할당 상담원 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnAllc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 프로젝트별 데이타 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnPrjNo(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 대상_리스트 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 할당 데이터 저장 처리
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 데이터 삭제 처리
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException;
}
