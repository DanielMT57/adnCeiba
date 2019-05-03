package co.com.ceiba.parqueadero.service.impl;

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
		Validation.checkNullOrEmpty(licensePlate, "placa");
		VehicleTypeEnum vehicleType = VehicleTypeEnum.checkLicensePlate(licensePlate);
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

		Vehicle vehicle = vehicleRepository.findByPlate(licensePlate);

		if (null == vehicle) {
			Validation.checkNull(vehicleDTO.getCylinderPower(), "cilindraje");
			vehicle = new Vehicle(licensePlate, Integer.valueOf(vehicleDTO.getCylinderPower()),
					vehicleType.getVehicleType());
			vehicle = vehicleRepository.saveAndFlush(vehicle);
		}

		LocalDateTime inDatetime = LocalDateTime.now();
		if (licensePlate.startsWith("A") && (!inDatetime.getDayOfWeek().equals(DayOfWeek.SUNDAY)
				|| !inDatetime.getDayOfWeek().equals(DayOfWeek.MONDAY))) {
			throw new ParkingException("No puede ingresar porque no est\u00E1 en un d\u00EDa habil");
		}

		Parking parking = new Parking(inDatetime, vehicle);
		parking = parkingRepository.save(parking);
		return modelMapper.map(parking, ParkingDTO.class);
	}

}
