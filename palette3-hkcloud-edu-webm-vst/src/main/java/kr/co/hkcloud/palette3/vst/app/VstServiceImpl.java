package kr.co.hkcloud.palette3.vst.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.fcm.app.FcmService;
import kr.co.hkcloud.palette3.fcm.message.FcmMessage;
import kr.co.hkcloud.palette3.message.app.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("vstService")
public class VstServiceImpl implements VstService {

	private final FcmService fcmService;
	private final MessageService messageService;
    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private String namespace = "kr.co.hkcloud.palette3.vst.dao.VstMapper";

    /**
     * 상품 유형 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON prdctTypeList(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.select(namespace, "prdctTypeList", jsonParams);
    }
    
    /**
     * 방문 일자별 시간 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON vstDayTimeList(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.select(namespace, "vstDayTimeList", jsonParams);
    }

    /**
     * 방문 일자 시간별 방문자 목록
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON vstDayTimeVstrList(TelewebJSON jsonParams) throws TelewebAppException {
    	TelewebJSON objRetParams = new TelewebJSON(jsonParams);

    	TelewebJSON rdyParam = new TelewebJSON();
		rdyParam.setString("STAT", "DEL");
		rdyParam.setString("CUST_ID", jsonParams.getString("CUST_ID"));
		rdyParam.setString("CUSL_ID", jsonParams.getString("USER_ID"));
		
		objRetParams = (TelewebJSON) this.vstVstrRdyProc(rdyParam);
		
		objRetParams = twbComDAO.select(namespace, "vstDayTimeVstrList", jsonParams);
    	
    	return objRetParams;
    }

    /**
     * 방문자 예약 상태 체크
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON vstVstrRdyStatChk(TelewebJSON jsonParams) throws TelewebAppException {
    	TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    	
    	TelewebJSON rdyParam = new TelewebJSON();
		rdyParam.setString("STAT", "DEL");
		rdyParam.setString("CUST_ID", jsonParams.getString("CUST_ID"));
		rdyParam.setString("CUSL_ID", jsonParams.getString("USER_ID"));
		
		objRetParams = (TelewebJSON) this.vstVstrRdyProc(rdyParam);
		
		objRetParams = twbComDAO.select(namespace, "vstVstrRdyStatChk", jsonParams);
		
		if(objRetParams.getDataObject(TwbCmmnConst.G_DATA).getJSONObject(0).getInt("CNT") > 0) {
			return objRetParams;
		}else {

			objRetParams = (TelewebJSON) this.vstVstrRdyProc(jsonParams);
			
			objRetParams.setString("PSBLTY_STAT", "Y");
			
			return objRetParams;
		}
    }

    /**
     * 방문자 대기 처리
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON vstVstrRdyProc(TelewebJSON jsonParams) throws TelewebAppException {
    	TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    	
    	if(StringUtils.isEmpty(jsonParams.getString("STAT"))) { //방문자 대기 등록
    		return twbComDAO.select(namespace, "vstVstrRdyReg", jsonParams);
    	}else { //방문자 대기 삭제
    		objRetParams = twbComDAO.select(namespace, "vstVstrRdyDel", jsonParams);
    		objRetParams.setString("PSBLTY_STAT", "Y");
    		
    		return objRetParams;
    	}
    	
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON vstRsvtList(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.select(namespace, "vstRsvtList", jsonParams);
    }
    
    /**
     * 방문 예약 저장, 수정
     * @throws ParseException 
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON vstRsvtProc(TelewebJSON jsonParams) throws TelewebAppException, ParseException {
    	TelewebJSON objRetParams = new TelewebJSON();
    	String sVstrId = jsonParams.getString("VSTR_ID");
    	
    	if(StringUtils.isEmpty(jsonParams.getString("VST_RSVT_ID"))) { //저장
	    	jsonParams.setString("VST_RSVT_ID", ""+innbCreatCmmnService.createSeqNo("VST_RSVT_ID"));
	    	
	        objRetParams = twbComDAO.insert(namespace, "vstRsvtReg", jsonParams);
	        
	        if(!StringUtils.isEmpty(jsonParams.getString("PHN_CUTT_ID"))) {
	        	TelewebJSON cuttCnParams = new TelewebJSON();
	        	cuttCnParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
	        	cuttCnParams.setString("VST_RSVT_ID", jsonParams.getString("VST_RSVT_ID"));
	        	
	        	objRetParams = this.vstRsvtCuttReg(cuttCnParams); //방문 상담 예약 저장
	        }
    	}else { //수정
    		TelewebJSON vstRsvtParams = jsonParams;
    		if("VRS_RTRCN".equals(vstRsvtParams.getString("STTS_CD"))) {
    			vstRsvtParams.setString("RSVT_DT", null); //예약 일시
    			vstRsvtParams.setString("VSTR_ID", null); //방문자 ID
    			vstRsvtParams.setString("ALTMNT_RGTR_ID", null);
    			vstRsvtParams.setString("ALTMNT_DT", null);
    		}
    		
    		objRetParams = twbComDAO.update(namespace, "vstRsvtMod", vstRsvtParams);
    	}
		
		//등록 시, api response 로 생성된 VST_RSVT_ID를 넘겨주기 위함.
		objRetParams.setString("VST_RSVT_ID", jsonParams.getString("VST_RSVT_ID"));
		
        jsonParams.setString("VSTR_ID", sVstrId);
        TelewebJSON tokenInfo = this.getVstrToken(jsonParams);
        
        String input = jsonParams.getString("RSVT_BGNG_DT");
        
        if("VRS_RTRCN".equals(jsonParams.getString("STTS_CD"))) { //취소
			input = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("RSVT_BGNG_DT");
		}
        
        objRetParams = this.vstRsvtHstryReg(jsonParams); //방문 예약 이력 저장
		
    	jsonParams.setString("STAT", "DEL");
    	jsonParams.setString("CUSL_ID", jsonParams.getString("USER_ID"));
		objRetParams = this.vstVstrRdyProc(jsonParams); //방문자 대기 삭제 처리
        
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        String sRsvtBgngDt = dateTime.format(outputFormatter); //방문일시(메시지용)
        
        String deviceToken = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("TOKEN"); //다비이스 토큰
        String vstrNm = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("USER_NM"); //방문 기사명
        String vstrTelNo = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("MBL_PHN_NO"); //방문 기사 전화번호
        String vstrDeptNm = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("DEPT_NM"); //방문 기사 소속 센터
        String deptTelNo = ((JSONObject) tokenInfo.getDataObject("DATA").get(0)).getString("OFC_NO"); //방문 기사 소속 센터 전화번호
        String serviceNm = ("ST_FIX".equals(jsonParams.getString("SRVC_TYPE_CD"))?"수리":"설치");
        String custNm = jsonParams.getString("CUST_NM"); //고객명
        String vstAddr = jsonParams.getString("ADDR") + " " + jsonParams.getString("ADDR_DTL"); //고객주소
        
        //FCM
        String sFcmTitl = "";
    	String sFcmMsg = "";
    	
    	//SMS
    	String sSujbect = "캐리어에어컨";
    	String sMessage = "";
    	
		if(!"VRS_CMPTN".equals(jsonParams.getString("STTS_CD")) && !"VRS_CMPTN".equals(jsonParams.getString("STTS_CD"))) { //완료, 재방문이 아닌 경우
			if("VRS_ALTMNT".equals(jsonParams.getString("STTS_CD"))) { //배정, 변경
				if(StringUtils.isEmpty(jsonParams.getString("STTS_DTL_CD"))) {//배정
					sFcmTitl = "새로운 일정이 등록되었습니다";
					sFcmMsg = sRsvtBgngDt + "에 새로운 " + serviceNm + " 일정이 등록되었습니다.자세한 내용은 앱을 확인 해 주세요.";
					
					sMessage = "안녕하세요 "+custNm+"님, 캐리어입니다.";
					sMessage += "\n"+serviceNm+" 예약이 접수되었습니다.";
					sMessage += "\n---------------";
					sMessage += "\n▶방문일: " + sRsvtBgngDt;
					sMessage += "\n▶방문지: " + vstAddr;
					sMessage += "\n▶방문 기사: " + vstrNm;
					sMessage += "\n---------------";
					sMessage += "\n감사합니다.";
				}else { //변경
					sFcmTitl = "일정이 변경되었습니다";
					sFcmMsg = custNm + "님의 " + serviceNm + " 일정이 " + sRsvtBgngDt + "으로 변경 되었습니다.자세한 내용은 앱을 확인 해 주세요.";
//					sMessage = "안녕하세요, "+custNm+"님! "+serviceNm+" 일정이 "+sRsvtBgngDt+"으로 변경 되었습니다."+vstrNm+" 기사님이 "+vstAddr+"로 방문 예정입니다. 감사합니다.";
					sMessage = "안녕하세요 "+custNm+"님, 캐리어입니다.";
					sMessage += "\n"+serviceNm+" 예약이 변경되었습니다.";
					sMessage += "\n---------------";
					sMessage += "\n▶방문일: " + sRsvtBgngDt;
					sMessage += "\n▶방문지: " + vstAddr;
					sMessage += "\n▶방문 기사: " + vstrNm;
					sMessage += "\n---------------";
					sMessage += "\n감사합니다.";
				}
			}else if("VRS_RTRCN".equals(jsonParams.getString("STTS_CD"))) { //취소
				sFcmTitl = "일정이 취소되었습니다";
				sFcmMsg = custNm + "님의 " + serviceNm + " 일정이 취소되었습니다.자세한 내용은 앱을 확인 해 주세요.";
//				sMessage = "안녕하세요, "+custNm+"님! "+sRsvtBgngDt+"에 예정된 "+serviceNm+" 일정이 취소 되었습니다. 불편을 드려 죄송합니다.";
				sMessage = "안녕하세요 "+custNm+"님, 캐리어입니다.";
				sMessage += "\n"+serviceNm+" 예약이 취소되었습니다.";
				sMessage += "\n---------------";
				sMessage += "\n▶방문일: " + sRsvtBgngDt;
				sMessage += "\n▶방문지: " + vstAddr;
				sMessage += "\n▶방문 기사: " + vstrNm;
				sMessage += "\n---------------";
				sMessage += "\n감사합니다.";
			}
		}else { //완료 재방문인 경우
			if("VRS_CMPTN".equals(jsonParams.getString("STTS_CD"))) { //완료
//				sMessage = "안녕하세요, "+custNm+"님! "+serviceNm+"가 성공적으로 완료 되었습니다. 저희 서비스를 이용해 주셔서 감사합니다.";
				sMessage = "안녕하세요 "+custNm+"님, 캐리어입니다.";
				sMessage += "\n"+serviceNm+"가 완료되었습니다.";
				sMessage += "\n---------------";
				sMessage += "\n▶방문일: " + sRsvtBgngDt;
				sMessage += "\n▶방문지: " + vstAddr;
				sMessage += "\n▶방문 기사: " + vstrNm;
				sMessage += "\n---------------";
				sMessage += "\n저희 서비스를 이용해주셔서 감사합니다.";
			}else if("VRS_CMPTN".equals(jsonParams.getString("STTS_CD"))) { //재방문
//				sMessage = "안녕하세요, "+custNm+"님! "+serviceNm+" 재방문 일정이 "+sRsvtBgngDt+" 으로 예약 되었습니다."+vstrNm+" 기사님이 "+vstAddr+"로 방문 예정입니다. 감사합니다.";
				sMessage = "안녕하세요 "+custNm+"님, 캐리어입니다.";
				sMessage += "\n"+serviceNm+" 재방문 예약이 접수되었습니다.";
				sMessage += "\n---------------";
				sMessage += "\n▶방문일: " + sRsvtBgngDt;
				sMessage += "\n▶방문지: " + vstAddr;
				sMessage += "\n▶방문 기사: " + vstrNm;
				sMessage += "\n---------------";
				sMessage += "\n감사합니다.";
			}
		}
	        
		//FCM 전송
		if(!StringUtils.isEmpty(sFcmTitl)) {
			FcmMessage fcmMessage = new FcmMessage();
	        fcmMessage.setToken(deviceToken);
	        fcmMessage.setTitle(sFcmTitl);
	        fcmMessage.setBody(sFcmMsg);    //메시지
	        try {
	            fcmService.sendMessage(fcmMessage);
	            Thread.sleep(100);
	        } catch (Exception e) {
	            log.error("fcmMessage is exception : " + e.getMessage());
	        }
		}
	    	
	    //SMS 전송
		if(!StringUtils.isEmpty(sMessage)) {
			TelewebJSON smsParams = new TelewebJSON();
	        smsParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
	        smsParams.setString("USER_ID", jsonParams.getString("USER_ID"));

	        smsParams.setString("callback_number", jsonParams.getString("callback_number"));
	        smsParams.setString("auth_code", "");
	        smsParams.setString("phone_number", jsonParams.getString("CUST_TELNO"));// 사용자 전화번호
	        smsParams.setString("subject", sSujbect);// 제목
	        smsParams.setString("message", sMessage);// 사용자에게 전달될 메시지
	        smsParams.setString("send_date", "");// 발송예정일
	        smsParams.setString("SNDNG_SE_CD", "LMS");// 발송구분코드 - SMS/LMS/MMS/ATALK 구분
	        smsParams.setString("img_url", "");// 이미지 파일 정보
	        smsParams.setString("tenantId", TenantContext.getCurrentTenant());
	        try {
	            messageService.sendInfo(smsParams);
	        } catch(Exception e) {
	            throw new BusinessException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
	        }
		}
		
        return objRetParams;
    }
    
    /**
     * 방문 예약 이력 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON vstRsvtHstryReg(TelewebJSON jsonParams) throws TelewebAppException {
    	jsonParams.setString("VST_RSVT_HSTRY_ID", ""+innbCreatCmmnService.createSeqNo("VST_RSVT_HSTRY_ID"));
    	
    	return twbComDAO.insert(namespace, "vstRsvtHstryReg", jsonParams);
    }

    /**
     * 방문 예약 상담 저장
     */
    @Transactional(readOnly = false)
    public TelewebJSON vstRsvtCuttReg(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.insert(namespace, "vstRsvtCuttReg", jsonParams);
    }
    
    /**
     * 배정 및 처리 이력 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON vstRsvtHistList(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.select(namespace, "vstRsvtHistList", jsonParams);
    }

    /**
     * 배정 기사 TOKEN 정보 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON getVstrToken(TelewebJSON jsonParams) throws TelewebAppException {
    	return twbComDAO.select(namespace, "getVstrToken", jsonParams);
    }
}
