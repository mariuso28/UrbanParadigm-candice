package org.cz.test;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.user.BaseUser;
import org.cz.user.Role;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestBaseUser 
{
	private static Logger log = Logger.getLogger(TestBaseUser.class);
	
	private static BaseUser createBaseUser1()
	{
		BaseUser bu = new BaseUser();
		bu.setContact("albert");
		bu.setEmail("albert@test.com");
		bu.setEnabled(true);
		bu.setIcon("");
		bu.setPassword("88888888");
		bu.setPhone("012345678");
		bu.setRole(Role.ROLE_TRADER);
		return bu;
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		BaseUser bu = createBaseUser1();
		home.storeBaseUser(bu);
		
		bu = home.getBaseUserByEmail("albert@test.com");
		
		log.info(bu);
	}
}
