package kr.co.hkcloud.palette3.phone.sms.app;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneSmsTemplateManageService")
public class PhoneSmsTemplateManageServiceImpl implements PhoneSmsTemplateManageService
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
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnSmsTree(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectSmsTree", mjsonParams);

        return objRetParams;
    }

    /**
     * SMS템플릿 분류 추가 가능 여부 조회
     * @param  jsonParams
     * 				TMPL_CLSF_ID - 템플릿 분류 ID
     * @return 생성 가능 여부(Y/N)
     * @throws TelewebAppException 
     */
//    public TelewebJSON isRegTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "isRegTmplClsf", mjsonParams);
//    }
    

    /**
     * 템플릿 분류 추가
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	// 템플릿 유형 ID
    	String tmplClsfId = Integer.toString(innbCreatCmmnService.createSeqNo("TMPL_CLSF_ID"));
    	JSONArray jsonArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		jsonObject.put("TMPL_CLSF_ID", tmplClsfId);
		jsonArray.add(jsonObject);
        return twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "insertTmplClsf", mjsonParams);
    }
    
    /**
     * 템플릿 분류 추가
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON modifyTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "modifyTmplClsf", mjsonParams);
    }
    
    /**
     * 템플릿 분류 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	/** 삭제하는 템플릿 분류의 하위 분류들 작업 시작 */
    	// 템플릿 분류 하위 모든 자식 조회
    	
//    	TelewebJSON childTmplClsfIdList = this.selectAllChildOfTmplClsf(mjsonParams);
    	
    	// 템플릿 분류 하위 모든 분류에 등록된 템플릿의 첨부파일들 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
//    			, "deleteAttacheFileConnectedToAllchildOfSmsTmpl", childTmplClsfIdList);
    	
    	// 템플릿 분류 하위 모든 분류에 등록된 템플릿 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
//    			, "deleteSmsTmplConnectedToAllchildOfTmplClsf", childTmplClsfIdList);
    	
    	// 템플릿 분류 하위 모든 분류 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
//    			, "deleteAllchildOfTmplClsf", childTmplClsfIdList);
    	/** 삭제하는 템플릿 분류의 하위 분류들 작업 끝 */
    	
    	/** 삭제 실행한 템플릿 분류 삭제 시작 */
    	// 템플릿 분류에 등록된 템플릿의 첨부파일 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
//    			, "deleteAttacheFileConnectedToSmsTmpl", mjsonParams);
    	// 템플릿 분류에 등록된 템플릿 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
//    			, "deleteSmsTmplConnectedToTmplClsf", mjsonParams);
    	
    	// 템플릿 분류 삭제
    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper"
    			, "deleteTmplClsf", mjsonParams);
    	/** 삭제 실행한 템플릿 분류 삭제 시작 */
    }
    

    /**
     * 템플릿 분류 모든 하위 요소(자식노드) 검색
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
//    @Transactional(readOnly = false)
//	private TelewebJSON selectAllChildOfTmplClsf(TelewebJSON mjsonParams) throws TelewebAppException
//    {
//    	TelewebJSON allChildList = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectAllChildOfTmplClsf", mjsonParams);
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
     * SMS 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsList(TelewebJSON mjsonParams) throws TelewebAppException
    {
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//
//        objRetParams = 

        return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectSmsList", mjsonParams);
    }
    

    /**
     * 템플릿 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteSmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	// 파일 그룹키 조회
//    	TelewebJSON newParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectSmsTmplFileGroupKey", mjsonParams);
    	// 첨부파일 삭제
//    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "deleteSmsTmplFile", newParams);
    	
    	// 템플릿 정보 삭제
    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "deleteSmsTmpl", mjsonParams);
    }
    

    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertSmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	// SMS 템플릿 ID
    	String smsTmplId = Integer.toString(innbCreatCmmnService.createSeqNo("SMS_TMPL_ID"));
    	JSONArray jsonArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		jsonObject.put("SMS_TMPL_ID", smsTmplId);
		jsonArray.add(jsonObject);
        return twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "insertSmsTmpl", mjsonParams);
    }
    

    /**
     * SMS 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON modifySmsTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {     
    	return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "modifySmsTmpl", mjsonParams);
    }
    
    /**
     * mms 그룹키 업데이트
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON fileKeyUnity(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON objNewParams = new TelewebJSON();
    	// 실행여부
    	boolean isExecute = false;
    	
    	List<String> arrFileKey = new ArrayList<>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	
        for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("arrFileKey") > -1) {
                	arrFileKey.add(strValue);
                	
                	if(!isExecute) {
                		isExecute = true;
                	}
                }
                
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("FILE_GROUP_KEY") > -1){
                	objNewParams.setObject("FILE_GROUP_KEY", 0, strValue);
                	
                	if(!isExecute) {
                		isExecute = true;
                	}
                }
            }
        }
        // 실행 여부
        if(isExecute) {
        	objNewParams.setObject("arrFileKey", 0, arrFileKey);		
		}
    	
        return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "fileKeyUnity", objNewParams);
    }

    /**
     * MMS 업로드 파일 목록 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectMmsUploadFileList(TelewebJSON mjsonParams) throws TelewebAppException
    {   
        return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectMmsUploadFileList", mjsonParams);
    }

    /**
     * 파일키로 파일 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteSmsTmplFileByFileKey(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON objNewParams = new TelewebJSON();
    	boolean isExecute = false;
    	
    	List<String> arrFileKey = new ArrayList<>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	
        for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("FILE_KEY") > -1) {
                	arrFileKey.add(strValue);
                	
                	if(!isExecute) {
                		isExecute = true;
                	}
                }
            }
        }
        //신규설정메뉴가 있으면
        if(isExecute) {
        	objNewParams.setObject("arrFileKey", 0, arrFileKey);		
		}
        
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "deleteSmsTmplFileByFileKey", objNewParams);
    }
    
    /**
     * 템플릿 유형 정렬 순서 변경
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON changeTmplClsfSortOrder(TelewebJSON mjsonParams) throws TelewebAppException{
    	
    	// moveCount - 순서 변경 방향, -1은 위로 이동 / 1은 아래로 이동
    	int moveCount = mjsonParams.getInt("MOVE_COUNT");
    	this.getMoveCount(mjsonParams);
    	
    	List<String> tmplClsfList = this.getTmplClsfIdList(mjsonParams);
    	
    	// otherNodes sort_order + moveCount
    	JSONArray jsonArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		
		// 순서 변경 대상 정보 수정
		int reversMoveCount = -(mjsonParams.getInt("MOVE_COUNT"));
		jsonObject.put("PARAM_MOVE_COUNT", reversMoveCount);
		jsonArray.add(jsonObject);
		
    	this.updateChangeSortOrder(mjsonParams);
    	
    	// 본 노드 (sort_order - otherNodes.count)
    	List<String> tmplClsfIdList = new ArrayList<>();
    	tmplClsfIdList.add(jsonObject.getString("TMPL_CLSF_ID"));
    	
    	jsonObject.put("tmplClsfIdList", tmplClsfIdList);
    	jsonObject.put("PARAM_MOVE_COUNT", 
    			moveCount > 0 ? tmplClsfList.size() : -tmplClsfList.size());
    	jsonArray.add(jsonObject);
    	
    	return this.updateChangeSortOrder(mjsonParams);
    }
    
    private void getMoveCount(TelewebJSON mjsonParams) throws TelewebAppException{
    	
    	JSONArray jsonAry = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject objData = jsonAry.getJSONObject(0);
    	int moveCount = mjsonParams.getInt("MOVE_COUNT");
    	objData.put("SUM_MOVE_COUNT", moveCount);
    	mjsonParams.setDataObject(jsonAry);
    }
    
	private List<String> getTmplClsfIdList(TelewebJSON mjsonParams) throws TelewebAppException{
    	boolean isContinue = true;
    	List<String> tmplClsfIdList = new ArrayList<>();
    	
    	int moveCount = mjsonParams.getInt("MOVE_COUNT");
    	int sumMoveCount = moveCount;
    	JSONArray jsonAry = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject objData = jsonAry.getJSONObject(0);
    	do {
    		TelewebJSON result = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectChangeSortOrderOtherNode", mjsonParams);
    		
    		JSONArray resultJSONArray = result.getDataObject(TwbCmmnConst.G_DATA);
    		
        	JSONObject resultObjData = resultJSONArray.getJSONObject(0);
        	tmplClsfIdList.add(resultObjData.getString("TMPL_CLSF_ID"));
        	if("Y".equals(resultObjData.getString("USE_YN"))) {
        		isContinue = false;
        	}else {
        		sumMoveCount = sumMoveCount + moveCount;
            	objData.put("SUM_MOVE_COUNT", sumMoveCount);
            	mjsonParams.setDataObject(jsonAry);
        	}
    	} while(isContinue);
    	objData.put("tmplClsfIdList", tmplClsfIdList);
    	mjsonParams.setDataObject(jsonAry);
		
    	return tmplClsfIdList;
    }
	
	private TelewebJSON updateChangeSortOrder(TelewebJSON mjsonParams) throws TelewebAppException{
		return twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateChangeSortOrder", mjsonParams);
	}
	
    ////////////////////////////////////////////////////////////////////////////
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectRtnSmsList", mjsonParams);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectRtnSmsDetail", mjsonParams);
//
//        return objRetParams;
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectRtnLowSms", mjsonParams);
//
//        return objRetParams;
//    }
    
    
    
    
	
//	private JSONObject getParamMoveCount(int moveCountValue) throws TelewebAppException{
//		JSONObject jsonObj = new JSONObject();
//    	jsonObj.put("SUM_MOVE_COUNT", moveCountValue);
//    	return jsonObj;
//	}
	
	
//	private JSONArray makeListToJsonArray(String key, <> value) throws TelewebAppException{
//		JSONArray jsonArray = new JSONArray();
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("tmplClsfIdList", value);
//		jsonArray.add(jsonObject);
//    	return jsonArray;
//	}
    
	
	
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
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateCuttTypeOtherSortOrder", mjsonParams);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateCuttTypeSortOrder", mjsonParams);
//    		}
//    	}else if("DOWN".equals(mjsonParams.getString("ORDER_TYPE"))) {
//    		if(Integer.parseInt(mjsonParams.getString("SORT_ORD")) < Integer.parseInt(mjsonParams.getString("MAX_SORT_ORD"))) {
//    			mjsonParams.setInt("ADD_NUM", -1);
//    			mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD"))+1);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateCuttTypeOtherSortOrder", mjsonParams);
//    			objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateCuttTypeSortOrder", mjsonParams);
//    		}
//    	}
//    	
//    	return objRetParams;
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
//        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "updateRtnSmsMng", mjsonParams);
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
//        objRetParams = twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "deleteRtnSmsMng", mjsonParams);
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
//	        sendNumParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectSendNum", jsonParams);
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
//	            objFileCnt = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "ipccFileCnt", jsonParams);
//	            String cnt = objFileCnt.getString("CNT");
//	            if(Integer.parseInt(cnt) != 0) {
//		            objFileName = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "ipccFileName", jsonParams);
//		            objFilePath = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "ipccFilePath", jsonParams);
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
//        objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "sendSMS", jsonParams);
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
//                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "insertRtnSmsCon", jsonParams);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "multiSendSMS", jsonParams);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "multiSendSMSInq", jsonParams);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "SMSTmpInq", jsonParams);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "SMSInq", jsonParams);
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
//                        objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "SMSMultiSend", objRetCust);
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
//                                objRetParams = twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "insertRtnSmsMultiCon", objRetCust);
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
//        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneSmsTemplateManageMapper", "selectParentPath", mjsonParams);
//
//        return objRetParams;
//	}

}
