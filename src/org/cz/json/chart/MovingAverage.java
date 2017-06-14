package org.cz.json.chart;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.security.SecurityDaily;

public class MovingAverage {
	
	private int days = 10;
	double multiplier;
	private List<MovingAverageEntry> entries;
	
	public MovingAverage()
	{
	}
	
	public static MovingAverage createMovingAverage(List<SecurityDaily> sdList)
	{
		MovingAverage ma = new MovingAverage();
		ma.calculate(sdList);
		return ma;
	}
	
	public static MovingAverage createMovingAverage(List<SecurityDaily> sdList,int days)
	{
		MovingAverage ma = new MovingAverage();
		ma.setDays(days);
		ma.calculate(sdList);
		return ma;
	}
	
	private void setDays(int days) {
		this.days = days;
	}

	private void calculate(List<SecurityDaily> sdList)
	{
		entries = new ArrayList<MovingAverageEntry>();
		if (sdList.size()<=days)
			return;
		for (int i=0; i<days; i++)
		{
			entries.add(null);
		}
		for (int i=days; i<sdList.size(); i++)
		{
			SecurityDaily sd = sdList.get(i);
			entries.add(new MovingAverageEntry(sd.getDate(),sd.getClose()));
		}	
		calcSmas(sdList); 
		multiplier = 2 / ((days + 1) * 1.0);
		calcEmas();
	}

	private void calcSmas(List<SecurityDaily> sdList) {
		int last = sdList.size()-1;
		while (true)
		{
			double ma = calcMovingAverage(sdList.subList(last-days,last));
			MovingAverageEntry mae = entries.get(last);
			mae.setSma(ma);
			if (last==days)
				break;
			last--;
		}
	}
	
	private void calcEmas()
	{
		double lastEma = entries.get(days).getSma();
		for (int i=days; i<entries.size(); i++)
		{
			double ema = ((entries.get(i).getClose() - lastEma) * multiplier) + lastEma;
			entries.get(i).setEma(ema);
			lastEma = ema;
		}
	}

	private double calcMovingAverage(List<SecurityDaily> range)
	{
		double total = 0.0;
		for (SecurityDaily element : range)
			total += element.getClose();
		return total/range.size();
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public List<MovingAverageEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<MovingAverageEntry> entries) {
		this.entries = entries;
	}

	public int getDays() {
		return days;
	}

	
}
