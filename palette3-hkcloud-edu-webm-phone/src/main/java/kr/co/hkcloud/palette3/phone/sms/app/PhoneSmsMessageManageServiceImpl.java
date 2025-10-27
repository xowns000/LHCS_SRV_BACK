package kr.co.hkcloud.palette3.phone.sms.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("phoneSmsMessageManageService")
public class PhoneSmsMessageManageServiceImpl implements PhoneSmsMessageManageService
{
    private final TwbComDAO            twbComDao;

    
    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsTmplClsfList(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsMessageManageMapper", "selectSmsTmplClsfList", mjsonParams);
    }

    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsTmplList(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON aa = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsMessageManageMapper", "selectSmsTmplList", mjsonParams);
//    	JSONArray jsonArray = aa.getDataObject(TwbCmmnConst.G_DATA);
//    	String tmplCn = jsonArray.getJSONObject(0).getString("SMS_TMPL_CN");
//    	log.debug("tmplCn = " + tmplCn);
//    	log.debug("tmplCn length = " + tmplCn.length());
    	return aa;
    }
    
    /**
     * SMS 발송 이력 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsSendHistory(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsMessageManageMapper", "selectSmsSendHistory", mjsonParams);
    }
}
