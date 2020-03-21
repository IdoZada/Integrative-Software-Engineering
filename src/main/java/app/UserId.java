package app;

public class UserId {

	private String email;
	private String domain;
	
	public UserId() {
		
	}
	public UserId(String email, String domain) {
		
		this.email = email;
		this.domain = domain;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
