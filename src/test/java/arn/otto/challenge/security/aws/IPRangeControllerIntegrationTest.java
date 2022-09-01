package arn.otto.challenge.security.aws;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IPRangeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegionEU() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/security/aws/iprange?region=EU"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues(CONTENT_TYPE, "text/plain;charset=UTF-8"));
    }

    @Test
    void testNotValidRegion() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/security/aws/iprange?region=EU2"))
                .andExpect(status().is4xxClientError());
    }
}
