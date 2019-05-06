package co.com.ceiba.parqueadero.service.impl;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.dto.ParkingDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.model.Parking;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.repository.ParkingRepository;
import co.com.ceiba.parqueadero.repository.VehicleRepository;
import co.com.ceiba.parqueadero.service.IParkingService;
import co.com.ceiba.parqueadero.util.Validation;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

@Component
public class ParkingServiceImpl implements IParkingService {

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

	@Override
	public ParkingDTO createParking(VehicleDTO vehicleDTO) throws ParkingException {
		String licensePlate = vehicleDTO.getLicensePlate();
		// parameter validation
		Validation.checkNullOrEmpty(licensePlate, "placa");
		Validation.checkNull(vehicleDTO.getCylinderPower(), "cilindraje");
		VehicleTypeEnum vehicleType = VehicleTypeEnum.getVehicleTypeFromLicense(licensePlate);
		clock = Clock.systemUTC();
		LocalDateTime inDatetime = LocalDateTime.now(clock);
		validateVehicle(vehicleType, licensePlate, inDatetime);
		Vehicle vehicle = getVehicle(vehicleDTO, vehicleType);
		Parking parking = new Parking(inDatetime, vehicle);
		parking = parkingRepository.save(parking);
		return modelMapper.map(parking, ParkingDTO.class);
	}

	public void validateVehicle(VehicleTypeEnum vehicleType, String licensePlate, LocalDateTime inDatetime)
			throws ParkingException {
		if (null == vehicleType) {
			throw new ParkingException("La placa a\u00F1adida no es v\u00E1lida");
		}

		if (parkingRepository.findVehicleByPlateAndStatusActive(licensePlate) != null) {
			throw new ParkingException("Este veh\u00EDculo ya se encuentra parqueado");
		}

		short slotsAvailable = vehicleType.equals(VehicleTypeEnum.CAR) ? maxSlotsAvailableCars
				: maxSlotsAvailableMotorcycles;
		if (parkingRepository.countParkedVehiclesByType(vehicleType.getVehicleType()) >= slotsAvailable) {
			throw new ParkingException("No hay espacio disponible para este veh\u00EDculo");
		}

		if (licensePlate.startsWith("A") && (!inDatetime.getDayOfWeek().equals(DayOfWeek.SUNDAY)
				|| !inDatetime.getDayOfWeek().equals(DayOfWeek.MONDAY))) {
			throw new ParkingException("No puede ingresar porque no est\u00E1 en un d\u00EDa habil");
		}
	}

	public Vehicle getVehicle(VehicleDTO vehicleDTO, VehicleTypeEnum vehicleType) {
		Vehicle vehicle = vehicleRepository.findByPlate(vehicleDTO.getLicensePlate());
		if (null == vehicle) {
			vehicle = new Vehicle(vehicleDTO.getLicensePlate(), vehicleDTO.getCylinderPower(),
					vehicleType.getVehicleType());
			vehicle = vehicleRepository.saveAndFlush(vehicle);
		}
		return vehicle;
	}

}
