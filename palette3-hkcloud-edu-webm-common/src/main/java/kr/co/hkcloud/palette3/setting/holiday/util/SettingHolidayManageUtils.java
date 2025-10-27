package kr.co.hkcloud.palette3.setting.holiday.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ibm.icu.util.ChineseCalendar;

import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;


/**
 * 휴일 유틸리티
 */
@Component
public class SettingHolidayManageUtils
{
    public static final Map<String, String> solarHolidayMap = new HashMap<>();	//양력 공휴일 맵
    public static final Map<String, String> lunarHolidayMap = new HashMap<>();	//음력 공휴일 맵


    /**
     * 휴일을 맵에 세팅힌다.
     */
    public SettingHolidayManageUtils() throws TelewebUtilException {
        setSolarHolidayMap();   //양력휴일
        setLunarHolidayMap();   //음력휴일
    }


    /**
     * 양력 휴일을 solarHolidayMap에 세팅한다.
     */
    private void setSolarHolidayMap() throws TelewebUtilException
    {
        solarHolidayMap.put("0101", "새해 첫날");
        solarHolidayMap.put("0301", "삼일절");
        solarHolidayMap.put("0505", "어린이날");
        solarHolidayMap.put("0606", "현충일");
        solarHolidayMap.put("0815", "광복절");
        solarHolidayMap.put("1003", "개천절");
        solarHolidayMap.put("1009", "한글날");
        solarHolidayMap.put("1225", "성탄절");
    }


    /**
     * 음력 휴일을 lunarHolidayMap에 세팅한다.
     */
    private void setLunarHolidayMap() throws TelewebUtilException
    {
        lunarHolidayMap.put("0101", "설날");
        lunarHolidayMap.put("0102", "설날");
        lunarHolidayMap.put("0408", "부처님 오신 날");
        lunarHolidayMap.put("0814", "추석");
        lunarHolidayMap.put("0815", "추석");
        lunarHolidayMap.put("0816", "추석");
    }


    /**
     * 
     * @param  date
     * @return
     */
    public String getDateByString(Date date) throws TelewebUtilException
    {
        return getDateByString(date, "-");
    }


    /**
     * 
     * @param  date
     * @param  separator
     * @return
     */
    public String getDateByString(Date date, String separator) throws TelewebUtilException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd");
        return sdf.format(date);
    }


    /**
     * 양력날짜를 음력날짜로 변환
     * 
     * @param  양력날짜 (yyyyMMdd)
     * @return      음력날짜 (yyyyMMdd)
     */
    public String converSolarToLunar(String date) throws TelewebUtilException
    {
        return converSolarToLunar(date, "-");
    }


    /**
     * 
     * @param  date
     * @param  separator
     * @return
     */
    public String converSolarToLunar(String date, String separator) throws TelewebUtilException
    {

        ChineseCalendar cc = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

        cc.setTimeInMillis(cal.getTimeInMillis());

        int y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int m = cc.get(ChineseCalendar.MONTH) + 1;
        int d = cc.get(ChineseCalendar.DAY_OF_MONTH);

        StringBuffer ret = new StringBuffer();

        ret.append(String.format("%04d", y)).append(separator);
        ret.append(String.format("%02d", m)).append(separator);
        ret.append(String.format("%02d", d));

        return ret.toString();

    }


    /**
     * 
     * @param  date
     * @param  amount
     * @return
     */
    public String getDay(String date, int amount) throws TelewebUtilException
    {

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6)));
        cal.add(Calendar.DAY_OF_MONTH, amount);

        return getDateByString(cal.getTime(), "");

    }
}