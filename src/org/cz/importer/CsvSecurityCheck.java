package org.cz.importer;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.json.security.Security;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class CsvSecurityCheck {

	private static Logger log = Logger.getLogger(CsvSecurityCheck.class);
	private String path;
	private Home home;
	
	public CsvSecurityCheck(Home home, String path)
	{
		setHome(home);
		setPath(path);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private void setHome(Home home) {
		this.home = home;
	}

	public void read() throws IOException
	{
		CSVReader reader = new CSVReader(new FileReader(path));
	     String [] nextLine;
	     while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	        System.out.println(nextLine[0] + nextLine[1]);
	     }
	     reader.close();
	}
	
	public void readSecurities() throws IOException
	{
		CSVReader reader = new CSVReader(new FileReader(path));
		log.info("Importing securityDailys from : " + path);
		
		CsvToBean<SecurityDailyImport2> csvToBean = new CsvToBean<SecurityDailyImport2>();

		ColumnPositionMappingStrategy<SecurityDailyImport2> strategy = new ColumnPositionMappingStrategy<SecurityDailyImport2>();
	    strategy.setType(SecurityDailyImport2.class);
	    
	    /*
	     * private String code;
			private String d;
			private Date date;
			private Double open;
			private Double high;
			private Double low;
			private Double close;
			private Double vol;
			private Double end;
	     */
	    
	    
	    String[] columns = new String[] {"code", "d", "dateString", "open", "high", "low", "close", "vol", "end"}; 
	    strategy.setColumnMapping(columns);
	    strategy.setType(SecurityDailyImport2.class);
		
		List<SecurityDailyImport2> list = csvToBean.parse(strategy, reader);
	    reader.close();
	    
	    int cnt=0;
	    
	    for (SecurityDailyImport2 sd : list)
	    {
	    	if (sd.getCode()==null || sd.getCode().isEmpty())
	    		continue;
	    	
	 //  	log.info(sd);
	    	
	    	Security sec = home.getSecurityByCode(sd.getCode());
	    	if (sec == null)
	    	{
	    		log.info("MISSING : " + sd.getCode());
	    		cnt++;
	        }
	    	
	    	if (sec==null)
	    		continue;
	    }
	   
	    log.info("Missing "  + cnt + " Securities");
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		String path = "/home/pmk/candice/dailyupdates/2017/klse_29_05_17.txt";
		
		
		if (args.length>0)
			path = args[0];
		
		try
		{
			log.info("Running with path : " + path); 
			 CsvSecurityCheck csv = new CsvSecurityCheck(home,path);
			 csv.readSecurities();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
