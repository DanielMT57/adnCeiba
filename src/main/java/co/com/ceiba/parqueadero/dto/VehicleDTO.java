package co.com.ceiba.parqueadero.dto;

import java.io.Serializable;

public class VehicleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String licensePlate;
	private Integer cylinderPower;
	private Integer type;
	private String typeName;

	public VehicleDTO(String licensePlate, Integer cylinderPower, Integer type, String typeName) {
		super();
		this.licensePlate = licensePlate;
		this.cylinderPower = cylinderPower;
		this.type = type;
		this.typeName = typeName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCylinderPower() {
		return cylinderPower;
	}

	public void setCylinderPower(Integer cylinderPower) {
		this.cylinderPower = cylinderPower;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
