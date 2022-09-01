package arn.otto.challenge.security.aws;

import arn.otto.challenge.security.aws.domain.IPRange;
import arn.otto.challenge.security.aws.domain.IPRangePrefix;
import arn.otto.challenge.security.aws.service.IPRangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IPRangeServiceTest {

    private IPRangeService ipRangeService;

    @Mock private RestTemplate restTemplate;
    @Mock private RestTemplateBuilder restTemplateBuilder;

    private final IPRangePrefix iprp_af_s_1 =  IPRangePrefix.builder()
            .ip_prefix("3.2.34.0/26")
            .region("af-south-1")
            .service("AMAZON")
            .network_border_group("af-south-1")
            .build();
    private final IPRangePrefix iprp_ap_ne_2 =  IPRangePrefix.builder()
            .ip_prefix("3.5.140.0/22")
            .region("ap-northeast-2")
            .service("AMAZON")
            .network_border_group("ap-northeast-2")
            .build();
    private final IPRangePrefix iprp_ap_se_4 =  IPRangePrefix.builder()
            .ip_prefix("13.34.37.64/27")
            .region("ap-southeast-4")
            .service("AMAZON")
            .network_border_group("ap-southeast-4")
            .build();
    private final IPRangePrefix iprp_eu_c_1 =  IPRangePrefix.builder()
            .ip_prefix("150.222.129.255/32")
            .region("eu-central-1")
            .service("AMAZON")
            .network_border_group("eu-central-1")
            .build();
    private final IPRangePrefix iprp6_us_w_2 =  IPRangePrefix.builder()
            .ipv6_prefix("2600:1f70:4000:300::/5")
            .region("us-west-2")
            .service("AMAZON")
            .network_border_group("us-west-2")
            .build();
    private final IPRangePrefix iprp6_eu_w_1 =  IPRangePrefix.builder()
            .ipv6_prefix("2a05:d03a:8000::/56")
            .region("eu-west-1")
            .service("AMAZON")
            .network_border_group("eu-west-2")
            .build();

    private final IPRange expectedIPRange = IPRange.builder()
            .syncToken("st1")
            .createDate("2202-08-31")
            .prefixes(List.of(iprp_af_s_1, iprp_ap_ne_2, iprp_ap_se_4, iprp_eu_c_1))
            .ipv6_prefixes(List.of(iprp6_us_w_2, iprp6_eu_w_1))
            .build();

    @BeforeEach
    void setUp() {
        var validRegions = "EU,US,AP,CN,SA,AF,CA";
        var webUrl = "https://unittest.challange.otto.arn/ip-ranges.json";

        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        lenient().when(restTemplate.getForObject(webUrl, IPRange.class)).thenReturn(expectedIPRange);

        this.ipRangeService = new IPRangeService(restTemplateBuilder, webUrl, validRegions);
    }

    @Test
    void testRegionAPWithTwoIPValues() {
        var actualIPRange = ipRangeService.getIPRanges("AP");
        assertNotNull(actualIPRange);
        var expectedIPRange = List.of(iprp_ap_ne_2.getIPPrefix(), iprp_ap_se_4.getIPPrefix());
        assertEquals(expectedIPRange, actualIPRange);
    }

    @Test
    void testRegionEUWithTwoIPValues() {
        var actualIPRange = ipRangeService.getIPRanges("EU");
        assertNotNull(actualIPRange);
        var expectedIPRange = List.of(iprp_eu_c_1.getIPPrefix(), iprp6_eu_w_1.getIPPrefix());
        assertEquals(expectedIPRange, actualIPRange);
    }

    @Test
    void testRegionCNWithNoValues() {
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
    }
}
