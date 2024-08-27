package com.kb;

import static com.oracle.microtx.lra.common.TransactionUtils.LRA_HTTP_ENDED_CONTEXT_HEADER;
import static com.oracle.microtx.springboot.lra.annotation.LRA.LRA_HTTP_CONTEXT_HEADER;
import static com.oracle.microtx.springboot.lra.annotation.LRA.LRA_HTTP_PARENT_CONTEXT_HEADER;

import com.oracle.microtx.springboot.lra.annotation.AfterLRA;
import com.oracle.microtx.springboot.lra.annotation.Compensate;
import com.oracle.microtx.springboot.lra.annotation.Complete;
import com.oracle.microtx.springboot.lra.annotation.Forget;
import com.oracle.microtx.springboot.lra.annotation.LRA;
import com.oracle.microtx.springboot.lra.annotation.Leave;
import com.oracle.microtx.springboot.lra.annotation.ParticipantStatus;
import com.oracle.microtx.springboot.lra.annotation.Status;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/svc-2/api")
public class Svc2Controller {

    private static final String ORACLE_TMM_TX_TOKEN = "Oracle-Tmm-Tx-Token";

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @LRA(value = LRA.Type.MANDATORY, end = false)
    public ResponseEntity<?> accountNew(
        @RequestParam(value = "account_name", required = false, defaultValue = "basic") String accountName,
        @RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
        @RequestHeader(value = ORACLE_TMM_TX_TOKEN, required = false) String oracleTmmTxToken) {
        log.info("[SVC-2]lraId {} / oracleTmmTxToken {}", lraId, oracleTmmTxToken);
        return ResponseEntity.ok().header("SAMPLE", "SAMPLE")
            .body(new HashMap<>().put("key1", "value1"));
    }

    @RequestMapping(value = "/complete", method = RequestMethod.PUT)
    @Complete
    public ResponseEntity<?> completeWork(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        log.info("[SVC-2][/complete] complete() called for LRA : " + lraId);
        // Business logic to complete the work related to this LRA
        String id = new String(
            Base64.getEncoder().encode(lraId.getBytes(StandardCharsets.UTF_8)));
        boolean status = true;
        if (status) {
            return ResponseEntity.ok(ParticipantStatus.Completed.name());
        }
        return ResponseEntity.ok(ParticipantStatus.FailedToComplete.name());
    }

    @RequestMapping(value = "/compensate", method = RequestMethod.PUT)
    @Compensate
    public ResponseEntity<?> compensateWork(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        log.info("[SVC-2][/compensate] compensate() called for LRA : " + lraId);
        // Business logic to compensate the work related to this LRA
        String bookingId = new String(
            Base64.getEncoder().encode(lraId.getBytes(StandardCharsets.UTF_8)));
        boolean status = true;
        if (status) {
            return ResponseEntity.ok(ParticipantStatus.Compensated.name());
        }
        return ResponseEntity.ok(ParticipantStatus.FailedToCompensate.name());
    }

    @RequestMapping(value = "/after", method = RequestMethod.PUT)
    @AfterLRA
    public ResponseEntity<?> after(@RequestHeader(LRA_HTTP_ENDED_CONTEXT_HEADER) String lraId,
        @RequestBody String status) {
        log.info("[SVC-2][/after] After LRA Called : " + lraId);
        log.info("[SVC-2][/after] Final LRA Status : " + status);
        // Clean up of resources held by this LRA
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/forget", method = RequestMethod.DELETE)
    @Forget
    public ResponseEntity<?> forget(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
        @RequestHeader(value = LRA_HTTP_PARENT_CONTEXT_HEADER, required = false) String parentLraID) {
        log.info("[SVC-2][/forget] After LRA Called : " + lraId);
        log.info("[SVC-2][/forget] Final LRA Status : " + parentLraID);
        return ResponseEntity.ok("Cleaned Up");
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @Status
    public ResponseEntity<?> status(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
        @RequestHeader(value = LRA_HTTP_PARENT_CONTEXT_HEADER, required = false) String parentLRA) {
        log.info("[SVC-2][/status] status() called for LRA : " + lraId);
        if (parentLRA != null) { // is the context nested
            // code which is sensitive to executing with a nested context goes here
        }
        return ResponseEntity.ok(ParticipantStatus.Compensated);
    }

    @RequestMapping(value = "/leave", method = RequestMethod.PUT)
    @Leave
    public ResponseEntity<?> leave(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) {
        log.info("[SVC-2][/leave] Leave called {}", lraId);
        if (lraId == null) {
            return new ResponseEntity<>("LRA header not present", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Left LRA");
    }
}
