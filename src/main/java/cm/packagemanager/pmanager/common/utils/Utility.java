package cm.packagemanager.pmanager.common.utils;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.io.PrintWriter;
import java.io.StringWriter;


public class Utility
{

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Utility.class);

	public static String getDateStandardFormatted(Date date)
	{
		try
		{
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(date);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public static String getDateFormatted(Format formatter, Date date)
	{
		return formatter.format(date);
	}

	public static String toStringDefEmpty(String value)
	{
		if (value == null) return "";
		else return value;
	}

	public static int toIntDef(String value, int defValue)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
			return defValue;
		}
	}

	public static String convertStreamToString(InputStream is)
	{
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try
		{
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String convertMillisecondToHumanTime(Long millisec)
	{
		String ret = String.valueOf(millisec);
		if (millisec > 999)
		{
			Long seconds = millisec / 1000;
			Long millis = millisec % 1000;
			if (seconds > 59)
			{
				Long minutes = seconds / 60;
				seconds = seconds % 60;

				if (minutes > 59)
				{
					Long hours = minutes / 60;
					minutes = minutes % 60;
					ret = String.valueOf(hours) + ":" + normalizeMinSec(minutes) + ":" + normalizeMinSec(seconds) + "." + normalizeMillis(millis);
				}
				else ret = "00:" + normalizeMinSec(minutes) + ":" + normalizeMinSec(seconds) + "." + normalizeMillis(millis);
			}
			else ret = "00:00:" + normalizeMinSec(seconds) + "." + normalizeMillis(millis);
		}
		else ret = "00:00:00." + normalizeMillis(millisec);
		return ret;
	}

	public static String normalizeMillis(Long millis)
	{
		String ret = String.valueOf(millis);

		if (millis > 9)
		{
			if (millis < 100) ret = "0" + ret;
		}
		else ret = "00" + ret;
		return ret;
	}

	public static String normalizeMinSec(Long minsec)
	{
		String ret = String.valueOf(minsec);
		if (minsec < 10) ret = "0" + ret;
		return ret;
	}


	public static String HTMLEntities(String inputStr)
	{
		return HTMLEntities.htmlAngleBrackets(HTMLEntities.htmlentities(toStringDefEmpty(inputStr)));
	}

	String normalizeWhitespaces(String s)
	{
		StringBuffer res = new StringBuffer();
		int prevIndex = 0;
		int currIndex = -1;
		int stringLength = s.length();
		String searchString = "  ";

		while ((currIndex = s.indexOf(searchString, currIndex + 1)) >= 0)
		{
			res.append(s.substring(prevIndex, currIndex + 1));

			while (currIndex < stringLength && s.charAt(currIndex) == ' ')
			{
				currIndex++;
			}

			prevIndex = currIndex;
		}
		res.append(s.substring(prevIndex));

		return res.toString();
	}

	public static String padRight(String s, int n)
	{
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n)
	{
		return String.format("%1$#" + n + "s", s);
	}

	public static String setFixedLength(String original, int length, char padChar)
	{
		return justifyLeft(original, length, padChar, false);
	}

	protected static String justifyLeft(String str, final int width, char padWithChar, boolean trimWhitespace)
	{
		// Trim the leading and trailing whitespace
		str = str != null ? (trimWhitespace ? str.trim() : str) : "";

		int addChars = width - str.length();
		if (addChars < 0)
		{
			// truncate
			return str.subSequence(0, width).toString();
		}

		// Write the content
		final StringBuilder sb = new StringBuilder();
		sb.append(str);

		// Append the whitespace
		while (addChars > 0)
		{
			sb.append(padWithChar);
			--addChars;
		}

		return sb.toString();
	}

	public static void copyFile(File in, File out) throws IOException
	{
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;

		while ((i = fis.read(buf)) != -1)
		{
			fos.write(buf, 0, i);
		}

		fis.close();
		fos.close();
	}

	public static String getStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}

	// Using PreparedStatement
	public static void setPreparedStatementBoolean(PreparedStatement statement, int parameterIndex, Boolean x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.BOOLEAN);
		else statement.setBoolean(parameterIndex, x);
	}

	public static void setPreparedStatementInteger(PreparedStatement statement, int parameterIndex, Integer x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.INTEGER);
		else statement.setInt(parameterIndex, x);
	}

	public static void setPreparedStatementLong(PreparedStatement statement, int parameterIndex, Long x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.BIGINT);
		else statement.setLong(parameterIndex, x);
	}

	public static void setPreparedStatementFloat(PreparedStatement statement, int parameterIndex, Float x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.FLOAT);
		else statement.setFloat(parameterIndex, x);
	}

	public static void setPreparedStatementFloat(PreparedStatement statement, int parameterIndex, Double x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
		else statement.setDouble(parameterIndex, x);
	}

	public static void setPreparedStatementString(PreparedStatement statement, int parameterIndex, String x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.VARCHAR);
		else statement.setString(parameterIndex, x);
	}

	public static void setPreparedStatementDate(PreparedStatement statement, int parameterIndex, Date x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.DATE);
		else statement.setTimestamp(parameterIndex, new Timestamp(x.getTime()));
	}

	public static void setPreparedStatementTime(PreparedStatement statement, int parameterIndex, Time x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.TIME);
		else statement.setTime(parameterIndex, x);
	}

	// Using CallableStatement
	public static void setCallableStatementBoolean(CallableStatement statement, int parameterIndex, Boolean x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.BOOLEAN);
		else statement.setBoolean(parameterIndex, x);
	}

	public static void setCallableStatementInteger(CallableStatement statement, int parameterIndex, Integer x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.INTEGER);
		else statement.setInt(parameterIndex, x);
	}

	public static void setCallableStatementLong(CallableStatement statement, int parameterIndex, Long x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.BIGINT);
		else statement.setLong(parameterIndex, x);
	}

	public static void setCallableStatementFloat(CallableStatement statement, int parameterIndex, Float x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.FLOAT);
		else statement.setFloat(parameterIndex, x);
	}

	public static void setCallableStatementFloat(CallableStatement statement, int parameterIndex, Double x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
		else statement.setDouble(parameterIndex, x);
	}

	public static void setCallableStatementString(CallableStatement statement, int parameterIndex, String x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.VARCHAR);
		else statement.setString(parameterIndex, x);
	}

	public static void setCallableStatementDate(CallableStatement statement, int parameterIndex, Date x) throws SQLException
	{
		if (x == null) statement.setNull(parameterIndex, java.sql.Types.DATE);
		else statement.setTimestamp(parameterIndex, new Timestamp(x.getTime()));
	}

	public String drawInput(String labelText, String fieldName, String fieldValue)
	{
		return drawInput(labelText, fieldName, fieldValue, false, "", "");
	}

	public String drawInput(String labelText, String fieldName, String fieldValue, boolean readOnly)
	{
		return drawInput(labelText, fieldName, fieldValue, readOnly, "", "");
	}

	public String drawInput(String labelText, String fieldName, String fieldValue, boolean readOnly, String className)
	{
		return drawInput(labelText, fieldName, fieldValue, readOnly, className, "");
	}

	public String drawInput(String labelText, String fieldName, String fieldValue, boolean readOnly, String className, String extraHtmlTag)
	{
		String classTag = "";
		String readOnlyTag = "";

		if (!className.isEmpty()) classTag = "class=\"" + className + "\"";
		if (readOnly) readOnlyTag = "readonly=\"readonly\"";

		return "<label>" + labelText + "</label>" + "<input " + classTag + " id=\"" + fieldName + "\" name=\"" + fieldName + "\" " + " " + extraHtmlTag + " " + "type=\"text\" value=\"" + fieldValue
				+ "\" " + readOnlyTag + "/>";
	}

	public static String getMimeType(File file)
	{
		String mimetype = "";
		if (file.exists())
		{
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) mimetype = "image/png";
			else
			{
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		return mimetype;
	}

	public static String getSuffix(String filename)
	{
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) suffix = filename.substring(pos + 1);

		return suffix;
	}

	public static String getSubstring(String inputString, int maxLength)
	{
		String inputTrimmed = inputString.trim();
		if (inputTrimmed.length() > maxLength) inputTrimmed = inputTrimmed.substring(0, maxLength - 1);
		return inputTrimmed;
	}
}