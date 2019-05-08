package co.com.ceiba.parqueadero.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.dto.ParkedVehicleDTO;
import co.com.ceiba.parqueadero.dto.ParkingDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.model.Parking;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.repository.ParkingRepository;
import co.com.ceiba.parqueadero.repository.VehicleRepository;
import co.com.ceiba.parqueadero.util.Validation;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

@Component
public class ParkingService {

    private Clock clock;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    public ParkingDTO createParking(VehicleDTO vehicleDTO) {
        clock = Clock.systemUTC();
        LocalDateTime inDatetime = LocalDateTime.now(clock);
        VehicleTypeEnum vehicleType = VehicleTypeEnum.getVehicleTypeFromLicense(vehicleDTO.getLicensePlate());
        validateVehicleToCheckIn(vehicleDTO, vehicleType, inDatetime);
        Vehicle vehicle = getVehicle(vehicleDTO, vehicleType);
        Parking parking = new Parking(inDatetime, vehicle);
        parking = parkingRepository.save(parking);
        return modelMapper.map(parking, ParkingDTO.class);
    }

    public void validateVehicleToCheckIn(VehicleDTO vehicleDTO, VehicleTypeEnum vehicleType, LocalDateTime inDatetime) {
        String licensePlate = vehicleDTO.getLicensePlate();
        // parameter validation
        Validation.checkNullOrEmpty(licensePlate, "placa");
        Validation.checkNull(vehicleDTO.getCylinderPower(), "cilindraje");
        if (null == vehicleType) {
            throw new ParkingException("La placa a\u00F1adida no es v\u00E1lida");
        }

        if (parkingRepository.findVehicleByPlateAndStatusActive(licensePlate) != null) {
            throw new ParkingException("Este veh\u00EDculo ya se encuentra parqueado");
        }

        short slotsAvailable = vehicleType.equals(VehicleTypeEnum.CAR) ? maxSlotsAvailableCars : maxSlotsAvailableMotorcycles;
        if (parkingRepository.countParkedVehiclesByType(vehicleType.getVehicleTypeName()) >= slotsAvailable) {
            throw new ParkingException("No hay espacio disponible para este veh\u00EDculo");
        }

        if (licensePlate.startsWith("A")
                && (!inDatetime.getDayOfWeek().equals(DayOfWeek.SUNDAY) || !inDatetime.getDayOfWeek().equals(DayOfWeek.MONDAY))) {
            throw new ParkingException("No puede ingresar porque no est\u00E1 en un d\u00EDa habil");
        }
    }

    public Vehicle getVehicle(VehicleDTO vehicleDTO, VehicleTypeEnum vehicleType) {
        Vehicle vehicle = vehicleRepository.findByPlate(vehicleDTO.getLicensePlate());
        if (null == vehicle) {
            vehicle = new Vehicle(vehicleDTO.getLicensePlate(), vehicleDTO.getCylinderPower(), vehicleType.getVehicleTypeName());
            vehicle = vehicleRepository.saveAndFlush(vehicle);
        }
        return vehicle;
    }

    public ParkingDTO leaveParking(VehicleDTO vehicleDTO) {
        String licensePlate = vehicleDTO.getLicensePlate();
        Validation.checkNullOrEmpty(licensePlate, "placa");
        vehicleDTO.setLicensePlate(licensePlate.toUpperCase());
        Parking parking = parkingRepository.findVehicleByPlateAndStatusActive(vehicleDTO.getLicensePlate());
        if (parking == null) {
            throw new ParkingException("Este veh\u00EDculo no se encuentra parqueado");
        }
        VehicleTypeEnum vehicleType = VehicleTypeEnum.getVehicleTypeFromLicense(licensePlate);
        clock = Clock.systemUTC();
        parking.setOutDatetime(LocalDateTime.now(clock));
        parking.setFare(BigDecimal.valueOf(calculateParkingFare(parking, vehicleType)));
        parking = parkingRepository.save(parking);
        return modelMapper.map(parking, ParkingDTO.class);
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
        return parkingRepository.listAllCurrentParkedVehicles();
    }

}