package co.com.ceiba.parqueadero.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.parqueadero.dto.ParkedVehicleDTO;
import co.com.ceiba.parqueadero.dto.ParkingDTO;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.service.ParkingService;

@RestController
@RequestMapping(path = "/parking", method = { RequestMethod.POST, RequestMethod.PUT })
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping(value = "/create")
    public ResponseEntity<ParkingDTO> create(@RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(parkingService.createParking(vehicleDTO));
    }

    @PutMapping(value = "/leave")
    public ResponseEntity<ParkingDTO> leave(@RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(parkingService.leaveParking(vehicleDTO));
    }

    @GetMapping(value = "/getVehicles")
    public ResponseEntity<List<ParkedVehicleDTO>> getVehicles() {
        return ResponseEntity.ok(parkingService.listAllVehicles());
    }
}
