package org.cz.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class DateUtil {

	private static final Logger log = Logger.getLogger(DateUtil.class);
	
	public static Date dateFromYYmmDDString(String dateStr)
	{
		// 2017-03-05 
		
		GregorianCalendar gc = new GregorianCalendar();	
		gc.clear();
		try
		{
			String[] toks = dateStr.split("-");
			gc.set(Calendar.YEAR, Integer.parseInt(toks[0]));
			gc.set(Calendar.MONTH, Integer.parseInt(toks[1])-1);
			gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(toks[2]));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Invalid date string : " + dateStr + " - " + e.getMessage());
			return null;
		}
		
		return gc.getTime();
	}
	
	public static Date getNowWithZeroedTime()
	{
		GregorianCalendar gc = new GregorianCalendar();
		zeroTime(gc);
		return gc.getTime();
	}
	
	public static Date getDateWithZeroedTime(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		zeroTime(gc);
		return gc.getTime();
	}
	
	private static void zeroTime(GregorianCalendar gc)
	{
		gc.set(Calendar.HOUR_OF_DAY,gc.getActualMinimum(Calendar.HOUR_OF_DAY));
		gc.set(Calendar.MINUTE,gc.getActualMinimum(Calendar.MINUTE));
		gc.set(Calendar.SECOND,gc.getActualMinimum(Calendar.SECOND));
		gc.set(Calendar.MILLISECOND,gc.getActualMinimum(Calendar.MILLISECOND));
	}
	
	
}
