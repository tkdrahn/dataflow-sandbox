package com.github.tkdrahn.dataflow.transform

import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.EngineStatusEvent
import com.google.cloud.dataflow.sdk.transforms.SimpleFunction
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class EngineStatusExtractor extends SimpleFunction<EngineStatusEvent, EngineStatus> {

    @Override
    EngineStatus apply(EngineStatusEvent event) {
        return event.engineStatus
    }

}