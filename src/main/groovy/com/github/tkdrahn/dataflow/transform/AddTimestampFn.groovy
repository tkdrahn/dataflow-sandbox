package com.github.tkdrahn.dataflow.transform

import com.github.tkdrahn.dataflow.domain.EngineStatusEvent
import com.google.cloud.dataflow.sdk.transforms.DoFn
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class AddTimestampFn extends DoFn<EngineStatusEvent, EngineStatusEvent> {

    @Override
    public void processElement(DoFn.ProcessContext c) {
        EngineStatusEvent event = c.element() as EngineStatusEvent
        c.outputWithTimestamp(event, event.timestamp.toInstant());
    }

}



