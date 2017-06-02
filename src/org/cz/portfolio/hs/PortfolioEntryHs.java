package org.cz.portfolio.hs;

public class PortfolioEntryHs 
{
	private long id;
	private PortfolioHsStatus status;
	private int dayCount;
	private double ceiling;
	
	public PortfolioEntryHs()
	{
		status = PortfolioHsStatus.SEEK;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
