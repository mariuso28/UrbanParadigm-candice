package org.cz.importer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.html.parser.UrlParserException;

public class SecurityDailyDownload {
	
	private static final Logger log = Logger.getLogger(SecurityDailyDownload.class);
			
	private Date date;
	private BufferedReader bins;
	private URL url;
	private String folder;
	
	
	public SecurityDailyDownload(Date date,String folder) throws UrlParserException, FileNotFoundException, IOException
	{
		setDate(date);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int day = gc.get(Calendar.DAY_OF_MONTH);
		int month = gc.get(Calendar.MONTH) + 1;
		int year =  gc.get(Calendar.YEAR);
		
		String monthStr = Integer.toString(month);
		if (monthStr.length()==1)
			monthStr = "0" + monthStr;
		String dayStr = Integer.toString(day);
		if (dayStr.length()==1)
			dayStr = "0" + dayStr;
		String yearStr = Integer.toString(year % 100);
		if (yearStr.length()==1)
			yearStr = "0" + yearStr;
		
		//  "http://www.klsedaily.com/data/2017/06/klse_05_06_17.txt";
		
		String fileName = "/klse_" + dayStr + "_" + monthStr + "_" + yearStr +  ".txt";
		String source = "http://www.klsedaily.com/data/" + year + "/" + monthStr + fileName;
		url = new URL( source );
		log.info("Using url: " +url);
		
		setFolder(folder + fileName);
		connectUrl();
		readData(); 
	}
	
	private void readData() throws IOException {
		String line;
		
		FileWriter fw = new FileWriter(folder);
		BufferedWriter bw = new BufferedWriter(fw);
		while ((line = bins.readLine()) != null)
		{
			log.info(line);
			bw.write(line);
		}
		bw.close();
	}

	private void connectUrl() throws UrlParserException, FileNotFoundException
	{
		int tries = 0;
		while (true)
		{
			int backoff = 5;
			try
			{
				URLConnection connection  = url.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0");
				InputStreamReader ins = new InputStreamReader( connection.getInputStream() );
				bins = new BufferedReader( ins );
				break;
			}
			catch (IOException e)
			{
				if (e.getClass().equals(FileNotFoundException.class))
				{
					throw (FileNotFoundException) e;
				}
				e.printStackTrace();
				log.warn( "Couldn't connect - backing off from : " + url + " for : " + backoff + " secs");
				try
				{
					Thread.sleep( backoff*1000 );
					if (tries++ > 5)
						throw new UrlParserException( "Maximum tries to connect exceeded" );
				}
				catch (InterruptedException ie)
				{
					log.error( ie );
					throw new UrlParserException( ie.getMessage() );
				}
			}
		}
	}
	
	public Date getDate() {
		return date;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public static void download(Date from,Date to,String folder)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(from);
		
		while (!gc.after(to))
		{
			try {
				@SuppressWarnings("unused")
				SecurityDailyDownload sdd = new SecurityDailyDownload(gc.getTime(), folder);
			} catch (UrlParserException e) {
				e.printStackTrace();
			}
			catch (FileNotFoundException e)
			{
			   log.error("Url not found. Is this a buiness day? (" + gc.getTime() + ")");
			} catch (IOException e) {
				e.printStackTrace();
			}
			gc.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

	public static void main(String[] args)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_YEAR, -3);
		try {
			
			String folder = "/home/pmk/candice/dailyupdates/" + gc.get(Calendar.YEAR);
			@SuppressWarnings("unused")
			SecurityDailyDownload sdd = new SecurityDailyDownload(gc.getTime(), folder);
		} catch (UrlParserException e) {
			e.printStackTrace();
			
		}catch (FileNotFoundException e)
		{
			log.error("Url not found. Is this a buiness day? (" + gc.getTime() + ")");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
