package com.kb.api;

import static com.oracle.microtx.lra.common.TransactionUtils.LRA_HTTP_ENDED_CONTEXT_HEADER;
import static com.oracle.microtx.springboot.lra.annotation.LRA.LRA_HTTP_CONTEXT_HEADER;

import com.oracle.microtx.springboot.lra.annotation.AfterLRA;
import com.oracle.microtx.springboot.lra.annotation.LRA;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Service
public class Svc1Sub1ApiProvider {

    public void accountNew(
            String lraId, String oracleTmmTxToken, Map body)
    {
        log.info("[SVC-1]Svc1Sub1ApiProvider {}", lraId);
    }
    @AfterLRA
    public void after(
            String lraId, String status) {
        log.info("[SVC-1][/after] After LRA Called : " + lraId);
        log.info("[SVC-1][/after] Final LRA Status : " + status);
        // Clean up of resources held by this LRA
//        return ResponseEntity.ok().build();
    }

}
