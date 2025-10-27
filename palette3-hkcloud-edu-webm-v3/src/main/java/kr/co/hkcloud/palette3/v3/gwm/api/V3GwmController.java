package kr.co.hkcloud.palette3.v3.gwm.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.v3.gwm.service.V3GwmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : kr.co.hkcloud.palette3.v3.customer.api
 * fileName       : V3GwmController
 * author         : KJD
 * date           : 2024-03-13
 * description    : Gwm전용 if Controller
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-13        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "V3GwmController", description = "GWM전용 V3-API REST 컨트롤러")
public class V3GwmController {

    private final V3GwmService v3GwmService;

    /* API 호출 예제
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
        }]
     }
    */

    @ApiOperation(value = "[V3-API GWM전용] BO시스템에 신규회원 또는 수정 시 Palette에 고객 정보를 신규 등록 또는 수정한다.", notes = "[V3-API GWM전용] BO시스템에 신규회원 또는 수정 시 Palette에 고객 정보를 신규 등록 또는 수정한다.")
    @PostMapping("/v3-api/gwm/merge/customer")
    public Object mergeCustomer(@TelewebJsonParam TelewebJSON mjsonParams,
        @RequestBody JSONObject jsonBody) throws TelewebApiException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        List<String> failData = new ArrayList<>();
        try {
            Object paramsObj = jsonBody.get("params");
            if (paramsObj != null) {
                if (paramsObj instanceof JSONObject) { // 단건 호출 시
                    TelewebJSON objBodyData = new TelewebJSON();
                    ((JSONObject) paramsObj).put("SCHEMA_ID", mjsonParams.getString("SCHEMA_ID"));
                    ((JSONObject) paramsObj).put("USER_ID", mjsonParams.getString("USER_ID"));
                    ((JSONObject) paramsObj).put("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
                    objBodyData.setDataObject("body", (JSONObject) paramsObj);
                    try {
                        objRetParams = v3GwmService.mergeCustInfo(objBodyData);
                        if (objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                            failData.add( ((JSONObject) paramsObj).getString("memberNo") );
                        }
                    }catch(Exception e){
                        failData.add( ((JSONObject) paramsObj).getString("memberNo") );
                    }
                } else if (paramsObj instanceof JSONArray) { // 다건 호출 시
                    JSONArray arr = (JSONArray) paramsObj;
                    for (int i = 0; i < arr.size(); i++) {
                        TelewebJSON arrBodyData = new TelewebJSON();
                        JSONObject obj = (JSONObject) arr.get(i);
                        obj.put("SCHEMA_ID", mjsonParams.getString("SCHEMA_ID"));
                        obj.put("USER_ID", mjsonParams.getString("USER_ID"));
                        obj.put("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
                        arrBodyData.setDataObject("body", obj);
                        try {
                            objRetParams = v3GwmService.mergeCustInfo(arrBodyData);
                            if (objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                                failData.add( ((JSONObject) paramsObj).getString("memberNo") );
                            }
                        }catch(Exception e){
                            failData.add( obj.getString("memberNo") );
                        }
                    }

                } else {
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "invalid parameter");
                }
                objRetParams.setHeader("ERROR_FLAG", false);
                objRetParams.setHeader("ERROR_MSG", "정상처리되었습니다.");
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "invalid parameter");
            }
        } catch (net.sf.json.JSONException nsje) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "invalid parameter");
        }
        objRetParams.setDataObject("FAIL_DATA", JSONArray.fromObject (failData) );


        return objRetParams;
    }

    @ApiOperation(value = "[V3-API GWM전용] BO시스템에 신규회원 또는 수정 시 Palette에 고객 정보를 신규 등록 또는 수정한다.", notes = "[V3-API GWM전용] BO시스템에 신규회원 또는 수정 시 Palette에 고객 정보를 신규 등록 또는 수정한다.")
    @PostMapping("/v3-api/gwm/select/customer")
    public Object selectCustomer(@TelewebJsonParam TelewebJSON mjsonParams,
        @RequestBody JSONObject jsonBody) throws  TelewebApiException, InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        try{
            Object paramsObj = jsonBody.get("params");
            if (paramsObj != null) {
                TelewebJSON objBodyData = new TelewebJSON();
                ((JSONObject) paramsObj).put("SCHEMA_ID", mjsonParams.getString("SCHEMA_ID"));
                ((JSONObject) paramsObj).put("USER_ID", mjsonParams.getString("USER_ID"));
                ((JSONObject) paramsObj).put("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
                objBodyData.setDataObject("body", (JSONObject) paramsObj);

                objRetParams = v3GwmService.selectCustomer(objBodyData);
                if (objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                    return objRetParams;
                }
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "invalid parameter");
            }
        } catch (net.sf.json.JSONException nsje) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "invalid parameter");
        }
        return objRetParams;
    }

    //test
    @PostMapping("/v3-api/getRepairRequestList")
    public JSONObject getRepairRequestList() throws Exception {
        String a = "{\"RESULT_CD\": \"200\",\"RESULT_DATA\": {\"RESULT_PAGE_PARAM\": {\"totalCnt\": \"yQYTDTPCxTT+YVz+LMRdg==\",\"pageNo\": \"s1W9vljJUqMs8sMAG+V8GA==\",\"totalPageNo\": \"x7bal/xatmd6uOLjR42Wo==\"},\"RESULT_LIST\": [{\"ordDtStr\": \"mZ21wO/0PFj9wSaTTpgnog==\",\"brndNm\":\"8A5HYghWL3wLFDtBIiybMA==\",\"ordNo\": \"AMw+usxVJj9OC/rIslfvFmz5m8PU74rJhYXMOIR6YBo=\",\"godNm\": \"CIUJnsOnyVJSqMMuthEYRJvpgqJbSnvL3ET9Ndmcfl+mZUGnO+w58bqs068tQv55e9IuGk19QQMrM3z645vHw==\",\"godImgUrl\":\"X34ndvbDOK2AEq0f5Ot4G3wx6pJHdjBz1T7u+IowfPoMa05LiIdnIoZZaTo9iPiX\",\"price\":\"Kty0l1cqWYYKG1Q0aF71ow==\",\"ordStatNm\": \"Oco5DAfvY/k0yFf3Vl5x4w==\",\"ordQty\":\"s1W9vljJUqMs8sMAG+V8GA==\",\"itmNm\": \"rak3BSDK0Jgca6OCJkaf0w==\"},{\"ordDtStr\": \"+4layQJa8ot+mmokMSrPg==\",\"brndNm\": \"nbmfqhivcsme7RPoAEAHmw==\",\"ordNo\":\"kaFR8xDKgMs6RZ51UC7W7TPUyLCYn5XjFAcd8mF2jnU=\",\"godNm\":\"VgGZjy4P9xOtdD3jtxUDy9lOPpMGeAv2nZ+PUaB1K3XZGvGqvJmuPimeQmJL7yLrWwa8Oy386aMkoQSx095D==\",\"godImgUrl\": \"jcbNsBi09bre+Msw0/rFkY5/Dr/4yepeRkFU7XzErO9KueToYT024/r1mxQsXzDF\",\"price\": \"pVrvr/+piz88apvERjtzfA==\",\"ordStatNm\": \"Oco5DAfvY/k0yFf3Vl5x4w==\",\"ordQty\":\"s1W9vljJUqMs8sMAG+V8GA==\",\"itmNm\": \"kP06qBIywRJEa0IN1eXu02XjVCV559VtiGx/DTTi/OE=\"},{\"ordDtStr\": \"K+4layQJa8ot+mmokMSrPg==\",\"brndNm\": \"O4Th7cAjImx7D/G+XRnDaQ==\",\"ordNo\":\"kaFR8xDKgMs6RZ51UC7W7TPUyLCYn5XjFAcd8mF2jnU=\",\"godNm\":\"1lOxlAYpRjGViHh8rtidbrLiaJHYSAeAtpi5QA3FlF9CyTKg8wLOduGpz4bgXuW2\",\"godImgUrl\":\"yZdBuTBHKQBSuSn1In65myjMOJtJr43BA7v4QUOKq9Ea59Pranp/3eDF/u9r1j3X\",\"price\": \"zpyfcgR+WK6qH5cNUx4pw==\",\"ordStatNm\": \"Oco5DAfvY/k0yFf3Vl5x4w==\",\"ordQty\": \"s1W9vljJUqMs8sMA+V8GA==\",\"itmNm\": \"3YEK/KeGJfRO2s5UaO70RXxzHTu/3DwWyJV9X82EAnQ=\"},{\"ordDtStr\": \"+4layQJa8ot+mmokMSrPg==\",\"brndNm\": \"O4Th7cAjImx7D/G+XRnDaQ==\",\"ordNo\":\"kaFR8xDKgMs6RZ51UC7W7TPUyLCYn5XjFAcd8mF2jnU=\",\"godNm\":\"1lOxlAYpRjGViHh8rtidbrLiaJHYSAeAtpi5QA3FlF9CyTKg8wLOduGpz4bgXuW2\",\"godImgUrl\":\"yZdBuTBHKQBSuSn1In65myjMOJtJr43BA7v4QUOKq9Ea59Pranp/3eDF/u9r1j3X\",\"price\": \"zpyfcgR+WK6qH5cNUx4pw==\",\"ordStatNm\": \"Oco5DAfvY/k0yFf3Vl5x4w==\",\"ordQty\": \"s1W9vljJUqMs8sMA+V8G==\",\"itmNm\": \"3YEK/KeGJfRO2s5UaO70RXxzHTu/3DwWyJV9X82EAnQ=\"}]},\"RESULT_MSG\": \"SUCCESS\"}";
        JSONObject obj = JSONObject.fromObject(JSONSerializer.toJSON(a));
        return obj;
    }
}
