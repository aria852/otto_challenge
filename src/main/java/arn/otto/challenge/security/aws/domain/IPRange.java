package arn.otto.challenge.security.aws.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPRange {

    @JsonProperty("syncToken")
    private String syncToken;
    @JsonProperty("createDate")
    private String createDate;
    @JsonProperty("prefixes")
    private List<IPRangePrefix> prefixes;
    @JsonProperty("ipv6_prefixes")
    private List<IPRangePrefix> ipv6Prefixes;
}