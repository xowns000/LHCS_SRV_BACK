package kr.co.hkcloud.palette3.admin.lkag.conn.test.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.util.PaletteLkagRestTemplate;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.json.XML;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.conn.app
 * fileName       : LkagConnTestServiceImpl
 * author         : KJD
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
@Service("LkagConnTestService")
public class LkagConnTestServiceImpl implements LkagConnTestService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final PaletteLkagRestTemplate paletteLkagRestTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public TelewebJSON executeTest(
        TelewebJSON jsonParams) throws TelewebAppException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, JsonProcessingException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //--------------------------------------------------------------------------------------
        // 0.
        String headersData = jsonParams.getString("headersData").replaceAll("&#91;", "[").replaceAll("&#93;", "]");
        String paramsData = jsonParams.getString("paramsData").replaceAll("&#91;", "[").replaceAll("&#93;", "]");
        if (StringUtils.isEmpty(headersData)) {
            headersData = "[]";
        }
        if (StringUtils.isEmpty(paramsData)) {
            paramsData = "[]";
        }
        JSONObject baseDataObj = (JSONObject) jsonParams.getObject("baseData");
        JSONArray headersDataObj = JSONArray.fromObject(headersData);
        JSONArray paramsDataObj = JSONArray.fromObject(paramsData);

        //--------------------------------------------------------------------------------------
        // 2. 헤더 설정.
        HttpHeaders headers = new HttpHeaders();
        if ("json".equals(baseDataObj.getString("RQST_DATA_CD_VL"))
//                || "xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))
        ) {
            headers.setContentType(new MediaType("application", baseDataObj.getString("RQST_DATA_CD_VL")));
        } else if("xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))){
            headers.setContentType(new MediaType("text", baseDataObj.getString("RQST_DATA_CD_VL")));
        }else {
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        }
        if (headersDataObj.size() > 0) {
            for (Object item : headersDataObj) {
                JSONObject obj = (JSONObject) item;
                headers.add(obj.getString("name"), obj.getString("KEY_VL"));  // 데이타연동 전문 테스트요청시.
            }
        }

        //--------------------------------------------------------------------------------------
        // 3. Parameter 설정
        Object query = null;
        // 3.1 Path Parameter 설정. 공통적용.
        String sUrl = baseDataObj.getString("LKAG_URI");
        if (paramsDataObj.size() > 0) {
            for (Object item : paramsDataObj) {
                JSONObject obj = (JSONObject) item;
                if (sUrl.contains("{" + obj.getString("COL_NM") + "}")) {
                    sUrl = sUrl.replaceAll("\\{" + obj.getString("COL_NM") + "}",
                        paletteLkagRestTemplate.endecryptValue(obj.getString("BSC_VL"), obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"),
                            obj.getString("PARAM_ENCPT_KEY")));
                }
            }
            // 3.2 Query String인경우
            if ("LPST_QUERY".equals(baseDataObj.getString("PARAM_DLVR_TYPE_CD"))) {
                MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
                for (Object item : paramsDataObj) {
                    JSONObject obj = (JSONObject) item;
                    String colVal = jsonParams.getString(obj.getString("COL_NM"));
                    if (StringUtils.isEmpty(colVal)) {
                        colVal = obj.getString("BSC_VL");
                    }

                    String endeVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"),
                        obj.getString("PARAM_ENCPT_KEY"));

                    if (("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals(obj.getString("DATA_TYPE_CD")))
                        && colVal.length() == 14) { //yyyyMMddHHmmss => 정해진 날짜포멧으로.

                        LocalDateTime date1 = LocalDateTime.parse(colVal, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                        parameters.put(obj.getString("COL_NM"), Collections.singletonList(
                            (new SimpleDateFormat(obj.getString("DATA_TYPE_FMT_CD")).format(Date.from(date1.toInstant(ZoneOffset.of("+09:00"))))).toString()));
                        //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

                    } else if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                        String[] cols = endeVal.split(",");
                        parameters.put(obj.getString("COL_NM"), Arrays.asList(cols));
                    } else {
                        parameters.put(obj.getString("COL_NM"), Collections.singletonList(endeVal));
                    }
                    query = parameters;

                }
            }
            // 3.3 BODY 인경우
            if ("LPST_BODY".equals(baseDataObj.getString("PARAM_DLVR_TYPE_CD"))) {
                if ("json".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
                    JSONObject parameters = this.makeParameterBodyJson(jsonParams, paramsDataObj);
                    query = parameters.toString();
                } else if ("xml".equals(baseDataObj.getString("RQST_DATA_CD_VL"))) {
                    // xml용 구현.
                    // JSON 형태로 선 가공 후 XML로 변환
                    JSONArray paramDataArr = new JSONArray();
                    for(int i = 0; i < paramsDataObj.size();i++){
                        if("LPIT_OBJECT".equals(paramsDataObj.getJSONObject(i).getString("DATA_TYPE_CD"))){
                            paramDataArr.add(paramsDataObj.getJSONObject(i));
                        };
                    }
                    net.sf.json.JSONObject objJson = this.makeParameterBodyJson(jsonParams, paramDataArr);

                    String jsonStr = objJson.toString();
                    org.json.JSONObject convertJson = new org.json.JSONObject(jsonStr);

                    String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                    String xmlString = XML.toString(convertJson);
                    // XML 문자열에 헤더 추가
                    StringBuilder buildXml = new StringBuilder();
                    buildXml.append(xmlHeader);
                    buildXml.append(xmlString);

//                    log.info(" ;:::::::::::::::::::::: "+ jsonParams.toString());
                    log.info("여기서 xml만들었음 ;:::::::::::::::::::::: "+ xmlString);
                    log.info("여기서 xml만들었음 ;:::::::::::::::::::::: "+ buildXml);

                    query = buildXml.toString();
                }
            }
        }

        try {

            HttpMethod httpMethod = HttpMethod.valueOf(baseDataObj.getString("RQST_SE_CD_VL").toUpperCase());

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
                    if(contentType != null && contentType.contains("xml")) {

                        org.json.JSONObject xmlJSONObj = XML.toJSONObject(response.getBody());
                        String jsonString = xmlJSONObj.toString();

                        ObjectMapper mapper = new ObjectMapper();

                        net.sf.json.JSONObject jsonObj = mapper.readValue(jsonString, net.sf.json.JSONObject.class);

                        objRetParams.setHeader("ERROR_FLAG", false);
                        objRetParams.setHeader("ERROR_MSG", "");
                        objRetParams.setDataObject("DATA", jsonObj);
                        objRetParams.setDataObject("TREE", this.makeResponseTreeObj(baseDataObj, jsonObj));
                        return objRetParams;
                    }

                    if (response.getBody().startsWith(("["))) {
                        JSONArray responseJson = objectMapper.readValue(response.getBody(), JSONArray.class);
                        JSONObject responseJsonTree = new JSONObject();
                        responseJsonTree.put("ArrayData", responseJson);

                        objRetParams.setHeader("ERROR_FLAG", false);
                        objRetParams.setHeader("ERROR_MSG", "");
                        objRetParams.setDataObject("DATA", responseJsonTree);
                        objRetParams.setDataObject("TREE", this.makeResponseTreeObj(baseDataObj, responseJsonTree));
                    } else {
                        JSONObject responseJson = objectMapper.readValue(response.getBody(), JSONObject.class);
                        objRetParams.setHeader("ERROR_FLAG", false);
                        objRetParams.setHeader("ERROR_MSG", "");
                        objRetParams.setDataObject("DATA", responseJson);
                        objRetParams.setDataObject("TREE", this.makeResponseTreeObj(baseDataObj, responseJson));
                    }
                } else {
                    objRetParams.setHeader("ERROR_FLAG", false);
                    objRetParams.setHeader("ERROR_MSG", "");
                    objRetParams.setDataObject("DATA", new JSONObject());
                    objRetParams.setDataObject("TREE", new JSONObject());
                }
            } else if (response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT) {   // 성공, 응답정보없음
                objRetParams.setHeader("ERROR_FLAG", false);
                objRetParams.setHeader("ERROR_MSG", "성공, 응답내용없음");
                objRetParams.setDataObject("DATA", new JSONObject());
                objRetParams.setDataObject("TREE", new JSONArray());
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", response.getBody());
            }

            /*
            paletteRestTemplate.setConnectTimeout(5000);
            paletteRestTemplate.setReadTimeout(5000);
            ResponseEntity<Object> response  = paletteRestTemplate.exchange(sUrl, query, headers, httpMethod, Object.class );
            if (response.getStatusCodeValue() == HttpStatus.SC_OK) {
                JSONObject responseJson = objectMapper.readValue( objectMapper.writeValueAsString(response.getBody()), JSONObject.class);
                objRetParams.setHeader("ERROR_FLAG", false);
                objRetParams.setHeader("ERROR_MSG", "");
                objRetParams.setDataObject("DATA", responseJson);
                objRetParams.setDataObject("TREE", paletteLkagRestTemplate.makeResponseTreeObj(baseDataObj, responseJson));

            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", objectMapper.writeValueAsString(response.getBody()));
            }
            */
        } catch (org.springframework.web.client.ResourceAccessException e) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", e.getMessage());
        } catch (URISyntaxException e) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", e.getMessage());
        }

        return objRetParams;
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

                String colVal = jsonParams.getString(obj.getString("COL_NM"));
                if (StringUtils.isEmpty(colVal)) {
                    colVal = obj.getString("BSC_VL");
                }

                String endeVal = paletteLkagRestTemplate.endecryptValue(colVal, obj.getString("ENCPT_YN"), obj.getString("PARAM_ENCPT_MTHD_CD_VL"),
                    obj.getString("PARAM_ENCPT_KEY"));

                if (("LPIT_DATETIME_S".equals(obj.getString("DATA_TYPE_CD")) || "LPIT_DATETIME_E".equals(obj.getString("DATA_TYPE_CD")))
                    && colVal.length() == 14) { //yyyyMMddHHmmss => 정해진 날짜포멧으로.

                    LocalDateTime date1 = LocalDateTime.parse(colVal, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    endeVal = Collections.singletonList(
                        (new SimpleDateFormat(obj.getString("DATA_TYPE_FMT_CD")).format(Date.from(date1.toInstant(ZoneOffset.of("+09:00")))))).toString();
                    parameters.put(obj.getString("COL_NM"), endeVal.replaceAll("\\[", "").replaceAll("]", ""));
                    //우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.

                } else if ("LPIT_ARRAY_STRING".equals(obj.getString("DATA_TYPE_CD"))) {
                    String[] cols = endeVal.split(",");
                    parameters.put(obj.getString("COL_NM"), cols);
                } else {
                    parameters.put(obj.getString("COL_NM"), endeVal.replaceAll("\\[", "").replaceAll("]", ""));
                }
            }

            if ("LPIT_OBJECT".equals(obj.getString("DATA_TYPE_CD"))) {
                parameters.put(obj.getString("COL_NM"), makeParameterBodyJson(jsonParams, obj.getJSONArray("children")));
            }
        }
        return parameters;
    }

    public JSONObject makeResponseBody(
        JSONArray paramsDataObj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONObject parameters = new JSONObject();
        return parameters;
    }

    /**
     * 전문 테스트 응답용 Tree구조 생성.
     */
    public JSONArray makeResponseTreeObj(JSONObject baseDataObj,
        JSONObject obj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONArray returnArray = new JSONArray();
        Iterator i = obj.keys();
        while (i.hasNext()) {
            JSONObject sub = new JSONObject();
            String key = i.next().toString();
            Object valueObj = obj.get(key);
            log.info(key + "::" + valueObj + "::" + valueObj.getClass().getName());
            sub.put("id", UUID.randomUUID().toString());
            sub.put("name", key);
            sub.put("COL_NM", key);
            sub.put("FULL_PATH", "");
            sub.put("LKAG_ID", baseDataObj.getString("LKAG_ID"));
            if( "payment_method".equals( key )) {
                System.out.println("payment_method");
            }
            if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                JSONArray _tmpValueObj = (JSONArray) valueObj;
                if (_tmpValueObj.size() > 0) {
                    log.info("_tmpValueObj.get(0).getClass().getName() :: " + _tmpValueObj.get(0).getClass().getName());
                    if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName())) { // ["aaa","bbb"] 스타일
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                    }else if ("java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName())) {
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                        //sub.put(key, ""+_tmpValueObj.get(0));
                    }else if ("net.sf.json.JSONObject".equals(_tmpValueObj.get(0).getClass().getName())) {
                        sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                        sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject)(_tmpValueObj.get(0))) );
                    }else { // [{"a1":"aaa"},{"b1":"bbb"}] 스타일
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY");
                        sub.put("children", makeResponseTreeSubArray(baseDataObj, _tmpValueObj));
                    }
                }

            } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject) valueObj));
            } else {
                sub.put("DATA_TYPE_CD", "LPIT_STRING");
                sub.put(key, valueObj);
            }
            returnArray.add(sub);

        }
        return returnArray;
    }

    public JSONArray makeResponseTreeSubObj(JSONObject baseDataObj,
        JSONObject obj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONArray returnObj = new JSONArray();
        Iterator i = obj.keys();
        while (i.hasNext()) {
            JSONObject sub = new JSONObject();
            String key = i.next().toString();
            Object valueObj = obj.get(key);
            log.info(key + "::" + valueObj + "::" + valueObj.getClass().getName());
            sub.put("id", UUID.randomUUID().toString());
            sub.put("name", key);
            sub.put("COL_NM", key);
            sub.put("FULL_PATH", "");
            sub.put("LKAG_ID", baseDataObj.getString("LKAG_ID"));
            if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                JSONArray _tmpValueObj = (JSONArray) valueObj;
                if (_tmpValueObj.size() > 0) {
                    log.info("_tmpValueObj.get(0).getClass().getName() :: " + _tmpValueObj.get(0).getClass().getName());
                    if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName())) { // ["aaa","bbb"] 스타일
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                    }else if ("java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName())) {
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                        //sub.put(key, ""+_tmpValueObj.get(0));
                    }else if ("net.sf.json.JSONObject".equals(_tmpValueObj.get(0).getClass().getName())) {
                        sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                        sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject)(_tmpValueObj.get(0))) );
                    } else { // [{"a1":"aaa"},{"b1":"bbb"}] 스타일
                        sub.put("DATA_TYPE_CD", "LPIT_ARRAY");
                        sub.put("children", makeResponseTreeSubArray(baseDataObj, _tmpValueObj));
                    }
                }
            } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject) valueObj));
            } else {
                sub.put("DATA_TYPE_CD", "LPIT_STRING");
                sub.put(key, valueObj);
            }
            returnObj.add(sub);
        }
        return returnObj;
    }

    public JSONArray makeResponseTreeSubArray(JSONObject baseDataObj,
        JSONArray arrayObj) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        JSONArray returnObj = new JSONArray();
        if (arrayObj.size() > 0) {
            //for (Object item : arrayObj) {
            Object item = arrayObj.get(0);  //응답 틀을 추출하기 위함으로 0번만 처리함.
            JSONObject tempObj = (JSONObject) item;
            Iterator i = tempObj.keys();
            while (i.hasNext()) {
                JSONObject sub = new JSONObject();
                String key = i.next().toString();
                Object valueObj = tempObj.get(key);
                sub.put("id", UUID.randomUUID().toString());
                sub.put("name", key);
                sub.put("COL_NM", key);
                sub.put("FULL_PATH", "");
                sub.put("LKAG_ID", baseDataObj.getString("LKAG_ID"));
                if ("net.sf.json.JSONArray".equals(valueObj.getClass().getName())) {
                    JSONArray _tmpValueObj = (JSONArray) valueObj;
                    if (_tmpValueObj.size() > 0) {
                        log.info("_tmpValueObj.get(0).getClass().getName() :: " + _tmpValueObj.get(0).getClass().getName());
                        if ("java.lang.String".equals(_tmpValueObj.get(0).getClass().getName())) { // ["aaa","bbb"] 스타일
                            sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                        }else if ("java.lang.Integer".equals(_tmpValueObj.get(0).getClass().getName())) {
                            sub.put("DATA_TYPE_CD", "LPIT_ARRAY_STRING");
                            //sub.put(key, ""+_tmpValueObj.get(0));
                        }else if ("net.sf.json.JSONObject".equals(_tmpValueObj.get(0).getClass().getName())) {
                            sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                            sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject)(_tmpValueObj.get(0))) );
                        } else { // [{"a1":"aaa"},{"b1":"bbb"}] 스타일
                            sub.put("DATA_TYPE_CD", "LPIT_ARRAY");
                            sub.put("children", makeResponseTreeSubArray(baseDataObj, _tmpValueObj));
                        }
                    }
                } else if ("net.sf.json.JSONObject".equals(valueObj.getClass().getName())) {
                    sub.put("DATA_TYPE_CD", "LPIT_OBJECT");
                    sub.put("children", makeResponseTreeSubObj(baseDataObj, (net.sf.json.JSONObject) valueObj));
                } else {
                    sub.put("DATA_TYPE_CD", "LPIT_STRING");
                    sub.put(key, valueObj);
                }
                returnObj.add(sub);
            }

        }
        //}
        return returnObj;
    }
}
