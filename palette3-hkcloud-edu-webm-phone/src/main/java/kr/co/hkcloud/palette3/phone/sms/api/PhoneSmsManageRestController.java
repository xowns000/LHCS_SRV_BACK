package kr.co.hkcloud.palette3.phone.sms.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.sms.app.PhoneSmsManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneSmsManageRestController",
     description = "문자 메시지 관리 REST 컨트롤러")
public class PhoneSmsManageRestController
{
	private final InnbCreatCmmnService innbCreatCmmnService;
    private final PhoneSmsManageService phoneSmsManageService;

    /**
     * SMS트리 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS트리 조회",
//                  notes = "SMS트리 조회")
//    @PostMapping("/phone-api/sms/manage/tr/manage")
//    public Object selectRtnSmsTree(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        return phoneSmsManageService.selectRtnSmsTree(mjsonParams);
//    }


    /**
     * SMS 리스트 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS리스트 조회",
//            notes = "SMS리스트 조회")
//    @PostMapping("/phone-api/sms/manage/list")
//    public Object selectMainSmsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        log.debug("mjsonParams" + mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.selectMainSmsList(mjsonParams);
//
//        return objRetParams;
//    }
    
//    @ApiOperation(value = "SMS리스트 조회",
//            notes = "SMS리스트 조회")
//    @PostMapping("/phone-api/sms/manage/parentPath")
//    public Object selectParentPath(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        log.debug("mjsonParams" + mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.selectParentPath(mjsonParams);
//
//        return objRetParams;
//    }
    
    /**
     * MMS 업로드 파일 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS리스트 조회",
//            notes = "SMS리스트 조회")
//    @PostMapping("/phone-api/sms/manage/uploadFiles")
//    public Object selectMmsUploadFileList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        log.debug("mjsonParams" + mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.selectMmsUploadFileList(mjsonParams);
//
//        return objRetParams;
//    }
    
    /**
     * SMS템플릿 분류 추가 가능 여부 조회
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
//    @ApiOperation(value = "SMS템플릿 분류 트리 노드 추가 가능 여부 조회",
//            notes = "해당 분류 노드에 SMS템플릿이 존재하면 화면에서 하위 노드 생성 불가하도록 제어하기 위함")
//    @PostMapping("/phone-api/sms/manage/isRegTmplClsf")
//    public Object isRegTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.isRegTmplClsf(mjsonParams);
//    }
    
    /**
     * 템플릿 첨부파일 삭제
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
//    @ApiOperation(value = "mms 그룹키 업데이트",
//    		notes = "mms 그룹키 업데이트")
//    @PostMapping("/phone-api/sms/manage/deleteSmsTmplFileByFileKey")
//    public Object deleteSmsTmplFileByFileKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.deleteSmsTmplFileByFileKey(mjsonParams);
//    }
    
    /**
     * mms 그룹키 업데이트
     * 
     * @param  mjsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     */
//    @ApiOperation(value = "mms 그룹키 업데이트",
//    		notes = "mms 그룹키 업데이트")
//    @PostMapping("/phone-api/sms/manage/fileKeyUnity")
//    public Object fileKeyUnity(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.fileKeyUnity(mjsonParams);
//    }
    
    /**
     * SMS템플릿 분류 추가
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS템플릿 분류 추가",
//            notes = "SMS템플릿 분류 추가")
//    @PostMapping("/phone-api/sms/manage/insertTmplClsf")
//    public Object insertTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        return phoneSmsManageService.insertTmplClsf(mjsonParams);
//    }
    
    /**
     * SMS템플릿 분류 추가
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS템플릿 분류 추가",
//    		notes = "SMS템플릿 분류 추가")
//    @PostMapping("/phone-api/sms/manage/deleteTmplClsf")
//    public Object deleteTmplClsf(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.deleteTmplClsf(mjsonParams);
//    }
    
    /**
     * 템플릿유형 Tree 순서 변경
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "템플릿유형 Tree 순서 변경",
//            notes = "템플릿유형 Tree Node 순서를 변경 한다")
//    @PostMapping("/phone-api/sms/manage/cuttTypeOrderUpdate")
//    public Object cuttTypeOrderUpdate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.cuttTypeOrderUpdate(mjsonParams);
//    }
    
    /**
     * 템플릿유형 Tree 순서 변경
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "템플릿유형 Tree 순서 변경",
//            notes = "템플릿유형 Tree Node 순서를 변경 한다")
//    @PostMapping("/phone-api/sms/manage/deleteSmsTmpl")
//    public Object deleteSmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.deleteSmsTmpl(mjsonParams);
//    }
    
    /**
     * SMS상세조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS상세조회",
//            notes = "SMS상세조회")
//    @PostMapping("/phone-api/sms/manage/selectRtnSmsDetail")
//    public Object selectRtnSmsDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.selectRtnSmsDetail(mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS 트리 클릭 시 해당 SMS의 하위 SMS 조회
     *
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS하위리스트 조회",
//            notes = "SMS하위리스트 조회")
//    @PostMapping("/phone-api/sms/manage/low/list")
//    public Object selectRtnLowSms(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.selectRtnLowSms(mjsonParams);
//
//        return objRetParams;
//    }

    /**
     * SMS 저장
     * 
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS 저장",
//                  notes = "SMS 저장")
//    @PostMapping("/phone-api/sms/manage/insertSmsTmpl")
//    public Object insertSmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        return phoneSmsManageService.insertSmsTmpl(mjsonParams);
//
//    }
    
    /**
     * SMS 저장
     * 
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS 저장",
//    		notes = "SMS 저장")
//    @PostMapping("/phone-api/sms/manage/modifySmsTmpl")
//    public Object modifySmsTmpl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//    	return phoneSmsManageService.modifySmsTmpl(mjsonParams);
//    	
//    }


    /**
     * SMS 업데이트
     * 
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS 업데이트",
//                  notes = "SMS 업데이트.")
//    @PostMapping("/phone-api/sms/manage/modify")
//    public Object updateRtnSmsMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.updateRtnSmsMng(mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS정보 삭제
     * 
     * @param  mjsonParams
     * @return
     */
//    @ApiOperation(value = "SMS정보 삭제",
//                  notes = "SMS정보 삭제")
//    @PostMapping("/phone-api/sms/manage/delete")
//    public Object deleteRtnSmsMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = phoneSmsManageService.deleteRtnSmsMng(mjsonParams);
//
//        return objRetParams;
//    }

    /**
     * 
     * 문자 발송
     * MTS - DB에 INSERT만 해주면 AGENT프로그램이 배치가 돌면서 문자 발송
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 발송",
//            	  notes = "문자 발송")
//	@PostMapping("/phone-api/main/sms/send")
//	public Object sendSMS(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//	{
//	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//      String desc = mjsonParams.getString("SMS_DESC");
//      desc = desc.replaceAll("&lt;br&gt;", "\n");
//      desc = desc.replaceAll("&amp;", "&");
//      desc = desc.replaceAll("&lt;", "<");
//      desc = desc.replaceAll("&gt;", ">");
//      desc = desc.replaceAll("&#40;", "(");
//      desc = desc.replaceAll("&#41;", ")");
//      desc = desc.replaceAll("&#91;", "[");
//      desc = desc.replaceAll("&#93;", "]");
//      desc = desc.replaceAll("&quot;", "\"");
//      desc = desc.replaceAll("&apos;", "'");
//      mjsonParams.setString("SMS_DESC", desc);
//	  objRetParams = phoneSmsManageService.sendSMS(mjsonParams);
//	  //최종결과값 반환
//	  return objRetParams;
//	} 
    
    /**
     * 
     * 문자 다건발송 조회
     * MTS - DB에 INSERT만 해주면 AGENT프로그램이 배치가 돌면서 문자 발송
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 다건발송 - 문자 다건발송 조회",
//            	  notes = "문자 다건발송 조회")
//	@PostMapping("/phone-api/main/sms/multiSendInq")
//	public Object multiSendSMSInq(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//	{
//	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//	  objRetParams = phoneSmsManageService.multiSendSMSInq(mjsonParams);
//	  //최종결과값 반환
//	  return objRetParams;
//	}  
    
    /**
     * 
     * 문자 템플릿 조회
     * MTS - DB에 INSERT만 해주면 AGENT프로그램이 배치가 돌면서 문자 발송
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 관리 -문자 템플릿 조회",
//            	  notes = "문자 템플릿 조회")
//	@PostMapping("/phone-api/main/sms/smsTmpInq")
//	public Object SMSTmpInq(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//	{
//	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//	  objRetParams = phoneSmsManageService.SMSTmpInq(mjsonParams);
//	  //최종결과값 반환
//	  return objRetParams;
//	}  
    
    /**
     * 
     * 발송 문자 이력 조회
     * MTS - DB에 INSERT만 해주면 AGENT프로그램이 배치가 돌면서 문자 발송
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 리스트 - 발송문자 이력조회",
//            	  notes = "발송문자 이력조회")
//	@PostMapping("/phone-api/main/sms/smsInq")
//	public Object SMSInq(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//	{
//	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//	  objRetParams = phoneSmsManageService.SMSInq(mjsonParams);
//	  //최종결과값 반환
//	  return objRetParams;
//	}  
    
    /**
     * sms다건발송.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
//    @ApiOperation(value = "sms다건발송",
//                  notes = "sms다건발송")
//    @PostMapping("/phone-api/main/sms/multiSend")
//    public Object SMSMultiSend(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//
//        TelewebJSON objRetKey = new TelewebJSON(mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);   //반환 파라메터 생성
//        TelewebJSON obndCustParams = new TelewebJSON(mjsonParams);
//
//        // FORM(DATA)데이터
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//
//        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
//        String regMan = mjsonParams.getString("REG_MAN");                   //화면에서 전송된 플래그 설정
//        String chngMan = mjsonParams.getString("CHNG_MAN");                   //화면에서 전송된 플래그 설정
//        String desc = mjsonParams.getString("SMS_DESC");
//        desc = desc.replaceAll("&lt;br&gt;", "\n");
//        desc = desc.replaceAll("&amp;", "&");
//        desc = desc.replaceAll("&lt;", "<");
//        desc = desc.replaceAll("&gt;", ">");
//        desc = desc.replaceAll("&#40;", "(");
//        desc = desc.replaceAll("&#41;", ")");
//        desc = desc.replaceAll("&#91;", "[");
//        desc = desc.replaceAll("&#93;", "]");
//        desc = desc.replaceAll("&quot;", "\"");
//        desc = desc.replaceAll("&apos;", "'");
//
//        jsonParams.setString("REG_MAN", regMan);
//        jsonParams.setString("CHNG_MAN", chngMan);
//        jsonParams.setString("DATA_FLAG", strTransFlag);
//        jsonParams.setString("SMS_DESC", desc);
//        JSONArray smsCustArr = mjsonParams.getDataObject("SMS_CUST_LIST");
//        jsonParams.setDataObject("SMS_CUST_ARR", smsCustArr);
//
//        //캠페인 등록
//        objRetParams = phoneSmsManageService.SMSMultiSend(jsonParams);
//
//        //최종결과값 반환
//        return objRetParams;
//    }
    
    /**
     * 
     * ipcc에서 문자 발송
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 발송 - ipcc에서 문자 발송",
//            	  notes = "문자 발송")
//	@PostMapping("/phone-api/main/sms/ipcc-send")
//	public String ipccSendSMS(HttpServletRequest request) throws Exception
//	{
//	  TelewebJSON objRetParams = new TelewebJSON();
//	  TelewebJSON mjsonParams = new TelewebJSON();
//      mjsonParams.setString("ASP_NEWCUST_KEY", request.getParameter("CUSTCO_ID"));
//      mjsonParams.setString("RCV_TEL_NO", request.getParameter("TRAN_PHONE"));
//      mjsonParams.setString("TRAN_DATE", request.getParameter("TRAN_DATE").substring(0,12));
//      String mmsNum = "";
//      String smsTyp = "";
//      Enumeration<String> paramNames = request.getParameterNames();
//      while(paramNames.hasMoreElements()) {
//    	  String paramName = paramNames.nextElement();
//    	  String paramValue = request.getParameter(paramName);
//    	  log.debug("paramName : " + paramName + "paramValue : " + paramValue);
//    	  if(paramName.equals("SMS_NUM")) {
//    		  mmsNum = paramValue;
//    	  }
//    	  if(paramName.equals("SMS_TYP")) {
//    		  smsTyp = paramValue;
//    	  }
//      }
//      mjsonParams.setString("SMS_NUM", mmsNum);
//      mjsonParams.setString("SMS_TYP", smsTyp);
//	  //request.setCharacterEncoding("euc-kr");
//      String desc = request.getParameter("TRAN_MSG");
//      //String desc = new String(request.getParameter("TRAN_MSG").getBytes("8859_1"),"euc-kr");
//      log.debug("desc" + desc);
//      desc = desc.replaceAll("&lt;br&gt;", "\n");
//      desc = desc.replaceAll("&amp;", "&");
//      desc = desc.replaceAll("&lt;", "<");
//      desc = desc.replaceAll("&gt;", ">");
//      desc = desc.replaceAll("&#40;", "(");
//      desc = desc.replaceAll("&#41;", ")");
//      desc = desc.replaceAll("&#91;", "[");
//      desc = desc.replaceAll("&#93;", "]");
//      desc = desc.replaceAll("&quot;", "\"");
//      desc = desc.replaceAll("&apos;", "'");
//      mjsonParams.setString("SMS_DESC", desc);
//      mjsonParams.setString("IPCC", "Y");
//	  objRetParams = phoneSmsManageService.sendSMS(mjsonParams);
//	  //최종결과값 반환
//	  TelewebJSON resultObj = new TelewebJSON();
//	  String header = objRetParams.toString();
//	  if(header.contains("HEADER")) {
//		  if(header.contains("ERROR_FLAG")) {
//			  if(header.contains("false") && header.contains("정상")) {
//				  resultObj.setString("result_code", "0");
//				  resultObj.setString("result_msg", "success");
//			  } else {
//				  resultObj.setString("result_code", "99");
//				  resultObj.setString("result_msg", "fail");
//			  }
//		  } else {
//			  resultObj.setString("result_code", "99");
//			  resultObj.setString("result_msg", "fail");
//		  }
//	  } else {
//		  resultObj.setString("result_code", "99");
//		  resultObj.setString("result_msg", "wrong data");
//	  }
//	  JSONArray resultArr = resultObj.getDataObject("DATA");
//	  String result = resultArr.getString(0);
//	  return result;
//	}  
    
    /**
     * 
     * ipcc에서 이미지가 포함된 문자 발송
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "문자 발송 - ipcc에서 이미지가 포함된 문자 발송",
//            	  notes = "문자 발송")
//	@PostMapping("/phone-api/main/mms/ipcc-send")
//	public String ipccSendMMS(HttpServletRequest request) throws Exception
//	{
//	  TelewebJSON objRetParams = new TelewebJSON();
//	  TelewebJSON mjsonParams = new TelewebJSON();
//      mjsonParams.setString("ASP_NEWCUST_KEY", request.getParameter("CUSTCO_ID"));
//      mjsonParams.setString("RCV_TEL_NO", request.getParameter("TRAN_PHONE"));
//      mjsonParams.setString("TRAN_DATE", request.getParameter("TRAN_DATE").substring(0,12));
//      mjsonParams.setString("MMS_NUM", request.getParameter("NUM"));
//	  //request.setCharacterEncoding("euc-kr");
//      String desc = request.getParameter("TRAN_MSG");
//      //String desc = new String(request.getParameter("TRAN_MSG").getBytes("8859_1"),"euc-kr");
//      log.debug("desc" + desc);
//      desc = desc.replaceAll("&lt;br&gt;", "\n");
//      desc = desc.replaceAll("&amp;", "&");
//      desc = desc.replaceAll("&lt;", "<");
//      desc = desc.replaceAll("&gt;", ">");
//      desc = desc.replaceAll("&#40;", "(");
//      desc = desc.replaceAll("&#41;", ")");
//      desc = desc.replaceAll("&#91;", "[");
//      desc = desc.replaceAll("&#93;", "]");
//      desc = desc.replaceAll("&quot;", "\"");
//      desc = desc.replaceAll("&apos;", "'");
//      mjsonParams.setString("SMS_DESC", desc);
//      mjsonParams.setString("IPCC", "Y");
//      mjsonParams.setString("SMS_TYP", "MMS");
//	  objRetParams = phoneSmsManageService.sendSMS(mjsonParams);
//	  //최종결과값 반환
//	  TelewebJSON resultObj = new TelewebJSON();
//	  String header = objRetParams.toString();
//	  if(header.contains("HEADER")) {
//		  if(header.contains("ERROR_FLAG")) {
//			  if(header.contains("false") && header.contains("정상")) {
//				  resultObj.setString("result_code", "0");
//				  resultObj.setString("result_msg", "success");
//			  } else {
//				  resultObj.setString("result_code", "99");
//				  resultObj.setString("result_msg", "fail");
//			  }
//		  } else {
//			  resultObj.setString("result_code", "99");
//			  resultObj.setString("result_msg", "fail");
//		  }
//	  } else {
//		  resultObj.setString("result_code", "99");
//		  resultObj.setString("result_msg", "wrong data");
//	  }
//	  JSONArray resultArr = resultObj.getDataObject("DATA");
//	  String result = resultArr.getString(0);
//	  return result;
//	}  
}
