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
@Service("phoneQAExecutManageService")
public class PhoneQAExecutManageServiceImpl implements PhoneQAExecutManageService
{
    private final TwbComDAO twbComDAO;

    private final InnbCreatCmmnService innbCreatCmmnService;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnExecutList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "selectRtnExecutList", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaDataRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "selectRtnQaDataRst", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON processRtnQaDataRst(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        TelewebJSON qaUpdate = new TelewebJSON(jsonParams);
        JSONArray dataRstList = jsonParams.getDataObject("dataRstList");
        int QA_TA_ST = jsonParams.getInt("QA_TA_ST");
        String QA_MANAGE_YN = jsonParams.getString("QA_MANAGE_YN");
        String QA_DATA_RST_TCOM = "";
        if(dataRstList != null && dataRstList.size() > 0) {
            JSONObject dataRst = null;
            TelewebJSON tempJson = null;
            String QA_DATA_RST_ID = "";
            for(int i = 0; i < dataRstList.size(); i++) {
                tempJson = new TelewebJSON();
                dataRst = dataRstList.getJSONObject(i);
                if(QA_MANAGE_YN.equals("Y")) {
                    if(i == 0) {
                        QA_DATA_RST_TCOM = dataRst.getString("QA_DATA_RST_TCOM");
                    }
                    if(jsonParams.getString("QA_DATA_RST_CN").equals("1")) {
                        QA_DATA_RST_ID = dataRst.getString("QA_DATA_RST_ID_1");
                        //1차
                        if(!dataRst.getString("QA_QS_VE_ID_1").equals("")) {
                            if(QA_TA_ST < 20) {
                                QA_TA_ST = 20;
                            }
                            tempJson.setString("QA_QS_VE_ID", dataRst.getString("QA_QS_VE_ID_1"));
                            tempJson.setString("QA_DATA_ID", dataRst.getString("QA_DATA_ID"));
                            tempJson.setString("QA_DATA_RST_CN", jsonParams.getString("QA_DATA_RST_CN"));
                            tempJson.setString("QA_DATA_RST_SUM", dataRst.getString("QA_DATA_RST_SUM_1"));
                            tempJson.setString("QA_DATA_RST_COM", dataRst.getString("QA_DATA_RST_COM_1"));
//                        tempJson.setString("QA_DATA_RST_TCOM", dataRst.getString("QA_DATA_RST_TCOM"));
                        }
                    }
                    else if(jsonParams.getString("QA_DATA_RST_CN").equals("2")) {
                        QA_DATA_RST_ID = dataRst.getString("QA_DATA_RST_ID_2");
                        //2차
                        if(!dataRst.getString("QA_QS_VE_ID_2").equals("")) {
                            if(QA_TA_ST < 30) {
                                QA_TA_ST = 30;
                            }
                            tempJson.setString("QA_QS_VE_ID", dataRst.getString("QA_QS_VE_ID_2"));
                            tempJson.setString("QA_DATA_ID", dataRst.getString("QA_DATA_ID"));
                            tempJson.setString("QA_DATA_RST_CN", jsonParams.getString("QA_DATA_RST_CN"));
                            tempJson.setString("QA_DATA_RST_SUM", dataRst.getString("QA_DATA_RST_SUM_2"));
                            tempJson.setString("QA_DATA_RST_COM", dataRst.getString("QA_DATA_RST_COM_2"));
//                        tempJson.setString("QA_DATA_RST_TCOM", dataRst.getString("QA_DATA_RST_TCOM"));
                        }
                    }
                    else if(jsonParams.getString("QA_DATA_RST_CN").equals("3")) {
                        QA_DATA_RST_ID = dataRst.getString("QA_DATA_RST_ID_3");
                        //3차
                        if(!dataRst.getString("QA_QS_VE_ID_3").equals("")) {
                            if(QA_TA_ST < 40) {
                                QA_TA_ST = 40;
                            }
                            tempJson.setString("QA_QS_VE_ID", dataRst.getString("QA_QS_VE_ID_3"));
                            tempJson.setString("QA_DATA_ID", dataRst.getString("QA_DATA_ID"));
                            tempJson.setString("QA_DATA_RST_CN", jsonParams.getString("QA_DATA_RST_CN"));
                            tempJson.setString("QA_DATA_RST_SUM", dataRst.getString("QA_DATA_RST_SUM_3"));
                        }
                    }
                }
                else {
                    if(jsonParams.getString("QA_DATA_RST_CN").equals("2")) {
                        QA_DATA_RST_ID = dataRst.getString("QA_DATA_RST_ID_1");
                        tempJson.setString("QA_QS_VE_ID", dataRst.getString("QA_QS_VE_ID_1"));
                        tempJson.setString("QA_DATA_RST_COM_RE", dataRst.getString("QA_DATA_RST_COM_RE_1"));
                    }
                    else if(jsonParams.getString("QA_DATA_RST_CN").equals("3")) {
                        QA_DATA_RST_ID = dataRst.getString("QA_DATA_RST_ID_2");
                        tempJson.setString("QA_QS_VE_ID", dataRst.getString("QA_QS_VE_ID_2"));
                        tempJson.setString("QA_DATA_RST_COM_RE", dataRst.getString("QA_DATA_RST_COM_RE_2"));
                    }
                }

                if(tempJson.getString("QA_QS_VE_ID") != null && !tempJson.getString("QA_QS_VE_ID").equals("") && !tempJson.getString("QA_QS_VE_ID").equals("null")) {
                    log.debug("======================================================");
                    log.debug("QA_QS_VE_ID : " + tempJson.getString("QA_QS_VE_ID"));
                    log.debug("======================================================");
                    if(QA_DATA_RST_ID.equals("")) {
                        QA_DATA_RST_ID = innbCreatCmmnService.getSeqNo("PLT_QA_DATA_RST");
                    }
                    tempJson.setString("LOGIN_ID", jsonParams.getString("LOGIN_ID"));
                    tempJson.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
                    tempJson.setString("QA_DATA_RST_ID", QA_DATA_RST_ID);

                    objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "processRtnQaDataRst", tempJson);
                }
            }

            qaUpdate.setString("QA_ID", jsonParams.getString("QA_ID"));
            qaUpdate.setString("REG_ID", jsonParams.getString("LOGIN_ID"));
            qaUpdate.setString("ASP_NEWCUST_KEY", jsonParams.getString("ASP_NEWCUST_KEY"));
            qaUpdate.setString("QA_TA_ST", String.valueOf(QA_TA_ST));
            objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAPlanManageMapper", "updateRtnQa", qaUpdate);

            if(QA_DATA_RST_TCOM != null && !QA_DATA_RST_TCOM.equals("")) {
                qaUpdate.setString("QA_DATA_ID", jsonParams.getString("QA_DATA_ID"));
                qaUpdate.setString("QA_DATA_RST_TCOM", QA_DATA_RST_TCOM);
                objRetParams = twbComDAO.update("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "updateRtnTcom", qaUpdate);
            }
        }

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaManageYn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "selectRtnQaManageYn", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAdminYn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.phone.qa2.dao.PhoneQAExecutManageMapper", "selectRtnAdminYn", jsonParams);
    }

}
