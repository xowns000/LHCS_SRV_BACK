package kr.co.hkcloud.palette3.integration.commerce_api.st11.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static java.util.Collections.replaceAll;

/**
 * packageName    : kr.co.hkcloud.palette3.integration.commerce_api.st11.service.St11ServiceImpl
 * fileName       : St11ServiceImpl
 * author         : KJD
 * date           : 2024-04-09
 * description    : 11번가 연동을 위한 API
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-09        KJD       최초 생성
 * 2024-09-05        NJY       11번가 개발 시작
 * </pre>
 */
@Slf4j
@Service(St11ServiceImpl.BEAN_ID)
public class St11ServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService {
    public static final String BEAN_ID = "ST11";

    public St11ServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
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
    public TelewebJSON authentication( TelewebJSON jsonParams, JSONObject baseDataObj ) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        // 11번가 구현
        log.info(BEAN_ID + " >> authentication");
        return objRetParams;
    }
    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, UnsupportedEncodingException {
        // 연동 기본 정보 조회(BEAN_ID, LKAG_ID)
        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi( jsonParams );
        log.info(BEAN_ID + " >> orderList");
        log.info(BEAN_ID + " >> objCustcoLkagApi" + objCustcoLkagApi.toString());

        JSONObject baseDataObj = (JSONObject)objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray rspnsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_RSPNS");
        if (rspnsDataObj == null) {
            rspnsDataObj = new JSONArray();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("openapikey",baseDataObj.getString("ACS_ACNT_KEY"));
        headers.setContentType(new MediaType("text","xml",Charset.forName("UTF-8")));
        headers.set("Content-Type", "text/xml; charset=UTF-8");

        String sUrl = super.getUrl( jsonParams, objCustcoLkagApi );

        Object query = super.getQuery( jsonParams, objCustcoLkagApi );
//        String encodedQuery = URLEncoder.encode(String.valueOf(query), "utf-8");

        TelewebJSON objRetRspns = super.getResponse( objCustcoLkagApi, sUrl, query, headers, HttpMethod.valueOf(baseDataObj.getString("RQST_SE_CD_VL").toUpperCase()) );
        log.info(BEAN_ID + " >> objRetRspns" + objRetRspns.toString());
        return objRetRspns;
    }

    //BBS 배치(고객문의, 상품문의) 전자상거래_문의_게시판 포멧이 맞추어 리턴. 처리용.
    @Override
    public TelewebJSON call_batch_api(
            TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException, UnsupportedEncodingException {

        TelewebJSON retObj = new TelewebJSON(jsonParams);

        JSONArray returnArray = new JSONArray();
        TelewebJSON receiveObj = new TelewebJSON(jsonParams);

        // 00 : 전체조회
        // 01 : 답변완료조회
        // 02 : 미답변조회
        if(StringUtils.isEmpty(jsonParams.getString("answerStatus"))) {     // 11번가 답변 여부
            jsonParams.setString("answerStatus", "02");                         // 미답변만 조회
        }
        if(StringUtils.isEmpty(jsonParams.getString("startTime"))) { // 시작시간 없다면 현재시간 넣어주기 ( yyyyMMdd까지만 쓰기때문에 시간 상관없음 )
            jsonParams.setString("startTime", jsonParams.getString("currentDateTime"));
        }
        if(StringUtils.isEmpty(jsonParams.getString("endTime"))) {
            jsonParams.setString("endTime", jsonParams.getString("currentDateTime"));
        }

        // API호출.
        receiveObj = this.call_api( jsonParams );
        if( receiveObj.getDataObject("DATA2") != null ) {

            JSONObject responseObj = (JSONObject) receiveObj.getDataObject("DATA2").get(0);
            // 고객문의 : INQRY_CUST,  상품문의 INQRY_GDS
            JSONObject dataobj = responseObj.getJSONObject("ns2:productQnas");
            JSONArray contentsArray = new JSONArray();
            if ("INQRY_CUST".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {

                // 상품 Q&A만 제공

            } else if ("INQRY_GDS".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {

                //상품문의
                System.out.println("contains :::::::::::::::: " + dataobj.containsKey("ns2:result_code"));
                // 정상 조회는 결과코드가 없음.
                if(!dataobj.containsKey("ns2:result_code")){

                    if("net.sf.json.JSONObject".equals(dataobj.get("ns2:productQna").getClass().getName())){
                        contentsArray.add(dataobj.get("ns2:productQna"));
                    }else{
                        contentsArray =  (JSONArray)dataobj.get("ns2:productQna");
                    }
                    if(  contentsArray !=null && contentsArray.size() > 0 ) {
                        for( Object o : contentsArray ) {
                            JSONObject sub = new JSONObject();
                            JSONObject contenObj = (JSONObject)o;

                            sub.put("BEAN_ID", "ST11" );
                            sub.put("BEAN_TP", "GDS" );
                            sub.put("SNDR_KEY", jsonParams.getString("SNDR_KEY") ); //발송자_키
                            sub.put("INQRY_NO", contenObj.getString("brdInfoNo") ); //문의번호 -
                            sub.put("GDS_NO", contenObj.getString("brdInfoClfNo")); //상품번호 -
                            sub.put("GDS_NM", contenObj.getString("prdNm")); //상품명 -

                            sub.put("WRTR_ID", contenObj.getString("memID")); // 작성자아이디 -
                            sub.put("WRTR_NM", contenObj.getString("memNM")); // 작성자아이디 -
                            sub.put("WRT_DT", DateCmmnUtils.getISO8601ToDateString("yyyy-MM-dd HH:mm:ss", contenObj.getString("createDt").replace(".0",""))); // 작성일시.-
                            sub.put("INQRY_TTL", contenObj.getString("brdInfoSbjct")); // 문의제목.-
                            sub.put("INQRY_CN", contenObj.getString("brdInfoCont")); // 문의내용.-
                            sub.put("INQRY_TYPE", contenObj.getString("qnaDtlsCdNm")); //문의유형 -

                            // 구매여부가 Y인 경우
                            if("Y".equals(contenObj.getString("buyYn"))){
                                sub.put("ORDR_YN", contenObj.getString("buyYn")); //주문번호 -
                                sub.put("ORDR_NO", contenObj.getString("ordNoDe")); //주문번호 -
                            }else{
                                sub.put("ORDR_YN", contenObj.getString("buyYn")); //주문번호 -
                            }
                            returnArray.add(sub);
                        }
                    }
                }
            }
        }
        retObj.setDataObject("DATA", returnArray);
        return retObj;
    }

}
