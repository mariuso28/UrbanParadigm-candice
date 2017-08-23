package org.cz.portfolio.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cz.json.portfolio.PortfolioActionType;
import org.cz.json.portfolio.PortfolioTransaction;
import org.springframework.jdbc.core.RowMapper;

public class PortfolioTransactionRowmapper implements RowMapper<PortfolioTransaction>{

	public PortfolioTransactionRowmapper()
	{
	}
	
	public PortfolioTransaction mapRow(ResultSet rs,int rowNum) {
	
		PortfolioTransaction pt = new PortfolioTransaction();
		try {
			setValues(rs,pt);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return pt;
	}

	public static void setValues(ResultSet rs,PortfolioTransaction pt) throws SQLException{
		
		pt.setId(rs.getLong("id"));
		pt.setTraderEmail(rs.getString("traderemail"));
		pt.setPortfolioName(rs.getString("portfolioname"));
		pt.setTimestamp(rs.getTimestamp("timestamp"));
		pt.setTicker(rs.getString("ticker"));
		pt.setPrice(rs.getDouble("price"));
		pt.setQuantity(rs.getDouble("quantity"));
		pt.setAction(PortfolioActionType.valueOf(rs.getString("action")));
		pt.setBrokerage(rs.getDouble("brokerage"));
		pt.setClearing(rs.getDouble("clearing"));
		pt.setStamp(rs.getDouble("stamp"));
		pt.setGst(rs.getDouble("gst"));
		pt.setNet(rs.getDouble("net"));
	}
	
}
