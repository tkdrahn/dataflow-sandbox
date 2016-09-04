package com.github.tkdrahn.dataflow.domain

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.google.cloud.dataflow.sdk.coders.DefaultCoder
import com.google.cloud.dataflow.sdk.coders.SerializableCoder
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.DateTime

@CompileStatic
@DefaultCoder(SerializableCoder)
@EqualsAndHashCode
@JsonPropertyOrder(alphabetic = true)
@ToString
class EngineStatusEvent implements Serializable {

    DateTime timestamp
    EngineStatus engineStatus

}
