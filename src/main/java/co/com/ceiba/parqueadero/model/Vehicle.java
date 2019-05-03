package co.com.ceiba.parqueadero.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "license_plate")
	private String licensePlate;

	@Column(name = "cylinder_power")
	private Integer cylinderPower;

	@Column(name = "type", nullable = false)
	private Integer type;

	public Vehicle() {
		super();
	}

	public Vehicle(String licensePlate, Integer cylinderPower, Integer type) {
		super();
		this.licensePlate = licensePlate;
		this.cylinderPower = cylinderPower;
		this.type = type;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getCylinderPower() {
		return cylinderPower;
	}

	public void setCylinderPower(Integer cylinderPower) {
		this.cylinderPower = cylinderPower;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
