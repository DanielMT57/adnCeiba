package co.com.ceiba.parqueadero.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParkedVehicleDTO {

    private String licensePlate;
    private String vehicleType;
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
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
