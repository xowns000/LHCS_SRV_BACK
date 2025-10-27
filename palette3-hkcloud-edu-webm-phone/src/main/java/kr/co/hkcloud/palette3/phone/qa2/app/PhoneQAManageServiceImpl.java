package kr.co.hkcloud.palette3.phone.qa2.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneQAManageService")
public class PhoneQAManageServiceImpl implements PhoneQAManageService
{

    private final TwbComDAO twbComDAO;

    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaEva", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnQaEva", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        /* 평가지 사용여부 체크 로직 추가 필요 */
        /* 평가지 사용중일 경우 수정/삭제 불가능 */
        objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "updateRtnQaEva", jsonParams);

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException
    {

        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        /* 평가지 사용여부 체크 로직 추가 필요 */
        /* 평가지 사용중일 경우 수정/삭제 불가능 */
        JSONArray delArray = jsonParams.getDataObject();
        if(delArray != null && delArray.size() > 0) {
            TelewebJSON telewebJson = new TelewebJSON();
            for(int i = 0; i < delArray.size(); i++) {
                JSONObject jsonObject = delArray.getJSONObject(i);
                telewebJson.setString("QA_EVA_ID", jsonObject.getString("QA_EVA_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "deleteRtnQaEva", telewebJson);

            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopyQaEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        /* 평가지 복사 시 평가 상세도 같이 복사 진행 */
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        JSONArray copyArray = jsonParams.getDataObject();

        if(copyArray != null && copyArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < copyArray.size(); i++) {
                JSONObject jsonObject = copyArray.getJSONObject(i);
                String NEW_QA_EVA_ID = innbCreatCmmnService.getSeqNo("PLT_QA_EVA");
                telewebJson = new TelewebJSON();
                telewebJson.setString("QA_EVA_ID", jsonObject.getString("QA_EVA_ID"));
                telewebJson.setString("NEW_QA_EVA_ID", NEW_QA_EVA_ID);
                telewebJson.setString("REG_ID", jsonParams.getString("REG_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                //평가지 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnCopyQaEva", telewebJson);
                //평가 상세 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnCopyEvaRst", telewebJson);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaQs", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON qaVeParam = new TelewebJSON(jsonParams);
        TelewebJSON qaVeDelParam = new TelewebJSON(jsonParams);

        //문항 저장
        objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnQaQs", jsonParams);

        JSONArray qaVeList = jsonParams.getDataObject("qaVeList");
        JSONArray qaVeDelList = jsonParams.getDataObject("delQaVeList");

        //문항보기 삭제
        if(qaVeDelList != null && qaVeDelList.size() > 0) {
            JSONObject qaVeDelObj = null;

            for(int i = 0; i < qaVeDelList.size(); i++) {
                qaVeDelObj = qaVeDelList.getJSONObject(i);

                qaVeDelParam.setString("QA_QS_VE_ID", qaVeDelObj.getString("QA_QS_VE_ID"));
                qaVeDelParam.setString("QA_QS_ID", qaVeDelObj.getString("QA_QS_ID"));
                qaVeDelParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));

                twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "deleteRtnQaVe", qaVeDelParam);
            }
        }

        log.debug("qaVeList size :" + qaVeList.size());
        //문항보기 저장
        if(qaVeList != null && qaVeList.size() > 0) {
            JSONObject qaVeObj = null;
            String QA_QS_VE_ID = "";

            for(int i = 0; i < qaVeList.size(); i++) {
                qaVeObj = qaVeList.getJSONObject(i);

                QA_QS_VE_ID = qaVeObj.getString("QA_QS_VE_ID");
                if(QA_QS_VE_ID.equals("")) {
                    QA_QS_VE_ID = innbCreatCmmnService.getSeqNo("PLT_QA_VE");
                }

                qaVeParam.setString("QA_QS_VE_ID", QA_QS_VE_ID);
                qaVeParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                qaVeParam.setString("QA_QS_ID", jsonParams.getString("QA_QS_ID"));
                qaVeParam.setString("QA_QS_VE_RT", qaVeObj.getString("QA_QS_VE_RT"));
                qaVeParam.setString("QA_QS_VE_SC", qaVeObj.getString("QA_QS_VE_SC"));
                qaVeParam.setString("QA_QS_VE_OD", qaVeObj.getString("QA_QS_VE_OD"));
                qaVeParam.setString("REG_ID", jsonParams.getString("REG_ID"));

                twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnQaVe", qaVeParam);

            }

        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        JSONArray deleteArray = jsonParams.getDataObject();
        if(deleteArray != null && deleteArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < deleteArray.size(); i++) {
                JSONObject jsonObject = deleteArray.getJSONObject(i);
                telewebJson = new TelewebJSON();
                telewebJson.setString("QA_QS_ID", jsonObject.getString("QA_QS_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                //평가문항 사용여부 체크 로직필요
                //사용 시 수정/삭제 불가능
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "deleteRtnQaQs", telewebJson);

                //평가문항 삭제 시 보기도 같이 삭제처리
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "deleteRtnQaVe", telewebJson);
            }
        }
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopyQaQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON qaVeParams = new TelewebJSON(jsonParams);
        TelewebJSON qaVeList = new TelewebJSON(jsonParams);

        JSONArray copyArray = jsonParams.getDataObject();

        if(copyArray != null && copyArray.size() > 0) {
            TelewebJSON copyParam = null;
            for(int i = 0; i < copyArray.size(); i++) {
                JSONObject copyObj = copyArray.getJSONObject(i);
                String NEW_QA_QS_ID = innbCreatCmmnService.getSeqNo("PLT_QA_QS");
                copyParam = new TelewebJSON();
                copyParam.setString("NEW_QA_QS_ID", NEW_QA_QS_ID);
                copyParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                copyParam.setString("QA_QS_ID", copyObj.getString("QA_QS_ID"));
                copyParam.setString("REG_ID", jsonParams.getString("REG_ID"));

                //문항 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnCopyQaQs", copyParam);
                //보기 복사
                qaVeList = twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaVe", copyParam);

                if(qaVeList != null && qaVeList.getSize() > 0) {
                    JSONObject temp = null;
                    JSONArray arrExtractRemove = qaVeList.getDataObject();
                    for(int j = 0; j < arrExtractRemove.size(); j++) {
                        String NEW_QA_QS_VE_ID = innbCreatCmmnService.getSeqNo("PLT_QA_VE");;
                        qaVeParams.setString("NEW_QA_QS_VE_ID", NEW_QA_QS_VE_ID);
                        temp = arrExtractRemove.getJSONObject(j);
                        qaVeParams.setString("QA_QS_VE_ID", temp.getString("QA_QS_VE_ID"));
                        qaVeParams.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                        qaVeParams.setString("QA_QS_ID", copyObj.getString("QA_QS_ID"));
                        qaVeParams.setString("NEW_QA_QS_ID", NEW_QA_QS_ID);
                        qaVeParams.setString("REG_ID", jsonParams.getString("REG_ID"));

                        twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnCopyQaVe", qaVeParams);
                    }
                }
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaVe(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaVe", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaEvaRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaEvaRst", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnQaEvaRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objQaEvaRst = new TelewebJSON(jsonParams);

        twbComDAO.delete("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "deleteRtnQaEvaRst", jsonParams);

        JSONArray qaEvaRstList = jsonParams.getDataObject("evlRstList");

        if(qaEvaRstList != null && qaEvaRstList.size() > 0) {
            JSONObject qaEvaRst = null;
            for(int i = 0; i < qaEvaRstList.size(); i++) {
                qaEvaRst = qaEvaRstList.getJSONObject(i);
                objQaEvaRst.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                objQaEvaRst.setString("QA_EVA_ID", jsonParams.getString("QA_EVA_ID"));
                objQaEvaRst.setString("QA_QS_ID", qaEvaRst.getString("QA_QS_ID"));
                objQaEvaRst.setString("QA_EVA_RST_OD", qaEvaRst.getString("QA_EVA_RST_OD"));
                objQaEvaRst.setString("REG_ID", jsonParams.getString("REG_ID"));
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "insertRtnQaEvaRst", objQaEvaRst);
            }
        }

//        updateRtnQaEvaSum

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnQaPreView(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaPreView", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnQaVePreView(TelewebJSON jsonParams) throws TelewebAppException
    {
        log.debug("jsonParams :" + jsonParams.toString());
//        log.debug("QA_QS_LIST:" + jsonParams.getDataObject("QA_QS_LIST").toString());
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAManageMapper", "selectRtnQaVePreView", jsonParams);
    }

}
