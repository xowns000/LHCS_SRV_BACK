package kr.co.hkcloud.palette3.phone.lm.app;


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
@Service("phoneLmManageService")
public class PhoneLmManageServiceImpl implements PhoneLmManageService
{
    private final TwbComDAO            twbComDAO;
    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmsEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmEva", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException
    {

        return twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmEva", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        /* 평가지 사용여부 체크 로직 추가 필요 */
        /* 평가지 사용중일 경우 수정/삭제 불가능 */
        objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "updateRtnLmEva", jsonParams);

        return objRetParams;

    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopyLmEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        /* 평가지 복사 시 평가 상세도 같이 복사 진행 */
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        JSONArray copyArray = jsonParams.getDataObject();

        if(copyArray != null && copyArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < copyArray.size(); i++) {
                JSONObject jsonObject = copyArray.getJSONObject(i);
                String NEW_LM_EVA_ID = innbCreatCmmnService.getSeqNo("PLT_LM_EVA");
                telewebJson = new TelewebJSON();
                telewebJson.setString("LM_EVA_ID", jsonObject.getString("LM_EVA_ID"));
                telewebJson.setString("NEW_LM_EVA_ID", NEW_LM_EVA_ID);
                telewebJson.setString("REG_ID", jsonParams.getString("REG_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                //평가지 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnCopyLmEva", telewebJson);
                //평가 상세 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnCopyLmEvaRst", telewebJson);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnLmEva(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        /* 평가지 사용여부 체크 로직 추가 필요 */
        /* 평가지 사용중일 경우 수정/삭제 불가능 */
        JSONArray delArray = jsonParams.getDataObject();
        if(delArray != null && delArray.size() > 0) {
            TelewebJSON telewebJson = new TelewebJSON();
            for(int i = 0; i < delArray.size(); i++) {
                JSONObject jsonObject = delArray.getJSONObject(i);
                telewebJson.setString("LM_EVA_ID", jsonObject.getString("LM_EVA_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmEva", telewebJson);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmQs", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON lmVeParam = new TelewebJSON(jsonParams);
        TelewebJSON lmVeDelParam = new TelewebJSON(jsonParams);

        //문항 저장
        objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmQs", jsonParams);

        JSONArray lmVeList = jsonParams.getDataObject("lmVeList");
        JSONArray lmVeDelList = jsonParams.getDataObject("delLmVeList");

        //문항보기 삭제
        if(lmVeDelList != null && lmVeDelList.size() > 0) {
            JSONObject lmVeDelObj = null;

            for(int i = 0; i < lmVeDelList.size(); i++) {
                lmVeDelObj = lmVeDelList.getJSONObject(i);

                lmVeDelParam.setString("LM_QS_VE_ID", lmVeDelObj.getString("LM_QS_VE_ID"));
                lmVeDelParam.setString("LM_QS_ID", lmVeDelObj.getString("LM_QS_ID"));
                lmVeDelParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));

                twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmVe", lmVeDelParam);

                //정답일 경우 정답 삭제 로직 추가 필요
                twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmQsAns", lmVeDelParam);

            }
        }

        //문항보기 저장
        if(lmVeList != null && lmVeList.size() > 0) {
            JSONObject lmVeObj = null;
            String LM_QS_VE_ID = "";

            for(int i = 0; i < lmVeList.size(); i++) {
                lmVeObj = lmVeList.getJSONObject(i);

                LM_QS_VE_ID = lmVeObj.getString("LM_QS_VE_ID");
                if(LM_QS_VE_ID.equals("")) {
                    LM_QS_VE_ID = innbCreatCmmnService.getSeqNo("PLT_LM_VE");
                }

                lmVeParam.setString("LM_QS_VE_ID", LM_QS_VE_ID);
                lmVeParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                lmVeParam.setString("LM_QS_ID", jsonParams.getString("LM_QS_ID"));
                lmVeParam.setString("LM_QS_VE_RT", lmVeObj.getString("LM_QS_VE_RT"));
                lmVeParam.setString("REG_ID", jsonParams.getString("REG_ID"));

                twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmVe", lmVeParam);

                if(lmVeObj.getString("LM_ANS_YN").equals("Y")) {
                    twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmQsAns", lmVeParam);
                }
                else if(lmVeObj.getString("LM_ANS_YN").equals("N")) {
                    twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmQsAns", lmVeParam);
                }
            }

        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCopyLmQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON lmVeParams = new TelewebJSON(jsonParams);
        TelewebJSON lmVeList = new TelewebJSON(jsonParams);

        JSONArray copyArray = jsonParams.getDataObject();

        if(copyArray != null && copyArray.size() > 0) {
            TelewebJSON copyParam = null;
            for(int i = 0; i < copyArray.size(); i++) {
                JSONObject copyObj = copyArray.getJSONObject(i);
                String NEW_LM_QS_ID = innbCreatCmmnService.getSeqNo("PLT_LM_QS");
                copyParam = new TelewebJSON();
                copyParam.setString("NEW_LM_QS_ID", NEW_LM_QS_ID);
                copyParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                copyParam.setString("LM_QS_ID", copyObj.getString("LM_QS_ID"));
                copyParam.setString("REG_ID", jsonParams.getString("REG_ID"));

                //문항 복사
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnCopyLmQs", copyParam);
                //보기 복사
                lmVeList = twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmVe", copyParam);

                if(lmVeList != null && lmVeList.getSize() > 0) {
                    JSONObject temp = null;
                    JSONArray arrExtractRemove = lmVeList.getDataObject();
                    TelewebJSON copyAnsParam = null;
                    for(int j = 0; j < arrExtractRemove.size(); j++) {
                        String NEW_LM_QS_VE_ID = innbCreatCmmnService.getSeqNo("PLT_LM_VE");;
                        lmVeParams.setString("NEW_LM_QS_ID", NEW_LM_QS_ID);
                        temp = arrExtractRemove.getJSONObject(j);
                        lmVeParams.setString("LM_QS_VE_ID", temp.getString("LM_QS_VE_ID"));
                        lmVeParams.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                        lmVeParams.setString("LM_QS_ID", copyObj.getString("LM_QS_ID"));
                        lmVeParams.setString("NEW_LM_QS_VE_ID", NEW_LM_QS_VE_ID);
                        lmVeParams.setString("REG_ID", jsonParams.getString("REG_ID"));

                        twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnCopyLmVe", lmVeParams);

                        //정답일 경우 정답 복사
                        if(temp.getString("LM_ANS_YN").equals("Y")) {
                            copyAnsParam = new TelewebJSON();
                            copyAnsParam.setString("LM_QS_ID", NEW_LM_QS_ID);
                            copyAnsParam.setString("LM_QS_VE_ID", NEW_LM_QS_VE_ID);
                            copyAnsParam.setString("REG_ID", jsonParams.getString("REG_ID"));
                            copyAnsParam.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                            twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmQsAns", copyAnsParam);
                        }
                    }
                }
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnLmQs(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        JSONArray deleteArray = jsonParams.getDataObject();
        if(deleteArray != null && deleteArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < deleteArray.size(); i++) {
                JSONObject jsonObject = deleteArray.getJSONObject(i);
                telewebJson = new TelewebJSON();
                telewebJson.setString("LM_QS_ID", jsonObject.getString("LM_QS_ID"));
                telewebJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                //평가문항 사용여부 체크 로직필요
                //사용 시 수정/삭제 불가능
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmQs", telewebJson);

                //평가문항 삭제 시 보기도 같이 삭제처리
                objRetParams = twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmVe", telewebJson);

                //정답 삭제

            }
        }
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmVe(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmVe", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLmEvaRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmEvaRst", jsonParams);
    }


    @Override
    public TelewebJSON insertRtnLmEvaRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON objLmEvaRst = null;

        twbComDAO.delete("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "deleteRtnLmEvaRst", jsonParams);

        JSONArray lmEvaRstList = jsonParams.getDataObject("evlRstList");

        if(lmEvaRstList != null && lmEvaRstList.size() > 0) {
            JSONObject lmEvaRst = null;
            for(int i = 0; i < lmEvaRstList.size(); i++) {
                objLmEvaRst = new TelewebJSON(jsonParams);
                lmEvaRst = lmEvaRstList.getJSONObject(i);
                objLmEvaRst.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                objLmEvaRst.setString("LM_EVA_ID", jsonParams.getString("LM_EVA_ID"));
                objLmEvaRst.setString("LM_QS_ID", lmEvaRst.getString("LM_QS_ID"));
                objLmEvaRst.setString("LM_EVA_RST_OD", lmEvaRst.getString("LM_EVA_RST_OD"));
                objLmEvaRst.setString("REG_ID", jsonParams.getString("REG_ID"));
                objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "insertRtnLmEvaRst", objLmEvaRst);
            }
        }

        return objRetParams;
    }


    @Override
    public TelewebJSON selectRtnLmPreView(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmPreView", jsonParams);
    }


    @Override
    public TelewebJSON selectRtnLmVePreView(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.lm.dao.PhoneLmManageMapper", "selectRtnLmVePreView", jsonParams);
    }

}
