package org.cz.json.portfolio.hs;

import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryType;

public class PortfolioEntryHs extends PortfolioEntry
{
	private PortfolioHsStatus status;
	private int dayCount;
	private double ceiling;
	
	public PortfolioEntryHs()
	{
	}
	
	public PortfolioEntryHs(String securityTicker)
	{
		super(PortfolioEntryType.HockeyStick,securityTicker);
		status = PortfolioHsStatus.SEEK;
	}
	
	public PortfolioHsStatus getStatus() {
		return status;
	}

	public void setStatus(PortfolioHsStatus status) {
		this.status = status;
	}

	public int getDayCount() {
		return dayCount;
	}

	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}

	public double getCeiling() {
		return ceiling;
	}

	public void setCeiling(double ceiling) {
		this.ceiling = ceiling;
	}

}
