package org.cz.json.portfolio;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Portfolio {

	private long id;
	private String name;
	private String description;
	private Date updated;
	private Map<String,PortfolioWatch> watchList = new TreeMap<String,PortfolioWatch>();
	
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

	public Map<String, PortfolioWatch> getWatchList() {
		return watchList;
	}

	public void setWatchList(Map<String, PortfolioWatch> watchList) {
		this.watchList = watchList;
	}
	
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "Portfolio [id=" + id + ", name=" + name + ", description=" + description + ", watchList=" + watchList
				+ "]";
	}

	
}
