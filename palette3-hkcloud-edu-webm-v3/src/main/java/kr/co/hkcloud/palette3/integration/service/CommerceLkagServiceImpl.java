package kr.co.hkcloud.palette3.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.core.util.PaletteLkagRestTemplate;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.XML;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * packageName    : kr.co.hkcloud.palette3.external.commerce_api.app
 * fileName       : CommerceService
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
@Getter
@RequiredArgsConstructor
public class CommerceLkagServiceImpl implements kr.co.hkcloud.palette3.integration.service.CommerceLkagService {

    private final TwbComDAO mobjDao;
    private final PaletteJsonUtils paletteJsonUtils;
    private final PaletteLkagRestTemplate paletteLkagRestTemplate;
    private final RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository;

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustcoLkagApi(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        if (!redisCacheCustcoLkagRepository.has(jsonParams.getString("LKAG_ID"), jsonParams.getString("CERT_CUSTCO_ID"))) {

            //기본정보
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "selectList", jsonParams);
            jsonParams.setInt("CERT_CUSTCO_LKAG_ID", objRetParams.getInt("CERT_CUSTCO_LKAG_ID"));

            //Headers
            TelewebJSON objHeades = new TelewebJSON(jsonParams);
            objHeades = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "selectHeadersList", jsonParams);
            if (objHeades.getDataObject("DATA").size() > 0) {
                List<Map<String, Object>> tree = null;
                String upId = ((JSONObject) objHeades.getDataObject("DATA").get(0)).getString("UP_ID");
                tree = paletteJsonUtils.convertorTreeMap(objHeades.getDataObject("DATA"), upId, "HEAD_ID", "UP_ID", "PARAM_COL_NM",
                        "HEAD_ID");
                objRetParams.setDataObject("CUSTCO_LKAG_HEADERS", paletteJsonUtils.getJsonArrayFromList(tree));
            }

            //Parameters
            TelewebJSON objParams = new TelewebJSON(jsonParams);
            objParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.custco.dao.LkagCustcoMapper", "treeParamsList", jsonParams);
            if (objParams.getDataObject("DATA").size() > 0) {
                List<Map<String, Object>> tree = null;
                String upId = ((JSONObject) objParams.getDataObject("DATA").get(0)).getString("UP_ARTCL_ID");
                tree = paletteJsonUtils.convertorTreeMap(objParams.getDataObject("DATA"), upId, "PARAM_ARTCL_ID", "UP_ARTCL_ID", "COL_NM",
                        "SORT");
                objRetParams.setDataObject("CUSTCO_LKAG_PARAMS", paletteJsonUtils.getJsonArrayFromList(tree));
            }

            //Response
            objRetParams.setDataObject("CUSTCO_LKAG_RSPNS",
                    mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.response.dao.LkagMngResponseMapper", "selectList", jsonParams));
            redisCacheCustcoLkagRepository.set(jsonParams.getString("LKAG_ID"), jsonParams.getString("CERT_CUSTCO_ID"), objRetParams);
        } else {
            log.info("selectCustcoLkagApi() :: cache :: redisCacheCustcoLkagRepository / LKAG_ID:" + jsonParams.getString("LKAG_ID")
                    + " , CERT_CUSTCO_ID:" + jsonParams.getString("CERT_CUSTCO_ID"));
            objRetParams = redisCacheCustcoLkagRepository.get(jsonParams.getString("LKAG_ID"), jsonParams.getString("CERT_CUSTCO_ID"));
            try {
                objRetParams.setHeader("TELEWEB_IDENTIFIER", jsonParams.getHeaderString("TELEWEB_IDENTIFIER"));
                objRetParams.setHeader("ACCESS_TOKEN", jsonParams.getHeaderString("ACCESS_TOKEN"));
            } catch (Exception e) {
            }
        }
        return objRetParams;
    }

    /* URL 설정, Path Parameter설정. */
    public String getUrl(TelewebJSON jsonParams,
                         TelewebJSON objCustcoLkagApi) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray paramsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_PARAMS");
        if (paramsDataObj == null) {
            paramsDataObj = new JSONArray();
        }

        String sUrl = baseDataObj.getString("LKAG_URI");
        if (paramsDataObj.size() > 0) {
            for (Object item : paramsDataObj) {
                JSONObject obj = (JSONObject) item;
                if( StringUtils.isNotEmpty(obj.getString("RSPNS_LKAG_ID"))) {

                    String colVal = PaletteJsonUtils.findMatchKeyValue(obj.getString("RSPNS_COL_NM"),
                            jsonParams.getDataObject("DATA_RSPNS_LKAG_ID_" + obj.getString("RSPNS_LKAG_ID")));
                    if( colVal == null ) colVal = "null";
                    sUrl = sUrl.replaceAll("\\{" + obj.getString("COL_NM") + "}",
                            paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"),
                                    obj.getString("PARAM_ENCPT_KEY")));
                }else {
                    if (sUrl.contains("{" + obj.getString("COL_NM") + "}")) {
                        String colVal = null;
                        if("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals(obj.getString("DATA_TYPE_CD"))){
                            // 11번가의 경우 date yyyyMMdd형식으로 날짜 변환
                            if("yyyyMMdd".equals(obj.getString("DATA_TYPE_FMT_CD"))){
                                colVal = DateCmmnUtils.getDate( obj.getString("DATA_TYPE_FMT_CD"), jsonParams.getString(obj.getString("COL_NM")));
                            }
                        }else{
                            colVal = jsonParams.getString(obj.getString("COL_NM"));
                        }
                        if (StringUtils.isEmpty(colVal)) {
                            colVal = obj.getString("BSC_VL");
                        }

                        sUrl = sUrl.replaceAll("\\{" + obj.getString("COL_NM") + "}",
                                paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"),
                                        obj.getString("PARAM_ENCPT_KEY")));
                    }
                }
            }
        }

        return sUrl;
    }

    /* 기본헤더설정. */
    public HttpHeaders getHeaders(TelewebJSON jsonParams, TelewebJSON objCustcoLkagApi) {
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray headersDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_HEADERS");
        if (headersDataObj == null) {
            headersDataObj = new JSONArray();
        }

        HttpHeaders headers = new HttpHeaders();
        if ("json".equals(baseDataObj.getString("RQST_DATA_CD_VL")) || "xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
            headers.setContentType(new MediaType("application", baseDataObj.getString("RQST_DATA_CD_VL")));
        } else {
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        }
        if (headersDataObj.size() > 0) {
            for (Object item : headersDataObj) {
                JSONObject obj = (JSONObject) item;
                headers.add(obj.getString("name"), obj.getString("KEY_VL"));  // 데이타연동 전문 테스트요청시.
            }
        }

        return headers;
    }

    /* 파라메터설정. */
    public Object getQuery(TelewebJSON jsonParams,
                           TelewebJSON objCustcoLkagApi) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray paramsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_PARAMS");
        if (paramsDataObj == null) {
            paramsDataObj = new JSONArray();
        }

        Object query = null;

        if (paramsDataObj.size() > 0) {
            // 3.2 Query String인경우
            if ("LPST_QUERY".equals(baseDataObj.getString("PARAM_DLVR_TYPE_CD"))) {
                MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
                for (Object item : paramsDataObj) {
                    JSONObject obj = (JSONObject) item;

                    if (StringUtils.isNotEmpty(jsonParams.getString("custId")) && StringUtils.isNotEmpty(obj.getString("LNKG_TBL_ID"))) {
                        //연동_연결_테이블
                        jsonParams.setString("LNKG_SCHEMA_ID", obj.getString("LNKG_SCHEMA_ID"));
                        jsonParams.setString("LNKG_TBL_NM", obj.getString("LNKG_TBL_NM"));
                        jsonParams.setString("LNKG_COL_NM", obj.getString("LNKG_COL_NM"));
                        jsonParams.setString("LNKG_KEY_COL", obj.getString("LNKG_KEY_COL"));
                        jsonParams.setString("LNKG_ATTR_ID", obj.getString("LNKG_ATTR_ID"));
                        TelewebJSON objTbl = mobjDao.select("kr.co.hkcloud.palette3.integration.dao.CommerceLkagMapper", "selectLnkgTbl",
                                jsonParams);
                        if (objTbl.getHeaderInt("COUNT") > 0) {
                            String endeVal = paletteLkagRestTemplate.endecryptValue(objTbl.getString(obj.getString("LNKG_COL_NM")),
                                    obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY"));
                            if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                                //배열 스트링 AAA,BBB ==> ["AAA","BBB"] 형태로 변경 하여 param세팅
                                String[] cols = endeVal.split(",");
                                parameters.put(obj.getString("COL_NM"), Arrays.asList(cols));
                            } else if(("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals(obj.getString("DATA_TYPE_CD"))) && endeVal.length() == 14){
                                //11번가의 경우 date yyyyMMdd 형식으로 변경해야함.
                                parameters.put(obj.getString("COL_NM"), Collections.singletonList( DateCmmnUtils.getDate( obj.getString("DATA_TYPE_FMT_CD"), endeVal)) );
                            }else{
                                parameters.put(obj.getString("COL_NM"), Collections.singletonList(endeVal));
                            }
                        }
                    } else if (StringUtils.isNotEmpty(jsonParams.getString("custId")) && StringUtils.isNotEmpty(
                            obj.getString("RSPNS_ARTCL_ID"))) {
                        try {
                            // 응답에서 params 변수 채워야하는경우
                            // 이미 jsonParams에 담아둔 DataObject에서 값을 찾아 채워준다.
                            String colVal = PaletteJsonUtils.findMatchKeyValue(obj.getString("RSPNS_COL_NM"),
                                    jsonParams.getDataObject("DATA_RSPNS_LKAG_ID_" + obj.getString("RSPNS_LKAG_ID")));

                            if (StringUtils.isNotEmpty(colVal)) {

                                String endeVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"),
                                        obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY"));

                                if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                                    //배열 스트링 AAA,BBB ==> ["AAA","BBB"] 형태로 변경 하여 param세팅
                                    String[] cols = endeVal.split(",");
                                    parameters.put(obj.getString("COL_NM"), Arrays.asList(cols));
                                } else {
                                    parameters.put(obj.getString("COL_NM"), Collections.singletonList(endeVal));
                                }

                            } else {
                                parameters.put(obj.getString("COL_NM"), null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            parameters.put(obj.getString("COL_NM"), Collections.singletonList(e.getMessage()));
                        }
                    } else {
                        String colVal = jsonParams.getString(obj.getString("COL_NM"));
                        if (StringUtils.isEmpty(colVal)) {
                            colVal = obj.getString("BSC_VL");
                        }

                        colVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"),
                                obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY"));

                        // 네이버인경우  yyyyMMddHHmmss => 2024-04-290T23:59:59.100+09:00
                        try {
                            if (("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals(obj.getString("DATA_TYPE_CD"))) && colVal.length() == 14) {
                                // 날짜 타입 별로 분기
                                // yyyyMMddHHmmss ==> yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
                                if("yyyy-MM-dd'T'HH:mm:ss.SSSXXX".equals(obj.getString("DATA_TYPE_FMT_CD"))){
                                    parameters.put(obj.getString("COL_NM"), Collections.singletonList( DateCmmnUtils.getDateStringToISO8601( obj.getString("DATA_TYPE_FMT_CD"), colVal)) );
                                    // yyyyMMddHHmmss ==> yyyy-MM-dd || yyyyMMddHHmmss ==> yyyyMMdd
                                }else if("yyyyMMdd".equals(obj.getString("DATA_TYPE_FMT_CD")) || "yyyy-MM-dd".equals(obj.getString("DATA_TYPE_FMT_CD"))){
                                    parameters.put(obj.getString("COL_NM"), Collections.singletonList( DateCmmnUtils.getDate( obj.getString("DATA_TYPE_FMT_CD"), colVal)) );
                                }

                            } else if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                                String[] cols = colVal.split(",");
                                parameters.put(obj.getString("COL_NM"), Arrays.asList(cols));
                            } else {
                                parameters.put(obj.getString("COL_NM"), Collections.singletonList(colVal));
                            }
                        } catch (Exception e) {
                            parameters.put(obj.getString("COL_NM"), Collections.singletonList(colVal));
                        }
                    }
                    query = parameters;

                }
                log.info("LPST_QUERY :: {}", parameters.toString());
            }
            // 3.3 BODY 인경우
            if ("LPST_BODY".equals(baseDataObj.getString("PARAM_DLVR_TYPE_CD"))) {
                if ("json".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
                    JSONObject parameters = new JSONObject();
                    if ("LPIT_ARRAY".equals(paramsDataObj.getJSONObject(0).getString("DATA_TYPE_CD"))) {
                        // 롯데온에서는 상품q&a 답변 처리에 array안에 object로 보내야함..
                        String[] val = new String[1];
                        val[0] = String.valueOf(makeParameterBodyJson(jsonParams, paramsDataObj.getJSONObject(0).getJSONArray("children")));
                        parameters.put(paramsDataObj.getJSONObject(0).getString("COL_NM"), val);
                    }else{
                        parameters = this.makeParameterBodyJson(jsonParams, paramsDataObj);
                    }
                    query = parameters.toString();
                    log.info("LPST_BODY :: {}", query);
                } else if ("xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
                    // xml용 구현.
                    // JSON 형태로 선 가공 후 XML로 변환, xml방식일 때 path+body형식이면 파라미터중 object인 파라미터만 body로 변환한다.
                    JSONArray paramDataArr = new JSONArray();
                    for(int i = 0; i < paramsDataObj.size();i++){
                        if("LPIT_OBJECT".equals(paramsDataObj.getJSONObject(i).getString("DATA_TYPE_CD"))){
                            paramDataArr.add(paramsDataObj.getJSONObject(i));
                        };
                    }
                    net.sf.json.JSONObject objJson = this.makeParameterBodyJson(jsonParams, paramDataArr);

                    String jsonStr = objJson.toString();
                    org.json.JSONObject convertJson = new org.json.JSONObject(jsonStr);

                    String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
                    String xmlString = XML.toString(convertJson);
                    // XML 문자열에 헤더 추가
                    StringBuilder buildXml = new StringBuilder();
                    buildXml.append(xmlHeader);
                    buildXml.append(xmlString);
                    log.info("여기서 xml만들었음 ;:::::::::::::::::::::: "+ xmlString);
                    log.info("여기서 xml만들었음 ;:::::::::::::::::::::: "+ buildXml);
                    query = buildXml.toString();
                }
            }
        }

        return query;
    }

    /**
     * parameter BODY json스타일인경우
     */
    public JSONObject makeParameterBodyJson(TelewebJSON jsonParams,
                                            JSONArray paramsDataObj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject parameters = new JSONObject();
        for (Object item : paramsDataObj) {
            JSONObject obj = (JSONObject) item;

            if (!"LPIT_ARRAY".equals(obj.getString("DATA_TYPE_CD")) && !"LPIT_OBJECT".equals(obj.getString("DATA_TYPE_CD"))) {
                //연동_연결_테이블
                if (StringUtils.isNotEmpty(jsonParams.getString("custId")) && StringUtils.isNotEmpty(obj.getString("LNKG_TBL_ID"))) {
                    jsonParams.setString("LNKG_SCHEMA_ID", obj.getString("LNKG_SCHEMA_ID"));
                    jsonParams.setString("LNKG_TBL_NM", obj.getString("LNKG_TBL_NM"));
                    jsonParams.setString("LNKG_COL_NM", obj.getString("LNKG_COL_NM"));
                    jsonParams.setString("LNKG_KEY_COL", obj.getString("LNKG_KEY_COL"));
                    jsonParams.setString("LNKG_ATTR_ID", obj.getString("LNKG_ATTR_ID"));
                    TelewebJSON objTbl = mobjDao.select("kr.co.hkcloud.palette3.integration.dao.CommerceLkagMapper", "selectLnkgTbl",
                            jsonParams);
                    if (objTbl.getHeaderInt("COUNT") > 0) {
                        parameters.put(obj.getString("COL_NM"),
                                paletteLkagRestTemplate.endecryptValue(objTbl.getString(obj.getString("LNKG_COL_NM")),
                                        obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY")));
                    }
                } else if (StringUtils.isNotEmpty(obj.getString("RSPNS_ARTCL_ID"))) {
                    try {
                        //응답에서 params 변수 채워야하는경우
                        String colVal = PaletteJsonUtils.findMatchKeyValue(obj.getString("RSPNS_COL_NM"),
                                jsonParams.getDataObject("DATA_RSPNS_LKAG_ID_" + obj.getString("RSPNS_LKAG_ID")));
                        if (StringUtils.isNotEmpty(colVal)) {

                            String endeVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"),
                                    obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY"));
                            if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                                String[] cols = endeVal.split(",");
                                parameters.put(obj.getString("COL_NM"), cols);
                            } else {
                                parameters.put(obj.getString("COL_NM"), endeVal);
                            }

                        } else {
                            parameters.put(obj.getString("COL_NM"), null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        parameters.put(obj.getString("COL_NM"), e.getMessage());
                    }
                } else {
                    String colVal = jsonParams.getString(obj.getString("COL_NM"));
                    if (StringUtils.isEmpty(colVal)) {
                        colVal = obj.getString("BSC_VL");
                    }

                    String endeVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"),
                            obj.getString("PARAM_ENCPT_MTHD_CD_VL"), obj.getString("PARAM_ENCPT_KEY"));

                    if (("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals( obj.getString("DATA_TYPE_CD"))) && colVal.length() == 14) {
                        //yyyyMMddHHmmss => 정해진 날짜포멧으로.
                        endeVal = Collections.singletonList(DateCmmnUtils.getDateStringToISO8601( obj.getString("DATA_TYPE_FMT_CD"), colVal)).toString();
                        parameters.put(obj.getString("COL_NM"), endeVal.replaceAll("\\[", "").replaceAll("]", ""));
                        //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

                    } else if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                        String[] cols = endeVal.split(",");
                        parameters.put(obj.getString("COL_NM"), cols);
                    } else {
                        parameters.put(obj.getString("COL_NM"), endeVal.replaceAll("\\[", "").replaceAll("]", ""));
                    }
                }


            }

            if ("LPIT_OBJECT".equals(obj.getString("DATA_TYPE_CD"))) {
                parameters.put(obj.getString("COL_NM"), makeParameterBodyJson(jsonParams, obj.getJSONArray("children")));
            }
        }
        return parameters;
    }

    /* 응답 */
    public TelewebJSON getResponse(TelewebJSON objCustcoLkagApi, String sUrl, Object query, HttpHeaders headers, HttpMethod httpMethod) {
        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray rspnsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_RSPNS");
        if (rspnsDataObj == null) {
            rspnsDataObj = new JSONArray();
        }

        Object oldQuery = query;
        if (oldQuery == null) {
            oldQuery = "";
        }
        TelewebJSON objRetRspns = new TelewebJSON();
        try {

            //            if(HttpMethod.GET == httpMethod){
            //                sUrl = sUrl + "?" + multiValueMapToUrlParam( (MultiValueMap<String, String>)query );
            //                query = null;
            //            }
            //
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sUrl);
            if (HttpMethod.GET == httpMethod) {
                if (query != null) {
                    Iterator i = ((Map<String, String>) query).keySet().iterator();
                    while (i.hasNext()) {
                        String key = i.next().toString();
                        Object val = ((Map<String, String>) query).get(key);
                        builder.queryParam(key, URLEncoder.encode(val.toString().replaceAll("\\[", "").replaceAll("]", "")));

                    }
                    query = null;
                }
            }

            UriComponents serviceUri = builder.build(false);
            sUrl = serviceUri.toUriString();

            ResponseEntity<String> response = paletteLkagRestTemplate.exchange(new URI(sUrl), query, headers, httpMethod);
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {

                if (StringUtils.isNotEmpty(response.getBody())) {
                    String contentType = response.getHeaders().getFirst("Content-Type");
                    // xml인 경우 response.body를 org.json의 XML로 org.json.JSONObject로  변환한 후
                    // json String -> net.sf.json.JSONObject 순서로 변환한다.
                    if(contentType != null && contentType.contains("xml")) {
                        org.json.JSONObject xmlJSONObj = XML.toJSONObject(response.getBody());
                        String jsonString = xmlJSONObj.toString();
                        ObjectMapper mapper = new ObjectMapper();

                        net.sf.json.JSONObject convertedXmlToJson = mapper.readValue(jsonString, net.sf.json.JSONObject.class);

                        objRetRspns.setHeader("ERROR_FLAG", false);
                        objRetRspns.setHeader("ERROR_MSG", "");
                        objRetRspns.setDataObject("DATA", convertedXmlToJson);
                        objRetRspns.setDataObject("DATA2", this.makeResponse(baseDataObj, rspnsDataObj,convertedXmlToJson));

                    }else {
                        JSONObject responseJson = (new ObjectMapper()).readValue(response.getBody(), JSONObject.class);
                        objRetRspns.setHeader("ERROR_FLAG", false);
                        objRetRspns.setHeader("ERROR_MSG", "");
                        objRetRspns.setHeader("URI", sUrl);
                        objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
                        objRetRspns.setDataObject("DATA", responseJson);
                        objRetRspns.setDataObject("DATA2", this.makeResponse(baseDataObj, rspnsDataObj, responseJson));
                    }
                } else {
                    objRetRspns.setHeader("ERROR_FLAG", false);
                    objRetRspns.setHeader("ERROR_MSG", "");
                    objRetRspns.setHeader("URI", sUrl);
                    objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
                    objRetRspns.setDataObject("DATA", new JSONObject());
                    objRetRspns.setDataObject("DATA2", new JSONObject());
                }
            } else if (response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT) {   // 성공, 응답정보없음
                objRetRspns.setHeader("ERROR_FLAG", false);
                objRetRspns.setHeader("ERROR_MSG", "성공, 응답내용없음");
                objRetRspns.setHeader("URI", sUrl);
                objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
                objRetRspns.setDataObject("DATA", new JSONObject());
                objRetRspns.setDataObject("DATA2", new JSONObject());
            } else {
                objRetRspns.setHeader("ERROR_FLAG", true);
                objRetRspns.setHeader("URI", sUrl);
                objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
                objRetRspns.setHeader("ERROR_MSG", response.getBody());
            }

            log.info("response :: {}", objRetRspns.toString());

        } catch (ResourceAccessException e) {
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("URI", sUrl);
            objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
            objRetRspns.setHeader("ERROR_MSG", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            objRetRspns.setHeader("ERROR_FLAG", true);
            objRetRspns.setHeader("URI", sUrl);
            objRetRspns.setHeader("PARAMETERS", oldQuery.toString());
            objRetRspns.setHeader("ERROR_MSG", e.getMessage());
        }
        return objRetRspns;
    }

    /* 응답 구조 유지  */
    public JSONArray makeResponse(JSONObject baseDataObj, JSONArray rspnsDataObj,
                                  JSONObject obj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONArray returnArray = new JSONArray();
        JSONObject sub = new JSONObject();
        Iterator i = obj.keys();
        while (i.hasNext()) {
            String key = i.next().toString();
            Object valueObj = obj.get(key);
            log.info("valueObj.getClass().getName()" + valueObj.getClass().getName());
            if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {    //배열타입
                JSONArray _tmpValueObj = (JSONArray) valueObj;
                if (_tmpValueObj.size() > 0) {
                    if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName()) || "java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName())) {
                        // 응답데이타중 ["aaa","bbb"] 스타일    => aaa,bbb
                        sub.put(key, makeResponseSubArrayString(baseDataObj, rspnsDataObj, _tmpValueObj, key));
                    } else {
                        // [{"a1":"aaa"},{"b1":"bbb"}] 스타일
                        sub.put(key, makeResponseSubArray(baseDataObj, rspnsDataObj, _tmpValueObj, key));
                    }
                }

            } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {    //Object타입
                sub.put(key, makeResponseSubObj(baseDataObj, rspnsDataObj, (net.sf.json.JSONObject) valueObj, key));
            } else {
                if (rspnsDataObj.size() > 0) {
                    JSONObject matchObj = PaletteJsonUtils.findMatchDataObj(key, "FULL_PATH_DOT", rspnsDataObj);
                    if (matchObj.size() > 0) {//
                        sub.put(key, paletteLkagRestTemplate.endecryptValue(valueObj.toString(), matchObj.getString("ENCPT_YN"),
                                matchObj.getString("RSPNS_DECPT_MTHD_CD_VL"), matchObj.getString("RSPNS_DECPT_KEY")));

                        if (StringUtils.isNotEmpty(matchObj.getString("LKAG_GROUP_CD_ID"))) {  //값이 연동코드인경우 처리.
                            sub.put(key, this.selectLnkgCommonCodeInfo(matchObj.getString("LKAG_GROUP_CD_ID"), sub.getString(key)));
                        }

                        try {
                            //날짜포맷 변경.
                            if ("LPIT_DATETIME".equals(matchObj.getString("DATA_TYPE_CD")) && StringUtils.isNotEmpty(
                                    matchObj.getString("DATA_TYPE_FMT_CD"))) {
                                if (StringUtils.isNotEmpty(sub.getString(key)) && !"null".equals(sub.getString(key))) {

                                    //yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ==> yyyyMMddHHmmss
                                    sub.put(key, DateCmmnUtils.getISO8601ToDateString(matchObj.getString("DATA_TYPE_FMT_CD"), sub.getString(key)));
                                }
                            }
                        } catch (Exception e) {
                        }

                    } else {
                        sub.put(key, valueObj);
                    }
                } else {
                    sub.put(key, valueObj);
                }
            }
        }
        returnArray.add(sub);
        return returnArray;
    }

    public JSONObject makeResponseSubObj(JSONObject baseDataObj, JSONArray rspnsDataObj, JSONObject obj,
                                         String path) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject sub = new JSONObject();
        Iterator i = obj.keys();
        while (i.hasNext()) {
            String key = i.next().toString();
            Object valueObj = obj.get(key);
            if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                JSONArray _tmpValueObj = (JSONArray) valueObj;
                if(_tmpValueObj.size() > 0) {
                    if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName()) || "java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName())) {
                        // 응답데이타중 ["aaa","bbb"] 스타일    => aaa,bbb
                        sub.put(key, makeResponseSubArrayString(baseDataObj, rspnsDataObj, _tmpValueObj, path + "." + key));
                    } else {
                        sub.put(key, makeResponseSubArray(baseDataObj, rspnsDataObj, _tmpValueObj, path + "." + key));
                    }
                }
            } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                sub.put(key, makeResponseSubObj(baseDataObj, rspnsDataObj, (net.sf.json.JSONObject) valueObj, path + "." + key));
            } else {
                JSONObject matchObj = PaletteJsonUtils.findMatchDataObj(path + "." + key, "FULL_PATH_DOT",
                        rspnsDataObj); //응답전문에서 값 비교하여 객체찾기.
                if (matchObj.size() > 0) {
                    sub.put(key, paletteLkagRestTemplate.endecryptValue(valueObj.toString(), matchObj.getString("ENCPT_YN"),
                            matchObj.getString("RSPNS_DECPT_MTHD_CD_VL"), matchObj.getString("RSPNS_DECPT_KEY")));

                    if (StringUtils.isNotEmpty(matchObj.getString("LKAG_GROUP_CD_ID"))) {  //값이 연동코드인경우 처리.
                        sub.put(key, this.selectLnkgCommonCodeInfo(matchObj.getString("LKAG_GROUP_CD_ID"), sub.getString(key)));
                    }

                    //날짜포맷 변경.
                    if ("LPIT_DATETIME".equals(matchObj.getString("DATA_TYPE_CD")) && StringUtils.isNotEmpty(
                            matchObj.getString("DATA_TYPE_FMT_CD"))) {
                        if (StringUtils.isNotEmpty(sub.getString(key)) && !"null".equals(sub.getString(key))) {
                            //yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ==> yyyyMMddHHmmss
                            sub.put(key, DateCmmnUtils.getISO8601ToDateString(matchObj.getString("DATA_TYPE_FMT_CD"), sub.getString(key)));
                        }
                    }
                } else {
                    sub.put(key, valueObj);
                }

                sub.put(path + "." + key, sub.get(key));
            }

        }
        return sub;
    }

    public JSONArray makeResponseSubArray(JSONObject baseDataObj, JSONArray rspnsDataObj, JSONArray arrayObj,
                                          String path) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONArray returnArray = new JSONArray();
        if (arrayObj.size() > 0) {
            for (Object item : arrayObj) {
                JSONObject sub = new JSONObject();
                JSONObject tempObj = (JSONObject) item;
                Iterator i = tempObj.keys();
                while (i.hasNext()) {
                    String key = i.next().toString();
                    Object valueObj = tempObj.get(key);
                    if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                        JSONArray _tmpValueObj = (JSONArray) valueObj;
                        if (_tmpValueObj.size() > 0) {
                            if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName()) || "java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName()) ) {
                                // 응답데이타중 ["aaa","bbb"] 스타일    => aaa,bbb
                                sub.put(key, makeResponseSubArrayString(baseDataObj, rspnsDataObj, _tmpValueObj, path + "." + key));
                            } else {
                                // [{"a1":"aaa"},{"b1":"bbb"}] 스타일
                                sub.put(key, makeResponseSubArray(baseDataObj, rspnsDataObj, _tmpValueObj, path + "." + key));
                            }
                        }
                    } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                        sub.put(key, makeResponseSubObj(baseDataObj, rspnsDataObj, (net.sf.json.JSONObject) valueObj, path + "." + key));
                    } else {
                        JSONObject matchObj = PaletteJsonUtils.findMatchDataObj(path + "." + key, "FULL_PATH_DOT",
                                rspnsDataObj); //응답전문에서 값 비교하여 객체찾기.
                        if (matchObj.size() > 0) {
                            sub.put(key, paletteLkagRestTemplate.endecryptValue(valueObj.toString(), matchObj.getString("ENCPT_YN"),
                                    matchObj.getString("RSPNS_DECPT_MTHD_CD_VL"), matchObj.getString("RSPNS_DECPT_KEY")));

                            if (StringUtils.isNotEmpty(matchObj.getString("LKAG_GROUP_CD_ID"))) {  //값이 연동코드인경우 처리.
                                sub.put(key, this.selectLnkgCommonCodeInfo(matchObj.getString("LKAG_GROUP_CD_ID"), sub.getString(key)));
                            }

                            try {
                                if ("LPIT_DATETIME".equals(matchObj.getString("DATA_TYPE_CD")) && StringUtils.isNotEmpty(
                                        matchObj.getString("DATA_TYPE_FMT_CD"))) {
                                    if (StringUtils.isNotEmpty(sub.getString(key)) && !"null".equals(sub.getString(key))) {
                                        //yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ==> yyyyMMddHHmmss
                                        sub.put(key, DateCmmnUtils.getISO8601ToDateString(matchObj.getString("DATA_TYPE_FMT_CD"), sub.getString(key)));
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            sub.put(key, sub.get(key));
                        }
                    }
                }
                returnArray.add(sub);
            }
        }
        return returnArray;
    }

    public String makeResponseSubArrayString(JSONObject baseDataObj, JSONArray rspnsDataObj, JSONArray arrayObj,
                                             String path) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String returnString = "";
        if (arrayObj.size() > 0) {
            for (int i = 0; i < arrayObj.size(); i++) {
                returnString +=  arrayObj.get(i).toString() + ",";
            }
            returnString = returnString.substring(0, returnString.length() - 1);
        }
        return returnString;
    }

    /* 연동코드 조회  */
    public String selectLnkgCommonCodeInfo(String groupCdId, String cdVal) {
        TelewebJSON objParamCd = new TelewebJSON();
        objParamCd.setString("LKAG_GROUP_CD_ID", groupCdId);
        objParamCd.setString("LKAG_CD_VL", cdVal);
        TelewebJSON objCommCd = mobjDao.select("kr.co.hkcloud.palette3.integration.dao.CommerceLkagMapper", "selectLnkgCommonCodeInfo",
                objParamCd);
        if (objCommCd.getHeaderInt("COUNT") > 0) {
            return objCommCd.getString("LKAG_CD_NM");
        } else {
            return cdVal;
        }
    }

}
