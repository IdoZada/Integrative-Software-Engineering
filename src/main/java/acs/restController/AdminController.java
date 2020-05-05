package acs.restController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.boundary.UserBoundary;
import acs.logic.ActionService;
import acs.logic.ExtendedElementService;
import acs.logic.UserService;

@RestController
public class AdminController {
	private ExtendedElementService extendedElementService;
	private ActionService actionService;
	private UserService userService;
	
	@Autowired
	public void setExtendedElementService(ExtendedElementService elementService) {
		this.extendedElementService = elementService;
	}
	
	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(path = "/acs/admin/users/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.userService.deleteAllUsers(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/elements/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.extendedElementService.deleteAllElements(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/actions/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.actionService.deleteAllActions(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllusers(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email){
		
		return this.userService.getAllUsers(domain, email).toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email){
		
		return this.actionService.getAllActions(domain, email).toArray(new ActionBoundary[0]);
	}
}
