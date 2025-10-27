package kr.co.hkcloud.palette3.core.chat.transfer.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Service("sendTestService")
public class SendTestServiceImpl implements SendTestService
{
    @Autowired
    private final TwbComDAO mobjDao;


    @Override
    public TelewebJSON selectMessage(String custcoId, String sysMsgId, int userId)
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("SYS_MSG_ID", sysMsgId);
        jsonParams.setInt("USER_ID", userId);
        return mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.SendTestMapper", "selectMessage", jsonParams);
    }
}
