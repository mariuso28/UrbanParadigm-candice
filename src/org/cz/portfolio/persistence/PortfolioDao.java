package org.cz.portfolio.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cz.json.fees.Fee;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioProfitLoss;
import org.cz.json.portfolio.PortfolioTransaction;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.mp.PortfolioEntryMp;
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
	public void setUpdated(Portfolio portfolio);
	public void storePortfolioTransaction(final PortfolioTransaction trans);
	public List<PortfolioTransaction> getPortfolioTransactions(final BaseUser bu,final Date startDate,final Date endDate,final String portfolioName);
	public List<PortfolioProfitLoss> getPortfolioProfitLoss(BaseUser bu, Date startDate, Date endDate, String portfolioName);
	public void updatePortfolioEntryMp(PortfolioEntryMp pmp);
	public Map<String, Fee> getFees();
}
