package com.github.tkdrahn.dataflow.pipeline

import com.google.cloud.dataflow.sdk.Pipeline
import com.google.cloud.dataflow.sdk.io.TextIO
import com.google.cloud.dataflow.sdk.options.PipelineOptions
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory
import com.google.cloud.dataflow.sdk.transforms.Count
import com.google.cloud.dataflow.sdk.transforms.Filter
import com.google.cloud.dataflow.sdk.transforms.MapElements
import com.google.cloud.dataflow.sdk.transforms.ParDo
import com.google.cloud.dataflow.sdk.transforms.windowing.FixedWindows
import com.google.cloud.dataflow.sdk.transforms.windowing.Window
import com.github.tkdrahn.dataflow.domain.EngineStatus
import com.github.tkdrahn.dataflow.transform.AddTimestampFn
import com.github.tkdrahn.dataflow.transform.EngineStatusEventMapper
import com.github.tkdrahn.dataflow.transform.EngineStatusExtractor
import com.github.tkdrahn.dataflow.transform.MatchHeaderPredicate
import com.github.tkdrahn.dataflow.transform.WindowedTextFormatter
import com.github.tkdrahn.dataflow.util.OutputTypes
import groovy.util.logging.Slf4j
import org.joda.time.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Slf4j
class EngineStatusCounterWindowedPipeline implements CommandLineRunner {

    static final String OUTPUT_FILE = '/data/output/engine-status-counter-windowed.txt'

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
    EngineStatusExtractor engineStatusExtractor

    @Autowired
    WindowedTextFormatter windowedTextFormatter

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
                .apply('extractEngineStatuses', MapElements.via(engineStatusExtractor).withOutputType(OutputTypes.ENGINE_STATUS))
                .apply('countEngineStatuses', Count.<EngineStatus> perElement())
                .apply('formatText', ParDo.of(windowedTextFormatter))
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
