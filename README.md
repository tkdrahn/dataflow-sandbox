# dataflow-sandbox

Sandbox for testing with Google Dataflow.

YOU MUST: Change the installDirectory located in /${moduleName}/src/main/resources/application.yml to the directory you
cloned this repository to.

To run the app (system properties are optional):

    ./gradlew run -DinputFile=data/input/input.txt

Executing the gradle run command will cause Spring Boot to run each class that implements CommandLineRunner. This repository contains three such classes:

* EngineStatusCounterPipeline
* EngineStatusCounterWindowedPipeline
* EngineStatusTimerWindowedPipeline

Outputs for the pipeline will be found under: ${installDirectory}/data/output/
