package acs.logic.mockup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.ActionId;
import acs.boundary.ActionBoundary;
import acs.data.ActionEntity;
import acs.logic.ActionService;

@Service
public class ActionServiceMockup implements ActionService {
	
	private String projectName;
	private Map<String, ActionEntity> actionDatabase;
	
	@Value("${spring.application.name:2020b.daniel.zusev}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@PostConstruct
	public void init() {
		this.actionDatabase = new TreeMap<>();
	}

	@Override
	public Object invokeAction(ActionBoundary action) { //action have actionId = null
		String actionId = ""; //TODO how Id is given
		ActionEntity actionInvoked = new ActionEntity(
									 new ActionId(this.projectName,actionId),
									 action.getType(),
									 action.getElementId(),
									 new Timestamp(System.currentTimeMillis()),
									 action.getInvokedBy(),
									 action.getAttributes());
		
		actionDatabase.put(actionInvoked.getActionId().getId(), actionInvoked);
		return actionInvoked;
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		//TODO check if this Admin is VALID
		List<ActionBoundary> allActions = new ArrayList<ActionBoundary>();
		for (ActionEntity actionEntity : actionDatabase.values()) {
			allActions.add(new ActionBoundary(actionEntity.getActionId(),
											  actionEntity.getType(),
											  actionEntity.getElementId(),
											  actionEntity.getTimeStamp(),
											  actionEntity.getInvokedBy(),
											  actionEntity.getAttributes()));
		}
		return allActions;
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		this.actionDatabase.clear();
		
	}
}
