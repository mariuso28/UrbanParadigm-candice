package org.cz.json.chart;

import java.util.Date;

public class StochasticOscillatorEntry extends ChartEntry{
	
	private double kLine;
	private double dLine;
	
	public StochasticOscillatorEntry(Date date,double close)
	{
		super(date,close);
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

	@Override
	public String toString() {
		return "StochasticOscillatorEntry [kLine=" + kLine + ", dLine=" + dLine + ", date=" + date + ", close=" + close
				+ "]";
	}

	
}
