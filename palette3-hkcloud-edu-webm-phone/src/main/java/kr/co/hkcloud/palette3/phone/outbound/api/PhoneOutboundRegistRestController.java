package kr.co.hkcloud.palette3.phone.outbound.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.profile.enumer.PaletteProfiles;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.main.app.PhoneMainService;
import kr.co.hkcloud.palette3.phone.outbound.app.PhoneOutboundRegistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneOutboundRegistRestController",
     description = "아웃바운드접수조회 REST 컨트롤러")
public class PhoneOutboundRegistRestController
{
    private final PaletteProfileUtils        paletteProfileUtils;
    private final PhoneOutboundRegistService phoneOutboundRegistService;
    private final PhoneMainService   phoneMainService; // 전화상담메인 서비스 

    /**
     * 아웃바운드 등록한다.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 등록",
                  notes = "아웃바운드 등록")
    @PostMapping("/phone-api/outbound/regist/process")
    public Object insertObndReg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetKey = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);   //반환 파라메터 생성
        TelewebJSON obndCustParams = new TelewebJSON(mjsonParams);

        // FORM(DATA)데이터
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
        String regMan = mjsonParams.getString("REG_MAN");                   //화면에서 전송된 플래그 설정
        String chngMan = mjsonParams.getString("CHNG_MAN");                   //화면에서 전송된 플래그 설정

        jsonParams.setString("REG_MAN", regMan);
        jsonParams.setString("CHNG_MAN", chngMan);
        jsonParams.setString("DATA_FLAG", strTransFlag);
        JSONArray obndCustArr = mjsonParams.getDataObject("OBND_CUST_LIST");
        jsonParams.setDataObject("OBND_CUST_ARR", obndCustArr);

        //캠페인 등록
        objRetParams = phoneOutboundRegistService.insertObndReg(jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 명 중복 체크.
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "아웃바운드 중복체크",
                  notes = "아웃바운드 중복체크")
    @PostMapping("/phone-api/outbound/regist/obnd/inqire")
    public Object selectObndCheck(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //아웃바운드 중복검색
        objRetParams = phoneOutboundRegistService.selectObndCheck(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

//    /**
//     * 아웃바운드 엑셀업로드 한다.
//     * 
//     * @param  inHashMap
//     * @return           TelewebJSON 형식의 처리 결과 데이터
//     */
//    @ApiOperation(value = "아웃바운드 엑셀업로드",
//                  notes = "아웃바운드 엑셀업로드")
//    @PostMapping("/phone-api/outbound/regist/excel/regist")
//    public Object excelObndReg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        // 센터구분 ( 공제 G 업무 E)
//        String centTyStr = mjsonParams.getString("CENT_TY");
//
//        //OutputStream objFileOutStream
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
//        TelewebJSON objRetFileParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
//        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
//        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
//        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
//
//        // 엑셀파일 JSON 변환
//        if(jsonParams.getDataObject(TwbCmmnConst.G_DATA) != null && jsonParams.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {
//            if(jsonParams.containsKey("EXCEL_UPLOAD_FILE") && !"".equals(jsonParams.containsKey("EXCEL_UPLOAD_FILE"))) {
//                objRetFileParams = getListFileTmp(jsonParams);
//            }
//        }
//
//        // 헤더값 세팅
//        objRetFileParams.setHeader("COUNT", objRetFileParams.getSize());
//        objRetFileParams.setHeader("TOT_COUNT", objRetFileParams.getSize());
//
//        // 중복 고객 확인.
//        List<String> CUST_ARR = new LinkedList<String>();
//        for(int i = 0; i < objRetFileParams.getSize(); i++) {
//            if(centTyStr.equals("G")) {
//                CUST_ARR.add(objRetFileParams.getString("CUST_NO", i));
//            }
//            else if(centTyStr.equals("E")) {
//                CUST_ARR.add(objRetFileParams.getString("RES_NO", i));
//            }
//        }
//        jsonParams.setObject("CUST_ARR", 0, CUST_ARR);
//
//        objRetParams = phoneOutboundRegistService.selectObndCustCheck(jsonParams);
//        String msg = "";
//        if(objRetParams.getSize() > 0) {
////            // 중복이 있는 경우
////            // 메시지
//            for(int i = 0; i < objRetParams.getSize(); i++) {
//                msg += objRetParams.getString("CUST_NM", i);
//                msg += "(" + objRetParams.getString("CUST_NO", i) + ")<br />";
//            }
//            objRetParams.setHeader("TELEWEB_IDENTIFIER", "중복데이터가 있습니다");
//            objRetParams.setHeader("ERROR_FLAG", true);
//            objRetParams.setHeader("ERROR_MSG", msg);
//        }
//        else {
//
//            //공통
//            /*
//             * CENT_TY//센터구분 IFLW_TY//추출01 엑셀02 REG_MAN //등록자 CHNG_MAN // 수정자
//             */
//            // 공통 값 세팅
//            for(int i = 0; i < objRetFileParams.getSize(); i++) {
//                objRetFileParams.setString("CENT_TY", i, centTyStr); // 센터구분
//                objRetFileParams.setString("IFLW_TY", i, mjsonParams.getString("IFLW_TY"));      //추출01 엑셀02
//                objRetFileParams.setString("REG_MAN", i, mjsonParams.getString("REG_MAN"));
//                objRetFileParams.setString("CHNG_MAN", i, mjsonParams.getString("CHNG_MAN"));
//            }
//
//            // 아웃바운드 저장
//            if(centTyStr.equals("G")) {
//                // 공제
//                /*
//                 * CUST_NO // 고객번호 INGNO // 인가번호 MOBIL_NO // 핸드폰 HOUSE_TEL_NO // 집전화 CO_TEL_NO // 회사전화 TEL_TY // 전화구분 IN_DATE // 인입일자 UPDT_DATE // 갱신일자 CUST_NM // 고객명 REM // 비고 SECU_NO // 증권번호 JOB_DTL_NM //작업상세명
//                 */
//                //objRetParams = phoneOutboundRegistService.insertExcelObndReg(objRetFileParams);
//
//            }
//            else if(centTyStr.equals("E")) {
//
//                // 업무
//                /*
//                 * IN_DATE // 인입일자 RES_NO // 주민번호 CUST_NM // 고객명 MOBIL_NO // 핸드폰번호 CARD_NO // 카드번호 ACT_NO // 계좌번호 CUST_NO // 조합원번호 REF1 // 참조1 REF2 // 참조2 REF3 // 참조3 REF4 // 참조4 REF5 // 참조5 REM // 비고
//                 */
//                //objRetParams = phoneOutboundRegistService.insertExcelObndRegE(objRetFileParams);
//            }
//
//        }
//
////        return objRetParams;
//        return objRetFileParams;
//    }
//
//
//    /*
//     * 엑셀 업로드
//     * 
//     */
//    private TelewebJSON getListFileTmp(TelewebJSON jsonParams) throws TelewebApiException
//    {
//
//        String fileOrgNm = jsonParams.getString("FILENAME_ORG");
//        String fileNm = jsonParams.getString("FILENAME");
//        String fileExt = jsonParams.getString("FILE_EXT");
//        String pathDir = jsonParams.getString("PATH_DIR");
//        String startRowNo = jsonParams.getString("START_ROW_NO");
//        String startSheetNo = jsonParams.getString("SHEET_NO");
//
//        int intStartRowNo = 1;
//        int intStartSheetNo = 0;
//
//        if(!"".equals(startRowNo)) {
//            intStartRowNo = Integer.parseInt(startRowNo);
//        }
//
//        if(!"".equals(startSheetNo)) {
//            intStartSheetNo = Integer.parseInt(startSheetNo);
//        }
//        TelewebJSON objRtnParams = new TelewebJSON(jsonParams);     //반환파라메터생성
//
//        String strRepositoryPath = excelProperties.getRepository().getTempDir().toString();         //Repository Path
//
//        // 파일 경로
//        excelUtils.loadExcel(pathDir + "/" + fileNm);
//        excelUtils.setSheet(intStartSheetNo);
//
//        // 공제단말 G 업무지원 E
//        String centTyStr = jsonParams.getString("CENT_TY");
//        // 공제단말 (순번 주민번호    계약자명    핸드폰번호   자택전화번호  직장전화번호  증서번호 )
//        String[] keys = {"NO", "CUST_NO", "CUST_NM", "IFLW_TY",
//            //  "INGNO", /* 인가번호 */
//            "MOBIL_NO", "CO_TEL_NO", "HOUSE_TEL_NO",
//            //  "TEL_TY", /* 전화번호구분 */
//            "SECU_NO", "JOB_DTL_NM",
//            //  "IN_DATE", /* 인입일자  */
//            "SALE_START_DATE", "UPDT_DATE", "REM"};
//
//        excelUtils.setKeys(keys);
//
//        JSONArray objArry = new JSONArray();
//        JSONObject objJson = new JSONObject();
//
//        for(int i = intStartRowNo; i < excelUtils.getRowCount(); i++) {
//
//            objJson = (JSONObject) excelUtils.getRowData(i + 1, keys.length).getDataObject().get(0);
//
//            objArry.add(objJson);
//        }
//        objRtnParams.setDataObject(objArry);
//
//        return objRtnParams;
//    }
}
