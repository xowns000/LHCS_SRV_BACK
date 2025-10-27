package kr.co.hkcloud.palette3.svy.app;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.FileDbMngMapper;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngDeleteRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest.DeleteFileKeys;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("SvyMakeItemsService")
public class SvyMakeItemsServiceImpl implements SvyMakeItemsService
{
    @Value("${stomp.allow.origin}")
    public String STOMP_ALLOW_ORIGIN;

    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    public final SvyPlanService svyPlanService;
    private final FileDbMngService fileDbMngService;
    private final FileRuleUtils fileRuleUtils;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyMakeItemsMapper";

    /**
     * 설문조사 계획을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectComboMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectComboMakeItems", jsonParams);
    }
    /**
     * 설문그룹을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectGrpListMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao.select(sqlNameSpace, "selectGrpListMakeItems", jsonParams);
        try{
            String strUrl = objParam.getString("SRVY_URL");
            if(!"".equals(strUrl)){
                String[] arrUrl = strUrl.split("key=");
                if(arrUrl.length > 1){
                    String urlEnc = URLEncoder.encode(arrUrl[1], "UTF-8");
                    String strShotUrl = strUrl.substring(0, strUrl.indexOf("=") +1);
                    objParam.setString("SRVY_URL", strShotUrl + urlEnc);
                }
            }
//            objParam.setString("SRVY_URL",URLEncoder.encode(objParam.getString("SRVY_URL"), "UTF-8"));
        }catch (Exception e) {
            throw new TelewebAppException(e);
        }


        return objParam;
    }

    /**
     * 설문보기내용을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChcMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectChcMakeItems", jsonParams);
    }

    /**
     * 설문참여자를 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTrgtList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        jsonParams.setHeader("ROW_CNT", 0);
    	jsonParams.setHeader("PAGES_CNT", 0);
    	
    	//확장 항목 조회 및 설정
    	TelewebJSON srvyExpsnInfo = selectSrvyExpsnAttrList(jsonParams);
    	 JSONArray jsonObj = null;
        if(srvyExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
            jsonObj = srvyExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
            jsonParams.setString("EXPSN_ATTR_LIST", jsonObj.toString());
        }
    	
        objParam = mobjDao.select(sqlNameSpace, "selectTrgtList", jsonParams);
        
        TelewebJSON retParam = new TelewebJSON(jsonParams);
        if(objParam.getSize() > 0){
            String encryptKey = "newSrvySecuriKey";
            JSONArray jsonArray = objParam.getDataObject();
            JSONObject jsonObject = new JSONObject();
            JSONArray oParam = new JSONArray();
            for(int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                try{
                    String KeyEnc = AES256Cipher.encryptString(jsonObject.getString("URL_KEY"), encryptKey);
                    jsonObject.replace("URL_KEY", jsonObject.getString("SRVY_URL") + URLEncoder.encode(KeyEnc, "UTF-8"));

//                    String KeyEnc = AES256Cipher.encryptString(jsonObject.getString("URL_KEY"), encryptKey);
//                    jsonObject.replace("URL_KEY", URLEncoder.encode(KeyEnc, "UTF-8"));
                }catch (Exception e){
                    throw new TelewebAppException(e);
                }
                oParam.add(jsonObject);
            }

            retParam.setDataObject(oParam);
        }

        return objParam;
        //설문조사ID 암호화
        //String srvyIdEnc = AES256Cipher.encryptString(jsonParams.getString("SRVY_ID"), encryptKey);
        //jsonParams.setString("URL", srvyIdEnc);
    }

    /**
     * 웅답설정을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSettingList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectSettingList", jsonParams);
    }

    /**
     * 설문그룹헤더를 저장 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateHeaderMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "updateHeaderMakeItems", jsonParams);
    }

    /**
     * 설문그룹블록을 저장 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON upsertBlockMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        //그룹id값이 없으면 신규
        if("".equals(jsonParams.getString("SRVY_QITEM_GROUP_ID"))){
            jsonParams.setString("SRVY_QITEM_GROUP_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_QITEM_GROUP_ID")));
            objParam = mobjDao.insert(sqlNameSpace, "insertBlockMakeItems", jsonParams);
        }else{
            objParam = mobjDao.update(sqlNameSpace, "updateBlockMakeItems", jsonParams);
        }
        return objParam;
    }

    /**
     * 설문그룹블록을 삭제 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteBlockMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "deleteBlockMakeItems", jsonParams);
    }
    
    
    /**
     * 설문_문항_그룹 - 다음 블록 정보 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON udpateGroupMvmnSrvyQitemGroup(TelewebJSON jsonParam) throws TelewebAppException {
        if(StringUtils.isEmpty(jsonParam.getString("MVMN_SRVY_QITEM_GROUP_ID"))) {
            jsonParam.setString("MVMN_SRVY_QITEM_GROUP_ID", null);
        }
        return mobjDao.update(sqlNameSpace, "udpateGroupMvmnSrvyQitemGroup", jsonParam);
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateBlockSortOrd(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objParam = new TelewebJSON();
        JSONArray jsonArray = jsonParams.getDataObject();
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        TelewebJSON teleObjJson = null;
//      
        for(int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            teleObjJson = new TelewebJSON();
            teleObjJson.setString("SRVY_QITEM_GROUP_ID", jsonObject.getString("SRVY_QITEM_GROUP_ID"));
            teleObjJson.setString("SORT_ORD", jsonObject.getString("SORT_ORD"));
            
            objParam = mobjDao.update(sqlNameSpace, "updateBlockSortOrd", teleObjJson);
        }
        return objParam;
    }
    

    /**
     * 설문아이템을 저장 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON upsertItemsMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        JSONArray jsonArray = jsonParams.getDataObject();
        TelewebJSON telewebJson = new TelewebJSON();

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray itemArray = new JSONArray();
        itemArray.add(jsonObject);
        telewebJson.setDataObject(itemArray);

//        Boolean modFlag = Boolean.parseBoolean(telewebJson.getString("SRVY_QITEM_ID"));
        if("".equals(telewebJson.getString("SRVY_QITEM_ID"))){
            telewebJson.setString("SRVY_QITEM_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_QITEM_ID")));
            objParam = mobjDao.insert(sqlNameSpace, "insertItemsMakeItems", telewebJson);
        }else{
            objParam = mobjDao.update(sqlNameSpace, "updateItemsMakeItems", telewebJson);
        }

        //에러가 아닌경우
        if(!objParam.getHeaderBoolean("ERROR_FLAG")){
            //단일/멀티 일때만 수행
            if(telewebJson.getString("QITEM_TYPE_CD").equals("SNGL") || telewebJson.getString("QITEM_TYPE_CD").equals("MULT")){
                TelewebJSON teleObjJson = null;
                jsonObject = null;
                for(int i = 0; i < jsonArray.size(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    teleObjJson = new TelewebJSON();
                    teleObjJson.setString("QITEM_CHC_CN", jsonObject.getString("QITEM_CHC_CN"));
                    teleObjJson.setString("CSORT_ORD", jsonObject.getString("CSORT_ORD"));
                    teleObjJson.setString("MVMN_SRVY_QITEM_GROUP_ID", StringUtils.isNotEmpty(jsonObject.getString("MVMN_SRVY_QITEM_GROUP_ID")) ? jsonObject.getString("MVMN_SRVY_QITEM_GROUP_ID") : null);
                    teleObjJson.setString("RSPNS_USE_YN", jsonObject.getString("RSPNS_USE_YN"));
                    teleObjJson.setString("SCR", StringUtils.isNotEmpty(jsonObject.getString("SCR")) ? jsonObject.getString("SCR") : null);
                    teleObjJson.setString("SRVY_QITEM_ID", telewebJson.getString("SRVY_QITEM_ID"));
                    teleObjJson.setString("USER_ID", telewebJson.getString("USER_ID"));
                    if("".equals(jsonObject.getString("QITEM_CHC_ID"))){
                        teleObjJson.setString("QITEM_CHC_ID", Integer.toString(innbCreatCmmnService.createSeqNo("QITEM_CHC_ID")));
                        objParam = mobjDao.insert(sqlNameSpace, "insertItemsChcMakeItems", teleObjJson);
                    }else{
                        teleObjJson.setString("QITEM_CHC_ID", jsonObject.getString("QITEM_CHC_ID"));
                        objParam = mobjDao.update(sqlNameSpace, "updateItemsChcMakeItems", teleObjJson);
                    }
                }
            }
        }
        return objParam;
    }

    /**
     * 설문질문을 삭제 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteItemMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        objParam = mobjDao.update(sqlNameSpace, "deleteItemMakeItems", jsonParams);
        if(!objParam.getHeaderBoolean("ERROR_FLAG")){
            objParam = mobjDao.update(sqlNameSpace, "deleteItemChcMakeItems", jsonParams);
        }

        return objParam;
    }

    /**
     * 설문질문항목 삭제 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteItemChcMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "deleteItemChcMakeItems", jsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON copySrvy(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        //설문 헤더 복제
        //헤더 이미지 있는지 체크.
        TelewebJSON objFileParam = mobjDao.select(sqlNameSpace, "selectCopyHeaderFileGroupKey", jsonParams);
        String copyFileGroupKey = objFileParam.getString("FILE_GROUP_KEY");
        log.debug("copySrvy ::: copyFileGroupKey ::: " + copyFileGroupKey);
        if(StringUtils.isNotEmpty(copyFileGroupKey)) {
            jsonParams.setString("FILE_KEY", fileRuleUtils.creatFileKey());
            try {
                //FILE_KEY, FILE_GROUP_KEY 텀을 주기 위함.
                Thread.sleep(50);
            } catch(InterruptedException ex) {

            }
            jsonParams.setString("FILE_GROUP_KEY", fileRuleUtils.creatFileKey());
            objParam = mobjDao.insert(sqlNameSpace, "copySrvyHeaderImage", jsonParams);
        } else {
            jsonParams.setString("FILE_GROUP_KEY", "");
        }
        
        objParam = mobjDao.update(sqlNameSpace, "copySrvyHeader", jsonParams);
        //설문지 복제(설문 그룹, 설문 문항, 설문 문항 선택)
        //설문 그룹 복제
        mobjDao.insert(sqlNameSpace, "copySrvyGroup", jsonParams);
        //블록 이동 정보 업데이트
        mobjDao.update(sqlNameSpace, "copySrvyGroupMvmn", jsonParams);
        
        
        //설문 문항 복제
        mobjDao.insert(sqlNameSpace, "copySrvyQitem", jsonParams);
        //설문 문항 선택 복제
        mobjDao.insert(sqlNameSpace, "copySrvyQitemChc", jsonParams);
        
        //응답 설정 복제
        mobjDao.update(sqlNameSpace, "copySrvyResponseSetting", jsonParams);
        return objParam;
    }

    /**
     * 설문참여자 엑셀 업로드 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON uploadExcelMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON srvyExpsnAttrJSON = selectSrvyExpsnAttrList(jsonParams);
        JSONArray srvyExpsnAttrList = srvyExpsnAttrJSON.getDataObject();
        
        TelewebJSON objParam = new TelewebJSON();
        objParam = mobjDao.update(sqlNameSpace, "deleteExcelMakeItems", jsonParams);
        if(!objParam.getHeaderBoolean("ERROR_FLAG")){
            JSONArray jsonArray = jsonParams.getDataObject();
            JSONObject jsonObject = new JSONObject();
            for(int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                TelewebJSON teleObjJson = new TelewebJSON();
                teleObjJson.setString("SRVY_TRGT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_TRGT_ID")));
                teleObjJson.setString("SRVY_ID", jsonObject.getString("SRVY_ID"));
                teleObjJson.setString("CUST_NM", jsonObject.getString("CUST_NM"));
                teleObjJson.setString("CUST_PHN_NO", jsonObject.getString("CUST_PHN_NO"));
                teleObjJson.setString("EML", jsonObject.getString("EML"));
                teleObjJson.setString("USER_ID", jsonArray.getJSONObject(0).getString("USER_ID"));
                teleObjJson.setString("PP_KEY_PP", jsonArray.getJSONObject(0).getString("PP_KEY_PP"));
                teleObjJson.setString("PP_ALG_PP", jsonArray.getJSONObject(0).getString("PP_ALG_PP"));
                objParam = mobjDao.insert(sqlNameSpace, "uploadExcelMakeItems", teleObjJson);
                
                //확장 항목 저장.
                if(srvyExpsnAttrJSON.getHeaderInt("COUNT") > 0) {
                    JSONArray arrData = new JSONArray();
                    
                    if(srvyExpsnAttrList != null && srvyExpsnAttrList.size() > 0) {
                        for(int eIdx = 0; eIdx < srvyExpsnAttrList.size(); eIdx++) {
                            JSONObject expsnAttr = srvyExpsnAttrList.getJSONObject(eIdx);
                            JSONObject expsnDetail = new JSONObject();
                            expsnDetail.put("ATTR_ID", expsnAttr.getString("ATTR_ID"));
                            expsnDetail.put("ATTR_VL", jsonObject.getString(expsnAttr.getString("EXPSN_ATTR_COL_ID")));
                            arrData.add(expsnDetail);
                        }
                        
                    }
                    JSONObject oCustList = new JSONObject();
                    oCustList.put("DETAIL_LIST", arrData);
                    oCustList.put("SRVY_TRGT_ID", teleObjJson.getString("SRVY_TRGT_ID"));
                    JSONArray oParam = new JSONArray();
                    oParam.add(oCustList);
                    TelewebJSON joParams = new TelewebJSON();
                    joParams.setDataObject(oParam);
                    objParam = mobjDao.insert(sqlNameSpace, "insertSrvyTargetDetail", joParams);
                }
            }
        }

        return objParam;
    }

    /**
     * 설문계획의 설정을 저장 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSettingPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "updateSettingPlan", jsonParams);
    }

    /**
     * 설문지를 게시한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSrvyOpen(TelewebJSON jsonParams) throws TelewebAppException
    {
        try {

            TelewebJSON objParam = new TelewebJSON();
            String encryptKey = "newSrvySecuriKey";

            //설문조사ID 암호화
            TelewebJSON urlKeyObjParam = mobjDao.select(sqlNameSpace, "selectDefaultUrlKey", jsonParams);
            
            String srvyIdEnc = AES256Cipher.encryptString(urlKeyObjParam.getString("URL_KEY"), encryptKey);
            String srvyUrl = STOMP_ALLOW_ORIGIN + "/SVY?key=" + srvyIdEnc;
            jsonParams.setString("URL", srvyUrl);
            
            //게시클릭이라면
            if (jsonParams.getString("PSTG_YN").equals("Y")) {
                //대상지정여부라면
                if (jsonParams.getString("TRGT_YN").equals("Y")) {
                    objParam = mobjDao.select(sqlNameSpace, "selectTrgtYn", jsonParams);
                    if (Integer.parseInt(objParam.getString("CNT")) < 1) {
                        objParam.setHeader("ERROR_FLAG", true);
                        objParam.setHeader("ERROR_MSG", "TRGT");
                        return objParam;
                    }
                }
            }
            log.info("BY_PASS?" + jsonParams);
            objParam = mobjDao.update(sqlNameSpace, "updateSrvyOpen", jsonParams);
            //설문_상태_이력 저장
            objParam = svyPlanService.insertSttsHstry(jsonParams);
            return objParam;
        }catch (Exception e){
            throw new TelewebAppException(e);
        }
    }
    /**
     * 설문아이템의 순서를 변경한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON moveItemMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        JSONArray jsonArray = jsonParams.getDataObject();
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            TelewebJSON teleObjJson = new TelewebJSON();
            teleObjJson.setString("SRVY_QITEM_ID", jsonObject.getString("SRVY_QITEM_ID"));
            teleObjJson.setString("SRVY_QITEM_GROUP_ID", jsonObject.getString("SRVY_QITEM_GROUP_ID"));
            teleObjJson.setString("SORT_ORD", jsonObject.getString("SORT_ORD"));
            objParam = mobjDao.update(sqlNameSpace, "moveItemMakeItems", teleObjJson);
        }
        return objParam;
    }
    /**
     * 암호화테스트(지워야함.)
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON aesUrlEncrypt(TelewebJSON jsonParams) throws TelewebAppException
    {
        try {
            TelewebJSON objParam = new TelewebJSON();
            String encryptKey = "newSrvySecuriKey";
            String strEnc = AES256Cipher.encryptString(jsonParams.getString("SRVY_KEY"), encryptKey);
            objParam.setString("URL", URLEncoder.encode(strEnc, "UTF-8"));

            return objParam;
        }catch (Exception e){
            throw new TelewebAppException(e);
        }
    }
    /**
     * 헤더에 이미지를 저장 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateImgMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "updateImgMakeItems", jsonParams);
    }
    
    /**
     * 헤더 이미지를 삭제한다.
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteHeaderImgMakeItems(TelewebJSON jsonParams) throws TelewebAppException {
        fileDbMngService.deleteFileGroup(jsonParams);
        
        return mobjDao.update(sqlNameSpace, "deleteHeaderImgMakeItems", jsonParams);
    }


    /**
     * 설문참여자 개별추가 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON uploadSingleMakeItems(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        
        if(jsonParams.getString("SRVY_TRGT_ID").equals("") || jsonParams.getString("SRVY_TRGT_ID") == null) {
            jsonParams.setString("SRVY_TRGT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_TRGT_ID")));
            objParam = mobjDao.insert(sqlNameSpace, "uploadExcelMakeItems", jsonParams);
        } else {
            objParam = mobjDao.update(sqlNameSpace, "updateExcelMakeItems", jsonParams);
        }
        
        //확장 항목 삭제 후 저장.
        if(StringUtils.isNotEmpty(jsonParams.getString("EXPSN_ATTR_CNT")) && jsonParams.getInt("EXPSN_ATTR_CNT") > 0) {
            objParam = mobjDao.insert(sqlNameSpace, "deleteSrvyTargetDetail", jsonParams);
            JSONArray arrData = jsonParams.getDataObject();
            JSONObject oCustList = new JSONObject();
            oCustList.put("DETAIL_LIST", arrData);
            oCustList.put("SRVY_TRGT_ID", jsonParams.getString("SRVY_TRGT_ID"));
            JSONArray oParam = new JSONArray();
            oParam.add(oCustList);
            TelewebJSON joParams = new TelewebJSON();
            joParams.setDataObject(oParam);
            objParam = mobjDao.insert(sqlNameSpace, "insertSrvyTargetDetail", joParams);
        }

        return objParam;
    }


    /**
     * 설문참여자를 삭제한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON delTrgt(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON retParam = new TelewebJSON();
        
    	JSONArray jsonArray = jsonParams.getDataObject("SRVY_TRGT_LIST");
        log.info("??" + jsonArray);
        if(jsonArray != null) {
            int srvyTrgtSize = jsonArray.size();
            for(int i = 0; i < srvyTrgtSize; i++) {
                TelewebJSON jsonTrgtParams = new TelewebJSON(jsonParams);
                jsonTrgtParams.setString("SRVY_TRGT_ID", jsonArray.getJSONObject(i).getString("SRVY_TRGT_ID"));
                jsonTrgtParams.setString("SRVY_ID", jsonArray.getJSONObject(i).getString("SRVY_ID"));
                
                jsonTrgtParams.setString("USER_ID", jsonParams.getString("USER_ID"));

                retParam = mobjDao.update(sqlNameSpace, "delTrgt", jsonTrgtParams);
            }
        }

        return retParam;
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSrvyExpsnAttrList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        jsonParams.setString("SE", "SRVY");
        objRetParams = mobjDao.select(sqlNameSpace, "selectSrvyExpsnAttrList", jsonParams);
        return objRetParams;
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectPossibleCopyYn(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectPossibleCopyYn", jsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteForceSrvyItem(TelewebJSON jsonParams) throws TelewebAppException {
    	mobjDao.update(sqlNameSpace, "deleteForceQitemGroup", jsonParams);
    	mobjDao.update(sqlNameSpace, "deleteForceQitem", jsonParams);
    	TelewebJSON objRetParams = mobjDao.update(sqlNameSpace, "deleteForceQitemChc", jsonParams);
    	return objRetParams;
    }
}
