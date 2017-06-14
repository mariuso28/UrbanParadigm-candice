package org.cz.json.portfolio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.cz.json.portfolio.hs.PortfolioEntryHs;

public class Portfolio {

	private long id;
	private String name;
	private String description;
	private Date updated;
	private Map<String,PortfolioWatch> watchList = new TreeMap<String,PortfolioWatch>();
	private List<PortfolioEntryHs> portFolioEntryHsList = new ArrayList<PortfolioEntryHs>(); 
	
	public Portfolio()
	{
	}
	
	private Portfolio createForSend()
	{
		// dont need to clone the entries as only used for sending
		Portfolio clone = new Portfolio();
		clone.setId(id);
		clone.setName(name);
		clone.setDescription(description);
		clone.setWatchList(watchList);
		clone.setPortFolioEntryHsList(portFolioEntryHsList);
		return clone;
	}
	
	public Portfolio prepareForJsonWrite()
	{
		// Must be called prior to any send
		Portfolio clone = createForSend();
		portFolioEntryHsList = new ArrayList<PortfolioEntryHs>();
		for (PortfolioWatch pfw : clone.getWatchList().values())
		{
			for (PortfolioEntry pfe : pfw.getEntries())
			{
				if (pfe.getType().equals(PortfolioEntryType.HockeyStick))
					clone.getPortFolioEntryHsList().add((PortfolioEntryHs) pfe);
			}
			pfw.getEntries().clear();
		}
		return clone;
	}

	public void restoreFromJsonRead()
	{
		// Must be called following receive
		for (PortfolioEntryHs pe : portFolioEntryHsList)
		{
			PortfolioWatch pw = watchList.get(pe.getSecurityTicker());
			pw.getEntries().add(pe);
		}
		portFolioEntryHsList.clear();
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

	public List<PortfolioEntryHs> getPortFolioEntryHsList() {
		return portFolioEntryHsList;
	}

	public void setPortFolioEntryHsList(List<PortfolioEntryHs> portFolioEntryHsList) {
		this.portFolioEntryHsList = portFolioEntryHsList;
	}

	
}
