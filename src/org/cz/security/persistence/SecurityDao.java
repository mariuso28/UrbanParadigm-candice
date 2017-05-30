package org.cz.security.persistence;

import java.util.List;

import org.cz.json.security.Security;

public interface SecurityDao {
	
	public Security getSecurityById(final long id);
	public Security getSecurity(final String ticker);
	public List<Security> getSecuritys();
	public void storeSecurity(final Security security);
	public Security getSecurityByCode(String code);
	public List<Security> searchSecuritys(String searchTerm);
		
}
