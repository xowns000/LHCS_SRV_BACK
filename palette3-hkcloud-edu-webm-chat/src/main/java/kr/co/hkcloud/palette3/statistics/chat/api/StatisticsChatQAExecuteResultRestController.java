package kr.co.hkcloud.palette3.statistics.chat.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsChatQAExecuteResultRestController",
     description = "QA채팅평가통계 REST 컨트롤러")
public class StatisticsChatQAExecuteResultRestController
{
    private final TwbComDAO mobjDao;


    /**
     * QA통계 조회
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "통계채팅QA평가결과-목록",
                  notes = "통계채팅QA평가결과-목록")
    @PostMapping("/api/statistics/chat/qa-execute-result/list")
    public Object selectRtnQaStsc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParamsL = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParamsM = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                     //반환 파라메터 생성

        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        objParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //정보를 조회한다.
        objRetParamsL = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnLNm", objParams);
        objRetParamsM = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnMNm", objParams);

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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnSts", objParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * QA 유형 조회
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "통계채팅QA평가결과-조회",
                  notes = "통계채팅QA평가결과-조회")
    @PostMapping("/api/statistics/chat/qa-execute-result/inqire")
    public Object selectRtnQaTy(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //필수객체정의
        TelewebJSON objRetParamsL = new TelewebJSON(mjsonParams);				//반환 파라메터 생성
        TelewebJSON objRetParamsM = new TelewebJSON(mjsonParams);				//반환 파라메터 생성
        TelewebJSON objRetParamsH = new TelewebJSON(mjsonParams);				//반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);				//반환 파라메터 생성

        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        objParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //정보를 조회한다.
        objRetParamsL = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnLNm", objParams);
        objRetParamsM = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnMNm", objParams);
        objRetParamsH = mobjDao.select("kr.co.hkcloud.palette3.statistics.chat.dao.StatisticsChatQAExecuteResultMapper", "selectRtnHeaderLNm", objParams);

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
