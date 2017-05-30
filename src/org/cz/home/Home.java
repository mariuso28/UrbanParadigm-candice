package org.cz.home;

import java.util.Date;
import java.util.List;

import org.cz.json.security.Security;
import org.cz.json.security.SecurityDaily;

public interface Home {
	
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
