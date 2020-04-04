package acs.data;

import java.util.Date;
import java.util.Map;

import acs.CreatedBy;
import acs.ElementID;
import acs.Location;

public class ElementEntity {
	
	private ElementID elementId;
	private String type;
	private String name;
	private boolean active;
	private Date timeStamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String,Object> attributes;
	
	public ElementEntity() {
		
	}

	public ElementEntity(ElementID elementId, String type, String name, boolean active, Date timeStamp,
			CreatedBy createdBy, Location location, Map<String, Object> attributes) {
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.timeStamp = timeStamp;
		this.createdBy = createdBy;
		this.location = location;
		this.attributes = attributes;
	}

	public ElementID getElementId() {
		return elementId;
	}

	public void setElementId(ElementID elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	

}
