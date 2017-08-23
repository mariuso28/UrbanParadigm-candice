package org.cz.json.portfolio;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.json.fees.Fee;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.hs.PortfolioHsStatus;
import org.cz.json.portfolio.mp.PortfolioEntryMp;
import org.cz.json.portfolio.mp.PortfolioMpStatus;
import org.cz.json.security.SecurityDaily;
import org.cz.json.security.YearHigh;
import org.cz.user.BaseUser;
import org.cz.util.DateUtil;

public class PortfolioMgr {

	private static final Logger log = Logger.getLogger(PortfolioMgr.class);
	private Map<String,YearHigh> yearHighs;
	private Date today;
	private Home home;
	private Map<String,Fee> fees;
	
	public PortfolioMgr(Home home)
	{
		setHome(home);
		setFees(home.getFees());
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
	
	public void createPortfolioEntryPrice(Portfolio portfolio, String ticker, PortfolioEntryType type, double price) throws PortfolioException {
		PortfolioWatch watch = portfolio.getWatchList().get(ticker);
		if (watch == null)
			throw new PortfolioException("Watch : " + ticker + " in Portfolio " + portfolio.getName() + " doesn't exist");
		
		PortfolioEntry pe = watch.createPortfolioEntry(type,price);
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

	public void updatePortfolios() throws PortfolioException
	{
		GregorianCalendar gc = new GregorianCalendar();
		today = DateUtil.getNowWithZeroedTime();
		gc.setTime(today);
		gc.add(Calendar.DAY_OF_YEAR, -1);
		yearHighs = home.getYearHighs(today, gc.getTime());
		
		List<BaseUser> bus = home.getActiveBaseUsers();
		for (BaseUser bu : bus)
			updatePortfolios(bu);
	}
	
	private void updatePortfolios(BaseUser bu) throws PortfolioException {
		for (Portfolio port : bu.getPortfolios().values())
		{
			updatePortfolio(port);
		}
	}

	private void updatePortfolio(Portfolio port) throws PortfolioException {
		
		port.getActions().clear();
		for (PortfolioWatch pw : port.getWatchList().values())
		{
			for (PortfolioEntryI pe : pw.getEntries())
			{
				PortfolioActionType action = updatePortfolioEntry(pe,port);
				if (!action.equals(PortfolioActionType.None))
				{
					PortfolioAction pa = new PortfolioAction(pw.getTicker(),action);
					port.getActions().put(pa.getTicker(), pa);
				}
			}
		}
			
		home.setUpdated(port);
	}

	private PortfolioActionType updatePortfolioEntry(PortfolioEntryI pei,Portfolio portfolio) throws PortfolioException {
		
		PortfolioEntry pe = (PortfolioEntry) pei;
		if (pe.getType().equals(PortfolioEntryType.HockeyStick))
		{
			return updatePortfolioEntryHs((PortfolioEntryHs) pe,portfolio);
		}
		else
		if (pe.getType().equals(PortfolioEntryType.MarketPrice))
		{
			return updatePortfolioEntryMp((PortfolioEntryMp) pe,portfolio);
		}
		else
		{
			log.error("Invalid portfolio entry type - ignored : " + pe.getType());
			throw new PortfolioException("Invalid portfolio entry type - ignored : " + pe.getType());
		}
	}

	private PortfolioActionType updatePortfolioEntryMp(PortfolioEntryMp pe, Portfolio portfolio) {
		PortfolioActionType action = PortfolioActionType.None;
		SecurityDaily sd = home.getSecurityDaily(pe.getSecurityTicker(), today);
		if (pe.getStatus().equals(PortfolioMpStatus.SEEK) || pe.getStatus().equals(PortfolioMpStatus.DROPPED))
		{
			if (sd.getClose()>=pe.getTarget())
			{
				action = PortfolioActionType.Sell;
				pe.setStatus(PortfolioMpStatus.MET);
				home.updatePortfolioEntryMp(pe);
			}	
		}
		else
		if (pe.getStatus().equals(PortfolioMpStatus.MET))
		{
			if (sd.getClose()<pe.getTarget())
			{
				action = PortfolioActionType.Buy;
				pe.setStatus(PortfolioMpStatus.DROPPED);
				home.updatePortfolioEntryMp(pe);
			}	
		}
		else
		if (pe.getStatus().equals(PortfolioMpStatus.DROPPED))
		{
			pe.setStatus(PortfolioMpStatus.SEEK);
			home.updatePortfolioEntryMp(pe);
		}
		return action;
	}

	private PortfolioActionType updatePortfolioEntryHs(PortfolioEntryHs peh,Portfolio portfolio) throws PortfolioException
	{
		PortfolioActionType action = PortfolioActionType.None;
		if (peh.getStatus().equals(PortfolioHsStatus.SEEK))
		{
			YearHigh yh = yearHighs.get(peh.getSecurityTicker()); 
			if (yh!=null)
			{
				changeHsStatusForSecurity(portfolio,peh,PortfolioHsStatus.HITYEARHIGH,yh.getHigh());
			}
		}
		else
		if (peh.getStatus().equals(PortfolioHsStatus.HITYEARHIGH))
		{	
			peh.setDayCount(peh.getDayCount()+1);
			SecurityDaily sd = home.getSecurityDaily(peh.getSecurityTicker(), today);
			if ((peh.getCeiling()*0.95) < sd.getHigh())
				changeHsStatusForSecurity(portfolio,peh,PortfolioHsStatus.SEEK,0.0);
			else
			{
				if (peh.getDayCount() > 3)
					changeHsStatusForSecurity(portfolio,peh,PortfolioHsStatus.BUY,sd.getClose());
				action = PortfolioActionType.Buy;
			}
		}
		else
		if (peh.getStatus().equals(PortfolioHsStatus.HOLD))
		{
			SecurityDaily sd = home.getSecurityDaily(peh.getSecurityTicker(), today);
			if ((peh.getCeiling()*0.95) < sd.getHigh())
			{
				changeHsStatusForSecurity(portfolio,peh,PortfolioHsStatus.SELL,sd.getClose());
				action = PortfolioActionType.Buy;
			}
		}
		return action;
	}
	
	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public Map<String,Fee> getFees() {
		return fees;
	}

	public void setFees(Map<String,Fee> fees) {
		this.fees = fees;
	}

	
}
