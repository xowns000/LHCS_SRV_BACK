package kr.co.hkcloud.palette3.admin.lkag.mng.api;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import kr.co.hkcloud.palette3.admin.lkag.mng.app.LkagMngService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.api
 * fileName       : LkagMstRestController
 * author         : 연동_인터페이스관리
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class LkagMngRestController {

    private final LkagMngService LkagMngService;

    @PostMapping("/admin-api/lkag/mng/selectList")
    public Object selectList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngService.selectList(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/insert")
    public Object insert(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngService.insert(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/update")
    public Object update(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngService.update(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/delete")
    public Object delete(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngService.delete(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }
    @PostMapping("/admin-api/lkag/mng/dpcn-chk")
    public Object dpcnChk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = LkagMngService.dpcnChk(mjsonParams);
        return objRetParams;    //최종결과값 반환
    }

    @PostMapping("/admin-api/lkag/mng/connectionTest")
    public Object connectionTest(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

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
        factory.setConnectTimeout(3000); //타임아웃 설정 3초
        factory.setReadTimeout(3000);//타임아웃 설정 3초
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity formEntity = null;

        if ("LPST_BODY".equals(mjsonParams.getString("PARAM_DLVR_TYPE_CD"))) { // => LKAG_PARAM_SEND_TP

            formEntity = new HttpEntity<>("{}", headers);
        }else {
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            formEntity = new HttpEntity<>(parameters, headers);
        }


        //ResponseEntity<String> response = restTemplate.exchange("http://localhost:8443/v3-api/gwm/merge/customer",HttpMethod.POST,formEntity,String.class);
        HttpMethod httpMethod = HttpMethod.POST;
        if ("RQST_POST".equals(mjsonParams.getString("RQST_SE_CD"))) {  // => LKAG_RQST_TP
            httpMethod = HttpMethod.POST;
        }else if ("RQST_GET".equals(mjsonParams.getString("RQST_SE_CD"))) {
            httpMethod = HttpMethod.GET;
        }else if ("RQST_PUT".equals(mjsonParams.getString("RQST_SE_CD"))) {
            httpMethod = HttpMethod.PUT;
        }else if ("RQST_PATCH".equals(mjsonParams.getString("RQST_SE_CD"))) {
            httpMethod = HttpMethod.PATCH;
        }else if ("RQST_DELETE".equals(mjsonParams.getString("RQST_SE_CD"))) {
            httpMethod = HttpMethod.DELETE;
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(mjsonParams.getString("LKAG_URI")
                , httpMethod, formEntity, String.class);

            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", response.getBody());
            objRetParams.setHeader("STATUS_CODE", response.getStatusCode().toString() );

            System.out.println( response.getStatusCode() );
            System.out.println( response.getBody() );
        }catch(org.springframework.web.client.HttpClientErrorException e){
            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", e.getMessage());
            objRetParams.setHeader("STATUS_CODE", e.getStatusCode().toString() );
            e.printStackTrace();
        }catch(Exception e){
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", e.getMessage());
            e.printStackTrace();
        }

        return objRetParams;

    }

}
