package org.cz.json.chart;

import java.util.Date;

public class MovingAverageEntry extends ChartEntry{
	
	private double sma;
	private double ema;

	
	public MovingAverageEntry(Date date,double close)
	{
		super(date,close);
	}

	public double getSma() {
		return sma;
	}


	public void setSma(double sma) {
		this.sma = sma;
	}


	public double getEma() {
		return ema;
	}


	public void setEma(double ema) {
		this.ema = ema;
	}


	@Override
	public String toString() {
		return "MovingAverageEntry [sma=" + sma + ", ema=" + ema + ", date=" + date + ", close=" + close + "]";
	}

	
}
