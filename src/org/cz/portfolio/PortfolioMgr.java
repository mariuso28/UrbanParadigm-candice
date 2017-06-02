package org.cz.portfolio;

import org.cz.home.Home;
import org.cz.json.security.Security;
import org.cz.portfolio.hs.PortfolioEntryHs;
import org.cz.portfolio.hs.PortfolioHsStatus;

public class PortfolioMgr {

	private Home home;
	
	public PortfolioMgr(Home home)
	{
		
	}
	
	public void createEntryForSecurity(Portfolio portfolio,Security security)
	{
		PortfolioEntry pe = new PortfolioEntry();
		pe.setSecurityTicker(security.getTicker());
		pe.setSecurity(security);
		portfolio.getEntries().put(pe.getSecurityTicker(),pe);
		home.storePortfolioEntry(portfolio, pe);
	}
	
	public void createHsForSecurity(Portfolio portfolio,String ticker)
	{
		PortfolioEntry pe = portfolio.getEntries().get(ticker);
		PortfolioEntryHs pehs = new PortfolioEntryHs();
		pe.getHsList().add(pehs);
		home.storePortfolioEntryHs(pehs, pe);
	}
	
	public void changeHsStatusForSecurity(Portfolio portfolio,String ticker,PortfolioHsStatus newStatus,double price) throws PortfolioException
	{
		// SEEK,HITYEARHIGH,BUY,HOLD,SELL;
		PortfolioEntry pe = portfolio.getEntries().get(ticker);
		if (newStatus.equals(PortfolioHsStatus.HITYEARHIGH))
		{
			PortfolioEntryHs target = pe.getPortfolioEntryHsWithStatus(PortfolioHsStatus.SEEK);
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.BUY))
		{
			PortfolioEntryHs target = pe.getPortfolioEntryHsWithStatus(PortfolioHsStatus.HITYEARHIGH);
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.HOLD))
		{
			PortfolioEntryHs target = pe.getPortfolioEntryHsWithStatus(PortfolioHsStatus.BUY);
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.SELL))
		{
			PortfolioEntryHs target = pe.getPortfolioEntryHsWithStatus(PortfolioHsStatus.HOLD);
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		home.storePortfolioEntry(portfolio, pe);
	}

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}
}
