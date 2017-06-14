package org.cz.portfolio.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.json.portfolio.Portfolio;
import org.cz.json.portfolio.PortfolioEntry;
import org.cz.json.portfolio.PortfolioEntryType;
import org.cz.json.portfolio.PortfolioWatch;
import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.user.BaseUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class PortfolioDaoImpl extends NamedParameterJdbcDaoSupport implements PortfolioDao {
	
	private static Logger log = Logger.getLogger(PortfolioDaoImpl.class);
	
	public PortfolioDaoImpl()
	{
	}

	@Override
	public void deletePortfolio(final Portfolio portfolio)  {
		try
		{
			getJdbcTemplate().update("DELETE FROM portfolio WHERE id = "
					 , new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
				    	  	ps.setLong(1, portfolio.getId());
				      }
				    });
			for (PortfolioWatch watch : portfolio.getWatchList().values())
			{
				deletePortfolioWatch(watch);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void storePortfolio(final BaseUser baseUser, final Portfolio portfolio)  {
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String sql = "INSERT INTO portfolio (baseuserid,name,description) VALUES (?,?,?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(sql, new String[] {"id"});
			            ps.setObject(1, baseUser.getId());
						ps.setString(2, portfolio.getName());
						ps.setString(3, portfolio.getDescription());
			            return ps;
			        }
			    },
			    keyHolder);
			
			final long id = keyHolder.getKey().longValue();
			portfolio.setId(id);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	@Override
	public Map<String,Portfolio> getPortfolios(final BaseUser baseUser)  {
		try
		{
			final String sql = "SELECT * FROM portfolio WHERE baseuserid=?";
			List<Portfolio> ports = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setObject(1,baseUser.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(Portfolio.class));
			Map<String,Portfolio> mp = new TreeMap<String,Portfolio>();
			for (Portfolio port : ports)
			{
				getPortfolioWatchList(port);
				mp.put(port.getName(),port);
			}
			return mp;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public void storePortfolioWatch(final Portfolio portfolio, final PortfolioWatch watch) {
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String sql = "INSERT INTO portfoliowatch (portfolioid,ticker) VALUES (?,?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(sql, new String[] {"id"});
			            ps.setLong(1, portfolio.getId());
						ps.setString(2, watch.getTicker());
			            return ps;
			        }
			    },
			    keyHolder);
			
			final long id = keyHolder.getKey().longValue();
			watch.setId(id);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void deletePortfolioWatch(final PortfolioWatch watch){
		try
		{
			getJdbcTemplate().update("DELETE FROM portfoliowatch WHERE id = ?"
					  , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setLong(1, watch.getId());
			      }
			    });
			for (PortfolioEntry pe : watch.getEntries())
			{
				deletePortfolioEntry(pe);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	private void getPortfolioWatchList(final Portfolio portfolio) {
		try
		{
			final String sql = "SELECT * FROM portfoliowatch WHERE portfolioid=?";
			List<PortfolioWatch> watches = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setLong(1,portfolio.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(PortfolioWatch.class));
			for (PortfolioWatch watch : watches)
			{
				getPortfolioEntries(watch);
				portfolio.getWatchList().put(watch.getTicker(), watch);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	
	private void getPortfolioEntries(final PortfolioWatch watch) {
		try
		{
			final String sql = "SELECT * FROM portfolioentry WHERE portfoliowatchid=?";
			List<PortfolioEntry> ports = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setLong(1,watch.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(PortfolioEntry.class));
			for (PortfolioEntry port : ports)
			{
				if (port.getType().equals(PortfolioEntryType.HockeyStick))
				{
					port = getPortfolioEntryHss(port);
				}
				watch.getEntries().add(port);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	private PortfolioEntry getPortfolioEntryHss(final PortfolioEntry portEntry) {
		try
		{
			final String sql = "SELECT * FROM portfolioentryhs WHERE portfolioentryid=?";
			PortfolioEntryHs portHs = getJdbcTemplate().queryForObject(sql,new Long[] { portEntry.getId() },BeanPropertyRowMapper.newInstance(PortfolioEntryHs.class));
			portHs.setSecurityTicker(portEntry.getSecurityTicker());
			portHs.setId(portEntry.getId());
			portHs.setType(portEntry.getType());
			return portHs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void storePortfolioEntry(final PortfolioWatch watch,final PortfolioEntry entry) {

		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String sql = "INSERT INTO portfolioentry (portfoliowatchid,securityticker,type) VALUES (?,?,?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(sql, new String[] {"id"});
			            ps.setLong(1, watch.getId());
						ps.setString(2, entry.getSecurityTicker());
						ps.setString(3, entry.getType().name());
			            return ps;
			        }
			    },
			    keyHolder);
			
			final long id = keyHolder.getKey().longValue();
			entry.setId(id);
			
			if (entry.getType().equals(PortfolioEntryType.HockeyStick))
				storePortfolioEntryHs((PortfolioEntryHs) entry);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	private void storePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException
	{
		final String sql = "INSERT INTO portfolioentryhs (portfolioentryid,status,daycount,ceiling) VALUES (?,?,?,?)";
		try
		{
			getJdbcTemplate().update(sql
			        , new PreparedStatementSetter() {
			      public void setValues(PreparedStatement ps) throws SQLException {
				        	ps.setLong(1, phs.getId());
							ps.setString(2, phs.getStatus().name());
							ps.setInt(3, phs.getDayCount());
							ps.setDouble(4, phs.getCeiling());
			      }
		    });
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updatePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException {
		
		final String sql = "UPDATE portfolioentryhs SET status=?,daycount=?,ceiling=? WHERE id = ?";
		
		try
		{
			getJdbcTemplate().update(sql
				, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, phs.getStatus().name());
					ps.setInt(2, phs.getDayCount());
					ps.setDouble(3, phs.getCeiling());
					ps.setLong(4, phs.getId());
				}
				});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void deletePortfolioEntry(final PortfolioEntry entry) {
		try
		{
			if (entry.getType().equals(PortfolioEntryType.HockeyStick))
				getJdbcTemplate().update("DELETE FROM portfolioentryhs WHERE portfolioentryid = ?"
						  , new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
				    	  	ps.setLong(1, entry.getId());
				      }
				    });
			
			getJdbcTemplate().update("DELETE FROM portfolioentry WHERE id = ?"
					  , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setLong(1, entry.getId());
			      }
			    });
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void setUpdated(final Portfolio portfolio) {
		
		final Timestamp ts = new Timestamp(portfolio.getUpdated().getTime());
		final String sql = "UPDATE portfolio SET updated=? WHERE id = ?";
		
		try
		{
			getJdbcTemplate().update(sql
				, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setTimestamp(1, ts);
					ps.setLong(2, portfolio.getId());
				}
				});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}


}
