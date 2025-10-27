package kr.co.hkcloud.palette3.integration.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.app
 * fileName       : CommerceService
 * author         : KJD
 * date           : 2024-04-09
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-09        KJD       최초 생성
 * </pre>
 */
public interface CommerceLkagService {
    TelewebJSON selectCustcoLkagApi( TelewebJSON jsonParams) throws TelewebAppException;

}
