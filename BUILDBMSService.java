package com.example.bms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BUILDBMSService {
    private final BUILDBMSStepConfig stepConfig;
    private static final Logger logger = LoggerFactory.getLogger(BUILDBMSService.class);

    public BUILDBMSService(BUILDBMSStepConfig stepConfig) {
        this.stepConfig = stepConfig;
    }

    public void printBMSMapData() {
        try {
            stepConfig.printBMSMapDataStep();
            // Execute IEBGENER program
            // Set up DD statements for SYSPRINT, SYSUT2, SYSIN, SYSUT1
        } catch (Exception e) {
            logger.error("Error in printBMSMapData: ", e);
        }
    }

    public void assembleMap() {
        try {
            stepConfig.assembleMapStep();
            // Execute ASMA90 program
            // Set up DD statements for SYSPRINT, SYSLIB, SYSUT1, SYSUT2, SYSUT3, SYSPUNCH, SYSIN, SYSLIN
        } catch (Exception e) {
            logger.error("Error in assembleMap: ", e);
        }
    }

    public void linkEdit() {
        try {
            stepConfig.linkEditStep();
            // Execute HEWL program
            // Set up DD statements for SYSPRINT, SYSLMOD, SYSUT1, SYSLIN
        } catch (Exception e) {
            logger.error("Error in linkEdit: ", e);
        }
    }

    public void generateDSECT() {
        try {
            stepConfig.generateDSECTStep();
            // Execute ASMA90 program for DSECT generation
            // Set up DD statements similar to assembly step
        } catch (Exception e) {
            logger.error("Error in generateDSECT: ", e);
        }
    }

    public void displayListing() {
        try {
            stepConfig.displayListingStep();
            // Execute IEBGENER program to display listing
            // Set up DD statements for listing dataset and SYSOUT
        } catch (Exception e) {
            logger.error("Error in displayListing: ", e);
        }
    }

    public void cleanupTemporaryDatasets() {
        try {
            stepConfig.cleanupTemporaryDatasetsStep();
            // Execute IEFBR14 program to delete temporary datasets
        } catch (Exception e) {
            logger.error("Error in cleanupTemporaryDatasets: ", e);
        }
    }
}