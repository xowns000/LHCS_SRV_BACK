package kr.co.hkcloud.palette3.phone.campaign.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignCustMngServiceImpl;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("PhoneCampaignAltmntMngService")
public class PhoneCampaignAltmntMngServiceImpl implements PhoneCampaignAltmntMngService
{
    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    public final PhoneCampaignCustMngServiceImpl phoneCampaignCustMngServiceImpl;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignAltmntMngMapper";

    /**
     * 캠페인 TOP리스트를 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTopCpiAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectTopCpiAltmntMng", jsonParams);
    }
    /**
     * 등록된 상담직원을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCuslAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectCuslAltmntMng", jsonParams);
    }
    /**
     * 대상자를 상담원에게 배분한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCuslAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        JSONArray jsonArray = jsonParams.getDataObject();
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            TelewebJSON teleObjJson = new TelewebJSON();

            teleObjJson.setString("CUSL_ID", jsonObject.getString("CUSL_ID"));
            teleObjJson.setString("CPI_ID", jsonObject.getString("CPI_ID"));
            teleObjJson.setString("ADDRE", jsonObject.getString("ADDRE"));
            objParam = mobjDao.insert(sqlNameSpace, "insertCuslAltmntMng", teleObjJson);
        }

        return objParam;
    }
    /**
     * 제외사유를 변경한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateExlAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update(sqlNameSpace, "updateExlAltmntMng", jsonParams);
    }
    /**
     * 상담사에게 할당된 미시도를 회수한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON returnCuslAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete(sqlNameSpace, "returnCuslAltmntMng", jsonParams);
    }
    /**
     * 배분된 정보를 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        TelewebJSON custExpsnInfo = phoneCampaignCustMngServiceImpl.custExpsnInfo(jsonParams);

        List<String> arrAttrColId = new LinkedList<String>();
        if(custExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {

            JSONArray jsonObj = custExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
            for (int i = 0; i < jsonObj.size(); i++) {
                JSONObject objData = jsonObj.getJSONObject(i);

                @SuppressWarnings("rawtypes")
                Iterator it = objData.keys();
                while(it.hasNext()) {
                    String strKey = (String) it.next();
                    String strValue = objData.getString(strKey);

                    if(strKey.indexOf("EXPSN_ATTR_COL_ID") > -1 && StringUtils.isNotEmpty(strValue)) {
                        arrAttrColId.add(strValue);
                    }
                }
            }
        }
        if(arrAttrColId.size() > 0) {
            jsonParams.setObject("arrAttrColId", 0, arrAttrColId);
        }

        return mobjDao.select(sqlNameSpace, "selectCustAltmntMng", jsonParams);
    }

    /**
     * 미처리건수를 인수인계 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON tkoverCuslAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        //선택한 상담사의 현재 상태 체크
        objRetParams = mobjDao.select(sqlNameSpace, "chkCustExlAltmntMng", jsonParams);
        if(objRetParams.getSize() > 0){
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "EXL");
            return objRetParams;
        }
        //상담사 없는경우 insert
        objRetParams = mobjDao.insert(sqlNameSpace, "addCuslAltmntMng", jsonParams);
        if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            //인계시작
            objRetParams = mobjDao.insert(sqlNameSpace, "tkoverCuslAltmntMng", jsonParams);
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                //인계한 데이터 삭제
                objRetParams = mobjDao.delete(sqlNameSpace, "deleteCuslAltmntMng", jsonParams);
            }
        }
        return objRetParams;
    }

    /**
     * 상담원을 변경 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON chgCuslAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        TelewebJSON joParams = new TelewebJSON(jsonParams);
        joParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
        joParams.setString("BFR_CUSL_ID", jsonParams.getString("BFR_CUSL_ID"));
        joParams.setString("AFTR_CUSL_ID", jsonParams.getString("AFTR_CUSL_ID"));
        joParams.setString("USER_ID", jsonParams.getString("USER_ID"));
        //선택한 상담사의 현재 상태 체크
        objRetParams = mobjDao.select(sqlNameSpace, "chkCustExlAltmntMng", joParams);
        if(objRetParams.getSize() > 0){
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "EXL");
            return objRetParams;
        }
        //상담사 없는경우 insert
        objRetParams = mobjDao.insert(sqlNameSpace, "addCuslAltmntMng", jsonParams);
        if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            //변경시작
            JSONArray arrData = jsonParams.getDataObject();
            JSONObject oCustList = new JSONObject();
            oCustList.put("CUST_LIST", arrData);
            oCustList.put("CPI_ID", jsonParams.getString("CPI_ID"));
            oCustList.put("BFR_CUSL_ID", jsonParams.getString("BFR_CUSL_ID"));
            oCustList.put("AFTR_CUSL_ID", jsonParams.getString("AFTR_CUSL_ID"));

            JSONArray oParam = new JSONArray();
            oParam.add(oCustList);
            joParams = new TelewebJSON(jsonParams);
            joParams.setDataObject(oParam);

            objRetParams = mobjDao.insert(sqlNameSpace, "chgCuslAltmntMng", joParams);
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                //변경한 데이터 삭제
                objRetParams = mobjDao.delete(sqlNameSpace, "deleteChgCuslAltmntMng", joParams);
            }
        }

        return objRetParams;
    }
    /**
     * 확장속성관리-목록
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON expsnAttrList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "expsnAttrList", jsonParams);
    }
    /**
     * 개별로 고객을 추가한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON addIndiCustAltmntMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        TelewebJSON joParams = new TelewebJSON(jsonParams);
        joParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
        joParams.setString("CUST_NM", jsonParams.getString("CUST_NM"));
        joParams.setString("CUST_PHN_NO", jsonParams.getString("CUST_PHN_NO"));
        joParams.setString("AFTR_CUSL_ID", jsonParams.getString("AFTR_CUSL_ID"));
        joParams.setString("USER_ID", jsonParams.getString("USER_ID"));
        joParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        //폰번호로 이미 있는 고객인지 체크
        objRetParams = mobjDao.select(sqlNameSpace, "chkCustAltmntMng", joParams);
        //값이 있다면
        if(objRetParams.getSize() > 0){
            //캠페인 고객정보에 이미있다면 리턴
            if(0 < Integer.parseInt(objRetParams.getString("OBD_CUST_ID"))){
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "OBD");
                return objRetParams;
            }else{
                //고객마스터에 있다면 셋팅
                if(0 < Integer.parseInt(objRetParams.getString("CUST_ID"))){
                    joParams.setString("CUST_ID", objRetParams.getString("CUST_ID"));
                }else{
                    //없다면 고객마스터에 저장
                    joParams.setString("CUST_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CUST_ID")));
                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignCustMngMapper", "insertCustMtCustMng", joParams);
                }
            }
        }else{
            //없다면 고객마스터에 저장
            joParams.setString("CUST_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CUST_ID")));
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignCustMngMapper", "insertCustMtCustMng", joParams);
        }
        if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            //캠페인 고객 저장
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignCustMngMapper", "insertCustCustMng", joParams);
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                //상담사 저장
                objRetParams = mobjDao.insert(sqlNameSpace, "addCuslAltmntMng", joParams);
                if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                    //배분 저장
                    objRetParams = mobjDao.insert(sqlNameSpace, "addCustAltmntMng", joParams);
                }
                if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                    //속성저장
                    JSONArray arrData = jsonParams.getDataObject();
                    JSONObject oCustList = new JSONObject();
                    oCustList.put("CUST_LIST", arrData);
                    oCustList.put("CPI_ID", joParams.getString("CPI_ID"));
                    oCustList.put("CUST_ID", joParams.getString("CUST_ID"));
                    oCustList.put("CUST_NM", joParams.getString("CUST_NM"));
                    oCustList.put("CUST_PHN_NO", joParams.getString("CUST_PHN_NO"));

                    JSONArray oParam = new JSONArray();
                    oParam.add(oCustList);
                    joParams = new TelewebJSON(jsonParams);
                    joParams.setDataObject(oParam);
                    
                    int iExpsnCnt = 0;
                    for (int i = 0; i < arrData.size(); i++) {
                    	JSONObject objData = arrData.getJSONObject(i);
                    	
                    	@SuppressWarnings("rawtypes")
                        Iterator it = objData.keys();
                        while(it.hasNext()) {
                            String strKey = (String) it.next();
                            String strValue = objData.getString(strKey);

                            if(strKey.indexOf("ATTR_ID") > -1 && StringUtils.isNotEmpty(strValue)) iExpsnCnt++;
                        }
                    }

                    if(iExpsnCnt > 0) objRetParams = mobjDao.insert(sqlNameSpace, "addIndiCustAltmntMng", joParams);
                }
            }
        }
        return objRetParams;
    }
    
    /**
     * 캠페인 이력 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cpiStatHistList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "cpiStatHistList", jsonParams);
    }

    /**
     * 상담원별 캠페인 상태
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslCampaignMonitor(TelewebJSON jsonParams) throws TelewebAppException
    {
    	return mobjDao.select(sqlNameSpace, "cuslCampaignMonitor", jsonParams);
    }

    /**
     * 상담원별 처리결과별 상태
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslCpiDtlMonitor(TelewebJSON jsonParams) throws TelewebAppException
    {
    	return mobjDao.select(sqlNameSpace, "cuslCpiDtlMonitor", jsonParams);
    }
}
