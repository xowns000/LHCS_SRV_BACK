package kr.co.hkcloud.palette3.integration.commerce_api.lotteon.service;

import java.lang.reflect.InvocationTargetException;
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
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * packageName    : kr.co.hkcloud.palette3.integration.commerce_api.lotteon.service
 * fileName       : LotteonServiceImpl
 * author         : NJY
 * date           : 2024-09-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-20        NJY       최초 생성
 * </pre>
 */
@Slf4j
@Service(LotteonServiceImpl.BEAN_ID)
public class LotteonServiceImpl extends CommerceLkagServiceImpl implements CommerceApiService{

    public static final String BEAN_ID = "LOTTEON";

    public LotteonServiceImpl(TwbComDAO mobjDao, PaletteJsonUtils paletteJsonUtils, PaletteLkagRestTemplate paletteLkagRestTemplate,
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
        // 롯데온 구현
        log.info(BEAN_ID + " >> authentication");
        return objRetParams;
    }

    @Override
    public TelewebJSON call_api(TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {

        TelewebJSON objCustcoLkagApi = this.selectCustcoLkagApi(jsonParams);
        log.info(BEAN_ID + " >> orderList");
        log.info(BEAN_ID + " >> objCustcoLkagApi" + objCustcoLkagApi.toString());

        JSONObject baseDataObj = (JSONObject) objCustcoLkagApi.getDataObject(TwbCmmnConst.G_DATA).get(0);
        JSONArray rspnsDataObj = objCustcoLkagApi.getDataObject("CUSTCO_LKAG_RSPNS");
        if (rspnsDataObj == null) {
            rspnsDataObj = new JSONArray();
        }

        String trNo = baseDataObj.getString("ETC_01"); //롯데ON의 경우 거래처번호
        String trGrpCd = baseDataObj.getString("ETC_02"); //롯데ON의 경우 거래처그룹코드
        jsonParams.setString("trNo", trNo);
        jsonParams.setString("trGrpCd", trGrpCd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " +baseDataObj.getString("ACS_ACNT_KEY"));
        headers.add("Content-Type", "application/json;charset=utf-8");

        String sUrl = super.getUrl( jsonParams, objCustcoLkagApi );
        //
        Object query = super.getQuery( jsonParams, objCustcoLkagApi );
        log.info(" queryqueryqueryqueryqueryquery:::::::::::::::::::::::::      "+ String.valueOf(query));
        TelewebJSON objRetRspns = super.getResponse( objCustcoLkagApi, sUrl, query, headers, HttpMethod.valueOf(baseDataObj.getString("RQST_SE_CD_VL").toUpperCase()) );

        return objRetRspns;
    }

    @Override
    public TelewebJSON call_batch_api(
        TelewebJSON jsonParams) throws TelewebAppException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        TelewebJSON retObj = new TelewebJSON(jsonParams);

        JSONArray returnArray = new JSONArray();
        TelewebJSON receiveObj = new TelewebJSON(jsonParams);

        if (StringUtils.isEmpty(jsonParams.getString("slrInqProcStatCd"))) {     //  롯데온 답변 여부
            jsonParams.setString("slrInqProcStatCd", "UNANS");                         // 미답변만 조회
        }
        // API호출.
        receiveObj = this.call_api(jsonParams);
        //  롯데온 문의 구현
        if (receiveObj.getDataObject("DATA2") != null) {
            JSONObject responseObj = (JSONObject) receiveObj.getDataObject("DATA2").get(0);
            JSONArray contentsArray = new JSONArray();
            //  고객문의 : INQRY_CUST,  상품문의 INQRY_GDS
            if ("INQRY_CUST".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {
                contentsArray = (JSONArray) responseObj.get("rsltList");
                if (contentsArray != null && contentsArray.size() > 0) {
                    for (Object o : contentsArray) {
                        JSONObject sub = new JSONObject();
                        JSONObject contenObj = (JSONObject) o;

                        sub.put("BEAN_ID", "LOTTEON");
                        sub.put("BEAN_TP", "CUST");

                        sub.put("SNDR_KEY", jsonParams.getString("SNDR_KEY") ); //발송자_키
                        sub.put("INQRY_NO", contenObj.getString("slrInqNo") ); //문의번호 -
                        sub.put("GDS_NO", contenObj.getString("spdNo")); //상품번호 -
                        sub.put("GDS_NM", contenObj.getString("spdNm")); //상품명 -
                        sub.put("ETC_2", contenObj.getString("sitmNo")); // 판매자 상품 단품번호(상품Q&A답변시 필요)

                        sub.put("WRTR_ID", "unknown"); // 작성자아이디 -
                        sub.put("WRTR_NM", "성명미상"); // 작성자아이디 -
                        sub.put("WRT_DT", contenObj.getString("accpDttm")); // 작성일시.-
                        sub.put("INQRY_TTL", contenObj.getString("inqTtl")); // 문의제목.-
                        sub.put("INQRY_CN", contenObj.getString("inqCnts")); // 문의내용.-
                        sub.put("INQRY_TYPE", contenObj.getString("vocTypNm")); //문의유형 -
                        switch(contenObj.getString("vocLcsfCd")){
                            case "IC00000265" :
                                sub.put("INQRY_TYPE", "상품"); //문의유형 -
                                break;
                            case "IC00000266" :
                                sub.put("INQRY_TYPE", "배송"); //문의유형 -
                                break;
                            case "IC00000267" :
                                sub.put("INQRY_TYPE", "교환"); //문의유형 -
                                break;
                            default:
                                sub.put("INQRY_TYPE", "기타"); //문의유형 -
                                break;
                        }


                        // 구매여부가 Y인 경우
                        if(StringUtils.isEmpty(contenObj.getString("odNo"))){
                            sub.put("ORDR_YN", "N"); //주문번호 -
                        }else{
                            sub.put("ORDR_YN", "Y"); //주문번호 -
                            sub.put("ORDR_NO", contenObj.getString("odNo")); //주문번호 -
                        }
                        log.info("contenObj.getString(\"atflList\").getClass().getName() :::::: >>>>  "+ contenObj.getString("atflList").getClass().getName());
                        log.info("contenObj.getString(\"atflList\") :::::: >>>>  "+ contenObj.getString("atflList"));
                        try{
                        if(StringUtils.isNotEmpty(contenObj.getString("atflList")) && !"null".equals(contenObj.getString("atflList"))){
                            JSONArray imgArray = JSONArray.fromObject(contenObj.get("atflList"));
                            if (imgArray != null && imgArray.size() > 0) {
                                String[] imgUrls = new String[imgArray.size()];
                                for (int i = 0; i < imgArray.size(); i++) {
                                    JSONObject imgObj = (JSONObject) imgArray.get(i);
                                    imgUrls[i] = imgObj.getString("atflUrl");
                                }
                                String urlString = String.join(",", imgUrls);
                                sub.put("ULD_IMG_URL", urlString); // 이미지 ',' 구분자로 넣기
                            }
                        }}catch(Exception e){
                            log.info("make img url Strings :::::: >>>>>>    "+ e );
                        }
                        returnArray.add(sub);
                    }
                }

            } else if ("INQRY_GDS".equals(jsonParams.getString("BBS_INQRY_TYPE_CD"))) {

                //상품문의
                contentsArray = (JSONArray) responseObj.get("data");
                if (contentsArray != null && contentsArray.size() > 0) {
                    for (Object o : contentsArray) {
                        JSONObject sub = new JSONObject();
                        JSONObject contenObj = (JSONObject) o;

                        sub.put("BEAN_ID", "LOTTEON");
                        sub.put("BEAN_TP", "GDS");

                        sub.put("SNDR_KEY", jsonParams.getString("SNDR_KEY") ); //발송자_키
                        sub.put("INQRY_NO", contenObj.getString("pdQnaNo") ); //문의번호
                        sub.put("GDS_NO", contenObj.getString("spdNo")); //상품번호
                        sub.put("ETC_2", contenObj.getString("sitmNo")); // 판매자 상품 단품번호(상품Q&A답변시 필요)
                        sub.put("GDS_NM", "상세보기 참조"); //상품명 -

                        sub.put("WRTR_ID", "unknown"); // 작성자아이디 -
                        sub.put("WRTR_NM", "성명미상"); // 작성자아이디 -
                        sub.put("WRT_DT", contenObj.getString("regDttm")); // 작성일시.-
                        sub.put("INQRY_TTL", "상품 문의입니다."); // 문의제목.-
                        sub.put("INQRY_CN", contenObj.getString("qstCnts")); // 문의내용.-
                        sub.put("INQRY_TYPE", contenObj.getString("qstTypCd")); //문의유형 -
                        if("ETC".equals(contenObj.getString("qstTypCd"))){
                            sub.put("INQRY_TYPE", "기타"); //문의유형 -
                        }else{
                            sub.put("INQRY_TYPE", "상품"); //문의유형 -
                        }

                        sub.put("ORDR_YN", "N"); //주문번호 -

                        returnArray.add(sub);
                    }
                }
            }
        }
        retObj.setDataObject("DATA", returnArray);
        return retObj;
    }

}
