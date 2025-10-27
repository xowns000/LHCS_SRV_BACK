package kr.co.hkcloud.palette3.config.logging;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import lombok.extern.slf4j.Slf4j;


/**
 * 사용
 * 
 * @author Orange
 *
 */
@Slf4j
@Configuration
public class TeletalkRequestLoggingConfig
{
//
//    @Bean
//    public FilterRegistrationBean<CommonsRequestLoggingFilter> requestLoggingFilterRegistration()
//    {
//        FilterRegistrationBean<CommonsRequestLoggingFilter> filterRegistration = new FilterRegistrationBean<CommonsRequestLoggingFilter>(requestLoggingFilter());
//        filterRegistration.addUrlPatterns("/**/web/**");
//        return filterRegistration;
//    }
//
//
//    /**
//     * logging.level.org.springframework.web.filter=debug 일 때 동작 대량의 로그를 생산하므로, 개발용으로만 사용할 것!
//     *
//     * @return
//     */
//    @Bean
//    public CommonsRequestLoggingFilter requestLoggingFilter()
//    {
//        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
//        loggingFilter.setIncludeQueryString(true);
//        loggingFilter.setIncludeClientInfo(true);
//        loggingFilter.setIncludeHeaders(true);
//        loggingFilter.setIncludePayload(true);
//        loggingFilter.setMaxPayloadLength(1000);
//        loggingFilter.setBeforeMessagePrefix("\n" + CommonsRequestLoggingFilter.DEFAULT_BEFORE_MESSAGE_PREFIX);
//        loggingFilter.setBeforeMessageSuffix(CommonsRequestLoggingFilter.DEFAULT_BEFORE_MESSAGE_SUFFIX + "\n");
//        loggingFilter.setAfterMessagePrefix("\n" + CommonsRequestLoggingFilter.DEFAULT_AFTER_MESSAGE_PREFIX);
//        loggingFilter.setAfterMessageSuffix(CommonsRequestLoggingFilter.DEFAULT_AFTER_MESSAGE_SUFFIX + "\n");
//        return loggingFilter;
//    }
}
