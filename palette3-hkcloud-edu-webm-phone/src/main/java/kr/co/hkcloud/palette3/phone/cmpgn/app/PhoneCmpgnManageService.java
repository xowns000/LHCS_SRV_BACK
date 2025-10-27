package kr.co.hkcloud.palette3.phone.cmpgn.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCmpgnManageService
{

    /**
     * ASIS 목록 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 상담원 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn2(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인데이터 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn3(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 고객데이터 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn4(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 불량데이터 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtn5(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인콤보 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnCombo01(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 파일템플릿콤보 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnCombo02(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 등록
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * CTC 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnCTC(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인시퀀스 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectRtnGetSEQ(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtnOBJLIST(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtnOBJDTL(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtnCmpData(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON upDataRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertRtnErrorData(TelewebJSON mjsonParams) throws TelewebAppException;
}
