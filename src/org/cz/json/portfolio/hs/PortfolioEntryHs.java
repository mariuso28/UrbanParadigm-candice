package org.cz.json.portfolio.hs;

import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryType;

public class PortfolioEntryHs extends PortfolioEntry
{
	private String portfolioEntryHsUnique = PortfolioEntryHs.class.getName();
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

	public String getPortfolioEntryHsUnique() {
		return portfolioEntryHsUnique;
	}

	public void setPortfolioEntryHsUnique(String portfolioEntryHsUnique) {
		this.portfolioEntryHsUnique = portfolioEntryHsUnique;
	}

	@Override
	public String toString() {
		return "PortfolioEntryHs [portfolioEntryHsUnique=" + portfolioEntryHsUnique + ", status=" + status
				+ ", dayCount=" + dayCount + ", ceiling=" + ceiling + ", getSecurityTicker()=" + getSecurityTicker()
				+ ", getType()=" + getType() + "]";
	}

	

}
