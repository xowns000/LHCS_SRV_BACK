package kr.co.hkcloud.palette3.integration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.app
 * fileName       : CommerceApiFactory
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
@Service
@RequiredArgsConstructor
public class CommerceApiServiceFactory {
    private final Map<String, kr.co.hkcloud.palette3.integration.service.CommerceApiService> serviceMap;

    public kr.co.hkcloud.palette3.integration.service.CommerceApiService getCommerceApiService(String commerceApiServiceType) {
        return serviceMap.get(commerceApiServiceType);
    }
}
