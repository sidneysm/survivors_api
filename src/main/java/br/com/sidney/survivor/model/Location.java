package br.com.sidney.survivor.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

@Entity
public class Location {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@ApiModelProperty(notes = "The database generated product ID")
	private Long id;
	
	@ApiModelProperty(notes = "The last location latitude")
	private double latitude;
	@ApiModelProperty(notes = "The last location longitude")
	private double longitude;
		
	public Location() {
		super();
	}

	public Location(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
}
