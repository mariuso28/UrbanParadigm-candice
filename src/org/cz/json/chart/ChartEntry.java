package org.cz.json.chart;

import java.util.Date;

public class ChartEntry {
	
	protected Date date;
	protected double close;
	
	public ChartEntry(Date date,double close)
	{
		setDate(date);
		setClose(close);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

}
