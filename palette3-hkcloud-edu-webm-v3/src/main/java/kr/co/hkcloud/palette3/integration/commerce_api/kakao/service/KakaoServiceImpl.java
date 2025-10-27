package kr.co.hkcloud.palette3.integration.commerce_api.kakao.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.core.util.PaletteLkagRestTemplate;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.integration.service.CommerceApiService;
import kr.co.hkcloud.palette3.integration.service.CommerceLkagServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

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
@Service(KakaoServiceImpl.BEAN_ID)
public class KakaoServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {

    public static final String BEAN_ID = "KAKAO";

    public KakaoServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
        RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository) {
        super(mobjDao, paletteJsonUtils, paletteLkagRestTemplate, redisCacheCustcoLkagRepository);
    }

    @Override
    public TelewebJSON oauthCode(TelewebJSON jsonParams) throws TelewebAppException {
        return jsonParams;
    }

    @Override
    public TelewebJSON sample_authentication(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        return objRetParams;
    }

    @Override
    public TelewebJSON authentication(TelewebJSON jsonParams, JSONObject baseDataObj) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        // 카카오 구현
        log.info(BEAN_ID + " >> authentication");
        return objRetParams;
    }

    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException {
        log.info(BEAN_ID + " >> orderList");
        return null;
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

}
