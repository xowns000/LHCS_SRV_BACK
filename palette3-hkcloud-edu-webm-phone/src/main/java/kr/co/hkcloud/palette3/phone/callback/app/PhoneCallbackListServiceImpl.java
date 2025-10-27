package kr.co.hkcloud.palette3.phone.callback.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneCallbackListService")
public class PhoneCallbackListServiceImpl implements PhoneCallbackListService
{
    private final TwbComDAO mobjDao;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackListMapper";

    /**
     * 콜백목록 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkInqList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackListMapper", "selectClbkInqList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    @Transactional(readOnly = false)
    public TelewebJSON updateClbkInq(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 처리결과 등록.
        objRetParams = mobjDao
            .update("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackListMapper", "updateClbkInqDiv", jsonParams);

        return objRetParams;
    }

    /**
     * 콜백 이력 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON clbkStatHistList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "clbkStatHistList", jsonParams);
    }

    /**
     * 상담원별 콜백 상태
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslClbkMonitor(TelewebJSON jsonParams) throws TelewebAppException
    {
    	return mobjDao.select(sqlNameSpace, "cuslClbkMonitor", jsonParams);
    }

    /**
     * 상담원별 처리결과별 상태
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslClbkDtlMonitor(TelewebJSON jsonParams) throws TelewebAppException
    {
    	return mobjDao.select(sqlNameSpace, "cuslClbkDtlMonitor", jsonParams);
    }
}
