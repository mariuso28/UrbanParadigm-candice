package org.cz.config.service;

import org.apache.log4j.Logger;
import org.cz.validate.Validator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private static final Logger log = Logger.getLogger(CustomUserDetailsService.class);
//	@Autowired
//	private Dx4Services dx4Services;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("loadUserByUsername email : " + email);
		
		email = email.toLowerCase();
		if (!Validator.isValidEmailAddress(email))
		{
			log.error("Email " + email + " Invalid");
			throw new UsernameNotFoundException("Invalid email: " + email);
		}
		
		/*
		Dx4SecureUser baseUser;
		try
		{
			baseUser = dx4Services.getDx4Home().getByUsername(email, Dx4SecureUser.class);
		}
		catch (PersistenceRuntimeException | Dx4PersistenceException e)
		{
			log.error("Error finding User: " + email + " not found");
			throw new UsernameNotFoundException("Error finding User: " + email);
		}
		
		log.info("User : " + baseUser.getEmail() + " found with role :" + baseUser.getRole());
		
		Collection<GrantedAuthority> authorities = getAuthorities(baseUser);
		
		User user = new User(baseUser.getEmail(), baseUser.getPassword(), baseUser.isEnabled(), true, true, true, authorities);
		
		log.info("Using User : " + user.getUsername() + " with authorities :" + authorities);
		return user;
		*/
		return null;
	}

	/*
	private Collection<GrantedAuthority> getAuthorities(Dx4SecureUser baseUser) {
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		authList.add(new SimpleGrantedAuthority(baseUser.getRole().name()));

		return authList;
	}
	*/
}
