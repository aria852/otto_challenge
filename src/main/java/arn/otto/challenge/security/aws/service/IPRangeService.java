package arn.otto.challenge.security.aws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class IPRangeService {

    private final RestTemplate restTemplate;
    private final String webUrl;
    private final List<String> validRegions;

    @Autowired
    public IPRangeService(RestTemplateBuilder restTemplateBuilder,
                          @Value("${AWS_IPRANGES_WEB_URL}") String webUrl,
                          @Value("${AWS_VALID_REGIONS}") String validRegions) {
        this.restTemplate = restTemplateBuilder.build();
        this.webUrl = webUrl;
        this.validRegions = List.of(validRegions.split(","));
    }

    public List<String> getIPRanges(String region) {
        if (!region.equalsIgnoreCase("all") &&
                this.validRegions.stream().noneMatch(r -> r.equalsIgnoreCase(region))) {
            throw new IllegalArgumentException(String.format("Region %s is not allowed", region));
        }

        return List.of("100.101.102.103", "1.2.3.4");
    }
}
