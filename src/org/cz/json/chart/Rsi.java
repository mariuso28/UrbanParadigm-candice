package org.cz.json.chart;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.security.SecurityDaily;

public class Rsi {
	
	private int periods = 14;
	private List<RsiEntry> entries;
	
	public Rsi()
	{
	}
	
	public static Rsi createRsi(List<SecurityDaily> sdList)
	{
		Rsi rsi = new Rsi();
		rsi.calculate(sdList);
		return rsi;
	}
	
	public static Rsi createRsi(List<SecurityDaily> sdList,int periods)
	{
		Rsi rsi = new Rsi();
		rsi.setPeriods(periods);
		rsi.calculate(sdList);
		return rsi;
	}
	
	private void setPeriods(int periods) {
		this.periods = periods;
	}

	private void calculate(List<SecurityDaily> sdList)
	{
		entries = new ArrayList<RsiEntry>();
		if (sdList.size()<=periods)
			return;
		
		entries.add(null);
		double lastClose = 0;
		for (int i=0; i<sdList.size(); i++)
		{
			SecurityDaily sd = sdList.get(i);
			RsiEntry re = new RsiEntry(sd.getDate(),sd.getClose());
			if (lastClose!=0)
				re.setChange(sd.getClose()-lastClose);
			entries.add(re);
			lastClose = sd.getClose();
		}	
		
		calculateFirstGainLosses(sdList);
		calculateSubsequentGainLosses(sdList);
	}

	private void calculateSubsequentGainLosses(List<SecurityDaily> sdList) {
		
		for (int i=periods+2; i<entries.size(); i++)
		{
			if (i>=sdList.size())
				break;
			
			RsiEntry re = entries.get(i);
			RsiEntry pre = entries.get(i-1);
			
			double avGain = (pre.getAvgGain() * 13 + re.getGain()) / periods;
			double avLoss = (pre.getAvgLoss() * 13 + re.getLoss()) / periods;
			
			re.setAvgGain(avGain);
			re.setAvgLoss(avLoss);
			re.calcRsi();
		}
	}
	
	private void calculateFirstGainLosses(List<SecurityDaily> sdList) {
	
		double avLoss = 0;
		double avGain = 0;
		for (int i=1; i<periods+1; i++)
		{
			RsiEntry rp = entries.get(i);
			if (rp==null)
				continue;
			if (rp.getChange()>0)
				avGain+=rp.getChange();
			else
				avLoss+=(rp.getChange() * -1.0);
		}
		RsiEntry rp = entries.get(periods+1);
		rp.setAvgGain(avGain/periods);
		rp.setAvgLoss(avLoss/periods);
		rp.calcRsi();
	}

	public List<RsiEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<RsiEntry> entries) {
		this.entries = entries;
	}

}
