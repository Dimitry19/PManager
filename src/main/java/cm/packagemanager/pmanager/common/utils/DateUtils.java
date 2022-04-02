package cm.packagemanager.pmanager.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static Date currentDate() {
        try {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
        }
        return null;
    }


    public static String getDateStandardFormatted(Date date) {
        try {
            return new SimpleDateFormat(FORMAT_STD_PATTERN).format(date);
        } catch (Exception e) {
            return "";
        }
    }


    public static Date milliSecondToDate(long dateTime) {

        //Converting milliseconds to Date using java.util.Date
        //current time in milliseconds
        //long currentDateTime = System.currentTimeMillis();

        System.out.println("To convert Date: " + dateTime);
        //creating Date from millisecond
        Date currentDate = dateWithoutTime(new Date(dateTime));
        System.out.println("converted Date: " + dateWithoutTime(new Date(dateTime)));

	/*	//printing value of Date
		System.out.println("current Date: " + currentDate);

		//DateFormat df = new SimpleDateFormat("ddMM:yy:HH:mm:ss");
		DateFormat df = new SimpleDateFormat(FORMAT_STD_PATTERN_4);

		//formatted value of current Date
		System.out.println("Milliseconds to Date: " + df.format(currentDate));*/
        return currentDate;

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

    public static java.sql.Date DateToSQLDate(Date inputDate) {
        java.sql.Date sqlDate = null;
        if (inputDate != null) {
            sqlDate = new java.sql.Date(inputDate.getTime());
        }
        return sqlDate;
    }


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


    public static Date dateWithoutTime(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
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

    static void printDate(Date date){

        System.out.println("converted Date: " + dateWithoutTime(date));

    }
}
