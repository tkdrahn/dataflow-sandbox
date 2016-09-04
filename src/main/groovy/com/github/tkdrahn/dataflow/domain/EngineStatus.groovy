package com.github.tkdrahn.dataflow.domain

import com.google.cloud.dataflow.sdk.coders.AvroCoder
import com.google.cloud.dataflow.sdk.coders.DefaultCoder
import groovy.transform.CompileStatic

@CompileStatic
@DefaultCoder(AvroCoder)
enum EngineStatus {

    OFF,
    PARKED,
    DRIVING,
    MALFUNCTION

}
