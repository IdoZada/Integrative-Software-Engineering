package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.ActionBoundary;
import acs.boundary.boundaryUtils.ActionId;
import acs.converter.ActionEntityConverter;
import acs.dal.ActionDao;
import acs.logic.ActionService;

@Service
public class DbActionService implements ActionService{

	private String projectName;
	private ActionDao actionDao;
	private ActionEntityConverter actionConverter;
	
	@Autowired
	public DbActionService(ActionDao actionDao, ActionEntityConverter actionConverter) {
		this.actionDao = actionDao;
		this.actionConverter = actionConverter;
	}
	
	@Value("${spring.application.name:2020b.daniel.zusev}") 
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Override
	@Transactional
	public Object invokeAction(ActionBoundary action) {
		if(action.getInvokedBy() == null)
			throw new RuntimeException("Action must be invoked by someone");
		if(action.getType() == null || action.getType().isEmpty())
			throw new RuntimeException("Action must have valid type");
		if(action.getElement().getElementId() == null){
			throw new RuntimeException("Action must have valid ElementId");
		}
		String key = UUID.randomUUID().toString();
		action.setActionId(new ActionId(this.projectName, key));
		action.setCreatedTimestamp(new Date());
		return this.actionConverter.fromEntity(this.actionDao.save(this.actionConverter.toEntity(action)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		return StreamSupport.stream(this.actionDao.findAll().spliterator(), false)
				.map(this.actionConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		actionDao.deleteAll();
	}

}
