package com.doramonz.aligonggoo.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

//@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository jobRepository;
    private final UpdateStateStep updateStateStep;
    private final JobLauncher jobLauncher;

//    @Bean(name = "productGongGooJob")
//    public Job productGongGooJob() {
//        return new JobBuilder("productGongGooJob", jobRepository)
//                .start(updateStateStep.updateStateStep())
//                .build();
//    }

//    @Scheduled(fixedDelay = 10 * 60 * 1000)
//    public void runProductGongGooJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        JobParameters jobParameter = new JobParametersBuilder()
//                .addString("productGongGooJob", String.valueOf(System.currentTimeMillis()))
//                .toJobParameters();
//        jobLauncher.run(productGongGooJob(), jobParameter);
//    }
}
