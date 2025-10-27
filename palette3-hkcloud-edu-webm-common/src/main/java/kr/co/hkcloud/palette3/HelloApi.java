package kr.co.hkcloud.palette3;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HelloApi {

    public static void main(
        String[] args) throws InvalidAlgorithmParameterException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, KeyStoreException, KeyManagementException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, java.text.ParseException {

        HelloApi hello = new HelloApi();

        String encryptKey = "54f920a2dd1b4969b920a2dd1bf9691d";

        System.out.println("2421561 : " + AES256Cipher.encryptString("2421561", encryptKey));
        System.out.println("1 : " + AES256Cipher.encryptString("1", encryptKey));
        System.out.println("100 : " + AES256Cipher.encryptString("100", encryptKey));
        System.out.println("DSC : " + AES256Cipher.encryptString("DSC", encryptKey));
        System.out.println("ALL : " + AES256Cipher.encryptString("ALL", encryptKey));
        System.out.println("2024-04-01 00:00 : " + AES256Cipher.encryptString("2024-04-01 00:00", encryptKey));
        System.out.println("2024-05-31 23:29 : " + AES256Cipher.encryptString("2024-05-31 23:29", encryptKey));


        /**
         * 회원정보 조회 I/F
         * @param {*} params.mbrNo : 회원번호
         * @returns 회원정보
         */
        System.out.println("01867111828 : " + AES256Cipher.encryptString("01867111828", encryptKey));
        System.out.println("123123_sk_12 : " + AES256Cipher.encryptString("123123_sk_12", encryptKey));
        System.out.println("1374674 : " + AES256Cipher.encryptString("1374674", encryptKey));

        System.out.println("mbrNo : " + AES256Cipher.encryptString("1619003", encryptKey));
        System.out.println("mbrNo : " + AES256Cipher.encryptString("MB202311279207461", encryptKey));
        System.out.println("mbrNo : " + AES256Cipher.encryptString("MB202401281292981", encryptKey));
        System.out.println("##################################");
        System.out.println("# 회원정보 조회 I/F :: https://if.goodwearmall.com/happyTalk/getMemberInfo");
        System.out.println(
            "# 요청정보 :: {\"userKey\":\"uPmD0ZFiGfWf1cUPXV_2nA\",\"mbrNo\":\"oLcfspZhHdsG7zRJznnOiGp2/jNx54AkuE7e6Ax+gYg=\",\"gPageNo\":1}");
        System.out.println("##################################");
        System.out.println("mbrNo : " + AES256Cipher.decryptString("oLcfspZhHdsG7zRJznnOiGp2/jNx54AkuE7e6Ax+gYg=", encryptKey));
        System.out.println("mbrNm : " + AES256Cipher.decryptString("VWzqeUBMllyK1vRnTvHe8A==", encryptKey));
        System.out.println("mbrEmail : " + AES256Cipher.decryptString("kkx9nDfe8B7sR1xKvZNQR39DpjvVlOtcQuqixyfYU9U=", encryptKey));
        System.out.println("mbrId : " + AES256Cipher.decryptString("1Mj8YY2n1Sbrze+tfTnmV96P2cO6BFWOm+MQSkokbhk=", encryptKey));
        System.out.println();

        /**
         * 주문 조회 I/F
         * @param {*} params.mbrNo : 멤버번호
         * @param {*} params.gPageNo : 페이지
         * @returns 주문 목록
         */
        System.out.println("##################################");
        System.out.println("# 주문 조회 I/F :: https://if.goodwearmall.com/happyTalk/getOrderList");
        System.out.println(
            "# 요청정보 :: {\"userKey\":\"uPmD0ZFiGfWf1cUPXV_2nA\",\"mbrNo\":\"oLcfspZhHdsG7zRJznnOiGp2/jNx54AkuE7e6Ax+gYg=\",\"gPageNo\":1}");
        System.out.println("##################################");
        System.out.println("RESULT_PAGE_PARAM :: ");
        System.out.println("totalCnt : " + AES256Cipher.decryptString("++vbD3gLh1bozvn1OuM+HA==", encryptKey));
        System.out.println("pageNo : " + AES256Cipher.decryptString("++vbD3gLh1bozvn1OuM+HA==", encryptKey));
        System.out.println("totalPageNo : " + AES256Cipher.decryptString("++vbD3gLh1bozvn1OuM+HA==", encryptKey));

        System.out.println("RESULT_LIST :: ");
        System.out.println("ordDt : " + AES256Cipher.decryptString("lrXjOY1sNI2Pl5pd5uS5JA==", encryptKey));
        System.out.println("brndNm : " + AES256Cipher.decryptString("A3Oop7bTMMYkh2PbI9VCmg==", encryptKey));
        System.out.println("ordNo : " + AES256Cipher.decryptString("Hn43JGYf2iiQtZBr60pZz7nYBuOcKNrhv5RkBik/hQE=", encryptKey));
        System.out.println("godNm : " + AES256Cipher.decryptString(
            "q7qEiD0Yzn0vtmWNMu9amJqdBG66nQJ2cgewzGyXJ3yTkXYriTR6bGgim+7AebDL16qwG13oEIqERqAg0oc8jg==", encryptKey));
        System.out.println("godImgUrl : " + AES256Cipher.decryptString(
            "3zcHQDNjE0IiH05XdRFgHSpQfdeMapkm+bsGnrlNNymrDX5hl+UfjWtNhPpA8KCrVMynbxS30T7VVhw7+WTgBw==", encryptKey));
        System.out.println("price : " + AES256Cipher.decryptString("qDFxZ3SNOQ0z7jTb/JGS1w==", encryptKey));
        System.out.println("ordStatNm : " + AES256Cipher.decryptString("dVtZSFw0cYo7CGHwGFanFg==", encryptKey));
        System.out.println("ordQty : " + AES256Cipher.decryptString("++vbD3gLh1bozvn1OuM+HA==", encryptKey));
        System.out.println("itmNm : " + AES256Cipher.decryptString("f7FD0ufbShsqEwjNoyW6CqTbTE8aqPFnDhJBU0Gng0c=", encryptKey));
        System.out.println();

        /**
         * 클레임 신청가능 목록 조회 I/F
         * @param {*} params.mbrNo : 멤버번호
         * @param {*} params.gPageNo : 페이지
         * @param {*} params.clmTpCd : 클레임 타입(취소-CNCL/교환-EXCHG/반품-RTGOD)
         * @returns 클레임 신청가능 목록
         */
        System.out.println("##################################");
        System.out.println("# 클레임 신청가능 목록 조회 I/F :: https://if.goodwearmall.com/happyTalk/getClaimRequestList");
        System.out.println("##################################");

        System.out.println("##################################");
        System.out.println("3448586 :: " + AES256Cipher.encryptString("3448586", encryptKey));
        System.out.println("# 회원정보 조회 I/F :: https://if.goodwearmall.com/happyTalk/getMemberInfo");
        System.out.println("# 요청정보 :: {\"mbrNo\":\"p7P//8yi1k2shWPaDzk3dg==\",\"gPageNo\":1}");
        System.out.println("mbrNo : " + AES256Cipher.decryptString("p7P//8yi1k2shWPaDzk3dg==", encryptKey));
        System.out.println("mbrNm : " + AES256Cipher.decryptString("7HtBsf+PCKLFtND+dngP/A==", encryptKey));
        System.out.println("mbrEmail : " + AES256Cipher.decryptString("zC0/mfShEHl04zEIskxVsXYz47R1GUwv/OJrkfiCwZc=", encryptKey));
        System.out.println("mbrId : " + AES256Cipher.decryptString("xIhljHXHOldpXd2NCyMZrw==", encryptKey));

        //System.out.println("8q3Lgy2GaGfPk2Dfd6eBew== : " + AES256Cipher.decryptString("8q3Lgy2GaGfPk2Dfd6eBew==", encryptKey) );

        System.out.println("##################################");
        Class<?> c = Class.forName("kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher");
        Method m = c.getMethod("decryptString", String.class, String.class);
        System.out.println(
            m.invoke(c, "q7qEiD0Yzn0vtmWNMu9amJqdBG66nQJ2cgewzGyXJ3yTkXYriTR6bGgim+7AebDL16qwG13oEIqERqAg0oc8jg==", encryptKey));
        System.out.println(
            m.invoke(c, "3zcHQDNjE0IiH05XdRFgHSpQfdeMapkm+bsGnrlNNymrDX5hl+UfjWtNhPpA8KCrVMynbxS30T7VVhw7+WTgBw==", encryptKey));
        System.out.println(m.invoke(c, "qDFxZ3SNOQ0z7jTb/JGS1w==", encryptKey));
        System.out.println(m.invoke(c, "dVtZSFw0cYo7CGHwGFanFg==", encryptKey));
        System.out.println(m.invoke(c, "++vbD3gLh1bozvn1OuM+HA==", encryptKey));

        System.out.println("##################################");

        System.out.println("MB202307141197301 :: " + AES256Cipher.encryptString("MB202307141197301", encryptKey));

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
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //headers.set("X-V3-Authorization", "bearer iGyUpyUtuQQe6N6Y16OddL9CsxL0mdXTg2JZ0j3KNKbYjCvKHvbwqw/McN92QfXYdE0b8qSJIKJ9xuvn7TP3IFrp0NPCsRKaBy/u+nkTo6U4+YvTzm11oaOmmo3uY11D");

        /* API 호출 예제
        파라메터 값  AES-256암호화키 값은 : 54f920a2dd1b4969b920a2dd1bf9691d 입니다.

        {
            "params" : [{
            "customerTelNo" : "1NpZcUszOk5AQK02DNQNaw==",
                "customerNm" : "bwMS5L0sT52jmp0aoxX+Mg==",
                "customerStatUdt" : "VOjFraCb+DMxRjDMI2GxpQ==",
                "customerStat" : "mWkF+u6wg7DwuB6RM7w95w==",
                "memberNo" : "HnZaQOYyYgnuXOUsgpJA7g=="
            },
            {
                "customerTelNo" : "mf0nbhA0uZKBDgh7ZwVjjw==",
                "customerNm" : "v6UKrgPakwc8o1n5e8Mm2g==",
                "customerStatUdt" : "VOjFraCb+DMxRjDMI2GxpQ==",
                "customerStat" : "mWkF+u6wg7DwuB6RM7w95w==",
                "memberNo" : "wLlEHu4GJIP111M7MIryDtJKA=="
            },
            {
                "customerTelNo" : "IRPmGOD4r1xJ0IgEsnKJEQ==",
                "customerNm" : "hQJL8mxRRMyKqUWT9irlkg==",
                "customerStatUdt" : "VOjFraCb+DMxRjDMI2GxpQ==",
                "customerStat" : "mWkF+u6wg7DwuB6RM7w95w==",
                "memberNo" : "rqKGPnh9s88q309XOyZGHw=="
            }
            ]
         }
        */

        String clientId = "27p2KaCJ0x4dXALj2cU2Ad";   //제공된 애플리케이션 ID
        String clientSecret = "$2a$04$vVAlxnhx.UnopcUogfJet.";
        String accountId =  "skawns6465"; //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
        Long timestamp = System.currentTimeMillis(); // 1643961623299L로 가정

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", clientId);  //7dMvteboKNHwyRremLXXXX
        parameters.add("timestamp", timestamp.toString() );
        parameters.add("grant_type", "client_credentials");
        parameters.add("client_secret_sign", hello.generateSignature(clientId, clientSecret, timestamp));    //전자서명 생성 방법을 따라 생성된 전자서명
        parameters.add("type", "SELF");
        parameters.add("account_id", accountId);

        log.info("parameters {}" , parameters.toString());

        HttpEntity formEntity = new HttpEntity<>(parameters, headers);
        //ResponseEntity<String> response = restTemplate.exchange("http://localhost:8443/v3-api/gwm/merge/customer",HttpMethod.POST,formEntity,String.class);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl( "https://api.commerce.naver.com/external/v1/oauth2/token" ).queryParam("param", "ABCDE+=,$%ABCDE").build();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri.toUri(), HttpMethod.POST,
                formEntity, String.class);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String text = sdf.format(date);
        System.out.println(text);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("https://api.commerce.naver.com/external/v1/contents/qnas?page=1&size=100&answered=true&fromDate=2024-04-24T00%3A00%3A00.000%2B09%3A00&toDate=2024-04-25T23%3A59%3A59.100%2B09%3A00")
            .get()
            .addHeader("Authorization", "Bearer 5Yr9IS6vRDZ2WvJoh14Css")
            .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            // 응답 받아서 처리
            ResponseBody body = response.body();
            if (body != null) {
                System.out.println("Response:" + body.string());
            }
        }
        else
            System.err.println("Error Occurred");


        String strDate4 = "2022-12-15T09:00:00.624+09:00";
        DateTimeFormatter pattern4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        LocalDateTime parse4 = LocalDateTime.parse(strDate4, pattern4);
        System.out.println(strDate4 +" LocalDateTime => "+ parse4.toString());

        OffsetDateTime parse5 = OffsetDateTime.parse(strDate4, pattern4);
        System.out.println(strDate4 +" OffsetDateTime => "+ parse5.toString() +" , " + new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(parse5.toInstant())));


        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("LocalDateTime.now().toString() => "+ localDateTime);

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        System.out.println("OffsetDateTime.now().toString() => "+ offsetDateTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println("zonedDateTime.now().toString() => "+ zonedDateTime);


        LocalDateTime parse6 = LocalDateTime.parse("20221215090000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(parse6 +" 6=> "+ parse6.toString() +" , " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(Date.from(parse5.toInstant())));

        LocalDateTime parse7 = LocalDateTime.parse("2022-12-15T09:00:00.624+09:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        System.out.println(parse7 +" 7=> "+ parse7.toString() +" , " + new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(parse7.toInstant(ZoneOffset.UTC))));
        System.out.println(parse7 +" 7=> "+ parse7.toString() +" , " + new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(parse7.toInstant(ZoneOffset.of("+09:00")))));    //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

        LocalDateTime parse8 = LocalDateTime.parse("20221215090000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println(parse8 +" 8=> "+ parse6.toString() +" , " + new SimpleDateFormat("yyyy-MM-dd").format(Date.from(parse8.toInstant(ZoneOffset.UTC))));
        System.out.println(parse8 +" 8=> "+ parse6.toString() +" , " + new SimpleDateFormat("yyyy-MM-dd").format(Date.from(parse8.toInstant(ZoneOffset.of("+09:00")))));    //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

        LocalDateTime date1 = LocalDateTime.parse("20240429235959", DateTimeFormatter.ofPattern( "yyyyMMddHHmmss"));
        System.out.println (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ).format(Date.from(date1.toInstant(ZoneOffset.UTC))) );
        System.out.println (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ).format(Date.from(date1.toInstant(ZoneOffset.of("+09:00")))) );    //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

        // "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ==> 2024-07-17T19:22:53.000+09:00
        // "yyyy-MM-dd'T'HH:mm:ss.SSS" ==> 2024-07-17T19:22:53.000
        String dateString = "2024-07-17T19:22:53.000+09:00";
        String convertDate = DateCmmnUtils.getISO8601ToDateString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", dateString);

        System.out.println("expires_at :::::::::::::: " + dateString + " ==> pattern : yyyy-MM-dd'T'HH:mm:ss.SSS , convert : " + convertDate );

        String convertDate2 = DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ss.SSS", convertDate);
        System.out.println("expires_at :::::::::::::: " + convertDate + " ==> pattern : yyyy-MM-dd'T'HH:mm:ss.SSS , convert : " + convertDate2 );

        String convertDate3 = DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", convertDate);
        System.out.println("expires_at :::::::::::::: " + convertDate + " ==> pattern : yyyy-MM-dd'T'HH:mm:ss.SSSXXX , convert : " + convertDate3 );

        String convertDate4 = DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd HH:mm", convertDate);
        System.out.println("expires_at :::::::::::::: " + convertDate + " ==> pattern : yyyy-MM-dd HH:mm , convert : " + convertDate4 );






        Calendar fromCal = Calendar.getInstance();
        SimpleDateFormat fromFmt = new SimpleDateFormat("yyyyMMddHHmmss");
        Date fromDate = fromFmt.parse("20240527094900");
        fromCal.setTime( fromDate );
        fromCal.add(Calendar.SECOND, -30);
        System.out.println( fromFmt.format(fromCal.getTime()) ) ;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date expiration = format.parse("20240718141600");

        Long now = new Date().getTime();            // 현재 시간
        System.out.println( (expiration.getTime() - now));

        System.out.println( ">>>> " +  DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd", "20240718141600") );



    }

    /**
     * 전자서명 생성
     * @param clientId
     * @param clientSecret
     * @param timestamp
     * @return
     */
    public String generateSignature(String clientId, String clientSecret, Long timestamp) {
        // 밑줄로 연결하여 password 생성
        String password = StringUtils.join(clientId, "_",  timestamp);
        // bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        // base64 인코딩
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${k8s.pod.name}")
    private String k8sPodName;

    @GetMapping("/api/helloenv")
    public Object processRtnAttachFiles() throws TelewebApiException {

        return System.getenv();
    }
}
