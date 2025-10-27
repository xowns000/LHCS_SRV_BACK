package kr.co.hkcloud.palette3.integration.commerce_api.naver.service;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.core.util.PaletteLkagRestTemplate;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.integration.service.CommerceApiService;
import kr.co.hkcloud.palette3.integration.service.CommerceLkagServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.naver.app
 * fileName       : SmartStoreServiceImpl
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
@Service(NaverServiceImpl.BEAN_ID)
public class NaverServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {

    public static final String BEAN_ID = "NAVER";

    @Value("${commerce.api.naver.auth_url}")
    private String AUTH_URL;

    public NaverServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
        RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository) {
        super(mobjDao, paletteJsonUtils, paletteLkagRestTemplate, redisCacheCustcoLkagRepository);
    }

    @Override
    public TelewebJSON oauthCode(TelewebJSON jsonParams) throws TelewebAppException {
        return jsonParams;
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

    @Override
    public TelewebJSON sample_authentication( TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );

        String clientId = "1IWAdgoBKS40BigXAcijl8";   //제공된 애플리케이션 ID
       String clientSecret = "$2a$04$EFovI3BLhYs2Ql53K0A5/e";
        String accountId =   "puliodays@gmail.com"; //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID

//        String clientId = "27p2KaCJ0x4dXALj2cU2Ad";   //제공된 애플리케이션 ID
//        String clientSecret = "$2a$04$vVAlxnhx.UnopcUogfJet.";
//        String accountId =  "skawns6465"; //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
        String oauthUrl =  AUTH_URL;
        Long timestamp = System.currentTimeMillis(); // 1643961623299L로 가정

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", clientId);  //7dMvteboKNHwyRremLXXXX
        parameters.add("timestamp", timestamp.toString() );
        parameters.add("grant_type", "client_credentials");
        parameters.add("client_secret_sign", this.generateSignature(clientId, clientSecret, timestamp));    //전자서명 생성 방법을 따라 생성된 전자서명
        parameters.add("type", "SELF");
        parameters.add("account_id", accountId);    //ncp_2sRZTWJVbDtHPoz9OXXXX
        objRetParams = super.getResponse( new TelewebJSON(), oauthUrl, parameters, headers, HttpMethod.valueOf("POST") );

        log.info(BEAN_ID + " >> sample_authentication {}", objRetParams.toString());
        return objRetParams;
    }

    /**
     * 네이버 인증 토큰 발급 요청
     * API 활용을 위한 인증 토큰을 발급/갱신 요청합니다.
     * 동일 리소스(type과 account_id에 따라)에는 하나의 인증 토큰의 발급되며 인증 토큰의 유효 시간은 3시간(10,800초)입니다.
     * 발급된 인증 토큰이 존재하는 경우 남은 유효 시간이 30분 이상이면 기존 인증 토큰이 반환되고 30분 미만이면 새로운 인증 토큰이 발급됩니다.
     * 새로운 인증 토큰이 발급된 경우에도 유효 시간이 만료되기 전까지는 기존 인증 토큰을 사용할 수 있습니다.
     *
     * 발급받은 토큰은 레디스에 윺효시간 3시간으로 담아두었다가 위에 방법대로 발급, 재발급 요청합니다.
     *
     * @param jsonParams
     * @param baseDataObj
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON authentication( TelewebJSON jsonParams, JSONObject baseDataObj) throws TelewebAppException {
        if( baseDataObj == null ) {
            TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi( jsonParams );
            baseDataObj = (JSONObject)objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        }

        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        String clientId = baseDataObj.getString("ACS_ACNT_KEY");   //제공된 애플리케이션 ID
        String clientSecret = baseDataObj.getString("ACS_SECTY_KEY");
        String accountId =  baseDataObj.getString("ETC_01"); //type이 SELLER인 경우 입력해야 하는 판매자 ID 혹은 판매자 UID
        Long timestamp = System.currentTimeMillis(); // 1643961623299L로 가정

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", clientId);  //7dMvteboKNHwyRremLXXXX
        parameters.add("timestamp", timestamp.toString() );
        parameters.add("grant_type", "client_credentials");
        parameters.add("client_secret_sign", this.generateSignature(clientId, clientSecret, timestamp));    //전자서명 생성 방법을 따라 생성된 전자서명
        parameters.add("type", "SELF");
        parameters.add("account_id", accountId);    //ncp_2sRZTWJVbDtHPoz9OXXXX
        objRetParams = super.getResponse( new TelewebJSON(), AUTH_URL, parameters, headers, HttpMethod.valueOf("POST") );

        log.info(BEAN_ID + " >> authentication {}", objRetParams.toString());
        return objRetParams;
    }

    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi( jsonParams );
        JSONObject baseDataObj = (JSONObject)objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONObject authenticationObj = (JSONObject)this.authentication( jsonParams, baseDataObj).getDataObject(TwbCmmnConst.G_DATA).get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authenticationObj.getString("access_token") );
        if ("json".equals(baseDataObj.getString("RQST_DATA_CD_VL")) || "xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
            headers.setContentType(new MediaType("application", baseDataObj.getString("RQST_DATA_CD_VL")));
        } else {
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        }

        String sUrl = super.getUrl( jsonParams, objCustcoLkagApi );

        // 3. Parameter 설정
        Object query = super.getQuery( jsonParams, objCustcoLkagApi );
        // 응답.
        TelewebJSON objRetRspns = super.getResponse( objCustcoLkagApi, sUrl, query, headers, HttpMethod.valueOf(baseDataObj.getString("RQST_SE_CD_VL").toUpperCase()) );
        return objRetRspns;
    }

    //BBS 배치(고객문의, 상품문의) 전자상거래_문의_게시판 포멧이 맞추어 리턴. 처리용.
    @Override
    public TelewebJSON call_batch_api(
        TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {



        TelewebJSON retObj = new TelewebJSON(jsonParams);
        JSONObject pageable = new JSONObject();

        JSONArray returnArray = new JSONArray();
        TelewebJSON receiveObj = new TelewebJSON(jsonParams);
        if(StringUtils.isEmpty(jsonParams.getString("answered"))) { // (고객문의,상품문의 공통) 답변 여부
            jsonParams.setString("answered", "false");
        }

        // API호출.
        receiveObj = this.call_api( jsonParams );
        if( receiveObj.getDataObject("DATA2") != null ) {

            JSONObject responseObj = (JSONObject) receiveObj.getDataObject("DATA2").get(0);
            // 고객문의 INQRY_GDS , 상품문의 : INQRY_CUST

            JSONArray contensArray = new JSONArray();
            if ("INQRY_CUST".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {

                pageable = (JSONObject)responseObj.get("pageable");
                pageable.put("totalPages",responseObj.get("totalPages"));
                pageable.put("totalElements",responseObj.get("totalElements"));

                //고객문의
                contensArray =  (JSONArray)responseObj.get("content");
                if(  contensArray !=null && contensArray.size() > 0 ) {
                    for( Object o : contensArray ) {
                        JSONObject sub = new JSONObject();
                        JSONObject contenObj = (JSONObject)o;

                        sub.put("BEAN_ID", "NAVER" );
                        sub.put("BEAN_TP", "CUST" );
                        sub.put("SNDR_KEY", jsonParams.getString("SNDR_KEY") ); //발송자_키
                        sub.put("INQRY_NO", contenObj.getString("inquiryNo") ); //문의번호
                        sub.put("INQRY_TYPE", contenObj.getString("category")); //문의유형
                        sub.put("ORDR_NO", contenObj.getString("orderId")); //주문번호
                        sub.put("GDS_NO", contenObj.getString("productNo")); //상품번호
                        sub.put("GDS_NM", contenObj.getString("productName")); //상품명

                        sub.put("WRTR_ID", contenObj.getString("customerId")); // 작성자아이디
                        sub.put("WRTR_NM", contenObj.getString("customerName")); // 작성자아이디
                        sub.put("WRT_DT", contenObj.getString("inquiryRegistrationDateTime")); // 작성일시.
                        sub.put("INQRY_TTL", contenObj.getString("title")); // 문의제목.
                        sub.put("INQRY_CN", contenObj.getString("inquiryContent")); // 문의내용.
                        sub.put("ETC_1", contenObj.getString("productOrderIdList")); // /상품 주문 ID 목록(여러 개의 상품 주문에 대해 문의했을 경우 각각의 상품 주문 ID가 ','로 구분되어 출력됨)

                        returnArray.add(sub);

                    }
                }
            } else if ("INQRY_GDS".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {
                pageable = new JSONObject();
                pageable.put("page",responseObj.get("page"));
                pageable.put("size",responseObj.get("size"));
                pageable.put("totalPages",responseObj.get("totalPages"));
                pageable.put("totalElements",responseObj.get("totalElements"));

                //상품문의
                contensArray =  (JSONArray)responseObj.get("contents");
                if( contensArray !=null && contensArray.size() > 0 ) {
                    for( Object o : contensArray ) {
                        JSONObject sub = new JSONObject();
                        JSONObject contenObj = (JSONObject)o;

                        sub.put("BEAN_ID", "NAVER" );
                        sub.put("BEAN_TP", "GDS" );
                        sub.put("SNDR_KEY", jsonParams.getString("SNDR_KEY") ); //발송자_키
                        sub.put("INQRY_NO", contenObj.getString("questionId") ); //문의번호
                        sub.put("GDS_NO", contenObj.getString("productId")); //상품번호
                        sub.put("GDS_NM", contenObj.getString("productName")); //상품명
                        sub.put("WRTR_ID", contenObj.getString("maskedWriterId")); // 작성자아이디
                        sub.put("WRT_DT", contenObj.getString("createDate")); // 작성일시.
                        sub.put("INQRY_CN", contenObj.getString("question")); // 문의내용.

                        returnArray.add(sub);

                    }
                }
            }
        }
        retObj.setDataObject("DATA", returnArray);
        retObj.setDataObject("pageable", pageable);
        return retObj;
    }


}
