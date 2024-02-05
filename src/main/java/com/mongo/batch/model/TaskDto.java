package com.mongo.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
	private String description;
	private int severity;
	private String assignee;
	private int storyPoint;
}
