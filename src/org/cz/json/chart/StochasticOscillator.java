package org.cz.json.chart;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.security.SecurityDaily;

public class StochasticOscillator {
		
	private int periods = 14;
	private List<StochasticOscillatorEntry> entries;
	
	public StochasticOscillator()
	{
	}
	
	public static StochasticOscillator createStochasticOscillator(List<SecurityDaily> sdList)
	{
		StochasticOscillator so = new StochasticOscillator();
		so.calculate(sdList);
		return so;
	}
	
	public static StochasticOscillator createStochasticOscillator(List<SecurityDaily> sdList,int periods)
	{
		StochasticOscillator so = new StochasticOscillator();
		so.setPeriods(periods);
		so.calculate(sdList);
		return so;
	}
	
	private void calculate(List<SecurityDaily> sdList) {
		entries = new ArrayList<StochasticOscillatorEntry>();
		for (SecurityDaily sd : sdList)
		{
			StochasticOscillatorEntry so =  new StochasticOscillatorEntry(sd.getDate(),sd.getClose(),sd.getHigh(),sd.getLow());
			entries.add(so);
		}
		for (int last=sdList.size()-1; last>=periods; last--)
		{
			calcHighLow(last);
		}
		calcKLine();
		calcDLine();
	}

	private void calcHighLow(int last) {
		
		double high = Double.MIN_VALUE;
		double low = Double.MAX_VALUE;
		for (int i=last; i>last-periods; i--)
		{
			StochasticOscillatorEntry so = entries.get(i);
			if (high<so.getHigh())
				high = so.getHigh();
			if (low>so.getLow())
				low = so.getLow();
		}
		StochasticOscillatorEntry so = entries.get(last);
		so.setHighestHigh(high);
		so.setLowestLow(low);
	}
	
	private void calcKLine() {
		
		for (int i=periods; i<entries.size(); i++)
		{
			StochasticOscillatorEntry so = entries.get(i);
			double k = ((so.getClose() - so.getLowestLow()) / (so.getHighestHigh() - so.getLowestLow())) * 100;	
			so.setkLine(k);
		}
	}

	private void calcDLine() {
		
		for (int i=periods+3; i<entries.size(); i++)
		{
			StochasticOscillatorEntry entry = entries.get(i);
			entry.setdLine(sma3(i));
		}
	}
	
	private double sma3(int last) {
		
		double total = 0;
		for (int i=0; i<3; i++)
		{
			total += entries.get(last--).getkLine();
		}
		return total / 3.0;
	}

	public List<StochasticOscillatorEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<StochasticOscillatorEntry> entries) {
		this.entries = entries;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	
}
