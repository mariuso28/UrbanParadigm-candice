package org.cz.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.importer.SecurityDailyDownload;
import org.cz.json.security.SecurityDaily;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

public class CzService {
	private static final Logger log = Logger.getLogger(CzService.class);	
	private Home home;
	private PlatformTransactionManager transactionManager;
	private PortfolioMgr portfolioMgr;
	private ThreadPoolTaskScheduler scheduler;
	
	public CzService()
	{
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("candice async scheduler- ");
		scheduler.initialize();
	}
	
	public void initServices()
	{
		portfolioMgr = new PortfolioMgr(home);
//		scheduleUpdateDailySecurities();
	}
	
	private void scheduleUpdateDailySecurities() 
	{	
		dailyRefresh();
		
		log.info("SCHEDULING UPDATE DAILY SECURITIES AT 19:00");
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		gc.set(Calendar.HOUR_OF_DAY, 19);
		
		if (gc.before(now))
		{
			gc.add(Calendar.DAY_OF_YEAR,1);
		}
		Date startAt = gc.getTime();
		long interval = 24 * 60 * 60 * 1000L;
		log.info("REFRESHING SECURITIES DAILY SCHEDULED FOR : " + startAt + " INTERVAL : " + interval);
		
		scheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						
						log.info("Running scheduled event");
						try {
							dailyRefresh();
						} catch (Exception e) {
							e.printStackTrace();
							log.error("Exception in scheduled dailyRefresh " + e.getMessage());
						}
					}
				}, startAt, interval);
	}

	public void dailyRefresh()
	{
		GregorianCalendar gc = new GregorianCalendar();
		log.info("REFRESHING SECURITIES DAILY AT : " + gc.getTime());
		String folder = "/home/pmk/candice/dailyupdates/" + gc.get(Calendar.YEAR);
		
		if (gc.get(Calendar.HOUR_OF_DAY) < 19)
			gc.add(Calendar.DAY_OF_YEAR, -1);
		Date to = gc.getTime();
		
		List<SecurityDaily> secs = home.getLastSecurityDailys();
		gc.setTime(secs.get(0).getDate());
		gc.add(Calendar.DAY_OF_YEAR, 1);
		Date from = gc.getTime();
		if (!from.before(to))
		{
			log.info("Securities up-to-date");
			return;
		}
		
		SecurityDailyDownload.download(from, to, folder);
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
