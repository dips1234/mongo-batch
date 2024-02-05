package com.mongo.batch.config;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongo.batch.model.Task;

@Configuration
public class MongoBatchReader {
	@Autowired
	private MongoTemplate template;

	@Bean
	@StepScope
	public MongoItemReader<Task> batchReader() {
		MongoItemReader<Task> reader = new MongoItemReader<>();
		reader.setTemplate(template);
		reader.setCollection("tasks");
		List<String> ids = BatchConfig.column1List;
		Query query = new Query(Criteria.where("assignee").in(ids));
		reader.setQuery(query);
		reader.setPageSize(10000);
		reader.setSort(Collections.singletonMap("_id", Direction.ASC));
		reader.setTargetType(Task.class);
		return reader;

	}

}
