package org.cz.home;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;
import org.cz.portfolio.Portfolio;
import org.cz.portfolio.PortfolioEntry;
import org.cz.portfolio.hs.PortfolioEntryHs;
import org.cz.user.BaseUser;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

public interface Home {
	
	public void storeBaseUser(BaseUser baseUser) throws PersistenceRuntimeException;
	public BaseUser getBaseUserByEmail(String email) throws PersistenceRuntimeException;
	public void updateBaseUserProfile(BaseUser baseUser) throws PersistenceRuntimeException;
	public void storeImage(String email,MultipartFile data, String contentType) throws PersistenceRuntimeException;
	public byte[] getImage(final String email) throws PersistenceRuntimeException;
	public String getEmailForId(UUID id) throws PersistenceRuntimeException;
	public void setDefaultPasswordForAll(String encoded);
	
	public void storePortfolio(BaseUser baseUser,Portfolio portfolio) throws PersistenceRuntimeException;
	public void deletePortfolio(Portfolio portfolio) throws PersistenceRuntimeException;
	public List<Portfolio> getPortfolios(BaseUser baseUser) throws PersistenceRuntimeException;
	public void storePortfolioEntry(final Portfolio portfolio,PortfolioEntry entry);
	public void deletePortfolioEntry(PortfolioEntry entry);
	public void storePortfolioEntryHs(final PortfolioEntryHs phs,final PortfolioEntry entry) throws DataAccessException;
	public void updatePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException;
	public void deletePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException;
	
	public Security getSecurity(final String ticker);
	public Security getSecurityByCode(final String code);
	public List<Security> getSecuritys();
	public List<Security> searchSecuritys(String searchTerm);
	public void storeSecurity(final Security security);
	
	public SecurityDaily getSecurityDaily(final String ticker,final Date date);
	public List<SecurityDaily> getSecurityDailys(final Date date);
	public List<SecurityDaily> getSecurityDailyForRange(final String ticker,final Date start,final Date end);
	public void storeSecurityDaily(final SecurityDaily securityDaily);
		
}
