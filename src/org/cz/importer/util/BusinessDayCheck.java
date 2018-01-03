package org.cz.importer.util;

import java.util.Calendar;

public class BusinessDayCheck {

	public static boolean isBusinessDay(Calendar cal){
		// check if weekend
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return false;
		}
		
		// check if New Year's Day
		if (cal.get(Calendar.MONTH) == Calendar.JANUARY
			&& cal.get(Calendar.DAY_OF_MONTH) == 1) {
			return false;
		}
		
		// check if Christmas
		if (cal.get(Calendar.MONTH) == Calendar.DECEMBER
			&& cal.get(Calendar.DAY_OF_MONTH) == 25) {
			return false;
		}
		
		
		return true;
	}
}
