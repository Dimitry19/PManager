package cm.travelpost.tp.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static final String FORMAT_STD_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String FORMAT_STD_PATTERN_2 = "yyyy/MM/dd, hh:mm:ss";
    public static final String FORMAT_STD_PATTERN_3 = "dd-MM-yyyy HH:mm:ss";
    public static final String FORMAT_STD_PATTERN_4 = "dd/MM/yyyy, HH:mm:ss";
    public static final String TIMESTAMP_PATTERN = "yy/MM/dd, hh:mm:ss";
    public static final String STD_PATTERN = "dd/MM/yyyy";
    public static final String STD_PATTERN_HMS = "dd/MM/yyyy, HH:mm:ss";
    public static final String STD_PATTERN_YMD = "yyyyMMdd";



    public static String getDateStandard(Date date) {
        try {
            return new SimpleDateFormat(STD_PATTERN).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateStandardHMS(Date date) {
        try {
            return new SimpleDateFormat(STD_PATTERN_HMS).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date currentDate() {
        try {
            return  dateWithoutTime( new Date());
        } catch (Exception e) {
            logger.trace("Impossible recuperer la date actuelle:" + e);
            throw e;
        }
    }


    public static String getDateStandardFormatted(Date date) {
        try {
            return new SimpleDateFormat(FORMAT_STD_PATTERN).format(date);
        } catch (Exception e) {
            return "";
        }
    }


    public static Date milliSecondToDate(long dateTime) {

        System.out.println("To convert Date: " + dateTime);
        //creating Date from millisecond
        Date currentDate = dateWithoutTime(new Date(dateTime));
        System.out.println("converted Date: " + currentDate);
        return calendar(dateTime);

    }



    @Nullable
    private static Date getDate(String dateStr, String stdPatternHms) {
        Date date = null;
        try {
            if (dateStr != null && dateStr.length() > 0) {
                DateFormat formatter;
                formatter = new SimpleDateFormat(stdPatternHms);
                date = formatter.parse(dateStr);
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + date + ". Exception :" + e);
        }
        return date;
    }


    @NotNull
    private static String getString(Date date, String stdPatternHms) {
        String dateStr = "";
        try {
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(stdPatternHms);
                dateStr = formatter.format(date);
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + date + ". Exception :" + e);
        }
        return dateStr;
    }

    public static java.sql.Date dateToSQLDate(Date inputDate) {
        java.sql.Date sqlDate = null;
        if (inputDate != null) {
            sqlDate = new java.sql.Date(inputDate.getTime());
        }
        return sqlDate;
    }

    //JAVA 8---------------------------------
    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static LocalDateTime convertToLocalDateTimeViaMillisecond(Date dateToConvert) {
        return convertToLocalDateTimeViaMillisecond(dateToConvert.getTime());
    }

    public static LocalDateTime convertToLocalDateTimeViaMillisecond(long milliseconds) {
        return Instant.ofEpochMilli(milliseconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static LocalDate convertToLocalDateMillisecondsViaSqlDate(long millisecond) {
        return new java.sql.Date( millisecond).toLocalDate();
    }
    //--------------------------------------

    public static boolean isAfter(Date compareDateOne, Date compareDateTwo) {
        if (compareDateOne != null && compareDateTwo != null) {

            return compareDateOne.after(compareDateTwo);
        }
        return false;
    }

    public static boolean isBefore(Date compareDateOne, Date compareDateTwo) {
        if (compareDateOne != null && compareDateTwo != null) {
            return compareDateOne.before(compareDateTwo);
        }
        return false;
    }

    public static boolean isSame(Date compareDateOne, Date compareDateTwo) {

        if (compareDateOne != null && compareDateTwo != null) {
            return !isAfter(compareDateOne,compareDateTwo) && !isBefore(compareDateOne,compareDateTwo);
        }
        return false;
    }


    public static Date calendar(long milliseconds){

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());

        cal.setTimeInMillis(milliseconds);
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    public static Date dateWithoutTime(Date date) {

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeZone(tz);
        calendar.setTime( date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();

    }

    public  static  boolean validLongValue(long l){
        return l>0;
    }


    public static Date stringToDate(String dateStr, String format) {
        return getDate(dateStr, format);
    }

    public static String dateToString(Date date) {
        return getString(date, STD_PATTERN);
    }

    public static String dateTimeToString(Date date) {
        return getString(date, STD_PATTERN_HMS);
    }

    public static String getDateFormatted(Format formatter, Date date) {
        return formatter.format(date);
    }

    public static Timestamp stringToTimestamp(String dateStr) {
        Timestamp timestamp = null;
        try {
            if (dateStr != null && dateStr.length() > 0) {
                DateFormat formatter;
                formatter = new SimpleDateFormat(TIMESTAMP_PATTERN);
                Date date = formatter.parse(dateStr);
                timestamp = new Timestamp(date.getTime());
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + dateStr + ". Exception :" + e);
        }
        return timestamp;
    }

    public static Date stringToDate(String dateStr) {
        return getDate(dateStr, STD_PATTERN_HMS);
    }

    public static Date milliSecondToDateCalendar(long currentDateTime) {


        try {
            DateFormat df = new SimpleDateFormat(FORMAT_STD_PATTERN_4);
            //Converting milliseconds to Date using Calendar
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentDateTime);
            System.out.println("Milliseconds to Date using Calendar:" + df.format(cal.getTime()));

            //copying one Date's value into another Date in Java
		/*Date now = new Date();
		Date copiedDate = new Date(now.getTime());

		System.out.println("original Date: " + df.format(now));
		System.out.println("copied Date: " + df.format(copiedDate));*/
            return cal.getTime();

        } catch (Exception e) {

        }
        return null;
    }


    public static boolean isDifferenceDay(Date d1, Date d2,long numbersDays, boolean extreme){

        return extreme ?differenceDay(d1,d2)==numbersDays: differenceDay(d1,d2)<=numbersDays;
    }

    public static Long differenceDay(Date d1, Date d2){

        if(d1 == null || d2 == null ) return null;

         long diff = d2.getTime() - d1.getTime();

         return diff / (24 * 60 * 60 * 1000);
    }
    static void printDate(Date date){

        System.out.println("converted Date: " + dateWithoutTime(date));

    }

    /**
     *  return date, month or year
     * @param elementType
     * @return int
     */
    public static int  gregorianCalendar(int elementType){
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        return gregorianCalendar.get(elementType);

    }
}
