package org.cz.json.portfolio;

import java.util.Date;

public class PortfolioTransaction {

	private long id;
	private String traderEmail;
	private String portfolioName;
	private Date timestamp;
	private String ticker;
	private double price;
	private double quantity;
	private PortfolioActionType action;
	
	public PortfolioTransaction()
	{
	}
	
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public PortfolioActionType getAction() {
		return action;
	}

	public void setAction(PortfolioActionType action) {
		this.action = action;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
