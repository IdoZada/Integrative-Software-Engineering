package acs.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.dal.MapToJsonConverter;

@Entity
@Table(name="ACTIONS")
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
	@Id
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
	@Temporal(TemporalType.TIMESTAMP)
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
	@Convert(converter = MapToJsonConverter.class)
	@Lob
	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	
}
