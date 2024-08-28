package com.kb.api;

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
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
public class Svc1Sub1Controller implements Svc1Sub1Api {

    private final Svc1Sub1ApiProvider provider;

    @LRA(value = LRA.Type.MANDATORY, end = false)
    public ResponseEntity<?> accountNew(
            String lraId, String oracleTmmTxToken, Map body)
    {
        log.info("[SVC-1][SUB-1]lraId {} / oracleTmmTxToken {}", lraId, oracleTmmTxToken);
        provider.accountNew("",oracleTmmTxToken,body);
        return ResponseEntity.ok().header("SAMPLE", "SAMPLE")
            .body(new HashMap<>().put("key1", "value1"));
    }

    @Complete
    public ResponseEntity<?> completeWork(String lraId) {
        log.info("[SVC-1][SUB-1][/complete] complete() called for LRA : " + lraId);
        // Business logic to complete the work related to this LRA
        String id = new String(
            Base64.getEncoder().encode(lraId.getBytes(StandardCharsets.UTF_8)));
        boolean status = true;
        if (status) {
            return ResponseEntity.ok(ParticipantStatus.Completed.name());
        }
        return ResponseEntity.ok(ParticipantStatus.FailedToComplete.name());
    }

    @Compensate
    public ResponseEntity<?> compensateWork(
            String lraId) {
        log.info("[SVC-1][SUB-1][/compensate] compensate() called for LRA : " + lraId);
        // Business logic to compensate the work related to this LRA
        String bookingId = new String(
            Base64.getEncoder().encode(lraId.getBytes(StandardCharsets.UTF_8)));
        boolean status = true;
        if (status) {
            return ResponseEntity.ok(ParticipantStatus.Compensated.name());
        }
        return ResponseEntity.ok(ParticipantStatus.FailedToCompensate.name());
    }

    @AfterLRA
    public ResponseEntity<?> after(
            URI lraId, String status) {
        log.info("[SVC-1][SUB-1][/after] After LRA Called : " + lraId);
        log.info("[SVC-1][SUB-1][/after] Final LRA Status : " + status);
        // Clean up of resources held by this LRA
        return ResponseEntity.ok().build();
    }
}
