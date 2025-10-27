package kr.co.hkcloud.palette3.setting.customer.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.excel.api.ExcelDownController;
import kr.co.hkcloud.palette3.excel.app.ExcelService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingCustomerInformationListRestController", description = "설정고객정보목록  REST 컨트롤러")
public class SettingCustomerInformationListRestController {

    private final SettingCustomerInformationListService settingCustomerInformationListService;
    private final ExcelService excelService;

    /**
     *
     */
    @ApiOperation(value = "설정고객정보목록-페이지목록", notes = "설정고객정보목록 페이지목록을 조회한다")
    @PostMapping("/api/setting/customer/info-list/page/inqire")
    public Object selectRtnPageCustInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingCustomerInformationListService.selectRtnPageCustInfo(mjsonParams);
    }

    /**
     *
     */
    @ApiOperation(value = "고객정보 삽입", notes = "고객정보 삽입.")
    @PostMapping("/api/setting/customer/info-list/regist")
    public Object insertUserInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingCustomerInformationListService.insertUserInfo(mjsonParams);
    }

    /**
     *
     */
    @ApiOperation(value = "설정고객정보목록-중복체크조회", notes = "설정고객정보목록 중복되는지 조회한다")
    @PostMapping("/api/setting/customer/info-list/dplct-ceck/inqire")
    public Object selectRtnCustInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingCustomerInformationListService.selectRtnCustInfo(mjsonParams);
    }

    /**
     * 고객사 확장항목 조회
     */
    @ApiOperation(value = "고객사 확장항목 조회", notes = "고객 리스트 조회 시 고객사 확장항목을 조회")
    @PostMapping("/api/setting/customer/info-expsn/expsninfo-inqire/inqire")
    public Object custcoExpsnInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String custcoId = mjsonParams.getString("CUSTCO_ID");        // 고객사키`

        objRetParams.setString("CUSTCO_ID", custcoId);                // 고객사키

        objRetParams.setDataObject("EXPSN_ATTR", settingCustomerInformationListService.custcoExpsnInfo(mjsonParams));

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객검색 팝업 조회
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @PrvcAspectAnotation(value = "CUST_LIST")   //개인정보 Logging관련
    @ApiOperation(value = "고객검색 팝업 조회", notes = "고객검색 팝업 조회")
    @PostMapping("/api/setting/customer/info-list/cstmrinfo-inqire-popup/inqire")
    public Object selectRtnComCust(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String custcoId = mjsonParams.getString("CUSTCO_ID");        // 고객사키
        String custId = mjsonParams.getString("CUST_ID");            // 고객ID
        String custNm = mjsonParams.getString("CUST_NM");            // 고객명
        String custPhnNo = mjsonParams.getString("CUST_PHN_NO");    // 전화번호
        String custSttsCd = mjsonParams.getString("CUST_STTS_CD");    // 고객상태

        objRetParams.setString("CUSTCO_ID", custcoId);                // 고객사키
        objRetParams.setString("CUST_ID", custId);                    // 고객ID
        objRetParams.setString("CUST_NM", custNm);                    // 고객명
        objRetParams.setString("CUST_PHN_NO", custPhnNo);            // 전화번호
        objRetParams.setString("CUST_STTS_CD", custSttsCd);         // 고객상태

        objRetParams = settingCustomerInformationListService.selectRtnCustInfoPop(mjsonParams);

        //Impl에서 추가함.
        //objRetParams.setDataObject("EXPSN_ATTR", settingCustomerInformationListService.custcoExpsnInfo(mjsonParams));

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객 전화번호 체크
     */
    @ApiOperation(value = "고객 전화번호 체크", notes = "고객 전화번호를 체크 한다.")
    @PostMapping("/api/setting/customer/custTelNoCheck")
    public Object custTelNoCheck(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.custTelNoCheck(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 동일 전화번호 존재 시 채팅 키를 통해 통합 가능 고객 판단
     */
    @ApiOperation(value = "동일 전화번호 존재 시 채팅 키를 통해 통합 가능 고객 판단", notes = "동일 전화번호 존재 시 채팅 키를 통해 통합 가능 고객 판단")
    @PostMapping("/api/setting/customer/chtCustDuplChk")
    public Object chtCustDuplChk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.chtCustDuplChk(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객정보 저장, 수정
     */
    @PrvcAspectAnotation(value = "CUST_REG,CUST_MOD") //개인정보 Logging관련
    @ApiOperation(value = "고객정보 저장, 수정", notes = "고객 정보를 저장,수정 한다.")
    @PostMapping("/api/setting/customer/custProc")
    public Object custProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.custProc(mjsonParams); //고객 정보 저장

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객 동의 이력 저장, 수정
     */
    @ApiOperation(value = "고객 동의 이력 저장, 수정", notes = "고객 동의 이력을 저장,수정 한다.")
    @PostMapping("/api/setting/customer/custAgreHstryProc")
    public Object custAgreHstryProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.custAgreHstryProc(mjsonParams); //고객 정보 저장

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 주의고객 정보 저장, 수정
     */
    @ApiOperation(value = "주의고객 정보 저장, 수정", notes = "주의고객 정보를 저장,수정 한다.")
    @PostMapping("/api/setting/customer/cautionCustProc")
    public Object cautionCustProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        if (StringUtils.isBlank(mjsonParams.getString("CAUTION_CUST_RGTR_ID"))) {
            mjsonParams.setString("CAUTION_CUST_STAT", "REG");
        } else {
            mjsonParams.setString("CAUTION_CUST_STAT", "MOD");
        }

        objRetParams = settingCustomerInformationListService.custMod(mjsonParams); //고객 정보 수정

        objRetParams.setDataObject(TwbCmmnConst.G_DATA, ((TelewebJSON) this.custDefaultInfo(mjsonParams)).getDataObject("DATA"));

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객검색 조회 Artrhur.Kim 2021.10.14
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "고객검색 조회", notes = "고객검색 조회")
    @PostMapping("/api/setting/customer/info-list/cstmrinfo-inqire/inqire")
    public Object selectRtnComCustInqire(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        String camId = mjsonParams.getString("CAM_ID");

        //아웃바운드 정보 조회
        objRetParams = settingCustomerInformationListService.selectRtnComCustInqire(mjsonParams);

        if (objRetParams.getSize() == 0) {

            if (camId != null && !camId.isEmpty() && camId.length() > 0) {

                objRetParams = settingCustomerInformationListService.selectRtnComObdCustInqire(mjsonParams);
            }
        }

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 채팅/전화메인 고객검색 버튼 조회
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "채팅/전화메인 고객검색 버튼 조회", notes = "채팅/전화메인 고객검색 버튼 조회")
    @PostMapping("/api/setting/customer/info-list/cstmrinfo/inqire")
    public Object selectRtnCust(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String custId = mjsonParams.getString("CUSTOMER_ID");
        String custNm = mjsonParams.getString("CUSTOMER_NM");        // 고객명
        String custcoId = mjsonParams.getString("CUSTCO_ID");        // CUSTCO_ID
        String custStat = mjsonParams.getString("CUST_STAT");        // CUSTCO_ID

        objRetParams.setString("CUSTOMER_ID", custId);                    // 고객명
        objRetParams.setString("CUSTOMER_NM", custNm);                    // 고객명
        objRetParams.setString("CUSTCO_ID", custcoId);                // CUSTCO_ID
        objRetParams.setString("CUST_STAT", custStat);                // CUSTCO_ID

        objRetParams = settingCustomerInformationListService.selectRtnCust(objRetParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     *
     */
    @ApiOperation(value = "고객정보 삭제", notes = "고객정보 삭제")
    @PostMapping("/api/setting/customer/info-list/delete")
    public Object deleteRtnCustInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return settingCustomerInformationListService.deleteRtnCustInfo(mjsonParams);
    }

    /**
     * 상담원 큐검색 조회
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "상담원 큐검색 조회", notes = "상담원 큐검색 조회")
    @PostMapping("/api/setting/customer/info-list/queueinfo-inqire/inqire")
    public Object selectQUEUEInqire(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        String USER_ID = mjsonParams.getString("USER_ID");

        //QUEUE 정보 조회

        objRetParams.setString("USER_ID", USER_ID);

        objRetParams = settingCustomerInformationListService.selectQUEUEInqire(objRetParams);

        //최종결과값 반환
        return objRetParams;
    }

    @ApiOperation(value = "PALETTE3 고객 기본 정보 조회", notes = "PALETTE3 고객 기본 정보 조회")
    @PostMapping("/api/setting/customer/custDefaultInoSelect")
    public Object custDefaultInoSelect(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.custDefaultInoSelect(mjsonParams); //고객 기본정보

        //최종결과값 반환
        return objRetParams;
    }

    @ApiOperation(value = "PALETTE3 고객 기본 정보 조회 및 신규 고객 생성", notes = "PALETTE3 고객 기본 정보 조회 및 신규 고객 생성")
    @PostMapping("/api/setting/customer/custDefaultInfo")
    public Object custDefaultInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.custSelect(mjsonParams); //고객 기본정보

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * PALETTE3 고객 상세 조회
     */
    @PrvcAspectAnotation(value = "CUST_SEL") //개인정보 Logging관련
    @ApiOperation(value = "PALETTE3 고객 상세 조회", notes = "PALETTE3 고객 상세 조회")
    @PostMapping("/api/setting/customer/custSelect")
    public Object custSelect(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams.setDataObject("DATA", ((TelewebJSON) this.custDefaultInoSelect(mjsonParams)).getDataObject("DATA"));

        objRetParams.setDataObject("CUST_EXPSN_INFO", settingCustomerInformationListService.custExpsnInfoSelect(mjsonParams)); //고객 확장 정보

        objRetParams.setDataObject("CUST_AGRE_INFO", settingCustomerInformationListService.custAgreeInfoSelect(mjsonParams)); //고객 동의(개인, sms, 알림톡, 이메일, push) 정보

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객정보 통합
     */
    @PrvcAspectAnotation(value = "CUST_REG,CUST_MOD") //개인정보 Logging관련
    @ApiOperation(value = "고객정보 저장, 수정", notes = "고객 정보를 저장,수정 한다.")
    @PostMapping("/api/setting/customer/mergeCust")
    public Object mergeCust(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = settingCustomerInformationListService.mergeCust(mjsonParams); //고객 정보 저장

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객 정보 관리 Excel 다운로드
     */
    @PrvcAspectAnotation(value = "CUST_DOWN")   //개인정보 Logging관련
    @ApiOperation(value = "고객 정보 관리 엑셀 다운로드", notes = "고객 정보 관리 엑셀을 다운로드한다")
    @PostMapping("/api/customer/custListExcelDwnld")
    public void custListExcelDwnld(HttpServletRequest request, HttpServletResponse response,
        @TelewebJsonParam TelewebJSON jsonParams) throws Exception {
        ExcelDownController bCon = new ExcelDownController(excelService);
        //다운로드 항목
        String CUSTOM_ATTR = jsonParams.getString("CUSTOM_ATTR").toString();
        CUSTOM_ATTR = CUSTOM_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("EXPSN_ATTR_LIST", CUSTOM_ATTR);

        //고객 확장 속성 검색
        String SCH_CUST_EXPSN_ATTR = jsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
        SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

        //상담 확장 속성 검색
        String SCH_CUTT_EXPSN_ATTR = jsonParams.getString("SCH_CUTT_EXPSN_ATTR").toString();
        SCH_CUTT_EXPSN_ATTR = SCH_CUTT_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_CUTT_EXPSN_ATTR", SCH_CUTT_EXPSN_ATTR);

        bCon.excel_tmpl(request, response, jsonParams);
    }
}