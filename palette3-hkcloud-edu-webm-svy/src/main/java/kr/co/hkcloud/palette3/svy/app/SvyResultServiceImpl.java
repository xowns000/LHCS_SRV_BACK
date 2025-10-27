package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service("SvyResultService")
public class SvyResultServiceImpl implements SvyResultService
{
    public final TwbComDAO mobjDao;
    public final SvyMakeItemsService svyMakeItemsService;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyResultMapper";

    /**
     * 설문지 메인항목을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSummyList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectSummyList", jsonParams);
    }
    /**
     * 분석결과 항목 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSummyItem(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        JSONArray arrData = jsonParams.getDataObject();
        JSONObject oTypeList = new JSONObject();
        oTypeList.put("LIST_TYPE", arrData);
        oTypeList.put("SRVY_ID", jsonParams.getString("SRVY_ID"));
        oTypeList.put("CNT", jsonParams.getString("CNT"));
        oTypeList.put("F_DATE", jsonParams.getString("F_DATE"));
        oTypeList.put("S_DATE", jsonParams.getString("S_DATE"));
        oTypeList.put("E_DATE", jsonParams.getString("E_DATE"));

        JSONArray oParam = new JSONArray();
        oParam.add(oTypeList);
        objParam.setDataObject(oParam);

        objParam = mobjDao.select(sqlNameSpace, "selectSummyItem", objParam);

        return objParam;
    }
    /**
     * 분석결과 항목을 자세히 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectDetailList(TelewebJSON jsonParams) throws TelewebAppException
    {
      //확장 항목 조회 및 설정
        TelewebJSON srvyExpsnInfo = svyMakeItemsService.selectSrvyExpsnAttrList(jsonParams);
         JSONArray jsonObj = null;
        if(srvyExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
            jsonObj = srvyExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
            jsonParams.setString("EXPSN_ATTR_LIST", jsonObj.toString());
        }
        return mobjDao.select(sqlNameSpace, "selectDetailList", jsonParams);
    }
}
