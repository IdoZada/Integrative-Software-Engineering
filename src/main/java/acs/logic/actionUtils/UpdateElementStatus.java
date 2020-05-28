package acs.logic.actionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.UserRole;
import acs.logic.ExtendedElementService;
import acs.logic.ExtendedUserService;

@Component
public class UpdateElementStatus {

	private static ExtendedUserService extendedUserService;
	private static ExtendedElementService extendedElementService;
	
	@Autowired
	public void setExtendedElementService(ExtendedElementService extendedElementService) {
		this.extendedElementService = extendedElementService;
	}
	
	@Autowired
	public void setUserService(ExtendedUserService extendedUserService) {
		this.extendedUserService = extendedUserService;
	}

	public static void updateFacikityStatus(ElementDao elementDao, ActionBoundary actionBoundary, String status) {
		//get element
		String elementDomain = actionBoundary.getElement().getElementId().getDomain();
		String elementId = actionBoundary.getElement().getElementId().getId();
		Optional<ElementEntity> elementEntityOptional = elementDao.findById(elementDomain+"@@"+elementId);
		ElementEntity elementEntity = elementEntityOptional.get();
		//update user role to manager
		updateUserRole(actionBoundary, UserRole.MANAGER);
		//update element status
		updateAttribute("status", status,actionBoundary,elementEntity);
		//update user role to player
		updateUserRole(actionBoundary, UserRole.PLAYER);
	}
	
	private static void updateAttribute(String key, Object value,ActionBoundary actionBoundary, ElementEntity elementEntity) {
		String elementDomain = actionBoundary.getElement().getElementId().getDomain();
		String elementId = actionBoundary.getElement().getElementId().getId();
		String userDomain = actionBoundary.getInvokedBy().getUserId().getDomain();
		String userEmail = actionBoundary.getInvokedBy().getUserId().getEmail();
		Map<String, Object> elementAttributes = new HashMap<>(elementEntity.getElementAttributes());
		elementAttributes.put(key, value);
		ElementBoundary elementBoundary = 
				new ElementBoundary(actionBoundary.getElement().getElementId(),
						null, null, null, null, null, null, elementAttributes);
		extendedElementService.update(userDomain, userEmail, elementDomain, elementId, elementBoundary);
	}
	private static void updateUserRole(ActionBoundary actionBoundary, UserRole userRole) {
		UserBoundary userBoundary = new UserBoundary(userRole, null, actionBoundary.getInvokedBy().getUserId(), null);
		String userDomain = actionBoundary.getInvokedBy().getUserId().getDomain();
		String userEmail = actionBoundary.getInvokedBy().getUserId().getEmail();
		extendedUserService.updateUser(userDomain, userEmail, userBoundary);
	}
	
	
}
