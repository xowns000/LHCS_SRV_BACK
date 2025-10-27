package kr.co.hkcloud.palette3.core.util;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : kr.co.hkcloud.palette3.core.util
 * fileName       : PaletteRestTemplate
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
@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class PaletteRestTemplate {

    private Integer connectTimeout = 3000;
    private Integer readTimeout = 20000;

    public <T, R> R exchange(URI uri, T query, HttpHeaders httpHeaders, HttpMethod httpMethod ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.exchange(uri, query, httpHeaders, httpMethod, String.class);
    }

    public <T, R> R exchange(URI uri, T query, HttpHeaders httpHeaders, HttpMethod httpMethod, Class responseType ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //--------------------------------------------------------------------------------------
        // 1. 연결준비.
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 모든 인증서를 신뢰하도록 설정한다
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();
        httpClientBuilder.setSSLContext(sslContext);
        // Https 인증 요청시 호스트네임 유효성 검사를 진행하지 않게 한다.
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        httpClientBuilder.setConnectionManager(connMgr);

        // RestTemplate 와 HttpClient 연결
        HttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(connectTimeout); //타임아웃 설정 3초
        factory.setReadTimeout(readTimeout);//타임아웃 설정 10초
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpEntity<T> formEntity = new HttpEntity<>(query, httpHeaders);
        return (R) restTemplate.exchange(uri, httpMethod, formEntity, responseType);
    }

}
