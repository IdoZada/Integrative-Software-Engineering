package acs.logic.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.UserId;
import acs.boundary.boundaryUtils.ValidEmail;
import acs.converter.UserEntityConverter;
import acs.dal.UserDao;
import acs.data.UserEntity;
import acs.data.UserRole;
import acs.logic.ExtendedUserService;
import acs.logic.UnauthorizedException;

@Service
public class DbUserService implements ExtendedUserService{
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
		if(!ValidEmail.isValid(user.getUserId().getEmail()))
			throw new RuntimeException("user Email Not Valid");
		if(!user.getRole().equals(UserRole.PLAYER) && !user.getRole().equals(UserRole.MANAGER) && !user.getRole().equals(UserRole.ADMIN))
			throw new RuntimeException("User Role Is Not Valid");
		if(user.getUsername() == null || user.getUsername().isEmpty())
			throw new RuntimeException("User Name Can Not Be Null");
		if(user.getAvatar() == null || user.getAvatar().trim().isEmpty())
			throw new RuntimeException("User Avatar Can Not Be Null Or Empty");
		if(userDao.findById(user.getUserId().getDomain()+"@@"+user.getUserId().getEmail()).isPresent())
			throw new RuntimeException("User Email Is Already Exist");
		
		user.setUserId(new UserId(this.projectName, user.getUserId().getEmail()));
		UserEntity userEntity = this.userEntityConverter.toEntity(user);
		return this.userEntityConverter.fromEntity(this.userDao.save(userEntity));	
	}

	@Override
	@Transactional(readOnly = true)
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
		if (update.getAvatar() != null && !update.getAvatar().trim().isEmpty()) {
			dirtyFlag = true;
			existing.setAvatar(update.getAvatar());
		}

		if (update.getUsername() != null) {
			dirtyFlag = true;
			existing.setUsername(update.getUsername());
		}
		
		if (update.getRole() != null) {
			if(update.getRole().equals(UserRole.PLAYER) || update.getRole().equals(UserRole.MANAGER) || update.getRole().equals(UserRole.ADMIN)) {
				dirtyFlag = true;
				existing.setRole(update.getRole());
			}
		}

		if (dirtyFlag)
			this.userDao.save(userEntityConverter.toEntity(existing));
		return existing;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) { 
		if(userDao.findById(adminDomain+"@@"+adminDomain).get().getRole().equals(UserRole.ADMIN)) {
			return StreamSupport
					.stream(this.userDao
					.findAll()
					.spliterator(),false) 
					.map(this.userEntityConverter::fromEntity) // Stream<UserBoundary>
					.collect(Collectors.toList()); // List<UserBoundary>
		}else {
			throw new UnauthorizedException("Only Admin Can Get All Users");
		}
	}
	

	@Override
	@Transactional
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		if(userDao.findById(adminDomain+"@@"+adminDomain).get().getRole().equals(UserRole.ADMIN)) {
			this.userDao.deleteAll();
		}else {
			throw new UnauthorizedException("Only Admin Can Delete All Users");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail, int size, int page) {
		if(userDao.findById(adminDomain+"@@"+adminDomain).get().getRole().equals(UserRole.ADMIN)) {
			return StreamSupport
					.stream(this.userDao
					.findAll(PageRequest.of(page, size, Direction.ASC, "userId"))
					.spliterator(),false) //Stream<UserEntity>
					.map(this.userEntityConverter::fromEntity) // Stream<UserBoundary>
					.collect(Collectors.toList()); // List<UserBoundary>
		}else {
			throw new UnauthorizedException("Only Admin Can Get All Users");
		}
	}

}
