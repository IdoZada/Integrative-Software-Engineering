package acs.restController;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;

@RestController
public class ActionRelatedController {
	
	@RequestMapping(path = "/acs/action",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
					consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public ActionBoundary invokeAction(@RequestBody ActionBoundary actionBoundary) {
		return new ActionBoundary();
		
	}

}
