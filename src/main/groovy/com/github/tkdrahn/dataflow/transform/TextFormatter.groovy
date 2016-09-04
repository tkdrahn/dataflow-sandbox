package com.github.tkdrahn.dataflow.transform

import com.google.cloud.dataflow.sdk.transforms.SimpleFunction
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@CompileStatic
@Component
@Slf4j
class TextFormatter extends SimpleFunction<Object, String> {

    @Override
    String apply(Object input) {
        return input.toString()
    }
}