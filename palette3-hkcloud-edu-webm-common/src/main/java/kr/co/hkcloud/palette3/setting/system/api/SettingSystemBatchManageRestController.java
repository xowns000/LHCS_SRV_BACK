package kr.co.hkcloud.palette3.setting.system.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemBatchManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemBatchManageRestController",
     description = "설정시스템배치관리 REST 컨트롤러")
public class SettingSystemBatchManageRestController
{
    private final SettingSystemBatchManageService settingSystemBatchManageService;


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템배치관리-목록",
                  notes = "설정시스템배치관리 목록을 조회한다.")
    @PostMapping("/api/setting/system/batch-manage/list")
    public Object selectRtnBatchList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = settingSystemBatchManageService.SELECT_PLT_BAT_MNG(mjsonParams);

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

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템배치관리-처리",
                  notes = "설정시스템배치관리 처리 프로세스")
    @PostMapping("/api/setting/system/batch-manage/process")
    public Object processRtnBatch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strTransFlag = mjsonParams.getString("DATA_FLAG");       //화면에서 전송된 플래그 설정

        //신규/수정 상태에 따라 insert, update 함수 호출
        if(TwbCmmnConst.TRANS_INS.equals(strTransFlag)) {
            objRetParams = settingSystemBatchManageService.INSERT_PLT_BAT(mjsonParams);
        }
        else if(TwbCmmnConst.TRANS_UPD.equals(strTransFlag)) {
            objRetParams = settingSystemBatchManageService.UPDATE_PLT_BAT(mjsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * /api/TwbBtcb01/processRtnRunBatch
     * 
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설정시스템배치관리-실행",
                  notes = "배치를 동적으로 실행한다.")
    @PostMapping("/api/setting/system/batch-manage/execut/process")
    public Object processRtnRunBatch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
//        //client에서 전송받은 파라메터 생성
////        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
//        //반환 파라메터 생성
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//
//        String classpath = env.getString("classpath");
//        String javahome = env.getString("javahome");
//
//        String strExec = "java -Dfile.encoding=utf-8 " + classpath + " " + mjsonParams
//            .getString("BATCH_EXECUT_CLASS") + " " + mjsonParams.getString("BATCH_ID");
//        log.debug("strExec ::: {}", strExec);
//        Process process = null;
//        String strJavaHome = javahome;
//
//        if(strJavaHome != null && !"".equals(strJavaHome)) {
//            process = Runtime.getRuntime().exec(strExec, null, new File(strJavaHome));
//        }
//        else {
//            process = Runtime.getRuntime().exec(strExec);
//        }
//
//        objRetParams.setHeader("ERROR_FLAG", false);
//        objRetParams.setHeader("ERROR_MSG", "정상처리되었습니다!");
//
//        return objRetParams;
        return null;
    }
}
