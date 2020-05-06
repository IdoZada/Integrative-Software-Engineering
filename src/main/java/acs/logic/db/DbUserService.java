package acs.logic.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.UserId;
import acs.boundary.UserBoundary;
import acs.converter.UserEntityConverter;
import acs.dal.UserDao;
import acs.data.UserEntity;
import acs.logic.UserService;

@Service
public class DbUserService implements UserService{
	private String projectName;
	private UserDao userDao;
	private UserEntityConverter userEntityConverter;
	
	@Autowired
	public DbUserService(UserDao userDao, UserEntityConverter userEntityConverter) {
		this.userDao = userDao;
		this.userEntityConverter = userEntityConverter;
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
	public UserBoundary createUser(UserBoundary user) {
		if(user.getUserId() == null)
			throw new RuntimeException("user id must not be null");
		if(user.getUserId().getEmail() == null || user.getUserId().getEmail().trim().isEmpty())
			throw new RuntimeException("user Email must not be null or empty");
		
		user.setUserId(new UserId(this.projectName, user.getUserId().getEmail()));
		UserEntity userEntity = this.userEntityConverter.toEntity(user);
		return this.userEntityConverter.fromEntity(this.userDao.save(userEntity));	
	}

	@Override
	@Transactional //(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		Optional<UserEntity> optionalUserEntity = this.userDao.findById(userDomain + "@@" + userEmail);
		if(!optionalUserEntity.isPresent())
			throw new RuntimeException("no user exist for this user Email: " + userEmail+" and user domain: " + userDomain);
		UserEntity userEntity = optionalUserEntity.get();
		UserBoundary userBoundary = this.userEntityConverter.fromEntity(userEntity);
		return userBoundary;
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		boolean dirtyFlag = false;
		Optional<UserEntity> optionalUserEntity = this.userDao.findById(userDomain + "@@" + userEmail);
		if(!optionalUserEntity.isPresent())
			throw new RuntimeException("no user exist for this user Email: " + userEmail+" and user domain: " + userDomain);
		UserBoundary existing = userEntityConverter.fromEntity(optionalUserEntity.get());
		if (update.getAvatar() != null) {
			dirtyFlag = true;
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUserName() != null) {
			dirtyFlag = true;
			existing.setUserName(update.getUserName());
		}
		
		if (update.getRole() != null) {
			dirtyFlag = true;
			existing.setRole(update.getRole());
		}

		if (dirtyFlag)
			this.userDao.save(userEntityConverter.toEntity(existing));
		return existing;
	}

	@Override
	@Transactional
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return StreamSupport
				.stream(
//						// INVOKE SELECT DATABASE 
						this.userDao
							.findAll()
							.spliterator(),
							
						false) //Stream<UserEntity>
				.map(this.userEntityConverter::fromEntity) // Stream<UserBoundary>
				.collect(Collectors.toList()); // List<UserBoundary>
	}
	

	@Override
	@Transactional
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		this.userDao.deleteAll();
	}

}
