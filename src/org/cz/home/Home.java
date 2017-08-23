package org.cz.home;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.json.fees.Fee;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioProfitLoss;
import org.cz.json.portfolio.PortfolioTransaction;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.mp.PortfolioEntryMp;
import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;
import org.cz.json.security.YearHigh;
import org.cz.user.BaseUser;
import org.springframework.web.multipart.MultipartFile;

public interface Home {
	
	public BaseUser getAdmin() throws PersistenceRuntimeException;
	public void storeBaseUser(BaseUser baseUser) throws PersistenceRuntimeException;
	public BaseUser getBaseUserByEmail(String email) throws PersistenceRuntimeException;
	public void updateBaseUserProfile(BaseUser baseUser) throws PersistenceRuntimeException;
	public void storeImage(String email,MultipartFile data, String contentType) throws PersistenceRuntimeException;
	public byte[] getImage(final String email) throws PersistenceRuntimeException;
	public String getEmailForId(UUID id) throws PersistenceRuntimeException;
	public List<BaseUser> getActiveBaseUsers() throws PersistenceRuntimeException;
	public List<BaseUser> getAllBaseUsers() throws PersistenceRuntimeException;
	public void setDefaultPasswordForAll(String encoded);
	
	public void storePortfolio(BaseUser baseUser,Portfolio portfolio);
	public void deletePortfolio(Portfolio portfolio);
	public Map<String,Portfolio> getPortfolios(BaseUser baseUser);
	public void storePortfolioWatch(Portfolio portfolio,PortfolioWatch watch);
	public void deletePortfolioWatch(PortfolioWatch watch);
	public void storePortfolioEntry(final PortfolioWatch watch,final PortfolioEntry entry);
	public void updatePortfolioEntryHs(PortfolioEntryHs phs);
	public void updatePortfolioEntryMp(PortfolioEntryMp pmp);
	public void deletePortfolioEntry(PortfolioEntry entry);
	public void setUpdated(Portfolio portfolio);
	public void storePortfolioTransaction(final PortfolioTransaction trans);
	public List<PortfolioTransaction> getPortfolioTransactions(final BaseUser bu,final Date startDate,final Date endDate,final String portfolioName);
	public List<PortfolioProfitLoss> getPortfolioProfitLoss(BaseUser bu, Date startDate, Date endDate, String portfolioName);		
	
	public Security getSecurity(final String ticker);
	public Security getSecurityByCode(final String code);
	public List<Security> getSecuritys();
	public List<Security> searchSecuritys(String searchTerm);
	public void storeSecurity(final Security security);
	
	public SecurityDaily getSecurityDaily(final String ticker,final Date date);
	public List<SecurityDaily> getSecurityDailys(final Date date);
	public List<SecurityDaily> getLastSecurityDailys();
	public List<SecurityDaily> getSecurityDailyForRange(final String ticker,final Date start,final Date end);
	public void storeSecurityDaily(final SecurityDaily securityDaily);
	public Map<String,YearHigh> getYearHighs(Date date,Date date2);
	public Map<String, Fee> getFees();	
		
}
