package co.com.ceiba.parqueadero.integration;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import co.com.ceiba.parqueadero.builder.VehicleDataBuilder;
import co.com.ceiba.parqueadero.dto.VehicleDTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class VehicleControllerTest extends RequestExecutor {
    public static final String URL_GET_VEHICLE = "/vehicle/get/";

    @Test
    public void getVehicleTest() throws Exception {
        VehicleDTO vehicleDTO = new VehicleDataBuilder().withLicensePlate(VehicleDataBuilder.LICENSE_PLATE_MOTORCYCLE_EX4)
                .withCylinderPower(125).buildDTO();
        makePOSTRequest("/create", vehicleDTO);
        MvcResult result = makeGETRequest(URL_GET_VEHICLE + vehicleDTO.getLicensePlate());
        JSONObject resp = new JSONObject(result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(vehicleDTO.getCylinderPower(), resp.get("cylinderPower"));
    }

    @Test
    public void getVehicleNotFoundTest() throws Exception {
        MvcResult result = makeGETRequest(URL_GET_VEHICLE + "NOTFOUND");
        JSONObject resp = new JSONObject(result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(Boolean.TRUE, resp.length() > 0);
    }
}
