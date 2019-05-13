package co.com.ceiba.parqueadero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.ceiba.parqueadero.dto.ParkedVehicleDTO;
import co.com.ceiba.parqueadero.model.Parking;
import co.com.ceiba.parqueadero.persistence.IParkingPersistence;

@Repository
public interface ParkingRepository extends IParkingPersistence, JpaRepository<Parking, Integer> {

    @Query("SELECT count(p) FROM Parking p WHERE p.outDatetime IS NULL AND p.vehicle.type = ?1")
    Integer countParkedVehiclesByType(String vehicleType);

    @Query("SELECT p FROM Parking p WHERE p.vehicle.licensePlate = ?1 AND p.outDatetime IS NULL")
    Parking findVehicleByPlateAndStatusActive(String licensePlate);

    @Query("SELECT new co.com.ceiba.parqueadero.dto.ParkedVehicleDTO(p.vehicle.licensePlate, p.vehicle.type, p.inDatetime) FROM Parking p WHERE p.outDatetime IS NULL")
    List<ParkedVehicleDTO> listAllCurrentParkedVehicles();
}
