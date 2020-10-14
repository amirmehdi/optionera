package com.gitlab.amirmehdi.web.rest;

import com.gitlab.amirmehdi.batch.BatchConfiguration;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/batch")
public class BatchResource {
    private final BatchConfiguration batchConfiguration;

    public BatchResource(BatchConfiguration batchConfiguration) {
        this.batchConfiguration = batchConfiguration;
    }

    @PostMapping(value = "run-job/{job}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> runJob(@PathVariable BatchConfiguration.JOBS job)
        throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException {
        batchConfiguration.runJobs(new String[]{job.toString()});
        return ResponseEntity.ok().build();
    }
}
