package arn.otto.challenge.security.aws.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPRangePrefix {
    @JsonProperty("ip_prefix")
    @JsonAlias("ipv6_prefix")
    private String ipPrefix;
    @JsonProperty("region")
    private String region;
    @JsonProperty("service")
    private String service;
    @JsonProperty("network_border_group")
    private String networkBorderGroup;
}