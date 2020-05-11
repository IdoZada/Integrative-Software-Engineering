package acs.boundary.boundaryUtils;

public class Location {
	private double lat;
	private double lng;
	
	public Location() {
	}
	
	public Location(double d, double e) {
		this.lat = d;
		this.lng = e;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public double getLng() {
		return lng;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
	
}
