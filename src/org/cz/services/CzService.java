package org.cz.services;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.springframework.transaction.PlatformTransactionManager;

public class CzService {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CzService.class);	
	private Home home;
	private PlatformTransactionManager transactionManager;
	private PortfolioMgr portfolioMgr;
	
	public CzService()
	{
	}
	
	public void initServices()
	{
		portfolioMgr = new PortfolioMgr(home);
	}
	
	public Home getHome() {
		return home;
	}
	
	public void setHome(Home home) {
		this.home = home;
	}
	
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PortfolioMgr getPortfolioMgr() {
		return portfolioMgr;
	}

	public void setPortfolioMgr(PortfolioMgr portfolioMgr) {
		this.portfolioMgr = portfolioMgr;
	}
	
	
}
