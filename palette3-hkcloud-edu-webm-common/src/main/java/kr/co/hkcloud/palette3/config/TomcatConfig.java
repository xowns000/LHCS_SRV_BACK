package kr.co.hkcloud.palette3.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            /**
             * 설문조사 참여자 대량 업로드 시 요청 파라미터 제한에 걸려 오류 발생 - MaxParameterCount - 10,000
             * MaxParameterCount 변경 - 무제한(-1)
             * application-production-lhcs.yml 에 톰캣 설정도 추가함. tomcat.max-http-form-post-size: 50MB
             */
            @Override
            protected void customizeConnector(org.apache.catalina.connector.Connector connector) {
                super.customizeConnector(connector);
                connector.setMaxParameterCount(-1);
            }
        };
    }
}
