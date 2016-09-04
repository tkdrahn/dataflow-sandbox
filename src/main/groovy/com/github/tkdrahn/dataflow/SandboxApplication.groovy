package com.github.tkdrahn.dataflow

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Slf4j
@CompileStatic
@ComponentScan(
        basePackageClasses = [SandboxApplication]
)
@Configuration
class SandboxApplication {

    static void main(final String[] args) {
        SpringApplication.run(this, args)
    }

}
