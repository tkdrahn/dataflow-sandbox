package com.github.tkdrahn.dataflow.transform.timer

import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.timer.TimerData
import com.google.cloud.dataflow.sdk.transforms.DoFn
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class EngineStatusTimerFinishWindowFunction extends DoFn<TimerData, TimerData> implements DoFn.RequiresWindowAccess {

    @Override
    public void processElement(DoFn.ProcessContext c) {
        TimerData timerData = c.element()

        Map<EngineStatus, Long> updatedTimes = timerData.engineStatusTimes.clone()
        if (timerData.finalEventInWindow) {
            Long toWindowEnd = c.window().maxTimestamp().millis - timerData.finalEventInWindow.timestamp.millis
            updatedTimes[timerData.finalEventInWindow.engineStatus] += toWindowEnd
        }

        c.output(new TimerData(
                engineStatusTimes: updatedTimes,
                finalEventInWindow: timerData.finalEventInWindow
        ))
    }
}
