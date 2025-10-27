package kr.co.hkcloud.palette3.phone.sms.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneAtalkMessagelManageService")
public class PhoneAtalkMessageManageServiceImpl implements PhoneAtalkMessageManageService
{
    private final TwbComDAO            twbComDao;
    private final SendSmsService sendSmsService;
    
    /**
     * 알림톡 템플릿 목록 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON atalkTmpls(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select(
    			"kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkMessageManageMapper"
    			, "atalkTmpls"
    			, mjsonParams);
    }
    
    /**
     * SMS 발송 이력 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON atalkSendHistory(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkMessageManageMapper"
    			, "atalkSendHistory"
    			, mjsonParams);
    }
}
