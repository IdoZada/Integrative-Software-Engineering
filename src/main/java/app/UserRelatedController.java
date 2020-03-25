package app;

import org.springframework.http.MediaType;
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
}
