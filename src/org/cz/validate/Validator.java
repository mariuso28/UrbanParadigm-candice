package org.cz.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validator {

	public static boolean isValidEmailAddress(String email) {
		
		   if (email==null)
			   return false;
		   if (email.isEmpty())
			   return false;
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
	
	public static boolean isEmpty(String field)
	{
		if (field==null)
			return true;
		return field.isEmpty();
	}
	
	public static boolean validateDate(String dateToValidate, String dateFromat)
	{
		if(dateToValidate == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}
	
	public static Date getValidDate(String dateToValidate, String dateFromat)
	{
		if(dateToValidate == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			return sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return null;
		}
	}
}
