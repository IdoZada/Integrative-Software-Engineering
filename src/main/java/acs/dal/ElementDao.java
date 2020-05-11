package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String>{
	
	
	public List<ElementEntity> findAllByType(
			@Param("type") String expectType,
			Pageable pageable);
	
	public List<ElementEntity> findAllByTypeAndActive(
			@Param("type") String expectType,
			@Param("active") Boolean expectActive,
			Pageable pageable);
	
	public List<ElementEntity> findAllByName(
			@Param("name") String expectname,
			Pageable pageable);
	
	public List<ElementEntity> findAllByNameAndActive(
			@Param("name") String expectname,
			@Param("active") Boolean expectActive,
			Pageable pageable);
	
	public List<ElementEntity> findAllByLocation(
			@Param("location") String expectType,
			Pageable pageable);
	
	public List<ElementEntity> findAllByLocationAndActive(
			@Param("location") String expectType,
			@Param("active") Boolean expectActive,
			Pageable pageable);
	
	public List<ElementEntity> findAllByOrigin_ElementId(
			@Param("originElemetId") String expectOrigin,
			Pageable pageable);
	
	public List<ElementEntity> findAllByOrigin_ElementIdAndActive(
			@Param("originElemetId") String expectOrigin,
			@Param("active") Boolean expectActive,
			Pageable pageable);
	
	public List<ElementEntity> findAllByChildElementsLike(
			@Param("childElements") String expectChildElement,
			Pageable pageable);
	
	public List<ElementEntity> findAllByChildElementsLikeAndActive(
			@Param("childElements") String expectChildElement,
			@Param("active") Boolean expectActive,
			Pageable pageable);
	
	

}
