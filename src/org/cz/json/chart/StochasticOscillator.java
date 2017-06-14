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
		for (int i=0; i<periods; i++)
			entries.add(null);
		for (int i=periods; i<sdList.size(); i++)
		{
			SecurityDaily sd = sdList.get(i);
			entries.add(new StochasticOscillatorEntry(sd.getDate(),sd.getClose()));	
		}
		
		for (int last=sdList.size()-1; last>=periods; last--)
		{
			calcHighLow(last-periods,last,sdList);
		}
	}

	private void calcHighLow(int first, int last, List<SecurityDaily> sdList) {
		
		SecurityDaily sd = sdList.get(first+1);
		double high = sd.getHigh();
		double low = sd.getLow();
		first++;
		for (; first<=last; first++)
		{
			sd = sdList.get(first);
			if (high<sd.getHigh())
				high = sd.getHigh();
			if (low>sd.getLow())
				low = sd.getLow();
		}
		
		double k=0;
		double close = sdList.get(last).getClose();
		if (close != low)
			k = ((close - low) / (high - low)) * 100;	
		
		StochasticOscillatorEntry entry = entries.get(last);
		if (k==0)
		{
			if (last!=entries.size()-1)
			{
				StochasticOscillatorEntry lentry = entries.get(last+1);
				if (lentry!=null)
					k = lentry.getkLine();
			}
		}
		
		entry.setkLine(k);
		if (last>periods+2)
			entry.setdLine(sma3(last));
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
