package co.com.ceiba.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ceiba.parqueadero.model.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Integer> {

	@Query("SELECT count(p) FROM Parking p WHERE p.outDatetime IS NULL AND p.vehicle.type = ?1")
	Integer countParkedVehiclesByType(Integer vehicleType);

	@Query("SELECT p FROM Parking p WHERE p.vehicle.licensePlate = ?1 AND p.outDatetime IS NULL")
	Parking findVehicleByPlateAndStatusActive(String licensePlate);
}
