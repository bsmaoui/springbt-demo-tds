package com.katatds.springbtdemo.batchutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchStepSkipper implements SkipPolicy {
	
	Logger log = LoggerFactory.getLogger(BatchStepSkipper.class);
	
	@Override
	public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
		if(t instanceof FlatFileFormatException && !"bloking".equals(((FlatFileFormatException) t).getInput())) {
			log.info(((FlatFileFormatException) t).getMessage());
			return true;
		}
		return false;
	}
}
