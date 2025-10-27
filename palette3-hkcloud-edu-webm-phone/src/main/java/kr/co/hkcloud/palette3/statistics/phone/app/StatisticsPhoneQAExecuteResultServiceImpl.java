package kr.co.hkcloud.palette3.statistics.phone.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("statisticsPhoneQAExecuteResultService")
public class StatisticsPhoneQAExecuteResultServiceImpl implements StatisticsPhoneQAExecuteResultService
{

    public final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnLNm(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.StatisticsPhoneQAExecuteResultMapper", "selectRtnLNm", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMNm(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.StatisticsPhoneQAExecuteResultMapper", "selectRtnMNm", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnSts(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.StatisticsPhoneQAExecuteResultMapper", "selectRtnSts", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHeaderLNm(TelewebJSON jsonParam) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.statistics.phone.StatisticsPhoneQAExecuteResultMapper", "selectRtnHeaderLNm", jsonParam);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaStsc(TelewebJSON mjsonParams) throws TelewebAppException
    {

        //client에서 전송받은 파라메터 생성
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //필수객체정의
        TelewebJSON objRetParamsL = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParamsM = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        objParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        //정보를 조회한다.
        objRetParamsL = selectRtnLNm(objParams);
        objRetParamsM = selectRtnMNm(objParams);

        String ColNm = "";
        String LCol = "";
        String MCol = "";
        String EVAL_CNS = "";
        if(objRetParamsL.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < objRetParamsL.getHeaderInt("TOT_COUNT"); i++) {
                LCol += ", SUM(CASE WHEN QA_TY_L = '" + objRetParamsL.getString("QA_TY_L", i) + "' THEN QA_TY_S_P ELSE 0 END) AS L_" + i;
                ColNm += ", L_" + i;
            }
        }

        if(objRetParamsM.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < objRetParamsM.getHeaderInt("TOT_COUNT"); i++) {
                MCol += ", SUM(CASE WHEN QA_TY_M = '" + objRetParamsM.getString("QA_TY_M", i) + "' THEN QA_TY_S_P ELSE 0 END) AS M_" + i;
                ColNm += ", M_" + i;
            }
        }

        if(objRetParamsM.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < objRetParamsM.getHeaderInt("TOT_COUNT"); i++) {
                EVAL_CNS += ", MAX(CASE WHEN QA_TY_M = '" + objRetParamsM.getString("QA_TY_M", i) + "' THEN EVAL_CN ELSE ' ' END) AS E_" + i;
                ColNm += ", E_" + i;
            }
        }

        objParams.setString("ColNm", ColNm);
        objParams.setString("LCol", LCol);
        objParams.setString("MCol", MCol);
        objParams.setString("EVAL_CNS", EVAL_CNS);

        //정보를 조회한다.
        objRetParams = selectRtnSts(objParams);

        //최종결과값 반환
        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnQaTy(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //client에서 전송받은 파라메터 생성
//      TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //필수객체정의
        TelewebJSON objRetParamsL = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParamsM = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParamsH = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        objParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        //objParams.setHeader("POOL_NM", "TeleTalkMysqlDs"); //poolNm
        //정보를 조회한다.
        objRetParamsL = selectRtnLNm(objParams);
        objRetParamsM = selectRtnMNm(objParams);
        objRetParamsH = selectRtnHeaderLNm(objParams);

        if(objRetParamsL.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < objRetParamsL.getHeaderInt("TOT_COUNT"); i++) {
                objRetParamsL.setString("COL_MODEL", i, "L_" + i);
            }
        }

        if(objRetParamsM.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < objRetParamsM.getHeaderInt("TOT_COUNT"); i++) {
                objRetParamsM.setString("COL_MODEL", i, "M_" + i);
                objRetParamsM.setString("COL_MODEL2", i, "E_" + i);
            }
        }

        //정보를 조회한다.
        objRetParams.setDataObject("TY_LCD", objRetParamsL);
        objRetParams.setDataObject("TY_MCD", objRetParamsM);
        objRetParams.setDataObject("M_HEADER", objRetParamsH);

        //최종결과값 반환
        return objRetParams;
    }

}
