package kr.co.hkcloud.palette3.chat.main.api;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
//import kr.co.hkcloud.palette3.infra.tplex.cti.app.IvrCallbackDataService;
import kr.co.hkcloud.palette3.chat.main.util.NotificationTalkTemplateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import kr.co.hkcloud.palette3.chat.main.app.NotificationTalkService;

/**
 * 알림톡 데이터 REST 컨트롤러 클래스
 * 
 * @author dckim
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "NotificationTalkRestController",
     description = "알림톡 데이터 REST 컨트롤러")
public class NotificationTalkRestController {

	private final InnbCreatCmmnService   innbCreatCmmnService;
	private final NotificationTalkTemplateUtils   NotificationTalkTemplateUtils;
	private final NotificationTalkService   NotificationTalkService;	
    public String erroMsg(String resultCode)
    {
        String ErrorMsg = ""; // 에러메시지와 에러코드 전부 임시값입니다.
        switch(resultCode)
        {
        case "0":
            ErrorMsg = "정상적으로 등록되었습니다.";
            break;
        case "1":
            ErrorMsg = "TRAN_CALLBACK is null.";
            break;
        case "2":
            ErrorMsg = "TRAN_PHONE is null.";
            break;
        case "3":
            ErrorMsg = "CUSTCO_ID in null";
            break;
        case "4":
            ErrorMsg = "TRAN_MSG is null.";
            break;
        case "5":
            ErrorMsg = "TRAN_DATE is null.";
            break;
        case "6":
            ErrorMsg = "FKEY 값이 없습니다.";
            break;
        case "7":
            ErrorMsg = "TRAN_STATUS is null.";
            break;
        case "8":
            ErrorMsg = "TRAN_REPLACE_MSG is null.";
            break;
        case "9":
            ErrorMsg = "TRAN_REPLACE_MSG is null.";
            break;

        }
        return ErrorMsg;
    }
    /**
     * 알림톡 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "알림톡 데이터 등록",
                  notes = "알립톡 데이터 등록")
    @PostMapping("/chat-api/main/ntTalk-regist/process")
    public String ntTalkRegistProcess(HttpServletRequest request) throws Exception
    {
        TelewebJSON objSetParams = new TelewebJSON();
        TelewebJSON custcoParams = new TelewebJSON();
        String TRAN_PHONE = "";
        String TRAN_DATE = "";        
        String ASP_CUST_KEY = "";
        String OPT = "";
        String TMPL_CD = "";
        String UUID = "";
        String SNDNG_SE_CD = "";
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			log.debug("paramName : " + paramName + "paramValue : " + paramValue);

			if(paramName.equals("ASP_CUST_KEY")) {
				ASP_CUST_KEY = paramValue;
			}
			if(paramName.equals("TRAN_PHONE")) {
				TRAN_PHONE = paramValue;
			}
			if(paramName.equals("TRAN_DATE")) {
				TRAN_DATE = paramValue;
			}
			if(paramName.equals("TMPL_CD")) {
				TMPL_CD = paramValue;
			}
			if(paramName.equals("TRAN_OPT")) {
				OPT = paramValue;
			}
			if(paramName.equals("UUID")) {
				UUID = paramValue;
			}
            if(paramName.equals("SNDNG_SE_CD")) {
                SNDNG_SE_CD = paramValue;
			}
		}
	       
        if(ASP_CUST_KEY == null || ASP_CUST_KEY.equals("") || ASP_CUST_KEY == "") { //return "false"; 
            log.debug("ASP_CUST_KEY in null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result_code", "1"); 
            String ResultCode = jsonObject.getString("result_code");
            jsonObject.put("result_msg", erroMsg(ResultCode));
            return jsonObject.toString();
        } 
        if(TRAN_PHONE == null || TRAN_PHONE.equals("") || TRAN_PHONE == "") { 
            log.debug("TRAN_PHONE is null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result_code", "2"); 
            String ResultCode = jsonObject.getString("result_code");
            jsonObject.put("result_msg", erroMsg(ResultCode));
            return jsonObject.toString();
        }        
//        if(TRAN_DATE == null || TRAN_DATE.equals("") || TRAN_DATE == "") { //return "false"; 
//            log.debug("TRAN_DATE is null.");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("result_code", "3"); 
//            String ResultCode = jsonObject.getString("result_code");
//            jsonObject.put("result_msg", erroMsg(ResultCode));
//            return jsonObject.toString();
//        }           
        if(TMPL_CD == null || TMPL_CD.equals("") || TMPL_CD == "") { //return "false"; 
            log.debug("TMPL_CD in null.");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result_code", "4"); 
            String ResultCode = jsonObject.getString("result_code");
            jsonObject.put("result_msg", erroMsg(ResultCode));
            return jsonObject.toString();
        }         
//        if(OPT == null || OPT.equals("") || OPT == "") { //return "false"; 
//            log.debug("OPT in null.");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("result_code", "5"); 
//            String ResultCode = jsonObject.getString("result_code");
//            jsonObject.put("result_msg", erroMsg(ResultCode));
//            return jsonObject.toString();
//        }          
//        if(UUID == null || UUID.equals("") || UUID == "") { //return "false"; 
//            log.debug("UUID in null.");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("result_code", "4"); 
//            String ResultCode = jsonObject.getString("result_code");
//            jsonObject.put("result_msg", erroMsg(ResultCode));
//            return jsonObject.toString();
//        }  
        objSetParams.setString("TRAN_PHONE", TRAN_PHONE);
        objSetParams.setString("TRAN_DATE", TRAN_DATE);    
        objSetParams.setString("ASP_CUST_KEY", ASP_CUST_KEY);  
        objSetParams.setString("OPT", OPT);
        objSetParams.setString("TMPL_CD", TMPL_CD);
        objSetParams.setString("UUID", UUID);
        objSetParams.setString("SNDNG_SE_CD", SNDNG_SE_CD);

        
        //log.debug("테스트 중입니다");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result_code", "0"); //{\"result_code\":\"0\",\"result_msg\":\"정상적으로 등록되었습니다.\"}
        String ResultCode = jsonObject.getString("result_code");
        jsonObject.put("result_msg", erroMsg(ResultCode));

        custcoParams = NotificationTalkService.getAtalkSchema(objSetParams);
        
        TenantContext.setCurrentTenant(custcoParams.getString("SCHEMA_ID"));
        log.info("===> TenantContext.getCurrentTenant(): " + TenantContext.getCurrentTenant());
        
        if(objSetParams.getString("SNDNG_SE_CD").isEmpty()){
            NotificationTalkService.nfTalkDataRegist(objSetParams);
        }else{
            NotificationTalkService.smsDataRegist(objSetParams);
        }

        return jsonObject.toString();
    }	
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 내역 리스트",
                  notes = "알림톡 내역 리스트")
    @PostMapping("/chat-api/alrim/list")
    public Object getAlrimList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.getAlrimList(jsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 목록 리스트",
                  notes = "알림톡 목록 리스트")
    @PostMapping("/chat-api/alrim/Catalog")
    public Object getAlrimCatalog(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.getAlrimCatalog(jsonParams);

        return objRetParams;
    }    
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 전송",
                  notes = "알림톡 전송")
    @PostMapping("/chat-api/alrim/regist")
    public Object regAlrim(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String desc = jsonParams.getString("TRAN_MSG");
        desc = desc.replaceAll("&lt;br&gt;", "\n");
        desc = desc.replaceAll("&amp;", "&");
        desc = desc.replaceAll("&lt;", "<");
        desc = desc.replaceAll("&gt;", ">");
        desc = desc.replaceAll("&#40;", "(");
        desc = desc.replaceAll("&#41;", ")");
        desc = desc.replaceAll("&#91;", "[");
        desc = desc.replaceAll("&#93;", "]");
        desc = desc.replaceAll("&quot;", "\"");
        desc = desc.replaceAll("&apos;", "'");
        jsonParams.setString("TRAN_MSG", desc);

        String rep_desc = jsonParams.getString("TRAN_REPLACE_MSG");
        rep_desc = rep_desc.replaceAll("&lt;br&gt;", "\n");
        rep_desc = rep_desc.replaceAll("&amp;", "&");
        rep_desc = rep_desc.replaceAll("&lt;", "<");
        rep_desc = rep_desc.replaceAll("&gt;", ">");
        rep_desc = rep_desc.replaceAll("&#40;", "(");
        rep_desc = rep_desc.replaceAll("&#41;", ")");
        rep_desc = rep_desc.replaceAll("&#91;", "[");
        rep_desc = rep_desc.replaceAll("&#93;", "]");
        rep_desc = rep_desc.replaceAll("&quot;", "\"");
        rep_desc = rep_desc.replaceAll("&apos;", "'");
        jsonParams.setString("TRAN_REPLACE_MSG", rep_desc);

        log.debug("jsonParams1" + jsonParams);
        log.debug("TRAN_BUTTON" + jsonParams.getString("TRAN_BUTTON"));

        String butn = jsonParams.getString("TRAN_BUTTON");
        butn = butn.replaceAll("&lt;br&gt;", "\n");
        butn = butn.replaceAll("&amp;", "&");
        butn = butn.replaceAll("&lt;", "<");
        butn = butn.replaceAll("&gt;", ">");
        butn = butn.replaceAll("&#40;", "(");
        butn = butn.replaceAll("&#41;", ")");
        butn = butn.replaceAll("&#91;", "[");
        butn = butn.replaceAll("&#93;", "]");
        butn = butn.replaceAll("&quot;", "\"");
        butn = butn.replaceAll("&apos;", "'");
        
        //butn = butn.replaceAll("\\{", "&#123;");
        //butn = butn.replaceAll("\\}", "&#125;");
        butn = butn.replaceAll("\\},\\{", "&#125;&#123;");
        
        jsonParams.setString("TRAN_BUTTON", butn);

        String hlit = jsonParams.getString("TMPL_HILIT");
        hlit = hlit.replaceAll("&lt;br&gt;", "\n");
        hlit = hlit.replaceAll("&amp;", "&");
        hlit = hlit.replaceAll("&lt;", "<");
        hlit = hlit.replaceAll("&gt;", ">");
        hlit = hlit.replaceAll("&#40;", "(");
        hlit = hlit.replaceAll("&#41;", ")");
        hlit = hlit.replaceAll("&#91;", "[");
        hlit = hlit.replaceAll("&#93;", "]");
        hlit = hlit.replaceAll("&quot;", "\"");
        hlit = hlit.replaceAll("&apos;", "'");
        
        //hlit = hlit.replaceAll("\\{", "&#123;");
        //hlit = hlit.replaceAll("\\}", "&#125;");
        
        jsonParams.setString("TMPL_HILIT", hlit);

        String item = jsonParams.getString("TMPL_ITEM");
        item = item.replaceAll("&lt;br&gt;", "\n");
        item = item.replaceAll("&amp;", "&");
        item = item.replaceAll("&lt;", "<");
        item = item.replaceAll("&gt;", ">");
        item = item.replaceAll("&#40;", "(");
        item = item.replaceAll("&#41;", ")");
        item = item.replaceAll("&#91;", "[");
        item = item.replaceAll("&#93;", "]");
        item = item.replaceAll("&quot;", "\"");
        item = item.replaceAll("&apos;", "'");
        
        //item = item.replaceAll("\\{", "&#123;");
        //item = item.replaceAll("\\}", "&#125;");
        
        jsonParams.setString("TMPL_ITEM", item);

        log.debug("jsonParams"+ jsonParams);

        objRetParams = NotificationTalkService.regAlrim(jsonParams);

        return objRetParams;
    }
 
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 사용여부 체크",
                  notes = "알림톡 사용여부 체크")
    @PostMapping("/chat-api/alrim/check")
    public Object checkAlrim(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.checkAlrim(jsonParams);

        return objRetParams;
    }    
    
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 등록",
                  notes = "알림톡 템플릿 등록")
    @PostMapping("/chat-api/alrim/tmpl/regist")
    public Object regiAlrimTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        
        String desc = jsonParams.getString("TMPL_CON");
        desc = desc.replaceAll("&lt;br&gt;", "\n");
        desc = desc.replaceAll("&amp;", "&");
        desc = desc.replaceAll("&lt;", "<");
        desc = desc.replaceAll("&gt;", ">");
        desc = desc.replaceAll("&#40;", "(");
        desc = desc.replaceAll("&#41;", ")");
        desc = desc.replaceAll("&#91;", "[");
        desc = desc.replaceAll("&#93;", "]");
        desc = desc.replaceAll("&quot;", "\"");
        desc = desc.replaceAll("&apos;", "'");
        jsonParams.setString("TMPL_CON", desc);
        
        String ext = jsonParams.getString("TMPL_EXT");
        ext = ext.replaceAll("&lt;br&gt;", "\n");
        ext = ext.replaceAll("&amp;", "&");
        ext = ext.replaceAll("&lt;", "<");
        ext = ext.replaceAll("&gt;", ">");
        ext = ext.replaceAll("&#40;", "(");
        ext = ext.replaceAll("&#41;", ")");
        ext = ext.replaceAll("&#91;", "[");
        ext = ext.replaceAll("&#93;", "]");
        ext = ext.replaceAll("&quot;", "\"");
        ext = ext.replaceAll("&apos;", "'");
        jsonParams.setString("TMPL_EXT", ext);

        objRetParams = NotificationTalkService.regiAlrimTmpl(jsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 조회",
                  notes = "알림톡 템플릿 조회")
    @PostMapping("/chat-api/alrim/tmpl/inqire")
    public Object inqAlrimTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.inqAlrimTmpl(jsonParams);

        return objRetParams;
    }
	
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 상태 업데이트",
                  notes = "알림톡 템플릿 상태 업데이트")
    @PostMapping("/chat-api/alrim/tmpl/stat/update")
    public Object updateAlrimTmplStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.updateAlrimTmplStat(jsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 삭제",
                  notes = "알림톡 템플릿 삭제")
    @PostMapping("/chat-api/alrim/tmpl/delete")
    public Object delAlrimTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.delAlrimTmpl(jsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡다건발송.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "알림톡다건발송",
                  notes = "알림톡다건발송")
    @PostMapping("/chat-api/alrim/multiSend")
    public Object alrimMultiSend(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {

        TelewebJSON objRetKey = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);   //반환 파라메터 생성
        TelewebJSON obndCustParams = new TelewebJSON(mjsonParams);

        // FORM(DATA)데이터
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
        String regMan = mjsonParams.getString("REG_MAN");                   //화면에서 전송된 플래그 설정
        String chngMan = mjsonParams.getString("CHNG_MAN");                   //화면에서 전송된 플래그 설정
        

        String desc = jsonParams.getString("TRAN_MSG");
        desc = desc.replaceAll("&lt;br&gt;", "\n");
        desc = desc.replaceAll("&amp;", "&");
        desc = desc.replaceAll("&lt;", "<");
        desc = desc.replaceAll("&gt;", ">");
        desc = desc.replaceAll("&#40;", "(");
        desc = desc.replaceAll("&#41;", ")");
        desc = desc.replaceAll("&#91;", "[");
        desc = desc.replaceAll("&#93;", "]");
        desc = desc.replaceAll("&quot;", "\"");
        desc = desc.replaceAll("&apos;", "'");
        jsonParams.setString("TRAN_MSG", desc);

        String rep_desc = jsonParams.getString("TRAN_REPLACE_MSG");
        rep_desc = rep_desc.replaceAll("&lt;br&gt;", "\n");
        rep_desc = rep_desc.replaceAll("&amp;", "&");
        rep_desc = rep_desc.replaceAll("&lt;", "<");
        rep_desc = rep_desc.replaceAll("&gt;", ">");
        rep_desc = rep_desc.replaceAll("&#40;", "(");
        rep_desc = rep_desc.replaceAll("&#41;", ")");
        rep_desc = rep_desc.replaceAll("&#91;", "[");
        rep_desc = rep_desc.replaceAll("&#93;", "]");
        rep_desc = rep_desc.replaceAll("&quot;", "\"");
        rep_desc = rep_desc.replaceAll("&apos;", "'");
        jsonParams.setString("TRAN_REPLACE_MSG", rep_desc);

        String butn = jsonParams.getString("TRAN_BUTTON");
        butn = butn.replaceAll("&lt;br&gt;", "\n");
        butn = butn.replaceAll("&amp;", "&");
        butn = butn.replaceAll("&lt;", "<");
        butn = butn.replaceAll("&gt;", ">");
        butn = butn.replaceAll("&#40;", "(");
        butn = butn.replaceAll("&#41;", ")");
        butn = butn.replaceAll("&#91;", "[");
        butn = butn.replaceAll("&#93;", "]");
        butn = butn.replaceAll("&quot;", "\"");
        butn = butn.replaceAll("&apos;", "'");
        
        //butn = butn.replaceAll("\\{", "&#123;");
        //butn = butn.replaceAll("\\}", "&#125;");
        butn = butn.replaceAll("\\},\\{", "&#125;&#123;");
        
        jsonParams.setString("TRAN_BUTTON", butn);

        String hlit = jsonParams.getString("TMPL_HILIT");
        hlit = hlit.replaceAll("&lt;br&gt;", "\n");
        hlit = hlit.replaceAll("&amp;", "&");
        hlit = hlit.replaceAll("&lt;", "<");
        hlit = hlit.replaceAll("&gt;", ">");
        hlit = hlit.replaceAll("&#40;", "(");
        hlit = hlit.replaceAll("&#41;", ")");
        hlit = hlit.replaceAll("&#91;", "[");
        hlit = hlit.replaceAll("&#93;", "]");
        hlit = hlit.replaceAll("&quot;", "\"");
        hlit = hlit.replaceAll("&apos;", "'");
        
        //hlit = hlit.replaceAll("\\{", "&#123;");
        //hlit = hlit.replaceAll("\\}", "&#125;");
        
        jsonParams.setString("TMPL_HILIT", hlit);

        String item = jsonParams.getString("TMPL_ITEM");
        item = item.replaceAll("&lt;br&gt;", "\n");
        item = item.replaceAll("&amp;", "&");
        item = item.replaceAll("&lt;", "<");
        item = item.replaceAll("&gt;", ">");
        item = item.replaceAll("&#40;", "(");
        item = item.replaceAll("&#41;", ")");
        item = item.replaceAll("&#91;", "[");
        item = item.replaceAll("&#93;", "]");
        item = item.replaceAll("&quot;", "\"");
        item = item.replaceAll("&apos;", "'");
        
        //item = item.replaceAll("\\{", "&#123;");
        //item = item.replaceAll("\\}", "&#125;");
        
        jsonParams.setString("TMPL_ITEM", item);

        jsonParams.setString("REG_MAN", regMan);
        jsonParams.setString("CHNG_MAN", chngMan);
        jsonParams.setString("DATA_FLAG", strTransFlag);
        JSONArray smsCustArr = mjsonParams.getDataObject("ALRIM_CUST_LIST");
        jsonParams.setDataObject("ALRIM_CUST_ARR", smsCustArr);

        //캠페인 등록
        objRetParams = NotificationTalkService.alrimMultiSend(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 중복체크",
                  notes = "알림톡 템플릿 중복체크")
    @PostMapping("/chat-api/alrim/tmpl/count")
    public Object cntAlrimTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.cntAlrimTmpl(jsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 카카오 메인 이미지URL",
                  notes = "알림톡 템플릿 카카오 메인 이미지URL")
    @PostMapping("/chat-api/alrim/tmpl/main-image")
    public Object alrimMainImgUrl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.alrimMainImgUrl(jsonParams);

        return objRetParams;
    }

    /**
     * 
     * @param  inHashMap
     * @return             TelewebJSON 형식의 조회결과 데이터
     * @throws IOException
     */
    @ApiOperation(value = "알림톡 템플릿 카카오 하이라이트 이미지URL",
                  notes = "알림톡 템플릿 카카오 하이라이트 이미지URL")
    @PostMapping("/chat-api/alrim/tmpl/hlit-image")
    public Object alrimHlitImgUrl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException, IOException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = NotificationTalkService.alrimHlitImgUrl(jsonParams);

        return objRetParams;
    }
}
