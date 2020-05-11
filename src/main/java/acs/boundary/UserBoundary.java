package acs.boundary;

import acs.boundary.boundaryUtils.UserId;
import acs.data.UserRole;

public class UserBoundary {

	private UserRole role;
	private String username;
	private UserId userId;
	private String avatar;
	
	public UserBoundary() {
		
	}
	public UserBoundary(UserRole role, String username, UserId userId,String avatar) {
		
		this.role = role;
		this.username = username;
		this.userId = userId;
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
