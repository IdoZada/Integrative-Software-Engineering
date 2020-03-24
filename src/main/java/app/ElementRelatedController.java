package app;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;

@RestController
public class ElementRelatedController {
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary createNewElement(@RequestBody ElementBoundary newElementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail) {

		return new ElementBoundary(new ElementID("2020b.daniel", "10"), "demoElement", true, new Date(),
				new CreatedBy(new UserId(managerDomain, managerEmail)), new Location(32.115139, 34.817804),
				Collections.singletonMap("key", "value"));
	}

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementBoundary elementBoundary,
			@PathVariable("managerDomain") String managerDomain, @PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementDomain") String elementDomain, @PathVariable("elementId") String elementId) {

	}
}
