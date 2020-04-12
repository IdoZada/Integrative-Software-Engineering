package acs.data;

import java.util.Date;
import java.util.Map;


public class ActionEntity {
	
	private String actionId;
	private String type;
	private String element;
	private Date createdTimeStamp;
	private String invokedBy;
	private Map<String, Object> actionAttributes;
	
	public ActionEntity() {
	
	}

	public ActionEntity(String actionId, String type, String element, Date createdTimeStamp, String invokedBy,Map<String, Object> actionAttributes) {
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimeStamp = createdTimeStamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public String getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	
}
