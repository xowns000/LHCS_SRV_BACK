package kr.co.hkcloud.palette3.chat.main.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Iterator;

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

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.message.app.MessageService;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
//import ;
import kr.co.hkcloud.palette3.chat.main.app.NotificationTalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("NotificationTalkService")
public class NotificationTalkServicelmpl implements NotificationTalkService{
	
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;
    private final PaletteCmmnService   paletteCmmnService;
    private final MessageService messageService;

    /**
     * 알림톡 데이터 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON nfTalkDataRegist(TelewebJSON mjsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON custcoParams = new TelewebJSON(mjsonParams);
        TelewebJSON custcoId = new TelewebJSON(mjsonParams);
        TelewebJSON tmplParams = new TelewebJSON(mjsonParams);
        //ASP_CUST_KEY로 고객사 정보(SCHEMA_ID) 가져오기
        custcoParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getSchemaId", mjsonParams);
        custcoId = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getCustcoId", mjsonParams);
        
        TenantContext.setCurrentTenant(custcoParams.getString("SCHEMA_ID"));
        log.info("===> TenantContext.getCurrentTenant(): " + TenantContext.getCurrentTenant());
        
        //템플릿 코드로 템플릿 정보 가져오기
        tmplParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getTmplInfo", mjsonParams);

        mjsonParams.setString("SNDNG_SE_CD", "ATALK");
		mjsonParams.setString("CUSTCO_ID", custcoId.getString("CUSTCO_ID"));
		mjsonParams.setString("tenantId", custcoParams.getString("SCHEMA_ID"));
        mjsonParams.setString("sender_key", tmplParams.getString("DSPTCH_PRF_KEY"));
        mjsonParams.setString("template_code", tmplParams.getString("TMPL_CD"));
        mjsonParams.setString("message", tmplParams.getString("TMPL_CN"));
        mjsonParams.setString("title", tmplParams.getString("EPSZ_INDCT_MPIT_INFO"));
        mjsonParams.setString("tran_type", "L");
        mjsonParams.setString("subject", "전환전송 메시지");
        mjsonParams.setString("tran_message", tmplParams.getString("TMPL_CN"));
        
        mjsonParams.setString("callback_number", custcoParams.getString("DSPTCH_NO"));
        
        mjsonParams.setString("phone_number", mjsonParams.getString("TRAN_PHONE"));
        
        mjsonParams.setString("USER_ID", "2");

        objRetParams = messageService.sendInfo(mjsonParams);

        return objRetParams;
    }

	/**
     * sms 데이터 등록
     * TODO 외부API 호출 문자발송 개발
     *
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON smsDataRegist(TelewebJSON mjsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON custcoParams = new TelewebJSON(mjsonParams);
        TelewebJSON custcoId = new TelewebJSON(mjsonParams);
        TelewebJSON tmplParams = new TelewebJSON(mjsonParams);
        //ASP_CUST_KEY로 고객사 정보(SCHEMA_ID) 가져오기
        custcoParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getSchemaId", mjsonParams);
        custcoId = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getCustcoId", mjsonParams);

        TenantContext.setCurrentTenant(custcoParams.getString("SCHEMA_ID"));
        log.info("===> TenantContext.getCurrentTenant(): " + TenantContext.getCurrentTenant());

        //템플릿 코드(=템플릿 타이틀)로 템플릿 정보 가져오기
        //문자 발송 요청일 때 템플릿 코드는 문자 템플릿타이틀 
        tmplParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getSmsTmplInfo", mjsonParams);

		mjsonParams.setString("CUSTCO_ID", custcoId.getString("CUSTCO_ID"));
		mjsonParams.setString("tenantId", custcoParams.getString("SCHEMA_ID"));
        mjsonParams.setString("message", tmplParams.getString("SMS_TMPL_CN"));
        mjsonParams.setString("subject", tmplParams.getString("SMS_TMPL_NM"));
        if(!tmplParams.getString("IMG_URL").equals("") && tmplParams.getString("IMG_URL") != null) {
        	mjsonParams.setString("img_url", tmplParams.getString("IMG_URL"));
        }

        mjsonParams.setString("callback_number", custcoParams.getString("DSPTCH_NO"));

        mjsonParams.setString("phone_number", mjsonParams.getString("TRAN_PHONE"));

        mjsonParams.setString("USER_ID", "2");

        objRetParams = messageService.sendInfo(mjsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡 내역 리스트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getAlrimList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getAlrimList", jsonParams);

        return objRetParams;
    }

    /**
     * 알림톡 목록 리스트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON getAlrimCatalog(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getAlrimCatalog", jsonParams);

        return objRetParams;
    }    
 
    /**
     * 알림톡 전송
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON regAlrim(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        String seq = innbCreatCmmnService.getSeqNo("MTS_ATALK_MSG", "ALM");

        int idx = seq.indexOf("A");
        //mjsonParams.setString("TRAN_PR", seq); 
        String TRAN_PR = seq.substring(idx);

        jsonParams.setString("TRAN_PR", TRAN_PR);

        String stritem = "";
        String item_list = "";
        jsonParams.setString("ITEM_SUM_TIT","");
        jsonParams.setString("ITEM_SUM_DESC","");
        if(jsonParams.getString("TMPL_ITEM") != null && jsonParams.getString("TMPL_ITEM") != "") {
        	stritem = jsonParams.getString("TMPL_ITEM");
        	item_list = stritem.substring(stritem.indexOf("["), stritem.indexOf("]"));

	        jsonParams.setString("ITEM_SUM_TIT",stritem.substring(stritem.lastIndexOf("e\":")+3, stritem.lastIndexOf(",")));
	        jsonParams.setString("ITEM_SUM_DESC",stritem.substring(stritem.lastIndexOf(":")+1, stritem.lastIndexOf("}")-1));
        }
        for(int i=0;i<10;i++){
            if(item_list.indexOf(":") == -1){
              jsonParams.setInt("ITEM_NUM",i);
              switch(i) {
              	case 0:
              		for(int n=0;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 1:
              		for(int n=1;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 2:
              		for(int n=2;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 3:
              		for(int n=3;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 4:
              		for(int n=4;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 5:
              		for(int n=5;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 6:
              		for(int n=6;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 7:
              		for(int n=7;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 8:
              		for(int n=8;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              	case 9:
              		for(int n=9;n<10;n++) {
                  		jsonParams.setString("ITEM_LIST_TIT_"+n,"");
                  		jsonParams.setString("ITEM_LIST_DESC_"+n,"");
              		}
              		break;
              }
              break;
            }
            jsonParams.setString("ITEM_LIST_TIT_"+i,item_list.substring(item_list.indexOf(":")+1, item_list.indexOf(",")));
            item_list = item_list.replaceFirst(":","_");
            item_list = item_list.replaceFirst(",","_");
            log.debug("item_list"+ item_list);
            jsonParams.setString("ITEM_LIST_DESC_"+i, item_list.substring(item_list.indexOf(":")+1, item_list.indexOf("}")));
            item_list = item_list.replaceFirst(":","_");
            item_list = item_list.replaceFirst(",","_");
            item_list = item_list.replaceFirst("}","_");
            log.debug("item_list"+ item_list);
            
        }
        
        String butn = "";
        String[] butn_list = new String[5];
        String[] butn_list_list = new String[8];
        butn_list[0] = "";
        butn_list[1] = "";
        butn_list[2] = "";
        butn_list[3] = "";
        butn_list[4] = "";
        butn_list_list[0] = "";
        butn_list_list[1] = "";
        butn_list_list[2] = "";
        butn_list_list[3] = "";
        butn_list_list[4] = "";
        butn_list_list[5] = "";
        butn_list_list[6] = "";
        butn_list_list[7] = "";
        if(jsonParams.getString("TRAN_BUTTON") != null && jsonParams.getString("TRAN_BUTTON") != "" && jsonParams.getString("TRAN_BUTTON").length() != 0) {
        	butn = jsonParams.getString("TRAN_BUTTON");

        	if(butn.contains("&#125;&#123;")) {
        		for(int n=0;n<butn.split("&#125;&#123;").length;n++) {
        			butn_list[n]=butn.split("&#125;&#123;")[n];
        		}
        	} else {
        		butn_list[0] = butn;
        	}
        }
        for(int i=0;i<5;i++){
        	if(butn_list[i] == "" || butn_list[i] == null) {
        		jsonParams.setString("BUTN_NM_"+i,"");
        		jsonParams.setString("BUTN_TY_"+i,"");
        		jsonParams.setString("BUTN_ADR_"+i,"");
                jsonParams.setString("BUTN_IOS_"+i,"");
                jsonParams.setString("BUTN_MO_"+i,"");
                jsonParams.setString("BUTN_PC_"+i,"");
                jsonParams.setString("BUTN_EXT_"+i,"");
                jsonParams.setString("BUTN_EVT_"+i,"");
        	} else {
        		jsonParams.setString("BUTN_NM_"+i,"");
                jsonParams.setString("BUTN_TY_"+i,"");
                jsonParams.setString("BUTN_ADR_"+i,"");
                jsonParams.setString("BUTN_IOS_"+i,"");
                jsonParams.setString("BUTN_MO_"+i,"");
                jsonParams.setString("BUTN_PC_"+i,"");
                jsonParams.setString("BUTN_EXT_"+i,"");
                jsonParams.setString("BUTN_EVT_"+i,"");
        		for(int x=0;x<butn_list[i].split("\",\"").length;x++) {
        			butn_list_list[x]=butn_list[i].split("\",\"")[x];
        		}
        		for(int j=0;j<8;j++) {
        			int listlen = butn_list_list[j].length();
        			if(butn_list_list[j].contains("name\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				}else {
        					jsonParams.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("type\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("scheme_android\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("scheme_ios\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("url_mobile\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("url_pc\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("chat_extra\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			} else if(butn_list_list[j].contains("chat_event\"")) {
        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
        					jsonParams.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
        					jsonParams.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
        				} else {
        					jsonParams.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
        				}
        			}
        		}
        	}
        }
        
        
        String strhlit = "";
        jsonParams.setString("HLIT_TIT","");
        jsonParams.setString("HLIT_DESC","");
        jsonParams.setString("HLIT_IMG","");
        if(jsonParams.getString("TMPL_HILIT") != null && jsonParams.getString("TMPL_HILIT") != "" && jsonParams.getString("TMPL_HILIT") != "{}") {
        	strhlit = jsonParams.getString("TMPL_HILIT");

        	if(strhlit.contains("title")) {
		        jsonParams.setString("HLIT_TIT",strhlit.substring(strhlit.indexOf("e\":")+3, strhlit.indexOf(",")));
		        if(strhlit.contains("imageUrl")) {
		        	jsonParams.setString("HLIT_DESC",strhlit.substring(strhlit.indexOf("n\":")+3, strhlit.lastIndexOf(",")));
		        	jsonParams.setString("HLIT_IMG",strhlit.substring(strhlit.lastIndexOf(":")+1, strhlit.lastIndexOf("}")).replaceFirst("&#58;",":"));
		        } else {
		        	jsonParams.setString("HLIT_DESC",strhlit.substring(strhlit.lastIndexOf(":")+1, strhlit.lastIndexOf("}")));
		        }
        	}
        }
        log.debug("jsonParams2"+ jsonParams);
        
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "regAlrim", jsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡 사용여부 체크
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON checkAlrim(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "checkAlrim", jsonParams);

        return objRetParams;
    }    
    
    
    /**
     * 알림톡 템플릿 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON regiAlrimTmpl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        
        String CHK = jsonParams.getString("CHK");
        
        if(CHK.equals("0")) {
        	objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "regiAlrimTmpl", jsonParams);
        } else if(CHK.equals("1")) {
        	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "modiAlrimTmpl", jsonParams);
        }

        return objRetParams;
    }
    
    /**
     * 알림톡 템플릿 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON inqAlrimTmpl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "inqAlrimTmpl", jsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡 템플릿 상태 업데이트
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateAlrimTmplStat(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "updateAlrimTmplStat", jsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡 템플릿 삭제
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON delAlrimTmpl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "delAlrimTmpl", jsonParams);

        return objRetParams;
    }
    
    /**
     * 알림톡다건발송
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON alrimMultiSend(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();


    	String SMS_TYP = jsonParams.getString("SMS_TYP");
    	String seq = innbCreatCmmnService.getSeqNo("MTS_ATALK_MSG", "ALM");
    	int idx = seq.indexOf("A"); 
        String TRAN_PR = seq.substring(idx);
        jsonParams.setString("TRAN_PR", TRAN_PR); 
        
        String btnOrg = jsonParams.getString("TRAN_BUTTON");
        String btn = "";
        if(!btnOrg.equals("") && !btnOrg.equals(null)) {
	        btn = btnOrg.substring(5,btnOrg.length()-1);
	        btn = btn.replaceAll("=", ":");
        }
        String item = jsonParams.getString("TRAN_ATTACHMENT");
        item = item.replaceAll("=", ":");
        
        String hilit = jsonParams.getString("TRAN_TITLE");
        hilit = hilit.replaceAll("=", ":");
        
        log.debug("TRAN_PR"+TRAN_PR);
        if(!TRAN_PR.equals("") && TRAN_PR != null) {
            // null 체크
            log.debug("here@@@" + jsonParams.getDataObject("ALRIM_CUST_ARR"));
            if(jsonParams.getDataObject("ALRIM_CUST_ARR") != null) {

                // 캠페인고객대상정보조회
                JSONArray smsCustArr = jsonParams.getDataObject("ALRIM_CUST_ARR");

                // 캠페인대상 고객 정보가 있는 경우만 
                if(smsCustArr.size() > 0) {
                    @SuppressWarnings("unchecked")
                    Iterator<JSONObject> smsCustIter = smsCustArr.iterator();

                    while(smsCustIter.hasNext()) {

                        JSONObject jsonObj = new JSONObject();
                        jsonObj = smsCustIter.next();

                        System.out.println("jsonObj====>" + jsonObj);
                        TelewebJSON objRetCust = new TelewebJSON();
                        objRetCust.setString("TRAN_PR", TRAN_PR+jsonObj.getString("MOBIL_NO").substring(3));// SMS ID
                        
                        String stritem = "";
                        String item_list = "";
                        objRetCust.setString("ITEM_SUM_TIT","");
                        objRetCust.setString("ITEM_SUM_DESC","");
                        if(jsonParams.getString("TMPL_ITEM") != null && jsonParams.getString("TMPL_ITEM") != "" && jsonParams.getString("TMPL_ITEM").length() != 0) {
                        	stritem = jsonParams.getString("TMPL_ITEM");
                        	item_list = stritem.substring(stritem.indexOf("["), stritem.indexOf("]"));

                	        objRetCust.setString("ITEM_SUM_TIT",stritem.substring(stritem.lastIndexOf("e\":")+3, stritem.lastIndexOf(",")));
                            if(stritem.substring(stritem.lastIndexOf(":")+1, stritem.lastIndexOf("}")-1).contains("&#35;&#123;")) {
                            	objRetCust.setString("ITEM_SUM_DESC", "\""+jsonObj.getString("SUMIT")+"\"");
                            } else {
                            	objRetCust.setString("ITEM_SUM_DESC",stritem.substring(stritem.lastIndexOf(":")+1, stritem.lastIndexOf("}")-1));
                            }
                        }
                        for(int i=0;i<10;i++){
                            if(item_list.indexOf(":") == -1){
                              objRetCust.setInt("ITEM_NUM",i);
                              switch(i) {
                              	case 0:
                              		for(int n=0;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 1:
                              		for(int n=1;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 2:
                              		for(int n=2;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 3:
                              		for(int n=3;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 4:
                              		for(int n=4;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 5:
                              		for(int n=5;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 6:
                              		for(int n=6;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 7:
                              		for(int n=7;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 8:
                              		for(int n=8;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              	case 9:
                              		for(int n=9;n<10;n++) {
                                  		objRetCust.setString("ITEM_LIST_TIT_"+n,"");
                                  		objRetCust.setString("ITEM_LIST_DESC_"+n,"");
                              		}
                              		break;
                              }
                              break;
                            }
                            objRetCust.setString("ITEM_LIST_TIT_"+i,item_list.substring(item_list.indexOf(":")+1, item_list.indexOf(",")));
                            item_list = item_list.replaceFirst(":","_");
                            item_list = item_list.replaceFirst(",","_");
                            if(item_list.substring(item_list.indexOf(":")+1, item_list.indexOf("}")).contains("&#35;&#123;")) {
                            	objRetCust.setString("ITEM_LIST_DESC_"+i, "\""+jsonObj.getString("ITEM"+(i+1))+"\"");
                            } else {
                            	objRetCust.setString("ITEM_LIST_DESC_"+i, item_list.substring(item_list.indexOf(":")+1, item_list.indexOf("}")));
                            }
                            item_list = item_list.replaceFirst(":","_");
                            item_list = item_list.replaceFirst(",","_");
                            item_list = item_list.replaceFirst("}","_");
                            
                        }
                        
                        String butn = "";
                        String[] butn_list = new String[5];
                        String[] butn_list_list = new String[8];
                        butn_list[0] = "";
                        butn_list[1] = "";
                        butn_list[2] = "";
                        butn_list[3] = "";
                        butn_list[4] = "";
                        butn_list_list[0] = "";
                        butn_list_list[1] = "";
                        butn_list_list[2] = "";
                        butn_list_list[3] = "";
                        butn_list_list[4] = "";
                        butn_list_list[5] = "";
                        butn_list_list[6] = "";
                        butn_list_list[7] = "";
                        if(jsonParams.getString("TRAN_BUTTON") != null && jsonParams.getString("TRAN_BUTTON") != "" && jsonParams.getString("TRAN_BUTTON").length() != 0) {
                        	butn = jsonParams.getString("TRAN_BUTTON");

                        	if(butn.contains("&#125;&#123;")) {
                        		for(int n=0;n<butn.split("&#125;&#123;").length;n++) {
                        			butn_list[n]=butn.split("&#125;&#123;")[n];
                        		}
                        	} else {
                        		butn_list[0] = butn;
                        	}
                        }
                        for(int i=0;i<5;i++){
                        	if(butn_list[i] == "" || butn_list[i] == null) {
                        		objRetCust.setString("BUTN_NM_"+i,"");
                                objRetCust.setString("BUTN_TY_"+i,"");
                                objRetCust.setString("BUTN_ADR_"+i,"");
                                objRetCust.setString("BUTN_IOS_"+i,"");
                                objRetCust.setString("BUTN_MO_"+i,"");
                                objRetCust.setString("BUTN_PC_"+i,"");
                                objRetCust.setString("BUTN_EXT_"+i,"");
                                objRetCust.setString("BUTN_EVT_"+i,"");
                        	} else {
                        		objRetCust.setString("BUTN_NM_"+i,"");
                                objRetCust.setString("BUTN_TY_"+i,"");
                                objRetCust.setString("BUTN_ADR_"+i,"");
                                objRetCust.setString("BUTN_IOS_"+i,"");
                                objRetCust.setString("BUTN_MO_"+i,"");
                                objRetCust.setString("BUTN_PC_"+i,"");
                                objRetCust.setString("BUTN_EXT_"+i,"");
                                objRetCust.setString("BUTN_EVT_"+i,"");
                        		for(int x=0;x<butn_list[i].split("\",\"").length;x++) {
                        			butn_list_list[x]=butn_list[i].split("\",\"")[x];
                        		}
                        		for(int j=0;j<8;j++) {
                        			int listlen = butn_list_list[j].length();
                        			if(butn_list_list[j].contains("name\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				}else {
                        					objRetCust.setString("BUTN_NM_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("type\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_TY_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("scheme_android\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_ADR_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("scheme_ios\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_IOS_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("url_mobile\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_MO_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("url_pc\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_PC_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("chat_extra\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_EXT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			} else if(butn_list_list[j].contains("chat_event\"")) {
                        				if(butn_list_list[j].charAt(listlen-1) == '\"') {
                        					objRetCust.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2));
                        				} else if(butn_list_list[j].charAt(listlen-1) == '}'){
                        					objRetCust.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2,listlen-1));
                        				} else {
                        					objRetCust.setString("BUTN_EVT_"+i,butn_list_list[j].substring(butn_list_list[j].indexOf("\":\"")+2)+"\"");
                        				}
                        			}
                        		}
                        	}
                        }
                        
                        String strhlit = "";
                        objRetCust.setString("HLIT_TIT","");
                        objRetCust.setString("HLIT_DESC","");
                        objRetCust.setString("HLIT_IMG","");
                        if(jsonParams.getString("TMPL_HILIT") != null && jsonParams.getString("TMPL_HILIT") != "" && jsonParams.getString("TMPL_HILIT") != "{}" && jsonParams.getString("TMPL_HILIT").length() != 0) {
                        	strhlit = jsonParams.getString("TMPL_HILIT");

                        	if(strhlit.contains("title")) {
                		        objRetCust.setString("HLIT_TIT",strhlit.substring(strhlit.indexOf("e\":")+3, strhlit.indexOf(",")));
                		        if(strhlit.contains("imageUrl")) {
                		        	if(strhlit.substring(strhlit.indexOf("n\":")+3, strhlit.lastIndexOf(",")).contains("&#35;&#123;")){
                		        		objRetCust.setString("HLIT_DESC", jsonObj.getString("SUMIT"));
                		        	} else {
                		        		objRetCust.setString("HLIT_DESC",strhlit.substring(strhlit.indexOf("n\":")+3, strhlit.lastIndexOf(",")));
                		        	}
                		        	objRetCust.setString("HLIT_IMG",strhlit.substring(strhlit.lastIndexOf(":")+1, strhlit.lastIndexOf("}")).replaceFirst("&#58;",":"));
                		        } else {
                		        	if(strhlit.substring(strhlit.lastIndexOf(":")+1, strhlit.lastIndexOf("}")).contains("&#35;&#123;")){
                		        		objRetCust.setString("HLIT_DESC", "\""+jsonObj.getString("SUMIT")+"\"");
                		        	} else {
                		        		objRetCust.setString("HLIT_DESC",strhlit.substring(strhlit.lastIndexOf(":")+1, strhlit.lastIndexOf("}")));
                		        	}
                		        }
                        	}
                        }
                        String con = jsonParams.getString("TRAN_MSG");
                        String word = "";
                        
                        log.debug("con11" + con);
                        for(int n=1;n<11;n++) {
                        	if(con.contains("&#35;&#123;")) {
                        		word = con.substring(con.indexOf("&#35;&#123;"),con.indexOf("}")+1);
                                con = con.replaceFirst(word,jsonObj.getString("CON"+n));
                        	}
                        }
                        log.debug("con22" + con);
                        
                        
                        objRetCust.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));   // 회사구분
                        objRetCust.setString("TRAN_SENDER_KEY", jsonParams.getString("TRAN_SENDER_KEY"));   // 발신프로필키
                        objRetCust.setString("TRAN_TMPL_CD", jsonParams.getString("TRAN_TMPL_CD"));         // 템플릿코드
                        //objRetCust.setString("TRAN_BUTTON", btn);             								// 버튼
                        objRetCust.setString("TRAN_PHONE", jsonObj.getString("MOBIL_NO"));             		// 수신번호
                        //objRetCust.setString("TRAN_MSG", jsonParams.getString("TRAN_MSG"));           		// 발신메시지
                        objRetCust.setString("TRAN_MSG", con);           		// 발신메시지
                        objRetCust.setString("TRAN_DATE", jsonParams.getString("TRAN_DATE"));             	// 발신 날짜
                        objRetCust.setString("TRAN_TYPE", jsonParams.getString("TRAN_TYPE"));           	// 발신종류
                        objRetCust.setString("TRAN_REPLACE_MSG", jsonParams.getString("TRAN_REPLACE_MSG")); // 대체문구
                        objRetCust.setString("TRAN_CALLBACK", jsonParams.getString("TRAN_CALLBACK"));       // 발신번호
                        objRetCust.setString("TRAN_TITLE", hilit);             								// 강조문구
                        objRetCust.setString("TRAN_SUBJECT", jsonParams.getString("TRAN_SUBJECT"));         // 발신제목
                        objRetCust.setString("RCV_NOW", jsonParams.getString("RCV_NOW"));             		// 즉시발송여부
                        objRetCust.setString("TRAN_HEADER", jsonParams.getString("TRAN_HEADER"));             	// 아이템헤더
                        objRetCust.setString("TRAN_ATTACHMENT", item);             							// 아이템
                        objRetCust.setString("USER_ID", jsonParams.getString("USER_ID"));             		// 발신인
                        
                        log.debug("objRetCust"+objRetCust);
                        
                        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "alrimMultiSend", objRetCust);
                    }
                }

            }
        }

        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 알림톡 템플릿 중복체크
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON cntAlrimTmpl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "cntAlrimTmpl", jsonParams);

        return objRetParams;
    }

    /**
     * 알림톡 템플릿 카카오 메인 이미지 URL
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON alrimMainImgUrl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "alrimMainImgUrl", jsonParams);

        return objRetParams;
    }

    /**
     * 알림톡 템플릿 카카오 하이라이트 이미지 URL
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON alrimHlitImgUrl(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "alrimHlitImgUrl", jsonParams);

        return objRetParams;
    }

	@Override
	public TelewebJSON testest(TelewebJSON jsonParams) throws IOException {
		//  Auto-generated method stub
		return null;
	}/**
     * 알림톡 데이터 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON getAtalkSchema(TelewebJSON mjsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException
    {
        TelewebJSON custcoParams = new TelewebJSON(mjsonParams);
        //ASP_CUST_KEY로 고객사 정보(SCHEMA_ID) 가져오기
        custcoParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.NotificationTalkMapper", "getSchemaId", mjsonParams);

        return custcoParams;
    }
}
