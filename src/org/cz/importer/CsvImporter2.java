package org.cz.importer;

import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.scrape.SecurityGrab;
import org.cz.security.Security;
import org.cz.security.SecurityDaily;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class CsvImporter2 {

	private static Logger log = Logger.getLogger(CsvImporter2.class);
	private String path;
	private Home home;
	
	public CsvImporter2(Home home, String path)
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
	    
	    for (SecurityDailyImport2 sd : list)
	    {
	    	if (sd.getCode()==null)
	    		continue;
	    	
	    	log.info(sd);
	    	
	    	Security sec = home.getSecurityByCode(sd.getCode());
	    	if (sec == null)
	    	{
	    		try
	    		{
	    			SecurityGrab sg = new SecurityGrab(sd.getCode());
	    			if (sg.getError()!=null)
	    			{
	    				log.warn("Could not grab security : " + sd.getCode());
	    				continue;
	    			}
	    			sec = new Security(sg.getName(),sg.getTicker(),sg.getCode());
		    		home.storeSecurity(sec);
	    		}
	    		catch (Exception e)
	    		{
	    			log.error("Could not grab security : " + sd.getCode() + " - " + e.getMessage());
	    			continue;
	    		}
	    	}
	    	
	    	SecurityDaily secDaily = new SecurityDaily();
	    	secDaily.setTicker(sec.getTicker());
	    	secDaily.setDate(sd.getDate());
	    	secDaily.setHigh(sd.getHigh());
	    	secDaily.setOpen(sd.getOpen());
	    	secDaily.setClose(sd.getClose());
	    	secDaily.setLow(sd.getLow());
	    	secDaily.setVolume(sd.getVol());
	    	
	    	home.storeSecurityDaily(secDaily);
	    	
	    }
	    
	    GregorianCalendar gc = new GregorianCalendar();
	    gc.clear();
	    gc.set(2017,4,26);
	    List<SecurityDaily> sdlist = home.getSecurityDailys(gc.getTime());
	    for (SecurityDaily sd : sdlist)
	    {
	    	log.info(sd);
	    }
	    
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		
		CsvImporter2 csv = new CsvImporter2(home,"/home/pmk/workspace/candice/sample/klse_26_05_17.txt");
		try {
			csv.readSecurities();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
