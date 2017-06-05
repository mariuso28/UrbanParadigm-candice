package org.cz.json.portfolio;

public class PortfolioEntry{

	private long id;
	private PortfolioEntryType type;
	private String securityTicker;
	
	public PortfolioEntry()
	{
	}
	
	public PortfolioEntry(PortfolioEntryType type,String securityTicker)
	{
		setType(type);
		setSecurityTicker(securityTicker);
	}
	
	public String getSecurityTicker() {
		return securityTicker;
	}

	public void setSecurityTicker(String securityTicker) {
		this.securityTicker = securityTicker;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PortfolioEntryType getType() {
		return type;
	}

	public void setType(PortfolioEntryType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PortfolioEntry [id=" + id + ", type=" + type + ", securityTicker=" + securityTicker + "]";
	}
	
}
