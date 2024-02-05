package com.mongo.batch.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import com.mongo.batch.model.Task;
import com.mongo.batch.model.TaskDto;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {
	@Autowired
	private FlatFileItemReader<TaskDto> reader1;
	@Autowired
	private MongoItemReader<Task> batchReader;
	@Autowired
	private FlatFileItemWriter<Task> writer;

	public static List<String> column1List = new ArrayList<>();
	public static List<TaskDto> taskList = new ArrayList<>();

	@Bean
	@StepScope
	public ItemWriter<TaskDto> writer1() {
		return items -> {
			for (TaskDto item : items) {
				taskList.add(item);
				column1List.add(item.getAssignee());
			}
		};
	}

	@Override
	@Bean
	public DataSource getDataSource() {
		return new EmbeddedDatabaseBuilder().addScript("/org/springframework/batch/core/schema-drop-h2.sql")
				.addScripts("/org/springframework/batch/core/schema-h2.sql").setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("step1", jobRepository).<TaskDto, TaskDto>chunk(1000, platformTransactionManager)
				.reader(reader1).writer(writer1()).build();
	}

	@Bean
	public Step step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("step2", jobRepository).<Task, Task>chunk(10000, platformTransactionManager)
				.reader(batchReader).writer(writer).taskExecutor(taskExecutor()).faultTolerant()
				.skip(FlatFileParseException.class).

				build();
	}

	@Bean
	public Job job(JobRepository jobRepository, JobExecutionListener jobExecutionListener, Step step1, Step step2) {
		return new JobBuilder("job", jobRepository).start(step1).next(step2).listener(jobExecutionListener)
				.incrementer(new RunIdIncrementer()).build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}

}
