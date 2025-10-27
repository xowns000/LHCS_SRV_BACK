package kr.co.hkcloud.palette3.phone.sms.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service("phoneSmsListService")
public class PhoneSmsListServiceImpl implements PhoneSmsListService
{
    private final TwbComDAO            twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectSmsTmplList(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsListMapper", "selectSmsTmplList", mjsonParams);
//    }
    
    /**
     * SMS 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectSmsSendHistory(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//    	objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsListMapper", "selectSmsSendHistory", mjsonParams);
//    	return this.sendHistoryJSonToList(objRetParams);
//    }
//    
//    private TelewebJSON sendHistoryJSonToList(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	JSONArray jsonObj = mjsonParams.getDataObject("DATA");
//    	
//    	return new TelewebJSON();
//    }
    
    /**
     * SMS 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectMainSmsList(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsListMapper", "selectMainSmsList", mjsonParams);
//
//        return objRetParams;
//    }

}
