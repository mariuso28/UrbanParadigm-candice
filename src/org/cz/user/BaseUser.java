package org.cz.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cz.portfolio.Portfolio;

public class BaseUser 
{
	private UUID id;
	private String contact;
	private String email;
	private String phone;
	private String password;
	private boolean enabled;
	private Role role;
	private String icon;
	private List<Portfolio> portfolios = new ArrayList<Portfolio>();
	
	public BaseUser()
	{
		
	}
	
	public BaseUser(String email)
	{
		setEmail(email);
		setEnabled(true);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	@Override
	public String toString() {
		return "BaseUser [id=" + id + ", contact=" + contact + ", email=" + email + ", phone=" + phone + ", password="
				+ password + ", enabled=" + enabled + ", role=" + role + ", icon=" + icon + "]";
	}

	
}
