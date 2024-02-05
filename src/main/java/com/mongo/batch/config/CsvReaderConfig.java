package com.mongo.batch.config;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.mongo.batch.model.TaskDto;

@Configuration
public class CsvReaderConfig {

	public FlatFileItemReader<TaskDto> reader1() {
		FlatFileItemReader<TaskDto> reader1 = new FlatFileItemReader<>();
		reader1.setResource(new ClassPathResource("data.csv"));
		reader1.setLineMapper(new DefaultLineMapper<TaskDto>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "severity", "description", "assignee", "storyPoint" });
					}

				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<TaskDto>() {
					{
						setTargetType(TaskDto.class);
					}
				});
			}
		});
		return reader1;
	}

}
