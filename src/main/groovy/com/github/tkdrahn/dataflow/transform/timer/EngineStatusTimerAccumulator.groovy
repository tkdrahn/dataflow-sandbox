package com.github.tkdrahn.dataflow.transform.timer

import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.EngineStatusEvent
import com.github.tkdrahn.dataflow.domain.timer.TimerData
import com.google.cloud.dataflow.sdk.coders.DefaultCoder
import com.google.cloud.dataflow.sdk.coders.SerializableCoder
import com.google.cloud.dataflow.sdk.transforms.Combine
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@CompileStatic
@Component
@Slf4j
class EngineStatusTimerAccumulator extends Combine.CombineFn<EngineStatusEvent, Accum, TimerData> {

    @Override
    Accum createAccumulator() { return new Accum() }

    @Override
    Accum addInput(Accum accum, EngineStatusEvent input) {
        accum.addEvent(input)
        return accum
    }

    @Override
    Accum mergeAccumulators(Iterable<Accum> accums) {
        Accum merged = createAccumulator()
        accums.each { Accum accum ->
            merged.addEvents(accum.events)
        }

        return merged
    }

    @Override
    // outputs time spent in each state
    TimerData extractOutput(Accum accum) {
        Map<EngineStatus, Long> engineStatusTimes = [:]

        EngineStatusEvent previousEvent = null

        accum.events.each { EngineStatusEvent event ->
            if (!previousEvent) {
                previousEvent = event
                engineStatusTimes[previousEvent.engineStatus] = 0L
            } else {
                if (!engineStatusTimes[previousEvent.engineStatus]) {
                    engineStatusTimes[previousEvent.engineStatus] = 0L
                }

                Long millisSinceLastEvent = event.timestamp.millis - previousEvent.timestamp.millis
                engineStatusTimes[previousEvent.engineStatus] += millisSinceLastEvent

                previousEvent = event
            }
        }

        return new TimerData(
                engineStatusTimes: engineStatusTimes,
                finalEventInWindow: previousEvent
        )
    }

    @DefaultCoder(SerializableCoder)
    static class Accum implements Serializable {

        SortedSet<EngineStatusEvent> events = new TreeSet({ EngineStatusEvent a, EngineStatusEvent b ->
            return (a.timestamp <=> b.timestamp)
        } as Comparator)


        void addEvent(EngineStatusEvent event) {
            events << event
        }

        void addEvents(Iterable<EngineStatusEvent> newEvents) {
            newEvents.each { addEvent(it) }
        }

    }
}

