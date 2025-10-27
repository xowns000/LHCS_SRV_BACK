package kr.co.hkcloud.palette3.chat.main.api;


import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

import kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import org.springframework.http.HttpEntity;
import org.apache.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.main.app.ChatMainService;
import kr.co.hkcloud.palette3.common.chat.domain.OrgFileVO;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.SimpleAspectAround;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.support.PaletteUserContextSupport;
import kr.co.hkcloud.palette3.core.util.PaletteDataFormatUtils;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatMainRestController",
     description = "채팅메인(vue) REST 컨트롤러")
public class ChatMainRestController
{
    private final FileRulePropertiesUtils      fileRulePropertiesUtils;
    private final InnbCreatCmmnService         innbCreatCmmnService;
    private final ChatMainService              chatMainService;
    private final PaletteCmmnService           paletteCmmnService;
    private final TalkRouteMapper talkRouteMapper;
    private final TalkRedisChatInoutRepository redisChatInoutRepository;
    private final PaletteFilterUtils           paletteFilterUtils;
    private final PaletteDataFormatUtils       paletteDataFormatUtils;

    private final String ALG = "AES/CBC/PKCS5Padding";
    private final String KEY = "54f920a2dd1b4969b920a2dd1bf9691d";
    //IV(Initial Vector)는 암복호화에서 필수값이 아니지만 보안에 취약
    private final byte[] IV = new byte[16];

    @Value("${palette.security.private.alg}")
    private String P_ALG;

    @Value("${palette.security.private.key}")
    private String P_KEY;

    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담목록",
                  notes = "채팅메인 상담목록을 조회한다")
    @RequestMapping(value = "/chat-api/main/cnslt/inqire",
                    method = RequestMethod.POST)
    public @ResponseBody Object getTalkers(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getName());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        System.out.println("bodyobj" + bodyobj);
        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        TelewebJSON jsonParams = new TelewebJSON();

        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("ST_DATE", bodyobj.getString("ST_DATE"));
        jsonParams.setString("END_DATE", bodyobj.getString("END_DATE"));
        jsonParams.setString("CHN_CLSF_CD", bodyobj.getString("CHN_CLSF_CD"));

        objRetParams = chatMainService.selectRtnTalkList(jsonParams);

        return responseEntity(objRetParams);
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅 키워드검색",
                  notes = "채팅 키워드검색")
    @RequestMapping(value = "/chat-api/main/cutt/detail",
                    method = RequestMethod.POST)
    public @ResponseBody Object getCuttDetailList(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("USER_ID", bodyobj.getString("USER_ID"));
        jsonParams.setString("CUSTCO_ID", bodyobj.getString("CUSTCO_ID"));
        jsonParams.setString("WORD", bodyobj.getString("WORD"));
        jsonParams.setString("ST_DATE", bodyobj.getString("ST_DATE"));
        jsonParams.setString("END_DATE", bodyobj.getString("END_DATE"));

        objRetParams = chatMainService.getCuttDetailList(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @ApiOperation(value = "채팅메인-고객정보조회",
                  notes = "채팅메인 고객정보를 조회한다",
                  tags = "20170804102922535KC11199")
    //@RequestMapping(value="/chat-api/main/cstmr-info-inqire/{talkUserKey}", method=RequestMethod.POST)
    @RequestMapping(value = "/chat-api/main/cstmr-info/inqire/{talkUserKey}",
                    method = RequestMethod.POST)
    public @ResponseBody Object selectRtnTalkCustInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String talkUserKey) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("CUSTCO_ID");
        String customerId = bodyobj.getString("CUSTOMER_ID");

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_USER_KEY", talkUserKey);
        jsonParams.setString("CUSTOMER_ID", customerId);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectRtnTalkCustInfo(jsonParams);

        log.debug("size >> {}", objRetParams.getDataObject("DATA").size());
        if(objRetParams.getDataObject("DATA").size() > 0) {

            String nameEnc = paletteDataFormatUtils.getFormatData("class_nameEnc", objRetParams.getString("CUSTOMER_NM"));  // 고객명 (마스킹)
            objRetParams.setString("CUSTOMER_NM_MASK", nameEnc);         // 고객명 (마스킹)

            String custPhone = paletteDataFormatUtils.getFormatData("class_tlno", objRetParams.getString("CUSTOMER_PHONE_NO"));
            objRetParams.setString("CUSTOMER_PHONE_NO_MASK", custPhone);
        }

        //최종결과값 반환
        return responseEntity(objRetParams);
    }


    /**
     * @param  headerobj
     * @param  bodyobj
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담상태별건수조회",
                  notes = "채팅메인 상담상태별 건수를 조회한다")
    @RequestMapping(value = "/chat-api/main/count-by-cnslt-sttus/inqire",
                    method = RequestMethod.POST)
    public @ResponseBody Object selectTalkStateProcessByUser(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getName());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectTalkStateProcessByUser(jsonParams);

        //최종결과값 반환
        return responseEntity(objRetParams);
    }


    /**
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-인사말정보조회",
                  notes = "채팅메인 인사말정보를 조회한다")
    @PostMapping("/chat-api/main/grt-info/inqire")
    public @ResponseBody Object getGreetingMessage(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        String userNickName = PaletteUserContextSupport.getCurrentUser().getNickname();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        String chtCuttId = bodyobj.getString("TALK_CONTACT_ID");
        String talkUserKey = bodyobj.getString("TALK_USER_KEY");
        String dsptchPrfKey = bodyobj.getString("DSPTCH_PRF_KEY");
        String sndrKey = bodyobj.getString("SNDR_KEY");

        TelewebJSON jsonParams = new TelewebJSON();

        jsonParams.setString("TALK_CONTACT_ID", chtCuttId);     // talk_contact_id
        jsonParams.setString("TALK_USER_KEY", talkUserKey);     // TALK_USER_KEY
        jsonParams.setString("CUSTCO_ID", custcoId);           // CUSTCO_ID

        // asp key
        if(sndrKey != null && !sndrKey.equals(""))
            jsonParams.setString("SNDR_KEY", sndrKey);   // SNDR_KEY
        else if(dsptchPrfKey != null && !dsptchPrfKey.equals(""))
            jsonParams.setString("DSPTCH_PRF_KEY", dsptchPrfKey);     // DSPTCH_PRF_KEY

        jsonParams.setString("USER_NICK_NAME", userNickName);       // USER_NICK_NAME

        objRetParams = chatMainService.getGreetingMessage(jsonParams);

        //최종결과값 반환
        return responseEntity(objRetParams);
    }


    /**
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-종료메시지조회",
                  notes = "채팅메인 종료메시지를 조회한다")
    @PostMapping("/chat-api/main/end-mssage/inqire")
    public @ResponseBody Object getSysMessage(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("ASP_NEWCUST_KEY");
        String systemMsgId = bodyobj.getString("system_id");

        // "20180403040958193MSG34895"
        String message = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(custcoId, systemMsgId);
        String filter2Msg = paletteFilterUtils.filter2(message);

        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");
        objRetParams.setString("data", filter2Msg);

        //최종결과값 반환
        return responseEntity(objRetParams);
    }


    /**
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @ApiOperation(value = "채팅메인-채팅메시지삭제",
                  notes = "채팅메인 채팅메시지를 삭제한다(메시지 내용 업데이트)",
                  tags = "20170804102922535KC11199")
    @PostMapping("/chat-api/main/chtt-mssage/delete")
    public @ResponseBody Object updateRemoveConent(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        TelewebJSON jsonParams = new TelewebJSON();

        bodyobj.put("CUSTCO_ID", custcoId);            // CUSTCO_ID
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);
        jsonParams.setDataObject(jsonArray);

        objRetParams = chatMainService.updateRemoveConent(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅메인-무응답메시지발송설정",
                  notes = "채팅메인-무응답메시지 발송정보를(ON/OFF) 설정한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/un-rspns-mssage-sndng-setup/process",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateSystemMsgSkip(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getName());
        String chtCuttId = bodyobj.getString("CHT_CUTT_ID");
        String sysmsgSkipYn = bodyobj.getString("SYS_MSG_SKIP_YN");
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        String msg = bodyobj.getString("RCPTN_DSPTCH_MSG");

        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("CHT_CUTT_ID", chtCuttId);             // talk_contact_id
        jsonParams.setString("SYS_MSG_SKIP_YN", sysmsgSkipYn);   // SYSMSG_SKIP_YN
        jsonParams.setString("RCPTN_DSPTCH_MSG", msg);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.updateSystemMsgSkip(jsonParams);

        if(sysmsgSkipYn != null && sysmsgSkipYn.equals("Y")) {
            jsonParams.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
            jsonParams.setString("USER_ID", userId);
            jsonParams.setString("RCPTN_DSPTCH_CD", "RCV");
            jsonParams.setString("MSG_TYPE_CD", "SKIPON");
            objRetParams = chatMainService.insertSystemMsgSkip(jsonParams);
        }
        else {
            jsonParams.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
            jsonParams.setString("RCPTN_DSPTCH_CD", "SND");
            jsonParams.setString("USER_ID", userId);
            jsonParams.setString("MSG_TYPE_CD", "SKIPOFF");
            objRetParams = chatMainService.insertSystemMsgSkip(jsonParams);
        }

        return responseEntity(objRetParams);
    }


    /**
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담정보조회",
                  notes = "채팅메인 상담정보를 조회한다")
    @RequestMapping(value = "/chat-api/main/cnslt-info/inqire",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChatInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("CUSTCO_ID");

        String chtCuttId = bodyobj.getString("TALK_CONTACT_ID");

        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("TALK_CONTACT_ID", chtCuttId);             // talk_contact_id
        jsonParams.setString("CUSTCO_ID", custcoId);                   // CUSTCO_ID
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));       // ASP_NEWCUST_KEY

        objRetParams = chatMainService.getChatInfo(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param  headerobj
     * @param  bodyobj
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅메인-상담이력임시저장",
                  notes = "채팅메인 상담이력을 임시로 저장한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/cnslt-hist-tmpr/regist",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateRtnTalkHistTemp(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = bodyobj.getString("CUSTCO_ID");
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        bodyobj.put("CUSTCO_ID", custcoId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);

        //상담이력 테이블에 저장
        objRetParams = chatMainService.updateRtnTalkHistTemp(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param  headerobj
     * @param  bodyobj
     * @return                     TelewebJSON 형식의 처리 결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅메인-상담이력저장",
                  notes = "채팅메인 상담이력을 저장한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/cnslt-hist/regist",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateRtnTalkHist(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = bodyobj.getString("CUSTCO_ID");
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        bodyobj.put("CUSTCO_ID", custcoId);
            
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);
            
        jsonParams.setDataObject(jsonArray);
        
        //CUTT_LIST 있으면 일괄저장
        if(bodyobj.has("CUTT_LIST")) {
            JSONArray cuttArray = bodyobj.getJSONArray("CUTT_LIST");
            log.info("0??" + bodyobj);
            log.info("1??" + jsonParams);
            log.info("2??" + cuttArray);
            if(cuttArray != null) {
                //채팅상담 일괄저장일 때
                int cuttListSize = cuttArray.size();
                for(int i = 0; i < cuttListSize; i++) {
                    jsonParams.setString("CHT_CUTT_ID", cuttArray.getJSONObject(i).getString("CHT_CUTT_ID"));
                    jsonParams.setString("CUST_ID", cuttArray.getJSONObject(i).getString("CUST_ID"));
                    jsonParams.setString("CUST_NM", cuttArray.getJSONObject(i).getString("CUST_NM"));
                    
                    jsonParams.setString("CUTT_STTS_CD", bodyobj.getString("CUTT_STTS_CD"));
                    jsonParams.setString("CUTT_TYPE_ID", bodyobj.getString("CUTT_TYPE_ID"));
                    jsonParams.setString("CUTT_CN", bodyobj.getString("CUTT_CN"));
                    
                    jsonParams.setString("CUSTCO_ID", bodyobj.getString("CUSTCO_ID"));
                    jsonParams.setString("USER_ID", bodyobj.getString("USER_ID"));
    
                    //상담이력 테이블에 저장
                    objRetParams = chatMainService.updateRtnTalkHist(jsonParams);
                    
                    //콜백 통합이력저장제외
                    if(!"CLBK_WAIT".equals(jsonParams.getString("CUTT_STTS_CD"))) {
                        //상담통합이력테이블 저장
                        TelewebJSON cnslUnityJsonParams = new TelewebJSON();
                        cnslUnityJsonParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                        cnslUnityJsonParams.setString("CHT_CUTT_ID", jsonParams.getString("CHT_CUTT_ID"));
                        cnslUnityJsonParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
                        
                        //이관 확장 속성이 존재하는 경우
                        if(!StringUtils.isEmpty(jsonParams.getString("TRNSF_EXPSN_ATTR"))) {
                        	cnslUnityJsonParams.setString("STAT", "REG");
                        	cnslUnityJsonParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                        	cnslUnityJsonParams.setString("TRGT_USER_ID", jsonParams.getString("TRGT_USER_ID"));
                        	cnslUnityJsonParams.setString("TRNSF_EXPSN_ATTR", jsonParams.getString("TRNSF_EXPSN_ATTR"));
                        }
                        paletteCmmnService.cuttItgrtHistReg(cnslUnityJsonParams);
                    } else {
                        //콜백저장일때
                        chatMainService.insertClbkRdy(jsonParams);      //콜백 대기에 인입
                    }
                }
            } 
        } else {
            //1. 상담이력 테이블에 저장
            objRetParams = chatMainService.updateRtnTalkHist(jsonParams);

            //2. 채팅 확장 정보 저장 (PLT_CHT_CUTT_DTL_EXPSN)
            if(jsonParams.containsKey("EXPSN_ATTR") && !jsonParams.getString("EXPSN_ATTR").isEmpty()){
                String expsnAttrStr = jsonParams.getString("EXPSN_ATTR");
                expsnAttrStr = expsnAttrStr.replace("&#91;", "[").replace("&#93;", "]");
                JSONArray arryExpsnAttr = JSONArray.fromObject(expsnAttrStr);

                if (arryExpsnAttr.size() > 0) {
                    for (Object expsnAttr : arryExpsnAttr) {
                        TelewebJSON expsnAttrParams = new TelewebJSON();

                        expsnAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                        expsnAttrParams.setString("PP_ALG_PP", P_ALG);
                        expsnAttrParams.setString("PP_KEY_PP", P_KEY);
                        expsnAttrParams.setString("CHT_CUTT_ID", jsonParams.getString("CHT_CUTT_ID"));
//                        expsnAttrParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
                        expsnAttrParams.setString("EXPSN_ATTR_COL_ID", (String) ((JSONObject) expsnAttr).get("EXPSN_ATTR_COL_ID"));
                        expsnAttrParams.setString("ATTR_ID", (String) ((JSONObject) expsnAttr).get("ATTR_ID"));
                        expsnAttrParams.setString("ATTR_VL", (String) ((JSONObject) expsnAttr).get("V_POST_PARAM"));
                        expsnAttrParams.setString("INDI_INFO_ENCPT_YN", (String) ((JSONObject) expsnAttr).get("INDI_INFO_ENCPT_YN"));
                        expsnAttrParams.setString("RSVT_PHN_CUTT_ID", jsonParams.getString("RSVT_PHN_CUTT_ID"));
                        expsnAttrParams.setString("USER_ID", jsonParams.getString("USER_ID"));
//                        expsnAttrParams.setString("CLBK_ID", jsonParams.getString("CLBK_ID"));
//                        expsnAttrParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
//                        expsnAttrParams.setString("RSVT_ID", jsonParams.getString("RSVT_ID"));


                        chatMainService.cuttExpsnAttrReg(expsnAttrParams); //채팅 상담 상세 확장 정보 저장
//                        if ("REG".equals(jsonParams.getString("STAT"))) { //등록 인 경우에만 확장 속성에 저장
//                            objRetParams = chatMainService.cuttExpsnAttrReg(expsnAttrParams); //채팅 상담 상세 확장 정보 저장
//                        }

                        //objRetParams = chatMainService.cuttChgExpsnAttrReg(expsnAttrParams); //채팅 상담 변경 상세 확장 정보 저장
                    }
                }
            }


            //콜백 통합이력저장제외
            if(!"CLBK_WAIT".equals(jsonParams.getString("CUTT_STTS_CD"))) {
                //상담통합이력테이블 저장
                TelewebJSON cnslUnityJsonParams = new TelewebJSON();
                cnslUnityJsonParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                cnslUnityJsonParams.setString("CHT_CUTT_ID", jsonParams.getString("CHT_CUTT_ID"));
                cnslUnityJsonParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
                
                //이관 확장 속성이 존재하는 경우
                if(!StringUtils.isEmpty(jsonParams.getString("TRNSF_EXPSN_ATTR"))) {
                	cnslUnityJsonParams.setString("STAT", "REG");
                	cnslUnityJsonParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                	cnslUnityJsonParams.setString("TRGT_USER_ID", jsonParams.getString("TRGT_USER_ID"));
                	cnslUnityJsonParams.setString("TRNSF_EXPSN_ATTR", jsonParams.getString("TRNSF_EXPSN_ATTR"));
                }
                paletteCmmnService.cuttItgrtHistReg(cnslUnityJsonParams);
            } else {
                //콜백저장일때
                chatMainService.insertClbkRdy(jsonParams);      //콜백 대기에 인입
            }
        }

        return responseEntity(objRetParams);

    }

        /**
         * @param  Object HashMap
         * @return        TelewebJSON 형식의 ERROR_FLAG
         *
         *                    @ApiOperation(value="채팅메인-고객정보저장", notes="채팅메인 고객정보를 저장한다", tags="20170804102922535KC11199") //@RequestMapping(value="/chat-api/main/cstmr-info-regist/{talkUserKey}" ,
         *                    method=RequestMethod.PUT) @RequestMapping(value="/chat-api/main/cstmr-info/regist/{talkUserKey}" , method=RequestMethod.PUT) public @ResponseBody Object
         *                    updateRtnMemoAndAtentCustomer(@RequestHeader LinkedHashMap<Object, Object> headerobj , @RequestBody JSONObject bodyobj, @PathVariable String talkUserKey) throws TelewebApiException { String
         *                    userId = ( UserContext.getCurrentUser() == null ? "test01" : UserContext.getCurrentUser().getUserId()); String userNm = ( UserContext.getCurrentUser() == null ? "test01" :
         *                    UserContext.getCurrentUser().getName()); String custcoId = UserContext.getCurrentUser().getCustcoId();
         *
         *                    TelewebJSON objRetParams = new TelewebJSON(); TelewebJSON jsonParams = new TelewebJSON();
         *
         *                    JSONArray jsonArray = new JSONArray(); jsonArray.add(bodyobj);
         *
         *                    jsonParams.setDataObject(jsonArray); jsonParams.setString("USER_ID", userId); jsonParams.setString("USER_NM", userNm); jsonParams.setString("TALK_USER_KEY", talkUserKey);
         *                    jsonParams.setString("CUSTCO_ID", custcoId); jsonParams.setHeader("METHOD", "MEMO_INSERT");//validation 구분 헤더 셋팅
         *
         *                    objRetParams = chatMainService.updateRtnMemoAndAtentCustomer(jsonParams);
         *
         *                    return responseEntity(objRetParams); }
         */

    /**
     * 채팅메인-고객정보저장
     *
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @ApiOperation(value = "채팅메인-고객정보저장",
                  notes = "채팅메인 고객정보를 저장한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/cstmr-info/regist/{talkUserKey}",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateRtnChatCustomerInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String talkUserKey) throws TelewebApiException
    {
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String userNm = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getName());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        String userId = bodyobj.getString("ASP_USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");
        //String userNm = bodyobj.getString("ASP_USER_NM");

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);
        jsonParams.setString("USER_ID", userId);
        //jsonParams.setString("USER_NM", userNm);
        jsonParams.setString("TALK_USER_KEY", talkUserKey);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setHeader("METHOD", "MEMO_INSERT");//validation 구분 헤더 셋팅

        objRetParams = chatMainService.updateRtnChatCustomerInfo(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력목록",
                  notes = "채팅메인 상담통합이력 목록을 조회한다")
    //@PostMapping("/chat-api/main/cnslt-hist/inqire/{userTelno}")
    //public @ResponseBody Object selectRtnCnslUnityHst(@RequestHeader LinkedHashMap<Object, Object> headerobj , @RequestBody JSONObject bodyobj, @PathVariable String userTelno) throws TelewebApiException
    @PostMapping("/chat-api/main/cnslt-hist/inqire")
    public @ResponseBody Object selectRtnCnslUnityHst(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        log.debug("log확인 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + bodyobj);
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("CUSTCO_ID");

        String detailSearch = bodyobj.getString("DET_SRC");

        jsonParams.setString("CSTMR_TELNO", bodyobj.getString("CSTMR_TELNO"));
        jsonParams.setString("CNSL_DIV", bodyobj.getString("CNSL_DIV"));
        jsonParams.setString("TALK_USER_KEY", bodyobj.getString("TALK_USER_KEY"));
        jsonParams.setString("CSTMR_ID", bodyobj.getString("CSTMR_ID"));
        jsonParams.setString("CSTMR_TELNO", bodyobj.getString("CSTMR_TELNO"));
        jsonParams.setString("CALL_TY", bodyobj.getString("CALL_TY"));
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));

        if(detailSearch != "") {

            jsonParams.setString("ST_DATE", bodyobj.getString("ST_DATE"));
            jsonParams.setString("END_DATE", bodyobj.getString("END_DATE"));
            jsonParams.setString("RESULT", bodyobj.getString("RESULT"));
            jsonParams.setString("AGENT_ID", bodyobj.getString("AGENT_ID"));
            //jsonParams.setString("CHENNEL", bodyobj.getString("CHENNEL"));
            jsonParams.setString("CHAT_RESULT", bodyobj.getString("CHAT_RESULT"));
        }

        objRetParams = paletteCmmnService.selectRtnCnslUnityHst(jsonParams);

        return responseEntity(objRetParams);
    }

    /*
     * @PostMapping("/chat-api/main/cnslt-hist/inqire/{talkUserKey}") public @ResponseBody Object selectRtnTalkSearch5(@RequestHeader LinkedHashMap<Object, Object> headerobj , @RequestBody JSONObject
     * bodyobj, @PathVariable String talkUserKey) throws TelewebApiException { TelewebJSON objRetParams = new TelewebJSON(); TelewebJSON jsonParams = new TelewebJSON();
     *
     * String custcoId = UserContext.getCurrentUser().getCustcoId();
     *
     * jsonParams.setHeader("PAGES_CNT", bodyobj.getString("PAGES_CNT")); //현재페이지 jsonParams.setHeader("ROW_CNT" , bodyobj.getString("ROW_CNT")); //이미지개수 jsonParams.setString("TALK_USER_KEY" , talkUserKey); //
     * TALK_USER_KEY jsonParams.setString("TALK_STAT_CD" , bodyobj.getString("TALK_STAT_CD")); // TALK_STAT_CD ( NOT IN ) jsonParams.setString("SORT_BY" , bodyobj.getString("SORT_BY")); // SORT_BY
     * jsonParams.setString("DECENDING" , bodyobj.getString("DECENDING")); // DECENDING jsonParams.setString("CUSTCO_ID" , custcoId);
     *
     * objRetParams = chatMainService.selectRtnTalkSearch5(jsonParams);
     *
     * return responseEntity(objRetParams); }
     */


    /**
     * @param Object HashMap.
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-스크립트목록",
                  notes = "채팅메인 스크립트 목록을 조회한다")
    @PostMapping("/chat-api/main/script/inqire")
    public @ResponseBody Object searchScripts(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        bodyobj.put("CUSTCO_ID", custcoId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);

        jsonParams.setHeader("PAGES_CNT", bodyobj.getString("PAGES_CNT"));  //현재페이지
        jsonParams.setHeader("ROW_CNT", bodyobj.getString("ROW_CNT"));      //이미지개수
        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));

        jsonParams.setHeader("METHOD", "SCRIPTS");//validation 구분 헤더 셋팅

        objRetParams = chatMainService.searchScripts(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param Object HashMap.
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-스크립트상세",
                  notes = "채팅메인 스크립트상세를 조회한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/script/detail/{scriptId}",
                    method = RequestMethod.POST)
    public @ResponseBody Object getScript(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String scriptId) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("ASP_NEWCUST_KEY");
        String userId = bodyobj.getString("USER_ID");

        jsonParams.setString("SCRIPT_ID", scriptId);    // SCRIPT_ID
        jsonParams.setString("USER_ID", userId);    // userId
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.getScript(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅메인-스크립트저장",
                  notes = "채팅메인 스크립트를 저장한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/script/regist",
                    method = RequestMethod.PUT)
    public @ResponseBody Object insertRtnScriptMng(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        bodyobj.put("CUSTCO_ID", custcoId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);
        jsonParams.setHeader("METHOD", "SCRIPTS_INSERT");//validation 구분 헤더 셋팅

        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setInt("LAST_USER_ID", userId);
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));  // 회사구분 셋팅

        objRetParams = chatMainService.insertRtnScriptMng(jsonParams);

        // 단축키 정보 저장
        JSONArray commands = bodyobj.getJSONArray("commands");
        chatMainService.deleteUserScriptShortKey(jsonParams, commands); // 기단축키 삭제
        chatMainService.insertUserScriptShortKey(jsonParams, commands); // 등록

        return responseEntity(objRetParams);

    }


    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅메인-스크립트수정",
                  notes = "채팅메인 스크립트를 수정한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/script/modify",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateRtnScriptMng(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        bodyobj.put("CUSTCO_ID", custcoId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);

        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setHeader("METHOD", "SCRIPTS_UPDATE");//validation 구분 헤더 셋팅
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));  // 회사구분 셋팅

        objRetParams = chatMainService.updateRtnScriptMng(jsonParams);

        // 단축키 정보 저장
        JSONArray commands = bodyobj.getJSONArray("commands");
        chatMainService.deleteUserScriptShortKey(jsonParams, commands); // 기단축키 삭제
        chatMainService.insertUserScriptShortKey(jsonParams, commands); // 등록

        return responseEntity(objRetParams);

    }


    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅메인-단축키 수정",
                  notes = "채팅메인 단축키를 수정한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/script-shortKey/modify",
                    method = RequestMethod.POST)
    public @ResponseBody Object updateRtnShortketMng(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("ASP_NEWCUST_KEY");
        int userId = bodyobj.getInt("USER_ID");

        bodyobj.put("CUSTCO_ID", custcoId);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bodyobj);

        jsonParams.setDataObject(jsonArray);

        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setHeader("METHOD", "SCRIPTS_UPDATE");//validation 구분 헤더 셋팅
        jsonParams.setString("ASP_NEWCUST_KEY", custcoId);  // 회사구분 셋팅
        jsonParams.setString("SCRIPT_ID", bodyobj.getString("SCRIPT_ID"));  // 회사구분 셋팅

        // 단축키 정보 저장
        JSONArray commands = bodyobj.getJSONArray("commands");

        if(bodyobj.getString("SHORT_KEY") == "") {
            chatMainService.deleteUserScriptShortKey(jsonParams, commands); // 기단축키 삭제
        }
        else {
            chatMainService.insertUserScriptShortKey(jsonParams, commands); // 등록
        }

        return responseEntity(objRetParams);

    }


    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅메인 스크립트삭제",
                  notes = "채팅메인 스크립트를 삭제한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/script/delete",
                    method = RequestMethod.POST)
    public @ResponseBody Object deleteRtnScriptMng(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setString("SCRIPT_ID", bodyobj.getString("SCRIPT_ID"));  // scriptId
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));  // scriptId
        jsonParams.setHeader("METHOD", "SCRIPTS_DELETE"); //validation 구분 헤더 셋팅

        objRetParams = chatMainService.deleteRtnScriptMng(jsonParams);

        // 단축키 삭제
        JSONArray commands = bodyobj.getJSONArray("commands");
        chatMainService.deleteUserScriptShortKey(jsonParams, commands); // 기단축키 삭제

        return responseEntity(objRetParams);
    }


    /**
     * @param jsonParams   전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-스크립트단축키중복체크 ",
                  notes = "채팅메인 스크립트 단축키가 중복되었는지 체크한다")
    @PostMapping("/chat-api/main/script-shrten-key-dplct-ceck/inqire")
    public @ResponseBody Object existScriptCommand(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String custcoId = bodyobj.getString("ASP_NEWCUST_KEY");
        int userId = bodyobj.getInt("USER_ID");

        jsonParams.setString("SHORT_KEY", bodyobj.getString("SHORT_KEY"));      // SHORT_KEY
        jsonParams.setInt("USER_ID", userId);                                // USER_ID
        jsonParams.setString("CUSTCO_ID", custcoId);                           // CUSTCO_ID
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));  // ASP_NEWCUST_KEY

        objRetParams = chatMainService.existScriptCommand(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param  Object HashMap.
     * @return        TelewebJSON 형식의 처리 결과 데이터
     */
    /*
     * @NoBizLog
     *
     * @ApiOperation(value = "채팅메인-링크목록", notes = "채팅메인 링크목록을 조회한다")
     *
     * //@PostMapping("/chat-api/main/link-list")
     *
     * @PostMapping("/chat-api/main/link/inqire") public @ResponseBody Object searchUntacts(@RequestHeader LinkedHashMap<Object, Object> headerobj , @RequestBody JSONObject bodyobj) throws TelewebApiException {
     * TelewebJSON objRetParams = new TelewebJSON(); TelewebJSON jsonParams = new TelewebJSON();
     *
     * String custcoId = UserContext.getCurrentUser().getCustcoId(); bodyobj.put("CUSTCO_ID", custcoId);
     *
     * JSONArray jsonArray = new JSONArray(); jsonArray.add(bodyobj);
     *
     * jsonParams.setDataObject(jsonArray);
     *
     * jsonParams.setHeader("PAGES_CNT", bodyobj.getString("PAGES_CNT")); //현재페이지 jsonParams.setHeader("ROW_CNT", bodyobj.getString("ROW_CNT")); //이미지개수
     *
     * jsonParams.setHeader("METHOD", "UNTACTS");//validation 구분 헤더 셋팅
     *
     * objRetParams = chatMainService.searchUntacts(jsonParams);
     *
     * return responseEntity(objRetParams); }
     */
    /**
     * @param mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-문의유형상세",
                  notes = "채팅메인 문의유형상세를 조회한다")
    @PostMapping("/chat-api/main/inqry-ty/detail/{chtCuttId}")
    public @ResponseBody Object selectRtnTalkInqryInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String chtCuttId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("TALK_CONTACT_ID", chtCuttId);                     // TALK_CONTACT_ID
        jsonParams.setString("CUSTCO_ID", custcoId);                       // custcoId

        objRetParams = chatMainService.selectRtnTalkInqryInfo(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param Object HashMap
     * @return TelewebJSON 형식의 처리결과 데이터
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담원상태조회",
                  notes = "채팅메인 상담원상태를 조회한다",
                  tags = "20170804102922535KC11199")
    @PostMapping("/chat-api/main/cnslr-sttus/inqire")
    public @ResponseBody Object getStatus(@PathVariable String scriptId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setInt("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectUserSta(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param jsonParams   전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     */
    @ApiOperation(value = "채팅메인-상담원상태저장",
                  notes = "채팅메인 상담원상태를 저장한다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/cnslr-sttus/regist",
                    method = RequestMethod.POST)
    public @ResponseBody Object insertTalkReadyOff(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getUserId());
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("USER_CHT_STAT", bodyobj.getString("USER_CHT_STAT"));               //USER_STATUS_CD
        jsonParams.setInt("USER_ID", bodyobj.getInt("USER_ID"));                             // USER_ID
        jsonParams.setString("CUSTCO_ID", bodyobj.getString("CUSTCO_ID"));                // CUSTCO_ID

        objRetParams = chatMainService.insertTalkReadyOff(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @SimpleAspectAround
    @ApiOperation(value = "채팅메인-대기시간초과상담조회",
                  notes = "채팅메인-대기시간초과상담을 조회한다")
    @PostMapping("/chat-api/main/wait-time-excess-cnslt/inqire")
    public @ResponseBody Object checkReadyTimeout(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setString("CUSTCO_ID", custcoId);   // custcoId
        jsonParams.setString("TALK_USER_KEY", bodyobj.getString("TALK_USER_KEY"));  // scriptId

        objRetParams = chatMainService.checkReadyTimeout(jsonParams);

        if(objRetParams == null || objRetParams.getSize() == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", "");
            objRetParams.setString("data", "");
        }

        return responseEntity(objRetParams);
    }


    /**
     * @param ORG_CONT_ID
     * @return Object
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-장문텍스트조회",
                  notes = "채팅메인 장문텍스트를 조회한다(DB)")
    @RequestMapping(value = "/chat-api/main/lngt-text-db/inqire/{ORG_CONT_ID}",
                    method = RequestMethod.POST)
    public @ResponseBody Object getAllTextByDB(@PathVariable String ORG_CONT_ID) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON prohibiteJsonParams = new TelewebJSON();

        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setInt("USER_ID", userId);        // USER_ID
        jsonParams.setString("CUSTCO_ID", custcoId);   // custcoId
        jsonParams.setString("ORG_CONT_ID", ORG_CONT_ID);   // ORG_CONT_ID

        objRetParams = chatMainService.getAllTextByDB(jsonParams);

        if(objRetParams.getHeaderBoolean("ERROR_FLAG") != true) {

            prohibiteJsonParams.setString("MESSAGE", objRetParams.getString("TEXT"));   // TEXT
            prohibiteJsonParams.setString("CUSTCO_ID", custcoId);                  // CUSTCO_ID
            TelewebJSON objRetParams2 = chatMainService.parseProhibiteByMessage(prohibiteJsonParams);

            if(objRetParams.getHeaderBoolean("ERROR_FLAG") != true)
                objRetParams.setString("TEXT", objRetParams2.getString("MESSAGE"));
        }

        return responseEntity(objRetParams);
    }


    /**
     * @param ORG_CONT_ID
     * @return Object
     * @throws TelewebApiException
     * @throws IOException
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-장문텍스트조회",
                  notes = "채팅메인 장문텍스트를 조회한다(URL)")
    @RequestMapping(value = "/chat-api/main/lngt-text-url/inqire/{URL}",
                    method = RequestMethod.POST)
    public @ResponseBody Object getAllText(@PathVariable String URL) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON prohibiteJsonParams = new TelewebJSON();

        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setInt("USER_ID", userId);        // USER_ID
        jsonParams.setString("CUSTCO_ID", custcoId);   // custcoId
        jsonParams.setString("URL", URL);   // URL

        objRetParams = chatMainService.getAllText(jsonParams);

        if(objRetParams.getHeaderBoolean("ERROR_FLAG") != true) {

            prohibiteJsonParams.setString("MESSAGE", objRetParams.getString("TEXT"));   // TEXT
            prohibiteJsonParams.setString("CUSTCO_ID", custcoId);                  // CUSTCO_ID
            TelewebJSON objRetParams2 = chatMainService.parseProhibiteByMessage(prohibiteJsonParams);

            if(objRetParams.getHeaderBoolean("ERROR_FLAG") != true)
                objRetParams.setString("TEXT", objRetParams2.getString("MESSAGE"));
        }

        return responseEntity(objRetParams);
    }


    /**
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담원금칙어필터링",
                  notes = "채팅메인 상담원이 입력한 금칙어를 필터링한다")
    @PostMapping("/chat-api/main/cnslr-prhibt-text-flter/process")
    public @ResponseBody Object parseProhibiteByMessage(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = bodyobj.getString("CUSTCO_ID");

        jsonParams.setString("MESSAGE", bodyobj.getString("MESSAGE"));  // scriptId
        jsonParams.setString("CUSTCO_ID", custcoId);               // CUSTCO_ID

        objRetParams = chatMainService.parseProhibiteByMessage(jsonParams);

        // 배열형도 스트링형으로 변환함.
//      if(objRetParams != null && !"".equals(objRetParams.getString("MESSAGE"))) {
//          objRetParams.setString("MESSAGE", jsonUtils.valueToStringWithoutQutoes(objRetParams.getString("MESSAGE")));
//      }

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        return responseEntity(objRetParams);
    }


    /**
     * response entity
     *
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    private Object responseEntity(TelewebJSON objRetParams)
    {

        JSONObject result = new JSONObject();
        //반환정보 세팅

        result.put("code", objRetParams.getHeaderBoolean("ERROR_FLAG") == true ? "-1" : "0");           // 정상
        result.put("error", objRetParams.getHeaderString("ERROR_MSG"));         // error

        if(objRetParams != null && objRetParams.getSize() > 0 && objRetParams.containsKey("data"))
            result.put("data", objRetParams.getString("data"));
        else
            result.put("data", objRetParams.getDataObject());

        if(objRetParams.getHeaderJSON() != null)
            result.put("header", objRetParams.getHeaderJSON());

        return (Object) new ResponseEntity<String>(result.toString(), HttpStatus.OK);
    }


    /**
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @ApiOperation(value = "채팅메인-상담내용엑셀다운로드",
                  notes = "채팅메인 상담내용을 엑셀로 다운로드받는다",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/cnslt-cn-excel-dwld/process",
                    method = RequestMethod.POST)
    public @ResponseBody Object excelExportChatContents(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        String chtCuttId = bodyobj.getString("TALK_CONTACT_ID");
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String userId = bodyobj.getString("ASP_USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        jsonParams.setString("TALK_CONTACT_ID", chtCuttId);
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.selectRtnChatContent(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param jsonParams   전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     * @return
     */
    @ApiOperation(value = "채팅메인-메시지읽음처리",
                  notes = "채팅메인-메시지읽음처리(메시지 전체 READ 여부 값 업데이트)",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/all-mssage-redng/process/{chtCuttId}",
                    method = RequestMethod.PUT)
    public @ResponseBody Object updateIsReadTalkAll(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("TALK_CONTACT_ID", bodyobj.getString("TALK_CONTACT_ID"));          // talk_contact_id
        jsonParams.setString("READ_YN", bodyobj.getString("READ_YN"));          //
        jsonParams.setString("CUSTCO_ID", custcoId);                           // CUSTCO_ID

        objRetParams = chatMainService.updateIsReadTalkAll(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param jsonParams   전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     * @return
     */
    @ApiOperation(value = "채팅메인-메시지읽음처리",
                  notes = "채팅메인-메시지읽음처리(단건)",
                  tags = "20170804102922535KC11199")
    @RequestMapping(value = "/chat-api/main/mssage-redng/process/{chtCuttId}",
                    method = RequestMethod.PUT)
    public @ResponseBody Object updateIsReadTalk(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("TALK_CONTACT_ID", bodyobj.getString("TALK_CONTACT_ID"));          // talk_contact_id
        jsonParams.setString("TALK_SERIAL_NUMBER", bodyobj.getString("TALK_SERIAL_NUMBER"));
        jsonParams.setString("READ_YN", bodyobj.getString("READ_YN"));          //
        jsonParams.setString("CUSTCO_ID", custcoId);                           // CUSTCO_ID

        objRetParams = chatMainService.updateIsReadTalk(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * @param headerobj
     * @param bodyobj
     * @return Object
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-대시보드조회",
                  notes = "채팅메인 대시보드데이터를 조회한다")
    @RequestMapping(value = "/chat-api/main/dash-board/inqire",
                    method = RequestMethod.POST)
    public @ResponseBody Object selectTalkStateDashboard(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser().getName());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
//        jsonParams.setString("USER_ATTR_B" , "B01");

        objRetParams = chatMainService.selectTalkStateDashboard(jsonParams);

        //최종결과값 반환
        return responseEntity(objRetParams);
    }


    /**
     * 채팅메인 상담통합이력 전화상담이력조회
     *
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-전화상담이력조회",
                  notes = "채팅메인 상담통합이력 전화상담정보를 조회한다")
    @PostMapping("/chat-api/main/cnslt-hist/phone-inqire/{cnslHstId}")
    public @ResponseBody Object selectRtnCnslUnityHstPhone(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String cnslHstId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        int userId = PaletteUserContextSupport.getCurrentUser().getUserId();
        String rdwtFile = "";

        jsonParams.setString("CNSL_HIST_NO", cnslHstId);
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));
        //jsonParams.setString("USER_ID"    , userId);
        //jsonParams.setString("CUSTCO_ID"   , custcoId);

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstPhone(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * 채팅메인 상담통합이력 메일이력조회
     *
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-메일이력조회",
                  notes = "채팅메인 상담통합이력 메일이력조회를 조회한다")
    @PostMapping("/chat-api/main/cnslt-hist/mail-inqire/{cnslHstId}")
    public @ResponseBody Object selectRtnCnslUnityHstMail(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String cnslHstId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        int userId = PaletteUserContextSupport.getCurrentUser().getUserId();

        jsonParams.setString("CNSL_HIST_NO", cnslHstId);
        //jsonParams.setString("USER_ID"    , userId);
        //jsonParams.setString("CUSTCO_ID"   , custcoId);

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstMail(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * 채팅메인 상담통합이력 문자이력조회
     *
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-문자이력조회",
                  notes = "채팅메인 상담통합이력 문자이력정보를 조회한다")
    @PostMapping("/chat-api/main/cnslt-hist/sms-inqire/{cnslHstId}")
    public @ResponseBody Object selectRtnCnslUnityHstSms(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String cnslHstId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        int userId = PaletteUserContextSupport.getCurrentUser().getUserId();

        jsonParams.setString("CNSL_HIST_NO", cnslHstId);
        //jsonParams.setString("USER_ID"    , userId);
        //jsonParams.setString("CUSTCO_ID"   , custcoId);

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstSms(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * 채팅메인 상담통합이력 알림톡이력조회
     *
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-알림톡이력조회",
                  notes = "채팅메인 상담통합이력 알림톡이력정보를 조회한다")
    @PostMapping("/chat-api/main/cnslt-hist/ntcn-talk-inqire/{cnslHstId}")
    public @ResponseBody Object selectRtnCnslUnityHstNtcnTalk(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String cnslHstId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        int userId = PaletteUserContextSupport.getCurrentUser().getUserId();

        jsonParams.setString("CNSL_HIST_NO", cnslHstId);
        //jsonParams.setString("USER_ID"    , userId);
        //jsonParams.setString("CUSTCO_ID"   , custcoId);

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstNtcnTalk(jsonParams);

        return responseEntity(objRetParams);
    }


    /**
     * 채팅메인-상담통합이력-채팅상세조회
     *
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-채팅상세조회",
                  notes = "채팅메인-상담통합이력-채팅상세조회")
    @RequestMapping(value = "/chat-api/main/cnslt-cn/inqire/{cnslHstId}",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChatCnslInfo(@RequestHeader LinkedHashMap<Object, Object> headerobj, @PathVariable String cnslHstId) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        TelewebJSON objRetInfoParams = new TelewebJSON();

        String chtCuttId = cnslHstId;
        int userId = PaletteUserContextSupport.getCurrentUser() != null ? PaletteUserContextSupport.getCurrentUser().getUserId() : 1;
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        log.debug("chtCuttId >> {}", chtCuttId);
        log.debug("userId >> {}", userId);
        log.debug("custcoId >> {}", custcoId);

        jsonParams.setString("TALK_CONTACT_ID", chtCuttId);
        jsonParams.setInt("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstChat(jsonParams);

        // [파일o] 채팅메인-통합이력-채팅상세조회: 채팅-이미지(고객)
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        RepositoryTrgtTypeCd strFileUploadPath = fileProperties.getTrgtTypeCd();
        switch(strFileUploadPath)
        {
            case DB:
            {
                JSONArray jsonArray = new JSONArray();

                JSONArray orgFileJsonArray = new JSONArray();
                orgFileJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
                for(int i = 0; i < objRetParams.getHeaderInt("COUNT"); i++) {
                    JSONObject orgFileJsonObject = orgFileJsonArray.getJSONObject(i);
                    if(!"".equals(orgFileJsonObject.getString("ORG_FILE_ID"))) {
                        OrgFileVO orgFileVO = new OrgFileVO();
                        orgFileVO.setCustcoId(custcoId);
                        orgFileVO.setOrgFileId(orgFileJsonObject.getString("ORG_FILE_ID"));
                        OrgFileVO orgFileVOtmp = talkRouteMapper.selectOrgFileVo(orgFileVO);
                        if(orgFileVOtmp != null && orgFileVOtmp.getOrgFile() != null) {
                            // byte[] --> Base64 변환하여 String으로 넘긴다.
                            String encFileString = Base64.getEncoder().encodeToString(orgFileVOtmp.getOrgFile());
                            orgFileJsonObject.put("FILE_BLOB", encFileString);
                            orgFileJsonObject.put("FILE_EXTN", orgFileVOtmp.getFileExtn());
                            jsonArray.add(orgFileJsonObject);
                        }
                        else {
                            orgFileJsonObject.put("FILE_BLOB", "");
                            orgFileJsonObject.put("FILE_EXTN", "");
                            jsonArray.add(orgFileJsonObject);
                        }
                    }
                    else {
                        orgFileJsonObject.put("FILE_BLOB", "");
                        orgFileJsonObject.put("FILE_EXTN", "");
                        jsonArray.add(orgFileJsonObject);
                    }
                }

                objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray);
                break;
            }
            default:
            {
                break;
            }
        }

        return responseEntity(objRetParams);

    }


    /**
     * 채팅메인 상담통합이력 채팅조회
     *
     * @param inHashMap
     * @return objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담통합이력-채팅조회",
                  notes = "채팅메인 상담통합이력 채팅 목록을 조회한다")
    @PostMapping("/chat-api/main/cnslt-hist/chat-inqire/{cnslHstId}")
    public @ResponseBody Object selectRtnCnslUnityHstChat(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj, @PathVariable String cnslHstId) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();

        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        //String userId = PaletteUserContextSupport.getCurrentUser().getUserId();
        String userId = bodyobj.getString("ASP_USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        jsonParams.setString("TALK_CONTACT_ID", cnslHstId);
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));

        objRetParams = paletteCmmnService.selectRtnCnslUnityHstChat(jsonParams);

        // [파일o] 채팅메인-통합이력-채팅조회: 채팅-이미지(고객)
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        RepositoryTrgtTypeCd strFileUploadPath = fileProperties.getTrgtTypeCd();
        switch(strFileUploadPath)
        {
            case DB:
            {
                JSONArray jsonArray = new JSONArray();

                JSONArray orgFileJsonArray = new JSONArray();
                orgFileJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
                for(int i = 0; i < objRetParams.getHeaderInt("COUNT"); i++) {
                    JSONObject orgFileJsonObject = orgFileJsonArray.getJSONObject(i);
                    if(!"".equals(orgFileJsonObject.getString("ORG_FILE_ID"))) {
                        OrgFileVO orgFileVO = new OrgFileVO();
                        orgFileVO.setCustcoId(custcoId);
                        orgFileVO.setOrgFileId(orgFileJsonObject.getString("ORG_FILE_ID"));
                        OrgFileVO orgFileVOtmp = talkRouteMapper.selectOrgFileVo(orgFileVO);
                        if(orgFileVOtmp != null && orgFileVOtmp.getOrgFile() != null) {
                            // byte[] --> Base64 변환하여 String으로 넘긴다.
                            String encFileString = Base64.getEncoder().encodeToString(orgFileVOtmp.getOrgFile());
                            orgFileJsonObject.put("FILE_BLOB", encFileString);
                            orgFileJsonObject.put("FILE_EXTN", orgFileVOtmp.getFileExtn());
                            jsonArray.add(orgFileJsonObject);
                        }
                        else {
                            orgFileJsonObject.put("FILE_BLOB", "");
                            orgFileJsonObject.put("FILE_EXTN", "");
                            jsonArray.add(orgFileJsonObject);
                        }
                    }
                    else {
                        orgFileJsonObject.put("FILE_BLOB", "");
                        orgFileJsonObject.put("FILE_EXTN", "");
                        jsonArray.add(orgFileJsonObject);
                    }
                }

                objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray);
                break;
            }
            default:
            {
                break;
            }
        }

        return responseEntity(objRetParams);
    }


    /**
     * @param Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-BLOB이미지조회(ORG_FILE_ID)",
                  notes = "채팅메인 ORG_FILE_ID로 BLOB이미지를 조회한다")
    @RequestMapping(value = "/chat-api/main/blob-image/inqire/{OrgFileId}",
                    method = RequestMethod.GET)

    public ResponseEntity<byte[]> selectBlobImageByOrgFileId(@PathVariable(required = true) String OrgFileId) throws TelewebApiException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setString("CUSTCO_ID", custcoId);

        OrgFileVO orgFileVO = new OrgFileVO();
        orgFileVO.setCustcoId(custcoId);
        orgFileVO.setOrgFileId(OrgFileId);
        OrgFileVO orgFileVOtmp = talkRouteMapper.selectOrgFileVo(orgFileVO);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        if(orgFileVOtmp != null && orgFileVOtmp.getFileExtn() != null) {
            switch(orgFileVOtmp.getFileExtn().toLowerCase())
            {
                case "jpeg":
                case "jpg":
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    break;
                case "png":
                    headers.setContentType(MediaType.IMAGE_PNG);
                    break;
                case "gif":
                    headers.setContentType(MediaType.IMAGE_GIF);
                    break;
                default:
                    break;
            }
        }

        if(orgFileVOtmp != null && orgFileVOtmp.getOrgFile() != null) {
            return new ResponseEntity<byte[]>(orgFileVOtmp.getOrgFile(), headers, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<byte[]>(orgFileVOtmp.getOrgFile(), headers, HttpStatus.NO_CONTENT);
        }
    }

 // @formatter:off
//    /**
//     *
//     * @param  Object HashMap
//     * @return        TelewebJSON 형식의 ERROR_FLAG
//     */
//    @NoBizLog
//    @ApiOperation(value = "채팅메인-BLOB이미지조회(FILE_KEY)",
//                  notes = "채팅메인 FILE_KEY로 BLOB이미지를 조회한다")
//    @RequestMapping(value = "/chat-api/main/blob-image/inqire/{fileGroupKey}/{fileKey}",
//                    method = RequestMethod.GET)
//    public ResponseEntity<Resource> selectBlobImageByFileKey(@PathVariable(required = true) String fileGroupKey, @PathVariable(required = true) String fileKey) throws TelewebApiException
//    {
////        TelewebJSON jsonParams = new TelewebJSON();
////        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
//
//        // [파일o] 채팅메인-BLOB이미지조회(FILE_KEY): 채팅-이미지(고객)
//        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
//        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
//        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
//        log.debug("fileProperties>>>{}", fileProperties);
//
////        jsonParams.setString("CUSTCO_ID", custcoId);
//
////        FileCmmnVO fileCmmnVO = new FileCmmnVO();
////        fileCmmnVO.setCustcoId(custcoId);
////        fileCmmnVO.setFileGroupKey(fileGroupKey);
////        fileCmmnVO.setFileKey(fileKey);
////        FileCmmnVO fileCmmnVOtmp = fileCmmnMapper.selectFileCmmnVO(fileCmmnVO);
//
//
//        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
//                                                                              .fileGroupKey(fileGroupKey)
//                                                                              .fileKey(fileKey).build();
//
//        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(fileProperties, fileDbMngSelectRequest);
//
//
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//
//        switch(fileDownloadResponse.getFileExtn().toLowerCase())
//        {
//            case "jpeg":
//            case "jpg":
//                headers.setContentType(MediaType.IMAGE_JPEG);
//                break;
//            case "png":
//                headers.setContentType(MediaType.IMAGE_PNG);
//                break;
//            case "gif":
//                headers.setContentType(MediaType.IMAGE_GIF);
//                break;
//            default:
//                break;
//        }
////        if(fileDownloadResponse != null && fileDownloadResponse.getResource() != null) {
////            return new ResponseEntity<byte[]>(fileDownloadResponse.getResource(), headers, HttpStatus.OK);
////        }
////        return new ResponseEntity<byte[]>(null, headers, HttpStatus.NO_CONTENT);
//
//        if(fileDownloadResponse != null && fileDownloadResponse.getResource() != null) {
//            return ResponseEntity.ok()
//                                 .headers(headers)
//                                 .body(fileDownloadResponse.getResource());
//        }
//        return ResponseEntity.status(HttpStatus.NO_CONTENT)
//                             .headers(headers)
//                             .body(fileDownloadResponse.getResource());
//    }
 // @formatter:on


    /**
     * @param  Object HashMap
     * @return        TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "채팅메인-상담내용조회",
                  notes = "채팅메인-상담내용조회")
    @RequestMapping(value = "/chat-api/main/cnslt-cn/inqire",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChatContent(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        log.info("신규건인지 확인하는거다" + bodyobj);
        String chtCuttId = bodyobj.getString("CHT_CUTT_ID");
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        //String userId = PaletteUserContextSupport.getCurrentUser() != null ? PaletteUserContextSupport.getCurrentUser().getName() : "test01";
        //String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");
        String cuttSttsCd = bodyobj.has("CUTT_STTS_CD") ? bodyobj.getString("CUTT_STTS_CD") : "";
        String chtUserKey = bodyobj.has("CHT_USER_KEY") ? bodyobj.getString("CHT_USER_KEY") : "";
        String sndrKey = bodyobj.has("SNDR_KEY") ? bodyobj.getString("SNDR_KEY") : "";
        String chnClsfCd = bodyobj.has("CHN_CLSF_CD") ? bodyobj.getString("CHN_CLSF_CD") : "";

        // move
        if(cuttSttsCd.contains("WAIT")) {
            //전송된 파라메터 반환
            jsonParams.setString("CUST_ID", bodyobj.getString("CUST_ID"));
            jsonParams.setString("CHT_CUTT_ID", chtCuttId);
            jsonParams.setString("CHT_RDY_ID", bodyobj.getString("CHT_RDY_ID"));
            jsonParams.setString("USER_ID", userId);
            jsonParams.setString("CUSTCO_ID", custcoId);
            
            jsonParams.setString("CHT_USER_KEY", chtUserKey);
            jsonParams.setString("SNDR_KEY", sndrKey);
            jsonParams.setString("CHN_CLSF_CD", chnClsfCd);
            
            jsonParams.setString("CUTT_STTS_CD", bodyobj.getString("CUTT_STTS_CD"));

            objRetParams = chatMainService.processRtnTalkHistInfo(jsonParams);

            //INOUT:SET:REDIS - 상담중
            log.trace("<<< INOUT:SET:REDIS - 상담중");
            redisChatInoutRepository.setStompVO(ChatStompVO.builder().userKey(jsonParams.getString("CUST_ID")).agentId(jsonParams.getString("USER_ID")).build());

            //chtCuttId = objRetParams.getString("CHT_CUTT_ID");
        }

        jsonParams.setString("CHT_CUTT_ID", chtCuttId);
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("CHN_CLSF_CD", chnClsfCd);

        objRetParams = chatMainService.selectRtnChatContent(jsonParams);

        objRetParams.setHeader("CHT_CUTT_ID", chtCuttId);
        objRetParams.setHeader("BFR_CHT", objRetParams.getString("BFR_CHT"));
        objRetParams.setHeader("AFTR_CHT", objRetParams.getString("AFTR_CHT"));
//        // [파일o] 채팅메인-통합이력-채팅조회: 채팅-이미지(고객)
//        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
//        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
//        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
//        log.debug("fileProperties>>>{}", fileProperties);
//
//        RepositoryTrgtTypeCd strFileUploadPath = fileProperties.getTrgtTypeCd();
//        switch(strFileUploadPath)
//        {
//            case DB:
//            {
//                JSONArray jsonArray = new JSONArray();
//
//                JSONArray orgFileJsonArray = new JSONArray();
//                orgFileJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
//                for(int i = 0; i < objRetParams.getHeaderInt("COUNT"); i++) {
//                    JSONObject orgFileJsonObject = orgFileJsonArray.getJSONObject(i);
//                    if(!"".equals(orgFileJsonObject.getString("ORG_FILE_ID"))) {
//                        OrgFileVO orgFileVO = new OrgFileVO();
//                        orgFileVO.setCustcoId(custcoId);
//                        orgFileVO.setOrgFileId(orgFileJsonObject.getString("ORG_FILE_ID"));
//                        OrgFileVO orgFileVOtmp = talkRouteMapper.selectOrgFileVo(orgFileVO);
//                        if(orgFileVOtmp != null && orgFileVOtmp.getOrgFile() != null) {
//                            // byte[] --> Base64 변환하여 String으로 넘긴다.
//                            String encFileString = Base64.getEncoder().encodeToString(orgFileVOtmp.getOrgFile());
//                            orgFileJsonObject.put("FILE_BLOB", encFileString);
//                            orgFileJsonObject.put("FILE_EXTN", orgFileVOtmp.getFileExtn());
//                            jsonArray.add(orgFileJsonObject);
//                        }
//                        else {
//                            orgFileJsonObject.put("FILE_BLOB", "");
//                            orgFileJsonObject.put("FILE_EXTN", "");
//                            jsonArray.add(orgFileJsonObject);
//                        }
//                    }
//                    else {
//                        orgFileJsonObject.put("FILE_BLOB", "");
//                        orgFileJsonObject.put("FILE_EXTN", "");
//                        jsonArray.add(orgFileJsonObject);
//                    }
//                }
//
//                objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray);
//                break;
//            }
//            default:
//            {
//                break;
//            }
//        }

        /*
         * if ( "BLOB".equals(strFileUploadPath) ) { JSONArray jsonArray = new JSONArray();
         *
         * JSONArray orgFileJsonArray = new JSONArray(); orgFileJsonArray = objRetParams.getDataObject(TwbCmmnConst.G_DATA); for(int i=0; i<objRetParams.getHeaderInt("COUNT"); i++) { JSONObject orgFileJsonObject =
         * orgFileJsonArray.getJSONObject(i); if ( !"".equals(orgFileJsonObject.getString("ORG_FILE_ID")) ) { OrgFileVO orgFileVO = new OrgFileVO(); orgFileVO.setCustcoId(custcoId);
         * orgFileVO.setOrgFileId(orgFileJsonObject.getString("ORG_FILE_ID")); OrgFileVO orgFileVOtmp = talkRouteMapper.selectOrgFileVo(orgFileVO); if ( orgFileVOtmp != null && orgFileVOtmp.getOrgFile() != null ) { //
         * byte[] --> Base64 변환하여 String으로 넘긴다. String encFileString = Base64.getEncoder().encodeToString(orgFileVOtmp.getOrgFile()); orgFileJsonObject.put("FILE_BLOB", encFileString); orgFileJsonObject.put("FILE_EXTN",
         * orgFileVOtmp.getFileExtn()); jsonArray.add(orgFileJsonObject); } else { orgFileJsonObject.put("FILE_BLOB", ""); orgFileJsonObject.put("FILE_EXTN", ""); jsonArray.add(orgFileJsonObject); } } else {
         * orgFileJsonObject.put("FILE_BLOB", ""); orgFileJsonObject.put("FILE_EXTN", ""); jsonArray.add(orgFileJsonObject); } }
         *
         * objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonArray); }
         */

        return responseEntity(objRetParams);

    }


    /**
     * @param  inHashMap
     * @return             objRetParams
     * @throws IOException
     */
    @NoBizLog
    @SimpleAspectAround
    @ApiOperation(value = "채팅메인-회사로고 경로조회",
                  notes = "채팅메인-회사로고 경로를 조회한다")
    @PostMapping("/chat-api/main/asp-logo/inqire")
    public @ResponseBody Object getImgSrc(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON jsonParams = new TelewebJSON();
        log.debug("=============headerobj=============" + headerobj);
        log.debug("==========bodyobj=========" + bodyobj);
        int userId = (PaletteUserContextSupport.getCurrentUser() == null ? 1 : PaletteUserContextSupport.getCurrentUser().getUserId());
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();

        jsonParams.setInt("USER_ID", userId);    // USER_ID
        jsonParams.setString("CUSTCO_ID", custcoId);   // custcoId
        jsonParams.setString("ASP_NEWCUST_KEY", bodyobj.getString("ASP_NEWCUST_KEY"));  // ASP_NEWCUST_KEY
        jsonParams.setString("SNDR_KEY", bodyobj.getString("SNDR_KEY"));  // ASP_NEWCUST_KEY

        objRetParams = chatMainService.getImgSrc(jsonParams);

        if(objRetParams == null || objRetParams.getSize() == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", "");
            objRetParams.setString("data", "");
        }

        return responseEntity(objRetParams);
    }


    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "채팅채널 조회",
                  notes = "채팅채널 조회")
    @PostMapping("/chat-api/main/sender-key/inqire")
    public Object getSenderKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatMainService.getSenderKey(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "책깔피 등록",
                  notes = "책깔피 등록")
    @PostMapping("/chat-api/main/markup/regist")
    public Object regiMarkUp(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatMainService.regiMarkUp(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "채팅 가능여부 조회",
                  notes = "채팅 가능여부 조회")
    @PostMapping("/chat-api/main/chatYN/inqire")
    public Object getChatYN(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatMainService.getChatYN(jsonParams);

        return objRetParams;
    }

    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "발신프로필키 조회",
                  notes = "발신프로필키 조회")
    @PostMapping("/chat-api/main/getSender/inqire")
    public Object getSender(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatMainService.getSender(jsonParams);

        return objRetParams;
    }

    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "UUID 조회",
                  notes = "UUID 조회")
    @PostMapping("/chat-api/main/get-uuid/inqire")
    public Object getUuid(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatMainService.getUuid(jsonParams);

        return objRetParams;
    }

    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "상담이력 강제생성",
                  notes = "상담이력 강제생성")
    @PostMapping("/chat-api/main/cnslt-hist/regist-force")
    public Object chatCnslForceRegist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.chatCnslForceRegist(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "고객사 채팅 카운트",
                  notes = "고객사 채팅 카운트")
    @PostMapping("/chat-api/main/cnslt/count")
    public Object chatCnslCount(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.chatCnslCount(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "상담유형 조회하기",
                  notes = "선택한 상담유형으로 최상위 상담유형까지 조회하기")
    @PostMapping("/chat-api/main/selCuttTypeSrch")
    public Object selectCuttType(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.selectCuttType(mjsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "게시판 문의 조회",
                  notes = "게시판 문의 조회")
    @PostMapping("/chat-api/main/pstQstn/select")
    public Object selectPstQstn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.selectPstQstn(mjsonParams);

        return objRetParams;
    }
    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "게시판 상담 - 연동 파라미터 조회",
                  notes = "게시판 상담 - 연동 파라미터 조회")
    @PostMapping("/chat-api/main/bbsParam/select")
    public Object selectBbsParams(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.selectBbsParams(mjsonParams);

        return objRetParams;
    }
    /**
     *
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "게시판 상담 - 연동 파라미터 조회",
                  notes = "게시판 상담 - 연동 파라미터 조회")
    @PostMapping("/chat-api/main/bbsAnswr/insert")
    public Object insertBbsParams(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.insertBbsAnswer(mjsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "채팅 설정값 조회",
                  notes = "채팅 설정값 조회")
    @PostMapping("/chat-api/main/getChtStng")
    public Object getChtStng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.getChtStng(mjsonParams);

        return objRetParams;
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "3자채팅을 위한 특정 채팅 조회",
                  notes = "3자채팅을 위한 특정 채팅 조회")
    @RequestMapping(value = "/chat-api/main/cnslt/cuttTriple",
                    method = RequestMethod.POST)
    public @ResponseBody Object getTripleCutt(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String userId = bodyobj.getString("USER_ID");
        String custcoId = bodyobj.getString("CUSTCO_ID");
        String chtCuttId = bodyobj.getString("CHT_CUTT_ID");

        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("CHT_CUTT_ID", chtCuttId);

        objRetParams = chatMainService.getTripleCutt(jsonParams);

        return responseEntity(objRetParams);
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "챗봇 고객에대한 데이터 가져오기",
                  notes = "챗봇 고객에대한 데이터 가져오기")
    @RequestMapping(value = "/chat-api/main/getChbtUserData",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChbtUserData(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String chtUserKey = bodyobj.getString("CHT_USER_KEY");

        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("CHT_USER_KEY", chtUserKey);

        objRetParams = chatMainService.getChbtUserData(jsonParams);
        TelewebJSON decObjRetParams = new TelewebJSON(objRetParams);

        try {
            decObjRetParams.setString("AUTH_YN", objRetParams.getString("AUTH_YN"));
            log.info("1" + this.decrypt(objRetParams.getString("MEMBER_NO")));
            decObjRetParams.setString("MEMBER_NO", decrypt(objRetParams.getString("MEMBER_NO")));
            log.info("2" + this.decrypt(objRetParams.getString("USER_NAME")));
            decObjRetParams.setString("USER_NAME", decrypt(objRetParams.getString("USER_NAME")));
            log.info("3" + this.decrypt(objRetParams.getString("USER_EMAIL")));
            decObjRetParams.setString("USER_EMAIL", decrypt(objRetParams.getString("USER_EMAIL")));
            log.info("4" + this.decrypt(objRetParams.getString("MEMBER_ID")));
            decObjRetParams.setString("MEMBER_ID", decrypt(objRetParams.getString("MEMBER_ID")));
            decObjRetParams.setString("AUTH_DTTM", objRetParams.getString("AUTH_DTTM"));
        } catch (Exception e){
            log.info("decrypt error" + e);
        }
        
        return responseEntity(decObjRetParams);
    }
    
    //챗봇데이터 복호화
    public String decrypt(String cipherText) throws Exception {
        log.info("cipherText" + cipherText);
        Cipher cipher = Cipher.getInstance(this.ALG);
        SecretKeySpec keySpec = new SecretKeySpec(this.KEY.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "고객 챗봇 내역 가져오기",
                  notes = "고객 챗봇 내역 가져오기")
    @RequestMapping(value = "/chat-api/main/getChbtData",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChbtData(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String chtUserKey = bodyobj.getString("CHT_USER_KEY");
        String chtCuttId = bodyobj.getString("CHT_CUTT_ID");
        String chtClsfCd = bodyobj.getString("CHN_CLSF_CD");

        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("CHT_USER_KEY", chtUserKey);
        jsonParams.setString("CHT_CUTT_ID", chtCuttId);
        jsonParams.setString("CHN_CLSF_CD", chtClsfCd);

        objRetParams = chatMainService.getChbtData(jsonParams);
        
        return responseEntity(objRetParams);
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "고객 챗봇 고객 리스트 가져오기 - 확인용",
                  notes = "고객 챗봇 고객 리스트 가져오기 - 확인용")
    @RequestMapping(value = "/chat-api/main/getChbtUser",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChbtUser(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String chtUserKey = bodyobj.getString("CHT_USER_KEY");
        String chtClsfCd = bodyobj.getString("CHN_CLSF_CD");
        String custcoId = bodyobj.getString("CUSTCO_ID");

        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("CHT_USER_KEY", chtUserKey);
        jsonParams.setString("CHN_CLSF_CD", chtClsfCd);
        jsonParams.setString("CUSTCO_ID", custcoId);

        objRetParams = chatMainService.getChbtUser(jsonParams);
        
        return responseEntity(objRetParams);
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "고객 챗봇 전체 내역 가져오기 - 확인용",
                  notes = "고객 챗봇 전체 내역 가져오기 - 확인용")
    @RequestMapping(value = "/chat-api/main/getChbtUserHsty",
                    method = RequestMethod.POST)
    public @ResponseBody Object getChbtUserHsty(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String chtUserKey = bodyobj.getString("CHT_USER_KEY");

        TelewebJSON jsonParams = new TelewebJSON();
        
        jsonParams.setString("CHT_USER_KEY", chtUserKey);

        objRetParams = chatMainService.getChbtUserHsty(jsonParams);
        
        return responseEntity(objRetParams);
    }
    
    /**
     * @param  inHashMap
     * @return           objRetParams
     */
    @NoBizLog
    @ApiOperation(value = "고객 챗봇 전체 내역 가져오기 - 확인용",
                  notes = "고객 챗봇 전체 내역 가져오기 - 확인용")
    @RequestMapping(value = "/chat-api/main/kakaoFileUpload",
                    method = RequestMethod.POST)
    public @ResponseBody Object kakaoFileUpload(@RequestHeader LinkedHashMap<Object, Object> headerobj, @RequestBody JSONObject bodyobj) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        System.out.println("bodyobj" + bodyobj);
        String filePath = bodyobj.getString("FILE");
        
        URL fileUrl;
		try {
			fileUrl = new URL(filePath);
	        File file = new File(fileUrl.getPath());
	        
	        String dsptchPrfKey = bodyobj.getString("DSPTCH_PRF_KEY");
	        String fileType = bodyobj.getString("TYPE");
	        
	        String url = "https://bzc-api.kakao.com/v1/0ee0d8c660525467124e8ce3c7464f0ad98dd0e0/file/upload";

	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(url);

	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	        builder.addTextBody("sender_key", dsptchPrfKey);
	        builder.addTextBody("file_type", fileType);
	        builder.addBinaryBody("file", file);
	        
	    	log.info("builder >>>> " + builder);

	        HttpEntity entity = (HttpEntity) builder.build();
	    	log.info("entity >>>> " + entity);
	        httpPost.setEntity((org.apache.http.HttpEntity) entity);

	        HttpResponse response;
			try {
				response = httpClient.execute(httpPost);
		        int statusCode = response.getStatusLine().getStatusCode();

		        if (statusCode == 200) {
		            // 성공 처리
		        	log.info("성공" + response);
		        } else {
		            // 실패 처리
		        	log.info("실패" + response);
		        }
			} catch (ClientProtocolException e) {
	        	log.info("실패 >>> ClientProtocolException" + e);
				e.printStackTrace();
			} catch (IOException e) {
	        	log.info("실패 >>> IOException" + e);
				e.printStackTrace();
			}     
		} catch (MalformedURLException e) {
        	log.info("실패 >>> MalformedURLException" + e);
			e.printStackTrace();
		}
        
        return responseEntity(objRetParams);
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "현재 고객이 상담중인지 체크",
                  notes = "현재 고객이 상담중인지 체크")
    @PostMapping("/chat-api/main/chkInputPsblty")
    public Object chkInputPsblty(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = chatMainService.chkInputPsblty(mjsonParams);

        return objRetParams;
    }
    
}
