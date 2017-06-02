package org.cz.test;

import org.apache.log4j.Logger;
import org.cz.home.Home;
import org.cz.json.security.Security;
import org.cz.portfolio.Portfolio;
import org.cz.portfolio.PortfolioEntry;
import org.cz.portfolio.hs.PortfolioEntryHs;
import org.cz.portfolio.hs.PortfolioEntryHsStatus;
import org.cz.user.BaseUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPortfoliojava 
{
	private static Logger log = Logger.getLogger(TestPortfoliojava.class);
	
	private static Portfolio createPortfolio(BaseUser bu,Home home) {
		
		Portfolio pf = new Portfolio();
		pf.setName("Popular");
		pf.setDescription("Popular Stocks");
		PortfolioEntry pe = new PortfolioEntry();
		Security sec = home.getSecurity("CYPARK");
		pe.setSecurity(sec);
		PortfolioEntryHs pehs = new PortfolioEntryHs();
		pehs.setStatus(PortfolioEntryHsStatus.INDAYHIGH);
		
		return pf;
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		Home home = (Home) context.getBean("home");
		
		BaseUser bu = home.getBaseUserByEmail("albert@test.com");
		
		Portfolio ps = createPortfolio(bu,home);
		
		
		log.info(ps);
	}


}
