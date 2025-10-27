package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
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
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
@RequiredArgsConstructor
@Service("SvyPreviewService")
public class SvyPreviewServiceImpl implements SvyPreviewService
{
    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyPreviewMapper";

    /**
     * 설문지 메인항목을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectMainList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        TelewebJSON objTrgt = new TelewebJSON();
        objParam = mobjDao.select(sqlNameSpace, "selectMainList", jsonParams);

        //데이터가있다면
        if(objParam.getSize() > 0){
            //미리보기가 아니라면
            if(!Boolean.parseBoolean(jsonParams.getString("VFLAG"))){
                //종료된 설문인지 확인한다.
                if(objParam.getString("STTS_CD").equals("TERMIAT")){
                    objParam.setHeader("ERROR_FLAG", true);
                    objParam.setHeader("ERROR_MSG", "CLS");
                    return objParam;
                }
                //목표인원이있다면
                if(objParam.getString("GOAL_PSNAL_DSGN_YN").equals("Y")){
                    objTrgt = mobjDao.select(sqlNameSpace, "selectTrgtFinishCnt", jsonParams);
                    if(Integer.parseInt(objParam.getString("GOAL_PSNAL")) <= Integer.parseInt(objTrgt.getString("CNT"))){
                        objParam.setHeader("ERROR_FLAG", true);
                        objParam.setHeader("ERROR_MSG", "PSN");
                        return objParam;
                    }
                }
                objTrgt = mobjDao.select(sqlNameSpace, "selectTrgtList", jsonParams);
                //대상자지정인경우 대상자가있는지 확인한다.
                if(objParam.getString("TRGT_DSGN_YN").equals("Y")){
                    if(objTrgt.getSize() == 0){    // 대상자 없으면 오류
                        objParam.setHeader("ERROR_FLAG", true);
                        objParam.setHeader("ERROR_MSG", "BAD");
                        return objParam;
                    }
                }
                //참여한 설문인지 확인한다.
                if(objTrgt.getSize() > 0 && !objTrgt.getString("SRVY_RSPNS_DT").equals("")) {
                    objParam.setHeader("ERROR_FLAG", true);
                    objParam.setHeader("ERROR_MSG", "ALR");
                    return objParam;
                }
                //설문 기간인지 확인한다.
                if(objParam.getString("SRVY_PERIOD_YN").equals("N")) {
                    objParam.setHeader("ERROR_FLAG", true);
                    objParam.setHeader("ERROR_MSG", "PRD");
                    return objParam;
                }
            }            
        }else{  //없으면 에러
            objParam.setHeader("ERROR_FLAG", true);
            objParam.setHeader("ERROR_MSG", "BAD");
            return objParam;
        }

        return objParam;
    }

    /**
     * 개인정보수집동의를 한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON upsertTerms(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        TelewebJSON objTrgt = new TelewebJSON();

        objTrgt = mobjDao.select(sqlNameSpace, "selectTrgtTermList", jsonParams);
        //참여한 설문인지 확인한다.
        if(objTrgt.getSize() > 0 && !objTrgt.getString("SRVY_RSPNS_DT").equals("")) {
            objParam.setHeader("ERROR_FLAG", true);
            objParam.setHeader("ERROR_MSG", "ALR");
            return objParam;
        }

        if(objTrgt.getSize() > 0){
            objParam = mobjDao.update(sqlNameSpace, "updateTerms", jsonParams);
            objParam.setString("SRVY_TRGT_ID", objTrgt.getString("SRVY_TRGT_ID"));
        }else{
            jsonParams.setString("SRVY_TRGT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_TRGT_ID")));
            objParam = mobjDao.insert(sqlNameSpace, "insertTerms", jsonParams);
            objParam.setString("SRVY_TRGT_ID", jsonParams.getString("SRVY_TRGT_ID"));
        }

        return objParam;
    }
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertItem(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();
        //입력했을 경우만 처리
        if(Integer.parseInt(jsonParams.getString("SRVY_QITEM_ID")) > -1){
            //멀티의 key구조로 인해 삭제후 인서트 방식으로 바꾼다.
            if(jsonParams.getString("CHC_YN").equals("Y")){
                objParam = mobjDao.delete(sqlNameSpace, "deleteRpnsChc", jsonParams);
            }else{
                objParam = mobjDao.delete(sqlNameSpace, "deleteRpnsDscrt", jsonParams);
            }
            String noRspnsYn = jsonParams.getString("NO_RSPNS_YN");
            //응답값이 있으면 저장.
            if("N".equals(noRspnsYn)) {
                //오류가없다면,
                if(!objParam.getHeaderBoolean("ERROR_FLAG")){
                    JSONArray jsonArray = jsonParams.getDataObject();
                    JSONObject jsonObject = new JSONObject();
                    for(int i = 0; i < jsonArray.size(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        TelewebJSON teleObjJson = new TelewebJSON();
    
                        teleObjJson.setString("SRVY_TRGT_ID", jsonObject.getString("SRVY_TRGT_ID"));
                        teleObjJson.setString("SRVY_QITEM_ID", jsonObject.getString("SRVY_QITEM_ID"));
                        teleObjJson.setString("RSPNS_CN", jsonObject.getString("RSPNS_CN"));
                        if(jsonObject.getString("CHC_YN").equals("Y")){
                            teleObjJson.setString("QITEM_CHC_ID", jsonObject.getString("QITEM_CHC_ID"));
                            objParam = mobjDao.insert(sqlNameSpace, "insertRpnsChc", teleObjJson);
                        }else{
                            objParam = mobjDao.insert(sqlNameSpace, "insertRpnsDscrt", teleObjJson);
                        }
                    }
                }
            }
        }
        //마지막이면
        if(jsonParams.getString("FINISH").equals("Y")){
            objParam = mobjDao.update(sqlNameSpace, "updateSrvyFinish", jsonParams);
        }
        return objParam;
    }

    /**
     * url복호화.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON aesUrlDecrypt(TelewebJSON jsonParams) throws TelewebAppException
    {
        try {
            TelewebJSON objParam = new TelewebJSON();
//            String strUrl = URLDecoder.decode(jsonParams.getString("URL"), "UTF-8");
            String strUrl = jsonParams.getString("URL");

            //복호화
            String encryptKey = "newSrvySecuriKey";
            String strRetValue = AES256Cipher.decryptString(strUrl, encryptKey);

            String[] arrParm = strRetValue.split("_");
            if(arrParm.length > 0){
            	for(int i=0; i<arrParm.length; i++){
            		if(i == 0){
            			objParam.setString("SRVY_ID", arrParm[i]);
            		}else if(i == 1){
            			objParam.setString("SRVY_TRGT_ID", arrParm[i]);
            		}else if(i == 2){
            			objParam.setString("UUID", arrParm[i]);
            		}
            	}
            }else{
            	objParam.setString("SRVY_TRGT_ID", "-1");
                objParam.setHeader("ERROR_FLAG", true);
                objParam.setHeader("ERROR_MSG", "BAD");
                return objParam;
            }
            return objParam;
        }catch (Exception e){
            throw new TelewebAppException(e);
        }

    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteGroupRspns(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objParam = new TelewebJSON();
        mobjDao.delete(sqlNameSpace, "deleteGroupRspnsQitemChc", jsonParam);
        mobjDao.delete(sqlNameSpace, "deleteGroupRspnsDscrt", jsonParam);
        
        return objParam;
    }
}
