package kr.co.hkcloud.palette3.integration.commerce_api.cafe24.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.security.crypto.Base64;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.core.util.PaletteLkagRestTemplate;
import kr.co.hkcloud.palette3.core.util.PaletteRestTemplate;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.integration.service.CommerceApiService;
import kr.co.hkcloud.palette3.integration.service.CommerceLkagServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service(Cafe24ServiceImpl.BEAN_ID)
public class Cafe24ServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {

    public static final String BEAN_ID = "CAFE24";

    @Autowired
    private TwbComDAO mobjDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PaletteRestTemplate paletteRestTemplate;

    @Autowired
    private PaletteLkagRestTemplate paletteLkagRestTemplate;

    @Autowired
    private PaletteUtils paletteUtils;

    @Value("${commerce.api.cafe24.auth_code_url}")
    private String AUTH_CODE_URL;

    @Value("${commerce.api.cafe24.auth_code_redirect_uri}")
    private String AUTH_CODE_REDIRECT_URI;

    @Value("${commerce.api.cafe24.auth_url}")
    private String AUTH_URL;

    private String X_CAFE24_API_VERSION = "2024-06-01";

    // 토큰 생성용
    private final String AUTHORIZE_CODE_KEY = "cafe24:authorize:code";
    private final String ACCESS_TOKEN_KEY = "cafe24:access_token";
    private final String ACCESS_TOKEN_EXPIRES_AT_KEY = "cafe24:access_token_expires_at";
    private final String REFRESH_TOKEN_KEY = "cafe24:refresh_token";
    private final String REFRESH_TOKEN_EXPIRES_AT_KEY = "cafe24:refresh_token_expires_at";


    public Cafe24ServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
                             RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository) {
        super(mobjDao, paletteJsonUtils, paletteLkagRestTemplate, redisCacheCustcoLkagRepository);
    }

    /**
     * cafe24 서버에서 access token 요청.
     *
     * @param baseDataObj
     */
    public boolean setAccessToken(JSONObject baseDataObj) {

        boolean result = false;

        String clientId = baseDataObj.getString("ACS_ACNT_KEY");   //제공된 애플리케이션 ID
        String clientSecret = baseDataObj.getString("ACS_SECTY_KEY");
        String serviceKey = baseDataObj.getString("ACS_ACNT_ID"); //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
        String mallId = baseDataObj.getString("ETC_01"); //카페24의경우 mallId
        log.info("clientId :::::::::::::: " + clientId);
        log.info("clientSecret :::::::::::::: " + clientSecret);
        log.info("serviceKey :::::::::::::: " + serviceKey);
        log.info("mallId :::::::::::::: " + mallId);
        log.info("authUrl :::::::::::::: " + AUTH_URL);
        log.info("redirectUri :::::::::::::: " + AUTH_CODE_REDIRECT_URI);

        String redisKey = baseDataObj.getString("SCHEMA_ID") + "_" + mallId;

        String sUrl = AUTH_URL.replaceAll("\\{mallid}", mallId);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        if ("authorization_code".equals(baseDataObj.getString("grant_type"))) {
            //access 토큰 신규발급 요청
            parameters.put("grant_type", Collections.singletonList("authorization_code"));
            parameters.put("code", Collections.singletonList(baseDataObj.getString("authorizeCode")));
            parameters.put("redirect_uri", Collections.singletonList(AUTH_CODE_REDIRECT_URI));
        } else if ("refresh_token".equals(baseDataObj.getString("grant_type"))) {
            //refresh 토큰으로  access토큰 발급 요청
            log.info("refresh 토큰으로  access토큰 발급");
            parameters.put("grant_type", Collections.singletonList("refresh_token"));
            parameters.put("refresh_token", Collections.singletonList(stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + ":" + redisKey)));
        } else {
            //저장소 refresh 토큰으로  access토큰 발급 요청
            log.info("refresh 토큰으로  access토큰 발급");
            String repositoryToken = paletteUtils.getRefreshTokenRepository(redisKey);

            parameters.put("grant_type", Collections.singletonList("refresh_token"));
            parameters.put("refresh_token", Collections.singletonList(repositoryToken.split(":")[0]));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Basic " + Base64.encode(new String(clientId + ":" + clientSecret).getBytes()));

        try {

            log.info(">>>>>>>>>> sUrl : " + sUrl);
            log.info(">>>>>>>>>> headers : " + headers.toString());
            log.info(">>>>>>>>>> parameters : " + parameters.toString());

            ResponseEntity<String> response = paletteRestTemplate.exchange(new URI(sUrl), parameters, headers, HttpMethod.valueOf("POST"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
                if (StringUtils.isNotEmpty(response.getBody())) {
                    JSONObject responseJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);

                    // access_token 만료시간 yyyy-MM-dd'T'HH:mm:ss.SSS ==> yyyyMMddHHmmss
                    LocalDateTime localDateTime = LocalDateTime.parse(responseJson.getString("expires_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                    String expires_at = (new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(localDateTime.toInstant(ZoneOffset.of("+09:00")))));

                    stringRedisTemplate.delete(ACCESS_TOKEN_KEY + ":" + redisKey);
                    stringRedisTemplate.delete(ACCESS_TOKEN_EXPIRES_AT_KEY + ":" + redisKey);

                    String access_token = responseJson.getString("access_token");
                    System.out.println("access_token :::::::::::::: " + access_token);
                    System.out.println("expires_at :::::::::::::: " + expires_at);
                    stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_KEY + ":" + redisKey, access_token, 2, TimeUnit.HOURS);
                    stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_EXPIRES_AT_KEY + ":" + redisKey, expires_at, 2, TimeUnit.HOURS);

                    // refresh_token 만료시간 yyyy-MM-dd'T'HH:mm:ss.SSS ==> yyyyMMddHHmmss
                    LocalDateTime localDateTime2 = LocalDateTime.parse(responseJson.getString("refresh_token_expires_at"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                    String refresh_token_expires_at = (new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(localDateTime2.toInstant(ZoneOffset.of("+09:00")))));

                    String refresh_token = responseJson.getString("refresh_token");
                    System.out.println("refresh_token :::::::::::::: " + refresh_token);
                    System.out.println("refresh_token_expires_at :::::::::::::: " + refresh_token_expires_at);

                    stringRedisTemplate.delete(REFRESH_TOKEN_KEY + ":" + redisKey);
                    stringRedisTemplate.delete(REFRESH_TOKEN_EXPIRES_AT_KEY + ":" + redisKey);
                    stringRedisTemplate.opsForValue().set(REFRESH_TOKEN_KEY + ":" + redisKey, refresh_token, 15, TimeUnit.DAYS);
                    stringRedisTemplate.opsForValue().set(REFRESH_TOKEN_EXPIRES_AT_KEY + ":" + redisKey, refresh_token_expires_at, 15, TimeUnit.DAYS);

                    //유실방지를 위해 저장소에도 저장함.
                    paletteUtils.setRefreshTokenRepository(redisKey,
                            refresh_token + ":" + refresh_token_expires_at + ":" + clientId + ":" + clientSecret + ":" + serviceKey + ":" + mallId
                    );

                    result = true;
                }
            }
        } catch (NoSuchAlgorithmException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Cafe24 인증서버로 부터 인증코드를 리턴받아 레디스에 저장한다. 만료시간 60초
     * 이 인증코드는 엑세스토큰을 만들기위한 필수 값이다.
     * <p>
     * 1. 엑세스토큰 & 리프레시토큰 만료시 토큰만들기 위해 인증코드요청( WEB팝업으로 요청 )한다.
     * https://{mallid}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id={client_id}&state={state}&redirect_uri={redirect_uri}&scope={scope}
     * https://jongd001.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id=Zkh8ovAcKRQmyjeFgTbCwH&redirect_uri=https://palette.hkpalette.com/intgr-api/commerce/v2/oauth/cafe24&scope=mall.read_community,mall.write_community,mall.read_order,mall.read_shipping,mall.read_product,mall.read_customer&state=goodwearmall
     * <p>
     * 현 메소드는 redirect_uri 호출되면 수행한다.
     * <p>
     * 2. redis에 authorizeCode 세팅
     * 3. authorizeCode를 활용하여 cafe24에 인증토큰 요청하여 access_token, refresh_token 레디스에 저장한다.
     *
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON oauthCode(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        String redisKey = jsonParams.getString("SCHEMA_ID") + "_" + jsonParams.getString("mallId");

        /* Redis Set */
        stringRedisTemplate.delete(AUTHORIZE_CODE_KEY + ":" + redisKey);
        stringRedisTemplate.opsForValue().set(AUTHORIZE_CODE_KEY + ":" + redisKey, jsonParams.getString("code"), 60, TimeUnit.SECONDS);
        String authorizeCode = String.valueOf(stringRedisTemplate.opsForValue().get(AUTHORIZE_CODE_KEY + ":" + redisKey));
        log.info("authorizeCode :::::::::::::: " + authorizeCode);

        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi(jsonParams);
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        baseDataObj.put("authorizeCode", authorizeCode);
        baseDataObj.put("grant_type", "authorization_code");    // access 토큰 신규발급 구분

        // access 토큰 발급요청.
        boolean isToken = this.setAccessToken(baseDataObj);
        if (isToken) {
            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", "");
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "토큰 발급 실패");
        }

        return objRetParams;
    }

    @Override
    public TelewebJSON sample_authentication(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        return objRetParams;
    }

    /**
     * 인증토큰 발급요청.
     *
     * @param baseDataObj
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON authentication(TelewebJSON jsonParams, JSONObject baseDataObj) throws TelewebAppException {
        if (baseDataObj == null) {
            TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi(jsonParams);
            baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        }

        TelewebJSON objRetRspns = new TelewebJSON(jsonParams);
        String clientId = baseDataObj.getString("ACS_ACNT_KEY");   //제공된 애플리케이션 ID
        String clientSecret = baseDataObj.getString("ACS_SECTY_KEY");
        String serviceKey = baseDataObj.getString("ACS_ACNT_ID"); //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
        String mallid = baseDataObj.getString("ETC_01"); //카페24의경우 mallid
        String schemaId = baseDataObj.getString("SCHEMA_ID");
        String certCustcoId = baseDataObj.getString("CERT_CUSTCO_ID");
        String lkagId = baseDataObj.getString("LKAG_ID");

        String redisKey = schemaId + "_" + mallid;

        String access_token = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + ":" + redisKey);
        String expires_at = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_EXPIRES_AT_KEY + ":" + redisKey);
        String refresh_token = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + ":" + redisKey);
        String refresh_token_expires_at = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_EXPIRES_AT_KEY + ":" + redisKey);

        JSONObject responseJson = new JSONObject();
        objRetRspns.setHeader("ERROR_FLAG", false);
        objRetRspns.setHeader("ERROR_MSG", "");

        try {

            if (!StringUtils.isEmpty(access_token) && DateCmmnUtils.getExpiration(expires_at) > (1000 * 60 * 10)) {   //만료시간 10분이상
                responseJson.put("access_token", access_token);
            } else {
                if (!StringUtils.isEmpty(refresh_token) && DateCmmnUtils.getExpiration(refresh_token_expires_at) > 0) {   //refresh토큰이 살아있는경우
                    baseDataObj.put("grant_type", "refresh_token");
                    // refresh_token 으로 access 토큰 발급요청.
                    this.setAccessToken(baseDataObj);
                    responseJson.put("access_token", stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + ":" + redisKey));

                } else {
                    String repositoryToken = paletteUtils.getRefreshTokenRepository(redisKey);
                    if (StringUtils.isNotEmpty(repositoryToken)) { //저장소 토큰으로 다시한번 체크
                        String[] repositoryTokenArr = repositoryToken.split(":");
                        refresh_token = repositoryTokenArr[0];
                        refresh_token_expires_at = repositoryTokenArr[1];
                        if (!StringUtils.isEmpty(refresh_token) && DateCmmnUtils.getExpiration(refresh_token_expires_at) > 0) {
                            baseDataObj.put("grant_type", "repository_refresh_token");
                            // refresh_token 으로 access 토큰 발급요청.
                            this.setAccessToken(baseDataObj);
                            responseJson.put("access_token", stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + ":" + redisKey));
                        } else {
                            objRetRspns.setHeader("ERROR_FLAG", true);
                            objRetRspns.setHeader("ERROR_MSG", "카페24 인증정보가 없습니다.");
                        }
                    } else {
                        objRetRspns.setHeader("ERROR_FLAG", true);
                        objRetRspns.setHeader("ERROR_MSG", "카페24 인증정보가 없습니다.");
                    }
                }
            }
        } catch (java.lang.NullPointerException e) {
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("ERROR_MSG", "카페24 인증정보가 없습니다.");

//            objRetRspns.setHeader("ERROR_MSG", "expire");
//            objRetRspns.setHeader("ou", Base64.encode(oauthUrl.replaceAll("\\{mallid}", mallid).getBytes())); //oauthUrl
//            objRetRspns.setHeader("ci", Base64.encode(clientId.getBytes()));    //clientId
//            objRetRspns.setHeader("ru", Base64.encode(redirectUri.getBytes()));  //redirectUri
//            objRetRspns.setHeader("sp", Base64.encode("mall.read_community,mall.write_community,mall.read_order,mall.read_shipping,mall.read_product,mall.read_customer".getBytes())); //scope
//            objRetRspns.setHeader("st", Base64.encode((mallid + "__@@__" + lkagId + "__@@__" + schemaId + "__@@__" + certCustcoId).getBytes()));  // state
        }
        objRetRspns.setDataObject("DATA", responseJson);

        return objRetRspns;
    }

    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi(jsonParams);
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray rspnsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_RSPNS");
        if (rspnsDataObj == null) {
            rspnsDataObj = new JSONArray();
        }

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + jsonParams.getHeaderString("ACCESS_TOKEN"));   // 자기자신을콜

        JSONObject jsonParamsObj = (JSONObject) jsonParams.getDataObject(TwbCmmnConst.G_DATA).get(0);
        Iterator i = jsonParamsObj.keys();
        while (i.hasNext()) {
            String key = i.next().toString();
            String val = jsonParamsObj.getString(key);
            parameters.add(key, val);
        }

        TelewebJSON objRetRspns = new TelewebJSON();
        try {
            ResponseEntity<String> response = paletteLkagRestTemplate.exchange(new URI(baseDataObj.getString("LKAG_URI")), parameters, headers, HttpMethod.valueOf("POST"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {

                if (StringUtils.isNotEmpty(response.getBody())) {
                    JSONObject responseJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                    objRetRspns.setHeader("ERROR_FLAG", false);
                    objRetRspns.setHeader("ERROR_MSG", "");
                    objRetRspns.setDataObject("DATA", responseJson);
                } else {
                    objRetRspns.setHeader("ERROR_FLAG", false);
                    objRetRspns.setHeader("ERROR_MSG", "");
                    objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                    objRetRspns.setHeader("PARAMETERS", parameters.toString());
                    objRetRspns.setDataObject("DATA", new JSONObject());
                }
            } else if (response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT) {   // 성공, 응답정보없음
                objRetRspns.setHeader("ERROR_FLAG", false);
                objRetRspns.setHeader("ERROR_MSG", "성공, 응답내용없음");
                objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                objRetRspns.setHeader("PARAMETERS", parameters.toString());
                objRetRspns.setDataObject("DATA", new JSONObject());
            } else {
                objRetRspns.setHeader("ERROR_FLAG", true);
                objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                objRetRspns.setHeader("PARAMETERS", parameters.toString());
                objRetRspns.setHeader("ERROR_MSG", response.getBody());
            }
        } catch (ResourceAccessException e) {
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
            objRetRspns.setHeader("PARAMETERS", parameters.toString());
            objRetRspns.setHeader("ERROR_MSG", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
            objRetRspns.setHeader("PARAMETERS", parameters.toString());
            objRetRspns.setHeader("ERROR_MSG", e.getMessage());
        }

        return objRetRspns;

    }

    @Override
    public TelewebJSON call_batch_api(
            TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON retObj = new TelewebJSON(jsonParams);
        retObj = this.call_api(jsonParams);
        log.info("response :: {}", retObj.toString());
        if (jsonParams.getDataObject("DATA2") != null) {

        }
        return null;
    }

    /////////////////////////////////////////////////////////////////
    // Cafe24는 풀리오(pulio)요청사항에 의해 아래 재정의

    // 1. 회원등급정보
    private String api_customergroups_url = "https://{mallid}.cafe24api.com/api/v2/admin/customergroups";

    // 2. 주문정보
    private String api_others_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders?start_date={start_date}&end_date={end_date}&buyer_cellphone={buyer_cellphone}&order_id={order_id}";

    // 3. 주문품목정보
    private String api_others_items_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/{order_id}/items";

    // 4. 취소 완료되었거나 취소처리 진행중인 내역, 환불 방식, 환불금액, 철회 사유 구분 등을 확인할 수 있습니다.
    private String api_others_cancellation_url = "https://{mallid}.cafe24api.com/api/v2/admin/cancellation/{claim_code}";

    // 5. 교환
    private String api_others_exchange_url = "https://{mallid}.cafe24api.com/api/v2/admin/exchange/{claim_code}";
    // 6. 반품
    private String api_others_return_url = "https://{mallid}.cafe24api.com/api/v2/admin/return/{claim_code}";
    // 7. 환불
    private String api_others_refunds_url = "https://{mallid}.cafe24api.com/api/v2/admin/refunds/{claim_code}";

    // 8. 주문자 정보
    private String api_orders_buyer_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/{order_id}/buyer";
    // 9. 수문 수령자 정보
    private String api_orders_receivers_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/{order_id}/receivers";

    // 10. 현금영수증조회
    private String api_orders_cashreceipt_url = "https://{mallid}.cafe24api.com/api/v2/admin/cashreceipt?order_id={order_id}&start_date={start_date}&end_date={end_date}";

    // 11. 혜택 정보
    private String api_orders_benefits_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/benefits?order_id={order_id}";

    // 12.쿠폰
    private String api_orders_coupons_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/coupons?order_id={order_id}";

    // 13.적립금
    //private String api_orders_points_url = "https://{mallid}.cafe24api.com/api/v2/admin/points?order_id={order_id}&start_date={start_date}&end_date={end_date}";

    // 13.적립금
    private String api_orders_memo_url = "https://{mallid}.cafe24api.com/api/v2/admin/orders/{order_id}/memos";


    public JSONObject orders(HttpServletRequest request) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JsonProcessingException {

        JSONObject retObj;

        if (StringUtils.isNotEmpty(request.getParameter("buyer_cellphone")) || StringUtils.isNotEmpty(request.getParameter("order_id"))) {

            String accessToken = "";
            String clientId = "";
            String clientSecret = "";
            String serviceKey = "";
            String mallId = "";
            String lkagId = "";
            String certCustcoId = "";
            String schemaId = "";

            //관리자 > 데이터연동메뉴를 통한 테스트 호출인경우
            if (StringUtils.isNotEmpty(request.getHeader("X-V3-MODE")) && "TEST".equals(request.getHeader("X-V3-MODE"))) {
                accessToken = "sTyPwtElIhFpfBuloENssH"; //테스트용 postman으로 토큰발행.
                clientId = "e1hSs1aKXaUHLxWrpc9R3G";   //제공된 애플리케이션 ID
                clientSecret = "OOVrOuX6Lh8kt64eSfaI7N";
                serviceKey = "uSLNGTqI3qufTFgKwcJ6v9yAnt5Zw8wpyWZ+ptQ7iTY="; //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
                mallId = "kjd4717"; //카페24의경우 mallId
                lkagId = "34"; // starlaw
                certCustcoId = "34"; // 개발서버 starlaw
                schemaId = "starlaw";
            } else {
                // 정상 호출인경우.
                TelewebJSON jsonParams = new TelewebJSON();
                jsonParams.setString("BEAN_ID", request.getParameter("BEAN_ID"));
                jsonParams.setString("LKAG_ID", request.getParameter("LKAG_ID"));
                jsonParams.setString("CERT_CUSTCO_ID", request.getParameter("CERT_CUSTCO_ID"));

                TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi(jsonParams);
                JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
                TelewebJSON authenticationJson = this.authentication(jsonParams, baseDataObj);
                if (authenticationJson.getHeaderBoolean("ERROR_FLAG")) {
                    return authenticationJson.getHeaderJSON();
                } else {
                    accessToken = authenticationJson.getString("access_token");

                    clientId = baseDataObj.getString("ACS_ACNT_KEY");   //제공된 애플리케이션 ID
                    clientSecret = baseDataObj.getString("ACS_SECTY_KEY");
                    serviceKey = baseDataObj.getString("ACS_ACNT_ID");
                    mallId = baseDataObj.getString("ETC_01"); //카페24의경우 mallId
                    lkagId = request.getParameter("LKAG_ID");
                    certCustcoId = request.getParameter("CERT_CUSTCO_ID");
                    schemaId = baseDataObj.getString("SCHEMA_ID");
                }
            }

            log.info("clientId :::::::::::::: " + clientId);
            log.info("clientSecret :::::::::::::: " + clientSecret);
            log.info("serviceKey :::::::::::::: " + serviceKey);
            log.info("mallId :::::::::::::: " + mallId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Cafe24-Api-Version", X_CAFE24_API_VERSION);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.setContentType(new MediaType("application", "json"));

            //1. 주문목록
            String start_date = request.getParameter("start_date");
            //if( !start_date.contains("-") ) start_date = start_date.substring(0,4) +"-"+ start_date.substring(4,6) +"-"+ start_date.substring(6,8);
            String end_date = request.getParameter("end_date");
            //if( !end_date.contains("-") ) end_date = end_date.substring(0,4) +"-"+ end_date.substring(4,6) +"-"+ end_date.substring(6,8);
            String srch_order_id = request.getParameter("order_id");
            if (srch_order_id == null) srch_order_id = "";
            String srch_buyer_cellphone = request.getParameter("buyer_cellphone");
            if (srch_buyer_cellphone == null) srch_buyer_cellphone = "";

//            if ("null".equals(srch_buyer_cellphone)) {  //검색요청 폰번호가 우선.
//                String custId = request.getParameter("custId");
//                if (custId != null) {
//                    TelewebJSON jsonParams = new TelewebJSON();
//                    jsonParams.setString("LNKG_SCHEMA_ID", schemaId);
//                    jsonParams.setString("custId", custId);
//                    TelewebJSON objTbl = mobjDao.select("kr.co.hkcloud.palette3.integration.dao.CommerceLkagMapper", "selectCustCellphone",
//                            jsonParams);
//                    if (objTbl.getHeaderInt("COUNT") > 0) {
//                        srch_buyer_cellphone = objTbl.getString("CUST_PHN_NO");
//                    }
//                }
//            }

            //DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ss.SSS", start_date)
            //DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ss.SSS", end_date)

            String sUrl = api_others_url
                    .replaceAll("\\{mallid}", mallId)
                    .replaceAll("\\{start_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd", start_date))
                    .replaceAll("\\{end_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd", end_date))
                    .replaceAll("\\{buyer_cellphone}", srch_buyer_cellphone.replaceAll("-", ""))
                    .replaceAll("\\{order_id}", srch_order_id);

            TelewebJSON objRetRspns = new TelewebJSON();

            ResponseEntity<String> response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
                if (StringUtils.isNotEmpty(response.getBody())) {
                    JSONObject responseJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                    JSONArray orders = responseJson.getJSONArray("orders");
                    responseJson.put("size", orders.size());
                    responseJson.put("start_date", start_date);
                    responseJson.put("end_date", end_date);
                    responseJson.put("mallid", mallId);
                    responseJson.put("at", accessToken);
                    if (orders.size() > 0) {

                        for (int i = 0; i < orders.size(); i++) {
                            JSONObject oJson = orders.getJSONObject(i);
                            String order_id = oJson.getString("order_id");

                            // 주문물품정보(items)
                            sUrl = api_others_items_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", order_id);
                            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);

                                JSONArray items = bJson.getJSONArray("items");
                                String RESP_product_name = "";
                                for (int j = 0; j < items.size(); j++) {
                                    JSONObject itemJson = items.getJSONObject(j);
                                    if (j == 0) RESP_product_name = itemJson.getString("product_name");
//                                    if (StringUtils.isNotEmpty(itemJson.getString("claim_code")) && !"null".equals(itemJson.getString("claim_code"))) {
//
//                                        //취소
//                                        sUrl = api_others_cancellation_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
//                                        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                            JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                            itemJson.put("RESP_cancellation", _bJson.getJSONObject("cancellation"));
//                                        } else {
//                                            itemJson.put("RESP_cancellation", null);
//                                        }
//
//                                        //교환
//                                        sUrl = api_others_exchange_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
//                                        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                            JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                            itemJson.put("RESP_exchange", _bJson.getJSONObject("exchange"));
//                                        } else {
//                                            itemJson.put("RESP_exchange", null);
//                                        }
//
//                                        //반품
//                                        sUrl = api_others_return_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
//                                        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                            JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                            itemJson.put("RESP_return", _bJson.getJSONObject("return"));
//                                        } else {
//                                            itemJson.put("RESP_return", null);
//                                        }
//
//                                        //환불
//                                        sUrl = api_others_refunds_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
//                                        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                            JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                            itemJson.put("RESP_refunds", _bJson.getJSONObject("refunds"));
//                                        } else {
//                                            itemJson.put("RESP_refunds", null);
//                                        }
//
//                                    } else {
//                                        itemJson.put("RESP_cancellation", null);
//                                        itemJson.put("RESP_exchange", null);
//                                        itemJson.put("RESP_return", null);
//                                    }
                                }
                                if (items.size() > 1)
                                    RESP_product_name = RESP_product_name + " 외 " + (items.size() - 1) + "건";

                                oJson.put("RESP_product_name", RESP_product_name);
                                oJson.put("RESP_items", items);
                            } else {
                                oJson.put("RESP_items", null);
                            }

                            // 주문자(Buyer)
                            sUrl = api_orders_buyer_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", order_id);
                            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                                oJson.put("RESP_buyer", bJson.getJSONObject("buyer"));
                            } else {
                                oJson.put("RESP_buyer", null);
                            }

//                            // 수령자(receivers)
//                            sUrl = api_orders_receivers_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", order_id);
//                            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                oJson.put("RESP_receivers", bJson.getJSONArray("receivers"));
//                            } else {
//                                oJson.put("RESP_receivers", null);
//                            }
//
//                            //현금영수증 조회
//                            sUrl = api_orders_cashreceipt_url
//                                    .replaceAll("\\{mallid}", mallId)
//                                    .replaceAll("\\{order_id}", order_id)
//                                    .replaceAll("\\{start_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ssXXX", start_date))
//                                    .replaceAll("\\{end_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ssXXX", end_date));
//                            try {
//                                response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                    JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                    oJson.put("RESP_cashreceipt", bJson.getJSONArray("cashreceipt"));
//                                } else {
//                                    oJson.put("RESP_cashreceipt", null);
//                                }
//                            } catch (org.springframework.web.client.HttpClientErrorException he) {
//                                oJson.put("RESP_cashreceipt", he.getMessage());
//                            }
//
//                            //혜택
//                            sUrl = api_orders_benefits_url
//                                    .replaceAll("\\{mallid}", mallId)
//                                    .replaceAll("\\{order_id}", order_id);
//                            try {
//                                response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                    JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                    oJson.put("RESP_benefits", bJson.getJSONArray("benefits"));
//                                } else {
//                                    oJson.put("RESP_benefits", null);
//                                }
//                            } catch (org.springframework.web.client.HttpClientErrorException he) {
//                                oJson.put("RESP_benefits", he.getMessage());
//                            }
//
//                            //쿠폰
//                            sUrl = api_orders_coupons_url
//                                    .replaceAll("\\{mallid}", mallId)
//                                    .replaceAll("\\{order_id}", order_id);
//                            try {
//                                response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                    JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                    oJson.put("RESP_coupons", bJson.getJSONArray("coupons"));
//                                } else {
//                                    oJson.put("RESP_coupons", null);
//                                }
//                            } catch (org.springframework.web.client.HttpClientErrorException he) {
//                                oJson.put("RESP_coupons", he.getMessage());
//                            }
//
//                            sUrl = api_orders_memo_url
//                                    .replaceAll("\\{mallid}", mallId)
//                                    .replaceAll("\\{order_id}", order_id);
//                            try {
//                                response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
//                                if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
//                                    JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
//                                    oJson.put("RESP_order_memo", bJson.getJSONArray("memos"));
//                                } else {
//                                    oJson.put("RESP_order_memo", null);
//                                }
//                            } catch (org.springframework.web.client.HttpClientErrorException he) {
//                                oJson.put("RESP_order_memo", he.getMessage());
//                            }

                        }
                    }

                    // 회원그룹
                    sUrl = api_customergroups_url
                            .replaceAll("\\{mallid}", mallId);
                    try {
                        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                            JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                            responseJson.put("RESP_customergroups", bJson.getJSONArray("customergroups"));
                        } else {
                            responseJson.put("RESP_customergroups", null);
                        }
                    } catch (org.springframework.web.client.HttpClientErrorException he) {
                        responseJson.put("RESP_customergroups", he.getMessage());
                    }

                    retObj = responseJson;
                } else {
                    retObj = null;
                }
            } else if (response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT) {   // 성공, 응답정보없음
                retObj = null;
            } else {
                retObj = null;
            }
        } else {
            retObj = null;
        }

        return retObj;
    }

    // 주문상세
    public JSONObject ordersDetail(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JsonProcessingException {

        JSONObject retObj = new JSONObject();

        String start_date = jsonParams.getString("start_date");
        String end_date = jsonParams.getString("end_date");
        String srch_order_id = jsonParams.getString("order_id");
        String mallId = jsonParams.getString("mallid");
        String at = jsonParams.getString("at");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Cafe24-Api-Version", X_CAFE24_API_VERSION);
        headers.add("Authorization", "Bearer " + at);
        headers.setContentType(new MediaType("application", "json"));

        // 주문물품정보(items)
        String sUrl = api_others_items_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", srch_order_id);

        sUrl = api_others_items_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", srch_order_id);
        ResponseEntity<String> response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
            JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);

            JSONArray items = bJson.getJSONArray("items");
            String RESP_product_name = "";
            for (int j = 0; j < items.size(); j++) {
                JSONObject itemJson = items.getJSONObject(j);
                if (j == 0) RESP_product_name = itemJson.getString("product_name");
                if (StringUtils.isNotEmpty(itemJson.getString("claim_code")) && !"null".equals(itemJson.getString("claim_code"))) {

                    //취소
                    sUrl = api_others_cancellation_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
                    response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                    if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                        JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        itemJson.put("RESP_cancellation", _bJson.getJSONObject("cancellation"));
                    } else {
                        itemJson.put("RESP_cancellation", null);
                    }

                    //교환
                    sUrl = api_others_exchange_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
                    response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                    if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                        JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        itemJson.put("RESP_exchange", _bJson.getJSONObject("exchange"));
                    } else {
                        itemJson.put("RESP_exchange", null);
                    }

                    //반품
                    sUrl = api_others_return_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
                    response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                    if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                        JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        itemJson.put("RESP_return", _bJson.getJSONObject("return"));
                    } else {
                        itemJson.put("RESP_return", null);
                    }

                    //환불
                    sUrl = api_others_refunds_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{claim_code}", itemJson.getString("claim_code"));
                    response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
                    if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                        JSONObject _bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        itemJson.put("RESP_refunds", _bJson.getJSONObject("refunds"));
                    } else {
                        itemJson.put("RESP_refunds", null);
                    }

                } else {
                    itemJson.put("RESP_cancellation", null);
                    itemJson.put("RESP_exchange", null);
                    itemJson.put("RESP_return", null);
                }
            }
            if (items.size() > 1)
                RESP_product_name = RESP_product_name + " 외 " + (items.size() - 1) + "건";

            retObj.put("RESP_items", items);
        } else {
            retObj.put("RESP_items", null);
        }

        // 수령자(receivers)
        sUrl = api_orders_receivers_url.replaceAll("\\{mallid}", mallId).replaceAll("\\{order_id}", srch_order_id);
        response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
        if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
            JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
            retObj.put("RESP_receivers", bJson.getJSONArray("receivers"));
        } else {
            retObj.put("RESP_receivers", null);
        }

        //현금영수증 조회
        sUrl = api_orders_cashreceipt_url
                .replaceAll("\\{mallid}", mallId)
                .replaceAll("\\{order_id}", srch_order_id)
                .replaceAll("\\{start_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ssXXX", start_date))
                .replaceAll("\\{end_date}", DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd'T'HH:mm:ssXXX", end_date));
        try {
            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                retObj.put("RESP_cashreceipt", bJson.getJSONArray("cashreceipt"));
            } else {
                retObj.put("RESP_cashreceipt", null);
            }
        } catch (org.springframework.web.client.HttpClientErrorException he) {
            retObj.put("RESP_cashreceipt", he.getMessage());
        }

        //혜택
        sUrl = api_orders_benefits_url
                .replaceAll("\\{mallid}", mallId)
                .replaceAll("\\{order_id}", srch_order_id);
        try {
            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                retObj.put("RESP_benefits", bJson.getJSONArray("benefits"));
            } else {
                retObj.put("RESP_benefits", null);
            }
        } catch (org.springframework.web.client.HttpClientErrorException he) {
            retObj.put("RESP_benefits", he.getMessage());
        }

        //혜택
        sUrl = api_orders_coupons_url
                .replaceAll("\\{mallid}", mallId)
                .replaceAll("\\{order_id}", srch_order_id);
        try {
            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                retObj.put("RESP_coupons", bJson.getJSONArray("coupons"));
            } else {
                retObj.put("RESP_coupons", null);
            }
        } catch (org.springframework.web.client.HttpClientErrorException he) {
            retObj.put("RESP_coupons", he.getMessage());
        }

        sUrl = api_orders_memo_url
                .replaceAll("\\{mallid}", mallId)
                .replaceAll("\\{order_id}", srch_order_id);
        try {
            response = paletteLkagRestTemplate.exchange(new URI(sUrl), null, headers, HttpMethod.valueOf("GET"));
            if (response.getStatusCodeValue() == HttpStatus.SC_OK && StringUtils.isNotEmpty(response.getBody())) {
                JSONObject bJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                retObj.put("RESP_order_memo", bJson.getJSONArray("memos"));
            } else {
                retObj.put("RESP_order_memo", null);
            }
        } catch (org.springframework.web.client.HttpClientErrorException he) {
            retObj.put("RESP_order_memo", he.getMessage());
        }


        return retObj;
    }
}