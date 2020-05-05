package acs.logic.mockup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.CreatedBy;
import acs.ElementId;
import acs.UserId;
import acs.boundary.ElementBoundary;
import acs.converter.ElementEntityConverter;
import acs.data.ElementEntity;
import acs.logic.ElementService;


public class ElementServiceMockup implements ElementService {
	private String projectName;
	private Map<String, ElementEntity> elementDatabase;
	private ElementEntityConverter elementEntityConverter;
	
	@Autowired
	public ElementServiceMockup(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}
	
	// inject value from configuration or use default value
	@Value("${spring.application.name:2020b.daniel.zusev}") 
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@PostConstruct
	public void init() {
		this.elementDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		String elementId = UUID.randomUUID().toString();
		element.setElementId(new ElementId(this.projectName, elementId));
		element.setCreatedTimestamp(new Date());
		element.setCreatedBy(new CreatedBy(new UserId(managerDomain,managerEmail)));
		ElementEntity elementEntity = elementEntityConverter.toEntity(element);
		elementDatabase.put(elementEntity.getElementId(), elementEntity);
		return element;

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail,String elementDomain, String elementId, ElementBoundary update) {
		boolean dirtyFlag = false;
		if (elementDatabase.get(elementDomain + "@@" + elementId) == null) {
			System.err.println("There Is No Such Element In The System");
			return update;
		}
		ElementEntity elementEntity = elementEntityConverter.toEntity(update);
		ElementBoundary existing = elementEntityConverter.fromEntity(elementDatabase.get(elementEntity.getElementId()));
		
		
		if(update.getType() != null) {
			dirtyFlag = true;
			existing.setType(update.getType());
		}
		
		if(update.getName() != null) {
			dirtyFlag = true;
			existing.setName(update.getName());
		}
		
		if(update.getActive() != null) {
			dirtyFlag = true;
			existing.setActive(update.getActive());
		}
		
		if(update.getLocation() != null) {
			dirtyFlag = true;
			existing.setLocation(update.getLocation());
		}
		
		if(update.getElementAttributes() != null) {
			dirtyFlag = true;
			existing.setElementAttributes(update.getElementAttributes());
		}
			
		if(dirtyFlag) {
			elementDatabase.put(elementEntity.getElementId(), elementEntityConverter.toEntity(existing));
		}
			
		return existing;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		List<ElementBoundary> allElement = new ArrayList<>();
		for (ElementEntity elementEntity : elementDatabase.values()) {
			allElement.add(elementEntityConverter.fromEntity(elementEntity));
		}
		return allElement;
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		ElementEntity specificElement = elementDatabase.get(elementId);
		return elementEntityConverter.fromEntity(specificElement);
		
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		elementDatabase.clear();
	}
}
