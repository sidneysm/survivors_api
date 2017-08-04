package br.com.sidney.survivor.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;



@Entity
public class Survivor {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private int age;
	private String gender;
	@OneToOne
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	private Location lastLocation;
	@OneToOne
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	private Inventory inventory;
	private boolean isInfected = false;
	
	public Survivor() {
		super();
	}

	public Survivor(String name, int age, String gender, Location lastLocation, Inventory inventory, boolean isInfected) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.lastLocation = lastLocation;
		this.inventory = inventory;
		this.isInfected = isInfected;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Location getLastLocation() {
		return lastLocation;
	}
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public boolean isInfected() {
		return isInfected;
	}

	public void setInfected(boolean isInfected) {
		this.isInfected = isInfected;
	}

	@Override
	public String toString() {
		return "Survivor [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", lastLocation="
				+ lastLocation + ", inventory=" + inventory + ", isInfected=" + isInfected + "]";
	}

}
