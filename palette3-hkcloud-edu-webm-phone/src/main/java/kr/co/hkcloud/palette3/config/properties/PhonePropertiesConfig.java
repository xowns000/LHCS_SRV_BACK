package kr.co.hkcloud.palette3.config.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 전화 Properties를 Bean으로 등록함
 * 
 * @author leeiy
 *
 */
@Configuration
@EnableConfigurationProperties(value = {kr.co.hkcloud.palette3.config.properties.phone.PhoneProperties.class})
public class PhonePropertiesConfig
{

}
