package com.katatds.springbtdemo.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class MowJobController {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MowJobController.class);
	
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job mowJob;

    @GetMapping("/mowJob")
    public String runJob(){
        try {
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addString("startDate", LocalDateTime.now().toString());
            JobExecution jobExecution = jobLauncher.run(mowJob, builder.toJobParameters());
            return jobExecution.getStatus().name();
        }catch(Exception e){
			logger.error(e.getMessage());
            return "ERROR";
        }		
    }
}