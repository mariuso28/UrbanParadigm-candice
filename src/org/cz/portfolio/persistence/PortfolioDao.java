package org.cz.portfolio.persistence;

import java.util.List;

import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.portfolio.Portfolio;
import org.cz.portfolio.PortfolioEntry;
import org.cz.portfolio.hs.PortfolioEntryHs;
import org.cz.user.BaseUser;
import org.springframework.dao.DataAccessException;

public interface PortfolioDao {

	public void storePortfolio(BaseUser baseUser,Portfolio portfolio) throws PersistenceRuntimeException;
	public void deletePortfolio(Portfolio portfolio) throws PersistenceRuntimeException;
	public List<Portfolio> getPortfolios(BaseUser baseUser) throws PersistenceRuntimeException;
	public void storePortfolioEntry(final Portfolio portfolio,PortfolioEntry entry);
	public void storePortfolioEntryHs(final PortfolioEntryHs phs,final PortfolioEntry entry) throws DataAccessException;
	public void updatePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException;
	public void deletePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException;
	public void deletePortfolioEntry(PortfolioEntry entry);
}
