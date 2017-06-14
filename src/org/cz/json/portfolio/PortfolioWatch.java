package org.cz.json.portfolio;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.portfolio.hs.PortfolioEntryHs;

public class PortfolioWatch {

	private long id;
	private String ticker;
	private List<PortfolioEntry> entries = new ArrayList<PortfolioEntry>();
	
	public PortfolioWatch()
	{
	}
	
	public PortfolioWatch(String ticker)
	{
		setTicker(ticker);
	}
	
	public PortfolioEntry getEntryById(long id)
	{
		for (PortfolioEntry pe : entries)
			if (pe.getId()==id)
				return pe;
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
	
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	

	public List<PortfolioEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PortfolioEntry> entries) {
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
