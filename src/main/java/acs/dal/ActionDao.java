package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ActionEntity;

public interface ActionDao extends PagingAndSortingRepository<ActionEntity, String> {

	public List<ActionEntity> findAllByType(
			@Param("type") String expectType,
			Pageable pageable);
	
	public List<ActionEntity> findAllByElement(
			@Param("element") String expectElement,
			Pageable pageable);
	
	public List<ActionEntity> findAllByCreatedTimeStamp(
			@Param("createdTimeStamp") String expectCreatedTimeStamp,
			Pageable pageable);
	
	public List<ActionEntity> findAllByInvokedBy(
			@Param("invokedBy") String expectInvokedBy,
			Pageable pageable);
	
}
