package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class BUILDBMSStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final BUILDBMSService buildBMSService;
    private static final Logger logger = LoggerFactory.getLogger(BUILDBMSStepConfig.class);

    public BUILDBMSStepConfig(StepBuilderFactory stepBuilderFactory, BUILDBMSService buildBMSService) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.buildBMSService = buildBMSService;
    }

    public Step printBMSMapDataStep() {
        return stepBuilderFactory.get("printBMSMapDataStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.printBMSMapData();
                } catch (Exception e) {
                    logger.error("Error in printBMSMapDataStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .retry(3)
            .build();
    }

    public Step assembleMapStep() {
        return stepBuilderFactory.get("assembleMapStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.assembleMap();
                } catch (Exception e) {
                    logger.error("Error in assembleMapStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .retry(3)
            .build();
    }

    public Step linkEditStep() {
        return stepBuilderFactory.get("linkEditStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.linkEdit();
                } catch (Exception e) {
                    logger.error("Error in linkEditStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .retry(3)
            .build();
    }

    public Step generateDSECTStep() {
        return stepBuilderFactory.get("generateDSECTStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.generateDSECT();
                } catch (Exception e) {
                    logger.error("Error in generateDSECTStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .retry(3)
            .build();
    }

    public Step displayListingStep() {
        return stepBuilderFactory.get("displayListingStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.displayListing();
                } catch (Exception e) {
                    logger.error("Error in displayListingStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .retry(3)
            .build();
    }

    public Step cleanupTemporaryDatasetsStep() {
        return stepBuilderFactory.get("cleanupTemporaryDatasetsStep")
            .tasklet((contribution, chunkContext) -> {
                try {
                    buildBMSService.cleanupTemporaryDatasets();
                } catch (Exception e) {
                    logger.error("Error in cleanupTemporaryDatasetsStep: {}", e.getMessage(), e);
                    throw e;
                }
                return RepeatStatus.FINISHED;
            })
            .build();
    }
}