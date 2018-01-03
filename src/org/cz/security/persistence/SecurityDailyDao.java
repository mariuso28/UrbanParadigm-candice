package org.cz.security.persistence;

import java.util.Date;
import java.util.List;

import org.cz.json.security.SecurityDaily;
import org.cz.json.security.YearHigh;

public interface SecurityDailyDao {
	
	public SecurityDaily getSecurityDaily(String ticker,Date date);
	public List<SecurityDaily> getSecurityDailys(Date date);
	public List<SecurityDaily> getLastSecurityDailys();
	public void storeSecurityDaily(SecurityDaily securityDaily);
	public List<SecurityDaily> getSecurityDailyForRange(String ticker, Date start, Date end);
	public List<YearHigh> getYearHighs(Date date,Date date2);
	public void securityDailyPatch();	
}
