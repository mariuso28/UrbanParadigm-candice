package org.cz.user.persistence;

import java.util.UUID;

import org.cz.home.persistence.PersistenceRuntimeException;
import org.cz.user.BaseUser;
import org.springframework.web.multipart.MultipartFile;

public interface BaseUserDao{

	public void storeBaseUser(BaseUser baseUser) throws PersistenceRuntimeException;
	public BaseUser getBaseUserByEmail(String email) throws PersistenceRuntimeException;
	public void updateBaseUserProfile(BaseUser baseUser) throws PersistenceRuntimeException;
	public void storeImage(String email,MultipartFile data, String contentType) throws PersistenceRuntimeException;
	public byte[] getImage(final String email) throws PersistenceRuntimeException;
	public String getEmailForId(UUID id) throws PersistenceRuntimeException;
	public void setDefaultPasswordForAll(String encoded);
}
