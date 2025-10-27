package kr.co.hkcloud.palette3.setting.holiday.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Calendar;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.holiday.app.SettingHolidayManageService;
import kr.co.hkcloud.palette3.setting.holiday.util.SettingHolidayManageUtils;
import kr.co.hkcloud.palette3.setting.holiday.util.SettingHolidayManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingHolidayManageRestController", description = "휴일관리 REST 컨트롤러")
public class SettingHolidayManageRestController {

    private final SettingHolidayManageUtils settingHolidayManageUtils;
    private final SettingHolidayManageService settingHolidayManageService;
    private final SettingHolidayManageValidator settingHolidayManageValidator;


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정휴일관리-목록", notes = "설정휴일관리 목록을 조회한다")
    @PostMapping("/api/setting/holiday/manage/list")
    public Object selectRtnPageHoliday(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //Validation 체크 
        //        settingHolidayManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        return settingHolidayManageService.selectRtnPageHoliday(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_HLDY_PROC", note = "휴일설정 변경(등록,수정)")
    @ApiOperation(value = "설정휴일관리-등록", notes = "휴일을 등록한다")
    @PostMapping("/api/setting/holiday/manage/regist")
    public Object insertRtnHoliday(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String hldyId = mjsonParams.getString("HLDY_ID");
        if (hldyId.equals("")) {
            //휴일 중복 체크
            objRetParams = settingHolidayManageService.selectRtnHolidayDupChk(jsonParams);
        } else {
            objRetParams.setString("CNT", "0");
        }
        if ("0".equals(objRetParams.getString("CNT"))) {
            //Validation 체크 
            //          settingHolidayManageValidator.validate(mjsonParams, result);
            //          if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

            objRetParams = settingHolidayManageService.insertRtnHoliday(jsonParams);
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_CODE", "-700");
            objRetParams.setHeader("ERROR_MSG", "이미 등록되어 있는 휴일입니다. 확인 후 재시도바랍니다.");
        }

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_HLDY_PROC", note = "휴일설정 변경(등록,수정)")
    @ApiOperation(value = "설정휴일관리-수정", notes = "휴일을 수정한다")
    @PostMapping("/api/setting/holiday/manage/modify")
    public Object updateRtnHoliday(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //Validation 체크 
        settingHolidayManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        return settingHolidayManageService.updateRtnHoliday(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "COM_HLDY_DEL", note = "휴일설정 삭제")
    @ApiOperation(value = "설정휴일관리-삭제", notes = "휴일을 삭제한다")
    @PostMapping("/api/setting/holiday/manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //Validation 체크 
        //        settingHolidayManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingHolidayManageService.deleteRtnHoliday(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_HLDY_HLDY_REGIST", note = "휴일설정 공휴일 일괄등록")
    @ApiOperation(value = "설정휴일관리-공휴일등록", notes = "공휴일을 계산하여 저장한다")
    @PostMapping("/api/setting/holiday/manage/hldy/regist")
    public Object addHoliday(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = mjsonParams;

        String solarDate = "";      //양력날짜
        String lunarDate = "";      //음력날짜
        String holidayDate = "";    //휴일날짜
        String holidayName = "";    //휴일명

        int year = mjsonParams.getInt("START_YEAR");  //시작년도 세팅
        int endYear = mjsonParams.getInt("END_YEAR");    //종료년도 세팅

        Calendar c = Calendar.getInstance();
        c.set(year, 0, 1); // 1월 1일부터 시작

        for (int i = year; i <= endYear; ) {
            holidayDate = "";   //휴일날짜 초기화
            holidayName = "";   //휴일명 초기화

            solarDate = settingHolidayManageUtils.getDateByString(c.getTime(), "");  //양력날짜 세팅
            lunarDate = settingHolidayManageUtils.converSolarToLunar(solarDate, ""); //양력에 대한 음력 날짜 세팅

            c.add(Calendar.DAY_OF_MONTH, 1);

            String solarMmdd = solarDate.substring(4, 8);    // 양력휴일 체크
            String lunarMmdd = lunarDate.substring(4, 8);    // 음력휴일 체크

            if (SettingHolidayManageUtils.solarHolidayMap.containsKey(solarMmdd)) {
                holidayDate = solarDate;
                holidayName = SettingHolidayManageUtils.solarHolidayMap.get(solarMmdd);
                settingHolidayManageService.insertRtnHoliday(objJsonParams, holidayDate, holidayName, "HLD");
            }

            if (SettingHolidayManageUtils.lunarHolidayMap.containsKey(lunarMmdd)) {
                if (lunarMmdd.equals("0101")) {
                    // 음력 12월은 마지막날이 29일, 30일 계속 번갈아가면서 있으므로 양력에서 하루를 빼준날이 구정시작하는 날짜이다.
                    holidayDate = settingHolidayManageUtils.getDay(solarDate, -1);
                    holidayName = "설날";
                    settingHolidayManageService.insertRtnHoliday(objJsonParams, holidayDate, holidayName, "HLD");
                }

                holidayDate = solarDate;
                holidayName = SettingHolidayManageUtils.lunarHolidayMap.get(lunarMmdd);
                settingHolidayManageService.insertRtnHoliday(objJsonParams, holidayDate, holidayName, "HLD");
            }

            year = c.get(Calendar.YEAR);

            if (year != i) {
                i++;
            }
            if (i > endYear) {
                break;
            }

        } // end for_i

        return objRetParams;
    }


    /**
     *
     * @param  inHashMap
     * @return
     */
    @SystemEventLogAspectAnotation(value = "COM_HLDY_WKEND_REGIST", note = "휴일설정 주말(토,일) 일괄등록")
    @ApiOperation(value = "설정휴일관리-주말등록", notes = "주말을 계산하여 저장한다")
    @PostMapping("/api/setting/holiday/manage/wkend/regist")
    public Object addWeeken(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strCase = mjsonParams.getString("STR_CASE");
        int startYear = mjsonParams.getInt("START_YEAR");
        int endYear = mjsonParams.getInt("END_YEAR");

        Calendar c = Calendar.getInstance();
        c.set(startYear, 0, 1); //1월 1일부터 시작

        for (int i = startYear; i <= endYear; ) {
            if (strCase.equals("SAT")) {
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    settingHolidayManageService.insertRtnHoliday(mjsonParams, settingHolidayManageUtils.getDateByString(c.getTime(), ""),
                        "토요일", "SAT");
                }
            } else if (strCase.equals("SUN")) {
                if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    settingHolidayManageService.insertRtnHoliday(mjsonParams, settingHolidayManageUtils.getDateByString(c.getTime(), ""),
                        "일요일", "SUN");
                }
            }

            c.add(Calendar.DAY_OF_WEEK, 1);

            startYear = c.get(Calendar.YEAR);

            if (startYear != i) {
                i++;
            }
            if (i > endYear) {
                break;
            }
        }

        return objRetParams;
    }


    /**
     * 휴일을 삽입한다.
     *
     * @param  objJsonParams
     * @return TelewebJSON
     */
    @ApiOperation(value = "휴일을 삽입한다", notes = "휴일을 삽입한다.")
    @PostMapping("/api/TwbTalkHolidayMgmt/insertRtnHoliday")
    public Object insertRtnHoliday(@TelewebJsonParam TelewebJSON objJsonParams, String holidayDate, String holidayName,
        String holidayGbCd) throws TelewebApiException {
        TelewebJSON objRetParam = new TelewebJSON(objJsonParams);

        objRetParam = settingHolidayManageService.insertRtnHoliday(objJsonParams, holidayDate, holidayName, holidayGbCd);

        return objRetParam;
    }

}
