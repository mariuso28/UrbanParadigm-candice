package org.cz.json.portfolio.mp;

import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryType;

public class PortfolioEntryMp extends PortfolioEntry
{
	private String portfolioEntryMpUnique = PortfolioEntryMp.class.getName();
	private PortfolioMpStatus status;
	private double target;
	
	public PortfolioEntryMp()
	{
	}
	
	public PortfolioEntryMp(String securityTicker)
	{
		super(PortfolioEntryType.MarketPrice,securityTicker);
		status = PortfolioMpStatus.SEEK;
	}

	public PortfolioMpStatus getStatus() {
		return status;
	}

	public void setStatus(PortfolioMpStatus status) {
		this.status = status;
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}

	public String getPortfolioEntryMpUnique() {
		return portfolioEntryMpUnique;
	}

	public void setPortfolioEntryMpUnique(String portfolioEntryMpUnique) {
		this.portfolioEntryMpUnique = portfolioEntryMpUnique;
	}

}
