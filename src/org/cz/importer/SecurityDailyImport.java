package org.cz.importer;

import java.util.Date;
import java.util.GregorianCalendar;

public class SecurityDailyImport {

//	<Name>,<TICKER>,<PER>,<DATE>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>
	
	private String name;
	private String ticker;
	private Date date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Double vol;
	
	public SecurityDailyImport()
	{
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}



	public Date getDate() {
		return date;
	}

	public void setDateString(String date) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(Integer.parseInt(date.substring(0,4)), 
				Integer.parseInt(date.substring(4,6))-1,
				Integer.parseInt(date.substring(6,8)));
		setDate(gc.getTime());
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

	public Double getVol() {
		return vol;
	}

	public void setVol(Double vol) {
		this.vol = vol;
	}
}
