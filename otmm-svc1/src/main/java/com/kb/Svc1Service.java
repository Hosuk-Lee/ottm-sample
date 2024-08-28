package com.kb;

import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class Svc1Service {
    @Autowired
    @Qualifier("MicroTxLRA")
    private RestTemplate restTemplate;

    @Value("${svc1-sbu1.service.url}")
    private String svc1sub1Uri;

    public void callSvc1Sub1(Map body) {
        URI uri = getSvc1Sub1Target()
//            .queryParam("hotelName", name)
            .build()
            .toUri();

        Map responseBody = restTemplate.postForEntity(uri, body, Map.class).getBody();
        log.info("responseBody : {} ", responseBody);
    }
    private UriComponentsBuilder getSvc1Sub1Target() {
        log.info("svc1Uri : {}", svc1sub1Uri);
        return UriComponentsBuilder.fromUri(URI.create(svc1sub1Uri));
    }
}
