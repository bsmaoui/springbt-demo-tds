package com.katatds.springbtdemo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

  private DataSource dataSource;

  public DataSourceConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  @Bean
  public DataSourceInitializer databasePopulator() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
    populator.setContinueOnError(false);
    populator.setIgnoreFailedDrops(false);
    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(dataSource);
    dataSourceInitializer.setDatabasePopulator(populator);
    return dataSourceInitializer;
  }
}