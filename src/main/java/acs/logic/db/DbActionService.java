package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.ActionBoundary;
import acs.boundary.boundaryUtils.ActionId;
import acs.converter.ActionEntityConverter;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.data.UserRole;
import acs.logic.ExtendedActionService;
import acs.logic.NotFoundException;
import acs.logic.UnauthorizedException;
import acs.logic.actionUtils.ClientActions;

@Service
public class DbActionService implements ExtendedActionService{

	private String projectName;
	private ActionDao actionDao;
	private ElementDao elementDao;
	private UserDao userDao;
	private ActionEntityConverter actionConverter;
	
	@Autowired
	public DbActionService(ActionDao actionDao, ElementDao elementDao, UserDao userDao, ActionEntityConverter actionConverter) {
		this.actionDao = actionDao;
		this.elementDao = elementDao;
		this.userDao = userDao;
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
		String userId = action.getInvokedBy().getUserId().getDomain() + "@@" +
				action.getInvokedBy().getUserId().getEmail();
		UserEntity invoker = this.userDao.findById(userId)
				.orElseThrow(()->new NotFoundException("User does not exist"));
		if(invoker.getRole() == UserRole.MANAGER) {
			throw new UnauthorizedException(invoker.getRole().toString() + "'s cant invoke actions!");
		}
		String elementId = action.getElement().getElementId().getDomain() + 
				"@@" + action.getElement().getElementId().getId();
		
		ElementEntity element = this.elementDao.findById(elementId)
				.orElseThrow(()-> new NotFoundException("Element not found"));
		if(!element.getActive() && invoker.getRole() == UserRole.PLAYER)
				throw new RuntimeException("Element must be active");
		
		String key = UUID.randomUUID().toString();
		action.setActionId(new ActionId(this.projectName, key));
		action.setCreatedTimestamp(new Date());
		this.actionDao.save(this.actionConverter.toEntity(action));
		return ClientActions.actionInvoker(action);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		String adminId = adminDomain + "@@" + adminEmail;
		UserEntity invoker = this.userDao.findById(adminId)
				.orElseThrow(()->new NotFoundException("Admin does not exist"));
		if(invoker.getRole() == UserRole.PLAYER) {
			throw new UnauthorizedException(invoker.getRole().toString() + "'s not allowed here");
		}
		return StreamSupport.stream(this.actionDao.findAll().spliterator(), false)
				.map(this.actionConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllActions(String adminDomain, String adminEmail) {
		String adminId = adminDomain + "@@" + adminEmail;
		UserEntity invoker = this.userDao.findById(adminId)
				.orElseThrow(()->new NotFoundException("Admin does not exist"));
		if(invoker.getRole() != UserRole.ADMIN) {
			throw new UnauthorizedException(invoker.getRole().toString() + "'s not allowed here");
		}
		actionDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {
		if(userDao.findById(adminDomain+"@@"+adminEmail).get().getRole().equals(UserRole.ADMIN)) {
			return StreamSupport
					.stream(this.actionDao
					.findAll(PageRequest.of(page, size, Direction.ASC,"createdTimeStamp","ActionId"))
					.spliterator(),false)
					.map(this.actionConverter::fromEntity)
					.collect(Collectors.toList());
		}else {
			throw new UnauthorizedException("Only Admin Can Get All Actions");
		}
		
	}

}
