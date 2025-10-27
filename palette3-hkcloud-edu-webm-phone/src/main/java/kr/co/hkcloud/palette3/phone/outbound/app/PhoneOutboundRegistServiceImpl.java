package kr.co.hkcloud.palette3.phone.outbound.app;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistService")
public class PhoneOutboundRegistServiceImpl implements PhoneOutboundRegistService
{
    private final TwbComDAO mobjDao;


    /**
     * 아웃바운드 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertObndReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //캠페인 Key 생성 조회
        TelewebJSON objRetKey = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndRegKey", jsonParams);
        String camId = objRetKey.getString("CAM_ID");
        String centTyStr = jsonParams.getString("CENT_TY");

        // 캠페인 KEY
        jsonParams.setString("CAM_ID", camId);
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        System.out.println(formatter.format(date));
        
        String SNDR_KEY = "";
        String ATENT_CUSTOMER = "";
        String WRC_TEL_NO = "";
        String GENDER_CD = "";
        String CUSTOMER_AGE = "";
        String ATENT_CD = "";
        String ATENT_NEWCUSTOMER = "";
        String PROC_ID = jsonParams.getString("CHNG_MAN");
        String CUSTOMER_LOCAL ="";
        String MEMO = "";
        String TALK_USER_KEY = "";
        String EML = "";

        if(!camId.equals("") && camId != null) {
            // 캠페인 등록
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndReg", jsonParams);

            // null 체크
            if(jsonParams.getDataObject("OBND_CUST_ARR") != null) {

                // 캠페인고객대상정보조회
                JSONArray obndCustArr = jsonParams.getDataObject("OBND_CUST_ARR");

                // 캠페인대상 고객 정보가 있는 경우만 
                if(obndCustArr.size() > 0) {
                    @SuppressWarnings("unchecked")
                    Iterator<JSONObject> obndCustIter = obndCustArr.iterator();

                    while(obndCustIter.hasNext()) {

                        JSONObject jsonObj = new JSONObject();
                        jsonObj = obndCustIter.next();

                        System.out.println("jsonObj====>" + jsonObj);
                        TelewebJSON objRetCust = new TelewebJSON();
                        objRetCust.setString("CAM_ID", camId);
                        
                        String custNo = jsonObj.getString("CUST_NO");
                        String phoneNo = jsonObj.getString("MOBIL_NO");
                        String custNm = jsonObj.getString("CUST_NM");
                        String NO = jsonObj.getString("NO");

                        if(custNo.equals("")) {
                        	custNo = formatter.format(date) + NO + phoneNo;
                        }
                        //아웃바운드 정보 수정
//                        if( centTyStr.equals("G") ) {
                        // 공제단말
                        // 고객정보
                        objRetCust.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));             // 회사명
                        objRetCust.setString("CUST_NO", custNo);   // 고객번호
                        
                        objRetCust.setString("CENT_TY", jsonParams.getString("CENT_TY"));// 센터구분
                        objRetCust.setString("IFLW_TY", jsonObj.getString("IFLW_TY"));   // 등록구분(01: 고객추출, 02: 엑셀업로드  03:단건등록)
                        objRetCust.setString("MOBIL_NO", phoneNo); // 핸드폰
                        objRetCust.setString("HOUSE_TEL_NO", jsonObj.getString("HOUSE_TEL_NO")); // 집전화
                        objRetCust.setString("CO_TEL_NO", jsonObj.getString("CO_TEL_NO")); // 회사전화
                        objRetCust.setString("IN_DATE", jsonParams.getString("IN_DATE")); // 기준일자(인입일자)
                        objRetCust.setString("CUST_NM", custNm); // 고객명
                        objRetCust.setString("CAM_TY", jsonParams.getString("CAM_TY")); // 캠페인구분
                        objRetCust.setString("REM", jsonObj.getString("REM")); // 비고
                        objRetCust.setString("REG_MAN", jsonParams.getString("REG_MAN"));   // 생성자
                        objRetCust.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN")); // 수정자

                        // 고객 부가 정보
                        //objRetCust.setString("SECU_NO", jsonObj.getString("SECU_NO"));      // 증권번호
                        //objRetCust.setString("JOB_DTL_NM", jsonObj.getString("JOB_DTL_NM"));// 작업상세명
                        //objRetCust.setString("SALE_START_DATE", jsonObj.getString("SALE_START_DATE")); // 판매개시일자
                        //objRetCust.setString("UPDT_DATE", jsonObj.getString("UPDT_DATE"));  // 갱신일자

                        objRetCust.setString("ETC1", jsonObj.getString("ETC1"));  // 기타1
                        objRetCust.setString("ETC2", jsonObj.getString("ETC2"));  // 기타2
                        objRetCust.setString("ETC3", jsonObj.getString("ETC3"));  // 기타3
                        objRetCust.setString("ETC4", jsonObj.getString("ETC4"));  // 기타4

                        // 캠페인 ID update
                        // mobjDao.update("kr.co.hkcloud.palette.phone.outbound.dao.PhoneOutboundRegistMapper", "updateObndReg", objRetCust);

                        // 캠페인 고객 정보
                        mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCamCustReg", objRetCust);

                        /* 아웃바운드고객 저장 */
                        mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustReg", objRetCust); //고객정보

                        objRetCust.setString("CUSTOMER_ID", custNo);  // 기타4
                        objRetCust.setString("CUSTOMER_NM", custNm);  // 기타4
                        objRetCust.setString("CUSTOMER_PHONE_NO", phoneNo);  // 기타4
                        objRetCust.setString("SNDR_KEY", SNDR_KEY);  // 기타4
                        objRetCust.setString("ATENT_CUSTOMER", ATENT_CUSTOMER);  // 기타4
                        objRetCust.setString("WRC_TEL_NO", WRC_TEL_NO);  // 기타4
                        objRetCust.setString("GENDER_CD", GENDER_CD);  // 기타4
                        objRetCust.setString("CUSTOMER_AGE", CUSTOMER_AGE);  // 기타4
                        objRetCust.setString("ATENT_CD", ATENT_CD);  // 기타4
                        objRetCust.setString("PROC_ID", PROC_ID);  // 기타4
                        objRetCust.setString("CUSTOMER_LOCAL", CUSTOMER_LOCAL);  // 기타4
                        objRetCust.setString("MEMO", MEMO);  // 기타4
                        objRetCust.setString("TALK_USER_KEY", TALK_USER_KEY);  // 기타4
                        objRetCust.setString("EML", EML);  // 기타4
                        mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "mergeCstmrinfo", objRetCust); //고객정보

                        //mobjDao.update("kr.co.hkcloud.palette.phone.outbound.dao.PhoneOutboundRegistMapper", "updateObndCustRegAdd", objRetCust);
                        mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegAdd", objRetCust); //고객추가정보

//                        }else if( centTyStr.equals("E") ){
//                            // 업무지원
//                            objRetCust.setString("RES_NO", jsonObj.getString("RES_NO") );   // 고객번호
//                            objRetCust.setString("CUST_NO", jsonObj.getString("CUST_NO") );   // 조합원번호
//                            objRetCust.setString("CENT_TY", jsonParams.getString("CENT_TY") );// 센터구분
//                            objRetCust.setString("IN_DATE", jsonParams.getString("IN_DATE") ); // 기준일자(인입일자)
//                            objRetCust.setString("CUST_NM", jsonObj.getString("CUST_NM") ); // 고객명
//                            objRetCust.setString("MOBIL_NO", jsonObj.getString("MOBIL_NO") ); // 핸드폰
//                            objRetCust.setString("CARD_NO", jsonObj.getString("CARD_NO") ); // 카드번호
//                            objRetCust.setString("ACT_NO", jsonObj.getString("ACT_NO") ); // 계좌번호
//                            objRetCust.setString("REF1", jsonObj.getString("REF1") ); // 고객정보1
//                            objRetCust.setString("REF2", jsonObj.getString("REF2") ); // 고객정보1
//                            objRetCust.setString("REF3", jsonObj.getString("REF3") ); // 고객정보1
//                            objRetCust.setString("REF4", jsonObj.getString("REF4") ); // 고객정보1
//                            objRetCust.setString("REF5", jsonObj.getString("REF5") ); // 고객정보1
//                            objRetCust.setString("REM", jsonObj.getString("REM") ); // 비고
//                            objRetCust.setString("REG_MAN", jsonParams.getString("REG_MAN") );   // 생성자
//                            objRetCust.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN") ); // 수정자
//                           
//                            // 캠페인 ID update
//                            // mobjDao.update("kr.co.hkcloud.palette.phone.outbound.dao.PhoneOutboundRegistMapper", "updateObndRegE", objRetCust);
//                            
//                            // 캠페인 고객 정보
//                            mobjDao.insert("kr.co.hkcloud.palette.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCamCustRegE", objRetCust);
//                            
//                            /* 아웃바운드고객 저장 */
//                            mobjDao.insert("kr.co.hkcloud.palette.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegE", objRetCust); // 고객정보
//                        }

                    }
                }

            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 key 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndRegKey(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndRegKey", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 고객 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertObndCustReg(JSONArray jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        for(int i = 0; i < jsonParams.size(); i++) {
            objRetParams = (TelewebJSON) mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustReg", jsonParams.get(i));
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드고객 추가정보등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertObndCustRegAdd(JSONArray jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        for(int i = 0; i < jsonParams.size(); i++) {
            objRetParams = (TelewebJSON) mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegAdd", jsonParams.get(i));
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드캠페인고객 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertObndCamCustReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCamCustReg", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteObndReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "deleteObndReg", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 고객을 추출한다. (현재사용 X)
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndCustList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String centTyStr = jsonParams.getString("CENT_TY");

        if(centTyStr.equals("E")) {
            // 업무지원
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndCustListE", jsonParams);
        }
        else if(centTyStr.equals("G")) {
            // 공제단말
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndCustList", jsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 중복 검색.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndCheck(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndCheck", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 고객중복 검색.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectObndCustCheck(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String centTyStr = jsonParams.getString("CENT_TY");

        if(centTyStr.equals("E")) {
            // 업무지원
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndCustCheckE", jsonParams);

        }
        else if(centTyStr.equals("G")) {
            // 공제단말
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "selectObndCustCheck", jsonParams);

        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 정보를 수정한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateObndReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "updateObndReg", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 공제 엑셀 업로드 한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertExcelObndReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 아웃바운드 공제단말 고객등록 - TB_OB_DE_CUST / TB_OB_DE_CUST_ADD
        /*
         * //공통 CENT_TY//센터구분 IFLW_TY//추출01 엑셀02 CAM_ID // 캠페인 ID 기본은 '' REG_MAN //등록자 CHNG_MAN // 수정자
         * 
         * //엑셀 CUST_NO // 주민번호 INGNO // 인가번호 MOBIL_NO // 핸드폰 HOUSE_TEL_NO // 집전화 CO_TEL_NO // 회사전화 TEL_TY // 전화구분 IN_DATE // 인입일자 UPDT_DATE // 갱신일자 CUST_NM // 고객명 REM // 비고 SECU_NO // 증권번호 JOB_DTL_NM //작업상세명
         */

        JSONArray jsonArr = jsonParams.getDataObject();

        if(jsonArr.size() > 0) {

            @SuppressWarnings("unchecked")
            Iterator<JSONObject> jsonArrIter = jsonArr.iterator();

            while(jsonArrIter.hasNext()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj = jsonArrIter.next();

                //고객정보
                mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustReg", jsonObj);

                //고객추가정보
                mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegAdd", jsonObj);

            }

        }

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상적으로 등록 되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 업무 엑셀 업로드 및 고객등록 한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertExcelObndRegE(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 업무
        /*
         * IN_DATE // 인입일자 RES_NO // 주민번호 CUST_NM // 고객명 MOBIL_NO // 핸드폰번호 CARD_NO // 카드번호 ACT_NO // 계좌번호 CUST_NO // 조합원번호 REF1 // 참조1 REF2 // 참조2 REF3 // 참조3 REF4 // 참조4 REF5 // 참조5 REM // 비고 CAM_ID // 캠페인ID REG_DTIM //
         * 등록일자 REG_MAN // 등록자 CHNG_DTIM // 수정일자 CHNG_MAN // 수정자
         */
        // 아웃바운드 업무지원 고객등록 - TB_OB_BI_CUST
        JSONArray jsonArr = jsonParams.getDataObject();
        for(int i = 0; i < jsonArr.size(); i++) {
            mobjDao.insert("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistMapper", "insertObndCustRegE", jsonArr.get(i));
        }

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상적으로 등록 되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }

}
