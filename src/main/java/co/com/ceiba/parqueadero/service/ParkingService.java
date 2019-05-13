package co.com.ceiba.parqueadero.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.com.ceiba.parqueadero.domain.VehicleTypeEnum;
import co.com.ceiba.parqueadero.dto.ParkedVehicleDTO;
import co.com.ceiba.parqueadero.dto.ParkingDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.model.Parking;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.persistence.IParkingPersistence;
import co.com.ceiba.parqueadero.persistence.IVehiclePersistence;
import co.com.ceiba.parqueadero.util.Constants;
import co.com.ceiba.parqueadero.util.Mapper;
import co.com.ceiba.parqueadero.util.Validation;

@Component
public class ParkingService {

    private Clock clock;

    @Autowired
    private IParkingPersistence parkingPersistence;

    @Autowired
    private IVehiclePersistence vehiclePersistence;
    
    @Autowired
    private Mapper mapper;

    @Value("${parking.maxSlotsAvailable.cars}")
    private short maxSlotsAvailableCars;

    @Value("${parking.maxSlotsAvailable.motorcycles}")
    private short maxSlotsAvailableMotorcycles;

    @Value("${parking.fare.car.hour}")
    private int carFarePerHour;

    @Value("${parking.fare.car.day}")
    private int carFarePerDay;

    @Value("${parking.fare.motorcycle.hour}")
    private int motorcycleFarePerHour;

    @Value("${parking.fare.motorcycle.day}")
    private int motorcycleFarePerDay;

    @Value("${parking.fare.motorcycle.overfare}")
    private int motorcycleOverFare;

    @Value("${parking.fare.motorcycle.overfaceCylinderPower}")
    private int motorcycleOverFareCylinderPower;

    @Value("${parking.trm.url}")
    private String trmServiceUrl;

    public ParkingDTO createParking(VehicleDTO vehicleDTO) {
        clock = Clock.systemUTC();
        LocalDateTime inDatetime = LocalDateTime.now(clock);
        VehicleTypeEnum vehicleType = VehicleTypeEnum.getVehicleTypeFromLicense(vehicleDTO.getLicensePlate());
        validateVehicleToCheckIn(vehicleDTO, vehicleType, inDatetime);
        Parking parking = new Parking(inDatetime, getVehicle(vehicleDTO, vehicleType));
        parking = parkingPersistence.save(parking);
        return mapper.map(parking, ParkingDTO.class);
    }

    public void validateVehicleToCheckIn(VehicleDTO vehicleDTO, VehicleTypeEnum vehicleType, LocalDateTime inDatetime) {
        String licensePlate = vehicleDTO.getLicensePlate();
        // parameter validation
        Validation.checkNullOrEmpty(licensePlate, "placa");
        Validation.checkNull(vehicleDTO.getCylinderPower(), "cilindraje");
        if (null == vehicleType) {
            throw new ParkingException(Constants.ERROR_INVALID_LICENSE_PLATE);
        }

        if (parkingPersistence.findVehicleByPlateAndStatusActive(licensePlate) != null) {
            throw new ParkingException(Constants.ERROR_VEHICLE_ALREADY_PARKED);
        }

        short slotsAvailable = vehicleType.equals(VehicleTypeEnum.CAR) ? maxSlotsAvailableCars : maxSlotsAvailableMotorcycles;
        if (parkingPersistence.countParkedVehiclesByType(vehicleType.getVehicleTypeName()) >= slotsAvailable) {
            throw new ParkingException(Constants.ERROR_NO_SPACE_AVAILABLE);
        }

        if (licensePlate.startsWith("A")
                && (!inDatetime.getDayOfWeek().equals(DayOfWeek.SUNDAY) || !inDatetime.getDayOfWeek().equals(DayOfWeek.MONDAY))) {
            throw new ParkingException(Constants.ERROR_INVALID_DAY);
        }
    }

    public Vehicle getVehicle(VehicleDTO vehicleDTO, VehicleTypeEnum vehicleType) {
        Vehicle vehicle = vehiclePersistence.findByPlate(vehicleDTO.getLicensePlate());
        if (null == vehicle) {
            vehicle = new Vehicle(vehicleDTO.getLicensePlate(), vehicleDTO.getCylinderPower(), vehicleType.getVehicleTypeName());
            vehicle = vehiclePersistence.saveAndFlush(vehicle);
        }
        return vehicle;
    }

    public ParkingDTO leaveParking(VehicleDTO vehicleDTO) {
        String licensePlate = vehicleDTO.getLicensePlate();
        Validation.checkNullOrEmpty(licensePlate, "placa");
        vehicleDTO.setLicensePlate(licensePlate.toUpperCase());
        Parking parking = parkingPersistence.findVehicleByPlateAndStatusActive(vehicleDTO.getLicensePlate());
        if (parking == null) {
            throw new ParkingException(Constants.ERROR_VEHICLE_NOT_PARKED);
        }
        VehicleTypeEnum vehicleType = VehicleTypeEnum.getVehicleTypeFromLicense(licensePlate);
        clock = Clock.systemUTC();
        parking.setOutDatetime(LocalDateTime.now(clock));
        parking.setFare(BigDecimal.valueOf(calculateParkingFare(parking, vehicleType)));
        parking = parkingPersistence.save(parking);
        return mapper.map(parking, ParkingDTO.class);
    }

    public int calculateParkingFare(Parking parking, VehicleTypeEnum vehicleType) {
        long totalHours = ChronoUnit.HOURS.between(parking.getInDatetime(), parking.getOutDatetime());
        int totalDays = 0;
        if (totalHours >= 24) {
            totalDays = (int) (totalHours / 24);
            totalHours = totalHours % 24;
        }
        if (totalHours > 8) {
            totalDays++;
            totalHours = 0;
        }
        totalHours++;
        int totalFare = 0;
        if (vehicleType == VehicleTypeEnum.CAR) {
            totalFare = (int) ((totalHours * carFarePerHour) + (totalDays * carFarePerDay));
        } else {
            totalFare = (int) ((totalHours * motorcycleFarePerHour) + (totalDays * motorcycleFarePerDay)
                    + (parking.getVehicle().getCylinderPower() >= motorcycleOverFareCylinderPower ? motorcycleOverFare : 0));
        }
        StringBuilder totalTime = new StringBuilder("days: ");
        totalTime.append(totalDays);
        totalTime.append(", hours: ");
        totalTime.append(totalHours);
        parking.setTotalTime(totalTime.toString());
        return totalFare;
    }

    public List<ParkedVehicleDTO> listAllVehicles() {
        return parkingPersistence.listAllCurrentParkedVehicles();
    }

    public String getTRM() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(trmServiceUrl, String.class);
        return response.getBody();
    }

}
