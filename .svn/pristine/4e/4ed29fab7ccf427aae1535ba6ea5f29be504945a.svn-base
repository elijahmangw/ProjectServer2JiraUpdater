package com.directv;

import java.text.SimpleDateFormat;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    //@Scheduled(fixedRate = 5000)
    //Using cron for schedule tasks
    //Running Tuesdays and Fridays @3pm
    //@Scheduled(cron = "0 15 12 ? * MON-FRI") // will run mon to fridat at 12:15am
    //@Scheduled(cron = "0 0/10 * * * ?") //running every 10 mins, just for testing purposes
    @Scheduled(cron = "0/10 * * * * ?") //running every 10 mins, just for testing purposes
    public void reportCurrentTime() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		jobLauncher.run(job, new JobParameters());
    }
}