package co.com.ceiba.parqueadero.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.com.ceiba.parqueadero.builder.VehicleDataBuilder;
import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.exception.ParkingException;
import co.com.ceiba.parqueadero.repository.ParkingRepository;
import co.com.ceiba.parqueadero.repository.VehicleRepository;
import co.com.ceiba.parqueadero.service.impl.ParkingServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ParkingServiceTest {

	@Mock
	ParkingRepository parkingRepository;

	@Mock
	VehicleRepository vehicleRepository;

	@Mock
	private Clock clock;

	@Autowired
	@InjectMocks
	ParkingServiceImpl parkingService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void saveParkingInvalidTypeTest() {
		VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("NOTVALID").build();
		try {
			parkingService.createParking(vehicleDTO);
			fail();
		} catch (ParkingException ex) {
			assertEquals(VehicleDataBuilder.MESSAGE_INVALID_LICENSE_PLATE, ex.getMessage());
		}
	}

	@Test
	public void saveParkingInvalidDateTest() {
		VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("ABC123").build();
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

	public void saveParkingAlreadyFullTest() {
		VehicleDTO vehicleDTO = new VehicleDataBuilder().build();
		Mockito.when(parkingRepository.countParkedVehiclesByType(1)).thenReturn(20);
		try {
			parkingService.createParking(vehicleDTO);
			fail();
		} catch (ParkingException ex) {
			assertEquals(VehicleDataBuilder.MESSAGE_INVALID_FULL_PARKING, ex.getMessage());
		}
	}

}