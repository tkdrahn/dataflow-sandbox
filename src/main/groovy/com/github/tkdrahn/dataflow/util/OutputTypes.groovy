package com.github.tkdrahn.dataflow.util

import com.google.cloud.dataflow.sdk.values.TypeDescriptor
import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.domain.EngineStatusEvent

class OutputTypes {

    static final TypeDescriptor<EngineStatusEvent> ENGINE_STATUS_EVENT = new TypeDescriptor<EngineStatusEvent>() {}
    static final TypeDescriptor<EngineStatus> ENGINE_STATUS = new TypeDescriptor<EngineStatus>() {}
    static final TypeDescriptor<String> STRING = new TypeDescriptor<String>() {}

}
