package org.cz.json.chart;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.security.SecurityDaily;

public class MovingAverageMacd {
	
	private int macd9 = 9;
	private int macd12 = 12;
	private int macd26 = 26;
	private List<MovingAverageMacdEntry> entries;
	
	public MovingAverageMacd()
	{
	}
	
	public static MovingAverageMacd createMovingAverage(List<SecurityDaily> sdList)
	{
		MovingAverageMacd ma = new MovingAverageMacd();
		ma.calculate(sdList);
		return ma;
	}
	
	public static MovingAverageMacd createMovingAverage(List<SecurityDaily> sdList,int macd9,int macd12,int macd26)
	{
		MovingAverageMacd ma = new MovingAverageMacd();
		ma.setMacd9(macd9);
		ma.setMacd12(macd12);
		ma.setMacd26(macd26);
		ma.calculate(sdList);
		return ma;
	}
	
	private void calculate(List<SecurityDaily> sdList)
	{
		entries = new ArrayList<MovingAverageMacdEntry>();
		if (sdList.size()<=macd26)
			return;
		calc1226(sdList); 
		calcMacdLine();
		calcSignal();
		calcMacdHistogram();
	}
	
	private void calc1226(List<SecurityDaily> sdList) {
		
		MovingAverage mv26 = MovingAverage.createMovingAverage(sdList,macd26);
		for (int i=0; i<macd26; i++)
		{
			entries.add(null);
		}
		
		for (int i=macd26; i<sdList.size(); i++)
		{
			MovingAverageMacdEntry mae = new MovingAverageMacdEntry(sdList.get(i).getDate(),sdList.get(i).getClose());
			mae.setMacd26(mv26.getEntries().get(i).getEma());
			entries.add(mae);
		}
		
		MovingAverage mv12 = MovingAverage.createMovingAverage(sdList,macd12);
		for (int i=macd26; i<sdList.size(); i++)
		{
			entries.get(i).setMacd12(mv12.getEntries().get(i).getEma());
		}
		
		MovingAverage mv9 = MovingAverage.createMovingAverage(sdList,macd9);
		for (int i=macd26; i<sdList.size(); i++)
		{
			entries.get(i).setMacd9(mv9.getEntries().get(i).getEma());
		}
	}

	private void calcMacdHistogram() {
		
		for (int i=macd26+macd9+1; i<entries.size(); i++)
		{
			entries.get(i).calcMacdHistogram();
		}
	}

	private void calcSignal()
	{
		calcEmas();
	}
	
	private void calcEmas()
	{
		double lastEma = calcSma(macd26,macd26+macd9);
		double multiplier = 2 / ((macd9 + 1) * 1.0);
		for (int i=macd26+macd9+1; i<entries.size(); i++)
		{
			double ema = ((entries.get(i).getMacdLine() - lastEma) * multiplier) + lastEma;
			entries.get(i).setSignalLine(ema);
			lastEma = ema;
		}
	}
	
	private double calcSma(int start,int end)
	{
		double total = 0.0;
		for (int i=start; i<=end; i++)
			total += entries.get(i).getMacdLine();
		return total/(end-start+1);
	}
	
	private void calcMacdLine()
	{
		int last = entries.size()-1;
		while (entries.get(last)!=null)
		{
			entries.get(last).calcMacdLine();
			last--;
		}
	}
	
	public List<MovingAverageMacdEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<MovingAverageMacdEntry> entries) {
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
