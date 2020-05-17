package acs.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.logic.ExtendedActionService;

@RestController
public class ActionRelatedController {
	
	private ExtendedActionService extendedActionService;
	
	@Autowired
	public ActionRelatedController(ExtendedActionService extendedActionService) {
		this.extendedActionService = extendedActionService;
	}
	
	@RequestMapping(path = "/acs/actions",
					method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
					consumes = MediaType.APPLICATION_JSON_VALUE)
	
	public Object invokeAction(@RequestBody ActionBoundary actionBoundary) {
		return this.extendedActionService.invokeAction(actionBoundary);
	}
}
