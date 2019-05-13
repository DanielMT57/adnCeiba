package co.com.ceiba.parqueadero.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.persistence.IVehiclePersistence;
import co.com.ceiba.parqueadero.util.Mapper;

@Component
public class VehicleService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private IVehiclePersistence vehiclePersistence;

    public VehicleDTO find(String licensePlate) {
        Vehicle vehicle = vehiclePersistence.findByPlate(licensePlate);
        return mapper.map(vehicle != null ? vehicle : new Vehicle(), VehicleDTO.class);
    }

}
