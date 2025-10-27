package kr.co.hkcloud.palette3.config.multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.hkcloud.palette3.config.multitenancy.interceptors.TenantInterceptor;
import lombok.RequiredArgsConstructor;

/**
 * 테넌트 관리를 위한 interceptor 설정
 * 
 * @author 황종혁
 *
 */
@Configuration
@RequiredArgsConstructor
public class TenantWebMvcConfig  implements WebMvcConfigurer
{
	@Autowired
	private final TenantInterceptor tenantInterceptor;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(tenantInterceptor);
//        registry.addInterceptor(tenantInterceptor()).addPathPatterns("/**"); 
    }
    
//    @Bean
//    public TenantInterceptor tenantInterceptor() {
//        return new TenantInterceptor();
//    }
}
