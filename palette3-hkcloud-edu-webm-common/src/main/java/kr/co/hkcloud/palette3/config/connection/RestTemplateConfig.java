package kr.co.hkcloud.palette3.config.connection;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * RestTemplate 설정
 * 
 * <pre>
 * https://zepinos.tistory.com/34
 * </pre>
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig
{
    private static final int MAX_CONN_TOTAL     = 200;    //ea
    private static final int MAX_CONN_PER_ROUTE = 20;     //ea
    private static final int CONNECT_TIMEOUT    = 5000;   //ms
    private static final int READ_TIMEOUT       = 30000;  //ms

    private final ProxyProperties proxyProperties;


    @Bean
    public RestTemplate restTemplate()
    {
        log.info("Loading RestTemplate!!");
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        // @formatter:off
        HttpClient client;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName()
                                                                               , proxy.getPort()
                                                                               , proxy.getSchemeName());
            
            client = HttpClientBuilder.create()
                                      .setMaxConnTotal(MAX_CONN_TOTAL)
                                      .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                                      .setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {
                                          public HttpHost determineProxy(HttpHost target, org.apache.http.HttpRequest request, HttpContext context) throws HttpException {
                                              return super.determineProxy(target, request, context);
                                          }
                                      })
                                      .build();
        }
        else
        {
            client = HttpClientBuilder.create()
                                      .setMaxConnTotal(MAX_CONN_TOTAL)
                                      .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                                      .build();
        }
        // @formatter:on

        requestFactory.setHttpClient(client);
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        //로그가 필요없는 경우
//        return new RestTemplate(factory);

        //로그가 필요한 경우
        //execute() 실행 시 Stream이 닫힘
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

        return restTemplate;
    }
}


@Slf4j
class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor
{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        HttpHeaders rqHeader = request.getHeaders();
        URI rqURI = request.getURI();
        HttpMethod rqMethod = request.getMethod();
        String rqMethodValue = request.getMethodValue();
        String rqbody;
        if(body != null) {
            rqbody = (body.length > 2000) ? "[...Big body...]" : body.toString();
        }
        else {
            rqbody = "body null~!!";
        }

        // @formatter:off
        log.debug(new StringBuffer().append("\n>>>>>REQUEST INFO\n")
                                    .append("HEADER      :").append(rqHeader.toString()).append("\n")
                                    .append("URI         :").append(rqURI.toString()).append("\n")
                                    .append("METHOD      :").append(rqMethod.toString()).append("\n")
                                    .append("METHOD_VALUE:").append(rqMethodValue).append("\n")
                                    .append("BODY        :").append(rqbody).append("\n\n")
                                    .toString());

        ClientHttpResponse response = execution.execute(request, body);

        HttpHeaders rpHeader = response.getHeaders();
        HttpStatus rpStatusCode = response.getStatusCode();
        String rpStatusText = response.getStatusText();
        String rpbody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);

        log.debug(new StringBuffer().append("\n>>>>>RESPONSE INFO\n")
                                    .append("HEADER      :").append(rpHeader.toString()).append("\n")
                                    .append("STATUS_CODE :").append(rpStatusCode.toString()).append("\n")
                                    .append("STATUS_TEXT :").append(rpStatusText).append("\n")
                                    .append("BODY        :").append(rpbody.length() > 2000 ? "[...Big body...]" : rpbody).append("\n\n")
                                    .toString());
        // @formatter:on
        return response;
    }
}
