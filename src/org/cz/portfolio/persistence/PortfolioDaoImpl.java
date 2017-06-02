package org.cz.portfolio.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.portfolio.Portfolio;
import org.cz.portfolio.PortfolioEntry;
import org.cz.portfolio.hs.PortfolioEntryHs;
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
	public void deletePortfolio(final Portfolio portfolio) throws PersistenceRuntimeException {
		try
		{
			getJdbcTemplate().update("DELETE FROM portfolio WHERE id = "
					 , new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
				    	  	ps.setLong(1, portfolio.getId());
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
	public void storePortfolio(final BaseUser baseUser, final Portfolio portfolio) throws PersistenceRuntimeException {
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
	public List<Portfolio> getPortfolios(final BaseUser baseUser) throws PersistenceRuntimeException {
		try
		{
			final String sql = "SELECT * FROM portfolio WHERE baseuserid=?";
			List<Portfolio> ports = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setObject(1,baseUser.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(Portfolio.class));
			for (Portfolio port : ports)
			{
				getPortfolioEntries(port);
			}
			return ports;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	private void getPortfolioEntries(final Portfolio portfolio) {
		try
		{
			final String sql = "SELECT * FROM portfolioentry WHERE portfolioid=? ORDER BY securityticker";
			List<PortfolioEntry> ports = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setLong(1,portfolio.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(PortfolioEntry.class));
			for (PortfolioEntry port : ports)
			{
				getPortfolioEntryHss(port);
				portfolio.getEntries().put(port.getSecurityTicker(), port);
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	private void getPortfolioEntryHss(final PortfolioEntry portEntry) {
		try
		{
			final String sql = "SELECT * FROM portfolioentryhs WHERE portfolioentryid=?";
			List<PortfolioEntryHs> hsList = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setObject(1,portEntry.getId());
				        }
				      }, BeanPropertyRowMapper.newInstance(PortfolioEntryHs.class));
			portEntry.setHsList(hsList);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void storePortfolioEntry(final Portfolio portfolio,final PortfolioEntry entry) {

		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String sql = "INSERT INTO portfolioentry (portfolioid,securityticker) VALUES (?,?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(sql, new String[] {"id"});
			            ps.setLong(1, portfolio.getId());
						ps.setString(2, entry.getSecurity().getTicker());
			            return ps;
			        }
			    },
			    keyHolder);
			
			final long id = keyHolder.getKey().longValue();
			entry.setId(id);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void storePortfolioEntryHs(final PortfolioEntryHs phs,final PortfolioEntry entry) throws DataAccessException
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO portfolioentryhs (portfolioentryid,status,daycount,ceiling) VALUES (?,?,?,?)";
		try
		{
			getJdbcTemplate().update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps =
				                connection.prepareStatement(sql, new String[] {"id"});
				        	ps.setLong(1, entry.getId());
							ps.setString(2, phs.getStatus().name());
							ps.setInt(3, phs.getDayCount());
							ps.setDouble(4, phs.getCeiling());
				            return ps;
				        }
				    },
				    keyHolder);
			final long id = keyHolder.getKey().longValue();
			phs.setId(id);
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
	public void deletePortfolioEntryHs(final PortfolioEntryHs phs) throws DataAccessException {
		
		final String sql = "UPDATE portfolioentryhs SET status=?,daycount=?,ceiling=? WHERE id = ?";
		
		try
		{
			getJdbcTemplate().update("DELETE FROM portfolioentryhs WHERE id = ?"
					  , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setLong(1, phs.getId());
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
	
}
