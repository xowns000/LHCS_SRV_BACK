package kr.co.hkcloud.palette3.integration.commerce_api.external.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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
@Service(ExternalApiServiceImpl.BEAN_ID)
public class ExternalApiServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {
    public static final String BEAN_ID = "EXTERNAL_API";

    public ExternalApiServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
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



        HttpHeaders headers = super.getHeaders( jsonParams, objCustcoLkagApi );
        log.info("HttpHeaders :: {}", headers.toString() );
        //--------------------------------------------------------------------------------------
        String sUrl = super.getUrl( jsonParams, objCustcoLkagApi );

        // 3. Parameter 설정
        Object query = super.getQuery( jsonParams, objCustcoLkagApi );

        // 응답.
        TelewebJSON objRetRspns = super.getResponse( objCustcoLkagApi, sUrl, query, headers, HttpMethod.valueOf(baseDataObj.getString("RQST_SE_CD_VL").toUpperCase()) );
        // SRCH_CERT_CUSTCO_ID, SRCH_LKAG_ID
        log.info(BEAN_ID + " >> orderList");
        return objRetRspns;
    }

    @Override
    public TelewebJSON call_batch_api(
        TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON retObj = new TelewebJSON(jsonParams);
        retObj = this.call_api( jsonParams );
        log.info("call_batch_api EXTERNAL response :: {}", retObj.toString());
        if( jsonParams.getDataObject("DATA2") != null ) {
        }
        
        JSONArray retObjArr = retObj.getDataObject("DATA");
        JSONObject extObj = retObjArr.getJSONObject(0);
        String strExtObj = extObj.toString();
        // JSON 문자열을 JSONObject 객체로 변환
        JSONObject jsonObject = JSONObject.fromObject(strExtObj);
        // "responseData" 키로 JSONArray 추출
        JSONArray responseData = jsonObject.getJSONArray("responseData");

        TelewebJSON retExtObj = new TelewebJSON();
        retExtObj.setDataObject("DATA", responseData);
        log.info("getExtApi objRetParams" + retExtObj);
        
		return retExtObj;
    }
}		
