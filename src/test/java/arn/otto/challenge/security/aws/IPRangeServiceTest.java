package arn.otto.challenge.security.aws;

import arn.otto.challenge.TestUtil;
import arn.otto.challenge.security.aws.domain.IPRange;
import arn.otto.challenge.security.aws.domain.IPRangePrefix;
import arn.otto.challenge.security.aws.service.IPRangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IPRangeServiceTest {

    private IPRangeService ipRangeService;
    private final String webUrl = "https://unittest.challange.otto.arn/ip-ranges.json";

    @Mock private RestTemplate restTemplate;
    @Mock private RestTemplateBuilder restTemplateBuilder;

    private final IPRangePrefix iprp_af_s_1 =  IPRangePrefix.builder()
            .ipv4Prefix("3.2.34.0/26")
            .region("af-south-1")
            .service("AMAZON")
            .networkBorderGroup("af-south-1")
            .build();
    private final IPRangePrefix iprp_ap_ne_2 =  IPRangePrefix.builder()
            .ipv4Prefix("3.5.140.0/22")
            .region("ap-northeast-2")
            .service("AMAZON")
            .networkBorderGroup("ap-northeast-2")
            .build();
    private final IPRangePrefix iprp_ap_se_4 =  IPRangePrefix.builder()
            .ipv4Prefix("13.34.37.64/27")
            .region("ap-southeast-4")
            .service("AMAZON")
            .networkBorderGroup("ap-southeast-4")
            .build();
    private final IPRangePrefix iprp_eu_c_1 =  IPRangePrefix.builder()
            .ipv4Prefix("150.222.129.255/32")
            .region("eu-central-1")
            .service("AMAZON")
            .networkBorderGroup("eu-central-1")
            .build();
    private final IPRangePrefix iprp6_us_w_2 =  IPRangePrefix.builder()
            .ipv6Prefix("2600:1f70:4000::/40")
            .region("us-west-2")
            .service("AMAZON")
            .networkBorderGroup("us-west-2")
            .build();
    private final IPRangePrefix iprp6_eu_w_1 =  IPRangePrefix.builder()
            .ipv6Prefix("2a05:d03a:8000::/56")
            .region("eu-west-1")
            .service("AMAZON")
            .networkBorderGroup("eu-west-2")
            .build();

    private static IPRange expectedIPRange;

    @BeforeAll
    static void beforeAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        expectedIPRange = objectMapper.readValue(TestUtil.readTestData("ip-ranges.json"), IPRange.class);
    }

    @BeforeEach
    void setUp() {
        var validRegions = "EU,US,AP,CN,SA,AF,CA";

        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        lenient().when(restTemplate.getForObject(webUrl, IPRange.class)).thenReturn(expectedIPRange);

        this.ipRangeService = new IPRangeService(restTemplateBuilder, webUrl, validRegions);
    }

    @Test
    void happyTestRegionEUWithTwoIPValues() {
        var actualIPRange = ipRangeService.getIPRanges("EU");
        assertNotNull(actualIPRange);
        var expectedIPRange = List.of(iprp_eu_c_1.getIPPrefix(), iprp6_eu_w_1.getIPPrefix());
        assertEquals(expectedIPRange, actualIPRange);

        verify(restTemplateBuilder, times(1)).build();
        verify(restTemplate, times(1)).getForObject(webUrl, IPRange.class);
    }

    @Test
    void testParseIPRange() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var iprange = objectMapper.readValue(TestUtil.readTestData("ip-ranges.json"), IPRange.class);
        assertNotNull(iprange);
        assertEquals(4, iprange.getPrefixes().size());
        assertEquals(2, iprange.getIpv6Prefixes().size());

        assertEquals("1662013390", iprange.getSyncToken());
        assertEquals("2022-09-01-06-23-10", iprange.getCreateDate());

        assertEquals(iprp_af_s_1.getIPPrefix(), iprange.getPrefixes().get(0).getIPPrefix());
        assertEquals(iprp_af_s_1.getIPPrefix(), iprange.getPrefixes().get(0).getIpv4Prefix());
        assertEquals(iprp_af_s_1.getService(), iprange.getPrefixes().get(0).getService());
        assertEquals(iprp_af_s_1.getRegion(), iprange.getPrefixes().get(0).getRegion());
        assertEquals(iprp_af_s_1.getNetworkBorderGroup(), iprange.getPrefixes().get(0).getNetworkBorderGroup());

        assertEquals(iprp6_us_w_2.getIPPrefix(), iprange.getIpv6Prefixes().get(0).getIPPrefix());
        assertEquals(iprp6_us_w_2.getIPPrefix(), iprange.getIpv6Prefixes().get(0).getIpv6Prefix());
        assertEquals(iprp6_us_w_2.getService(), iprange.getIpv6Prefixes().get(0).getService());
        assertEquals(iprp6_us_w_2.getRegion(), iprange.getIpv6Prefixes().get(0).getRegion());
        assertEquals(iprp6_us_w_2.getNetworkBorderGroup(), iprange.getIpv6Prefixes().get(0).getNetworkBorderGroup());
    }

    @Test
    void testRegionAPWithTwoIPValues() {
        var actualIPRange = ipRangeService.getIPRanges("AP");
        assertNotNull(actualIPRange);
        var expectedIPRange = List.of(iprp_ap_ne_2.getIPPrefix(), iprp_ap_se_4.getIPPrefix());
        assertEquals(expectedIPRange, actualIPRange);
    }

    @Test
    void testRegionWithNoValues() {
        var emptyIPRange = new IPRange();
        lenient().when(restTemplate.getForObject(webUrl, IPRange.class)).thenReturn(emptyIPRange);
        var actualIPRange = ipRangeService.getIPRanges("CN");
        assertNotNull(actualIPRange);
        assertEquals(List.of(), actualIPRange);
    }

    @Test
    void testRegionALL() {
        var actualIPRange = ipRangeService.getIPRanges("ALL");
        assertNotNull(actualIPRange);

        var expectedIPRange = List.of(
                iprp_af_s_1.getIPPrefix(),
                iprp_ap_ne_2.getIPPrefix(),
                iprp_ap_se_4.getIPPrefix(),
                iprp_eu_c_1.getIPPrefix(),
                iprp6_us_w_2.getIPPrefix(),
                iprp6_eu_w_1.getIPPrefix());

        assertEquals(expectedIPRange, actualIPRange);
    }

    @Test
    void testRegionNotValid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ipRangeService.getIPRanges("EU2"));
        assertEquals("Region EU2 is not allowed", exception.getMessage());
        verify(restTemplate, never()).getForObject(any(), any());
    }
}
