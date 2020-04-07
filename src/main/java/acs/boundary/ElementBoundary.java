package acs.boundary;

import java.util.Date;
import java.util.Map;

import acs.CreatedBy;
import acs.ElementID;
import acs.Location;

public class ElementBoundary {
	private ElementID elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String,Object> elementAttributes;
	
	

	public ElementBoundary() {
	}
	

	public ElementBoundary(ElementID elementId, String type, Boolean active,String name, Date createdTimestamp, CreatedBy createdBy,Location location,Map<String,Object> elementAttributes) {
		this.elementId = elementId;
		this.type = type;
		this.active = active;
		this.name = name;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttributes = elementAttributes;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	
	public Map<String, Object> getElementAttributes() {
		return elementAttributes;
	}


	public void setElementAttributes(Map<String, Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}

	
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}


	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	

}
