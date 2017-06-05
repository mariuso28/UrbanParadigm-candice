package org.cz.services;

import org.cz.home.Home;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryType;
import org.cz.json.portfolio.PortfolioException;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.hs.PortfolioHsStatus;
import org.cz.user.BaseUser;

public class PortfolioMgr {

	private Home home;
	
	public PortfolioMgr(Home home)
	{
		setHome(home);
	}
	
	public void createPortfolio(BaseUser baseUser,String name,String description) throws PortfolioException
	{
		if (baseUser.getPortfolios().get(name)!=null)
			throw new PortfolioException("Portfolio : " + name + " already exists");
		Portfolio portfolio = new Portfolio();
		portfolio.setName(name);
		portfolio.setDescription(description);
		home.storePortfolio(baseUser, portfolio);
		baseUser.getPortfolios().put(portfolio.getName(),portfolio);
	}
	
	public void deletPortfolio(BaseUser baseUser,Portfolio portfolio)
	{
		baseUser.getPortfolios().remove(portfolio.getName());
		home.deletePortfolio(portfolio);
	}
	
	public void createWatch(Portfolio portfolio,String ticker)
	{
		PortfolioWatch pw = new PortfolioWatch(ticker);
		portfolio.getWatchList().put(ticker,pw);
		home.storePortfolioWatch(portfolio, pw);
	}
	
	public void deleteWatch(Portfolio portfolio,String ticker) throws PortfolioException
	{
		PortfolioWatch pw = portfolio.getWatchList().get(ticker);
		if (pw == null)
			throw new PortfolioException("Watch : " + ticker + " in Portfolio " + portfolio.getName() + " doesn't exist");
		portfolio.getWatchList().remove(ticker);
		home.deletePortfolioWatch(pw);
	}
	
	public void createPortfolioEntry(Portfolio portfolio,String ticker,PortfolioEntryType type) throws PortfolioException
	{
		PortfolioWatch watch = portfolio.getWatchList().get(ticker);
		if (watch == null)
			throw new PortfolioException("Watch : " + ticker + " in Portfolio " + portfolio.getName() + " doesn't exist");
		
		PortfolioEntry pe = watch.createPortfolioEntry(type);
		home.storePortfolioEntry(watch,pe);
	}
	
	public void deletePortfolioEntry(Portfolio portfolio, String ticker, long entryId) throws PortfolioException
	{
		PortfolioWatch watch = portfolio.getWatchList().get(ticker);
		if (watch == null)
			throw new PortfolioException("Watch : " + ticker + " in Portfolio " + portfolio.getName() + " doesn't exist");
		
		PortfolioEntry pe = watch.getEntryById(entryId);
		if (pe == null)
			throw new PortfolioException("Entry" + entryId + " in Watch/Portfolio " + ticker + "/" + portfolio.getName() + " doesn't exist");
		
		watch.getEntries().remove(pe);
		home.deletePortfolioEntry(pe);
	}
	
	public void changeHsStatusForSecurity(Portfolio portfolio,PortfolioEntryHs target,PortfolioHsStatus newStatus,double price) throws PortfolioException
	{
		// SEEK,HITYEARHIGH,BUY,HOLD,SELL;
		if (newStatus.equals(PortfolioHsStatus.HITYEARHIGH))
		{
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.BUY))
		{
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.HOLD))
		{
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		else
		if (newStatus.equals(PortfolioHsStatus.SELL))
		{
			target.setCeiling(price * 0.95);
			target.setDayCount(0);
			target.setStatus(newStatus);
		}
		home.updatePortfolioEntryHs(target);
	}

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	
}
