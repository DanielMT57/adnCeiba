package co.com.ceiba.parqueadero.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.com.ceiba.parqueadero.builder.VehicleDataBuilder;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.model.Parking;
import co.com.ceiba.parqueadero.repository.ParkingRepository;
import co.com.ceiba.parqueadero.repository.VehicleRepository;
import co.com.ceiba.parqueadero.service.impl.ParkingServiceImpl;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ParkingServiceTest {

    @Mock
    ParkingRepository parkingRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    private Clock clock;

    @Value("${parking.maxSlotsAvailable.cars}")
    private int maxSlotsAvailableCars;

    @Value("${parking.fare.car.day}")
    private int carFareDay;

    @Value("${parking.fare.car.hour}")
    private int carFareHour;

    @Autowired
    @InjectMocks
    ParkingServiceImpl parkingService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveParkingInvalidTypeTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("NOTVALID").buildDTO();
        try {
            parkingService.createParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_INVALID_LICENSE_PLATE, ex.getMessage());
        }
    }

    @Test
    public void saveParkingNullCylinderPower() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setLicensePlate("SQL312");
        try {
            parkingService.createParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_NULL_CYLINDER_POWER, ex.getMessage());
        }
    }

    @Test
    public void saveParkingInvalidDateTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("ABC123").buildDTO();
        LocalDateTime fixedDate = LocalDateTime.of(2019, 05, 06, 0, 0);
        Clock fixedClock = Clock.fixed(fixedDate.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        Mockito.doReturn(fixedClock.instant()).when(clock).instant();
        Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();
        try {
            parkingService.createParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_INVALID_DAY, ex.getMessage());
        }
    }

    @Test
    public void saveParkingAlreadyFullTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().buildDTO();
        Mockito.when(parkingRepository.countParkedVehiclesByType(Mockito.anyString())).thenReturn(maxSlotsAvailableCars);
        try {
            parkingService.createParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_INVALID_FULL_PARKING, ex.getMessage());
        }
    }
    
    @Test
    public void saveParkingAlreadyParkedVehicleTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().buildDTO();
        Mockito.when(parkingRepository.findVehicleByPlateAndStatusActive(Mockito.anyString())).thenReturn(new Parking());
        try {
            parkingService.createParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_ALREADY_PARKED, ex.getMessage());
        }
    }

    @Test
    public void leaveParkingVehicleNotFoundTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().buildDTO();
        try {
            parkingService.leaveParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_INVALID_VEHICLE_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    public void leaveParkingNullLicensePlate() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        try {
            parkingService.leaveParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_NULL_LICENSE_PLATE, ex.getMessage());
        }
    }

    @Test
    public void leaveParkingEmptyLicensePlate() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("").buildDTO();
        try {
            parkingService.leaveParking(vehicleDTO);
            fail();
        } catch (ParkingException ex) {
            assertEquals(VehicleDataBuilder.MESSAGE_NULL_LICENSE_PLATE, ex.getMessage());
        }
    }

    @Test
    public void leaveParkingCalculateDayFare() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        Parking parking = new Parking(1, now, tomorrow, "", BigDecimal.valueOf(0), null);
        int totalFare = parkingService.calculateParkingFare(parking, VehicleTypeEnum.CAR);
        assertEquals(carFareDay + carFareHour, totalFare);
    }

    @Test
    public void leaveParkingCalculateNineHoursFare() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(9);
        Parking parking = new Parking(1, now, tomorrow, "", BigDecimal.valueOf(0), null);
        int totalFare = parkingService.calculateParkingFare(parking, VehicleTypeEnum.CAR);
        assertEquals(carFareHour * 9, totalFare);
    }

}
