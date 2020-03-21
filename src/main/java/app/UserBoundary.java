package app;

public class UserBoundary {

	private Role role;
	private String userName;
	private UserId userId;
	
	public UserBoundary() {
		
	}
	public UserBoundary(Role role, String userName, UserId userId) {
		
		this.role = role;
		this.userName = userName;
		this.userId = userId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
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
}
