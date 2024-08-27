package com.kb.offer;

import static com.oracle.microtx.springboot.lra.annotation.LRA.LRA_HTTP_CONTEXT_HEADER;
import static com.oracle.microtx.springboot.lra.annotation.LRA.LRA_HTTP_ENDED_CONTEXT_HEADER;

import com.oracle.microtx.springboot.lra.annotation.AfterLRA;
import com.oracle.microtx.springboot.lra.annotation.Compensate;
import com.oracle.microtx.springboot.lra.annotation.LRA;
import com.oracle.microtx.springboot.lra.annotation.ParticipantStatus;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/account-service/api")
public class OfferController {

    private static final String ORACLE_TMM_TX_TOKEN = "Oracle-Tmm-Tx-Token";

    private final OfferService offerService;

    @PostMapping("/new")
    @LRA(value = LRA.Type.REQUIRES_NEW, end = false)
    public ResponseEntity<?> accountNew(
        @RequestBody Map body,
        @RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
        @RequestHeader(value = ORACLE_TMM_TX_TOKEN, required = false) String oracleTmmTxToken) {
        boolean result = process(body, lraId, oracleTmmTxToken);
        if (!result) {
            return ResponseEntity.internalServerError()
                .header(ORACLE_TMM_TX_TOKEN, oracleTmmTxToken)
                .body(new HashMap<>().put("key1", "value1"));
        }
        return ResponseEntity.ok().header(ORACLE_TMM_TX_TOKEN, oracleTmmTxToken)
            .body(new HashMap<>().put("key1", "value1"));
    }

    @PostMapping("/new/end")
    @LRA(value = LRA.Type.REQUIRES_NEW, end = true)
    public ResponseEntity<?> accountNewEnd(
        @RequestBody Map body,
        @RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
        @RequestHeader(value = ORACLE_TMM_TX_TOKEN, required = false) String oracleTmmTxToken) {
        boolean result = process(body, lraId, oracleTmmTxToken);
        if (!result) {
            return ResponseEntity.internalServerError()
                .header(ORACLE_TMM_TX_TOKEN, oracleTmmTxToken)
                .body(new HashMap<>().put("key1", "value1"));
        }
        return ResponseEntity.ok().header(ORACLE_TMM_TX_TOKEN, oracleTmmTxToken)
            .body(new HashMap<>().put("key1", "value1"));
    }

    private boolean process(Map body, String lraId, String oracleTmmTxToken) {
        boolean result = true;
        log.info("{}", body);
        log.info("lraId {} / oracleTmmTxToken {}", lraId, oracleTmmTxToken);

        try {
            offerService.callSvc1(body);
            offerService.callSvc2(body);
        } catch (Exception e) {
            log.error("[ERROR] {}", e.getMessage(), e);
            result = false;
        }
        return result;
    }

    @RequestMapping(value = "/after", method = RequestMethod.PUT)
    @AfterLRA
    public ResponseEntity<?> afterLra(@RequestHeader(LRA_HTTP_ENDED_CONTEXT_HEADER) String lraId,
        @RequestBody String status) {
        String bookingId = new String(
            Base64.getEncoder().encode(lraId.getBytes(StandardCharsets.UTF_8)));
        log.info("After LRA Called : " + lraId);
        log.info("Final LRA Status : " + status);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/compensate", method = RequestMethod.PUT)
    @Compensate
    public ResponseEntity<?> compensateWork(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        log.info("[OFFER] compensate() called for LRA : " + lraId);
        boolean status = true;
        if (status) {
            return ResponseEntity.ok(ParticipantStatus.Compensated.name());
        }
        return ResponseEntity.ok(ParticipantStatus.FailedToCompensate.name());
    }

}
