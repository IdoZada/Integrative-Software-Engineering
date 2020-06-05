package acs.logic.actionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.GardenInfo;
import acs.boundary.boundaryUtils.InfoFacility;
import acs.converter.AttributeConverter;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.FacilityStatus;
import acs.data.UserRole;
import acs.logic.ExtendedElementService;
import acs.logic.ExtendedUserService;
import acs.logic.NotFoundException;

@Component
public class ClientActions {

	private static ExtendedUserService extendedUserService;
	private static ExtendedElementService extendedElementService;
	private static AttributeConverter attributeConverter;
	private static ElementDao elementDao;
	
	@Autowired
	public void setExtendedElementService(ExtendedElementService extendedElementService) {
		ClientActions.extendedElementService = extendedElementService;
	}
	
	@Autowired
	public void setUserService(ExtendedUserService extendedUserService) {
		ClientActions.extendedUserService = extendedUserService;
	}
	@Autowired
	public void setElementDao(AttributeConverter attributeConverter) {
		ClientActions.attributeConverter = attributeConverter;
	}
	
	@Autowired
	public void setAttributeConverter(ElementDao elementDao) {
		ClientActions.elementDao = elementDao;
	}

	private static void updateFacilityStatus(ActionBoundary actionBoundary) {
		//get element
		ElementEntity elementEntity = getCurrentElement(elementDao,actionBoundary);
		//update user role to manager
		updateUserRole(actionBoundary, UserRole.MANAGER);
		//update element status
		FacilityStatus facilityStatus = attributeConverter.toAttribute(actionBoundary.getActionAttributes().get("status"), FacilityStatus.class);
		updateAttribute("Info", facilityStatus,actionBoundary,elementEntity);
		//update user role to player
		updateUserRole(actionBoundary, UserRole.PLAYER);
	}
	
	private static void updateAttribute(String key, FacilityStatus value,ActionBoundary actionBoundary, ElementEntity elementEntity) {
		String elementDomain = actionBoundary.getElement().getElementId().getDomain();
		String elementId = actionBoundary.getElement().getElementId().getId();
		String userDomain = actionBoundary.getInvokedBy().getUserId().getDomain();
		String userEmail = actionBoundary.getInvokedBy().getUserId().getEmail();
		Map<String, Object> elementAttributes = new HashMap<>(elementEntity.getElementAttributes());
		InfoFacility info = attributeConverter.toAttribute(elementAttributes.get(key), InfoFacility.class);
		info.setStatus(value);
		elementAttributes.put("key", info);
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
	
	public static Object actionInvoker(ActionBoundary actionBoundary) {
		String type = actionBoundary.getType();
		Map<String, Object> map = new HashMap<>();
		switch(type) {
		case "update_facility_status":{updateFacilityStatus(actionBoundary);}
		break;
		case "update_rating":{updateGardenRating(actionBoundary);}
		break;
		case "get_capacity":{ map.put("capcity",getCapacityOfGarden(actionBoundary));}
		break;
		case "get_info_facility":{map.put("info_facility", getInfoFacility(actionBoundary));}
		break;
		case "get_info_garden":{map.put("info_garden", getInfoGarden(actionBoundary));}
		break;
		default:{
			throw new NotFoundException("action type is not valid");
		}
		
		}
		
		return map;
		
	}
	
	private static void updateGardenRating(ActionBoundary actionBoundary) {
		int rating = (int) actionBoundary.getActionAttributes().get("rating");
		
		//get element
				
				ElementEntity elementEntity = getCurrentElement(elementDao,actionBoundary);
				
				GardenInfo gardenInfo = attributeConverter.toAttribute(elementEntity.getElementAttributes().get("Info"), GardenInfo.class);

				
				double elementRating = gardenInfo.getRating();
				int elementNumRated = gardenInfo.getNumOfRatedBy();
				
				double sumOfRating = (elementRating * elementNumRated) + rating;
				elementNumRated++;
				
				elementRating = sumOfRating/elementNumRated;
				
				updateUserRole(actionBoundary, UserRole.MANAGER);
				
				gardenInfo.setRating(elementRating);
				gardenInfo.setNumOfRatedBy(elementNumRated);
				
				elementEntity.getElementAttributes().put("Info", gardenInfo);
				
				elementDao.save(elementEntity);
				updateUserRole(actionBoundary, UserRole.PLAYER);
	}
	
	private static Integer getCapacityOfGarden(ActionBoundary actionBoundary) {
		//get element
		ElementEntity elementEntity = getCurrentElement(elementDao,actionBoundary);
		GardenInfo gardenInfo = attributeConverter.toAttribute(elementEntity.getElementAttributes().get("Info"), GardenInfo.class);
		return gardenInfo.getCapacity();
		
	}
	
	private static InfoFacility getInfoFacility(ActionBoundary actionBoundary) {
		ElementEntity elementEntity = getCurrentElement(elementDao,actionBoundary);
		InfoFacility infoFacility = attributeConverter.toAttribute(elementEntity.getElementAttributes().get("Info"), InfoFacility.class);
		return infoFacility;
		
	}
	
	private static GardenInfo getInfoGarden(ActionBoundary actionBoundary) {
		ElementEntity elementEntity = getCurrentElement(elementDao,actionBoundary);
		GardenInfo gardenInfo = attributeConverter.toAttribute(elementEntity.getElementAttributes().get("Info"), GardenInfo.class);
		return gardenInfo;
		
	}
	
	private static ElementEntity getCurrentElement(ElementDao elementDao,ActionBoundary actionBoundary) {
		String elementDomain = actionBoundary.getElement().getElementId().getDomain();
		String elementId = actionBoundary.getElement().getElementId().getId();
		Optional<ElementEntity> elementEntityOptional = elementDao.findById(elementDomain+"@@"+elementId);
		return elementEntityOptional.get();
	}
	
	
}
