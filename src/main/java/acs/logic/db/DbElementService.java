package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;
import acs.converter.ElementEntityConverter;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.logic.ExtendedElementService;
import acs.logic.IdNotFoundException;

@Service
public class DbElementService implements ExtendedElementService{
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
		if(element.getName() == null) {
			throw new RuntimeException("Element Name Can Not Be Null");
		}
		if(element.getType() == null) {
			throw new RuntimeException("Element Type Can Not Be Null");
		}
		ElementEntity entity = this.elementEntityConverter.toEntity(element);
		entity.setElementId(this.projectName + "@@" + key);
		entity.setCreatedTimestamp(new Date());
		entity.setCreatedBy(managerDomain + "@@" + managerEmail);
		return this.elementEntityConverter.fromEntity(this.elementDao.save(entity));
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,ElementBoundary update) {
		
		ElementBoundary existing = this.getSpecificElement(managerDomain, managerEmail, elementDomain, elementId);
		
		if(update.getType() != null) {
			existing.setType(update.getType());
		}
		
		if(update.getName() != null) {
			existing.setName(update.getName());
		}
		
		if(update.getActive() != null) {
			existing.setActive(update.getActive());
		}
		
		if(update.getLocation() != null) {
			existing.setLocation(update.getLocation());
		}
		
		if(update.getElementAttributes() != null) {
			existing.setElementAttributes(update.getElementAttributes());
		}
		
		this.elementDao.save(this.elementEntityConverter.toEntity(existing));
		return existing;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		
		return StreamSupport.stream(
				this.elementDao.findAll().spliterator(), false)
				.map(this.elementEntityConverter::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		return this.elementEntityConverter.fromEntity(this.elementDao.findById(elementDomain + "@@" + elementId)
				.orElseThrow(()->new RuntimeException("No element for id: " + elementId)));
	}

	@Override
	@Transactional
	public void deleteAllElements(String adminDomain, String adminEmail) {
		this.elementDao.deleteAll();
	}

	@Override
	@Transactional
	public void bindExistingElementToAnExistingChildElement(String managerDomain,String managerEmail,String originElementDomain, String originElementId, ElementIdBoundary elementIdBoundary) {
		if (elementIdBoundary.getId() == null) {
			throw new IdNotFoundException("No Such ID In Database");
		}
		
		ElementEntity origin = this.elementDao.findById(originElementDomain + "@@" + originElementId)
								.orElseThrow(() -> new IdNotFoundException("No Element For Id: " + originElementId));
		
		ElementEntity child = this.elementDao.findById(elementIdBoundary.getDomain() + "@@" + elementIdBoundary.getId())
							.orElseThrow(() -> new IdNotFoundException("No Element For Id: " + elementIdBoundary.getId()));
		
		origin.addChildElement(child);
		this.elementDao.save(origin);
		
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userDomain,String userEmail,String originElementDomain,String originElementId) {
		
		ElementEntity origin = this.elementDao.findById(originElementDomain + "@@" + originElementId)
				.orElseThrow(() -> new IdNotFoundException("No Element For Id: " + originElementId));
		
		return origin
					.getChildElements()
					.stream()
					.map(this.elementEntityConverter :: fromEntity)
					.collect(Collectors.toList())
					.toArray(new ElementBoundary[0]);

	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAnArrayWithElementParent(String userDomain,String userEmail,String originElementDomain,String originElementId) {
		// TODO If we make a "many to many" relationship
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page) {
		return elementDao
				.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp","elementId"))
				.getContent()
				.stream()
				.map(this.elementEntityConverter :: fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userDomain, String userEmail,
			String elementDomain, String elementId, int size, int page) {
		
		return elementDao
				.findAllByChildElementsLikeAndActive(elementId, true, PageRequest.of(page, size, Direction.ASC, "createdTimestamp","elementId"))
				.stream()
				.map(this.elementEntityConverter :: fromEntity)
				.collect(Collectors.toList()).toArray(new ElementBoundary[0]);
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAnArrayWithElementParent(String userDomain, String userEmail, String elementDomain,
			String elementId, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllByName(String userDomain, String userEmail, String name, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllByType(String userDomain, String userEmail, String type, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary[] getAllByLocation(String userDomain, String userEmail, double lat, double lng,
			double distance, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
