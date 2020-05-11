package acs.restController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;
import acs.logic.ExtendedElementService;


@RestController
public class ElementRelatedController {
	
	private ExtendedElementService extendedElementService;
	
	@Autowired
	public void setExtendedElementService(ExtendedElementService extendedElementService) {
		this.extendedElementService = extendedElementService;
	}
	
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary createNewElement(@RequestBody ElementBoundary newElementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail) {
			
		return this.extendedElementService.create(managerDomain, managerEmail, newElementBoundary);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public void updateElement(@RequestBody ElementBoundary elementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {
			this.extendedElementService.update(managerDomain, managerEmail,elementDomain, elementId, elementBoundary);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary getElement(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {
		return this.extendedElementService.getSpecificElement(userDomain, userEmail, elementDomain, elementId);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary[] getAllElements(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		return this.extendedElementService.getAll(userDomain, userEmail).toArray(new ElementBoundary[0]);
	}
	
	@RequestMapping( path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void bindExistingElementToAnExistingChildElement(@RequestBody ElementIdBoundary elementIdBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {//TODO path variable
		this.extendedElementService.bindExistingElementToAnExistingChildElement(managerDomain, managerEmail, elementDomain, elementId, elementIdBoundary);
	}
	
	@RequestMapping( path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllChildrenOfAnExistingElement(
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {//TODO need to fix the service!
		ElementBoundary[] children = this.extendedElementService.getAllChildrenOfAnExistingElement(userDomain, userEmail, elementDomain, elementId);
		if(children != null) {
			return children;
		}else {
			return new ElementBoundary[0];
		}
	}
 	
	@RequestMapping( path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAnArrayWithElementParent(
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {
		ElementBoundary[] parents = this.extendedElementService.getAnArrayWithElementParent(userDomain, userEmail, elementDomain, elementId);
		if(parents != null) {
			return parents;
		}else {
			return new ElementBoundary[0];
		}
	}
	
}
