package acs.logic.mockup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.ElementID;
import acs.boundary.ElementBoundary;
import acs.data.ElementEntity;
import acs.logic.ElementService;

@Service
public class ElementServiceMockup implements ElementService {
	private String projectName;
	private Map<String, ElementEntity> ElementDatabase;
	
	// inject value from configuration or use default value
	@Value("${spring.application.name:demo}") 
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@PostConstruct
	public void init() {
		// TODO make sure that this is actually the proper Map for this application
		this.ElementDatabase = new TreeMap<>();
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementId, ElementBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		List<ElementBoundary> allElement = new ArrayList<>();
		for (ElementEntity elementEntity : ElementDatabase.values()) {
			allElement.add(new ElementBoundary(
					elementEntity.getElementId(),
					elementEntity.getType(),
					elementEntity.isActive(),
					elementEntity.getName(),
					elementEntity.getTimeStamp(),
					elementEntity.getCreatedBy(),
					elementEntity.getLocation(),
					elementEntity.getAttributes()));
		}
		return allElement;
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		ElementEntity specificElement = ElementDatabase.get(userDomain+"@@"+userEmail+"@@"+elementId);
		return new ElementBoundary(specificElement.getElementId(),
				specificElement.getType(), 
				specificElement.isActive(),
				specificElement.getName(),
				specificElement.getTimeStamp(),
				specificElement.getCreatedBy(),
				specificElement.getLocation(),
				specificElement.getAttributes());
		
	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		ElementDatabase.clear();
	}
}
