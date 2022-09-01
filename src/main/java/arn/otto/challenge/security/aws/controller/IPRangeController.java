package arn.otto.challenge.security.aws.controller;

import arn.otto.challenge.security.aws.service.IPRangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/security/aws/iprange")
public class IPRangeController {

    private final IPRangeService ipRangeService;

    @Autowired
    public IPRangeController(IPRangeService ipRangeService) {
        this.ipRangeService = ipRangeService;
    }

    @GetMapping
    public List<String> getIPRanges(@RequestParam String region) {
        return this.ipRangeService.getIPRanges(region);
    }
}
