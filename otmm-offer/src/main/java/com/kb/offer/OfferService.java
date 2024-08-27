package com.kb.offer;

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
public class OfferService {

    @Autowired
    @Qualifier("MicroTxLRA")
    private RestTemplate restTemplate;

    @Value("${svc1.service.url}")
    private String svc1Uri;
    @Value("${svc2.service.url}")
    private String svc2Uri;

    public void callSvc1(Map body) {
//        log.info("Calling Hotel Service to book hotel with booking Id : " + id);

        URI uri = getSvc1Target()
//            .queryParam("hotelName", name)
            .build()
            .toUri();

        Map responseBody = restTemplate.postForEntity(uri, body, Map.class).getBody();
        log.info("responseBody : {} ", responseBody);

        return;
    }

    public void callSvc2(Map body) {
        log.info("[OFFER][SVC2][CALL]");

        URI uri = getSvc2Target()
            .build()
            .toUri();

        Map responseBody = restTemplate.postForEntity(uri, body, Map.class).getBody();
        log.info("responseBody : {} ", responseBody);

        return;
    }

    private UriComponentsBuilder getSvc1Target() {
        log.info("svc1Uri : {}", svc1Uri);
        return UriComponentsBuilder.fromUri(URI.create(svc1Uri));
    }

    private UriComponentsBuilder getSvc2Target() {
        log.info("svc2Uri : {}", svc2Uri);
        return UriComponentsBuilder.fromUri(URI.create(svc2Uri));
    }
}
