package com.github.tkdrahn.dataflow.transform

import com.google.cloud.dataflow.sdk.transforms.SimpleFunction
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

@CompileStatic
@Component
@Slf4j
class MatchHeaderPredicate extends SimpleFunction<String, Boolean> {

    @Override
    Boolean apply(String input) {
        return !input.startsWith('timestamp')
    }

}