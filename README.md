Expected migration to Spring Batch.

Purpose and Concept
The purpose of this document is to provide a comprehensive guide for configuring and running the BUILDBMS job within the Spring Batch framework. This file serves as a guide for developers and engineers to understand the steps involved in compiling BMS maps, the input parameters required, and the expected outputs. The main responsibilities of this file include providing clear instructions on how to set up the environment, execute the job, and handle any potential errors that may arise during the process.

Interactions
This document interacts with the following components:
- BUILDBMSJobConfig
- BUILDBMSStepConfig
- BUILDBMSService

Inputs and Outputs
Inputs:
- MAPNAME: The name of the BMS map to be compiled.
- HLQ: The high-level qualifier for dataset names.
- SRCCODE: The source code dataset path, formatted as &HLQ..CARDDEMO.BMS.
- LOADLIB: The load library dataset path, formatted as &HLQ..CARDDEMO.LOADLIB.
- CPYBKS: The copybooks dataset path, formatted as &HLQ..CARDDEMO.CPY.
- CICSMAC: The CICS macro library dataset path, formatted as OEM.CICSTS.V05R06M0.CICS.SDFHMAC.
- LISTING: The listing output dataset path, formatted as &HLQ..CARDDEMO.LST.

Outputs:
The output of each step in the BMS compilation process, including printed BMS map data, assembled maps, link-edited outputs, generated DSECTs, and the final listing of the compiled map.

Error Handling
Instructions for handling errors during the execution of the BUILDBMS job include logging error messages and stack traces, as well as troubleshooting guidance for common issues.

Performance and Scalability Considerations
The job is designed to handle multiple executions with different parameters, allowing for efficient processing of BMS maps in a batch environment.

Pseudocode
```
PROCEDURE BUILDBMS
    INPUT PARAMETERS:
        MAPNAME
        HLQ
        SRCCODE = HLQ..CARDDEMO.BMS
        LOADLIB = HLQ..CARDDEMO.LOADLIB
        CPYBKS = HLQ..CARDDEMO.CPY
        CICSMAC = OEM.CICSTS.V05R06M0.CICS.SDFHMAC
        LISTING = HLQ..CARDDEMO.LST

    // Step 1: Print BMS Map Data
    EXECUTE PRINT
        PROGRAM = IEBGENER
        DD STATEMENTS:
            SYSPRINT = SYSOUT
            SYSUT2 = TEMPORARY DATASET
            SYSIN = DUMMY
            SYSUT1 = DATASET SRCCODE WITH MAPNAME

    // Step 2: Assemble Map
    EXECUTE MAP
        PROGRAM = ASMA90
        DD STATEMENTS:
            SYSPRINT = SYSOUT
            SYSLIB = DATASET CICSMAC
            SYSUT1 = TEMPORARY DATASET
            SYSUT2 = TEMPORARY DATASET
            SYSUT3 = TEMPORARY DATASET
            SYSPUNCH = TEMPORARY DATASET
            SYSIN = TEMPORARY DATASET FROM PRINT STEP
            SYSLIN = DUMMY

    // Step 3: Link-Edit Step
    EXECUTE LKED
        PROGRAM = HEWL
        DD STATEMENTS:
            SYSPRINT = SYSOUT
            SYSLMOD = DATASET LOADLIB WITH MAPNAME
            SYSUT1 = TEMPORARY DATASET
            SYSLIN = TEMPORARY DATASET FROM ASSEMBLE MAP STEP

    // Step 4: DSECT Generation
    EXECUTE DSECT
        PROGRAM = ASMA90
        DD STATEMENTS:
            SYSPRINT = DATASET LISTING WITH MAPNAME
            SYSLIB = DATASET CICSMAC
            SYSUT1 = TEMPORARY DATASET
            SYSUT2 = TEMPORARY DATASET
            SYSUT3 = TEMPORARY DATASET
            SYSPUNCH = DATASET CPYBKS WITH MAPNAME
            SYSIN = TEMPORARY DATASET FROM PRINT STEP
            SYSLIN = DUMMY

    // Step 5: Display Listing
    EXECUTE DISPLIST
        PROGRAM = IEBGENER
        DD STATEMENTS:
            SYSPRINT = SYSOUT
            SYSUT1 = DATASET LISTING WITH MAPNAME
            SYSUT2 = SYSOUT
            SYSIN = DUMMY

    // Step 6: Cleanup Temporary Datasets
    EXECUTE DELTEMP
        PROGRAM = IEFBR14
        DD STATEMENT:
            TEMPM = TEMPORARY DATASET TO DELETE

    // Step 7: Error Handling
    IMPLEMENT ERROR HANDLING
        LOG ANY ISSUES DURING EXECUTION OF EACH STEP

    // Step 8: Documentation
    DOCUMENT THE PROCESS
        INCLUDE PURPOSE OF EACH STEP, INPUT PARAMETERS, EXPECTED OUTPUTS