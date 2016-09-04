package com.github.tkdrahn.dataflow.transform.timer

import com.github.tkdrahn.dataflow.domain.timer.TimerData
import com.google.cloud.dataflow.sdk.transforms.DoFn
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class TimerDataToWindowedTextFormatter extends DoFn<TimerData, String> implements DoFn.RequiresWindowAccess {

    @Override
    public void processElement(DoFn.ProcessContext c) {
        TimerData timerData = c.element()
        c.output('window: ' + c.window() + timerData.engineStatusTimes.toString())
    }
}
