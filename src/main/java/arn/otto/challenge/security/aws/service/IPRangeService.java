package arn.otto.challenge.security.aws.service;

import arn.otto.challenge.security.aws.domain.IPRange;
import arn.otto.challenge.security.aws.domain.IPRangePrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        var ipRange = restTemplate.getForObject(webUrl, IPRange.class);
        if (ipRange == null) {
            throw new IllegalStateException("IPRange is null, but should be empty list");
        }

        List<String> filterIPRange;
        List<String> regionFilter;
        if(region.equalsIgnoreCase("all")) {
            regionFilter = validRegions;
        } else {
            regionFilter = List.of(region);
        }
        filterIPRange = (filterRegion(regionFilter, ipRange.getPrefixes()));
        filterIPRange.addAll(filterRegion(regionFilter, ipRange.getIpv6Prefixes()));

        return filterIPRange;
    }

    private List<String> filterRegion(List<String> regions, List<IPRangePrefix> ipRangePrefix) {
        if (ipRangePrefix != null && !ipRangePrefix.isEmpty()) {
            return ipRangePrefix
                    .stream()
                    .filter(i -> regions.stream().anyMatch(r -> i.getRegion().toLowerCase().startsWith(r.toLowerCase() + "-")))
                    .map(IPRangePrefix::getIpPrefix)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList() ;
    }
}
