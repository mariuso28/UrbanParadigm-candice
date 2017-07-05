package org.cz.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.json.chart.Rsi;
import org.cz.json.chart.RsiEntry;
import org.cz.json.security.SecurityDaily;

public class RsiSample {

	// @SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(RsiSample.class);
	
	public static List<SecurityDaily> create() throws FileNotFoundException, IOException
	{
		List<SecurityDaily> sdList = new ArrayList<SecurityDaily>();
		
		try(BufferedReader br = new BufferedReader(new FileReader("/home/pmk/workspace/candice/sample/rsi-sample.txt"))) {
		    String line = br.readLine();

		    while (line != null && !line.isEmpty()) {
		       
		    	log.info("Read : " + line);
		       sdList.add(createSd(line));
		       line = br.readLine();
		    }
		 
		}
		
		return sdList;
	}
	
	private static SecurityDaily createSd(String line)
	{
		SecurityDaily sd = new SecurityDaily();
		sd.setTicker("TICKER");
		String[] toks=line.split(",");
		
		String dstr = toks[0].trim();
		String cstr = toks[1].trim();
		
		toks=dstr.split("-");
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Integer.parseInt(toks[2])+2000,Integer.parseInt(toks[1])-1,Integer.parseInt(toks[0]));
		sd.setDate(gc.getTime());
		sd.setClose(Double.parseDouble(cstr));
		
		log.info(sd.getDate() + " " + sd.getClose());
		
		return sd;
	}

	private static void logEntry(RsiEntry rsiEntry, BufferedWriter bw) throws IOException {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		String line = sdf.format(rsiEntry.getDate()) + ",";
		
			line += String.format("%.2f",rsiEntry.getClose()) + "," + String.format("%.2f",rsiEntry.getChange()) + "," + String.format("%.2f",rsiEntry.getGain()) + "," 
					+ String.format("%.2f",rsiEntry.getLoss()) + "," +
					String.format("%.2f",rsiEntry.getAvgGain()) + "," + String.format("%.2f",rsiEntry.getAvgLoss()) + "," 
					+ String.format("%.2f",rsiEntry.getRs()) + "," + String.format("%.2f",rsiEntry.getRsi());
		bw.write(line);
		bw.newLine();
	}
	
	public static void main(String[] args)
	{
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("/home/pmk/workspace/candice/sample/rsi-sample.csv"));
			bw.write("Date,Close,Change,Gain,Loss,Avg Gain,Avg Loss,RS,RSI");
			bw.newLine();
			List<SecurityDaily> sdList = create();
			
			Rsi rsi = Rsi.createRsi(sdList);
			int cnt=0;
			for (SecurityDaily sd : sdList)
			{
				RsiEntry rsiEntry = rsi.getEntries().get(cnt++);
				if (rsiEntry != null)
					logEntry(rsiEntry,bw);
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}


