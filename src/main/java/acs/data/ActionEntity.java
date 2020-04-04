package acs.data;

import java.util.Date;
import java.util.Map;

import acs.ActionId;
import acs.ElementID;
import acs.UserId;

public class ActionEntity {
	
	private ActionId actionId;
	private String Type;
	private ElementID elementId;
	private Date timeStamp;
	private UserId invokedBy;
	private Map<String, Object> attributes;
	
	public ActionEntity() {
	
	}

	public ActionEntity(ActionId actionId, String type, ElementID elementId, Date timeStamp, UserId invokedBy,Map<String, Object> attributes) {
		this.actionId = actionId;
		this.Type = type;
		this.elementId = elementId;
		this.timeStamp = timeStamp;
		this.invokedBy = invokedBy;
		this.attributes = attributes;
	}

	public ActionId getActionId() {
		return actionId;
	}

	public void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public ElementID getElementId() {
		return elementId;
	}

	public void setElementId(ElementID elementId) {
		this.elementId = elementId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public UserId getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserId invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
