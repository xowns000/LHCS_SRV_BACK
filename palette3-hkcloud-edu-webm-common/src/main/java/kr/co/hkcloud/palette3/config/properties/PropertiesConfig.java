package kr.co.hkcloud.palette3.config.properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Properties를 Bean으로 등록함
 * 
 * @author leeiy
 *
 */
@Configuration
// @formatter:off
@EnableConfigurationProperties(value = {kr.co.hkcloud.palette3.config.properties.alimtalk.AlimtalkProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.batch.BatchProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.bbs.BbsProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.datasources.DatasourcesProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.editor.EditorProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.excel.ExcelProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.file.FileProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties.class
                                      , kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties.class
                                      , kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties.class}) 
// @formatter:on
public class PropertiesConfig
{

}
