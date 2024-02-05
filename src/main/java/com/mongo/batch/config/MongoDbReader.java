package com.mongo.batch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongo.batch.model.Task;
import com.mongo.batch.model.TaskDto;

@Configuration
public class MongoDbReader {
	@Autowired
	private MongoTemplate template1;
	@Bean
	@StepScope
	public MongoItemReader<Task> dbReader() {
		MongoItemReader<Task> reader = new MongoItemReader<>();
		reader.setTemplate(template1);
		reader.setCollection("tasks");
		String customQuery="{$or:[";
		for(TaskDto task: BatchConfig.taskList) {
			customQuery+="{assignee: '" + task.getAssignee() +"', description: '"+task.getDescription()+"'},";
		}
		customQuery= customQuery.substring(0,customQuery.length()-1);
		customQuery+= "]}";
		reader.setQuery(customQuery);
		reader.setPageSize(10000);
		reader.setTargetType(Task.class);
		return reader;
	}

}
