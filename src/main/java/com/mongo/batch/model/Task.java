package com.mongo.batch.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	
	private String taskId;
	private String description;
	private int severity;
	private String assignee;
	private int storyPoint;

}
