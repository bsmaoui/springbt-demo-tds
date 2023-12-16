package com.katatds.springbtdemo.config;

import com.katatds.springbtdemo.batchutils.BatchStepSkipper;
import com.katatds.springbtdemo.model.InputData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
public class MowJobConfig {

  public static final Logger logger = LoggerFactory.getLogger(MowJobConfig.class);


  private final JobRepository jobRepository;

  public MowJobConfig(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
  }

//  @Value("classpath:inputData")
  @Value("${inputData}")
  private String inputFeed;

  @Autowired
  private BatchStepSkipper skipper;
  
  @Bean(name="mowJob")
  public Job mowJob(Step step) {

    var name = "Tondeuse";
    var builder = new JobBuilder(name, jobRepository);

    return builder.start(step)
        .build();
  }

  @Bean
  public Step step(ItemReader<InputData> reader,
                    ItemWriter<InputData> writer,
                    ItemProcessor<InputData, InputData> processor,
                    PlatformTransactionManager txManager) {
    var name = "Tondeuse mouvement";
    var builder = new StepBuilder(name, jobRepository);
    return builder
        .<InputData, InputData>chunk(1, txManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .faultTolerant()
        .skipPolicy(skipper)
        .build();
  }


  @Bean
  public FlatFileItemReader<InputData> flatFileileReader(
      LineMapper<InputData> lineMapper) {
    var itemReader = new FlatFileItemReader<InputData>();
    itemReader.setLineMapper(lineMapper);
    //itemReader.setResource(inputFeed);
    itemReader.setResource(new FileSystemResource(inputFeed));
    itemReader.setStrict(false);
    return itemReader;
  }

  @Bean
  public DefaultLineMapper<InputData> lineMapper(LineTokenizer tokenizer,
                                              FieldSetMapper<InputData> mapper) {
    var lineMapper = new DefaultLineMapper<InputData>();
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(mapper);
    return lineMapper;
  }

  @Bean
  public BeanWrapperFieldSetMapper<InputData> fieldSetMapper() {
    var fieldSetMapper = new BeanWrapperFieldSetMapper<InputData>();
    fieldSetMapper.setTargetType(InputData.class);
    return fieldSetMapper;
  }

  @Bean
  public DelimitedLineTokenizer tokenizer() {
    var tokenizer = new DelimitedLineTokenizer();
    //tokenizer.setDelimiter(" ");
    //tokenizer.setNames("coordX", "coordY", "orientation", "control");
    tokenizer.setNames("line");
    tokenizer.setStrict(false);
    return tokenizer;
  }

}