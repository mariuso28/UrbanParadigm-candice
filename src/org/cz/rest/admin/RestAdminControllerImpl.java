package org.cz.rest.admin;

import org.apache.log4j.Logger;
import org.cz.json.message.CzResultJson;
import org.cz.rest.trader.RestTraderControllerImpl;
import org.cz.services.CzService;
import org.cz.services.CzServicesException;
import org.cz.user.BaseUser;
import org.cz.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class RestAdminControllerImpl implements RestAdminController {
	
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
		BaseUser user = getAdmin(email,result);
		if (user==null)
			return result;
		
		try
		{
			log.info(user.getEmail() + " - updating portfolios");
			czServices.getPortfolioMgr().updatePortfolios();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			result.fail("Could not update portfolios - " + e1.getMessage());
		}
		
		return result;
	}

	@Override
	@RequestMapping(value = "/enableUser")
	// CzResultJson contains info message if success, error message if fail
	public CzResultJson enableUser(OAuth2Authentication auth, String userEmail) {
		return setEnabled(auth,userEmail,true);
	}

	@Override
	@RequestMapping(value = "/disableUser")
	// CzResultJson contains info message if success, error message if fail
	public CzResultJson disableUser(OAuth2Authentication auth, String userEmail) {
		return setEnabled(auth,userEmail,false);
	}
	
	private CzResultJson setEnabled(OAuth2Authentication auth, String userEmail, boolean enabled)
	{
		String email = ((User)auth.getPrincipal()).getUsername();
		
		CzResultJson result = new CzResultJson();
		BaseUser user = getAdmin(email,result);
		if (user == null)
			return result;
		
		try
		{
			czServices.updateEnabled(userEmail,enabled);
			String msg = "User : " + userEmail + " successfully " + (enabled ? "enabled." : "disabled.");
			result.success(msg);
		}
		catch (CzServicesException e)
		{
			result.fail(e.getMessage());
		}
		catch (Exception e1)
		{
			String msg = "Could not enable/disable user : " + userEmail + " - " + e1.getMessage(); 
			log.error(msg);
			e1.printStackTrace();
			result.fail(msg);
		}
		
		return result;
	}
	
	private BaseUser getAdmin(String email,CzResultJson result)
	{
		BaseUser user = czServices.getHome().getBaseUserByEmail(email);
		if (user == null)
		{
			result.fail("User : " + email + " not found");
			return null;
		}
		if (!user.getRole().equals(Role.ROLE_ADMIN))
		{
			result.fail("User : " + email + " not an admin user");
			return null;
		}
		return user;
	}

	
	
}
