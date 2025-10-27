package kr.co.hkcloud.palette3.setting.customer.app;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service("SettingCautionCustomerListService")
public class SettingCautionCustomerListServiceImpl  implements SettingCautionCustomerListService {

    private final TwbComDAO mobjDao;

    public TelewebJSON selectCautionCustList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCautionCustomerListMapper", "selectCautionCustList",
            jsonParams);
    }
}
