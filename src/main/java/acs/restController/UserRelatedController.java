package acs.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.NewUserDetails;
import acs.boundary.boundaryUtils.UserId;
import acs.logic.ExtendedUserService;
import acs.logic.UserService;

@RestController
public class UserRelatedController {
	
	private ExtendedUserService extendedUserService;
	
	@Autowired
	public void setUserService(ExtendedUserService extendedUserService) {
		this.extendedUserService = extendedUserService;
	}

	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary newUser (@RequestBody NewUserDetails newUserDetails) {//TODO check about what argument the method receive
		UserBoundary userBoudary = new UserBoundary(
													newUserDetails.getRole(),newUserDetails.getUsername(),
													new UserId(null,
													newUserDetails.getEmail()),
													newUserDetails.getAvatar());
		return this.extendedUserService.createUser(userBoudary);
	}
	
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary retrieveUserDetails (@PathVariable("userDomain") String userDomain,@PathVariable("userEmail") String userEmail ) {
		 return this.extendedUserService.login(userDomain, userEmail);
	}
	
	
	@RequestMapping(path = "/acs/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public void updateUserDetails (
			@RequestBody UserBoundary userBoundary
			, @PathVariable("userDomain") String userDomain
			, @PathVariable("userEmail") String userEmail) {

		this.extendedUserService.updateUser(userDomain, userEmail, userBoundary);
	}
}