package acs.restController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	public ElementBoundary[] getAllElements(
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.extendedElementService.getAll(userDomain, userEmail, size, page).toArray(new ElementBoundary[0]);
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
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {//TODO need to fix the service!
		ElementBoundary[] children = this.extendedElementService.getAllChildrenOfAnExistingElement(userDomain, userEmail, elementDomain, elementId, size, page);
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
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		ElementBoundary[] parents = this.extendedElementService.getAnArrayWithElementParent(userDomain, userEmail, elementDomain, elementId, size, page);
		if(parents != null) {
			return parents;
		}else {
			return new ElementBoundary[0];
		}
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] SearchElementByName(
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("name") String name,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.extendedElementService.getAllByName(userDomain, userEmail, name, size, page);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] SearchElementByType(
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("type") String type,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.extendedElementService.getAllByType(userDomain, userEmail, type, size, page);
	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] SearchElementByLocation(
			@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("lat") double lat,
			@PathVariable("lng") double lng,
			@PathVariable("distance") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.extendedElementService.getAllByLocation(userDomain, userEmail, lat, lng, distance, size, page);
	}
}
