package acs.converter;

import org.springframework.stereotype.Component;
import acs.CreatedBy;
import acs.ElementID;
import acs.Location;
import acs.UserId;
import acs.boundary.ElementBoundary;
import acs.data.ElementEntity;

@Component
public class ElementEntityConverter {
	public ElementBoundary fromEntity(ElementEntity elementEntity) {
		ElementID elementID;
		if(elementEntity.getElementId() != null) {
			String[] elementIdParts = elementEntity.getElementId().split("@@");
			elementID = new ElementID(elementIdParts[0], elementIdParts[1]);
		}else {
			elementID = null;
		}
		
		CreatedBy createdBy;
		if(elementEntity.getCreatedBy() != null) {
			String[] createdByParts = elementEntity.getCreatedBy().split("@@");
			createdBy = new CreatedBy();
			createdBy.setUserId(new UserId(createdByParts[0], createdByParts[1]));
		}else {
			createdBy = null;
		}
		
		Location location;
		if(elementEntity.getLocation() != null) {
			String[] locationParts = elementEntity.getLocation().split(":");
			double lat = Double.parseDouble(locationParts[0]);
			double lng = Double.parseDouble(locationParts[1]);
			location = new Location(lat, lng);
			
		}else {
			location = null;
		}
		return new ElementBoundary(
				elementID,
				elementEntity.getType(), 
				elementEntity.getActive(),
				elementEntity.getName(),
				elementEntity.getCreatedTimestamp(),
				createdBy,
				location,
				elementEntity.getElementAttributes());
	}
	
	public ElementEntity toEntity(ElementBoundary elementBoundary) {
		ElementEntity elementEntity = new ElementEntity();
		if(elementBoundary.getElementId() != null) {
			elementEntity.setElementId(elementBoundary.getElementId().getDomain() + "@@" + elementBoundary.getElementId().getId());
		}else {
			elementEntity.setElementId("@@");
		}
		
		elementEntity.setType(elementBoundary.getType());
		
		elementEntity.setName(elementBoundary.getName());
		
		if(elementBoundary.getActive() != null) {
			elementEntity.setActive(elementBoundary.getActive());
		}else {
			elementEntity.setActive(false);
		}
		elementEntity.setCreatedTimestamp(elementBoundary.getCreatedTimestamp());
		
		if(elementBoundary.getCreatedBy() != null) {
			elementEntity.setCreatedBy(elementBoundary.getCreatedBy().getUserId().getDomain() + "@@" + elementBoundary.getCreatedBy().getUserId().getEmail());
		}else {
			elementEntity.setCreatedBy("@@");
		}
		
		if(elementBoundary.getLocation() != null) {
			elementEntity.setLocation(elementBoundary.getLocation().getLat() + ":" + elementBoundary.getLocation().getLng());
		}else {
			elementEntity.setLocation(":");
		}
		
		elementEntity.setElementAttributes(elementBoundary.getElementAttributes());
		
		return elementEntity;
	}
	
}
