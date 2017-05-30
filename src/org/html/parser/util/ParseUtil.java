package org.html.parser.util;

public class ParseUtil
{
	public static Double getParsedDouble( String str )
	{
		if (str == null || str.length() == 0)
			return null;
		String dString = "";
		int cnt = 0;
	    for (; cnt<str.length(); cnt++)								// strip leading 
	    {
	    	char ch = str.charAt(cnt);
	    	if (Character.isDigit(ch) || ch=='.' ||  ch=='-')
	    		break;
	    }
	    if (cnt == str.length())
	    	return null;
	    for (; cnt<str.length(); cnt++)								// use values 
	    {
	    	char ch = str.charAt(cnt);
	    	if (!(Character.isDigit(ch) || ch=='.' || ch=='-'))
	    		break;
	    	dString += ch;
	    }
	    
	    if (dString.length() == 0)
	    	return null;
		return Double.parseDouble(dString);							// leave oki koki strngs to this 
	}
	
	public static Integer getParsedInt( String str )
	{
		Double d = getParsedDouble( str );
		if (d==null)
			return null;
		return (Integer) d.intValue();
	}
	
	public static double trimDecimals2(double value)
	{
		int iValue = (int) (value * 100.0);
		return iValue/100.0;
	}
	
}
