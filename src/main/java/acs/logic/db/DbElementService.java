package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import acs.ElementId;
import acs.boundary.ElementBoundary;
import acs.converter.ElementEntityConverter;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.logic.ElementService;

public class DbElementService implements ElementService{
	private String projectName;
	private ElementDao elementDao;
	private ElementEntityConverter elementEntityConverter;
	
	public DbElementService(ElementDao elementDao, ElementEntityConverter elementEntityConverter) {
		this.elementDao = elementDao;
		this.elementEntityConverter = elementEntityConverter;
	}

	// inject value from configuration or use default value
	@Value("${spring.application.name:2020b.daniel.zusev}") 
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@PostConstruct
	public void init() {
		// initialize object after injection
		System.err.println("project name: " + this.projectName);
	}
	
	@Override
	@Transactional
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		String key = UUID.randomUUID().toString();
		ElementEntity entity = this.elementEntityConverter.toEntity(element);
		entity.setElementId(key);
		entity.setCreatedTimestamp(new Date());
		entity.setCreatedBy(managerDomain + "@@" + managerEmail);
		return this.elementEntityConverter.fromEntity(this.elementDao.save(entity));
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		return this.elementEntityConverter.fromEntity(this.elementDao.findById(elementId)
				.orElseThrow(()->new RuntimeException("No element for id: " + elementId)));
	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDao.deleteAll();
	}

}
