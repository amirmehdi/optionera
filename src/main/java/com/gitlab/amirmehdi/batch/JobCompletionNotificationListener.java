package com.gitlab.amirmehdi.batch;

/**
 * author : Amirmehdi Naghavi
 * 5/26/2019
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    public JobCompletionNotificationListener() {
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info(" JOB {} {}!", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus().toString());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("!!! JOB {} {}! Time to verify the results", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus().toString());
    }
}
