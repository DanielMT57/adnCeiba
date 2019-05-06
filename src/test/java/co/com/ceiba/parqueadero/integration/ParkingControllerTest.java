package co.com.ceiba.parqueadero.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import co.com.ceiba.parqueadero.builder.VehicleDataBuilder;
import co.com.ceiba.parqueadero.dto.VehicleDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class ParkingControllerTest extends GeneralTest {

	public static final String URL_CREATE_PARKING = "/createParking";

	public MvcResult createParking(VehicleDTO vehicleDTO) throws Exception {
		return makePOSTRequest(URL_CREATE_PARKING, vehicleDTO);
	}

	@Test
	public void createParkingTest() {
		VehicleDTO vehicleDTO = new VehicleDataBuilder().build();
		try {
			MvcResult result = createParking(vehicleDTO);
			assertEquals(200, result.getResponse().getStatus());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void createParkingInvalidPlateTest() {
		VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("NOTVALID").build();
		MvcResult result = null;
		try {
			result = createParking(vehicleDTO);
			assertEquals(500, result.getResponse().getStatus());
			JSONObject resp = new JSONObject(result.getResponse().getContentAsString());
			assertEquals(VehicleDataBuilder.MESSAGE_INVALID_LICENSE_PLATE, resp.get("message"));
		} catch (Exception e) {
			fail();
		}
	}
}
