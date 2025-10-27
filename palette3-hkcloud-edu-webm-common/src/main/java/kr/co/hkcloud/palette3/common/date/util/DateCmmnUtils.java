package kr.co.hkcloud.palette3.common.date.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DateCmmnUtils {
    /**
     * 서버의 현재시간을 pattern에 맞게 가져온다.
     *
     * @param pattern
     * @return
     */
    public static String getCurrentTime(String pattern) {
        //java8
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return now.format(formatter);
    }


    /**
     * @return
     */
    public static Long getEpochSecond() {
        return Instant.now().getEpochSecond();
    }


    /**
     * 밀리초
     *
     * @return
     */
    public static Long toEpochMilli() {
        return Instant.now().toEpochMilli();
    }


    /**
     * @param dateString
     * @return
     */
    public static Long getDateStringToTimestamp(String dateString) {
        //JAVA8 이상
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm", Locale.KOREAN);
        log.debug("dateString" + dateString);
        if (dateString.length() < 12) {
            String fill = "";
            for (int i = 0; i < 12 - dateString.length(); i++) {
                fill = fill + "0";
            }
            dateString = dateString + fill;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        return localDateTime.atZone(ZoneId.of("GMT+9")).toInstant().toEpochMilli();
    }


    /**
     * @param timestampStr
     * @return
     */
    public static String getTimestampToDate(String timestampStr) {
        long timestamp = Long.parseLong(timestampStr);
        return getTimestampToDate(timestamp);
    }


    /**
     * timeStamp to yyyy/MM/dd HH:mm:ss.SSS
     *
     * @param timestamp
     * @return
     */
    public static String getTimestampToDate(final long timestamp) {
        //JAVA8 이상
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS", Locale.KOREAN);
        final String formattedDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("GMT+9")).format(formatter);
        return formattedDate;
    }

    /**
     * Dates 속성은 ISO_8601 Format to yyyyMMddHHmmss
     *  yyyy-MM-dd'T'HH:mm:ss.SSSXXX ==> yyyyMMddHHmmss
     *
     * ISO-8601는 날짜와 시간과 관련된 국제표준이다.
     * ISO-8601의 핵심은 - : T W Z 같은 정해진 문자만 써서 작성한다는 것
     *
     * 우리나라는 KST를 사용하는데 KST는 UTC보다 9시간이 빠르므로 UTC +09:00으로 표기한다.
     *
     */
    public static String getISO8601ToDateString( String patternString, String dateString ) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern( patternString ));
        return (new SimpleDateFormat("yyyyMMddHHmmss").format(Date.from(localDateTime.toInstant(ZoneOffset.of("+09:00")))));
    }

    /**
     * yyyyMMddHHmmss ==> yyyy-MM-dd'T'HH:mm:ss.SSSXXX
     */
    public static String getDateStringToISO8601( String patternString, String dateString ) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return (new SimpleDateFormat(patternString).format(Date.from(localDateTime.toInstant(ZoneOffset.of("+09:00")))));
    }

    public static Long getExpiration(String expires_at)  {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date expiration = format.parse(expires_at);
            Long now = new Date().getTime();            // 현재 시간
            return (expiration.getTime() - now); // accessToken 남은 유효시간
        }catch( ParseException e){
            return null;
        }
    }

    public static String getDate( String patternString, String dateString){
        String output = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat(patternString);
        try {
            Date date = inputFormat.parse(dateString);
            output = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }


}
