package org.cz.portfolio.persistence;

import java.util.Map;

import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.user.BaseUser;

public interface PortfolioDao {

	public void storePortfolio(BaseUser baseUser,Portfolio portfolio);
	public void deletePortfolio(Portfolio portfolio);
	public Map<String,Portfolio> getPortfolios(BaseUser baseUser);
	public void storePortfolioWatch(Portfolio portfolio,PortfolioWatch watch);
	public void deletePortfolioWatch(PortfolioWatch watch);
	public void storePortfolioEntry(final PortfolioWatch watch,final PortfolioEntry entry);
	public void updatePortfolioEntryHs(PortfolioEntryHs phs);
	public void deletePortfolioEntry(PortfolioEntry entry);
}
