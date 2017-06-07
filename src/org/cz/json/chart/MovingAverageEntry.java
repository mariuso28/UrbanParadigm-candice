package org.cz.json.chart;

import java.util.Date;

public class MovingAverageEntry {
	private double macd12;
	private double macd26;
	private double macdLine;
	private double signalLine;
	private double macdhistogram;
	private Date date;
	private double close;
	
	public MovingAverageEntry(Date date,double close)
	{
		setDate(date);
		setClose(close);
	}

	public void calcMacdLine()
	{
		macdLine = macd12 - macd26;
	}
	
	public void calcMacdHistogram() {
		macdhistogram = macdLine - signalLine;
	}
	
	public double getMacd12() {
		return macd12;
	}

	public void setMacd12(double macd12) {
		this.macd12 = macd12;
	}

	public double getMacd26() {
		return macd26;
	}

	public void setMacd26(double macd26) {
		this.macd26 = macd26;
	}

	public double getMacdLine() {
		return macdLine;
	}

	public void setMacdLine(double macdLine) {
		this.macdLine = macdLine;
	}

	public double getSignalLine() {
		return signalLine;
	}

	public void setSignalLine(double signalLine) {
		this.signalLine = signalLine;
	}

	public double getMacdhistogram() {
		return macdhistogram;
	}

	public void setMacdhistogram(double macdhistogram) {
		this.macdhistogram = macdhistogram;
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

	@Override
	public String toString() {
		return "MovingAverageEntry [macd12=" + macd12 + ", macd26=" + macd26 + ", macdLine=" + macdLine
				+ ", signalLine=" + signalLine + ", macdhistogram=" + macdhistogram + ", date=" + date + ", close="
				+ close + "]";
	}
	
}
