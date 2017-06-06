package org.cz.rest.trader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cz.json.message.CzResultJson;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntryType;
import org.cz.json.security.YearHigh;
import org.cz.services.CzService;
import org.cz.user.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/trader")
public class RestTraderControllerImpl implements RestTraderController{
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(RestTraderControllerImpl.class);
	
	@Autowired
	private CzService czServices;
	

	@Override
	@RequestMapping(value = "/getProfile")
	// CzResultJson contains CzBaseUserProfileJson if success, message if fail
	public CzResultJson getProfile(OAuth2Authentication auth){
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		result.success(user.createCzBaseUserProfileJson());
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getPortfolios")
	// CzResultJson contains TreeMap<String,Portfolio> if success, message if fail
	public CzResultJson getPortfolios(OAuth2Authentication auth)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		result.success(user.getPortfolios());
		return result;
	}

	@Override
	@RequestMapping(value = "/getPortfolio")
	// CzResultJson contains Portfolio if success, message if fail
	public CzResultJson getPortfolios(OAuth2Authentication auth,@RequestParam("name") String name)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		Portfolio portfolio = user.getPortfolios().get(name);
		if (portfolio == null)
			result.fail("Portfolio : " + name + " - doen't exist");
		else
			result.success(portfolio);
		return result;
	}
	
	@Override
	@RequestMapping(value = "/createPortfolio")
	// CzResultJson contains TreeMap<String,Portfolio> if success, message if fail
	public CzResultJson createPortfolio(OAuth2Authentication auth,@RequestParam("name") String name,@RequestParam("description") String description)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			czServices.getPortfolioMgr().createPortfolio(user,name,description);
			result.success(user.getPortfolios());
		}
		catch (Exception e)
		{
			result.fail("Could no add ticker - " + e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/addToWatchList")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson addToWatchList(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,@RequestParam("ticker") String ticker)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			Portfolio portfolio = user.getPortfolios().get(portfolioName);
			if (portfolio == null)
				result.fail("Could not add ticker - portfolio : " + portfolioName + " - doen't exist");
			else
			{
				czServices.getPortfolioMgr().createWatch(portfolio, ticker);
				result.success(portfolio);
			}
		}
		catch (Exception e)
		{
			result.fail("Could no add ticker - " + e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/deleteFromWatchList")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson deleteFromWatchList(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,@RequestParam("ticker") String ticker)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			Portfolio portfolio = user.getPortfolios().get(portfolioName);
			if (portfolio == null)
				result.fail("Could not delete ticker - portfolio : " + portfolioName + " - doen't exist");
			else
			{
				czServices.getPortfolioMgr().deleteWatch(portfolio, ticker);
				result.success(portfolio);
			}
		}
		catch (Exception e)
		{
			result.fail("Could no add ticker - " + e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/createPortfolioEntry")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson createPortfolioEntry(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,
					@RequestParam("ticker") String ticker,@RequestParam("entryType") PortfolioEntryType type)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			Portfolio portfolio = user.getPortfolios().get(portfolioName);
			czServices.getPortfolioMgr().createPortfolioEntry(portfolio, ticker,type);
			result.success(portfolio);
		}
		catch (Exception e)
		{
			result.fail("Could not add entry - " + e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/deletePortfolioEntry")
	// CzResultJson contains refreshed portfolio if success, message if fail
	public CzResultJson deletePortfolioEntry(OAuth2Authentication auth,@RequestParam("portfolioName") String portfolioName,
			@RequestParam("ticker") String ticker,@RequestParam("entryId") long entryId)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			Portfolio portfolio = user.getPortfolios().get(portfolioName);
			czServices.getPortfolioMgr().deletePortfolioEntry(portfolio,ticker,entryId);
			result.success(portfolio);
		}
		catch (Exception e)
		{
			result.fail("Could not add entry - " + e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/getYearHighs")
	// CzResultJson contains Map<String,YearHigh> if success, message if fail
	public CzResultJson getYearHighs(OAuth2Authentication auth,@RequestParam("date") Date date)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		
		try
		{
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			gc.add(Calendar.YEAR, -1);
			Map<String,YearHigh> yhs = czServices.getHome().getYearHighs(date, gc.getTime());
			result.success(yhs);
		}
		catch (Exception e)
		{
			result.fail("Could get year highs - " + e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/getYearHighsStr")
	// CzResultJson contains Map<String,YearHigh> if success, message if fail
	public CzResultJson getYearHighsStr(OAuth2Authentication auth,@RequestParam("dateStr") String dateStr)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return getYearHighs(auth,sdf.parse(dateStr));
		} catch (ParseException e) {
			CzResultJson result = new CzResultJson();
			result.fail("Date format must be yyyy-MM-dd");
			return result;
		}
	}
}
