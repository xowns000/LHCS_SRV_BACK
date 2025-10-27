package kr.co.hkcloud.palette3.statistics.phone.api;


import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.statistics.phone.app.StatisticsPhoneCnsltTyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "StatisticsPhoneCnsltTyRestController",
     description = "상담유형별(유형별)통계 컨트롤러")
public class StatisticsPhoneCnsltTyRestController
{
    private final StatisticsPhoneCnsltTyService statisticsPhoneCnsltTyService;
    public final TwbComDAO                      mobjDao;
    private final HcTeletalkEnvironment         mobjProperty;


    /**
     * 
     * @param  jsonParam
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형별(유형별)건수 통계",
                  notes = "상담유형별(유형별)건수 통계를 조회한다.")
    @PostMapping("/api/statistics/phone/cnslt-ty/inqire")
    public Object selectCnslTypeTypeSttcList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {

        TelewebJSON objParam = new TelewebJSON(jsonParam);
        JSONArray arrObj = jsonParam.getDataObject();
        JSONObject setParam = new JSONObject();

        List<String> arrTelTy = new LinkedList<String>();
        List<String> arrLclsCd = new LinkedList<String>();
        int telCnt = 0;
        int lclsCnt = 0;

        if(!arrObj.isEmpty()) {

            setParam = arrObj.getJSONObject(0);

            //  telCnt = Integer.parseInt(  setParam.getString("TEL_TY_CNT") );
            //  lclsCnt = Integer.parseInt(  setParam.getString("LCLS_CD_CNT") );

            telCnt = setParam.getInt("TEL_TY_CNT");
            lclsCnt = setParam.getInt("LCLS_CD_CNT");

            for(int i = 0; telCnt > 0 && i < telCnt; i++) {
                String telTyCd = setParam.getString("TEL_TY_CODE" + i);
                if(telTyCd != null && !"".equals(telTyCd)) {

                    arrTelTy.add(telTyCd);
                }
            }

            for(int j = 0; lclsCnt > 0 && j < lclsCnt; j++) {
                String lclsCd = setParam.getString("M_CNSL_TY_LCLS" + j);
                if(lclsCd != null && !"".equals(lclsCd)) {

                    arrLclsCd.add(lclsCd);
                }
            }

        }

        jsonParam.setObject("arrTelTy", 0, arrTelTy);
        jsonParam.setObject("arrLclsCd", 0, arrLclsCd);

        objParam = statisticsPhoneCnsltTyService.selectCnslTypeTypeSttcList(jsonParam);

        return objParam;
    }


    @ApiOperation(value = "배치 리스트",
                  notes = "배치 리스트를 조회한다.")
    @PostMapping("/api/statistics/phone/cnslt-ty/batch/list")
    public Object selectRtnBatchList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = statisticsPhoneCnsltTyService.SELECT_TWB_BTCB01_MNG(mjsonParams);

        //배치주기명 설정
        if(objRetParams.getHeaderInt("COUNT") > 0) {
            String[] strBatchSchedule = null;
            String strScheduleKor = "";
            String strMin = "";
            String strHour = "";
            String strDay = "";
            String strMon = "";
            for(int i = 0; i < objRetParams.getDataObject().size(); i++) {
                strBatchSchedule = null;
                strScheduleKor = "";
                strMin = "";
                strHour = "";
                strDay = "";
                strMon = "";
                strBatchSchedule = objRetParams.getString("BATCH_EXECUT_CYCLE", i).split(" ");
                for(int j = strBatchSchedule.length - 1; j > 0; j--) {
                    if(j == 1) {
                        strMin += strBatchSchedule[j].replaceAll(",", "분,");
                        strMin += "분";
                    }
                    else if(j == 2) {
                        if("*".equals(strBatchSchedule[j])) {
                            strHour += "매 시";
                        }
                        else {
                            strHour += strBatchSchedule[j].replaceAll(",", "시,");
                            strHour += "시";
                        }
                    }
                    else if(j == 3) {
                        if("*".equals(strBatchSchedule[j])) {
                            strDay += "매 일";
                        }
                        else {
                            strDay += strBatchSchedule[j].replaceAll(",", "일,");
                            strDay += "일";
                        }
                    }
                    else if(j == 4) {
                        if("*".equals(strBatchSchedule[j])) {
                            strMon += "매 월";
                        }
                        else {
                            strMon += strBatchSchedule[j].replaceAll(",", "월,");
                            strMon += "월";
                        }
                    }
                }
                strScheduleKor = strMon + "/" + strDay + "/" + strHour + "/" + strMin;
                objRetParams.setString("BATCH_EXECUT_CYCLE_NM", i, strScheduleKor);
            }
        }
        return objRetParams;
    }

//    /**
//     * 배치를 동적으로 실행시킨다.
//     * 
//     * @return
//     * @throws TelewebApiException
//     */
//    @ApiOperation(value = "배치 동적 실행",
//                  notes = "배치를 동적으로 실행시킨다.")
//    @PostMapping("/api/statistics/phone/cnslt-ty/batch/execut")
//    public Object processRtnRunBatch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        //client에서 전송받은 파라메터 생성
////        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
//        //반환 파라메터 생성
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        // path 및 javahome path 확인 필요
//        String batclasspath = mobjProperty.getString("batclasspath");
//        String javahome = mobjProperty.getString("javahome");
//        String packagepath = mobjProperty.getString("packagepath");
//
//        log.info("################# packagepath 1111 : " + packagepath);
//
//        /*
//         * // 패키지 경로 강제 설정 //필요시 if( packagepath == null || "".equals( packagepath ) ){
//         * 
//         * packagepath = "";
//         * 
//         * log.info( "################# packagepath 2 : " + packagepath ); }
//         */
//
//        //String strExec = "java -Dfile.encoding=utf-8 " + batclasspath + " " + mjsonParams.getString("BATCH_EXECUT_CLASS") + " " + mjsonParams.getString("BATCH_KEY") + " " + "BATCH" + " " + "NULL";
//        String strExec = "java -Dfile.encoding=utf-8 " + batclasspath + " " + packagepath + "." + mjsonParams
//            .getString("BATCH_EXECUT_CLASS") + " " + mjsonParams.getString("BATCH_KEY") + " " + mjsonParams
//                .getString("BATCH_EXECUT_LC") + " " + mjsonParams.getString("PARAM");
//
//        log.info("################# strExec  : " + strExec);
//
//        Process process = null;
//        String strJavaHome = javahome;
//
//        if(strJavaHome != null && !"".equals(strJavaHome)) {
//            process = Runtime.getRuntime().exec(strExec, null, new File(strJavaHome));
//            log.info("################# plan A process : " + process);
//        }
//        else {
//            process = Runtime.getRuntime().exec(strExec);
//            log.info("################# plan B process : " + process);
//        }
//
//        objRetParams.setHeader("ERROR_FLAG", false);
//        objRetParams.setHeader("ERROR_MSG", "정상처리되었습니다!");
//
//        return objRetParams;
//    }
}
