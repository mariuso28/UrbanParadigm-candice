package org.cz.services;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.springframework.transaction.PlatformTransactionManager;

public class Service {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Service.class);	
	private Home home;
	private PlatformTransactionManager transactionManager;
	
	public Service()
	{
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
	
	
}
