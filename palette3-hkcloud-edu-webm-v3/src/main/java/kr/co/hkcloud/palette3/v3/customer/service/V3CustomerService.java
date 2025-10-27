package kr.co.hkcloud.palette3.v3.customer.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * << 여기에 설명. >>
 *
 * @author KJD
 * @version 1.0
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-01        KJD       최초 생성
 * </pre>
 * @since 2023-12-01
 */
public interface V3CustomerService {
    TelewebJSON selectCustcoExpsnInfo(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCustomer(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCustomerList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectCuttHistoryList(TelewebJSON jsonParams) throws TelewebAppException;

}
