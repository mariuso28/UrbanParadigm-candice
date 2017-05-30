package org.cz.rest.anon;

import java.util.Date;

import org.cz.json.message.CzResultJson;
import org.springframework.web.bind.annotation.RequestMapping;

public interface RestAnonController {

	@RequestMapping(value = "/getSecurities")
	// CzResultJson contains List<Security> if success, message if fail
	public CzResultJson getSecurities();
	
	@RequestMapping(value = "/getSecurityDailyForRange")
	// Dx4ResultJson contains List<SecurityDaily> if success, message if fail
	public CzResultJson getSecurityDailyForRange(final String ticker,final Date start,final Date end);
	
	@RequestMapping(value = "/getSecurityDailyForRangeStr")
	// Dx4ResultJson contains List<SecurityDaily> if success, message if fail
	public CzResultJson getSecurityDailyForRangeStr(final String ticker,String start,final String end);
	
	
}
