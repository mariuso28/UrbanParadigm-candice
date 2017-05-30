package org.cz.rest.anon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.cz.json.message.CzResultJson;
import org.cz.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anon")
public class RestAnonControllerImpl implements RestAnonController{
	
	private static final Logger log = Logger.getLogger(RestAnonControllerImpl.class);
	
	@Autowired
	private Service services;

	
	@Override
	@RequestMapping(value = "/getSecurities")
	// CzResultJson contains List<Security> if success, message if fail
	public CzResultJson getSecurities()
	{
		log.info("Got /getSecurities");
		CzResultJson result = new CzResultJson();
		
		try
		{
			result.success(services.getHome().getSecuritys());
		}
		catch (Exception e)
		{
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/searchSecurities")
	// CzResultJson contains List<Security> if success, message if fail
	public CzResultJson searchSecurities(final String searchTerm)
	{
		log.info("Got /searchSecurities");
		CzResultJson result = new CzResultJson();
		
		try
		{
			result.success(services.getHome().searchSecuritys(searchTerm));
		}
		catch (Exception e)
		{
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getSecurityDailyForRange")
	// CzResultJson contains List<SecurityDaily> if success, message if fail
	public CzResultJson getSecurityDailyForRange(final String ticker,final Date start,final Date end)
	{
		log.info("Got /getSecurityDailyForRange");
		CzResultJson result = new CzResultJson();
		
		try
		{
			result.success(services.getHome().getSecurityDailyForRange(ticker, start, end));
		}
		catch (Exception e)
		{
			result.fail(e.getMessage());
		}
		return result;
	}
	
	@Override
	@RequestMapping(value = "/getSecurityDailyForRangeStr")
	// Dx4ResultJson contains List<SecurityDaily> if success, message if fail
	public CzResultJson getSecurityDailyForRangeStr(final String ticker,String start,final String end)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return getSecurityDailyForRange(ticker,sdf.parse(start),sdf.parse(end));
		} catch (ParseException e) {
			CzResultJson result = new CzResultJson();
			result.fail("Date format must be yyyy-MM-dd");
			return result;
		}
	}
}
