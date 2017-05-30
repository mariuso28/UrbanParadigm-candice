package org.cz.home.persistence;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.security.Security;
import org.cz.security.SecurityDaily;
import org.cz.security.persistence.SecurityDailyDao;
import org.cz.security.persistence.SecurityDao;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class HomeImpl extends NamedParameterJdbcDaoSupport implements Home {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(HomeImpl.class);
	private SecurityDailyDao securityDailyDao;
	private SecurityDao securityDao;
	
	public HomeImpl()
	{	
	}
	
	@Override
	public Security getSecurity(final String ticker)
	{
		return securityDao.getSecurity(ticker);
	}
	
	@Override
	public Security getSecurityByCode(String code) {
		return securityDao.getSecurityByCode(code);
	}
	
	
	@Override
	public List<Security> getSecuritys()
	{
		return securityDao.getSecuritys();
	}
	
	@Override
	public void storeSecurity(final Security security)
	{
		securityDao.storeSecurity(security);
	}
	
	@Override
	public SecurityDaily getSecurityDaily(final String ticker,final Date date) {
		SecurityDaily sd = securityDailyDao.getSecurityDaily(ticker, date);
		return sd;
	}
	
	@Override
	public List<SecurityDaily> getSecurityDailys(final Date date) {
		
		return securityDailyDao.getSecurityDailys(date);
	}

	@Override
	public List<SecurityDaily> getSecurityDailyForRange(final String ticker,final Date start,final Date end)
	{
		return securityDailyDao.getSecurityDailyForRange(ticker,start,end);
	}
	
	@Override
	public void storeSecurityDaily(final SecurityDaily securityDaily){
		
		securityDailyDao.storeSecurityDaily(securityDaily);
	}

	public SecurityDailyDao getSecurityDailyDao() {
		return securityDailyDao;
	}

	public void setSecurityDailyDao(SecurityDailyDao securityDailyDao) {
		this.securityDailyDao = securityDailyDao;
	}

	public SecurityDao getSecurityDao() {
		return securityDao;
	}

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	
}
