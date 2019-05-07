package co.com.ceiba.parqueadero.dto;

import java.time.LocalDateTime;

public class ParkedVehicleDTO {

    private String licensePlate;
    private String vehicleType;
    private LocalDateTime checkinDate;

    public ParkedVehicleDTO() {
        super();
    }

    public ParkedVehicleDTO(String licensePlate, String vehicleType, LocalDateTime checkinDate) {
        super();
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.checkinDate = checkinDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDateTime checkinDate) {
        this.checkinDate = checkinDate;
    }
}
