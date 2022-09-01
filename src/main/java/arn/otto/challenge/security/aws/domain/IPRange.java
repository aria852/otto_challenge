package arn.otto.challenge.security.aws.domain;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class IPRange {

    String syncToken;
    String createDate;
    List<IPRangePrefix> prefixes;
    List<IPRangePrefix> ipv6_prefixes;
}