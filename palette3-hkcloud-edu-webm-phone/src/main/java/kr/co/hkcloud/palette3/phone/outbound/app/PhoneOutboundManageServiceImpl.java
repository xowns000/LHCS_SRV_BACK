package kr.co.hkcloud.palette3.phone.outbound.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundManageService")
public class PhoneOutboundManageServiceImpl implements PhoneOutboundManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectObndList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 완전판매 아웃바운드조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndEndFcntList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectObndEndFcntList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 진행조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndPrceList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectObndPrceList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 상세조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDtail(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectObndDtail", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 배분정보
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDivInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectObndDivInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드마감
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndEnd(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "updateObndEnd", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 정보 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "updateObndInfo", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 사용여부 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndUseYn(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "updateObndUseYn", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드(캠페인고객)삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteObndCust(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "updateObndUseYn", jsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "deleteObndCust", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 상담사별 아웃바운드 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectUserObndList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundManageMapper", "selectUserObndList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
