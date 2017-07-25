package Utiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateConvertUtils class which containing methods that allow us to convert Date
 * to string and util.date to sql.date format<br>
 */
public class DateConvertUtils {
	/**
	 * java.sql.Date convertToSql method is converting a srtig in to SQL format.
	 * 
	 * @param date
	 *            The date to convert.
	 * @return java.sql.Date;
	 */
	public static java.sql.Date convertToSql(Date date) {
		return dateToSqlDate(date);
	}

	/**
	 * stringToDate method Translates the string input into java.util.Date.
	 * 
	 * @param dateInFormat
	 *            The date to convert.
	 * @return Date
	 */
	public static Date stringToDate(String dateInFormat) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return dateFormater.parse(dateInFormat);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * java.sql.Date dateToSqlDate method is converting the java.util.Date in to
	 * SQL format
	 * 
	 * @param date
	 *            The date to convert.
	 * @return java.sql.Date;
	 */
	public static java.sql.Date dateToSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}
}
