package org.cz.security.persistence;

import java.util.Date;
import java.util.List;

import org.cz.security.SecurityDaily;

public interface SecurityDailyDao {
	
	public SecurityDaily getSecurityDaily(String ticker,Date date);
	public List<SecurityDaily> getSecurityDailys(Date date);
	public void storeSecurityDaily(SecurityDaily securityDaily);
	public List<SecurityDaily> getSecurityDailyForRange(String ticker, Date start, Date end);
		
}
