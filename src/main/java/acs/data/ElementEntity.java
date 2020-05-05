package acs.data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import acs.dal.MapToJsonConverter;

@Entity
@Table(name="ELEMENTS")
public class ElementEntity {
	
	private String elementId;
	private String type;
	private String name;
	private boolean active;
	private Date createdTimestamp;
	private String createdBy;
	private String location;
	private Map<String,Object> elementAttributes;
	private ElementEntity origin;
	private Set<ElementEntity> childElements;
	
	public ElementEntity() {
		
	}

	public ElementEntity(String elementId, String type, String name, boolean active, Date createdTimestamp,
			String createdBy, String location, Map<String, Object> elementAttributes) {
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttributes = elementAttributes;
	}
	@Id
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
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

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	@Convert(converter = MapToJsonConverter.class)
	@Lob
	public Map<String, Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(Map<String, Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public ElementEntity getOrigin() {
		return origin;
	}
	
	public void setOrigin(ElementEntity origin) {
		this.origin = origin;
	}

	@OneToMany(mappedBy = "origin", fetch = FetchType.LAZY)
	public Set<ElementEntity> getChildElements() {
		return childElements;
	}
	
	public void setChildElements(Set<ElementEntity> childElements) {
		this.childElements = childElements;
	}
	
	public void addChildElement (ElementEntity childElement) {
		this.childElements.add(childElement);
		childElement.setOrigin(this);
	}
}
