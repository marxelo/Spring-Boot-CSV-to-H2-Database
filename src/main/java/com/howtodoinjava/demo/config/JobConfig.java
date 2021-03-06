package com.howtodoinjava.demo.config;

import javax.sql.DataSource;

import com.howtodoinjava.demo.model.Employee;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
 
@Configuration
public class JobConfig {

  @Bean
  public Job readCSVFileJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
    return jobBuilders.get("readCSVFileJob").start(step(stepBuilders)).build();
  }

  @Bean
  public Step step(StepBuilderFactory stepBuilders) {
    return stepBuilders.get("step").<Employee, Employee>chunk(1).reader(reader()).processor(processor())
        .writer(writer()).faultTolerant()
        // .skipPolicy(new CustomSkipPolicy())
        .skipLimit(7).skip(FlatFileParseException.class).skip(NumberFormatException.class).skip(StringIndexOutOfBoundsException.class).skip(DuplicateKeyException.class)
        .skip(ArithmeticException.class).skip(DuplicateKeyException.class)
        // .skip(IllegalArgumentException.class).skip(ItemStreamException.class)
        .listener(new CustomSkippedListener())
       .build();

  }


  @Value("classPath:/input/inputData.csv")
  private Resource inputResource;

  @Bean
  public FlatFileItemReader<Employee> reader() {
      FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<Employee>();
      itemReader.setLineMapper(lineMapper());
      itemReader.setLinesToSkip(1);
      itemReader.setResource(inputResource);
      return itemReader;
  }
  @Bean
  public LineMapper<Employee> lineMapper() {
      DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
      DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
      lineTokenizer.setNames(new String[] { "id", "firstName", "lastName" });
      lineTokenizer.setIncludedFields(new int[] { 0, 1, 2 });
      BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
      fieldSetMapper.setTargetType(Employee.class);
      lineMapper.setLineTokenizer(lineTokenizer);
      lineMapper.setFieldSetMapper(fieldSetMapper);
      return lineMapper;
  }
  
  @Bean
  public EmployeeItemProcessor processor() {
    return new EmployeeItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<Employee> writer() {
      JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
      itemWriter.setDataSource(dataSource());
      itemWriter.setSql("INSERT INTO EMPLOYEE (ID, FIRSTNAME, LASTNAME) VALUES (:id, :firstName, :lastName)");
      itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
      return itemWriter;
  }
  @Bean
    public DataSource dataSource(){
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .addScript("classpath:employee.sql")
                .setType(EmbeddedDatabaseType.H2) 
                .build();
    }

}