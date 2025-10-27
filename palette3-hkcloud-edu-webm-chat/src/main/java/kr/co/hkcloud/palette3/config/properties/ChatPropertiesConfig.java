package kr.co.hkcloud.palette3.config.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 채팅 Properties를 Bean으로 등록함
 * 
 * @author leeiy
 *
 */
@Configuration
// @formatter:off
@EnableConfigurationProperties(value = {kr.co.hkcloud.palette3.config.properties.chat.ChatProperties.class
                                      , kr.co.hkcloud.palette3.config.security.properties.ChatSecurityProperties.class}) 
// @formatter:on
public class ChatPropertiesConfig
{

}
