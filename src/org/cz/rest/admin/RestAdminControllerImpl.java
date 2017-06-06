package org.cz.rest.admin;

import org.apache.log4j.Logger;
import org.cz.json.message.CzResultJson;
import org.cz.rest.trader.RestTraderControllerImpl;
import org.cz.services.CzService;
import org.cz.user.BaseUser;
import org.cz.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

public class RestAdminControllerImpl implements RestAdminController {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(RestTraderControllerImpl.class);
	
	@Autowired
	private CzService czServices;

	@Override
	@RequestMapping(value = "/dailyUpdate")
	// CzResultJson contains info message if success, error message if fail
	public CzResultJson dailyUpdate(OAuth2Authentication auth)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return result;
		}
		if (!user.getRole().equals(Role.ROLE_ADMIN))
		{
			result.fail("User : " + email + " not an admin user");
			return result;
		}
		
		try
		{
			czServices.getPortfolioMgr().updatePortfolios();
		}
		catch (Exception e)
		{
			result.fail("Could not update portfolios - " + e.getMessage());
		}
		
		return result;
	}

}
