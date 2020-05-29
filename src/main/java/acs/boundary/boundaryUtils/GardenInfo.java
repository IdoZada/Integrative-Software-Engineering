package acs.boundary.boundaryUtils;

import java.util.Map;

import acs.data.FacilityType;

public class GardenInfo {
	
	private int rating;
	private int capacity;
	private Map<FacilityType,String> facilityTypes;
	
	
	public GardenInfo() {

	}

	
	public GardenInfo(int rating, int capacity, Map<FacilityType, String> facilityTypes) {
		this.rating = rating;
		this.capacity = capacity;
		this.facilityTypes = facilityTypes;
	}


	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public Map<FacilityType, String> getFacilityTypes() {
		return facilityTypes;
	}


	public void setFacilityTypes(Map<FacilityType, String> facilityTypes) {
		this.facilityTypes = facilityTypes;
	}
	
	

}
