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

	private Map<String,UserEntity> users = new ConcurrentHashMap<>();
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		String domain = user.getUserId().getDomain();
		String email = user.getUserId().getEmail();
		if(users.containsKey(domain + "@@" + email)){
			throw new RuntimeException("User Already exist");
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(user.getUserId());
		userEntity.setRole(user.getRole());
		userEntity.setAvatar(user.getAvatar());
		userEntity.setUserName(user.getUserName());
		users.put(domain + "&&" + email,userEntity);
		return user;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		UserEntity entity = users.get(userDomain + "@@" + userEmail);
		if(entity == null)
			throw new RuntimeException("User Does not exist");
		UserBoundary result = new UserBoundary();
		result.setAvatar(entity.getAvatar());
		result.setRole(entity.getRole());
		result.setUserId(entity.getUserId());
		result.setUserName(entity.getUserName());
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
