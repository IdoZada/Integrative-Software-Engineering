package app;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	@RequestMapping(path = "/acs/admin/users/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		return;
	}
	
	@RequestMapping(path = "/acs/admin/elements/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		return;
	}
	
	@RequestMapping(path = "/acs/admin/actions/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		return;
	}
}
