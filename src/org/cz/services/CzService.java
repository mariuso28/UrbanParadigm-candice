package org.cz.services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.importer.SecurityDailyDownload;
import org.cz.json.portfolio.PortfolioException;
import org.cz.json.security.SecurityDaily;
import org.cz.user.BaseUser;
import org.cz.user.Role;
import org.cz.validate.RegistrationValidator;
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
		simulateScheduleUpdateDailySecurities();
		// scheduleUpdateDailySecurities();
	}
	
	private void simulateScheduleUpdateDailySecurities()
	{
		new Runnable() {
			@Override
			public void run() {
				try {
					GregorianCalendar gc = new GregorianCalendar();
					gc.set(2003, 10, 1);
					for (int day=0; day<20; day++)
					{
						portfolioMgr.updatePortfolios();
						gc.add(Calendar.DAY_OF_YEAR, 1);
					}
				} catch (PortfolioException e) {
					e.printStackTrace();
					log.error("Portfolios could not be updated");
				}
			}
			};
	}
	
	@SuppressWarnings("unused")
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
		
		try {
			portfolioMgr.updatePortfolios();
		} catch (PortfolioException e) {
			e.printStackTrace();
			log.error("Portfolios could not be updated");
		}
	}
	
	
	public void register(String email, String password, String contact, String phone, String deviceId) throws CzServicesException{
		
		String msg = RegistrationValidator.validateFields(email, password, contact, phone);
		if (!msg.isEmpty())
			throw new CzServicesException("Missing or invalid fields: " + msg);
		
		if (deviceId==null || deviceId.isEmpty())
			throw new CzServicesException("Missing deviceId");
		
		BaseUser member = getHome().getBaseUserByEmail(email);
		
		if (member!=null)
		{
			throw new CzServicesException("Cannot register : " + email + " aleady exists on system - please try alternative email.");
		}

		BaseUser user = new BaseUser();
		
	//	PasswordEncoder encoder = new BCryptPasswordEncoder();
	//	user.setPassword(encoder.encode(password));
		user.setPassword(password);
		user.setContact(contact);
		user.setPhone(phone);
		user.setDeviceId(deviceId);
		user.setEmail(email);
		user.setRole(Role.ROLE_TRADER);
		user.setEnabled(false);
		
		getHome().storeBaseUser(user);
	}

	public void updateEnabled(String email,boolean enabled) throws CzServicesException
	{
		BaseUser user = getHome().getBaseUserByEmail(email);
		if (user==null)
		{
			throw new CzServicesException("Cannot enable/disable : " + email + " user does not exist on system.");
		}
		user.setEnabled(enabled);
		home.updateBaseUserProfile(user);
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
