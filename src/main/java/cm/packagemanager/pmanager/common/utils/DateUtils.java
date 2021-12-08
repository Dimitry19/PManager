package cm.packagemanager.pmanager.common.utils;

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

    public static String getDateStandardFormatted(Date date) {
        try {
            return new SimpleDateFormat(FORMAT_STD_PATTERN).format(date);
        } catch (Exception e) {
            return "";
        }
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

    public static Date milliSecondToDate(long currentDateTime) {

        //Converting milliseconds to Date using java.util.Date
        //current time in milliseconds
        //long currentDateTime = System.currentTimeMillis();

        //creating Date from millisecond
        Date currentDate = new Date(currentDateTime);

	/*	//printing value of Date
		System.out.println("current Date: " + currentDate);

		//DateFormat df = new SimpleDateFormat("ddMM:yy:HH:mm:ss");
		DateFormat df = new SimpleDateFormat(FORMAT_STD_PATTERN_4);

		//formatted value of current Date
		System.out.println("Milliseconds to Date: " + df.format(currentDate));*/
        return currentDate;

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

    public static Date StringToDate(String dateStr) {
        Date date = null;
        try {
            if (dateStr != null && dateStr.length() > 0) {
                DateFormat formatter;
                formatter = new SimpleDateFormat(STD_PATTERN_HMS);
                date = formatter.parse(dateStr);
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + date + ". Exception :" + e);
        }
        return date;
    }

    public static Date StringToDate(String dateStr, String format) {
        Date date = null;
        try {
            if (dateStr != null && dateStr.length() > 0) {
                DateFormat formatter;
                formatter = new SimpleDateFormat(format);
                date = formatter.parse(dateStr);
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + date + ". Exception :" + e);
        }
        return date;
    }

    public static String DateToString(Date date) {
        String dateStr = "";
        try {
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(STD_PATTERN);
                dateStr = formatter.format(date);
            }
        } catch (Exception e) {
            logger.trace("Impossibile representer la date " + date + ". Exception :" + e);
        }
        return dateStr;
    }

    public static String DateTimeToString(Date date) {
        String dateStr = "";
        try {
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(STD_PATTERN_HMS);
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
}
