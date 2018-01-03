package org.cz.security.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.json.security.SecurityDaily;
import org.cz.json.security.YearHigh;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class SecurityDailyDaoImpl extends NamedParameterJdbcDaoSupport implements SecurityDailyDao {
	
	private static Logger log = Logger.getLogger(SecurityDailyDaoImpl.class);
	
	@Override
	public void securityDailyPatch()
	{
		final String sql = "DELETE FROM securitydaily WHERE date > '2030-01-01'";
		log.info("Performing security daily patch for erroneous dates");
		try
		{
			getJdbcTemplate().update(sql);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public SecurityDaily getSecurityDaily(final String ticker,final Date date) {
		
		final Timestamp ts = new Timestamp(date.getTime());
		try
		{
			final String sql = "SELECT * FROM securitydaily WHERE ticker = ? AND date = ?";
			List<SecurityDaily> secs = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				        	preparedStatement.setString(1, ticker);
				        	preparedStatement.setTimestamp(2, ts);
				        }
				      }, BeanPropertyRowMapper.newInstance(SecurityDaily.class));
			if (secs.isEmpty())
				return null;
			return secs.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<SecurityDaily> getSecurityDailyForRange(final String ticker, Date start, Date end) {

		final Timestamp ts1;
		final Timestamp ts2;
		if (start.before(end))
		{
			ts1 = new Timestamp(start.getTime());
			ts2 = new Timestamp(end.getTime());
		}
		else
		{
			ts2 = new Timestamp(start.getTime());
			ts1 = new Timestamp(end.getTime());
		}
		try
		{
			final String sql = "SELECT * FROM securitydaily WHERE ticker=? AND date>=? AND date<=? ORDER BY date";
			List<SecurityDaily> secs = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				        	preparedStatement.setString(1, ticker);
				        	preparedStatement.setTimestamp(2, ts1);
				        	preparedStatement.setTimestamp(3, ts2);
				        }
				      }, BeanPropertyRowMapper.newInstance(SecurityDaily.class));
			return secs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	
	@Override
	public List<SecurityDaily> getSecurityDailys(final Date date) {
		
		final Timestamp ts = new Timestamp(date.getTime());
		try
		{
			final String sql = "SELECT * FROM securitydaily WHERE date = ?";
			List<SecurityDaily> secs = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				        	preparedStatement.setTimestamp(1, ts);
				        }
				      }, BeanPropertyRowMapper.newInstance(SecurityDaily.class));
			return secs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public List<SecurityDaily> getLastSecurityDailys() {
		
		try
		{
			final String sql = "SELECT * FROM securitydaily WHERE date = (select max(date) from securitydaily)";
			List<SecurityDaily> secs = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(SecurityDaily.class));
			return secs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void storeSecurityDaily(final SecurityDaily securityDaily){
		
		
		// delete old 
		deleteSecurityDaily(securityDaily);
		
		final Timestamp ts = new Timestamp(securityDaily.getDate().getTime());
		try
		{
			getJdbcTemplate().update("INSERT INTO securityDaily (ticker,date,open,high,low,close,volume) "
					+ "VALUES( ?, ?, ?, ?, ?, ?, ? )"
			        , new PreparedStatementSetter() {
			      public void setValues(PreparedStatement psSd) throws SQLException {
			    	  	psSd.setString(1,securityDaily.getTicker());
						psSd.setTimestamp(2,ts);
						psSd.setDouble(3,securityDaily.getOpen());
						psSd.setDouble(4,securityDaily.getHigh());
						psSd.setDouble(5,securityDaily.getLow());
						psSd.setDouble(6,securityDaily.getClose());
						psSd.setDouble(7,securityDaily.getVolume());
			      }
			    });
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	private void deleteSecurityDaily(final SecurityDaily securityDaily){
		
		final Timestamp ts = new Timestamp(securityDaily.getDate().getTime());
		try
		{
			getJdbcTemplate().update("DELETE FROM securitydaily WHERE ticker = ? AND date = ?"
					, new PreparedStatementSetter() {
			      public void setValues(PreparedStatement psSd) throws SQLException {
			    		psSd.setString(1,securityDaily.getTicker());
						psSd.setTimestamp(2,ts);
					}
			    });
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException(e.getMessage());
		}		
	}

	@Override
	public List<YearHigh> getYearHighs(Date date, Date date2) {
		
		final Timestamp ts1 = new Timestamp(date.getTime());
		final Timestamp ts2 = new Timestamp(date2.getTime());
		
		try
		{
			final String sql = "SELECT * FROM yearhigh WHERE date = ? AND date2 = ?";
			List<YearHigh> yhs = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				        	preparedStatement.setTimestamp(1, ts1);
				        	preparedStatement.setTimestamp(2, ts2);
				        }
				      }, BeanPropertyRowMapper.newInstance(YearHigh.class));
			if (!yhs.isEmpty())
				return yhs;
			
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			gc.add(Calendar.DAY_OF_YEAR, -1);
			final Timestamp ts3 = new Timestamp(gc.getTime().getTime());
			
			final String sql2 = "select high as high,ticker,date,s1.avg as avg,s1.sd as stddev from securitydaily as sd " +
					"join (select max(high) as max,ticker as t1,avg(high) as avg,stddev(high) as sd from securitydaily " +
					" where date<? and date>=? group by ticker) as s1 on sd.ticker = s1.t1 " +
					" where sd.high > s1.max and sd.date = ?"; 
			
		//	log.info(sql2);
			yhs = getJdbcTemplate().query(sql2,new PreparedStatementSetter() {
		        public void setValues(PreparedStatement preparedStatement) throws SQLException {
		        	preparedStatement.setTimestamp(1, ts3);
		        	preparedStatement.setTimestamp(2, ts2);
		        	preparedStatement.setTimestamp(3, ts1);
		        }
		      }, BeanPropertyRowMapper.newInstance(YearHigh.class));
			
			
			storeYearHighs(yhs, date2);
			return yhs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	private void storeYearHighs(List<YearHigh> yhs,Date date2) {
		for (final YearHigh yh : yhs)
		{
			yh.setDate2(date2);
			final Timestamp ts1 = new Timestamp(yh.getDate().getTime());
			final Timestamp ts2 = new Timestamp(yh.getDate2().getTime());
			
	//		log.info("Storing : " + yh);
			try
			{
				getJdbcTemplate().update("INSERT INTO yearhigh (ticker,date,date2,high,avg,stddev) "
						+ "VALUES( ?, ?, ?, ?, ?, ?)"
				        , new PreparedStatementSetter() {
				      public void setValues(PreparedStatement psSd) throws SQLException {
				    	  	psSd.setString(1,yh.getTicker());
							psSd.setTimestamp(2,ts1);
							psSd.setTimestamp(3,ts2);
							psSd.setDouble(4,yh.getHigh());
							psSd.setDouble(5,yh.getAvg());
							psSd.setDouble(6,yh.getStddev());
				      }
				    });
			}
			catch (Exception e)
			{
				log.error("Could not execute : " + e.getMessage());
				throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
			}	
		}
	}


}
