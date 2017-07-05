package org.cz.json.chart;

import java.util.Date;

public class StochasticOscillatorEntry extends ChartEntry{
	
	private double kLine;
	private double dLine;
	private double high;
	private double low;
	private double highestHigh;
	private double lowestLow;
	
	public StochasticOscillatorEntry(Date date,double close,double high,double low)
	{
		super(date,close);
		setHigh(high);
		setLow(low);
	}
	
	public double getkLine() {
		return kLine;
	}

	public void setkLine(double kLine) {
		this.kLine = kLine;
	}

	public double getdLine() {
		return dLine;
	}

	public void setdLine(double dLine) {
		this.dLine = dLine;
	}

	
	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHighestHigh() {
		return highestHigh;
	}

	public void setHighestHigh(double highestHigh) {
		this.highestHigh = highestHigh;
	}

	public double getLowestLow() {
		return lowestLow;
	}

	public void setLowestLow(double lowestLow) {
		this.lowestLow = lowestLow;
	}

	@Override
	public String toString() {
		return "StochasticOscillatorEntry [kLine=" + kLine + ", dLine=" + dLine + ", date=" + date + ", close=" + close
				+ "]";
	}

	
}
