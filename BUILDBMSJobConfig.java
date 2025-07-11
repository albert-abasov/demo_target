package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableBatchProcessing
public class BUILDBMSJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(BUILDBMSJobConfig.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BUILDBMSStepConfig buildBmsStepConfig;
    private final Environment environment;

    public BUILDBMSJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, 
                             BUILDBMSStepConfig buildBmsStepConfig, Environment environment) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.buildBmsStepConfig = buildBmsStepConfig;
        this.environment = environment;
    }

    @Bean
    public Job createJob() {
        String mapName = environment.getProperty("MAPNAME");
        String hlq = environment.getProperty("HLQ");

        if (mapName == null || hlq == null) {
            logger.error("Job parameters MAPNAME and HLQ must be provided.");
            throw new IllegalArgumentException("Missing required job parameters.");
        }

        return jobBuilderFactory.get("Compile BMS Map")
                .incrementer(new RunIdIncrementer())
                .start(buildBmsStepConfig.printBMSMapDataStep())
                .next(buildBmsStepConfig.assembleMapStep())
                .next(buildBmsStepConfig.linkEditStep())
                .next(buildBmsStepConfig.generateDSECTStep())
                .next(buildBmsStepConfig.displayListingStep())
                .next(buildBmsStepConfig.cleanupTemporaryDatasetsStep())
                .build();
    }
}