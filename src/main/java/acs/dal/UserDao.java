package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.UserEntity;
import acs.data.UserRole;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {
	
	public List<UserEntity> findAllByUserName(
			@Param("userName") String expectUserName,
			Pageable pageable);
	
	public List<UserEntity> findAllByRole(
			@Param("role") UserRole expectRole,
			Pageable pageable);
	
	
	public List<UserEntity> findAllByAvatar(
			@Param("avatar") UserRole expectAvatar,
			Pageable pageable);

}
