package com.github.tkdrahn.dataflow.domain.timer

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.EngineStatusEvent
import com.google.cloud.dataflow.sdk.coders.DefaultCoder
import com.google.cloud.dataflow.sdk.coders.SerializableCoder
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@CompileStatic
@DefaultCoder(SerializableCoder)
@EqualsAndHashCode
@JsonPropertyOrder(alphabetic = true)
@ToString
class TimerData implements Serializable {

    Map<EngineStatus, Long> engineStatusTimes= [:]

    EngineStatusEvent finalEventInWindow
}
