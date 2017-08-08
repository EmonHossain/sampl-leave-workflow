package com.csit.flow;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskService taskService;

	@Transactional
	public List<Task> getAssinedTasks(String category, String assignee) {
		logger.debug("method called getAssinedTasks with params " + category + " and " + assignee);
		return taskService.createTaskQuery().taskName(category).taskAssignee(assignee).list();
	}

	public void completeTask(String processInstanceId, String assignee, Map<String, Object> data) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(assignee).singleResult();
		logger.debug("task id : " + task.getId() + " task name : " + task.getName());
		taskService.complete(task.getId(), data);
	}

}
