package cm.packagemanager.pmanager.common.utils;

import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


	private static org.slf4j.Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static final String FORMAT_STD_PATTERN="yyyy/MM/dd HH:mm:ss.SSS";
	public static final String FORMAT_STD_PATTERN_2="yyyy/MM/dd, hh:mm:ss";
	public static final String TIMESTAMP_PATTERN="yy/MM/dd, hh:mm:ss";
	public static final String STD_PATTERN="dd/MM/yyyy";
	public static final String STD_PATTERN_HMS="dd/MM/yyyy, HH:mm:ss";

	public static String getDateStandardFormatted(Date date){
		try	{
			return new SimpleDateFormat(FORMAT_STD_PATTERN).format(date);
		}catch (Exception e){
			return "";
		}
	}

	public static String getDateFormatted(Format formatter, Date date){
		return formatter.format(date);
	}

	public static Timestamp stringToTimestamp(String dateStr){
		Timestamp timestamp = null;
		try
		{
			if (dateStr != null && dateStr.length() > 0)
			{
				DateFormat formatter;
				formatter = new SimpleDateFormat(TIMESTAMP_PATTERN);
				Date date = (Date) formatter.parse(dateStr);
				timestamp = new Timestamp(date.getTime());
			}
		}catch (Exception e)	{
			logger.trace("Impossibile interpretare la data " + dateStr + ". Exception :" + e);
		}
		return timestamp;
	}

	public static Date StringToDate(String dateStr){
		Date date = null;
		try	{
			if (dateStr != null && dateStr.length() > 0)
			{
				DateFormat formatter;
				formatter = new SimpleDateFormat(STD_PATTERN_HMS);
				date = (Date) formatter.parse(dateStr);
			}
		}
		catch (Exception e)
		{
			logger.trace("Impossible interpreter la date " + dateStr + ". Exception :" + e);
		}
		return date;
	}

	public static Date StringToDate(String dateStr, String format){
		Date date = null;
		try
		{
			if (dateStr != null && dateStr.length() > 0)
			{
				DateFormat formatter;
				formatter = new SimpleDateFormat(format);
				date = (Date) formatter.parse(dateStr);
			}
		}
		catch (Exception e)
		{
			logger.trace("Impossibile interpretare la data " + dateStr + ". Exception :" + e);
		}
		return date;
	}

	public static String DateToString(Date date){
		String dateStr = "";
		try	{
			if (date != null){
				SimpleDateFormat formatter = new SimpleDateFormat(STD_PATTERN);
				dateStr = formatter.format(date);
			}
		}catch (Exception e){
			logger.trace("Impossibile rappresentare la data " + date + ". Exception :" + e);
		}
		return dateStr;
	}

	public static String DateTimeToString(Date date){
		String dateStr = "";
		try	{
			if (date != null){
				SimpleDateFormat formatter = new SimpleDateFormat(STD_PATTERN_HMS);
				dateStr = formatter.format(date);
			}
		}
		catch (Exception e)
		{
			logger.trace("Impossibile rappresentare la data " + date + ". Exception :" + e);
		}
		return dateStr;
	}

	public static java.sql.Date DateToSQLDate(Date inputDate){
		java.sql.Date sqlDate = null;
		if (inputDate != null){
			sqlDate = new java.sql.Date(inputDate.getTime());
		}
		return sqlDate;
	}


	public static boolean isAfter(Date compareDateOne, Date compareDateTwo){
		if(compareDateOne!=null && compareDateTwo!=null){
			return compareDateOne.after(compareDateTwo);
		}return false;
	}

	public static boolean isBefore(Date compareDateOne, Date compareDateTwo){
		if(compareDateOne!=null && compareDateTwo!=null){
			return compareDateOne.before(compareDateTwo);
		}return false;
	}
}
