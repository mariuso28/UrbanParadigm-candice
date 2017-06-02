package org.cz.user.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.user.BaseUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Transactional
public class BaseUserDaoImpl extends NamedParameterJdbcDaoSupport implements BaseUserDao {
	
	private static Logger log = Logger.getLogger(BaseUserDaoImpl.class);
	
	@Override
	public void storeBaseUser(final BaseUser baseUser) throws PersistenceRuntimeException {
		
		baseUser.setId(UUID.randomUUID());
		
		try
		{
			getJdbcTemplate().update("DELETE FROM baseuser WHERE email=?"
					 , new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
				    	  	ps.setString(1, baseUser.getEmail().toLowerCase());
				      }
				    });
			
			getJdbcTemplate().update("INSERT INTO baseuser (id,email,contact,phone,role,icon,enabled,password) "
										+ "VALUES (?,?,?,?,?,?,?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setObject(1, baseUser.getId());
						ps.setString(2, baseUser.getEmail().toLowerCase());
						ps.setString(3, baseUser.getContact());
						ps.setString(4, baseUser.getPhone());
						ps.setString(5, baseUser.getRole().name());
						ps.setString(6, baseUser.getIcon());
						ps.setBoolean(7, baseUser.isEnabled());
						ps.setString(8, baseUser.getPassword());
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
	public void updateBaseUserProfile(final BaseUser baseUser) throws PersistenceRuntimeException {
		
		try
		{
			getJdbcTemplate().update("UPDATE baseuser SET contact=?,phone=?,icon =?,enabled=?,password=? WHERE id=?"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, baseUser.getContact());
						ps.setString(2, baseUser.getPhone());
						ps.setString(4, baseUser.getIcon());
						ps.setBoolean(5, baseUser.isEnabled());
						ps.setString(6, baseUser.getPassword());
						ps.setObject(7, baseUser.getId());
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
	public String getEmailForId(UUID id) throws PersistenceRuntimeException
	{
		String email;	
		try
		{
			String sql = "SELECT email FROM baseuser WHERE id=?";
			email = getJdbcTemplate().queryForObject(sql,new Object[] { id }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
		return email;
	}
	
	@Override
	public BaseUser getBaseUserByEmail(final String email) throws PersistenceRuntimeException 
	{	
		try
		{
			final String sql = "SELECT id,email,password,enabled,icon,contact,phone,role FROM baseUser WHERE email=?";
			List<BaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, email);
				        }
				      }, BeanPropertyRowMapper.newInstance(BaseUser.class));
			if (bus.isEmpty())
				return null;
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
	}
	

	@Override
	public void storeImage(final String email, MultipartFile data, final String contentType) throws PersistenceRuntimeException {
		
		final InputStream is;
		try {
			is = data.getInputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
			log.error("Could not convert : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
		try
		{
			String sql = "DELETE FROM image WHERE email = '" + email + "'";
			getJdbcTemplate().update(sql);
			getJdbcTemplate().update("INSERT INTO image (email,contenttype,data) VALUES (?, ?, ?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, email);
							ps.setString(2, contentType);
							ps.setBinaryStream(3, is);
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
	public byte[] getImage(final String email) throws PersistenceRuntimeException {
		
		byte[] imgBytes = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = getConnection().prepareStatement("SELECT data FROM image WHERE email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs != null) {
			    while (rs.next()) {
			        imgBytes = rs.getBytes(1);
			    }
			    rs.close();
			}
			ps.close();
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new PersistenceRuntimeException("Could not execute : " + e.getMessage());
		}
		finally
		{
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ps!=null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return imgBytes;
	}

	@Override
	public void setDefaultPasswordForAll(String encoded) {
		String sql = "UPDATE baseuser SET password = '" + encoded + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
		}
	}
}
