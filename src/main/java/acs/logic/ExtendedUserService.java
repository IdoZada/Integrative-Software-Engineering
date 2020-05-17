package acs.logic;

import java.util.List;

import acs.boundary.UserBoundary;

public interface ExtendedUserService extends UserService{
	public List<UserBoundary> getAllUsers(String admainDomain, String admainEmail, int size, int page);
}
