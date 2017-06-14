package org.cz.json.chart;

import java.util.Date;

public class MovingAverageMacdEntry extends ChartEntry{
	private double macd12;
	private double macd26;
	private double macd9;
	private double macdLine;
	private double signalLine;
	private double macdhistogram;
	
	public MovingAverageMacdEntry(Date date,double close)
	{
		super(date,close);
	}

	public void calcMacdLine()
	{
		macdLine = macd12 - macd26;
	}
	
	public void calcMacdHistogram() {
		macdhistogram = macdLine - signalLine;
	}
	
	public double getMacd9() {
		return macd9;
	}

	public void setMacd9(double macd9) {
		this.macd9 = macd9;
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

	@Override
	public String toString() {
		return "MovingAverageMacdEntry [macd12=" + macd12 + ", macd26=" + macd26 + ", macd9=" + macd9 + ", macdLine="
				+ macdLine + ", signalLine=" + signalLine + ", macdhistogram=" + macdhistogram + ", date=" + date
				+ ", close=" + close + "]";
	}


	
}
