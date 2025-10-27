package kr.co.hkcloud.palette3.integration.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.integration.service.CommerceApiServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * packageName    : kr.co.hkcloud.palette3.external.api
 * fileName       : CommerceRestController
 * author         : KJD
 * date           : 2024-04-12
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-12        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "CommerceRestController", description = "external Commerce API 컨트롤러")
public class CommerceRestController {

    private final CommerceApiServiceFactory commerceApiServiceFactory;

    private final TwbComDAO mobjDao;    //임시 테스트 후 서비스단으로 변경.

    @RequestMapping(value = "/intgr-api/commerce/v2/app/{bean_id}", method = {RequestMethod.GET})
    public ResponseEntity<String> app(HttpServletRequest request, HttpServletResponse response,@PathVariable("bean_id") String bean_id) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return new ResponseEntity<>(bean_id.toUpperCase(), HttpStatus.OK);
    }

    @RequestMapping(value = "/intgr-api/commerce/v2/oauth-code/{bean_id}", method = {RequestMethod.GET})
    public Object oauthCode(HttpServletRequest request, HttpServletResponse response, @PathVariable("bean_id") String bean_id) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("BEAN_ID",  bean_id.toUpperCase());
        jsonParams.setString("code",  request.getParameter("code"));
        jsonParams.setString("state",  request.getParameter("state"));
        if( request.getParameter("state") != null && request.getParameter("state").contains("__@@__")) {
            String[] stateArr =  request.getParameter("state").split("__@@__");
            jsonParams.setString("mallId",  stateArr[0]);
            jsonParams.setString("LKAG_ID",  stateArr[1]);
            jsonParams.setString("SCHEMA_ID",  stateArr[2]);
            jsonParams.setString("CERT_CUSTCO_ID",  stateArr[3]);
        }
        String message = "";
        TelewebJSON objRetParams = commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).oauthCode(jsonParams);
        if( objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            return new ResponseEntity<>("카페24인증에 실패 하였습니다.", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("카페24인증에 성공 하였습니다. ", HttpStatus.OK);
        }
    }

    @ApiOperation(value = "sample_authentication", notes = "샘플용도 인증처리.")
    @PostMapping("/intgr-api/commerce/sample_authentication")
    public Object sample_authentication(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).sample_authentication(jsonParams);
    }

    @ApiOperation(value = "orderList", notes = "주문목록조회")
    @PostMapping("/intgr-api/commerce/authentication")
    public Object authentication(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).authentication(jsonParams, null);
    }

    @ApiOperation(value = "call_api", notes = "주문목록조회")
    @PostMapping("/intgr-api/commerce/call_api")
    public Object call_api(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, UnsupportedEncodingException {

        /* 응답에서 params 변수 채워야하는경우 LKAG_ID 조회하여 먼저 세팅한다.
           테스트 후 서비스단으로 구현하여 이동한다.
           DATA_RSPNS_LKAG_ID_{{LKAG_ID}} 객체에 담고 필요한곳에서 활용한다.*/
        TelewebJSON objInsParams = new TelewebJSON(jsonParams);
        objInsParams = mobjDao.select("kr.co.hkcloud.palette3.integration.dao.CommerceLkagMapper", "selectInsertParamRspns", jsonParams);
        JSONArray jsonObj = objInsParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);
            TelewebJSON tempParams = new TelewebJSON();
            tempParams.setString("BEAN_ID" , jsonParams.getString("BEAN_ID"));
            tempParams.setString("CERT_CUSTCO_ID" , jsonParams.getString("CERT_CUSTCO_ID"));
            tempParams.setString("LKAG_ID" , objData.getString("RSPNS_LKAG_ID"));
            tempParams.setString("custId" , jsonParams.getString("custId"));

            objInsParams = commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).call_api(tempParams);

            jsonParams.setDataObject( "DATA_RSPNS_LKAG_ID_"+objData.getString("RSPNS_LKAG_ID"), objInsParams.getDataObject("DATA2") );

        }

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).call_api(jsonParams);
        return objRetParams;
    }
}