package kr.co.hkcloud.palette3.infra.chat.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.chat.main.app.ChatMainService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("TwbChatbotContactHistoryService")
public class TeletalkChatbotContactHistoryServiceImpl implements TeletalkChatbotContactHistoryService
{
    private final TwbComDAO                             mobjDao;
    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final ChatMainService                       chatMainService;


    /**
     * 상담이력 저장 서비스
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON insertChatbotContact(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertChatbotContact", jsonParams);
    }


    /**
     * 상담이력상세 저장 서비스
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON insertChatbotContactDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertChatbotContactDetail", jsonParams);
    }


    /**
     * 고객정보 및 상담정보 조회 서비스
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectCustInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON jsonReturn = new TelewebJSON(jsonParams);

        jsonReturn.setDataObject("CUST_INFO", mobjDao
            .select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper", "selectRtnCustInfoCustomerSeqIsNotNull", jsonParams));
        jsonReturn.setDataObject("CONTACT_INFO", chatMainService.selectRtnTalkHistInfo(jsonParams));

        return jsonReturn;
    }


    /**
     * 고객정보 삽입 서비스
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertCustInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return settingCustomerInformationListService.insertCustInfo(jsonParams);
    }


    /**
     * 상담이력상세 저장 서비스
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON checkLastChatbotBusiness(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "checkLastChatbotBusiness", jsonParams);
    }

}