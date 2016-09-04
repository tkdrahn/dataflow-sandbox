package com.github.tkdrahn.dataflow.transform

import com.google.cloud.dataflow.sdk.transforms.DoFn
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@Component
@Slf4j
class WindowedTextFormatter extends DoFn<Object, String> implements DoFn.RequiresWindowAccess {

    @Override
    public void processElement(DoFn.ProcessContext c) {
        c.output('window: ' + c.window() + ' - ' + c.element().toString())
    }
}
