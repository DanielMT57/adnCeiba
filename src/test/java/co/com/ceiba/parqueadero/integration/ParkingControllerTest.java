package co.com.ceiba.parqueadero.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import co.com.ceiba.parqueadero.builder.VehicleDataBuilder;
import co.com.ceiba.parqueadero.dto.VehicleDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ParkingControllerTest extends RequestExecutor {

    public static final String URL_CREATE_PARKING = "/createParking";

    public static final String URL_LEAVE_PARKING = "/leaveParking";

    @Value("${parking.fare.motorcycle.hour}")
    private int motorcycleFareHour;

    @Value("${parking.fare.motorcycle.overfare}")
    private int motorcycleOverFare;

    public MvcResult createParking(VehicleDTO vehicleDTO) throws Exception {
        return makePOSTRequest(URL_CREATE_PARKING, vehicleDTO);
    }

    public MvcResult leaveParking(VehicleDTO vehicleDTO) throws Exception {
        return makePUTRequest(URL_LEAVE_PARKING, vehicleDTO);
    }

    @Test
    public void createParkingTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().buildDTO();
        try {
            MvcResult result = createParking(vehicleDTO);
            assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void createParkingInvalidPlateTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate("NOTVALID").buildDTO();
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

    @Test
    public void leaveParkingTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate(VehicleDataBuilder.LICENSE_PLATE_CAR_EX2).buildDTO();
        try {
            createParking(vehicleDTO);
            MvcResult result = leaveParking(vehicleDTO);
            assertEquals(200, result.getResponse().getStatus());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void leaveParkingNotFoundTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate(VehicleDataBuilder.LICENSE_PLATE_CAR_EX2).buildDTO();
        try {
            MvcResult result = leaveParking(vehicleDTO);
            assertEquals(500, result.getResponse().getStatus());
            JSONObject resp = new JSONObject(result.getResponse().getContentAsString());
            assertEquals(VehicleDataBuilder.MESSAGE_INVALID_VEHICLE_NOT_FOUND, resp.get("message"));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void leaveParkingMotorcycleOverfareTest() {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate(VehicleDataBuilder.LICENSE_PLATE_MOTORCYCLE_EX)
                .withCylinderPower(700).buildDTO();
        try {
            createParking(vehicleDTO);
            MvcResult result = leaveParking(vehicleDTO);
            JSONObject resp = new JSONObject(result.getResponse().getContentAsString());
            assertEquals(200, result.getResponse().getStatus());
            assertEquals(BigDecimal.valueOf(motorcycleFareHour + motorcycleOverFare), BigDecimal.valueOf((int) resp.get("fare")));
        } catch (Exception e) {
            fail();
        }
    }
}
