package kr.co.hkcloud.palette3.setting.env.app;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * packageName : kr.co.hkcloud.palette3.setting.env.api
 * fileName : SettingEnvServiceImpl
 * author : USER
 * date : 2023-11-01
 * description : << 여기 설명 >>
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023-11-01 USER 최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingEnvService")
public class SettingEnvServiceImpl implements SettingEnvService {

    private final TwbComDAO mobjDao;
    private final SettingCustomerInformationListService settingCustomerInformationListService;

    /**
     * 고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등) 조회
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON custcoSettingList(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.env.dao.SettingEnvMapper", "custcoSettingList", jsonParams);

        return objRetParams;
    }

    /**
     * 고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등) 조회
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON selectSettingEnv(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성
        int iRowCnt = jsonParams.getHeaderInt("ROW_CNT");
        int iPagesCnt = jsonParams.getHeaderInt("PAGES_CNT");
        jsonParams.setString("SE", "ENV"); //고객사 환경설정 확장속성

        TelewebJSON custcoExpsnInfo = settingCustomerInformationListService.custcoExpsnInfo(jsonParams);

        JSONArray jsonObj = new JSONArray();
        if (custcoExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
            jsonObj = custcoExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
        }

        if (jsonObj.size() > 0) {
            //            jsonParams.setObject("arrAttrColId", 0, arrAttrColId);
            JSONObject expsnAttrObj = new JSONObject();
            expsnAttrObj.put("EXPSN_ATTR_LIST", jsonObj);

            expsnAttrObj.put("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
            expsnAttrObj.put("USER_ID", jsonParams.getString("USER_ID"));

            JSONArray arrParam = new JSONArray();
            arrParam.add(expsnAttrObj);

            jsonParams.setDataObject(arrParam);

            jsonParams.setHeader("ROW_CNT", 0);
            jsonParams.setHeader("PAGES_CNT", 0);

            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.env.dao.SettingEnvMapper", "selectSettingEnv", jsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "확장항목 정보 없음");
        }
        objRetParams.setDataObject("EXPSN_ATTR", custcoExpsnInfo);

        return objRetParams;
    }

    /**
     * 고객사별 환경설정(템플릿변경, 고객핵심정보, 콜밸, 예약콜 등등) 변경처리
     * 
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    @Override
    public TelewebJSON updateSettingEnv(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.env.dao.SettingEnvMapper", "updateSettingEnv", jsonParams);
    }
}
