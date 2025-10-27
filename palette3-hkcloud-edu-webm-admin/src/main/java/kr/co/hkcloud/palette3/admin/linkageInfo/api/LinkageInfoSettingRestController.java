package kr.co.hkcloud.palette3.admin.linkageInfo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.admin.linkageInfo.app.LinkageInfoSettingService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.linkageInfo.api
 * fileName       :
 * author         : njy
 * date           : 2024-03-19
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-19           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "LinkageInfoSettingRestController", description = "연동 정보 관리 REST 컨트롤러")
public class LinkageInfoSettingRestController {
    @Value("${api.gwm.encryptKey}")
    private String KEY;
    private final LinkageInfoSettingService service;

    String apiKey = "bearer pRLn+CFE2UV9R8xHvmLZUAvdx711AwUxJJOGOQG9BoTA+0xHYDYRVTheTiW8YODQ4vmaUcDYGG0m443lPmbHAYPHGjrhHrxRL+4q1+vKTd7OjpgP5K3AAkjqW7MTtyjo8ySzJ4sJtCHitF/c1HU2pcqtxjoB0Xa56niPfxnS7peZQyoHVMyRbPJslURdpKH6";
    @ApiOperation(value = "", notes = "")
    @PostMapping("/admin-api/linkageInfo/test")
    public Object responseParamDetailInfo(@TelewebJsonParam TelewebJSON mjsonParam) throws TelewebApiException {

        TelewebJSON objRetParams = new TelewebJSON();
        RestTemplate restTemplate = new RestTemplate();
//        String uri = "https://palette.hkpalette/v3-api/gwm/merge/customer";
        String uri = "http://localhost:8443/v3-api/getRepairRequestList";

        JSONObject param = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject body = new JSONObject();
        JSONObject body2 = new JSONObject();
        JSONObject body3 = new JSONObject();

        body.put("customerTelNo", "1NpZcUszOk5AQK02DNQNaw==");
        body.put("customerNm", "bwMS5L0sT52jmp0aoxX+Mg==");
        body.put("customerStatUdt", "VOjFraCb+DMxRjDMI2GxpQ==");
        body.put("customerStat", "mWkF+u6wg7DwuB6RM7w95w==");
        body.put("memberNo", "rqKGPnh9s88q309XOyZGHw==");

        body2.put("customerTelNo", "mf0nbhA0uZKBDgh7ZwVjjw==");
        body2.put("customerNm", "v6UKrgPakwc8o1n5e8Mm2g==");
        body2.put("customerStatUdt", "VOjFraCb+DMxRjDMI2GxpQ==");
        body2.put("customerStat", "mWkF+u6wg7DwuB6RM7w95w==");
        body2.put("memberNo", "rqKGPnh9s88q309XOyZGHw==");

        body3.put("customerTelNo", "IRPmGOD4r1xJ0IgEsnKJEQ==");
        body3.put("customerNm", "hQJL8mxRRMyKqUWT9irlkg==");
        body3.put("customerStatUdt", "VOjFraCb+DMxRjDMI2GxpQ==");
        body3.put("customerStat", "U5Z076KpC2SDmRYQfYHyNA==");
        body3.put("memberNo", "rqKGPnh9s88q309XOyZGHw==");
        arr.add(body);
        arr.add(body2);
        arr.add(body3);
        param.put("params", arr);
        String data = param.toString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-V3-Authorization", apiKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(data.toString(),httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity ,String.class);

        JSONObject resObj = JSONObject.fromObject(JSONSerializer.toJSON(response.getBody()));;
        objRetParams = service.responseParamDetailInfo(resObj);

        return objRetParams;
    }

}
