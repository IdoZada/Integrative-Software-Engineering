package acs.logic;

import java.util.List;
import acs.boundary.UserBoundary;

public interface UserService {
	
	public UserBoundary createUser(UserBoundary user);
	public UserBoundary login(String userDomain, String userEmail);
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update);
	public List<UserBoundary> getAllUsers(String admainDomain, String admainEmail);
	public void deleteAllUsers(String admainDomain, String admainEmail);

}