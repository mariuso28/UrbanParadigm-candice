package org.cz.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	@SuppressWarnings("unused")
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
			System.out.println("Date,Close,Change,Gain,Loss,Avg Gain,Avg Loss,RS,RSI");
			NumberFormat formatter = new DecimalFormat("#0.00");
			
			int dnum = sdList.size() - ma.getEntries().size();
			for (RsiEntry m : ma.getEntries())
			{
				if (m==null)
					continue;
				String  str = sdList.get(dnum++).getDate() + "," + formatter.format(m.getClose()) + "," + formatter.format(m.getChange()) + "," 
							+ formatter.format(m.getGain()) + "," + formatter.format(m.getAvgGain()) +"," 
							+ formatter.format(m.getAvgLoss()) + "," + formatter.format(m.getRs()) + "," + formatter.format(m.getRsi());
				System.out.println(str);
				if (cnt++>30)
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
