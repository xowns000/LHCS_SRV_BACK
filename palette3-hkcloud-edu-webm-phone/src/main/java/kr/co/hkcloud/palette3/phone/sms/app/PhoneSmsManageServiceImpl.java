package kr.co.hkcloud.palette3.phone.sms.app;


import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneSmsManageService")
public class PhoneSmsManageServiceImpl implements PhoneSmsManageService
{
    private final TwbComDAO            twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * SMS트리 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectRtnSmsTree(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectRtnSmsTree", mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS트리 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectRtnSmsList(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectRtnSmsList", mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectMainSmsList(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectMainSmsList", mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS상세조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = true)
//    public TelewebJSON selectRtnSmsDetail(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectRtnSmsDetail", mjsonParams);
//
//        return objRetParams;
//    }
    
    /**
     * SMS템플릿 분류 추가 가능 여부 조회
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    public TelewebJSON isRegTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "isRegTmplClsf", mjsonParams);
//    }
    
    /**
     * 템플릿 분류 추가
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON insertTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        return twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "insertTmplClsf", mjsonParams);
//    }
    
    /**
     * 템플릿 분류 모든 하위 요소(자식노드) 검색
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON selectAllChildOfTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON allChildList = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectAllChilOfTmplClsf", mjsonParams);
//    	
//    	TelewebJSON newParam = new TelewebJSON();
//    	List<String> tmplClsfIdList = new ArrayList<>();
//    	JSONArray jsonObj = allChildList.getDataObject("DATA");
//    	
//    	if(jsonObj.size() > 0){
//	    	for (int n = 0; n < jsonObj.size(); n++) {
//	        	JSONObject objData = jsonObj.getJSONObject(n);
//	        	
//	        	@SuppressWarnings("rawtypes")
//	            Iterator it = objData.keys();
//	            while(it.hasNext()) {
//	                String strKey = (String) it.next();
//	                String strValue = objData.getString(strKey);
//	                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_CLSF_ID") > -1) {
//	                	tmplClsfIdList.add(strValue);
//	                }
//	            }
//	        }
//	    	newParam.setObject("tmplClsfIdList", 0, tmplClsfIdList);
//    	}
//    	return newParam;
//    }
    
    /**
     * 템플릿 분류 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON deleteTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	/** 삭제하는 템플릿 분류의 하위 분류들 작업 시작 */
//    	// 템플릿 분류 하위 모든 자식 조회
//    	TelewebJSON childTmplClsfIdList = this.selectAllChildOfTmplClsf(mjsonParams);
//    	
//    	// 템플릿 분류 하위 모든 분류에 등록된 템플릿의 첨부파일들 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteAttacheFileConnectedToAllchildOfSmsTmpl", childTmplClsfIdList);
//    	
//    	// 템플릿 분류 하위 모든 분류에 등록된 템플릿 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteSmsTmplConnectedToAllchildOfTmplClsf", childTmplClsfIdList);
//    	
//    	// 템플릿 분류 하위 모든 분류 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteAllchildOfTmplClsf", childTmplClsfIdList);
//    	/** 삭제하는 템플릿 분류의 하위 분류들 작업 끝 */
//    	
//    	/** 삭제 실행한 템플릿 분류 삭제 시작 */
//    	// 템플릿 분류에 등록된 템플릿의 첨부파일 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteAttacheFileConnectedToSmsTmpl", mjsonParams);
//    	// 템플릿 분류에 등록된 템플릿 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteSmsTmplConnectedToTmplClsf", mjsonParams);
//    	// 템플릿 분류 삭제
//    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper"
//    			, "deleteTmplClsf", mjsonParams);
//    	/** 삭제 실행한 템플릿 분류 삭제 시작 */
//    }
    
    /**
     * SMS하위리스트조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON selectRtnLowSms(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectRtnLowSms", mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON insertSmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
//    {     
//        return twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "insertSmsTmpl", mjsonParams);
//    }
    
    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON modifySmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
//    {     
//    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "modifySmsTmpl", mjsonParams);
//    }
    
    /**
     * mms 그룹키 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON fileKeyUnity(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON objNewParams = new TelewebJSON();
//    	boolean isExecute = false;
//    	
//    	List<String> arrFileKey = new ArrayList<>();
//    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
//    	
//        for (int n = 0; n < jsonObj.size(); n++) {
//        	JSONObject objData = jsonObj.getJSONObject(n);
//        	
//        	@SuppressWarnings("rawtypes")
//            Iterator it = objData.keys();
//            while(it.hasNext()) {
//                String strKey = (String) it.next();
//                String strValue = objData.getString(strKey);
//                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("arrFileKey") > -1) {
//                	arrFileKey.add(strValue);
//                	
//                	if(!isExecute) {
//                		isExecute = true;
//                	}
//                }
//                
//                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("FILE_GROUP_KEY") > -1){
//                	objNewParams.setObject("FILE_GROUP_KEY", 0, strValue);
//                	
//                	if(!isExecute) {
//                		isExecute = true;
//                	}
//                }
//            }
//        }
//        //신규설정메뉴가 있으면
//        if(isExecute) {
//        	objNewParams.setObject("arrFileKey", 0, arrFileKey);		
//		}
//    	
//        return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "fileKeyUnity", objNewParams);
//    }
    
    /**
     * 템플릿 유형 Tree 순서 변경
     */
//    @Transactional(propagation = Propagation.REQUIRED,
//    		rollbackFor = {Exception.class, SQLException.class},
//    		readOnly = false)
//    public TelewebJSON cuttTypeOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
//    	
//    	if("UP".equals(mjsonParams.getString("ORDER_TYPE"))) {
//    		if(Integer.parseInt(mjsonParams.getString("SORT_ORD")) > 1) {
//    			mjsonParams.setInt("ADD_NUM", 1);
//    			mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD"))-1);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "updateCuttTypeOtherSortOrder", mjsonParams);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "updateCuttTypeSortOrder", mjsonParams);
//    		}
//    	}else if("DOWN".equals(mjsonParams.getString("ORDER_TYPE"))) {
//    		if(Integer.parseInt(mjsonParams.getString("SORT_ORD")) < Integer.parseInt(mjsonParams.getString("MAX_SORT_ORD"))) {
//    			mjsonParams.setInt("ADD_NUM", -1);
//    			mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD"))+1);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "updateCuttTypeOtherSortOrder", mjsonParams);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "updateCuttTypeSortOrder", mjsonParams);
//    		}
//    	}
//    	
//    	return objRetParams;
//    }
    
    /**
     * 템플릿 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON deleteSmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	// 파일 그룹키 조회
//    	TelewebJSON newParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectSmsTmplFileGroupKey", mjsonParams);
//    	// 첨부파일 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "deleteSmsTmplFile", newParams);
//    	
//    	// 템플릿 정보 삭제
//    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "deleteSmsTmpl", mjsonParams);
//    }
    
    /**
     * 파일키로 파일 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON deleteSmsTmplFileByFileKey(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON objNewParams = new TelewebJSON();
//    	boolean isExecute = false;
//    	
//    	List<String> arrFileKey = new ArrayList<>();
//    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
//    	
//        for (int n = 0; n < jsonObj.size(); n++) {
//        	JSONObject objData = jsonObj.getJSONObject(n);
//        	
//        	@SuppressWarnings("rawtypes")
//            Iterator it = objData.keys();
//            while(it.hasNext()) {
//                String strKey = (String) it.next();
//                String strValue = objData.getString(strKey);
//                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("FILE_KEY") > -1) {
//                	arrFileKey.add(strValue);
//                	
//                	if(!isExecute) {
//                		isExecute = true;
//                	}
//                }
//            }
//        }
//        //신규설정메뉴가 있으면
//        if(isExecute) {
//        	objNewParams.setObject("arrFileKey", 0, arrFileKey);		
//		}
//        
//    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "deleteSmsTmplFileByFileKey", objNewParams);
//    }
    
    
    
    /**
     * MMS 업로드 파일 목록 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON selectMmsUploadFileList(TelewebJSON mjsonParams) throws TelewebAppException
//    {   
//        return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectMmsUploadFileList", mjsonParams);
//    }

    /**
     * SMS 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON updateRtnSmsMng(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        log.debug("11mjsonParams ==========" + mjsonParams);
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "updateRtnSmsMng", mjsonParams);
//
//        return objRetParams;
//    }


    /**
     * SMS정보 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    @Transactional(readOnly = false)
//    public TelewebJSON deleteRtnSmsMng(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "deleteRtnSmsMng", mjsonParams);
//
//        return objRetParams;
//    }

    /**
     * 
     * 문자발송(SMS)
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON sendSMS(TelewebJSON jsonParams) throws TelewebAppException
//    {
//    	String SMS_TYP = jsonParams.getString("SMS_TYP");
//
//    	String sendTel = jsonParams.getString("SND_TEL_NO");
//    	if(sendTel == "" || sendTel == null) {
//	        TelewebJSON sendNumParams = new TelewebJSON();
//	        sendNumParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectSendNum", jsonParams);
//	        jsonParams.setString("SND_TEL_NO",sendNumParams.getString("SND_TEL_NO"));
//    	}
//    	
//        String msg = jsonParams.getString("SMS_DESC");
//    	String ipcc = jsonParams.getString("IPCC");
//    	if(ipcc.equals("Y")) {
//    		if(SMS_TYP.equals("MMS")) {
//	            TelewebJSON objFileCnt = new TelewebJSON();
//	            TelewebJSON objFileName = new TelewebJSON();
//	            TelewebJSON objFilePath = new TelewebJSON();
//	            
//	            objFileCnt = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "ipccFileCnt", jsonParams);
//	            String cnt = objFileCnt.getString("CNT");
//	            if(Integer.parseInt(cnt) != 0) {
//		            objFileName = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "ipccFileName", jsonParams);
//		            objFilePath = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "ipccFilePath", jsonParams);
//		            String fileNm = objFileName.getString("CD_NM");
//		            String filePath = objFilePath.getString("CD_NM");
//		            jsonParams.setString("FILE_NM",fileNm);
//		            jsonParams.setString("FILE_PATH",filePath);
//	            } else {
//	    	        if(msg.length()<80) {
//	    	        	jsonParams.setString("SMS_TYP","SMS");
//	    	        } else {
//	    	        	jsonParams.setString("SMS_TYP","LMS");
//	    	        }
//	            }
//    		} else {
//    	        if(msg.length()<80) {
//    	        	jsonParams.setString("SMS_TYP","SMS");
//    	        } else {
//    	        	jsonParams.setString("SMS_TYP","LMS");
//    	        }
//    		}
//    	}
//    	
//    	SMS_TYP = jsonParams.getString("SMS_TYP");
//    	String seq = "";
//    	int idx = 0;
//    	if(SMS_TYP.equals("SMS")) {
//            seq = innbCreatCmmnService.getSeqNo("MTS_SMS_MSG", "SMS");
//            idx = seq.indexOf("S"); 
//    	} else if(SMS_TYP.equals("LMS")) {
//    		seq = innbCreatCmmnService.getSeqNo("MTS_MMS_MSG", "LMS");
//            idx = seq.indexOf("L"); 
//    	} else if(SMS_TYP.equals("MMS")) {
//    		seq = innbCreatCmmnService.getSeqNo("MTS_MMS_MSG", "MMS");
//            idx = seq.indexOf("M"); 
//    	} 
//        String TRAN_PR = seq.substring(idx);
//        jsonParams.setString("TRAN_PR", TRAN_PR); 
//        
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "sendSMS", jsonParams);
//    	
//        if(!jsonParams.getString("FILE_NM").equals("")) {
//        	String file = jsonParams.getString("FILE_NM");
//        	String[] fileArr = file.split(",");
//        	String filePath = jsonParams.getString("FILE_PATH");
//        	filePath = filePath.substring(filePath.indexOf("content")+7);
//        	
//        	for(int i=0; i<fileArr.length;i++) {
//        		jsonParams.setString("FILE", filePath + "/" +fileArr[i]);             		// 발송컨텐츠
//        		jsonParams.setString("CONTENT_SEQ",  Integer.toString(i+1));             		// 이미지순서
//
//                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "insertRtnSmsCon", jsonParams);
//        	}
//        }
//        return objRetParams;
//    }
    
    /**
     * 
     * 다건발송
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON multiSendSMS(TelewebJSON jsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "multiSendSMS", jsonParams);
//        return objRetParams;
//    }
    
    /**
     * 
     * 다건발송 조 회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON multiSendSMSInq(TelewebJSON jsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "multiSendSMSInq", jsonParams);
//        return objRetParams;
//    }
    
    /**
     * 
     * 문자목록조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON SMSTmpInq(TelewebJSON jsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "SMSTmpInq", jsonParams);
//        return objRetParams;
//    }
    
    /**
     * 
     * 문자 리스트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Override
//    public TelewebJSON SMSInq(TelewebJSON jsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON();
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "SMSInq", jsonParams);
//        return objRetParams;
//    }
    
    /**
     * sms다건발송
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
//    @Transactional(propagation = Propagation.REQUIRED,
//                   rollbackFor = {Exception.class, SQLException.class},
//                   readOnly = false)
//    public TelewebJSON SMSMultiSend(TelewebJSON jsonParams) throws TelewebAppException
//    {
//        TelewebJSON objRetParams = new TelewebJSON();
//
//
//    	String SMS_TYP = jsonParams.getString("SMS_TYP");
//    	String seq = "";
//    	int idx = 0;
//    	if(SMS_TYP.equals("SMS")) {
//            seq = innbCreatCmmnService.getSeqNo("MTS_SMS_MSG_BAT", "SMS");
//            idx = seq.indexOf("S"); 
//    	} else if(SMS_TYP.equals("LMS")) {
//    		seq = innbCreatCmmnService.getSeqNo("MTS_MMS_MSG_BAT", "LMS");
//            idx = seq.indexOf("L"); 
//    	} else if(SMS_TYP.equals("MMS")) {
//    		seq = innbCreatCmmnService.getSeqNo("MTS_MMS_MSG_BAT", "MMS");
//            idx = seq.indexOf("M"); 
//    	} 
//        String TRAN_PR = seq.substring(idx);
//        jsonParams.setString("TRAN_PR", TRAN_PR); 
//
//        log.debug("TRAN_PR"+TRAN_PR);
//        if(!TRAN_PR.equals("") && TRAN_PR != null) {
//            // null 체크
//            log.debug("here@@@" + jsonParams.getDataObject("SMS_CUST_ARR"));
//            if(jsonParams.getDataObject("SMS_CUST_ARR") != null) {
//
//                // 캠페인고객대상정보조회
//                JSONArray smsCustArr = jsonParams.getDataObject("SMS_CUST_ARR");
//
//                // 캠페인대상 고객 정보가 있는 경우만 
//                if(smsCustArr.size() > 0) {
//                    @SuppressWarnings("unchecked")
//                    Iterator<JSONObject> smsCustIter = smsCustArr.iterator();
//
//                    while(smsCustIter.hasNext()) {
//
//                        JSONObject jsonObj = new JSONObject();
//                        jsonObj = smsCustIter.next();
//
//                        System.out.println("jsonObj====>" + jsonObj);
//                        TelewebJSON objRetCust = new TelewebJSON();
//                        objRetCust.setString("TRAN_PR", TRAN_PR+jsonObj.getString("MOBIL_NO"));				// SMS ID
//
//                        objRetCust.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));   // 회사구분
//                        objRetCust.setString("RCV_TEL_NO", jsonObj.getString("MOBIL_NO"));             		// 전화번호
//                        objRetCust.setString("USER_ID", jsonParams.getString("USER_ID"));             		// USER_ID
//                        objRetCust.setString("INLNE_NO", jsonParams.getString("INLNE_NO"));             	// 내선번호
//                        objRetCust.setString("SND_TEL_NO", jsonParams.getString("SND_TEL_NO"));             // 수신번호
//                        objRetCust.setString("SMS_DET_DIV", jsonParams.getString("SMS_DET_DIV"));           // 발신번호
//                        objRetCust.setString("SMS_TYP", jsonParams.getString("SMS_TYP"));             		// 문자종류
//                        objRetCust.setString("SMS_DET_TIT", jsonParams.getString("SMS_DET_TIT"));           // SMS제목
//                        objRetCust.setString("SMS_DESC", jsonParams.getString("SMS_DESC"));             	// SMS내용
//                        objRetCust.setString("RCV_NOW", jsonParams.getString("RCV_NOW"));             		// 즉시발송
//                        objRetCust.setString("TRAN_DATE", jsonParams.getString("TRAN_DATE"));             	// 발송 날짜
//                        
//                        objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "SMSMultiSend", objRetCust);
//                        
//                        if(!jsonParams.getString("FILE_NM").equals("")) {
//                        	String file = jsonParams.getString("FILE_NM");
//                        	String[] fileArr = file.split(",");
//                        	String filePath = jsonParams.getString("FILE_PATH");
//                        	filePath = filePath.substring(filePath.indexOf("content")+7);
//                        	
//                        	for(int i=0; i<fileArr.length;i++) {
//                                objRetCust.setString("FILE", "../../.."+filePath + "/" + fileArr[i]);             		// 발송컨텐츠
//                        		//objRetCust.setString("FILE", "https://dev.hkpalette.com/smsmmsImg"+ filePath + "/" + fileArr[i]);             		// 발송컨텐츠
//                                objRetCust.setString("CONTENT_SEQ",  Integer.toString(i+1));             		// 이미지순서
//
//                                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "insertRtnSmsMultiCon", objRetCust);
//                        	}
//                        }
//                    }
//                }
//
//            }
//        }
//
//        //최종결과값 반환
//        return objRetParams;
//    }


//	@Override
//	public TelewebJSON selectParentPath(TelewebJSON mjsonParams) throws TelewebAppException {
//		
//		TelewebJSON objRetParams = new TelewebJSON();
//
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsManageMapper", "selectParentPath", mjsonParams);
//
//        return objRetParams;
//	}

}
