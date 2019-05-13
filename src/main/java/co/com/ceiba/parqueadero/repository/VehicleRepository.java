package co.com.ceiba.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.persistence.IVehiclePersistence;

@Repository
public interface VehicleRepository extends IVehiclePersistence, JpaRepository<Vehicle, String> {

	@Query("SELECT v FROM Vehicle v WHERE v.licensePlate = ?1")
	Vehicle findByPlate(String licensePlate);
}
