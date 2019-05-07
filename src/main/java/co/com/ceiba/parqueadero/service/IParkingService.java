package co.com.ceiba.parqueadero.service;

import co.com.ceiba.parqueadero.dto.ParkingDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;

public interface IParkingService {

	ParkingDTO createParking(VehicleDTO vehicleDTO) throws ParkingException;

	ParkingDTO leaveParking(VehicleDTO vehicleDTO) throws ParkingException;
}
