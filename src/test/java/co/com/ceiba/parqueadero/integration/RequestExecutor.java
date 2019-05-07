package co.com.ceiba.parqueadero.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestExecutor {

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/parking";

    public MvcResult makePOSTRequest(String serviceUri, Object body) throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.post(BASE_URL + serviceUri).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(body));

        return mockMvc.perform(builder).andReturn();
    }

    public MvcResult makePUTRequest(String serviceUri, Object body) throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.put(BASE_URL + serviceUri).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(body));

        return mockMvc.perform(builder).andReturn();
    }

    public MvcResult makeGETRequest(String serviceUri) throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.get(BASE_URL + serviceUri).contentType(MediaType.APPLICATION_JSON_UTF8);
        return mockMvc.perform(builder).andReturn();
    }

}
