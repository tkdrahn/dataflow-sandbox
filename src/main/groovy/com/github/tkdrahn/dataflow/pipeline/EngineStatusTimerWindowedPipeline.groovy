package com.github.tkdrahn.dataflow.pipeline

import com.google.cloud.dataflow.sdk.Pipeline
import com.google.cloud.dataflow.sdk.io.TextIO
import com.google.cloud.dataflow.sdk.options.PipelineOptions
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory
import com.google.cloud.dataflow.sdk.transforms.Combine
import com.google.cloud.dataflow.sdk.transforms.Filter
import com.google.cloud.dataflow.sdk.transforms.MapElements
import com.google.cloud.dataflow.sdk.transforms.ParDo
import com.google.cloud.dataflow.sdk.transforms.windowing.FixedWindows
import com.google.cloud.dataflow.sdk.transforms.windowing.Window
import com.github.tkdrahn.dataflow.transform.AddTimestampFn
import com.github.tkdrahn.dataflow.transform.EngineStatusEventMapper
import com.github.tkdrahn.dataflow.transform.timer.EngineStatusTimerAccumulator
import com.github.tkdrahn.dataflow.transform.timer.EngineStatusTimerFinishWindowFunction
import com.github.tkdrahn.dataflow.transform.MatchHeaderPredicate
import com.github.tkdrahn.dataflow.transform.timer.TimerDataToWindowedTextFormatter
import com.github.tkdrahn.dataflow.util.OutputTypes
import groovy.util.logging.Slf4j
import org.joda.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Slf4j
class EngineStatusTimerWindowedPipeline implements CommandLineRunner {

    static final String OUTPUT_FILE = '/data/output/engine-status-timer-windowed.txt'

    @Value('${installDirectory}')
    private String installDirectory

    @Value('${inputFile}')
    private String inputFile

    @Autowired
    MatchHeaderPredicate matchHeaderPredicate

    @Autowired
    EngineStatusEventMapper engineStatusEventMapper

    @Autowired
    AddTimestampFn addTimestampFn

    @Autowired
    EngineStatusTimerAccumulator engineStatusTimerAccumulator

    @Autowired
    EngineStatusTimerFinishWindowFunction engineStatusTimerFinishWindowFunction

    @Autowired
    TimerDataToWindowedTextFormatter timerDataToWindowedTextFormatter

    void run(String... args) {
        log.info('inputFile is: {}', pipelineInputFile)
        log.info('outputFile is: {}', pipelineOutputFile)

        // create pipeline using default options
        PipelineOptions options = PipelineOptionsFactory.create()
        Pipeline pipeline = Pipeline.create(options)

        // apply transforms
        pipeline.apply('readFile', TextIO.Read.from(pipelineInputFile))
                .apply('filterHeader', Filter.byPredicate(matchHeaderPredicate))
                .apply('mapToEvents', MapElements.via(engineStatusEventMapper).withOutputType(OutputTypes.ENGINE_STATUS_EVENT))
                .apply('applyTimestamps', ParDo.of(addTimestampFn))
                .apply('applyWindow', Window.<String> into(FixedWindows.of(Duration.standardMinutes(1))))
                .apply(Combine.globally(engineStatusTimerAccumulator).withoutDefaults())
                .apply('runToWindowEnd', ParDo.of(engineStatusTimerFinishWindowFunction))
                .apply('formatText', ParDo.of(timerDataToWindowedTextFormatter))
                .apply('writeToFile', TextIO.Write.to(pipelineOutputFile))

        // run the pipeline
        pipeline.run()
    }

    String getPipelineInputFile() {
        return installDirectory + inputFile
    }

    String getPipelineOutputFile() {
        return installDirectory + OUTPUT_FILE
    }

}
