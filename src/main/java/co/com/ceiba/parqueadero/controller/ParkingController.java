package co.com.ceiba.parqueadero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.parqueadero.dto.ErrorDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.service.IParkingService;

@RestController
@RequestMapping(path = "/parking")
public class ParkingController {

	@Autowired
	private IParkingService parkingService;

	@PostMapping(value = "/createParking")
	public ResponseEntity<Object> createParking(@RequestBody VehicleDTO vehicleDTO) {
		try {
			return ResponseEntity.ok(parkingService.createParking(vehicleDTO));
		} catch (ParkingException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(ex));
		}
	}
}
