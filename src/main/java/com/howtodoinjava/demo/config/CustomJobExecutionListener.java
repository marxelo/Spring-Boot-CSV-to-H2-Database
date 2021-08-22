package com.howtodoinjava.demo.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecutionListener implements JobExecutionListener {
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Iniciando job " + jobExecution.getJobInstance());
    }
 
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job execution info: " + jobExecution.toString());
    }
}
