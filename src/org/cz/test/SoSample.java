package org.cz.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.json.chart.StochasticOscillator;
import org.cz.json.chart.StochasticOscillatorEntry;
import org.cz.json.security.SecurityDaily;

public class SoSample {

	// @SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(SoSample.class);
	
	public static List<SecurityDaily> create() throws FileNotFoundException, IOException, ParseException
	{
		List<SecurityDaily> sdList = new ArrayList<SecurityDaily>();
		
		try(BufferedReader br = new BufferedReader(new FileReader("/home/pmk/workspace/candice/sample/so-sample.txt"))) {
		    String line = br.readLine();				// chuck away

		    line = br.readLine();
		    while (line != null && !line.isEmpty()) {
		       
		    	log.info("Read : " + line);
		       sdList.add(createSd(line));
		       line = br.readLine();
		    }
		 
		}
		
		return sdList;
	}
	
	private static SecurityDaily createSd(String line) throws ParseException
	{
		SecurityDaily sd = new SecurityDaily();
		sd.setTicker("TICKER");
		String[] toks=line.split(",");
		
		String dstr = toks[0].trim();
		String hstr = toks[1].trim();
		String lstr = toks[2].trim();
		String cstr = null;
		if (toks.length>3)
			cstr = toks[3].trim();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MMM-dd");
		sd.setDate(sdf.parse(dstr));
		
		sd.setHigh(Double.parseDouble(hstr));
		sd.setLow(Double.parseDouble(lstr));
		
		if (cstr!=null)
			sd.setClose(Double.parseDouble(cstr));
		else
			sd.setClose(0.0);
		
		log.info(sd.getDate() + " " + sd.getHigh() + " " + sd.getLow()+ " " + sd.getClose());
		
		return sd;
	}

	private static void logEntry(StochasticOscillator so,int index,StochasticOscillatorEntry soEntry,BufferedWriter bw) throws IOException {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		String line = sdf.format(soEntry.getDate()) + ",";
		
		line += String.format("%.2f",soEntry.getHigh()) + "," + String.format("%.2f",soEntry.getLow());
		if (index >= so.getPeriods())
		{
				line += "," + String.format("%.2f",soEntry.getHighestHigh()) + "," 
			    + String.format("%.2f",soEntry.getLowestLow()) + ","
				+ String.format("%.2f",soEntry.getClose()) + ","
				+ String.format("%.2f",soEntry.getkLine()) + ","
				+ String.format("%.2f",soEntry.getdLine());
		}
		bw.write(line);
		bw.newLine();
	}
	
	public static void main(String[] args) 
	{
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("/home/pmk/workspace/candice/sample/so-sample.csv"));
			bw.write("Date,High,Low,Hisgest High,Lowest Low,close,kline,dLine");
			bw.newLine();
			List<SecurityDaily> sdList = create();
			
			StochasticOscillator so = StochasticOscillator.createStochasticOscillator(sdList);
			int cnt=0;
			for (StochasticOscillatorEntry soEntry : so.getEntries())
			{
				logEntry(so,cnt++,soEntry,bw);
			}
			bw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}


