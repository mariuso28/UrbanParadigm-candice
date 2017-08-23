package org.cz.json.portfolio;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.mp.PortfolioEntryMp;

public class PortfolioWatch {

	private long id;
	private String ticker;
	private List<PortfolioEntryI> entries = new ArrayList<PortfolioEntryI>();
	
	public PortfolioWatch()
	{
	}
	
	public PortfolioWatch(String ticker)
	{
		setTicker(ticker);
	}
	
	public PortfolioEntry getEntryById(long id)
	{
		for (PortfolioEntryI pei : entries)
		{
			PortfolioEntry pe = (PortfolioEntry) pei;
			if (pe.getId()==id)
				return pe;
		}
		return null;
	}
		
	public PortfolioEntry createPortfolioEntry(PortfolioEntryType type)
	{
		PortfolioEntry pe = null;
		if (type.equals(PortfolioEntryType.HockeyStick))
			pe = new PortfolioEntryHs(this.getTicker());
		else
			pe = new PortfolioEntry(null,this.getTicker());

		entries.add(pe);
		return pe;
	}
	
	public PortfolioEntry createPortfolioEntry(PortfolioEntryType type, double price) {
		PortfolioEntry pe = null;
		if (type.equals(PortfolioEntryType.MarketPrice))
		{
			pe = new PortfolioEntryMp(this.getTicker());
			PortfolioEntryMp pmp = (PortfolioEntryMp) pe;
			pmp.setTarget(price);
		}
		else
			pe = new PortfolioEntry(null,this.getTicker());
		entries.add(pe);
		return pe;
	}
	
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	

	public List<PortfolioEntryI> getEntries() {
		return entries;
	}

	public void setEntries(List<PortfolioEntryI> entries) {
		this.entries = entries;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "PortfolioWatch [id=" + id + ", ticker=" + ticker + ", entries=" + entries + "]";
	}
	
}
