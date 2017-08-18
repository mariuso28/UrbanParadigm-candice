package org.cz.json.portfolio;

import java.util.Date;

public class PortfolioProfitLoss {

	private String traderEmail;
	private String portfolioName;
	private String ticker;
	private Date startDate;
	private Date endDate;
	private double buy;
	private double sell;
	private double pl;
	
	public PortfolioProfitLoss()
	{
	}

	public String getTraderEmail() {
		return traderEmail;
	}

	public void setTraderEmail(String traderEmail) {
		this.traderEmail = traderEmail;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getBuy() {
		return buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public double getSell() {
		return sell;
	}

	public void setSell(double sell) {
		this.sell = sell;
	}

	public double getPl() {
		return pl;
	}

	public void setPl(double pl) {
		this.pl = pl;
	}

}
