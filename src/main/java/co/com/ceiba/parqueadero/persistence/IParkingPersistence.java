package co.com.ceiba.parqueadero.persistence;

import java.util.List;

import co.com.ceiba.parqueadero.dto.ParkedVehicleDTO;
import co.com.ceiba.parqueadero.model.Parking;

public interface IParkingPersistence {
    Integer countParkedVehiclesByType(String vehicleType);
    
    Parking findVehicleByPlateAndStatusActive(String licensePlate);
    
    List<ParkedVehicleDTO> listAllCurrentParkedVehicles();
    
    Parking save(Parking parking);
}
