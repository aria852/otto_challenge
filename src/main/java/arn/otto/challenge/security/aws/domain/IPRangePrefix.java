package arn.otto.challenge.security.aws.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPRangePrefix {
    @JsonProperty("ip_prefix")
    private String ipv4Prefix;
    @JsonProperty("ipv6_prefix")
    private String ipv6Prefix;
    @JsonProperty("region")
    private String region;
    @JsonProperty("service")
    private String service;
    @JsonProperty("network_border_group")
    private String networkBorderGroup;

    public String getIPPrefix() {
        if(StringUtils.hasText(ipv4Prefix))
            return ipv4Prefix;
        return ipv6Prefix;
    }
}