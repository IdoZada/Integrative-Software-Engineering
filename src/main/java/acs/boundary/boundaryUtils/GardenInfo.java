package acs.boundary.boundaryUtils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Lob;

import acs.dal.GardenMapToJsonConverter;
import acs.data.FacilityType;

public class GardenInfo {
	
	private double rating;
	private int capacity;
	private Map<FacilityType,String> facilityTypes;
	private int numOfRatedBy;
	
	
	public GardenInfo() {
		this.facilityTypes = new HashMap<>();
	}
	
	public GardenInfo(double rating, int capacity, Map<FacilityType, String> facilityTypes, int numOfRatedBy) {
		this.rating = rating;
		this.capacity = capacity;
		this.facilityTypes = facilityTypes;
		this.numOfRatedBy = numOfRatedBy;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	@Convert(converter = GardenMapToJsonConverter.class)
	@Lob
	public Map<FacilityType, String> getFacilityTypes() {
		return facilityTypes;
	}
	public void setFacilityTypes(Map<FacilityType, String> facilityTypes) {
		this.facilityTypes = facilityTypes;
	}
	public int getNumOfRatedBy() {
		return numOfRatedBy;
	}
	public void setNumOfRatedBy(int numOfRatedBy) {
		this.numOfRatedBy = numOfRatedBy;
	}
	

}
