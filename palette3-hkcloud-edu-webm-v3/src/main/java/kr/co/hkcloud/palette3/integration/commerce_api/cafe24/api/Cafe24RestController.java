package kr.co.hkcloud.palette3.integration.commerce_api.cafe24.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.integration.commerce_api.cafe24.service.Cafe24ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Cafe24RestController {

    private final Cafe24ServiceImpl cafe24Service;

    @PostMapping("/intgr-api/commerce/cafe24/orders")
    public ResponseEntity<String> orders(HttpServletRequest request, HttpServletResponse response ) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, JsonProcessingException, KeyManagementException {

        JSONObject ret = cafe24Service.orders( request );
        return new ResponseEntity<>(ret.toString(), HttpStatus.OK);
    }

    @PostMapping("/intgr-api/commerce/cafe24/ordersDetail")
    public Object ordersDetail(@TelewebJsonParam TelewebJSON jsonParams) throws TelewebApiException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, JsonProcessingException, KeyManagementException {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        JSONArray resArr = new JSONArray();
        resArr.add( cafe24Service.ordersDetail( jsonParams ) );
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setDataObject( resArr );

        return objRetParams;
    }
}
