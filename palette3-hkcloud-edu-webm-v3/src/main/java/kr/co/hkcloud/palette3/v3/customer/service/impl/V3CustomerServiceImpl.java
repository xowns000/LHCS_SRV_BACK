package kr.co.hkcloud.palette3.v3.customer.service.impl;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.v3.customer.service.V3CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * << 여기에 설명. >>
 *
 * @author KJD
 * @version 1.0
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-01        KJD       최초 생성
 * </pre>
 * @since 2023-12-01
 */
@Slf4j
@RequiredArgsConstructor
@Service("v3CustomerService")
public class V3CustomerServiceImpl implements V3CustomerService {

    private final TwbComDAO mobjDao;

    /**
     *  고객사별 고객정보 확장속성
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON selectCustcoExpsnInfo(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        jsonParams.setHeader("ROW_CNT", 0);
        jsonParams.setHeader("PAGES_CNT", 0);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "custcoExpsnInfo",
            jsonParams);

        return objRetParams;
    }

    @Override
    public TelewebJSON selectCustomer(TelewebJSON jsonParams) throws TelewebAppException {
        return null;
    }

    @Override
    public TelewebJSON selectCustomerList(TelewebJSON jsonParams) throws TelewebAppException {

        log.debug("jsonParams ====================   " + jsonParams);

        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        TelewebJSON custcoExpsnInfo = this.selectCustcoExpsnInfo(jsonParams);
        JSONArray jsonExpsnObj = custcoExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);

        if (jsonExpsnObj.size() > 0) {
            JSONObject expsnAttrObj = new JSONObject();
            expsnAttrObj.put("EXPSN_ATTR_LIST", jsonExpsnObj);
            expsnAttrObj.put("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
            expsnAttrObj.put("USER_ID", jsonParams.getString("USER_ID"));
            expsnAttrObj.put("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
            expsnAttrObj.put("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));

            expsnAttrObj.put("SCH_CUST_ID", jsonParams.getString("SCH_CUST_ID"));
            expsnAttrObj.put("SCH_CUST_STAT", jsonParams.getString("SCH_CUST_STAT"));
            expsnAttrObj.put("SCH_GB", jsonParams.getString("SCH_GB"));
            expsnAttrObj.put("SCH_KEYWORD", jsonParams.getString("SCH_KEYWORD"));
            expsnAttrObj.put("SCH_CUST_REG_TP", jsonParams.getString("SCH_CUST_REG_TP"));
            expsnAttrObj.put("SCH_ST_DTS", jsonParams.getString("SCH_ST_DTS"));
            expsnAttrObj.put("SCH_END_DTS", jsonParams.getString("SCH_END_DTS"));

            JSONArray arrParam = new JSONArray();
            arrParam.add(expsnAttrObj);
            jsonParams.setDataObject(arrParam);

            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper",
                "selectRtnCustInfoPop", jsonParams);
            if (!objRetParams.getDataObject(TwbCmmnConst.G_DATA).isEmpty()) {
                JSONArray jsonObj = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
                for (int i = 0; i < jsonObj.size(); i++) {

                    JSONObject objData = jsonObj.getJSONObject(i);
                    TelewebJSON param = new TelewebJSON();
                    param.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                    param.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
                    param.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
                    param.setString("CUST_ID", objData.getString("CUST_ID"));

                    objData.put("CUST_DETAIL_DATA",
                        (mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "custSelect",
                            param)).getDataObject(TwbCmmnConst.G_DATA));

                    objData.put("CUST_EXPSN_INFO",
                        (mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "custExpsnInfoSelect",
                            param)).getDataObject(TwbCmmnConst.G_DATA));
                }
            }
            objRetParams.setDataObject("EXPSN_ATTR", custcoExpsnInfo);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "조회된 정보 없음");
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON selectCuttHistoryList(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "cuttHistList", jsonParams);
        JSONArray jsonObj = objRetParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);
            TelewebJSON param = new TelewebJSON();
            param.setString("CUSTCO_ID",jsonParams.getString("CUSTCO_ID"));
            param.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
            param.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
            param.setString("PHN_CUTT_ID",objData.getString("PHN_CUTT_ID"));
            objData.put("CUTT_EXPSN_INFO",
                (mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "expsnAttrList",
                    param)).getDataObject(TwbCmmnConst.G_DATA));
        }
        objRetParams.setDataObject("EXPSN_ATTR", mobjDao.select("kr.co.hkcloud.palette3.v3.customer.dao.V3CustomerMapper", "expsnAttrList", jsonParams));
        return objRetParams;
    }
}
