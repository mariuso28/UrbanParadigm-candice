package org.cz.portfolio;

import java.util.Map;
import java.util.TreeMap;

public class Portfolio {

	private long id;
	private String name;
	private String description;
	private Map<String,PortfolioEntry> entries = new TreeMap<String,PortfolioEntry>();
	
	public Portfolio()
	{
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, PortfolioEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, PortfolioEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "Portfolio [id=" + id + ", name=" + name + ", description=" + description + ", entries=" + entries + "]";
	}
	
}
