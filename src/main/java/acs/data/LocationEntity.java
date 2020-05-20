package acs.data;

import javax.persistence.Embeddable;

@Embeddable
public class LocationEntity {
	private double lat;
	private double lng;
	
	public LocationEntity() {
	}
	
	public LocationEntity(double d, double e) {
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
