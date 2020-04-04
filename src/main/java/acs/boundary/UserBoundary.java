package acs.boundary;

import acs.UserId;
import acs.data.UserRole;

public class UserBoundary {

	private UserRole role;
	private String userName;
	private UserId userId;
	private String avatar;
	
	public UserBoundary() {
		
	}
	public UserBoundary(UserRole role, String userName, UserId userId,String avatar) {
		
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
	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
