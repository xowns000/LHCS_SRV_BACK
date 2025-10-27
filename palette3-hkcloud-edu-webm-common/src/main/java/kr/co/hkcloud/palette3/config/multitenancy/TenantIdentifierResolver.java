package kr.co.hkcloud.palette3.config.multitenancy;

import java.util.Optional;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * 테넌트 Resolver
 * 
 * TenantContext 의 테넌트 값을 리턴한다.
 * 
 * @author 황종혁
 */
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		return Optional.ofNullable(TenantContext.getCurrentTenant())
				.orElse(TenantContext.DEFAULT_TENANT_CODE);
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
