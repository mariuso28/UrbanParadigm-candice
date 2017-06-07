package org.cz.json.chart;

import java.util.List;

import org.apache.log4j.Logger;
import org.cz.json.security.SecurityDaily;

public class MovingAverage {

	private static Logger log = Logger.getLogger(MovingAverage.class);
	
	private int macd9 = 9;
	private int macd12 = 12;
	private int macd26 = 26;
	private MovingAverageEntry[] entries;
	
	public MovingAverage()
	{
	}
	
	public static MovingAverage createMovingAverage(List<SecurityDaily> sdList)
	{
		MovingAverage ma = new MovingAverage();
		ma.calculate(sdList);
		return ma;
	}
	
	public static MovingAverage createMovingAverage(List<SecurityDaily> sdList,int macd9,int macd12,int macd26)
	{
		MovingAverage ma = new MovingAverage();
		ma.setMacd9(macd9);
		ma.setMacd12(macd12);
		ma.setMacd26(macd26);
		ma.calculate(sdList);
		return ma;
	}
	
	private void calculate(List<SecurityDaily> sdList)
	{
		entries = new MovingAverageEntry[sdList.size()];
		calc1226(sdList); 
		calcMacdLine();
		calcSignal();
		calcMacdHistogram();
	}
	
	private void calc1226(List<SecurityDaily> sdList) {
		
		log.info("calc1226");
		
		int last = sdList.size()-1;
		while (last-macd26 > 0)
		{
			SecurityDaily lastSd = sdList.get(last);
			double ma = calcMovingAverage(sdList.subList(last-macd26,last));
			MovingAverageEntry mae = new MovingAverageEntry(lastSd.getDate(),lastSd.getClose());
			mae.setMacd26(ma);
			entries[last] = mae;
			last--;
		}
		
		last = sdList.size()-1;
		while (last-macd26 > 0)
		{
			double ma = calcMovingAverage(sdList.subList(last-macd12,last));
			entries[last].setMacd12(ma);;
			last--;
		}
	}

	private void calcMacdHistogram() {
		
		log.info("calcMacdHistogram");
		
		int last = entries.length-1;
		while (entries[last]!=null)
		{
			entries[last].calcMacdHistogram();
			last--;
		}
	}

	private void calcSignal()
	{
		log.info("calcSignal");
		int last = entries.length-1;
		while (last-macd9 >0)
		{
			int start = last-macd9;
			if (entries[start]==null)
				break;
			calcMovingAverage(start,last);
			last--;
		}
	}
	
	private double calcMovingAverage(int start,int end)
	{
		double total = 0.0;
		for (; start<=end; start++)
			total += entries[start].getMacdLine();
		return total/(end-start)+1;
	}
	
	private void calcMacdLine()
	{
		log.info("calcMacdLine");
		int last = entries.length-1;
		while (entries[last]!=null)
		{
			entries[last].calcMacdLine();
			last--;
		}
	}
	
	private double calcMovingAverage(List<SecurityDaily> range)
	{
		double total = 0.0;
		for (SecurityDaily element : range)
			total += element.getClose();
		return total/range.size();
	}

	
	public MovingAverageEntry[] getEntries() {
		return entries;
	}

	public void setEntries(MovingAverageEntry[] entries) {
		this.entries = entries;
	}

	public int getMacd9() {
		return macd9;
	}

	public void setMacd9(int macd9) {
		this.macd9 = macd9;
	}

	public int getMacd12() {
		return macd12;
	}

	public void setMacd12(int macd12) {
		this.macd12 = macd12;
	}

	public int getMacd26() {
		return macd26;
	}

	public void setMacd26(int macd26) {
		this.macd26 = macd26;
	}

}
