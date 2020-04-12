package acs.boundary;

import java.util.Date;
import java.util.Map;

import acs.ActionId;
import acs.Element;
import acs.InvokedBy;

public class ActionBoundary {
	
	private ActionId actionId;
	private String type;
	private Element element;
	private Date createdTimeStamp;
	private InvokedBy invokedBy;
	private Map<String, Object> actionAttributes;

	public ActionBoundary() {
		
	}

	public ActionBoundary(ActionId actionId, String type, Element element, Date createdTimeStamp, InvokedBy invokedBy,Map<String, Object> actionAttributes) {
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimeStamp = createdTimeStamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public ActionId getActionId() {
		return actionId;
	}

	public void setActionId(ActionId actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	
}