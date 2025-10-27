package kr.co.hkcloud.palette3.common.palette.app;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import static kr.co.hkcloud.palette3.constant.DatabaseConstants.*;


/**
 * 팔레트공통 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("paletteCmmnService")
public class PaletteCmmnServiceImpl implements PaletteCmmnService
{
	private final InnbCreatCmmnService innbCreatCmmnService;
	private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final PaletteProperties paletteProperties;
    private final TwbComDAO         twbComDAO;
    private final SseService sseService;


    /**
     * 특정 사용자소속 ComboBox 추가
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAttrDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnAttrDiv", jsonParams);
    }


    /**
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
//    @Cacheable(value="palette:cache:asp-sender-key"
//             , key="T(com.hcteletalk.teletalk.config.cache.generates.CacheAspSenderKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingAspSenderKey(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnAspSenderKey", jsonParams);
    }


    /**
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
//    @Cacheable(value="palette:cache:biz-services-cd"
//             , key="T(com.hcteletalk.teletalk.config.cache.generates.CacheBizServicesKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCachingBizServicesCd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnBizServicesCd", jsonParams);
    }


    /**
     * 상담통합이력 insert
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCnslUnityHst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "insertRtnCnslUnityHst", jsonParams);
    }


    /**
     * 나의상담이력(통합상담이력) 조회 Arthur.Kim 2021.10.13
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityList", jsonParams);
    }


    /**
     * 상담통합이력 조회
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHst", jsonParams);
    }


    /**
     * 상담통합이력 상세
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstDtl(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstDtl", jsonParams);
    }


    /**
     * 상담통합이력 상세 - 전화
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstPhone(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstPhone", jsonParams);

        if("Y".equals(objRetParams.getString("RDWT_SEND_YN"))) {

            String strRdwtFile = "";
            String strRdwtFileNm = objRetParams.getString("RDWT_FILE_NM");
            String strSaveDate = strRdwtFileNm.split("-")[3];
            String strRecordingPath = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar c1 = Calendar.getInstance();
            String strToday = sdf.format(c1.getTime());

            if(strToday.equals(strSaveDate)) {
                strRecordingPath = paletteProperties.getCtiServer().getRecordingPath();
            }
            else {
                strRecordingPath = paletteProperties.getCtiServer().getRecordingBackupPath();
            }

            strRdwtFile = paletteProperties.getCtiServer().getUrl() + ":" + paletteProperties.getCtiServer().getRecordingPort() + strRecordingPath + objRetParams.getString("RDWT_FILE_PATH") + "/" + objRetParams
                .getString("RDWT_FILE_NM");
            //http://121.67.187.236:60080/monitor-wav-backup/2021/05/24/out-01030148066-1001-20210524-174325-1621845805.63.wav

            objRetParams.setString("RDWT_FILE", strRdwtFile);
        }

        return objRetParams;
    }


    /**
     * 상담통합이력 상세 - 채팅
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstChat(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstChat", jsonParams);
    }


    /**
     * 상담통합이력 상세 - 메일
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstMail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstMail", jsonParams);
    }


    /**
     * 상담통합이력 상세 - sms
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstSms(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstSms", jsonParams);
    }


    /**
     * 상담통합이력 상세 - 알림톡
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslUnityHstNtcnTalk(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "selectRtnCnslUnityHstNtcnTalk", jsonParams);
    }


    /**
     * 최상위 공통코드 조회
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getCmmCode1(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "getCmmCode1", jsonParams);
    }


    /**
     * 최상위 공통코드 조회
     * 
     * @param   jsonParams
     * @return
     * @version            5.0
     * @See
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getCompanyNM(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "getCompanyNM", jsonParams);
    }


    /**
     * 상담 통합 이력 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttItgrtHistReg(TelewebJSON jsonParams) throws TelewebAppException
    {
    	//상담 통합 이력 ID
    	int ITGRT_HSTRY_ID = innbCreatCmmnService.createSeqNo("ITGRT_HSTRY_ID");
    	jsonParams.setInt("ITGRT_HSTRY_ID", ITGRT_HSTRY_ID);
    	
    	TelewebJSON objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "cuttItgrtHistReg", jsonParams);
    	
    	//이관 처리
    	if(!StringUtils.isEmpty(jsonParams.getString("TRNSF_EXPSN_ATTR"))) this.cuttTrnsfReg(jsonParams);
    		
    	return objRetParams;
    }
    
    /**
     * 상담 이관 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttTrnsfReg(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON();
    	
    	String TRNSF_ESPSN_ATTR = jsonParams.getString("TRNSF_EXPSN_ATTR");
		TRNSF_ESPSN_ATTR = TRNSF_ESPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        JSONArray arryExpsnAttr = JSONArray.fromObject(TRNSF_ESPSN_ATTR);
        
        if (arryExpsnAttr.size() > 0) {
        	jsonParams.setString("DMND_CUSL_ID", jsonParams.getString("USER_ID"));
        	jsonParams.setString("STTS_CD", "REQ"); //대기
        	
        	if ("REG".equals(jsonParams.getString("STAT"))) {//등록인 경우
                TelewebJSON trnsfTrgtOptInfo = getTrnsfTargetOptCd(jsonParams);
                String targetOptCd = trnsfTrgtOptInfo.getHeaderInt("TOT_COUNT") > 0 ? trnsfTrgtOptInfo.getString("TARGET_OPT_CD") : "";

	        	int CUTT_TRNSF_ID = innbCreatCmmnService.createSeqNo("CUTT_TRNSF_ID");

	        	jsonParams.setInt("CUTT_TRNSF_ID", CUTT_TRNSF_ID);
	        	jsonParams.setString("TARGET_OPT_CD", targetOptCd); // 이관 담당자 옵션 데이터 추가
	        	//상담 이관 저장 처리
	        	objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfReg", jsonParams);

                // 이관 담당자 옵션이 'USER'인 경우 배분 이력 테이블에 데이터 insert
                if("USER".equals(targetOptCd)) {
                    TelewebJSON historyParams = new TelewebJSON();

                    historyParams.setInt("CUTT_TRNSF_ID", CUTT_TRNSF_ID);
                    historyParams.setString("CUSL_ID", jsonParams.getString("TRGT_USER_ID"));
                    historyParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                    addTransferHistory(historyParams, "ASSIGNED");
                }
        	}
        	
            for (Object expsnAttr : arryExpsnAttr) {
                TelewebJSON expsnAttrParams = new TelewebJSON();

                expsnAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                expsnAttrParams.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
                expsnAttrParams.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
                expsnAttrParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
                expsnAttrParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
                expsnAttrParams.setString("EXPSN_ATTR_COL_ID", (String) ((JSONObject) expsnAttr).get("EXPSN_ATTR_COL_ID"));
                expsnAttrParams.setString("ATTR_ID", (String) ((JSONObject) expsnAttr).get("ATTR_ID"));
                expsnAttrParams.setString("ATTR_VL", (String) ((JSONObject) expsnAttr).get("V_POST_PARAM"));
                expsnAttrParams.setString("INDI_INFO_ENCPT_YN", (String) ((JSONObject) expsnAttr).get("INDI_INFO_ENCPT_YN"));
                expsnAttrParams.setString("RSVT_PHN_CUTT_ID", jsonParams.getString("RSVT_PHN_CUTT_ID"));
                expsnAttrParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                expsnAttrParams.setString("CLBK_ID", jsonParams.getString("CLBK_ID"));
                expsnAttrParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
                expsnAttrParams.setString("RSVT_ID", jsonParams.getString("RSVT_ID"));
                expsnAttrParams.setString("ITGRT_HSTRY_ID", jsonParams.getString("ITGRT_HSTRY_ID")); //통합 접촉 이력 ID
                expsnAttrParams.setString("CUTT_TRNSF_ID", jsonParams.getString("CUTT_TRNSF_ID")); //상담 이관 ID
                expsnAttrParams.setString("STTS_CD", jsonParams.getString("STTS_CD"));

                if ("REG".equals(jsonParams.getString("STAT"))) { //등록 인 경우에만 확장 속성에 저장
                	//상담 이관 상세 확장 정보 저장
                    objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfExpsnAttrReg", expsnAttrParams);
                }else {
                	//상담 이관 상세 확장 정보 수정
                    objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfExpsnAttrMerge", expsnAttrParams);
                }
            }
            
            //상담 이관 이력 저장 처리
        	objRetParams = this.cuttTrnsfHstryReg(jsonParams);
        }
        
        return objRetParams;
    }

    private void addTransferHistory(@NonNull final TelewebJSON transfer, @NonNull final String type) {
        final TelewebJSON insertHistoryParams = new TelewebJSON();
        insertHistoryParams.setInt(TRANSFER_ALLOCATION_SEQ_KEY, innbCreatCmmnService.createSeqNo(TRANSFER_ALLOCATION_SEQ_KEY));
        insertHistoryParams.setString(CONSULTATION_TRANSFER_ID_KEY, transfer.getString(CONSULTATION_TRANSFER_ID_KEY));
        insertHistoryParams.setString(CONSULTANT_ID_KEY, transfer.getString(CONSULTANT_ID_KEY));
        insertHistoryParams.setString(TYPE_KEY, type);
        insertHistoryParams.setString(USER_ID_KEY, transfer.getString(USER_ID_KEY));

        twbComDAO.insert(TRANSFER_ALLOCATION_MAPPER_NAME, "insertTransferHistory", insertHistoryParams);
    }
    
    /**
     * 상담 이관 상태 변경
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttTrnsfHstryReg(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON();
    	
    	if(!StringUtils.isEmpty(jsonParams.getString("CUTT_TRNSF_ID_LIST"))) {
        	
    		String CUTT_TRNSF_ID_LIST = jsonParams.getString("CUTT_TRNSF_ID_LIST");
    		CUTT_TRNSF_ID_LIST = CUTT_TRNSF_ID_LIST.replace("&#91;", "[").replace("&#93;", "]");
    		JSONArray arryCuttTrnsfIdList = JSONArray.fromObject(CUTT_TRNSF_ID_LIST);
             
			if (arryCuttTrnsfIdList.size() > 0) {
				List<String> cuslIdList = new ArrayList<>();
				for (Object cuttTrnsf : arryCuttTrnsfIdList) {
					TelewebJSON cuttTrnsfParams = new TelewebJSON();

                  	cuttTrnsfParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                  	cuttTrnsfParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                  	cuttTrnsfParams.setInt("CUTT_TRNSF_HSTRY_ID", innbCreatCmmnService.createSeqNo("CUTT_TRNSF_HSTRY_ID"));
                  	cuttTrnsfParams.setString("CUTT_TRNSF_ID", (String) ((JSONObject) cuttTrnsf).get("CUTT_TRNSF_ID"));
                  	cuttTrnsfParams.setString("STTS_CD", jsonParams.getString("STTS_CD"));
                  	cuttTrnsfParams.setString("RSN", jsonParams.getString("RSN"));
                  	
                  	//상담 이관 이력 저장 처리
                	objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfHstryReg", cuttTrnsfParams);
                  	
                	//상담 이관 상태 변경
                	objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfSttsCdUpdate", cuttTrnsfParams);
                	
                	if (!cuslIdList.contains((String) ((JSONObject) cuttTrnsf).get("CUSL_ID"))) {
                		cuslIdList.add((String) ((JSONObject) cuttTrnsf).get("CUSL_ID"));
                    }
				}
				
				if(cuslIdList.size() > 0) {
					if("RTN".equals(jsonParams.getString("STTS_CD"))) { //반려인 경우
						for(Integer i=0; i<cuslIdList.size(); i++) {
							System.out.println("cuslIdList.get("+i+") : " + cuslIdList.get(i));
							
							JSONObject messageJson = new JSONObject();
					        messageJson.put("ALAM_MESSAGE", "반려된 이관 내역이 존재 합니다.");
	
					        log.debug("=================messageJson ::: " + messageJson);
					        //인증 기관사 변경시 상담사에게 인증사 정보 PUSH
					        SseMessage sseMessage = new SseMessage();
					        sseMessage.setType(SseMessage.MessageType.SYSTEM_MESSAGE);
					        sseMessage.setSender("2");
					        sseMessage.setReceiver(cuslIdList.get(i));    // ALL은 전체 , userId 개인별
					        sseMessage.setRoomId(TenantContext.getCurrentTenant() +"_"+ TenantContext.getCurrentCustco());
					        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
					        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
					        sseMessage.setMessage(messageJson.toString());    //메시지
					        try {
					            sseService.sendMessage(sseMessage);
					            Thread.sleep(100);
					        } catch (Exception e) {
					            log.error("sseMessage is exception : " + e.getMessage());
					        }
						}
					}
				}
             }
    	}else {
    		int CUTT_TRNSF_HSTRY_ID = innbCreatCmmnService.createSeqNo("CUTT_TRNSF_HSTRY_ID");
        	jsonParams.setInt("CUTT_TRNSF_HSTRY_ID", CUTT_TRNSF_HSTRY_ID);
        	
        	//상담 이관 이력 저장 처리
        	objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfHstryReg", jsonParams);
        	
        	//상담 이관 상태 변경
        	objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfSttsCdUpdate", jsonParams);
        	
        	if("RTN".equals(jsonParams.getString("STTS_CD"))) { //반려인 경우
				JSONObject messageJson = new JSONObject();
		        messageJson.put("ALAM_MESSAGE", "반려된 이관 내역이 존재 합니다.");

		        log.debug("=================messageJson ::: " + messageJson);
		        //인증 기관사 변경시 상담사에게 인증사 정보 PUSH
		        SseMessage sseMessage = new SseMessage();
		        sseMessage.setType(SseMessage.MessageType.SYSTEM_MESSAGE);
		        sseMessage.setSender("2");
		        sseMessage.setReceiver(jsonParams.getString("CUSL_ID"));    // ALL은 전체 , userId 개인별
		        sseMessage.setRoomId(TenantContext.getCurrentTenant() +"_"+ TenantContext.getCurrentCustco());
		        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
		        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
		        sseMessage.setMessage(messageJson.toString());    //메시지
		        try {
		            sseService.sendMessage(sseMessage);
		            Thread.sleep(100);
		        } catch (Exception e) {
		            log.error("sseMessage is exception : " + e.getMessage());
		        }
			}
    	}
    	
    	return objRetParams;
    }

    /**
     * 전화 콜 이력 저장 처리
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON phnCallHistReg(TelewebJSON jsonParams) throws TelewebAppException
    {
    	//발신 이력 ID
    	int DSPTCH_HSTRY_ID = innbCreatCmmnService.createSeqNo("DSPTCH_HSTRY_ID");
    	jsonParams.setInt("DSPTCH_HSTRY_ID", DSPTCH_HSTRY_ID);
    	
    	return twbComDAO.insert("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "phnCallHistReg", jsonParams);
    }

    /**
     * 발신 프로필 키에 따른 테넌시 검색
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON getCertCustcoId(TelewebJSON jsonParams) throws TelewebAppException
    {
    	return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "getCertCustcoId", jsonParams);
    }
    
    /**
     * 이관 처리현황 이관 및 고객 확장속성 정보 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON cuttTrnsfHistGetExpsnAttr(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttTrnsfHistGetExpsnAttr", jsonParams);
    }
    
    /**
     * 상담 이관 이력 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuttTrnsfHstryList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        
        int iRowCnt = jsonParams.getHeaderInt("ROW_CNT");
        int iPagesCnt = jsonParams.getHeaderInt("PAGES_CNT");
        jsonParams.setHeader("ROW_CNT", 0);
        jsonParams.setHeader("PAGES_CNT", 0);

        jsonParams.setString("SE", "TRNSF");
        TelewebJSON trnsfExpsnAttr = settingCustomerInformationListService.custcoExpsnInfo(jsonParams); //이관 확장 속성 조회

        JSONArray jsonObj = new JSONArray();
        if (trnsfExpsnAttr.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
            jsonObj = trnsfExpsnAttr.getDataObject(TwbCmmnConst.G_DATA);
        }

        if (jsonObj.size() > 0) {
            jsonParams.setString("EXPSN_ATTR_LIST", jsonObj.toString());

            //고객 확장 속성 검색
            String SCH_CUST_EXPSN_ATTR = jsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
            SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
            jsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

            //이관 확장 속성 검색
            String SCH_TRNSF_EXPSN_ATTR = jsonParams.getString("SCH_TRNSF_EXPSN_ATTR").toString();
            SCH_TRNSF_EXPSN_ATTR = SCH_TRNSF_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
            jsonParams.setString("SCH_TRNSF_EXPSN_ATTR", SCH_TRNSF_EXPSN_ATTR);

            jsonParams.setHeader("ROW_CNT", iRowCnt);
            jsonParams.setHeader("PAGES_CNT", iPagesCnt);

            objRetParams = twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "cuttTrnsfHstryList", jsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "확장항목 정보 없음");
        }
        objRetParams.setDataObject("EXPSN_ATTR", trnsfExpsnAttr);
        return objRetParams;
    }

    /**
     * 해당 고객사의 이관 담당자 옵션 코드 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getTrnsfTargetOptCd(TelewebJSON jsonParams) throws TelewebAppException {
        jsonParams.setString("SCHEMA_ID", TenantContext.getCurrentTenant());
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "getTrnsfTargetOptCd", jsonParams);
    }

    /**
     * (설문)부서 리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON getDeptList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.palette.dao.PaletteCmmnMapper", "getDeptList", jsonParams);
    }
    
}
