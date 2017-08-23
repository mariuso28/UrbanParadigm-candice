package org.cz.json.portfolio;

import java.util.Date;
import java.util.Map;

import org.cz.json.fees.Fee;

public class PortfolioTransaction {

	private long id;
	private String traderEmail;
	private String portfolioName;
	private Date timestamp;
	private String ticker;
	private double price;
	private double quantity;
	private PortfolioActionType action;
	private double brokerage;
	private double clearing;
	private double stamp;
	private double gst;
	private double net;
	
	public PortfolioTransaction()
	{
	}
	
	public void calcFees(Map<String,Fee> fees)
	{
		brokerage = fees.get("brokerage").calc(price*quantity);
		clearing = fees.get("clearing").calc(price*quantity);
		stamp = fees.get("stamp").calc(price*quantity);
		gst = fees.get("gst").calc(price*quantity);
		if (action.equals(PortfolioActionType.Buy))
			net = (price*quantity) + brokerage + clearing + stamp + gst;
		else
			net = (price*quantity) - (brokerage + clearing + stamp + gst);
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

	public double getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(double brokerage) {
		this.brokerage = brokerage;
	}

	public double getClearing() {
		return clearing;
	}

	public void setClearing(double clearing) {
		this.clearing = clearing;
	}

	public double getStamp() {
		return stamp;
	}

	public void setStamp(double stamp) {
		this.stamp = stamp;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getNet() {
		return net;
	}

	public void setNet(double net) {
		this.net = net;
	}

	
}
