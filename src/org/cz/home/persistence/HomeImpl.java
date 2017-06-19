package org.cz.home.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;
import org.cz.json.security.YearHigh;
import org.cz.portfolio.persistence.PortfolioDao;
import org.cz.security.persistence.SecurityDailyDao;
import org.cz.security.persistence.SecurityDao;
import org.cz.user.BaseUser;
import org.cz.user.persistence.BaseUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.web.multipart.MultipartFile;

public class HomeImpl extends NamedParameterJdbcDaoSupport implements Home {
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(HomeImpl.class);
	
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private SecurityDailyDao securityDailyDao;
	@Autowired
	private SecurityDao securityDao;
	@Autowired
	private PortfolioDao portfolioDao;
	
	
	public HomeImpl()
	{	
	}
	
	@Override
	public BaseUser getAdmin() throws PersistenceRuntimeException
	{
		return baseUserDao.getAdmin();
	}
	
	@Override
	public void storeBaseUser(BaseUser baseUser) throws PersistenceRuntimeException
	{
		baseUserDao.storeBaseUser(baseUser);
	}
	
	@Override
	public BaseUser getBaseUserByEmail(String email) throws PersistenceRuntimeException
	{
		BaseUser bu = baseUserDao.getBaseUserByEmail(email);
		if (bu==null)
			return null;
		bu.setPortfolios(getPortfolios(bu));
		return bu;
	}
	
	public List<BaseUser> getActiveBaseUsers() throws PersistenceRuntimeException 
	{
		 List<BaseUser> bus = baseUserDao.getActiveBaseUsers();
		 for (BaseUser bu : bus)
		 {
			 bu.setPortfolios(getPortfolios(bu));
		 }
		 return bus;
	}
	
	@Override
	public void updateBaseUserProfile(BaseUser baseUser) throws PersistenceRuntimeException
	{
		baseUserDao.updateBaseUserProfile(baseUser);
	}
	
	@Override
	public void storeImage(String email,MultipartFile data, String contentType) throws PersistenceRuntimeException
	{
		baseUserDao.storeImage(email, data, contentType);
	}
	
	@Override
	public byte[] getImage(final String email) throws PersistenceRuntimeException
	{
		return baseUserDao.getImage(email);
	}
	
	@Override
	public String getEmailForId(UUID id) throws PersistenceRuntimeException
	{
		return baseUserDao.getEmailForId(id);
	}

	@Override
	public void setDefaultPasswordForAll(String encoded)
	{
		baseUserDao.setDefaultPasswordForAll(encoded);
	}
	
	@Override
	public void storePortfolio(BaseUser baseUser,Portfolio portfolio)
	{
		portfolioDao.storePortfolio(baseUser, portfolio);
	}
	
	@Override
	public void deletePortfolio(Portfolio portfolio)
	{
		portfolioDao.deletePortfolio(portfolio);
	}
	
	@Override
	public Map<String,Portfolio> getPortfolios(BaseUser baseUser)
	{
		return portfolioDao.getPortfolios(baseUser);
	}
	
	@Override
	public void storePortfolioWatch(Portfolio portfolio,PortfolioWatch watch)
	{
		portfolioDao.storePortfolioWatch(portfolio, watch);
	}
	
	@Override
	public void deletePortfolioWatch(PortfolioWatch watch)
	{
		portfolioDao.deletePortfolioWatch(watch);
	}
	
	@Override
	public void storePortfolioEntry(final PortfolioWatch watch,final PortfolioEntry entry)
	{
		portfolioDao.storePortfolioEntry(watch, entry);
	}
	
	@Override
	public void updatePortfolioEntryHs(PortfolioEntryHs phs)
	{
		portfolioDao.updatePortfolioEntryHs(phs);
	}
	
	@Override
	public void deletePortfolioEntry(PortfolioEntry entry)
	{
		portfolioDao.deletePortfolioEntry(entry);
	}
	
	@Override
	public void setUpdated(Portfolio portfolio)
	{
		portfolioDao.setUpdated(portfolio);
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
	public List<Security> searchSecuritys(String searchTerm) {
		return securityDao.searchSecuritys(searchTerm);
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
	public List<SecurityDaily> getLastSecurityDailys()
	{
		return securityDailyDao.getLastSecurityDailys();
	}
	
	@Override
	public Map<String,YearHigh> getYearHighs(Date date,Date date2)
	{
		List<YearHigh> yhs = securityDailyDao.getYearHighs(date, date2);
		Map<String,YearHigh> map = new TreeMap<String,YearHigh>();
		for (YearHigh yh : yhs)
			map.put(yh.getTicker(),yh);
		return map;
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

	public BaseUserDao getBaseUserDao() {
		return baseUserDao;
	}

	public void setBaseUserDao(BaseUserDao baseUserDao) {
		this.baseUserDao = baseUserDao;
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

	public PortfolioDao getPortfolioDao() {
		return portfolioDao;
	}

	public void setPortfolioDao(PortfolioDao portfolioDao) {
		this.portfolioDao = portfolioDao;
	}

	
}
