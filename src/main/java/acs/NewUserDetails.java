package acs;

import acs.data.UserRole;

public class NewUserDetails {
	
	private UserRole role;
	private String userName;
	private String email;
	private String avatar;
	
	public NewUserDetails() {
		
	}
	
	public NewUserDetails(UserRole role, String userName, String email, String avatar) {
		this.role = role;
		this.userName = userName;
		this.email = email;
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
