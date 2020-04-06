package acs.logic.mockup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import acs.*;
import acs.boundary.UserBoundary;
import acs.data.UserEntity;
import acs.logic.UserService;

public class UserServiceMockup implements UserService{

	private static long id = 1000;
	private Map<String,UserEntity> users = new ConcurrentHashMap<>();
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		//TODO: check if need to change UserBoundry param to NewUserDetails
		if(users.containsKey(user.getUserId())){
			throw new RuntimeException("User Already exist");
		}
		users.put(user.getUserId(), user);
		return user;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		UserId userId = new UserId(userDomain, userEmail);
		UserBoundary result = users.get(userId);
		if(result == null)
			throw new RuntimeException("User Does not exist");
		return result;
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		if(!users.containsKey(userDomain + "@@" + userEmail))
			throw new RuntimeException("User does not exist");
		UserBoundary userBoundary = new UserBoundary(update.getRole(), update.getUserName(), new UserId(userDomain, userEmail), update.getAvatar());
		users.get(userDomain + "@@" + userEmail).setAvatar(update.getAvatar());
		users.get(userDomain + "@@" + userEmail).setRole(update.getRole());
		users.get(userDomain + "@@" + userEmail).setUserName(update.getUserName());
		return userBoundary;
	}

	@Override
	public List<UserBoundary> getAllUsers(String admainDomain, String admainEmail) {
		List<UserBoundary> allUsers = new ArrayList<>();
		for(UserEntity userEntity : users.values()) {
			allUsers.add(new UserBoundary(userEntity.getRole(), userEntity.getUserName(), userEntity.getUserId(), userEntity.getAvatar()));
		}
		return allUsers;
	}

	@Override
	public void deleteAllUsers(String admainDomain, String admainEmail) {
		users.clear();
	}

}
