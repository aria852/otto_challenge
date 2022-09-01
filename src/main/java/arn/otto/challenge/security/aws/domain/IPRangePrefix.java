package arn.otto.challenge.security.aws.domain;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

@Value
@Builder
public class IPRangePrefix {
    String ip_prefix;
    String ipv6_prefix;
    String region;
    String service;
    String network_border_group;

    public String getIPPrefix() {
        if(StringUtils.hasText(ip_prefix))
            return ip_prefix;
        return ipv6_prefix;
    }
}