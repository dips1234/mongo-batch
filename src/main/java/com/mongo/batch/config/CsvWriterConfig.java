package com.mongo.batch.config;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.mongo.batch.model.Task;

@Configuration
public class CsvWriterConfig {

	public FlatFileItemWriter<Task> writer() {
		FlatFileItemWriter<Task> writer = new FlatFileItemWriter<>();
		writer.setResource(new FileSystemResource("src/main/resources/task1.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<Task>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Task>() {
					{
						setNames(new String[] { "taskId", "description", "severity", "assignee", "storyPoint" });
					}
				});
			}
		});
		return writer;
	}

}
