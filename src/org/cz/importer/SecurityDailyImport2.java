package org.cz.importer;

import java.util.Date;
import java.util.GregorianCalendar;

public class SecurityDailyImport2 {

	private String code;
	private String d;
	private Date date;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Double vol;
	private Double end;
	
	public SecurityDailyImport2()
	{
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDate() {
		return date;
	}

	public void setDateString(String date) {
		String[] toks = date.split("/");
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.clear();
		gc.set(Integer.parseInt("20"+toks[2]), 
				Integer.parseInt(toks[1])-1,
				Integer.parseInt(toks[0]));
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

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public Double getEnd() {
		return end;
	}

	public void setEnd(Double end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "SecurityDailyImport2 [code=" + code + ", d=" + d + ", date=" + date + ", open=" + open + ", high="
				+ high + ", low=" + low + ", close=" + close + ", vol=" + vol + ", end=" + end + "]";
	}
	
}
