package kr.co.hkcloud.palette3.layout.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.security.crypto.Base64;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.layout.service.LayoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * packageName    : kr.co.hkcloud.palette3.layout.service.impl
 * fileName       :
 * author         : njy
 * date           : 2024-04-17
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-17           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("LayoutService")
public class LayoutServiceImpl implements LayoutService {

    private final TwbComDAO twbComDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${commerce.api.cafe24.auth_code_url}")
    private String AUTH_CODE_URL;

    @Value("${commerce.api.cafe24.auth_code_redirect_uri}")
    private String AUTH_CODE_REDIRECT_URI;

    private final String CAFE24_REFRESH_TOKEN_KEY = "cafe24:refresh_token";
    private final String CAFE24_REFRESH_TOKEN_EXPIRES_AT_KEY = "cafe24:refresh_token_expires_at";

    @Override
    public TelewebJSON selectLayout(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParam = new TelewebJSON(jsonParams);
        TelewebJSON selectParams = new TelewebJSON();

        objRetParam.setDataObject("tabList", twbComDao.select("kr.co.hkcloud.palette3.layout.dao.LayoutMapper", "selectTabList", jsonParams)
            .getDataObject(TwbCmmnConst.G_DATA));

        /////////////////////////////////////////////////////////////////
        // Cafe24인경우 토큰 체크 시작
        boolean isCafe24 = false;
        String schemaId = jsonParams.getString("SCHEMA_ID");
        String certCustcoId = jsonParams.getString("CERT_CUSTCO_ID");
        String lkagId = "";
        String clientId = "";
        String clientSecret = "";
        String serviceKey = "";
        String mallid = "";
        JSONArray tabJsonArray = objRetParam.getDataObject("tabList");
        for (Object o : tabJsonArray) {
            JSONObject j = (JSONObject) o;
            if( "CAFE24".equals(j.getString("LKAG_BEAN_ID")) ) {
                isCafe24 = true;
                lkagId = j.getString("LKAG_ID");
                clientId = j.getString("AAK");  //제공된 애플리케이션 ID
                clientSecret = j.getString("ASK");
                serviceKey = j.getString("AAI");
                mallid = j.getString("E1"); //account_id or mallid
                break;
            }
        }
        if( isCafe24 ) {

            String refresh_token = stringRedisTemplate.opsForValue().get( CAFE24_REFRESH_TOKEN_KEY + ":" + schemaId + "_" + mallid );
            String refresh_expires_at = stringRedisTemplate.opsForValue().get( CAFE24_REFRESH_TOKEN_EXPIRES_AT_KEY + ":" + schemaId + "_" + mallid );
            if (!StringUtils.isEmpty(refresh_token) && DateCmmnUtils.getExpiration(refresh_expires_at) > 0) {   //만료시간
                objRetParam.setHeader("CAFE24_AT", true);
            } else {
                objRetParam.setHeader("CAFE24_AT", false);

                String auth_code_url = AUTH_CODE_URL;
                auth_code_url = auth_code_url.replaceAll("\\{mallid}", mallid);
                auth_code_url = auth_code_url.replaceAll("\\{cliend_id}", clientId);
                auth_code_url = auth_code_url.replaceAll("\\{redirect_uri}", AUTH_CODE_REDIRECT_URI);
                auth_code_url = auth_code_url.replaceAll("\\{state}", mallid + "__@@__" + lkagId + "__@@__" + schemaId + "__@@__" + certCustcoId);

                //commerce.api.cafe24.auth_code_url: https://{mallid}.cafe24api.com/api/v2/oauth/authorize?response_type=code&client_id={cliend_id}&redirect_uri={redirect_uri}&scope=mall.read_community,mall.write_community,mall.read_order,mall.read_shipping,mall.read_product,mall.read_customer&state={state)

                objRetParam.setHeader("CAFE24_ACU", Base64.encode(auth_code_url.getBytes()));
            }

        }
        // Cafe24인경우 토큰 체크 종료
        ////////////////////////////////////////////////////////////////
        // list_id조회를 위한 tab_id 리스트
        List<String> arrTabId = new LinkedList<String>();
        List<String> arrListId = new LinkedList<String>();

        if (!ObjectUtils.isEmpty(objRetParam.getDataObject("tabList"))) {

            if( !isCafe24 ) {   //카페24는 아래 필요없음.

                //TAB_ID를 list형태로 가공
                for (int i = 0; i < objRetParam.getDataObject("tabList").size(); i++) {
                    arrTabId.add(objRetParam.getDataObject("tabList").getJSONObject(i).getString("TAB_ID"));
                }
                if (arrTabId.size() != 0) {
                    selectParams.setObject("arrTabId", 0, arrTabId);
                }

                selectParams = twbComDao.select("kr.co.hkcloud.palette3.layout.dao.LayoutMapper", "selectLayoutList", selectParams);

                //LIST_ID를 list형태로 가공
                for (int i = 0; i < selectParams.getDataObject(TwbCmmnConst.G_DATA).size(); i++) {
                    arrListId.add(selectParams.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(i).getString("LIST_ID"));
                }
                if (arrListId.size() != 0) {
                    selectParams.setObject("arrListId", 0, arrListId);
                }

                // thumnail
                objRetParam.setDataObject("thumnail",
                        twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListThumnailMapper", "selectThumnail",
                                selectParams).getDataObject(TwbCmmnConst.G_DATA));
                // srch
                objRetParam.setDataObject("srch", twbComDao.select("kr.co.hkcloud.palette3.layout.dao.LayoutMapper", "selectSrch", selectParams)
                        .getDataObject(TwbCmmnConst.G_DATA));
                // stts
                objRetParam.setDataObject("stts",
                        twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListSttsMapper", "selectStts",
                                selectParams).getDataObject(TwbCmmnConst.G_DATA));
                //listGroup
                objRetParam.setDataObject("listGroup",
                        twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListGroupMapper", "selectListGroup",
                                selectParams).getDataObject(TwbCmmnConst.G_DATA));
                //dwnGroup
                objRetParam.setDataObject("dwnGroup",
                        twbComDao.select("kr.co.hkcloud.palette3.admin.layoutMng.tab.list.dao.LayoutMngTabListDwnGroupMapper", "selectDwnGroup",
                                selectParams).getDataObject(TwbCmmnConst.G_DATA));

            }

        } else {
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "등록된 레이아웃이 없습니다.");
        }

        return objRetParam;
    }
}
