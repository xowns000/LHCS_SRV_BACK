package kr.co.hkcloud.palette3.phone.main.app;


import java.net.URI;
import java.util.Iterator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("PhoneMainService")
public class PhoneMainServiceImpl implements PhoneMainService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;
    private final PaletteCmmnService   paletteCmmnService;


    /**
     * 상담이력 등록 Arthur.Kim 2021.10.15
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON cnsltHistRegist(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String seq = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL", "PLT");
        String seq2 = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL_DTL", "PLT");

        // 아웃바운드 세팅
        String FISH_TY = "";
        String PROC_TY = "";
        String CAM_ID = "";

        // 콜백 세팅
        String EOT_TY_RLT = "";
        String PROC_CODE = "";
        String CLBK_NO = "";

        // 예약콜 저장 여부
        String RESV_SEQ = "";
        String RESVE_CALL = "";
        String RESV_DATE = "";
        String RESV_TIME_HH = "";
        String RESV_TIME_MM = "";
        String RESV_TEL_NO = "";

        // 예약콜 결과 저장
        String RESV_SEQ_RESULT = "";        //예약콜ID 
        String FISH_YN = "";        //종료여부

        String CUSTOMER_ID = "";        //고객ID  

        // 아웃바운드 데이터 세팅
        FISH_TY = mjsonParams.getString("FISH_TY", 0);
        PROC_TY = mjsonParams.getString("PROC_TY", 0);
        CAM_ID = mjsonParams.getString("CAM_ID", 0);

        // 아웃바운드 데이터 세팅
        EOT_TY_RLT = mjsonParams.getString("EOT_TY_RLT", 0);
        PROC_CODE = mjsonParams.getString("PROC_CODE", 0);
        CLBK_NO = mjsonParams.getString("CLBK_NO", 0);

        // 예약콜 데이터 세팅
        RESVE_CALL = mjsonParams.getString("RESVE_CALL", 0);
        RESV_DATE = mjsonParams.getString("RESV_DATE", 0);
        RESV_TIME_HH = mjsonParams.getString("RESV_TIME_HH", 0);
        RESV_TIME_MM = mjsonParams.getString("RESV_TIME_MM", 0);
        RESV_TEL_NO = mjsonParams.getString("RESV_TEL_NO", 0);

        RESV_SEQ_RESULT = mjsonParams.getString("RESV_SEQ", 0);
        FISH_YN = mjsonParams.getString("FISH_YN", 0);

        CUSTOMER_ID = mjsonParams.getString("CUSTOMER_ID", 0);

        // 기업정보명 세팅
        String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY", 0);                 // PaletteUserContextSupport.getCurrentUser().getCustcoId();

        log.debug("RESV_SEQ_RESULT: " + RESV_SEQ_RESULT);
        log.debug("FISH_YN: " + FISH_YN);

        mjsonParams.setString("CNSL_HIST_NO", seq);
        mjsonParams.setString("CNSL_HIST_DTL_NO", seq2);
        mjsonParams.setString("CUSTCO_ID", custcoId);

        if(!"".equals(FISH_TY) || !"".equals(PROC_TY)) {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateObndCallRlt", mjsonParams);
            
            /* 퇴직연금 아웃바운드관련 */
            if(mjsonParams.getString("ASP_NEWCUST_KEY").equals("pension_02")) {
            	objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updatePensionOut", mjsonParams);
            }
            
        }
        else if(!"".equals(EOT_TY_RLT) || !"".equals(PROC_CODE)) {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateClbkInqDiv", mjsonParams);
        }
        else if(!"".equals(RESVE_CALL)) {
            String seq3 = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL_RSV", "PLT");
            mjsonParams.setString("RESV_SEQ", seq3);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "resveCallRegistProcess", mjsonParams);
        }
        else if(!"".equals(RESV_SEQ_RESULT)) {
            mjsonParams.setString("RESV_SEQ_RESULT", RESV_SEQ_RESULT);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateResvInqDiv", mjsonParams);
        }

        //상담통합이력테이블 저장
        TelewebJSON cnslUnityJsonParams = new TelewebJSON();
        cnslUnityJsonParams.setString("UNITY_HST_ID", innbCreatCmmnService.getSeqNo("PLT_CNSL_UNITY_HST", "PLT"));

        //cnslUnityJsonParams.setString("CUSTCO_ID", custcoId);
        cnslUnityJsonParams.setString("CNSL_CUST_KEY", mjsonParams.getString("CNSL_CUST_KEY"));

        cnslUnityJsonParams.setString("JOBBY_CNSL_HST_ID", seq);
        cnslUnityJsonParams.setString("CNSL_DIV", mjsonParams.getString("CALL_TY"));
        cnslUnityJsonParams.setString("CSTMR_TELNO", mjsonParams.getString("CUST_TEL_NO"));

        cnslUnityJsonParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));     // Arthur.Kim

        cnslUnityJsonParams.setString("PDS", mjsonParams.getString("PDS"));
        
        // Arthur.Kim 고객ID 확인)전화상담 2021.10.20
        if(CUSTOMER_ID != null && !CUSTOMER_ID.isEmpty() && CUSTOMER_ID != "" && CUSTOMER_ID.length() > 0) {
            cnslUnityJsonParams.setString("CSTMR_ID", CUSTOMER_ID);
        }
        else {
            cnslUnityJsonParams.setString("CSTMR_ID", mjsonParams.getString("CSTMR_ID"));
        }

        cnslUnityJsonParams.setString("REGR_ID", mjsonParams.getString("REG_MAN"));
        paletteCmmnService.insertRtnCnslUnityHst(cnslUnityJsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "PhoneMainConsultingRecordCallRegist", mjsonParams);      //전화상담이력 등록
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "ConsultingRecordCallRegistDetail", mjsonParams);         //전화상담이력상세정보 등록
        return objRetParams;
    }


    /**
     * 호전환 팝업 그룹조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON cnvrsList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "cnvrsList", mjsonParams);
    }


    /**
     * 호전환 팝업 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON cnvrsDetailList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "cnvrsDetailList", mjsonParams);
    }


    @Override
    public TelewebJSON selectObndMainList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectObndMainList", mjsonParams);
    }


    /**
     * 캠페인조회 Arthur.Kim 2021.10.12
     */
    @Override
    public TelewebJSON selectCamMainList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectCamMainList", mjsonParams);
    }


    @Override
    public TelewebJSON srvyProcList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectSrvyProcList", mjsonParams);
    }


    @Override
    public TelewebJSON srvyProcQList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(mjsonParams);
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectSrvyProcQList", mjsonParams);

        // 문항 건수가 있는 경우만
        if(objRetParams.getSize() > 0) {

            // 주관식 객관식 (01 객관식/ 02 주관식)
            objParams.setString("ANS_TYPE", objRetParams.getString("ANS_TYPE", 0));
            // 설문번호
            objParams.setString("QUEST_ID", objRetParams.getString("QUEST_ID", 0));
            // 문항번호
            objParams.setString("Q_NO", objRetParams.getString("Q_NO", 0));
            // 고객번호
            objParams.setString("CUST_NO", mjsonParams.getString("CUST_NO"));
            //캠페인ID
            objParams.setString("CAM_ID", mjsonParams.getString("CAM_ID"));

            if(objParams.getString("ANS_TYPE").equals("02")) {
                // 주관식
                objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectRtnResult_short", objParams);
                objRetParams2.getDataObject("DATA");
                objRetParams.setDataObject("SHORT", objRetParams2.getDataObject("DATA"));
            }
            else {
                // 객관식
                objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectRtnResult_multi", objParams);
                // 답변
                objRetParams2.getDataObject("DATA");
                objRetParams.setDataObject("MULTI", objRetParams2.getDataObject("DATA"));
            }

        }

        return objRetParams;
    }


    @Override
    public TelewebJSON mergeSrvyProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "mergeSrvyProc", mjsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON cntobndcallModify(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateTryCntObndCall", mjsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON obndcallModify(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateObndCallRlt", mjsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON callbackRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "callbackRegistProcess", mjsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON selectClbkInqList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //콜백 목록을 조회한다.
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectClbkInqList", jsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 목록을 조회한다. Arthur.Kim 2021.10.12
     */
    @Override
    public TelewebJSON selectClbkMngInqire(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //콜백 목록을 조회한다.
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectClbkMngInqire", jsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    @Override
    public TelewebJSON updateClbkInqDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //콜백 진행상태 수정
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateClbkInqDiv", jsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    @Override
    public TelewebJSON updateClbkTryCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //콜백 진행상태 수정
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateClbkTryCnt", jsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    @Override
    public TelewebJSON callbackObundList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objRetParams2 = new TelewebJSON();
        TelewebJSON objRetParams3 = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectObundCheack", jsonParams);
        objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectCallbackCheack", jsonParams);

        objRetParams3.setDataObject("OBUND", objRetParams.getDataObject("DATA"));
        objRetParams3.setDataObject("CALLBACK", objRetParams2.getDataObject("DATA"));

        return objRetParams3;
    }


    @Override
    public TelewebJSON resveCallRegistProcess(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "resveCallRegistProcess", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON resveCallList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "resveCallList", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON updateResvTryCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateResvTryCnt", jsonParams);
        return objRetParams;
    }


    /**
     * 예약 조회 Arthur.Kim 2021.10.13
     */
    @Override
    public TelewebJSON selectSchdulList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectSchdulList", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON selectScheduleManageList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectScheduleManageList", jsonParams);
        return objRetParams;
    }


    /**
     * 고객정보/메모및[B/L]저장 2021.10.16
     */
    @Override
    public TelewebJSON mergeCstmrinfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        JSONObject jsonObj = new JSONObject();
        JSONArray jsonAry = new JSONArray();

        String CUSTOMER_ID = "";
        String seq = innbCreatCmmnService.getSeqNo("PLT_CUS", "CUS");

        CUSTOMER_ID = jsonParams.getString("CUSTOMER_ID", 0);

        /* 고객ID */
        if(CUSTOMER_ID == null || CUSTOMER_ID.isEmpty() || CUSTOMER_ID.equals("") || CUSTOMER_ID.length() == 0) {

            jsonParams.setString("CUSTOMER_ID", seq);

            jsonObj.put("CUSTOMER_ID", jsonParams.getString("CUSTOMER_ID"));
            jsonAry.add(jsonObj);
        }

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "mergeCstmrinfo", jsonParams);

        if(jsonAry.size() > 0) {
            objRetParams.setDataObject(jsonAry);
        }

        return objRetParams;
    }


    @Override
    public TelewebJSON updateCstmrinfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updateCstmrinfo", jsonParams);
        return objRetParams;
    }


    @Override
    public TelewebJSON selectIplxtnnoInqire(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectIplxtnnoInqire", jsonParams);
        return objRetParams;
    }


    /**
     * 고객등급 조회
     */
    @Override
    public TelewebJSON getBLVIP(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //TelewebJSON countBotalk = new TelewebJSON(jsonParams);
        log.debug("jsonParams" + jsonParams);

        log.debug("come here ======= objRetParams" + objRetParams);

        TelewebJSON resultParams = new TelewebJSON(jsonParams);

        try {
            String inNum = jsonParams.getString("IN_NUM");
            String phnNo = jsonParams.getString("PHN_NO");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://139.150.75.152/api/db_select.php?mode=grade&ext=" + inNum + "&tel=" + phnNo;     //외부 API주소

            JSONObject jsonObject = new JSONObject();

            //외부 API로 보낼 HEADER부분
            header.add("Content-Type", "application/x-www-form-urlencoded");
            //header.add("X-Bottalks-Auth-key", certifyKey);

            log.debug("jsonObject =====================" + jsonObject);

            //외부 API로 보낼 BODY부분
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("mode", "monView");
            parameters.add("ext", inNum);
            parameters.add("tel", phnNo);

            log.debug("parameters ====" + parameters.toString());
            log.debug("header ====" + header.toString());
            log.debug("url=======" + url);

            //외부 API로 보내기
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

            //외부 API로 보낸 결과 저장
            String result = restTemplate.postForObject(new URI(url), request, String.class);

            log.debug("result ====" + result);

            //STRING데이터를 JSON데이터로 변환
            JSONObject jsonObj = JSONObject.fromObject(result);

            resultParams.setString("tel", jsonObj.getString("tel"));
            resultParams.setString("grade", jsonObj.getString("grade"));

        }
        catch(HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        }
        catch(Exception e) {

            System.out.println(e.toString());
        }

        return resultParams;
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertBotalk", jsonParams);
        //}
        //else {

        //}
    }


    /**
     * 고객등급 저장
     */
    @Override
    public TelewebJSON registBLVIP(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //TelewebJSON countBotalk = new TelewebJSON(jsonParams);
        log.debug("jsonParams" + jsonParams);

        log.debug("come here ======= objRetParams" + objRetParams);

        TelewebJSON resultParams = new TelewebJSON(jsonParams);

        try {
            String inNum = jsonParams.getString("IN_NUM");
            String phnNo = jsonParams.getString("PHN_NO");
            String grade = jsonParams.getString("GRADE");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://139.150.75.152/api/db_select.php?mode=grade&ext=" + inNum + "&tel=" + phnNo + "&grade=" + grade;     //외부 API주소

            JSONObject jsonObject = new JSONObject();

            //외부 API로 보낼 HEADER부분
            header.add("Content-Type", "application/x-www-form-urlencoded");
            //header.add("X-Bottalks-Auth-key", certifyKey);

            log.debug("jsonObject =====================" + jsonObject);

            //외부 API로 보낼 BODY부분
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("mode", "monView");
            parameters.add("ext", inNum);
            parameters.add("tel", phnNo);
            parameters.add("grade", grade);

            log.debug("parameters ====" + parameters.toString());
            log.debug("header ====" + header.toString());
            log.debug("url=======" + url);

            //외부 API로 보내기
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

            //외부 API로 보낸 결과 저장
            String result = restTemplate.postForObject(new URI(url), request, String.class);

            log.debug("result ====" + result);

            //STRING데이터를 JSON데이터로 변환
            JSONObject jsonObj = JSONObject.fromObject(result);

            resultParams.setString("result", jsonObj.getString("result"));

        }
        catch(HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        }
        catch(Exception e) {

            System.out.println(e.toString());
        }

        return resultParams;
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertBotalk", jsonParams);
        //}
        //else {

        //}
    }


    /**
     * 전화받기
     */
    @Override
    public TelewebJSON callReceive(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //TelewebJSON countBotalk = new TelewebJSON(jsonParams);
        log.debug("jsonParams" + jsonParams);

        log.debug("come here ======= objRetParams" + objRetParams);

        TelewebJSON resultParams = new TelewebJSON(jsonParams);

        try {
            String fanvilIp = jsonParams.getString("FANVIL_IP");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://admin:meta12!!@" + fanvilIp + "/cgi-bin/ConfigManApp.com?key=F_ACCEPT";     //외부 API주소
            //String url = "http://139.150.75.152/api/db_select.php?mode=grade&ext=" + inNum + "&tel=" + phnNo + "&grade=" + grade;     //외부 API주소

            JSONObject jsonObject = new JSONObject();

            //외부 API로 보낼 HEADER부분
            header.add("Content-Type", "application/x-www-form-urlencoded");
            //header.add("X-Bottalks-Auth-key", certifyKey);

            log.debug("jsonObject =====================" + jsonObject);

            //외부 API로 보낼 BODY부분
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("key", "F_ACCEPT");

            log.debug("parameters ====" + parameters.toString());
            log.debug("header ====" + header.toString());
            log.debug("url=======" + url);

            //외부 API로 보내기
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

            //외부 API로 보낸 결과 저장
            String result = restTemplate.postForObject(new URI(url), request, String.class);

            log.debug("result ====" + result);

            //STRING데이터를 JSON데이터로 변환
            JSONObject jsonObj = JSONObject.fromObject(result);

            resultParams.setString("result", jsonObj.getString("result"));

        }
        catch(HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        }
        catch(Exception e) {

            System.out.println(e.toString());
        }

        return resultParams;
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertBotalk", jsonParams);
        //}
        //else {

        //}
    }
    
    /**
     * 상담이력 등록 Arthur.Kim 2021.10.15
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON cnslForceRegist(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String seq = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL", "PLT");
        String seq2 = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL_DTL", "PLT");
        String seq3 = innbCreatCmmnService.getSeqNo("PLT_CNSL_UNITY_HST", "PLT");

        mjsonParams.setString("CNSL_HIST_NO", seq);
        mjsonParams.setString("CNSL_HIST_DTL_NO", seq2);
        mjsonParams.setString("UNITY_HST_ID", seq3);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertForceUnity", mjsonParams);         //톱합이력등록
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertForceCnsl", mjsonParams);      	 //전화상담이력등록
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertForceCnslDet", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    
    
    /**
     * 신규고객팝업요류 처리
     * 221201
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON searchCustId(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "searchCustId", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    
    /**
     * 만트럭 차량정보조회
     * 221208
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON getManCarInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "getManCarInfo", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    
    /**
     * 만트럭 차량정보저장
     * 221209
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON insertManCarInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON();
        if(mjsonParams.getDataObject("CAR_LIST") != null) {

            // 캠페인고객대상정보조회
            JSONArray carArr = mjsonParams.getDataObject("CAR_LIST");

            // 캠페인대상 고객 정보가 있는 경우만 
            if(carArr.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> carIter = carArr.iterator();

                while(carIter.hasNext()) {

                    JSONObject jsonObj = new JSONObject();
                    jsonObj = carIter.next();

                    System.out.println("jsonObj====>" + jsonObj);
                    TelewebJSON objRetCar = new TelewebJSON();
                    objRetCar.setString("EURO_STANDARD", jsonObj.getString("EURO_STANDARD"));             		// 전화번호
                    objRetCar.setString("REG_NO", jsonObj.getString("REG_NO"));             		// 전화번호
                    objRetCar.setString("CAR_NO", jsonObj.getString("CAR_NO"));             		// 전화번호
                    objRetCar.setString("YEAR", jsonObj.getString("YEAR"));             		// 전화번호
                    objRetCar.setString("REG_DATE", jsonObj.getString("REG_DATE"));             		// 전화번호
                    objRetCar.setString("CUST_NM", jsonObj.getString("CUST_NM"));             		// 전화번호
                    objRetCar.setString("POST_CD", jsonObj.getString("POST_CD"));             		// 전화번호
                    objRetCar.setString("POST", jsonObj.getString("POST"));             		// 전화번호
                    objRetCar.setString("POST_DET", jsonObj.getString("POST_DET"));             		// 전화번호
                    objRetCar.setString("PHONE_NO_1", jsonObj.getString("PHONE_NO_1"));             		// 전화번호
                    objRetCar.setString("PHONE_NO_2", jsonObj.getString("PHONE_NO_2"));             		// 전화번호
                    objRetCar.setString("PHONE_NO_3", jsonObj.getString("PHONE_NO_3"));             		// 전화번호
                    objRetCar.setString("PHONE_NO_REAL", jsonObj.getString("PHONE_NO_REAL"));             		// 전화번호
                    objRetCar.setString("PHONE_NM_1", jsonObj.getString("PHONE_NM_1"));             		// 전화번호
                    objRetCar.setString("PHONE_NM_2", jsonObj.getString("PHONE_NM_2"));             		// 전화번호
                    objRetCar.setString("PHONE_NM_3", jsonObj.getString("PHONE_NM_3"));             		// 전화번호
                    objRetCar.setString("PHONE_NM_4", jsonObj.getString("PHONE_NM_4"));             		// 전화번호
                    objRetCar.setString("CO_NM", jsonObj.getString("CO_NM"));             		// 전화번호
                    objRetCar.setString("OWNER_NM", jsonObj.getString("OWNER_NM"));             		// 전화번호
                    objRetCar.setString("HORSEPOWER", jsonObj.getString("HORSEPOWER"));             		// 전화번호
                    objRetCar.setString("CAR_TY", jsonObj.getString("CAR_TY"));             		// 전화번호
                    objRetCar.setString("CUST_TY", jsonObj.getString("CUST_TY"));             		// 전화번호
                    objRetCar.setString("EML", jsonObj.getString("EML"));             		// 전화번호
                    objRetCar.setString("PRIVATE_AGREE", jsonObj.getString("PRIVATE_AGREE"));             		// 전화번호
                    objRetCar.setString("AGREE_DATE", jsonObj.getString("AGREE_DATE"));             		// 전화번호
                    objRetCar.setString("AGREE_DEALER", jsonObj.getString("AGREE_DEALER"));             		// 전화번호
                    objRetCar.setString("AGREE_MANAGER", jsonObj.getString("AGREE_MANAGER"));             		// 전화번호
                    objRetCar.setString("PDI_DATE", jsonObj.getString("PDI_DATE"));             		// 전화번호
                    objRetCar.setString("VEHICLE_TY", jsonObj.getString("VEHICLE_TY"));             		// 전화번호
                    objRetCar.setString("RMC", jsonObj.getString("RMC"));             		// 전화번호
                    objRetCar.setString("ENGINE_NO", jsonObj.getString("ENGINE_NO"));             		// 전화번호
                    objRetCar.setString("ENGINE_TY", jsonObj.getString("ENGINE_TY"));             		// 전화번호
                    objRetCar.setString("YEAR7_PAC", jsonObj.getString("YEAR7_PAC"));             		// 전화번호
                    objRetCar.setString("YEAR7_SOLD_DEALER", jsonObj.getString("YEAR7_SOLD_DEALER"));             		// 전화번호
                    objRetCar.setString("REPORT_AGREE", jsonObj.getString("REPORT_AGREE"));             		// 전화번호
                    objRetCar.setString("REPORT_AGREE_DATE", jsonObj.getString("REPORT_AGREE_DATE"));             		// 전화번호
                    objRetCar.setString("REPORT_AGREE_DEALER", jsonObj.getString("REPORT_AGREE_DEALER"));             		// 전화번호
                    objRetCar.setString("REPORT_AGREE_WRITER", jsonObj.getString("REPORT_AGREE_WRITER"));             		// 전화번호
                    objRetCar.setString("OVER_50T", jsonObj.getString("OVER_50T"));             		// 전화번호

                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertManCarInfo", objRetCar);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 만트럭 지역별 서비스센터조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON getManCenter(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "getManCenter", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    

    /**
     * PDS조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON getPds(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "getPds", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 정보 조회
     * 230221
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON getPensionOutInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "getPensionOutInfo", mjsonParams);       //전화상담이력상세정보등록
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 정보 저장
     * 230221
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON insertPensionOutInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        System.out.println("mjsonParams====>" + mjsonParams);
    	TelewebJSON objRetParams = new TelewebJSON();
    	String userId = mjsonParams.getString("ASP_USER_ID");
    	String camNm = mjsonParams.getString("CAM_NM");
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "selectPensionCamId", mjsonParams);
        String camId = objRetParams.getString("CAM_ID");
        log.debug("mjsonParams"+mjsonParams);
        if(mjsonParams.getDataObject("PENSION_DATA") != null) {

            // 캠페인고객대상정보조회
            JSONArray pensionData = mjsonParams.getDataObject("PENSION_DATA");

            // 캠페인대상 고객 정보가 있는 경우만 
            if(pensionData.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> pensionIter = pensionData.iterator();

                while(pensionIter.hasNext()) {

                    JSONObject jsonObj = new JSONObject();
                    jsonObj = pensionIter.next();

                    System.out.println("jsonObj====>" + jsonObj);
                    TelewebJSON objRetPension = new TelewebJSON();
                    objRetPension.setString("USER_ID", userId);           								// 사용자명
                    objRetPension.setString("CAM_ID", camId);            	 							// 전화번호
                    objRetPension.setString("PLEN", camNm);             								// 플랜명
                    objRetPension.setString("PHONE_NO", jsonObj.getString("MOBIL_NO"));            	 	// 전화번호
                    objRetPension.setString("POST", jsonObj.getString("ETC1"));             			// 주소
                    objRetPension.setString("SITE", jsonObj.getString("ETC2"));             			// 관할지사

                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "insertPensionOutInfo", objRetPension);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 정보 수정
     * 230221
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */

    @Override
    public TelewebJSON updatePensionOutInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON();
        if(mjsonParams.getDataObject("NEW_DATA") != null) {

            // 캠페인고객대상정보조회
            JSONArray newData = mjsonParams.getDataObject("NEW_DATA");
            // 캠페인고객대상정보조회
            JSONArray orgData = mjsonParams.getDataObject("ORG_DATA");

            // 캠페인대상 고객 정보가 있는 경우만 
            if(newData.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> newIter = newData.iterator();
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> orgIter = orgData.iterator();

                while(newIter.hasNext()) {

                    JSONObject jsonNewObj = new JSONObject();
                    JSONObject jsonOrgObj = new JSONObject();
                    jsonNewObj = newIter.next();
                    jsonOrgObj = orgIter.next();

                    System.out.println("jsonNewObj====>" + jsonNewObj);
                    System.out.println("jsonOrgObj====>" + jsonOrgObj);
                    TelewebJSON objRetPension = new TelewebJSON();
                    objRetPension.setString("PHONE_NO", mjsonParams.getString("PHONE_NO"));             // 전화번호
                    objRetPension.setString("USER_ID", mjsonParams.getString("USER_ID"));             	// 사용자명
                    objRetPension.setString("ORG_PLEN", jsonOrgObj.getString("PLEN"));             		// 수정전 플랜명
                    objRetPension.setString("ORG_POST", jsonOrgObj.getString("POST"));             		// 수정전 주소
                    objRetPension.setString("ORG_SITE", jsonOrgObj.getString("SITE"));             		// 수정전 관할지사
                    objRetPension.setString("PLEN", jsonNewObj.getString("PLEN"));             			// 수정후 플랜명
                    objRetPension.setString("POST", jsonNewObj.getString("POST"));             			// 수정후 주소
                    objRetPension.setString("SITE", jsonNewObj.getString("SITE"));             			// 수정후 관할지사

                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.main.dao.PhoneMainMapper", "updatePensionOutInfo", objRetPension);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }
}