package org.cz.rest.admin;

import org.cz.json.message.CzResultJson;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/admin")
public interface RestAdminController {
		
		@RequestMapping(value = "/dailyUpdate")
		// CzResultJson contains info message if success, error message if fail
		public CzResultJson dailyUpdate(OAuth2Authentication auth);
		
		@RequestMapping(value = "/enableUser")
		// CzResultJson contains info message if success, error message if fail
		public CzResultJson enableUser(OAuth2Authentication auth,String userEmail);
		
		@RequestMapping(value = "/diableUser")
		// CzResultJson contains info message if success, error message if fail
		public CzResultJson disableUser(OAuth2Authentication auth,String userEmail);
}
