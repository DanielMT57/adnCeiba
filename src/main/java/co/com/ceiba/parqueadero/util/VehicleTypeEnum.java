package co.com.ceiba.parqueadero.util;

import java.util.regex.Pattern;

public enum VehicleTypeEnum {

	CAR(2, "[A-Z]{3}\\d{3}", "Carro"), MOTORCYCLE(1, "[A-Z]{3}\\d{2}[A-Z]", "Moto");

	private VehicleTypeEnum(Integer vehicleType, String licensePlateRegex, String vehicleTypeName) {
		this.vehicleType = vehicleType;
		this.licensePlateRegex = licensePlateRegex;
		this.vehicleTypeName = vehicleTypeName;
	}

	private Integer vehicleType;
	private String licensePlateRegex;
	private String vehicleTypeName;

	public static VehicleTypeEnum getVehicleTypeFromLicense(String licensePlate) {
		for (VehicleTypeEnum vehType : values()) {
			if (Pattern.matches(vehType.licensePlateRegex, licensePlate)) {
				return vehType;
			}
		}
		return null;
	}

	public Integer getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getLicensePlateRegex() {
		return licensePlateRegex;
	}

	public void setLicensePlateRegex(String licensePlateRegex) {
		this.licensePlateRegex = licensePlateRegex;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

}
