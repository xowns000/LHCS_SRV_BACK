package kr.co.hkcloud.palette3.v3.customer.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import kr.co.hkcloud.palette3.common.code.app.CodeCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.v3.customer.service.V3CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@Api(value = "V3CustomerController", description = "고객정보 관련 V3  REST 컨트롤러")
public class V3CustomerController {

    private final V3CustomerService v3CustomerService;
    private final CodeCmmnService codeCmmnService;
    private final PaletteCmmnService paletteCmmnService;

    @ApiOperation(value = "[V3-API] 고객정보 조회", notes = "[V3-API] 고객정보 조회")
    @PostMapping("/v3-api/customer/list")
    public Object selectCustomerList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        /*DATA[0][SCH_CUST_ID]:    ==>고객ID(필수X) => 고객ID로 검색하는경우 조회시작일시,조회종료일시 는 무시되며 입력안하여도 됩니다.
        DATA[0][SCH_ST_DTS]: 20231219000000  ==> 조회시작일시(yyyyMMddHHmmss)(필수O => SCH_CUST_ID값이 존재하는경우 필수X , 입력하여도 무시됨)
        DATA[0][SCH_END_DTS]: 20231219235959 ==> 조회종료일시(yyyyMMddHHmmss)(필수O => SCH_CUST_ID값이 존재하는경우 필수X , 입력하여도 무시됨)

        DATA[0][SCH_CUST_STAT]: NOML ==>고객상태(필수X) ==> 미입력:전체, NOMAL:정상 , SLEP:휴면 , DELT:삭제
        DATA[0][SCH_GB]:         ==> 검색구분(필수X) ==> CUST_NM:고객명 , CUST_ID:고객ID , CUST_PHN_NO:고객 전화번호
        DATA[0][SCH_KEYWORD]:    ==> 검색어(필수X)
        ※ 고객ID로 기간별로 조회하고자 하는경우 SCH_GB값을 CUST_ID, SCH_KEYWORD 값에 고객ID를 입력하여 사용.*/

        TelewebJSON objRetParams = new TelewebJSON();
        boolean isReturn = this.isDifferenceHours( mjsonParams );   //조회기간을 하루로 끊기 위함.
        if( StringUtils.isNotEmpty(mjsonParams.getString("SCH_CUST_ID"))) {    //고객아이디로 검색하는경우는 날짜조건에서 제외시킨다.
            isReturn = true;
        }

        if ( isReturn ) {
            objRetParams = v3CustomerService.selectCustomerList(mjsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "조회 기간을 확인해주세요.");
        }
        return objRetParams;
    }

    @ApiOperation(value = "[V3-API] 상담이력 조회", notes = "[V3-API] 상담이력 조회")
    @PostMapping("/v3-api/cutt/historyList")
    public Object selectCuttHistoryList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        /*DATA[0][SCH_PHN_CUTT_ID]:    ==>전화상담ID(필수X) => 전화상담ID로 검색하는경우 조회시작일시,조회종료일시 는 무시되며 입력안하여도 됩니다.
        DATA[0][SCH_ST_DTS]: 20231219000000  ==> 조회시작일시(yyyyMMddHHmmss)(필수O => SCH_PHN_CUTT_ID값이 존재하는경우 필수X , 입력하여도 무시됨)
        DATA[0][SCH_END_DTS]: 20231219235959 ==> 조회종료일시(yyyyMMddHHmmss)(필수O => SCH_PHN_CUTT_ID값이 존재하는경우 필수X , 입력하여도 무시됨)

        DATA[0][SCH_GB]:         ==> 검색구분(필수X) ==> CUST_NM:고객명 , CUST_PHN_NO:고객 전화번호, LGN_ID:상담사로그인아이디, USER_NM: 상담사 이름
        DATA[0][SCH_KEYWORD]:    ==> 검색어(필수X)

        DATA[0][SCH_CL_TYPE_CD]:  ==>콜 유형 (필수X) ==> 미입력:전체, IN:인바운드 , OUT:아웃바운드
        DATA[0][SCH_CUSL_RS_CD]:  ==>상담결과 (필수X) ==> 미입력:전체, COMPLETED:처리완료 , PROCESSING:처리중*/

        TelewebJSON objRetParams = new TelewebJSON();
        boolean isReturn = this.isDifferenceHours( mjsonParams );   //조회기간을 하루로 끊기 위함.
        if( StringUtils.isNotEmpty(mjsonParams.getString("SCH_PHN_CUTT_ID")) || StringUtils.isNotEmpty(mjsonParams.getString("SCH_CUST_ID")) ) {    //전화상담ID(SCH_PHN_CUTT_ID)로 검색하는경우는 날짜조건에서 제외시킨다.
            isReturn = true;
        }

        if ( isReturn ) {
            objRetParams = v3CustomerService.selectCuttHistoryList(mjsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "조회 기간을 확인해주세요.");
        }
        return objRetParams;
    }

    @NoBizLog
    @ApiOperation(value = "공통코드 검색", notes = "프레임워크에서 기본적으로 제공하는 공통코드의 코드,코드명를 검색한다.(콤보박스로드용)")
    @PostMapping("/v3-api/code-book/list")
    public Object selectRtnCodeBook(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        for (int i = 0; i < mjsonParams.getSize(); i++) {
            //코드타입에 ETC 코드값 설정 시 분리
            String[] objCodeType = mjsonParams.getString("GROUP_CD_ID", i).split("\\|");
            String[] objCodeGubun = objCodeType[0].split("\\:");
            jsonParams.setString("GROUP_CD_ID", objCodeGubun[0]);              //코드타입에 대한 키값 설정

            if ("".equals(objCodeType[0])) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "잘못된 Param정보 입니다.");
                return objRetParams;
            }

            if (objCodeGubun.length > 1) {
                jsonParams.setString("CD_TYPE", objCodeGubun[1]);           //코드구분값 설정
            }

            for (int j = 1; j < objCodeType.length; j++) {
                //코드타입에 ETC코드값에 추가조건(CODE_TY|ETC_CODE1|ETC_CODE2|ETC_CODE3)설정시 해당 등록조건만큼 키 정보 생성
                jsonParams.setString("ETC_INFO0" + String.valueOf(j), objCodeType[j]);
            }
            objRetParams.setDataObject(objCodeType[0], codeCmmnService.selectRtnCachingCodeBook(jsonParams));
        }

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 조회조건의 날짜 기간 제한
     * @param mjsonParams
     * @return
     */
    public boolean isDifferenceHours(TelewebJSON mjsonParams) {
        try {
            String startDts = mjsonParams.getString("SCH_ST_DTS");
            String endDts = mjsonParams.getString("SCH_END_DTS");
            if (StringUtils.isNotEmpty(startDts) && StringUtils.isNotEmpty(endDts) && startDts.length() == 14 && endDts.length() == 14) {
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date startDate = format.parse(startDts);
                Date endDate = format.parse(endDts);

                long differenceInMillis = endDate.getTime() - startDate.getTime();
                long years = (differenceInMillis / (365 * 24 * 60 * 60 * 1000L));
                long days = (differenceInMillis / (24 * 60 * 60 * 1000L)) % 365;
                long hours = (differenceInMillis / (60 * 60 * 1000L)) % 24;
                long minutes = (differenceInMillis / (60 * 1000L)) % 60;
                long seconds = (differenceInMillis / 1000) % 60;
                System.out.printf(startDts +" ~ " + endDts + "\n");
                System.out.printf("두 날짜 사이: %d년 %d일 %d:%d:%d \n", years, days, hours, minutes, seconds);
                System.out.printf("diffHours : %d \n", ((endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000L)));
                System.out.print("\n");

                long diffHours = ((endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000L));
                return (diffHours <= 72);   //3일

            } else {
                return false;
            }
        } catch (ParseException pe) {
            return false;
        }
    }
}
