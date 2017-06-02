package org.cz.portfolio;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.security.Security;
import org.cz.portfolio.hs.PortfolioEntryHs;
import org.cz.portfolio.hs.PortfolioHsStatus;

public class PortfolioEntry{

	private long id;
	private String securityTicker;
	private Security security;
	private List<PortfolioEntryHs> hsList = new ArrayList<PortfolioEntryHs>();;
	
	public PortfolioEntry()
	{
	}

	public PortfolioEntryHs getPortfolioEntryHsWithStatus(PortfolioHsStatus status) throws PortfolioException
	{
		PortfolioEntryHs target = null;
		for (PortfolioEntryHs pehs : hsList)
		{
			if (pehs.getStatus().equals(status))
			{
				target = pehs;
				break;
			}
		}
		if (target == null)
			throw new PortfolioException("No Hockey Stick in STATUS : " + status);
		return target;
	}
	
	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public List<PortfolioEntryHs> getHsList() {
		return hsList;
	}

	public void setHsList(List<PortfolioEntryHs> hsList) {
		this.hsList = hsList;
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

	@Override
	public String toString() {
		return "PortfolioEntry [id=" + id + ", securityTicker=" + securityTicker + ", security=" + security
				+ ", hsList=" + hsList + "]";
	}
	
}
