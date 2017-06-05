package org.cz.test;

import org.apache.log4j.Logger;
import org.cz.json.portfolio.PortfolioException;
import org.cz.json.security.Security;
import org.cz.services.CzService;
import org.cz.services.PortfolioMgr;
import org.cz.user.BaseUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPortfoliojava 
{
	private static Logger log = Logger.getLogger(TestPortfoliojava.class);
	
	private static void createPortfolio(BaseUser bu,CzService service) throws PortfolioException {

	
		PortfolioMgr portfolioMgr = service.getPortfolioMgr();
		portfolioMgr.createPortfolio(bu,"Popular","Popular Stocks");
		
		Security sec = service.getHome().getSecurity("CYPARK");
		portfolioMgr.createWatch(bu.getPortfolios().get("Popular"),sec.getTicker());
		
		
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		CzService service = (CzService) context.getBean("services");
		
		BaseUser bu = service.getHome().getBaseUserByEmail("albert@test.com");
		
		try {
			createPortfolio(bu,service);
		} catch (PortfolioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bu = service.getHome().getBaseUserByEmail("albert@test.com");
		
		log.info(bu.getPortfolios());
	}


}
