package org.cz.security;

import java.util.Date;

public class SecurityDaily {

//	<Name>,<TICKER>,<PER>,<DATE>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>
	
	private String ticker;
	private Date date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Double volume;
	
	public SecurityDaily()
	{
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@Override
	public String toString() {
		return "SecurityDaily [ticker=" + ticker + ", date=" + date + ", open=" + open + ", high=" + high + ", low="
				+ low + ", close=" + close + ", volume=" + volume + "]";
	}
	
}
