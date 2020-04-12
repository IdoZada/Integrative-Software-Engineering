package acs.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ElementBoundary;
import acs.logic.ElementService;


@RestController
public class ElementRelatedController {
	
	private ElementService elementService;
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
	
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary createNewElement(@RequestBody ElementBoundary newElementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail) {
			
		return this.elementService.create(managerDomain, managerEmail, newElementBoundary);
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public void updateElement(@RequestBody ElementBoundary elementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {
			this.elementService.update(managerDomain, managerEmail, elementId, elementBoundary);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public ElementBoundary getElement(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {
		return this.elementService.getSpecificElement(userDomain, userEmail, elementDomain, elementId);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	
	public List<ElementBoundary> getAllElements(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		return this.elementService.getAll(userDomain, userEmail);
	}
}
