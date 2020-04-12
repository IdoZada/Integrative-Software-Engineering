package acs.logic.mockup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.boundary.ElementBoundary;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;
import acs.logic.ElementService;

@Service
public class ElementServiceMockup implements ElementService {
	private String projectName;
	private Map<String, ElementEntity> ElementDatabase;
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
		this.ElementDatabase = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		ElementEntity elementEntity = elementEntityConverter.toEntity(element);
		ElementDatabase.put(elementEntity.getElementId(), elementEntity);
		return element;

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementId, ElementBoundary update) {
		ElementEntity elementEntity = elementEntityConverter.toEntity(update);
		ElementDatabase.put(elementEntity.getElementId(), elementEntity);
		return update;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		List<ElementBoundary> allElement = new ArrayList<>();
		for (ElementEntity elementEntity : ElementDatabase.values()) {
			allElement.add(elementEntityConverter.fromEntity(elementEntity));
		}
		return allElement;
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		ElementEntity specificElement = ElementDatabase.get(elementId);
		return elementEntityConverter.fromEntity(specificElement);
		
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		ElementDatabase.clear();
	}
}
