package org.html.parser;

import java.util.HashMap;

public class ParseSchema {
	private String name;
	private HashMap<String,ParseUrl> parseUrls;
	
	public ParseSchema(String name)
	{
		setName(name);
		parseUrls = new HashMap<String,ParseUrl>();
	}
	
	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public HashMap<String,ParseUrl> getParseUrls() {
		return parseUrls;
	}
	
	public void addParseUrl( ParseUrl parseUrl )
	{
		parseUrls.put(parseUrl.getName(), parseUrl );
	}

	@Override
	public String toString() {
		String str = "ParseSchema [name=" + name + "] parseUrls: \n";
		for (ParseUrl parseUrl : parseUrls.values())
			str += parseUrl + "\n"; 
		return str;
	}
	
	
	
}
