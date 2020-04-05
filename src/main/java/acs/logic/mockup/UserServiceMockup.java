package acs.logic.mockup;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import acs.*;
import acs.boundary.UserBoundary;
import acs.logic.UserService;

public class UserServiceMockup implements UserService{

	private static long id = 1000;
	private Map<UserId,UserBoundary> users = new ConcurrentHashMap<>();
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String admainDomain, String admainEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers(String admainDomain, String admainEmail) {
		users.clear();
	}

}
