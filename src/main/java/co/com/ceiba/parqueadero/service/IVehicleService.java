package co.com.ceiba.parqueadero.service;

import co.com.ceiba.parqueadero.dto.VehicleDTO;

public interface IVehicleService {

	VehicleDTO findVehicle(String licensePlate);
}
