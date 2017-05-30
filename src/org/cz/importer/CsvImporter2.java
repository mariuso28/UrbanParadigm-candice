package org.cz.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.importer.util.MoveFile;
import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;
import org.cz.scrape.SecurityGrab;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

public class CsvImporter2 {

	private static Logger log = Logger.getLogger(CsvImporter2.class);
	private String path;
	private String gotToWeb;
	private Home home;
	
	public CsvImporter2(Home home, String path,String goToWeb)
	{
		setHome(home);
		setPath(path);
		setGotToWeb(goToWeb);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getGotToWeb() {
		return gotToWeb;
	}

	public void setGotToWeb(String gotToWeb) {
		this.gotToWeb = gotToWeb;
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
	    	
	    	log.info(sd);
	    	
	    	Security sec = home.getSecurityByCode(sd.getCode());
	    	if (sec == null && gotToWeb.equals("on"))
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
	    	
	    	if (sec==null)
	    		continue;
	    	
	    	if (home.getSecurityDaily(sec.getTicker(),sd.getDate())!=null)
	    		continue;
	    	
	    	SecurityDaily secDaily = new SecurityDaily();
	    	secDaily.setTicker(sec.getTicker());
	    	secDaily.setDate(sd.getDate());
	    	secDaily.setHigh(sd.getHigh());
	    	secDaily.setOpen(sd.getOpen());
	    	secDaily.setClose(sd.getClose());
	    	secDaily.setLow(sd.getLow());
	    	secDaily.setVolume(sd.getVol());
	    	
	    	home.storeSecurityDaily(secDaily);
	    	cnt++;
	    }
	   
	    log.info("Loaded "  + cnt + " Securities");
	    
	    /*
	    GregorianCalendar gc = new GregorianCalendar();
	    gc.clear();
	    gc.set(2017,4,26);
	    List<SecurityDaily> sdlist = home.getSecurityDailys(gc.getTime());
	    for (SecurityDaily sd : sdlist)
	    {
	    	log.info(sd);
	    }
	    */
	    
	}
	
	public static void importFilesForFolder(final String folder,final String loaded,String goToWeb,Home home) {
	    
		File dir = new File(folder);
		for (final File fileEntry : dir.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	            log.info("Importing :" +  fileEntry.getAbsolutePath());
	            CsvImporter2 csv = new CsvImporter2(home,fileEntry.getAbsolutePath(),goToWeb);
	    		try {
	    			csv.readSecurities();
	    			if (loaded=="null")
	    				MoveFile.deleteFile(fileEntry.getAbsolutePath());
	    			else
	    				MoveFile.move(fileEntry.getAbsolutePath(),loaded + "/" + fileEntry.getName());
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        }
	    }
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		String folder = "/home/pmk/candice/dailyupdates/";
		String loaded = "/home/pmk/candice/loaded/";
		String goToWeb = "off";
		
		if (args.length>0)
			folder = args[0];
		if (args.length>1)
			loaded = args[1];
		if (args.length>2)
			goToWeb = args[2];
		
		try
		{
			log.info("Running with folder : " + folder + " loaded dir : " + loaded); 
			importFilesForFolder(folder,loaded,goToWeb,home);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}
