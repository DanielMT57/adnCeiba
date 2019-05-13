package co.com.ceiba.parqueadero.persistence;

import co.com.ceiba.parqueadero.model.Vehicle;

public interface IVehiclePersistence {
    Vehicle findByPlate(String licensePlate);

    Vehicle saveAndFlush(Vehicle vehicle);
}
