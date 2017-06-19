package org.cz.rest.trader;

import java.util.Date;

import org.cz.json.message.CzResultJson;
import org.cz.json.portfolio.PortfolioEntryType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/trader")
public interface RestTraderController {

	@RequestMapping(value = "/getProfile")
	// CzResultJson contains CzBaseUserProfileJson if success, message if fail
	public CzResultJson getProfile(OAuth2Authentication auth);

	@RequestMapping(value = "/getPortfolios")
	// CzResultJson contains TreeMap<String,Portfolio> if success, message if fail
	public CzResultJson getPortfolios(OAuth2Authentication auth);
	
	@RequestMapping(value = "/getPortfolio")
	// CzResultJson contains Portfolio if success, message if fail
	public CzResultJson getPortfolios(OAuth2Authentication auth,@RequestParam("name") String name);
	
	@RequestMapping(value = "/createPortfolio")
	// CzResultJson contains TreeMap<String,Portfolio> if success, message if fail
	public CzResultJson createPortfolio(OAuth2Authentication auth,@RequestParam("name") String name,@RequestParam("description") String description);
	
	@RequestMapping(value = "/addToWatchList")
	// CzResultJson refreshed portfolio nothing if success, message if fail
	public CzResultJson addToWatchList(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,@RequestParam("ticker") String ticker);
	
	@RequestMapping(value = "/deleteFromWatchList")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson deleteFromWatchList(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,@RequestParam("ticker") String ticker);
	
	@RequestMapping(value = "/createPortfolioEntry")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson createPortfolioEntry(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,
					@RequestParam("ticker") String ticker,@RequestParam("entryType") PortfolioEntryType type);
	
	@RequestMapping(value = "/deletePortfolioEntry")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson deletePortfolioEntry(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,
			@RequestParam("ticker") String ticker,@RequestParam("entryId") long entryId);
	
	@RequestMapping(value = "/getYearHighs")
	// CzResultJson contains Map<String,YearHigh> if success, message if fail
	public CzResultJson getYearHighs(OAuth2Authentication auth,@RequestParam("date") Date date);
	
	@RequestMapping(value = "/getYearHighsStr")
	// CzResultJson contains Map<String,YearHigh> if success, message if fail
	public CzResultJson getYearHighsStr(OAuth2Authentication auth,@RequestParam("dateStr") String dateStr);
}
