package kr.co.hkcloud.palette3.phone.outbound.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONArray;


public interface PhoneOutboundRegistService
{
    /**
     * 아웃바운드 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertObndReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 key를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndRegKey(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 고객 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertObndCustReg(JSONArray jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드고객 추가정보 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertObndCustRegAdd(JSONArray jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드캠페인고객 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertObndCamCustReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 삭제한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteObndReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 고객을 추출 한다. (현재사용 X)
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndCustList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 중복검색.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndCheck(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 고객을 중복검색.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndCustCheck(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 정보를 수정 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateObndReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 공제 파일 업로드 및 고객등록 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertExcelObndReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 업무 파일 업로드 및 고객등록 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertExcelObndRegE(TelewebJSON jsonParams) throws TelewebAppException;

}
