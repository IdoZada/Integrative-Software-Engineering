package acs.boundary.boundaryUtils;

import acs.data.UserRole;

public class NewUserDetails {
	
	private UserRole role;
	private String username;
	private String email;
	private String avatar;
	
	public NewUserDetails() {
		
	}
	
	public NewUserDetails(UserRole role, String username, String email, String avatar) {
		this.role = role;
		this.username = username;
		this.email = email;
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
