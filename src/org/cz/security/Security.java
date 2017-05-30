package org.cz.security;

public class Security {

	private long id;
	private String name;
	private String ticker;
	private String code;
	
	public Security()
	{
	}
	
	public Security(String name, String ticker,String code) {
		setName(name);
		setTicker(ticker);
		setCode(code);
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Security [id=" + id + ", name=" + name + ", ticker=" + ticker + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
