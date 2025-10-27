import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeTest {
	public static void main(String[] args) {
		String procDt = getDatePrevDD(getFormatString("yyyyMMdd"));
		System.out.println("procDt ::: " + procDt);
		
		String backupDt = getAdjustDateString(0, 0, Integer.valueOf(-14).intValue());
		System.out.println("backupDt ::: " + backupDt);
	}
	
	public static String getDatePrevDD(String curYYYYMMDD)
    {
        return getAdjustDate(curYYYYMMDD, 5, -1);
    }
	
	
	public static String getAdjustDate(String curYYYYMMDD, int field, int move)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(1, Integer.parseInt(curYYYYMMDD.substring(0, 4)));
        cal.set(2, Integer.parseInt(curYYYYMMDD.substring(4, 6)) - 1);
        cal.set(5, Integer.parseInt(curYYYYMMDD.substring(6)));
        cal.add(field, move);
        return getString(cal.getTime(), "yyyyMMdd");
    }
	
	public static String getString(Date date, String format)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }
	
	public static String getFormatString(String pattern)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        String dateString = formatter.format(new Date());
        return dateString;
    }
	
	
	public static String getAdjustDateString(int yy, int mm, int dd)
    {
        return getAdjustDateString(yy, mm, dd, "yyyyMMdd");
    }

    public static String getAdjustDateString(int yy, int mm, int dd, String format)
    {
        Calendar can = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if(yy != 0)
            can.add(1, yy);
        if(mm != 0)
            can.add(2, mm);
        if(dd != 0)
            can.add(5, dd);
        return formatter.format(can.getTime());
    }
}
