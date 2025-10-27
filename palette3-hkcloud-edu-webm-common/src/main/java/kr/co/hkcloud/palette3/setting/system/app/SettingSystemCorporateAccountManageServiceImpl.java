package kr.co.hkcloud.palette3.setting.system.app;


import java.net.URI;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemCorporateAccountManageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("settingSystemCorporateAccountManageService")
public class SettingSystemCorporateAccountManageServiceImpl implements SettingSystemCorporateAccountManageService
{
    private final TwbComDAO                                mobjDao;
    private final InnbCreatCmmnService                     innbCreatCmmnService;
    private final SettingSystemCorporateAccountManageUtils aes;


    /**
     * 설정시스템기업계정관리 큐리스트 삭제.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON deleteQueue(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "deleteQueue", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 큐리스트 조회.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectQueueList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectQueueList", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 큐를 등록한다.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON insertAspQueue(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertAspQueue", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 큐등록시 상세정보를 조회한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectAspQueue(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectAspQueue", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 목록을 조회한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnPageAspCustList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectRtnPageAspCustList", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 상세를 조회한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnAspCustDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectRtnAspCustDetail", jsonParams);
    }


    /**
     * ASP고객사 서비스 정보 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnAspCustServiceInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectRtnAspCustServiceInfo", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 기업계정코드를 조회한다(comboData)
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnAspCustComboData(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectRtnAspCustComboData", jsonParams);
    }


    /**
     * ASP고객 회사정보 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectAspCustInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectAspCustInfo", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 기업계정을 등록한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertRtnAspCust(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        jsonParams.setString("CUSTCO_ID", innbCreatCmmnService.getSeqNo("TWB_SEQ_CUSTCO_ID"));

        log.debug("jsonParams ==================" + jsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "insertRtnAspCust", jsonParams);

        String uuid = UUID.randomUUID().toString().substring(0, 5);

        jsonParams.setString("MANAGE_DATE_FROM", "20010101");
        jsonParams.setString("MANAGE_DATE_TO", "29991231");
        jsonParams.setString("WORK_TIME_FROM", "0000");
        jsonParams.setString("WORK_TIME_TO", "2359");
        jsonParams.setString("USE_YN", "Y");
        jsonParams.setString("ID", "20190501151722738TWB" + uuid);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertRtnHistoryOfWorkTime", jsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertInitSysMsgFin", jsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertInitSysMsgLate", jsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertInitSysMsgOut", jsonParams);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingManageMapper", "insertInitSysLnkOut", jsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "INSERT_MEW_ASP_BRD", jsonParams);

        return objRetParams;
    }


    /**
     * 설정시스템기업계정관리 기업계정을 수정한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnAspCustDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateRtnAspCustDetail", jsonParams);
    }


    /**
     * 설정시스템기업계정관리 기업계정을 삭제한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnAspCust(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "deleteRtnAspCust", jsonParams);
    }


    /**
     * 봇톡스 로그인
     */
    @Override
    public TelewebJSON botalkProcess(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //TelewebJSON countBotalk = new TelewebJSON(jsonParams);
        log.debug("jsonParams" + jsonParams);

        //countBotalk = mobjDao.select("kr.co.hkcloud.palette.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectCntBotalk", jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectRtnBotalk", jsonParams);

        log.debug("come here ======= objRetParams" + objRetParams);

        TelewebJSON resultParams = new TelewebJSON(jsonParams);

        //if(countBotalk.getInt("CNT") == 0) {//봇톡스에 등록된 사용자가 아니면
        try {
            //전송할 파라미터 설정
            //String custcoId = jsonParams.getString("ASP_NEWCUST_KEY").toString();
            String custcoId = "1041";

            String palette = jsonParams.getString("PALETTE");
            String certifyKey = jsonParams.getString("CERTIFY_KEY");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "https://www.bottalks.co.kr/api/o1/logauth/" + palette + "/" + custcoId;     //외부 API주소

            JSONObject jsonObject = new JSONObject();

            //외부 API로 보낼 HEADER부분
            header.add("Content-Type", "application/x-www-form-urlencoded");
            header.add("X-Bottalks-Auth-key", certifyKey);

            log.debug("jsonObject =====================" + jsonObject);

            //외부 API로 보낼 BODY부분
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();

            parameters.add("", "");

            log.debug("parameters ====" + parameters.toString());
            log.debug("header ====" + header.toString());
            log.debug("url=======" + url);

            //외부 API로 보내기
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(parameters, header);

            //외부 API로 보낸 결과 저장
            String result = restTemplate.postForObject(new URI(url), request, String.class);

            log.debug("result ====" + result);

            if(result.indexOf("userinfo") >= 0) {
                log.debug("in if");
                try {
                    //전송할 파라미터 설정
                    log.debug("in try");
                    String custId = "";
                    String custName = "";
                    String custEmail = "";
                    String custMobile = "";
                    String secretKey = "13c13544f5e9c9287ee1ee76179d4009";

                    if(objRetParams.getString("ID") != null || objRetParams.getString("ID") != "") {
                        custId = objRetParams.getString("ID");
                    }
                    if(objRetParams.getString("NAME") != null || objRetParams.getString("ID") != "") {
                        custName = objRetParams.getString("NAME");
                    }
                    if(objRetParams.getString("EML") != null || objRetParams.getString("ID") != "") {
                        custEmail = objRetParams.getString("EML");
                    }
                    if(objRetParams.getString("MOBILE") != null || objRetParams.getString("ID") != "") {
                        custMobile = objRetParams.getString("MOBILE");
                    }

                    HttpComponentsClientHttpRequestFactory factory2 = new HttpComponentsClientHttpRequestFactory();
                    factory2.setConnectTimeout(5000); //타임아웃 설정 5초
                    factory2.setReadTimeout(5000);//타임아웃 설정 5초

                    RestTemplate restTemplate2 = new RestTemplate(factory2);

                    String url2 = "https://www.bottalks.co.kr/api/o1/userinfo/" + palette + "/" + custId;       //외부 API주소

                    JSONObject jsonObject2 = new JSONObject();

                    jsonObject2.put("id", custId);
                    jsonObject2.put("name", custName);
                    jsonObject2.put("eml", custEmail);
                    jsonObject2.put("mobile", custMobile);

                    log.debug("jsonObject2 =====================" + jsonObject2);

                    String stringJsonObject2 = jsonObject2.toString();
                    log.debug("stringJsonObject2========" + stringJsonObject2);
                    String iv = result.substring(result.length() - 18, result.length() - 2);
                    log.debug("secretKey======" + secretKey);
                    log.debug("iv=========" + iv);
                    String aesJsonObject2 = aes.encrypt(stringJsonObject2, secretKey, iv);
                    //String aesJsonObject2 = aes.encrypt(jsonObject2, secretKey, iv);
                    log.debug("aesJsonObjcet2========" + aesJsonObject2);

                    //외부 API로 보낼 BODY부분
                    MultiValueMap<String, Object> parameters2 = new LinkedMultiValueMap<String, Object>();
                    parameters2.add("", aesJsonObject2);

                    log.debug("parameters2 ====" + parameters2.toString());
                    log.debug("header ====" + header.toString());
                    log.debug("url2=======" + url2);

                    //외부 API로 보내기
                    HttpEntity<MultiValueMap<String, Object>> request2 = new HttpEntity<MultiValueMap<String, Object>>(parameters2, header);

                    //외부 API로 보낸 결과 저장
                    String result2 = restTemplate2.postForObject(new URI(url2), request2, String.class);
                    log.debug("result2 ====" + result2);

                    //STRING데이터를 JSON데이터롤 변환
                    JSONObject jsonObj2 = JSONObject.fromObject(result2);

                    resultParams.setString("error", jsonObj2.getString("error"));
                    resultParams.setString("action", jsonObj2.getString("action"));
                    resultParams.setString("logurl", jsonObj2.getString("logurl"));
                    resultParams.setString("token", jsonObj2.getString("token"));

                }
                catch(HttpClientErrorException | HttpServerErrorException e) {

                    System.out.println(e.toString());

                }
                catch(Exception e) {

                    System.out.println(e.toString());
                }
            }
            else {
                //STRING데이터를 JSON데이터로 변환
                JSONObject jsonObj = JSONObject.fromObject(result);

                resultParams.setString("error", jsonObj.getString("error"));
                resultParams.setString("action", jsonObj.getString("action"));
                resultParams.setString("logurl", jsonObj.getString("logurl"));
                resultParams.setString("token", jsonObj.getString("token"));
            }

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
     * 설정시스템기업계정관리 신규기업계정 존재여부 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectCustExist(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "selectCustExist", jsonParams);
    }
    
    /**
     * CUSTCO_ID 변경
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateCustcoId(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateCustcoId", jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateBlbd", jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateChatLnk", jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateChatMsg", jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCorporateAccountManageMapper", "updateChatTime", jsonParams);

        return objRetParams;
    }
}
