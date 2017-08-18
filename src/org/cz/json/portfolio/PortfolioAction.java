package org.cz.json.portfolio;

public class PortfolioAction {

	private String ticker;
	private PortfolioActionType action;
	
	public PortfolioAction()
	{
	}
	
	public PortfolioAction(String ticker,PortfolioActionType action)
	{
		setTicker(ticker);
		setAction(action);
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public PortfolioActionType getAction() {
		return action;
	}

	public void setAction(PortfolioActionType action) {
		this.action = action;
	}
	
	
}
