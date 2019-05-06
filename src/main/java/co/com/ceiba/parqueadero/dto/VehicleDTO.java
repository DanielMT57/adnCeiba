package co.com.ceiba.parqueadero.dto;

import java.io.Serializable;

public class VehicleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String licensePlate;
	private Integer cylinderPower;
	private Integer type;

	public VehicleDTO(String licensePlate, Integer cylinderPower, Integer type) {
		super();
		this.licensePlate = licensePlate;
		this.cylinderPower = cylinderPower;
		this.type = type;
	}

	public VehicleDTO() {
		super();
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
