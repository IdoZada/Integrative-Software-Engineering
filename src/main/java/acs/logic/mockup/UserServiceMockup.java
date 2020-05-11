package acs.logic.mockup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.UserId;
import acs.converter.UserEntityConverter;
import acs.data.UserEntity;
import acs.logic.UserService;


public class UserServiceMockup implements UserService {

	private String projectName;
	private UserEntityConverter userEntityConverter;
	private Map<String, UserEntity> usersDatabase;

	@Autowired
	public UserServiceMockup(UserEntityConverter userEntityConverter) {
		this.userEntityConverter = userEntityConverter;
	}

	// inject value from configuration or use default value
	@Value("${spring.application.name:2020b.daniel.zusev}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {
		this.usersDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		user.setUserId(new UserId(this.projectName, user.getUserId().getEmail()));
		UserEntity userEntity = userEntityConverter.toEntity(user);
		usersDatabase.put(userEntity.getUserId(), userEntity);
		return user;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		UserEntity userEntity = usersDatabase.get(userDomain + "@@" + userEmail);
		UserBoundary userBoundary = userEntityConverter.fromEntity(userEntity);
		return userBoundary;
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		boolean dirtyFlag = false;
		if(usersDatabase.get(userDomain+"@@"+userEmail) == null) {
			System.err.println("There Is No Such User In The System");
			return update;
		}
		
		UserEntity userEntity = userEntityConverter.toEntity(update);
		UserBoundary existing = userEntityConverter.fromEntity(usersDatabase.get(userEntity.getUserId()));
		if (update.getAvatar() != null) {
			dirtyFlag = true;
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUserName() != null) {
			dirtyFlag = true;
			existing.setUserName(update.getUserName());
		}
		
		if (update.getRole() != null) {
			dirtyFlag = true;
			existing.setRole(update.getRole());
		}

		if (dirtyFlag)
			usersDatabase.put(userEntity.getUserId(), userEntityConverter.toEntity(existing));
		return existing;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		List<UserBoundary> allUsers = new ArrayList<>();
		for (UserEntity userEntity : usersDatabase.values())
			allUsers.add(userEntityConverter.fromEntity(userEntity));
		return allUsers;
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		usersDatabase.clear();
	}

}
