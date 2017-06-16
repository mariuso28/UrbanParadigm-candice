package org.cz.test;

import java.util.ArrayList;
import java.util.List;

import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryI;
import org.cz.json.portfolio.PortfolioEntryType;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.hs.PortfolioHsStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDeserializer {

	private static ObjectMapper objectMapper;
	static{
		objectMapper = new ObjectMapper();
	}
	
	@SuppressWarnings("unchecked")
	public static Object readJsonObject(String jsonString,@SuppressWarnings("rawtypes") Class clazz) throws Exception
	{
		return objectMapper.readValue(jsonString, clazz);
	}
	
	public static String writeJsonObject(Object obj) throws JsonProcessingException 
	{
		return objectMapper.writeValueAsString(obj);
	}
	
	public static void main(String args[])
	{
		PortfolioEntry pe = new PortfolioEntry();
		pe.setSecurityTicker("CALTEC");
		pe.setType(PortfolioEntryType.Base);
		
		PortfolioEntryHs peh = new PortfolioEntryHs();
		peh.setCeiling(10.0);
		peh.setDayCount(3);
		peh.setSecurityTicker("AIRASIA");
		peh.setStatus(PortfolioHsStatus.HITYEARHIGH);
		peh.setType(PortfolioEntryType.HockeyStick);
		
		List<PortfolioEntryI> list = new ArrayList<PortfolioEntryI>();
		list.add(pe);
		list.add(peh);
		
		System.out.println("Before");
		for (PortfolioEntryI pei : list)
			System.out.println(pei);
		
		try
		{
			String bb = writeJsonObject(list);
			TypeReference<List<PortfolioEntryI>> mapType = new TypeReference<List<PortfolioEntryI>>() {};
			list = objectMapper.readValue(bb,mapType);
			
			System.out.println("After");

			for (PortfolioEntryI pei : list)
				System.out.println(pei);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
