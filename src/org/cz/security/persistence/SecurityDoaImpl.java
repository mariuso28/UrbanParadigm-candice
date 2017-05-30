package org.cz.security.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.security.Security;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class SecurityDoaImpl extends NamedParameterJdbcDaoSupport implements SecurityDao {
	
	private static Logger log = Logger.getLogger(SecurityDoaImpl.class);
	
	@Override
	public Security getSecurityById(final long id) {
		
		try
		{
			final String sql = "SELECT * FROM security WHERE id = ?";
			return getJdbcTemplate().queryForObject(sql,new Long[] { id },BeanPropertyRowMapper.newInstance(Security.class));
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public Security getSecurity(final String ticker) {
		
		try
		{
			final String sql = "SELECT * FROM security WHERE ticker = ?";
			return getJdbcTemplate().queryForObject(sql,new String[] { ticker },BeanPropertyRowMapper.newInstance(Security.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public Security getSecurityByCode(String code) {
		try
		{
			final String sql = "SELECT * FROM security WHERE code = ?";
			return getJdbcTemplate().queryForObject(sql,new String[] { code },BeanPropertyRowMapper.newInstance(Security.class));
		}
		catch (EmptyResultDataAccessException e)
		{
			return null;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	
	
	@Override
	public List<Security> getSecuritys() {
		
			try
		{
			final String sql = "SELECT * FROM security";
			List<Security> secs = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Security.class));
			return secs;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public void storeSecurity(final Security security){
				
		// delete old 
		deleteSecurity(security);
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String INSERT_SQL = "INSERT INTO security (name,ticker,code) VALUES (?,?,?)";
			getJdbcTemplate().update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps =
			                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
			            ps.setString(1, security.getName());
			            ps.setString(2, security.getTicker());
			            ps.setString(3, security.getCode());
			            return ps;
			        }
			    },
			    keyHolder);
			
		//	log.info("key holder: " + keyHolder.getKey().longValue());
			final long id = keyHolder.getKey().longValue();
			security.setId(id);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}	
	}

	private void deleteSecurity(Security security){
		
		try
		{
			getJdbcTemplate().update("DELETE FROM security WHERE ticker = ?"
					, new PreparedStatementSetter() {
			      public void setValues(PreparedStatement psSd) throws SQLException {
			    		psSd.setString(1,security.getTicker());
					}
			    });
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException(e.getMessage());
		}		
	}
	
}
