package org.cz.importer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.importer.util.BusinessDayCheck;
import org.cz.importer.util.UrlConnection;
import org.cz.json.security.SecurityDaily;
import org.cz.util.DateUtil;
import org.html.parser.UrlParserException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opencsv.CSVReader;

public class UrlImporter {
	
	private static final Logger log = Logger.getLogger(UrlImporter.class);
	
	public static void importForUrl(final String url,String goToWeb,Home home) {
	    
		try {
			UrlConnection urlConn = new UrlConnection(url);
			try {
				CsvImporter2 csv = new CsvImporter2(home,url,goToWeb);
    			CSVReader reader = new CSVReader(urlConn.getBins());
    			csv.readSecurities(reader);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("Fatal exiting");
			System.exit(1);
		} catch (UrlParserException e) {
			e.printStackTrace();
			log.error("Fatal exiting");
			System.exit(1);
		}
	}
	
	public static void updateSecuritiesDaily(Home home)
	{
		List<SecurityDaily> secs = home.getLastSecurityDailys();
		Date last = secs.get(0).getDate();
		GregorianCalendar gcLast = new GregorianCalendar();
		gcLast.setTime(last);
		gcLast.add(Calendar.DAY_OF_YEAR, 1);
		last = gcLast.getTime();
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		Date start = gc.getTime();
		
		log.info("Updating securities at: " + start);
		while (last.before(start))
		{
			if (BusinessDayCheck.isBusinessDay(gcLast))
			{
				String url = buildUrl(gcLast);
				try
				{
					log.info("	RUNNING WITH URL : " + url); 
					importForUrl(url,"on",home);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			gcLast.add(Calendar.DAY_OF_YEAR, 1);
			last = gcLast.getTime();
		}
		
		gc = new GregorianCalendar();
		Date finish = gc.getTime();
		log.info("Finished Updating securities at: " + finish);
		log.info("Process took: " + DateUtil.formatStartFinish(start,finish));
	}
	
	private static String buildUrl(GregorianCalendar gc)
	{
		String urlStub = "http://www.klsedaily.com/data/";
		String ys = Integer.toString(gc.get(Calendar.YEAR));
		String ms = Integer.toString(gc.get(Calendar.MONTH)+1);
		if (ms.length()<2)
			ms = "0" + ms;
		String ds = Integer.toString(gc.get(Calendar.DAY_OF_MONTH));
		if (ds.length()<2)
			ds = "0" + ds;
		
		return urlStub + ys + "/" + ms + "/klse_" + ds + "_" + ms + "_" + ys.substring(2) + ".txt";
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		updateSecuritiesDaily(home);
		
		/*
		String url = "http://www.klsedaily.com/data/2017/06/klse_05_06_17.txt";
		try
		{
			log.info("Running with url : " + url); 
			importForUrl(url,"on",home);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
	}


}
