package com.github.tkdrahn.dataflow.transform

import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.EngineStatusEvent
import com.google.cloud.dataflow.sdk.transforms.SimpleFunction
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.stereotype.Component

@CompileStatic
@Component
@Slf4j
class EngineStatusEventMapper extends SimpleFunction<String, EngineStatusEvent> {

    @Override
    EngineStatusEvent apply(String input) {
        String[] fields = input.split(',')

        return new EngineStatusEvent(
                timestamp: DateTime.parse(fields[0]),
                engineStatus: EngineStatus.valueOf(fields[1])
        )
    }
}