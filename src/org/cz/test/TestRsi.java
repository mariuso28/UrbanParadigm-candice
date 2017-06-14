package org.cz.test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.json.chart.Rsi;
import org.cz.json.chart.RsiEntry;
import org.cz.json.security.SecurityDaily;
import org.cz.services.CzService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestRsi {
	private static Logger log = Logger.getLogger(TestRsi.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"cz-service.xml");

		CzService service = (CzService) context.getBean("services");
		
		
		try {
			GregorianCalendar gc = new GregorianCalendar();
			final Date end = gc.getTime();
			gc.set(2002,0,1);
			final Date start = gc.getTime();
			
			List<SecurityDaily> sdList = service.getHome().getSecurityDailyForRange("AIRASIA",start,end);
			
			Rsi ma = Rsi.createRsi(sdList);
			int cnt = 0;
			for (RsiEntry mae : ma.getEntries())
			{
				log.info(cnt + " : " + mae);
				if (cnt++>30)
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
