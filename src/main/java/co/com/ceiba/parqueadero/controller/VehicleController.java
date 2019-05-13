package co.com.ceiba.parqueadero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.service.VehicleService;

@RestController
@RequestMapping(path = "/parking/vehicle", method = { RequestMethod.GET })
@CrossOrigin(origins = { "http://localhost:4200" })
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(value = "/get/{licensePlate}")
    public ResponseEntity<VehicleDTO> get(@PathVariable String licensePlate) {
        return ResponseEntity.ok(vehicleService.find(licensePlate));
    }
}
