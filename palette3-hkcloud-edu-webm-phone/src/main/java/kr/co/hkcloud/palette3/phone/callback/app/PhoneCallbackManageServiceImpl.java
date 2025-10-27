package kr.co.hkcloud.palette3.phone.callback.app;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("phoneCallbackManageService")
public class PhoneCallbackManageServiceImpl implements PhoneCallbackManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 콜백목록 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkMngList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkMngList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 완료처리
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateClbkMng(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 선택한 CLBK_NO
        String clbkNoStr = jsonParams.getString("CLBK_NO");

        String[] clbkNoArr = clbkNoStr.split("/");

        for(int i = 0; i < clbkNoArr.length; i++) {

            jsonParams.setString("CLBK_NO", clbkNoArr[i]);

            //콜백 진행상태 수정
            objRetParams = mobjDao
                .update("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "updateClbkMng", jsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 배분 처리 등록
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertClbkMngDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 배분 처리 등록 한다.
        objRetParams = mobjDao
            .insert("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "insertClbkMngDiv", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 배분 처리 수정
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateClbkMngDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 배분처리를 수정한다.
        objRetParams = mobjDao
            .update("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "updateClbkMngDiv", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백배분정보를 조회한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkDivList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkDivList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백상담원배분정보를 조회한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkDstrDivList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkDstrDivList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백고객정보를 조회한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkCustList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkCustList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백배분을 등록한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertClbkDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objParams = new TelewebJSON();

        String dataStr = jsonParams.getString("ALTMNT_INFO");
//        String totChgCnt = jsonParams.getString("TOT_CHG_CNT");
        String RGTR_ID = jsonParams.getString("USER_ID");

        String[] dataArr = dataStr.split("/");

        for(int i = 0; i < dataArr.length; i++) {

            // 수정할 콜백 유무 확인.
            if(!dataArr[i].equals("") && dataArr[i] != null) {
                String[] dataDtlArr = dataArr[i].split(":");

                String cuslId = dataDtlArr[0];
                String chgCnt = dataDtlArr[1];

                jsonParams.setString("CUSL_ID", cuslId); // 상담원사번
                jsonParams.setString("DIV_CNT", chgCnt);        // 배분 횟수
                jsonParams.setString("ALTMNT_YN", "N");
                jsonParams.setString("RGTR_ID", RGTR_ID);

                objParams = mobjDao
                    .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkCustList", jsonParams);

                if(objParams.getSize() > 0) {

                    for(int z = 0; z < objParams.getSize(); z++) {

                        jsonParams.setString("CLBK_ID", objParams.getString("CLBK_ID", z));

                        //콜백 등록 한다.
                        objRetParams = mobjDao
                            .insert("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "insertClbkDiv", jsonParams);

                        //콜백 배분처리 수정.
                        jsonParams.setString("ALTMNT_YN", "Y");
                        mobjDao
                            .update("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "updateResiClbkCustDivYn", jsonParams);
                    }

                }

            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백미시도를 조회한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectClbkNotEndList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //콜백 목록을 조회한다.
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkNotEndList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백배분을 회수한다.
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteClbkDiv(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objParams = new TelewebJSON();

        // String clbkNoStr = jsonParams.getString("CLBK_NO");
        //String dataStr = jsonParams.getString("DSTR_INFO");
        //String totChgCnt = jsonParams.getString("TOT_CHG_CNT");

        //String[] dataArr = dataStr.split("/");
        // String[] clbkNoArr = clbkNoStr.split("/");

        //for(int i = 0; i < dataArr.length; i++) {

            // 수정할 콜백 유무 확인.
            //if(!dataArr[i].equals("") && dataArr[i] != null) {
                //String[] dataDtlArr = dataArr[i].split(":");

                //String userId = jsonParams.getString('DSTR_CSLT_ID');
                //String chgCnt = dataDtlArr[1];

                //jsonParams.setString("CLBK_NO", clbkNoArr[i]);
                //jsonParams.setString("DSTR_CSLT_ID", userId);    // 상담원ID
                //jsonParams.setString("COL_CNT", chgCnt);           // 회수 횟수

//                objParams = mobjDao.select("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "selectClbkNotEndList", jsonParams);


                //jsonParams.setString("CLBK_NO", objParams.getString("CLBK_NO", z));

        JSONArray jsonArray = jsonParams.getDataObject("CLBK_ALTMNT_INFO");
        JSONArray returnJsonArray = new JSONArray();
        if(jsonArray != null) {
            int intClbkSize = jsonArray.size();
            for(int i = 0; i < intClbkSize; i++) {
                TelewebJSON jsonClbkParams = new TelewebJSON(jsonParams);
                jsonClbkParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                jsonClbkParams.setString("USER_ID", jsonParams.getString("USER_ID"));

                jsonClbkParams.setString("CLBK_ID", jsonArray.getJSONObject(i).getString("CLBK_ID"));
                jsonClbkParams.setString("CUSL_ID", jsonArray.getJSONObject(i).getString("CUSL_ID"));
       
                jsonClbkParams.setString("ALTMNT_YN", "N");

           
                log.debug("@@@@@@@@@@@@@@@@@@@ dsdsdsd"+jsonClbkParams);

		       //콜백 배분처리 수정.
		      mobjDao.update("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "updateClbkCustDivYn", jsonClbkParams);
		             
		      //콜백 배분처리 삭제.
		      objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.callback.dao.PhoneCallbackManageMapper", "deleteClbkDiv", jsonClbkParams);
            }
        }
        //최종결과값 반환
        return objRetParams;
    }

}
