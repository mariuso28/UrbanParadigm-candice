package org.cz.json.chart;

import java.util.Date;

public class RsiEntry extends ChartEntry{
	
	private double change;
	private double gain;
	private double loss;
	private double avgGain;
	private double avgLoss;
	private double rs;
	private double rsi;
	
	public RsiEntry(Date date,double close)
	{
		super(date,close);
	}

	public void calcRsi() {
		rs = avgGain - avgLoss;
		rsi = 100 - (100 / (1 + rs));
	}
	
	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
		if (change>0)
			setGain(change);
		else
			setLoss(change*-1.0);
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public double getLoss() {
		return loss;
	}

	public void setLoss(double loss) {
		this.loss = loss;
	}

	public double getAvgGain() {
		return avgGain;
	}

	public void setAvgGain(double avgGain) {
		this.avgGain = avgGain;
	}

	public double getAvgLoss() {
		return avgLoss;
	}

	public void setAvgLoss(double avgLoss) {
		this.avgLoss = avgLoss;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	@Override
	public String toString() {
		return "RsiEntry [change=" + change + ", gain=" + gain + ", loss=" + loss + ", avgGain=" + avgGain
				+ ", avgLoss=" + avgLoss + ", rs=" + rs + ", rsi=" + rsi + ", close=" + close + "]";
	}

	

}
