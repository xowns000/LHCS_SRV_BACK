package kr.co.hkcloud.palette3.integration.commerce_api.ezadmin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
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
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.goodwearmall.app
 * fileName       : GoodWearMallService
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
@Service(EzadminServiceImpl.BEAN_ID)
public class EzadminServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {
    public static final String BEAN_ID = "EZADMIN";

    @Autowired
    private PaletteLkagRestTemplate paletteLkagRestTemplate;

    public EzadminServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
                              RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository) {
        super(mobjDao, paletteJsonUtils, paletteLkagRestTemplate, redisCacheCustcoLkagRepository);
    }

    @Override
    public TelewebJSON oauthCode(TelewebJSON jsonParams) throws TelewebAppException {
        return jsonParams;
    }

    @Override
    public TelewebJSON sample_authentication( TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        return objRetParams;
    }

    @Override
    public TelewebJSON authentication(TelewebJSON jsonParams, JSONObject baseDataObj) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        log.info(BEAN_ID + " >> authentication ");
        //objRetParams = this.selectCustcoLkagApi( jsonParams );

        return objRetParams;
    }
    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi( jsonParams );
        JSONObject baseDataObj = (JSONObject)objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray rspnsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_RSPNS");
        if (rspnsDataObj == null) {
            rspnsDataObj = new JSONArray();
        }


        JSONObject jsonParamsObj = (JSONObject) jsonParams.getDataObject(TwbCmmnConst.G_DATA).get(0);
        jsonParamsObj.put("sUrl", baseDataObj.getString("LKAG_URI"));
        jsonParamsObj.put("partner_key", baseDataObj.getString("ACS_ACNT_KEY"));
        jsonParamsObj.put("domain_key", baseDataObj.getString("ACS_SECTY_KEY"));
        jsonParamsObj.put("action", "get_order_info");
        jsonParamsObj.put("date_type", "order_date");
        TelewebJSON objRetRspns = new TelewebJSON();
        if( (jsonParamsObj.has("srch_mng_seq") && StringUtils.isEmpty( jsonParamsObj.getString("srch_mng_seq") ) ) && StringUtils.isEmpty( jsonParamsObj.getString("buyer_cellphone") ) ) {
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
            objRetRspns.setHeader("ERROR_MSG", "휴대전화 또는 관리번호는 필수 입니다.");
        }else {

            try {
                ResponseEntity<String> response = this.getActionInfo( jsonParamsObj );
                if (response.getStatusCodeValue() == HttpStatus.SC_OK) {

                    if (StringUtils.isNotEmpty(response.getBody())) {
                        JSONObject responseJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        responseJson.put("size", responseJson.get("total"));
                        JSONArray orders = responseJson.getJSONArray("data");
                        if (orders.size() > 0) {
                            // 관리번호별 cs이력 세팅
                            for (int i = 0; i < orders.size(); i++) {
                                JSONObject oJson = orders.getJSONObject(i);
                                jsonParamsObj.put("action", "get_cs_info");
                                jsonParamsObj.put("date_type", "crdate");
                                jsonParamsObj.put("seq", oJson.getString("seq"));
                                oJson.put("RESP_cs",this.getEtcInfo(jsonParamsObj));
                            }
                        }

                        //기타(배송업체코드)조회
                        jsonParamsObj.put("action", "get_etc_info");
                        jsonParamsObj.put("date_type", "reg_date");
                        jsonParamsObj.put("search_type", "transinfo");
                        responseJson.put("RESP_trans_corp", this.getEtcInfo(jsonParamsObj) );

                        objRetRspns.setHeader("ERROR_FLAG", false);
                        objRetRspns.setHeader("ERROR_MSG", "");
                        objRetRspns.setDataObject("DATA", responseJson);
                    } else {
                        objRetRspns.setHeader("ERROR_FLAG", false);
                        objRetRspns.setHeader("ERROR_MSG", "");
                        objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                        objRetRspns.setDataObject("DATA", new JSONObject());
                    }
                } else if (response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT) {   // 성공, 응답정보없음
                    objRetRspns.setHeader("ERROR_FLAG", false);
                    objRetRspns.setHeader("ERROR_MSG", "성공, 응답내용없음");
                    objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                    objRetRspns.setDataObject("DATA", new JSONObject());
                } else {
                    objRetRspns.setHeader("ERROR_FLAG", true);
                    objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                    objRetRspns.setHeader("ERROR_MSG", response.getBody());
                }
            } catch (ResourceAccessException e) {
                objRetRspns.setHeader("ERROR_FLAG", true);
                objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                objRetRspns.setHeader("ERROR_MSG", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                objRetRspns.setHeader("ERROR_FLAG", true);
                objRetRspns.setHeader("URI", baseDataObj.getString("LKAG_URI"));
                objRetRspns.setHeader("ERROR_MSG", e.getMessage());
            }
        }

        return objRetRspns;
    }

    public ResponseEntity<String> getActionInfo( JSONObject jsonParamsObj ) throws URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String sUrl = jsonParamsObj.getString("sUrl");
        sUrl += "?limit=1000";
        sUrl += "&action=" +  jsonParamsObj.getString("action");
        sUrl += "&date_type=" + jsonParamsObj.getString("date_type");
        sUrl += "&partner_key=" + jsonParamsObj.getString("partner_key");
        sUrl += "&domain_key=" + jsonParamsObj.getString("domain_key");
        sUrl += "&start_date=" + DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd", jsonParamsObj.getString("start_date"));
        sUrl += "&end_date=" + DateCmmnUtils.getDateStringToISO8601("yyyy-MM-dd", jsonParamsObj.getString("end_date"));

        if( "get_order_info".equals(jsonParamsObj.getString("action")) ) {  //주문조회인경우.
            if (StringUtils.isNotEmpty(jsonParamsObj.getString("buyer_cellphone"))) {
                sUrl += "&tel=" + jsonParamsObj.getString("buyer_cellphone");
            }
            try {
                if (StringUtils.isNotEmpty(jsonParamsObj.getString("srch_mng_seq"))) {
                    sUrl += "&seq=" + jsonParamsObj.getString("srch_mng_seq");
                }
            } catch (Exception e) {
                jsonParamsObj.put("srch_mng_seq", "");
            }
            try {
                if (StringUtils.isNotEmpty(jsonParamsObj.getString("order_id"))) {
                    sUrl += "&order_id=" + jsonParamsObj.getString("order_id");
                }
            } catch (Exception e) {
                jsonParamsObj.put("order_id", "");
            }

        }else if( "get_etc_info".equals(jsonParamsObj.getString("action")) ) {  //기타정보조회인 경우 .
            sUrl += "&search_type=" + jsonParamsObj.getString("search_type");
        }else if( "get_cs_info".equals(jsonParamsObj.getString("action")) ) {  //CS이력조회인 경우 .
            sUrl += "&seq=" + jsonParamsObj.getString("seq");
        }

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> response = paletteLkagRestTemplate.exchange(new URI(sUrl), parameters, headers, HttpMethod.valueOf("GET"));
        return response;
    }

    public JSONObject getEtcInfo( JSONObject jsonParamsObj ) throws URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JsonProcessingException {
        JSONObject retObj;
        ResponseEntity<String> response = this.getActionInfo( jsonParamsObj );
        if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
            if (StringUtils.isNotEmpty(response.getBody())) {
                retObj = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
            }else {
                retObj = null;
            }
        }else {
            retObj = null;
        }
        return retObj;
    }

    @Override
    public TelewebJSON call_batch_api(
        TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON retObj = new TelewebJSON(jsonParams);
        retObj = this.call_api( jsonParams );
        log.info("response :: {}", retObj.toString());
        if( jsonParams.getDataObject("DATA2") != null ) {

        }
        return null;
    }
}
