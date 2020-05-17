package acs.restController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.boundary.UserBoundary;
import acs.logic.ExtendedActionService;
import acs.logic.ExtendedElementService;
import acs.logic.ExtendedUserService;

@RestController
public class AdminController {
	private ExtendedElementService extendedElementService;
	private ExtendedActionService extendedActionService;
	private ExtendedUserService extendedUserService;
	
	@Autowired
	public void setExtendedElementService(ExtendedElementService elementService) {
		this.extendedElementService = elementService;
	}
	
	@Autowired
	public void setActionService(ExtendedActionService extendedActionService) {
		this.extendedActionService = extendedActionService;
	}
	
	@Autowired
	public void setExtendedUserService(ExtendedUserService userService) {
		this.extendedUserService = userService;
	}
	
	@RequestMapping(path = "/acs/admin/users/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.extendedUserService.deleteAllUsers(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/elements/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.extendedElementService.deleteAllElements(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/actions/{domain}/{email}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable("domain") String domain, @PathVariable("email") String email) {
		this.extendedActionService.deleteAllActions(domain, email);
	}
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllusers(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page){
		
		return this.extendedUserService.getAllUsers(domain, email, size, page).toArray(new UserBoundary[0]);
	}
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page){
		
		return this.extendedActionService.getAllActions(domain, email, size, page).toArray(new ActionBoundary[0]);
	}
}
