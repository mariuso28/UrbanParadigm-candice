package org.cz.importer;

import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class CsvImporter {

	private static Logger log = Logger.getLogger(CsvImporter.class);
	private String path;
	private Home home;
	
	public CsvImporter(Home home, String path)
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
		
		CsvToBean<SecurityDailyImport> csvToBean = new CsvToBean<SecurityDailyImport>();

		Map<String, String> columnMapping = new HashMap<String, String>();
		// <Name>,<TICKER>,<PER>,<DATE>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>
		
		columnMapping.put("<Name>", "name");
		columnMapping.put("<TICKER>", "ticker");
		columnMapping.put("<DATE>", "dateString");
		columnMapping.put("<OPEN>", "open");
		columnMapping.put("<HIGH>", "high");
		columnMapping.put("<LOW>", "low");
		columnMapping.put("<CLOSE>", "close");
		columnMapping.put("<VOL>", "vol");

		HeaderColumnNameTranslateMappingStrategy<SecurityDailyImport> strategy = new HeaderColumnNameTranslateMappingStrategy<SecurityDailyImport>();
		strategy.setType(SecurityDailyImport.class);
		strategy.setColumnMapping(columnMapping);

		List<SecurityDailyImport> list = csvToBean.parse(strategy, reader);
	    reader.close();
	    
	    for (SecurityDailyImport sd : list)
	    {
	    	if (sd.getTicker()==null)
	    		continue;
	    	
	    //	log.info(sd);
	    	Security sec = home.getSecurity(sd.getTicker());
	    	if (sec == null)
	    	{
	    		sec = new Security(sd.getName(),sd.getTicker(),null);
	    		home.storeSecurity(sec);
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
	    gc.set(2017,4,12);
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
		
		CsvImporter csv = new CsvImporter(home,"/home/pmk/workspace/candice/sample/KLSE_20170512.txt");
		try {
			csv.readSecurities();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
