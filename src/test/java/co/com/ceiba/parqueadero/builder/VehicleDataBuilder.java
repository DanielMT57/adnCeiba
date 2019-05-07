package co.com.ceiba.parqueadero.builder;

import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

public class VehicleDataBuilder {

    public static final String LICENSE_PLATE_CAR_EX = "HGS123";

    public static final String LICENSE_PLATE_CAR_EX2 = "MMM402";

    public static final String LICENSE_PLATE_MOTORCYCLE_EX = "WKL28P";
    
    public static final String LICENSE_PLATE_MOTORCYCLE_EX2 = "POR42M";

    public static final String MESSAGE_INVALID_LICENSE_PLATE = "La placa a\u00F1adida no es v\u00E1lida";

    public static final String MESSAGE_INVALID_DAY = "No puede ingresar porque no est\u00E1 en un d\u00EDa habil";

    public static final String MESSAGE_NULL_LICENSE_PLATE = "El par\u00E1metro placa es nulo o vac\u00EDo";
    
    public static final String MESSAGE_NULL_CYLINDER_POWER = "El par\u00E1metro cilindraje es nulo";

    public static final String MESSAGE_INVALID_FULL_PARKING = "No hay espacio disponible para este veh\u00EDculo";

    public static final String MESSAGE_INVALID_VEHICLE_NOT_FOUND = "Este veh\u00EDculo no se encuentra parqueado";

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

    public VehicleDTO buildDTO() {
        return new VehicleDTO(this.licensePlate, this.cylinderPower, this.type, "");
    }

    public Vehicle buildEntity() {
        return new Vehicle(this.licensePlate, this.cylinderPower, this.type);
    }

}
