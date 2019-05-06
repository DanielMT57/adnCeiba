package co.com.ceiba.parqueadero.builder;

import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

public class VehicleDataBuilder {

	public static final String LICENSE_PLATE_CAR_EX = "HGS123";

	public static final String LICENSE_PLATE_MOTORCYCLE_EX = "WKL28P";

	public static final String MESSAGE_INVALID_LICENSE_PLATE = "La placa a\u00F1adida no es v\u00E1lida";

	public static final String MESSAGE_INVALID_DAY = "No puede ingresar porque no est\u00E1 en un d\u00EDa habil";

	public static final String MESSAGE_INVALID_FULL_PARKING = "No hay espacio disponible para este veh\u00EDculo";

	private String licensePlate;
	private Integer cylinderPower;
	private Integer type;

	public VehicleDataBuilder() {
		super();
		this.licensePlate = LICENSE_PLATE_CAR_EX;
		this.cylinderPower = 1400;
		this.type = VehicleTypeEnum.CAR.getVehicleType();
	}

	public VehicleDataBuilder withLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
		return this;
	}

	public VehicleDataBuilder withCylinderPower(Integer cylinderPower) {
		this.cylinderPower = cylinderPower;
		return this;
	}

	public VehicleDTO build() {
		return new VehicleDTO(this.licensePlate, this.cylinderPower, this.type, "");
	}

}
