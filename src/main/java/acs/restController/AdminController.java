package acs.restController;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.boundary.UserBoundary;

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
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<UserBoundary> exportAllusers(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email){
		
		return new ArrayList<UserBoundary>();
	}
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<ActionBoundary> exportAllActions(@PathVariable("adminDomain") String domain, @PathVariable("adminEmail") String email){
		
		return new ArrayList<ActionBoundary>();
	}
}
