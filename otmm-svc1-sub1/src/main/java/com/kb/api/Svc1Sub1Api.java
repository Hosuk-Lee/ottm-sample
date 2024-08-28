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
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/svc-1/api")
@Tag(name = "Svc1Sub1Api", description = "Service1 SubModule1 API")
public interface Svc1Sub1Api {

    final String ORACLE_TMM_TX_TOKEN = "Oracle-Tmm-Tx-Token";

    @PostMapping(
            value = "/new",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
//    @LRA(value = LRA.Type.REQUIRES_NEW, end = false)
    public ResponseEntity<?> accountNew(
        @RequestHeader(value = LRA_HTTP_CONTEXT_HEADER, required = false) String lraId,
        @RequestHeader(value = ORACLE_TMM_TX_TOKEN, required = false) String oracleTmmTxToken,
        @RequestBody Map body);

    @PutMapping(
            value = "/complete",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
//    @Complete
    public ResponseEntity<?> completeWork(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) ;

    @RequestMapping(value = "/compensate", method = RequestMethod.PUT)
//    @Compensate
    public ResponseEntity<?> compensateWork(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) ;

//    @RequestMapping(value = "/after", method = RequestMethod.PUT)
//    @AfterLRA
//    public ResponseEntity<?> after(@RequestHeader(LRA_HTTP_ENDED_CONTEXT_HEADER) String lraId,
//        @RequestBody String status);

//    @DeleteMapping(value = "/forget")
//    public ResponseEntity<?> forget(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
//        @RequestHeader(value = LRA_HTTP_PARENT_CONTEXT_HEADER, required = false) String parentLraID);
//
//    @RequestMapping(value = "/status", method = RequestMethod.GET)
//    public ResponseEntity<?> status(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId,
//        @RequestHeader(value = LRA_HTTP_PARENT_CONTEXT_HEADER, required = false) String parentLRA) ;
//
//    @RequestMapping(value = "/leave", method = RequestMethod.PUT)
//    public ResponseEntity<?> leave(@RequestHeader(LRA_HTTP_CONTEXT_HEADER) String lraId) ;
}
