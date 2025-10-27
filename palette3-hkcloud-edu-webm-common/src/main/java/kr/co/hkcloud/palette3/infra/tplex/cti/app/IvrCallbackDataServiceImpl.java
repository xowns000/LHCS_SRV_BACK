package kr.co.hkcloud.palette3.infra.tplex.cti.app;

import java.net.URI;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 칭찬콜관리 서비스 구현체 클래스
 * 
 * @author dckim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("IvrCallbackDataService")
public class IvrCallbackDataServiceImpl implements IvrCallbackDataService {

    private final TwbComDAO mobjDao;
    private final SseService sseService;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SettingCustomerInformationListService settingCustomerInformationListService;

    public String erroMsg(String resultCode) {
        String ErrorMsg = ""; // 에러메시지와 에러코드 전부 임시값입니다.
        switch (resultCode) {
            case "0":
                ErrorMsg = "정상적으로 등록되었습니다.";
                break;
            case "1":
                ErrorMsg = "등록되지 않은 상담사 ID 입니다.";
                break;
            case "2":
                ErrorMsg = "중복된 큐가 있습니다.";
                break;
            case "3":
                ErrorMsg = "존재하지 않는 스키마 입니다.";
                break;
            case "4":
                ErrorMsg = "존재하지 않는 고객사 입니다.";
                break;
            case "5":
                ErrorMsg = "FDATA 값이 없습니다.";
                break;
            case "6":
                ErrorMsg = "FKEY 값이 없습니다.";
                break;
            case "7":
                ErrorMsg = "Asp_cust_code is wrong.";
                break;
            case "8":
                ErrorMsg = "Asp_cust_code is null.";
                break;
            case "9":
                ErrorMsg = "call number is null.";
                break;
            case "10":
                ErrorMsg = "배분 가능한 상담사가 없습니다.";
                break;

        }
        return ErrorMsg;
    }

    @Override
    //    public TelewebJSON callbackRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException {
    public String callbackRegistProcess(HttpServletRequest request) throws TelewebAppException {

        /* 응답코드 반환을 위한 JSON 객체 */
        JSONObject jsonObject = new JSONObject();

        /* HttpServletRequest 변환 객체  */
        TelewebJSON objClbkParams = this.setClbkInfo(request);

        /* 콜백 저장을 위한 객체 */
        TelewebJSON objSetParams = new TelewebJSON();

        if (objClbkParams.containsKey("result_code")) {
            jsonObject.put("result_code", objClbkParams.getString("result_code"));
            jsonObject.put("result_msg", erroMsg(objClbkParams.getString("result_code")));
            return jsonObject.toString();
        }

        /* ASP_CUST_CODE로 고객사 schemaId와 custcoId 조회 */

        objSetParams.setString("ASP_CUST_KEY", objClbkParams.getString("aspCustCode"));

        TelewebJSON getTenantParams = new TelewebJSON();
        TelewebJSON getCustcoParams = new TelewebJSON();

        String custcoId = "";

        try {
            getTenantParams = this.getSchemaId(objSetParams);
        } catch (Exception e) {
            jsonObject.put("result_code", "3");
            jsonObject.put("result_msg", erroMsg("3"));
            return jsonObject.toString();
        }

        /* tenant 정보 */
        if (getTenantParams.getSize() > 0) {

            TenantContext.setCurrentTenant(getTenantParams.getString("SCHEMA_ID"));

            try {
                getCustcoParams = this.getCustcoId(objSetParams);   //ASP_CUST_KEY 이용해서 회사명 가져옴
            } catch (Exception e) {
                jsonObject.put("result_code", "4");
                jsonObject.put("result_msg", erroMsg("4"));
                return jsonObject.toString();
            }

            if (getCustcoParams.getSize() > 0) {
                JSONObject data = (JSONObject) getCustcoParams.getDataObject("DATA").get(0);
                custcoId = data.getString("CUSTCO_ID");
            }
        }

        /* 콜백 등록 시작 */

        String DRWI_SE_NM = objClbkParams.getString("ivrName");              // 인입구분코드
        String phoneNum = objClbkParams.getString("phoneNum");
        ;              // 콜백전화번호 -
        String ALTMNT_YN = "";													  // 배정여부
        String CUST_ID = "";
        int SYSTEM_ID = 2;														  // 시스템 아이디

        objSetParams.setString("CUSTCO_ID", custcoId); // 고객사 ID
        objSetParams.setString("DRWI_SE_NM", DRWI_SE_NM);           					// 인입구분명
        objSetParams.setString("CLBK_PHN_NO", phoneNum);         				// 콜백번호
        objSetParams.setString("ALTMNT_YN", ALTMNT_YN);             				// 배정여부
        objSetParams.setString("SYSTEM_ID", Integer.toString(SYSTEM_ID));           // 시스템
        objSetParams.setInt("RGTR_ID", 2);  							// 사용자가 아닌 시스템이 등록하는 경우는 RGTR_ID는 2로 통일

        log.info("step1 ---->> 고객저장 시작");
        // 인입 전화번호가 PLT_CUST 테이블에 있는 전화번호이면 고객명 변경해서 수정
        JSONArray jsonArray = (this.custInfoProcess(objSetParams)).getDataObject("DATA");
        System.out.println("jsonArray==>" + jsonArray);

        if (jsonArray.size() == 0) {

            TelewebJSON objSetCustParam = new TelewebJSON();

            objSetCustParam.setString("CHNL", "TEL");
            objSetCustParam.setString("CUSTCO_ID", custcoId);
            objSetCustParam.setString("CUST_PHN_NO", phoneNum);
            objSetCustParam.setString("CUST_NM", "성명미상");
            objSetCustParam.setInt("USER_ID", SYSTEM_ID); // 콜백은 등록자가 SYSTEM인데 전화상담의 고객 등록 로직을 사용해 SYSTEM ID를 사용자 아이디로 넣어줌

            TelewebJSON resultParam = settingCustomerInformationListService.custProc(objSetCustParam);    // 고객ID 생성 후 PLT_CUST 테이블에 고객 저장

            resultParam.setString("CUSTCO_ID", custcoId);
            resultParam.setString("CLBK_PHN_NO", phoneNum);

            //고객 확장 정보 고객상태 정상 강제 적용
            this.clbkCustExpsnAttrReg(resultParam);

            resultParam.setString("CUST_STTS_CD", "NOML");
            //고객 기본정보 저장
            settingCustomerInformationListService.custMod(resultParam);

            JSONArray rtnArray = (this.custInfoProcess(resultParam)).getDataObject("DATA");
            log.debug("rtnArray.getJSONObject(0)@##############" + rtnArray.getJSONObject(0));
            JSONObject rtnParams = rtnArray.getJSONObject(0);
            log.debug("rtnArray.getJSONObject(0)@##############" + rtnParams.getString("CUST_ID"));
            CUST_ID = rtnParams.getString("CUST_ID");
            log.debug("rtnArray.getJSONObject(0)@##############" + CUST_ID);

        } else {

            CUST_ID = jsonArray.getJSONObject(0).getString("CUST_ID");

        }

        objSetParams.setString("CUST_ID", CUST_ID);  //고객 ID

        log.info("step1 ---->> 고객저장 완료");

        System.out.println("objSetParams===>" + objSetParams);

        // 콜백저장
        log.info("step2 ---->> 콜백저장 시작");

        int CLBK_ID = innbCreatCmmnService.createSeqNo("PLT_PHN_CLBK_ID");  // 콜백ID 생성
        objSetParams.setInt("CLBK_ID", CLBK_ID);                 					// 콜백ID

        mobjDao.insert("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "callbackRegistProcess", objSetParams);

        log.info("step2 ---->> 콜백저장 완료");

        JSONArray callbackCheck = (this.callbackYNCheck(objSetParams)).getDataObject("DATA"); //콜백여부 Y 숫자 체크I

        log.info("콜백 가능 상담사 체크 ---= " + callbackCheck.size());

        if (callbackCheck.size() == 0) {
            log.info("########### callbackCheck :null입니다. 콜백 배분할 상담사가 없습니다.");
            jsonObject.put("result_code", "10");

        } else {
            //        	objSetParams.setString("PRCS_CD", "UNTRY");
            log.info("step3 ---->> 콜백배분 시작");
            this.callbackAutoRegistProcess(objSetParams); // 콜백 자동 배분

            this.callbackAutoUpdateProcess(objSetParams); // 콜배저장

            log.info("step3 ---->> 콜백배분 완료");
            log.info("정상적으로 등록되었습니다.");

            jsonObject.put("result_code", "0");

        }
        String ResultCode = jsonObject.getString("result_code");
        jsonObject.put("result_msg", erroMsg(ResultCode));

        return jsonObject.toString();
    }

    private TelewebJSON setClbkInfo(HttpServletRequest request) throws TelewebAppException {

        TelewebJSON objRetParam = new TelewebJSON();
        /**
         * 파라미터 값 받아오기
         *
         * Asp_cust_code
         * phoneNum
         * DID
         * IVRNAME
         *
         */

        String aspCustCode = request.getParameter("Asp_cust_code");     // 해당 전화의 고객사 이름
        String phoneNum = request.getParameter("phoneNum");             // 콜백 고객 전화번호
        String DID = request.getParameter("DID");                       // 전화번호
        String ivrName = request.getParameter("IVRNAME");               // 해당 전화의 IVR 이름
        log.info("aspCustCodeaspCustCodeaspCustCode" + aspCustCode);
        if (aspCustCode.contains("&")) {

            /*
                IVR에서 넘어오는 데이터가 가장 첫번째 인덱스인 Asp_cust_code만 키값을 받고 나머지 키:밸류 들은 모두 String으로 받을 때 처리
            
                key : Asp_cust_code
                value : pallet3&phoneNum=01057575757&IVRNAME=테스트
            
            */

            String[] params = aspCustCode.split("&");
            aspCustCode = params[0];
            objRetParam.setString("aspCustCode", aspCustCode);

            for (int i = 0; i < params.length; i++) {
                if (params[i].contains("phoneNum")) {
                    String[] keyValue = params[i].split("=");
                    objRetParam.setString("phoneNum", keyValue[1]);
                } else if (params[i].contains("IVRNAME")) {
                    try {
                        String[] keyValue = params[i].split("=");
                        objRetParam.setString("ivrName", keyValue[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        objRetParam.setString("ivrName", "CALL BACK");
                        log.debug(String.valueOf(e));
                    }
                } else if (params[i].contains("DID")) {
                    String[] keyValue = params[i].split("=");
                    objRetParam.setString("DID", keyValue[1]);
                }
            }
        } else {

            log.info("aspCustCode > " + aspCustCode + ", phoneNum >" + phoneNum + ", DID >" + DID + ", ivrName >" + ivrName);

            Enumeration<String> paramNames = request.getParameterNames();

            //         파라미터 분리
            while (paramNames.hasMoreElements()) {
                log.trace("request : >>>> " + paramNames);
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                log.info("paramName : " + paramName + " paramValue : " + paramValue);

                if (paramName.equals("Asp_cust_code")) { //
                    objRetParam.setString("aspCustCode", paramValue);
                    log.info("aspCustCode : " + paramValue);
                }
                if (paramName.equals("DID")) { //
                    objRetParam.setString("DID", paramValue);
                    log.info("DID : " + paramValue);
                }
                if (paramName.equals("phoneNum")) {
                    objRetParam.setString("phoneNum", paramValue);
                    log.info("phoneNum : " + paramValue);
                }
                if (paramName.equals("IVRNAME")) {
                    objRetParam.setString("ivrName", paramValue);
                    log.info("ivrName : " + paramValue);
                }
            }
        }

        //        if (aspCustCode == null || "".equals(aspCustCode) || aspCustCode == "") { //return "false";
        if (objRetParam.getString("aspCustCode") == null || "".equals(objRetParam.getString("aspCustCode"))) { //return "false";
            // 잘못된 요청이라는 에러 응답 처리 -- 400 Error
            //             response.sendError(HttpServletResponse.SC_BAD_REQUEST);

            //접근 금지 에러 - 403 Error
            //response.sendError(HttpServletResponse.SC_FORBIDDEN);
            //response.setStatus(400);
            //            log.info("Asp_cust_code is null.");
            //            JSONObject jsonObject = new JSONObject();

            objRetParam.setString("result_code", "8");

            //        }else if (phoneNum == null || phoneNum.equals("") || phoneNum == "") {
        } else if (objRetParam.getString("phoneNum") == null || "".equals(objRetParam.getString("phoneNum"))) {

            objRetParam.setString("result_code", "9");

        }

        log.info("-------------------->>>>>>>>> " + objRetParam.toString());
        return objRetParam;
    }

    @Override
    public TelewebJSON custRegPrcs(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "custRegPrcs", mjsonParams);
    }

    @Override
    public TelewebJSON clbkCustExpsnAttrReg(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "clbkCustExpsnAttrReg", mjsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON custInfoProcess(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "custInfoProcess", mjsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON getCustcoId(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "getCustcoId", mjsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON getSchemaId(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "getSchemaId", mjsonParams);
    }

    @Override
    public TelewebJSON callInfoProgress(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "callInfoProgress", mjsonParams);
    }

    @Override
    public TelewebJSON selectSendNum(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette.infra.tplex.cti.dao.IvrCallbackDataMapper", "selectSendNum", mjsonParams);
    }

    @Override
    public TelewebJSON callbackAutoRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException {
        JSONObject messageJson = new JSONObject();
        StringBuffer sInfo = new StringBuffer();
        sInfo.append("콜백 등록 및 배정");
        messageJson.put("CALLBACK_INFO", sInfo.toString());

        String roomId = TenantContext.getCurrentTenant() + "_" + mjsonParams.getString("CUSTCO_ID");
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(roomId);
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return mobjDao.update("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "callbackAutoRegistProcess",
            mjsonParams);
    }

    @Override
    public TelewebJSON callbackAutoUpdateProcess(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "callbackAutoUpdateProcess",
            mjsonParams);
    }

    @Override
    public TelewebJSON callbackYNCheck(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloude.palette3.infra.tplex.cti.dao.IvrCallbackDataMapper", "callbackYNCheck", mjsonParams);
    }

    /**
     * 개인모니터링 조회
     */
    @Override
    public TelewebJSON getMonitor(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //TelewebJSON countBotalk = new TelewebJSON(jsonParams);
        log.debug("jsonParams" + jsonParams);

        log.debug("come here ======= objRetParams" + objRetParams);

        TelewebJSON resultParams = new TelewebJSON(jsonParams);

        try {
            String INLNE_NO = jsonParams.getString("INLNE_NO"); //내선번호
            //String INLNE_NO = "1002";
            //log.debug("INLNE_NO1111111111111 =====================" + INLNE_NO);
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://139.150.75.152/api/db_select.php?mode=monView2&ext=" + INLNE_NO;     //외부 API주소

            JSONObject jsonObject = new JSONObject();

            //외부 API로 보낼 HEADER부분
            header.add("Content-Type", "application/x-www-form-urlencoded");
            //header.add("X-Bottalks-Auth-key", certifyKey);

            log.debug("jsonObject =====================" + jsonObject);

            //외부 API로 보낼 BODY부분
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("mode", "monView2");
            parameters.add("ext", INLNE_NO);

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
            log.debug("jsonObj ====" + jsonObj);
            resultParams.setString("td_mon_3", jsonObj.getString("td_mon_3")); //IB콜수
            resultParams.setString("td_mon_4", jsonObj.getString("td_mon_4")); //OB콜수
            resultParams.setString("td_mon_5", jsonObj.getString("td_mon_5")); //응대율
            resultParams.setString("td_mon_6", jsonObj.getString("td_mon_6")); //개인IB콜수
            resultParams.setString("td_mon_7", jsonObj.getString("td_mon_7")); //개인IB시간
            resultParams.setString("td_mon_8", jsonObj.getString("td_mon_8")); //개인OB콜수
            resultParams.setString("td_mon_9", jsonObj.getString("td_mon_9")); //개인OB시간

            log.debug("resultParams ====" + resultParams);

        } catch (HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        } catch (Exception e) {

            System.out.println(e.toString());
        }

        return resultParams;
        //objRetParams = mobjDao.insert("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertBotalk", jsonParams);
        //}
        //else {

        //}
    }

}
