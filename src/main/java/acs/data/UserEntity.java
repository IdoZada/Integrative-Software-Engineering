package acs.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
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

	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Column(nullable = false, unique = true)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Id
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(nullable = false)
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
