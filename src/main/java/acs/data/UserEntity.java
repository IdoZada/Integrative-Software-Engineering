package acs.data;

import acs.*;

public class UserEntity {
	
	private UserRole role;
	private String userName;
	private String userId;
	private String avatar;
	
	public UserEntity() {
		
	}
	
	public UserEntity(UserRole role, String userName, String userId, String avatar) {
		this.role = role;
		this.userName = userName;
		this.userId = userId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
