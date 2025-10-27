package kr.co.hkcloud.palette3.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.jcodec.common.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * packageName    : kr.co.hkcloud.palette3.core.util
 * fileName       : PaletteRestTemplate
 * author         : KJD
 * date           : 2024-04-09
 * description    : 연동용도 RestTemplate
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-09        KJD       최초 생성
 * </pre>
 */
@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class PaletteLkagRestTemplate {

    private final PaletteRestTemplate paletteRestTemplate;

    private String url;
    private Object query;
    private HttpHeaders httpHeaders;
    private HttpMethod httpMethod;

    public void setConnectTimeout(Integer connectTimeout) {
        paletteRestTemplate.setConnectTimeout(connectTimeout);
    }

    public void setReadTimeout(Integer readTimeout) {
        paletteRestTemplate.setReadTimeout(readTimeout);
    }

    public <T, R> R exchange(URI uri, T query, HttpHeaders httpHeaders, HttpMethod httpMethod ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return paletteRestTemplate.exchange(uri, query, httpHeaders, httpMethod, String.class);
    }

    public <T, R> R exchange(URI uri, T query, HttpHeaders httpHeaders, HttpMethod httpMethod, Class responseType ) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return paletteRestTemplate.exchange(uri, query, httpHeaders, httpMethod, responseType);
    }
    /**
     * 암복호화처리.
     */
    public String endecryptValue(String value, String encptYn, String endecryptClass,
        String key) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if ("N".equals(encptYn) || StringUtils.isEmpty(endecryptClass)) {
            return value;
        } else {
            String findMethod = endecryptClass.substring(endecryptClass.lastIndexOf(".") + 1);
            String findClass = endecryptClass.replaceAll("." + findMethod, "");

            Class<?> c = Class.forName(findClass);
            String resultStr = "";
            if (StringUtils.isEmpty(key)) {
                Method m = c.getMethod(findMethod, String.class, String.class);
                resultStr = (String) m.invoke(c, value);
            } else {
                Method m = c.getMethod(findMethod, String.class, String.class);
                resultStr = (String) m.invoke(c, value, key);
            }
            return resultStr;
        }
    }

    //

    /**
     * parameter BODY xml스타일인경우
     */
    public Object makeParameterBodyXml(JSONArray paramsDataObj) {
        return null;
    }

}
