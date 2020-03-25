package app;

public class NewUserDetails {
	
	private Role role;
	private String userName;
	private String email;
	
	public NewUserDetails() {
		
	}
	

	
	public NewUserDetails(Role role, String userName, String email) {
		this.role = role;
		this.userName = userName;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	

	
	
	
	

}
