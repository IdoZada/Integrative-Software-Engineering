package app;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRelatedController {

	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary newUser (@RequestBody NewUserDetails newUserDetails) {
		
		return new UserBoundary (newUserDetails.getRole(), newUserDetails.getUserName(),
				new UserId(newUserDetails.getEmail(), "2020b"));
	}
	
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public UserBoundary retrieveUserDetails (
			@PathVariable("userDomain") String userDomain
			,@PathVariable("userEmail") String userEmail ) {
		UserBoundary userBoundary = new UserBoundary(
				Role.PLAYER, "Tomer"
				, new UserId("tomer@gmail.com", "2020t"));
		
		if(userDomain.equals(userBoundary.getUserId().getDomain())
				&& userEmail.equals(userBoundary.getUserId().getEmail())) {
			
			return userBoundary;
			
		}else {
			
			return new UserBoundary();
			
		}
		
	}
	
	
	@RequestMapping(path = "/acs/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public void updateUserDetails (
			@RequestBody UserBoundary userBoundary
			, @PathVariable("userDomain") String userDomain
			, @PathVariable("userEmail") String userEmail) {
		
		 UserBoundary updateUsBoundary = new UserBoundary (
				userBoundary.getRole()
				, userBoundary.getUserName(),
				new UserId(userBoundary.getUserId().getEmail(), userBoundary.getUserId().getDomain()));
		 
		 UserId userId = new UserId(userEmail, userDomain);
		 updateUsBoundary.setUserId(userId);
	}
	
}
