package kr.co.hkcloud.palette3.core.cron.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import jdk.internal.org.jline.utils.Log;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyDAO;
import kr.co.hkcloud.palette3.core.chat.busy.util.TalkBusyUtils;
import kr.co.hkcloud.palette3.core.chat.messenger.domain.ChatOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.RuleFilenameAndExtentResponse;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import kr.co.hkcloud.palette3.integration.service.CommerceApiServiceFactory;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
@Slf4j
@RequiredArgsConstructor
@Service("cronChatBatchService")
public class CronChatBatchServiceImpl implements CronChatBatchService {

    private final CommerceApiServiceFactory commerceApiServiceFactory;
    private final FileRuleUtils    fileRuleUtils;
    private final FileDbMngService fileDbMngService;
    private final InnbCreatCmmnService innbCreatCmmnService;
    
    private final TalkBusyUtils             talkBusyUtils;
    private final TalkBusyDAO               busyDAO;
    
    private final TwbComDAO mobjDao;
    
    private final ApplicationEventPublisher eventPublisher;
    
    private final SettingCustomerInformationListService custInfoService;
    
    private final TalkDataProcessService talkDataProcess;
    
    private String namespace = "kr.co.hkcloud.palette3.core.cron.dao.CronChatBatchMapper";
    
    
    @Autowired
    private HcTeletalkEnvironment env;
    
    @Value("${stomp.allow.origin}")
    public String SERVER_DOMAIN;
    
    private String logDevider = "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-";
    
    /**
     * 채팅_상담_이메일 - 마지막 수신 일시 조회
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON getSwRsvt(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON retObj = new TelewebJSON(jsonParams);
        try {
            int settingCount = jsonParams.getHeaderInt("TOT_COUNT");
            if (settingCount > 0) {
                int insertCnt = 0;
                int modifyCnt = 0;
                JSONObject settingObj = (JSONObject) jsonParams.getDataObject().get(0);
                String custcoId = settingObj.getString("CUSTCO_ID");
                log.info("+=+=+=+=+=+=+=+=+=+=+=+=___BATCH " + jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD")
                    + " 수집 ___ ::: Start ___CUSTCO_ID___" + custcoId);

                jsonParams.setString(settingObj.getString("FROM_DATE_COL_NM"), jsonParams.getString("LAST_SRCH_DT"));
                jsonParams.setString(settingObj.getString("TO_DATE_COL_NM"), jsonParams.getString("currentDateTime"));

                TelewebJSON retJson = commerceApiServiceFactory.getCommerceApiService(jsonParams.getString("BEAN_ID")).call_batch_api(jsonParams);
                //JSONObject pageable = (JSONObject)retJson.getDataObject("pageable").get(0);
                JSONArray retArray = retJson.getDataObject(TwbCmmnConst.G_DATA);
                if (retArray.size() > 0) {

                    for (int j = 0; j < retArray.size(); j++) {
                        TelewebJSON returnJson = new TelewebJSON();
                        TelewebJSON paramJson = new TelewebJSON();
                        JSONArray dataArray = new JSONArray();
                        dataArray.add(retArray.getJSONObject(j));
                        paramJson.setDataObject(dataArray);
                        
                        paramJson.setString("CUSTCO_ID",custcoId);
                        
                        log.info("getSwRsvt paramJson >> " + paramJson);
                        //신규 예약인지 조회
                        TelewebJSON swNewRsvt = new TelewebJSON();
                        swNewRsvt = mobjDao.select(namespace, "selectNewSwRsvt", paramJson);
                        
                        if(swNewRsvt.getHeaderString("COUNT").equals("0")) {
                        	//신규예약
                        	paramJson.setString("CHT_USER_KEY", paramJson.getString("MEMBER_ID"));
	                        //sw상담예약 저장
	                        //고객정보 있는지 판단 후 저장
	                        TelewebJSON objRetParams = new TelewebJSON(paramJson);
	                        //채팅 고객 ID 가져오기
	                        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectSwCustCount", paramJson);
	                        //SW고객정보가 없을 때
	                        if(objRetParams.getString("CNT").equals("0")) {
	                        	//채팅고객 저장을 위해서는 sndr_key가 있어야함
	                        	//배치로 수집해오기 때문에 sndr_key가 없음
	                        	//sndr_key는 0으로 고정
	                        	//고객정보 INSERT
	                        	CustomerVO customerVO = new CustomerVO();
	                	        customerVO.setCustomerId(paramJson.getString("MEMBER_ID"));
	                	        customerVO.setTalkUserKey(paramJson.getString("MEMBER_ID"));
	                	        customerVO.setCustcoId(custcoId);
	                	        customerVO.setSndrKey("0");
	                	        customerVO.setChnClsfCd("TTALK");
	                	        custInfoService.mergeCustomerBaseInfo(customerVO);
	                        }
	                        
	                        //채팅 고객 ID 재조회
	                        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectSwCust", paramJson);
	                		log.info("objRetParams" + objRetParams);
	                        paramJson.setString("CUST_ID", objRetParams.getString("CUST_ID"));
	                		
	                        //예약고객이 이미 전화번호를 가지고 있을 때
	                        if(objRetParams.getString("CUST_PHN_NO").equals(paramJson.getString("CUST_TEL_NO"))) {
                        		//고객명 예약고객 기준으로 업데이트
                                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtCustNm", paramJson);
	                        } else if(objRetParams.getString("CUST_PHN_NO").equals("")) {
	                        	//예약고객이 전화번호가 저장되어있지 않을 때
	                        	//해당 전화번호를 가지고 있는 팔레트 고객 조회
	                        	paramJson.setString("BF_CUST_ID", objRetParams.getString("CUST_ID"));
	                            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRsvtPaletteCust", paramJson);
	                            JSONArray custArr = objRetParams.getDataObject();
	                            //전화번호를 가지고 있는 팔레트 고객이 있다면
	                            if(custArr.size()>0) {
	                        		log.info("고객 통합 로직 실행_extRetParams >> " + paramJson);
	                        		paramJson.setString("AF_CUST_ID", objRetParams.getString("CUST_ID"));
	                        		paramJson.setString("CUST_ID", objRetParams.getString("CUST_ID"));
	                        		TelewebJSON mergeCustObj = new TelewebJSON();
	                        		mergeCustObj = custInfoService.mergeCust(paramJson);
	                        		
	                        		//고객명 예약고객 기준으로 업데이트
	                                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtCustNm", paramJson);
	                            } else {
	                            	//해당 전화번호를 가진 고객이 없다면 고객 전화번호,고객명 업데이트
	                                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtCust", paramJson);
	                            }
	                        }
	                        //상담이 이미 들어와있는지 판단 후 저장
	                        //상담예약 저장
	                        paramJson.setString("RSVT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("RSVT_ID")));
	                        mobjDao.insert(namespace, "getSwRsvt", paramJson);
	                        //예약 배분 상담원도 SW에서 받아온 상담원 기준으로 저장
	                        mobjDao.insert(namespace, "getSwRsvtAltmnt", paramJson);
	                        //상담예약 배정정보 업데이트
	                        mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtAltmntInfo", paramJson);
	                        
//	                        //배분받을 수 있는 상담원 있는지 체크
//	                        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRsvtAltmnt", paramJson);
//	                        //상담예약 배분
//	                        if(objRetParams.getHeaderString("COUNT").equals("0")) {
//	                        	//배분 받을 수 있는 상담원이 없을 때
//	                        	//아무동작 하지 않음
//	                        } else {
//	                        	objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRsvtAltmnt", paramJson);
//		                        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "updateRsvtAltmntInfo", paramJson);
//		                    
//		                        //배정 상담사를 sw쪽에도 insert
//		                        TelewebJSON reqParams = new TelewebJSON();
//		                        //배정된 상담원 정보 가져오기
//		                        reqParams =  mobjDao.select(namespace, "selectAltmntCusl", paramJson);
//		                        
//		                        String apiUrl = "updateAltmnt?BOOKING_ID="+ paramJson.getString("BOOKING_ID") + "&ADMIN_LGN_ID=" + reqParams.getString("CUSTCO_CUSL_ID");
//		                        talkDataProcess.postExtApi(apiUrl);
//	                        }
                        } else {
                        	//이미 있는 예약이라면 예약시간 수정여부 파악
                        	if(swNewRsvt.getString("RSVT_DT").equals(paramJson.getString("RSVT_DT")+"00")) {
                        		//예약시간 변경 없을 시
                        		//아무동작 하지 않음
                        	} else {
                        		//상담예약 시간 변경
                        		mobjDao.update(namespace, "updateSwRsvt", paramJson);
//                        		//상담예약 재배정 먼저 해야함
//                        		//시간을 먼저 변경하면 재배정 시 재배정 상담원 조회할 때 오류 발생
//                        		//상담예약시간 변경
//                        		//배분받을 수 있는 상담원 있는지 체크
//    	                        TelewebJSON objRsvtAltmntParams = new TelewebJSON(paramJson);
//    	                        objRsvtAltmntParams = mobjDao.select("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "selectRsvtAltmnt", paramJson);
//    	                        //상담예약 배분
//    	                        if(objRsvtAltmntParams.getHeaderString("COUNT").equals("0")) {
//    	                        	//배분 받을 수 있는 상담원이 없을 때
//    	                        	//아무동작 하지 않음
//    	                        } else {
//                                    //상담예약 재배정
//                                    mobjDao.update(namespace, "updateSwRsvtAltmnt", paramJson);
//	                                mobjDao.update(namespace, "updateSwRsvt", paramJson);
//	                                //재배정 상담원 sw쪽에 update
//	    	                        TelewebJSON reqParams = new TelewebJSON();
//	    	                        //배정된 상담원 정보 가져오기
//	    	                        reqParams =  mobjDao.select(namespace, "selectAltmntCusl", paramJson);
//	    	                        log.info("reqParams2" + reqParams);
//	    	                        log.info("reqParams2.getString(\"CUSTCO_CUSL_ID\")" + reqParams.getString("CUSTCO_CUSL_ID"));
//	    	                        
//	    	                        String apiUrl = "updateAltmnt?BOOKING_ID="+ paramJson.getString("BOOKING_ID") + "&ADMIN_LGN_ID=" + reqParams.getString("CUSTCO_CUSL_ID");
//	    	                        talkDataProcess.postExtApi(apiUrl);
//    	                        }
                        	}
                        	
                        	//예약상태 변경 시 예약테이블에 예약상태 update해줘야함
                        	if(swNewRsvt.getString("RSVT_STTS_CD").equals(paramJson.getString("BOOKING_STATUS"))) {
                        		//예약상태 변경 없을 시
                        		//아무동작 하지 않음
                        	} else {
                        		//예약상태 변경
                                mobjDao.update(namespace, "updateSwRsvtStts", paramJson);
                        	}
                        	
                        	//배정 상담원 변경 체크
                        	if(swNewRsvt.getString("CUSTCO_CUSL_ID").equals(paramJson.getString("BOOK_USER_ID"))) {
                        		//배정상담원 변경 없을 시
                        		//아무동작 하지 않음
                        	} else {
                        		//배정상담원 변경이력 업데이트
                                mobjDao.insert(namespace, "insertSwRsvtAltmntFromSw", paramJson);
                        		//배정 상담원 업데이트
                                mobjDao.update(namespace, "updateSwRsvtAltmntFromSw", paramJson);
                        	}
                        }
                    }

                    //                    JSONObject jsonObj = new JSONObject();
                    //                    jsonObj.put("ITEM_LIST", jsonParams.getDataObject(TwbCmmnConst.G_DATA));
                    //                    JSONArray arrParam = new JSONArray();
                    //                    arrParam.add(jsonObj);
                    //jsonParams.setDataObject("ITEM_LIST", retArray );
                }
            } else {
                retObj.setString("JOB_SCS_YN", "Y");
                retObj.setString("JOB_RSLT_MSG", jsonParams.getString("BEAN_ID") + "-" + jsonParams.getString("BBS_INQRY_TYPE_CD") + " 수집 완료 - 0 건");
                retObj.setHeader("ERROR_FLAG", false);
                retObj.setHeader("ERROR_MSG", "");
            }
        } catch (Exception e) {
            retObj.setHeader("ERROR_FLAG", true);
            retObj.setHeader("ERROR_MSG", e.getMessage());
            retObj.setString("JOB_SCS_YN", "N");
            retObj.setString("JOB_RSLT_MSG", e.toString());
        }

        return retObj;

    }
    
}
