package com.katatds.springbtdemo;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest	
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-testko.properties")
class SpringbtDemoApplicationTestsKO {
	
	@Autowired
	JobLauncherTestUtils jobLaucherTestUtils;
	
	@Test
	public void testSpringbtDemoApplicationTests() throws Exception {
		JobExecution jobExecution = jobLaucherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
	}

}
