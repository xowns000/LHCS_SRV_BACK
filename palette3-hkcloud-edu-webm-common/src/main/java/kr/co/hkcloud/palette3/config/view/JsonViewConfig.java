package kr.co.hkcloud.palette3.config.view;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


/**
 * 
 * @author jangh
 *
 */
@Configuration
public class JsonViewConfig
{
    /**
     * 
     * @return
     */
    @Bean
    public MappingJackson2JsonView jsonView()
    {
        return new MappingJackson2JsonView();
    }
}
