package com.csit.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.GetStartFormCmd;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csit.dtos.Leave;

@Transactional
@Service
public class WorkflowService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RuntimeService runtimeService;

	@Transactional
	public List<Task> getAssinedTasks(String category, String assignee) {
		logger.debug("method called getAssinedTasks with params " + category + " and " + assignee);
		return taskService.createTaskQuery().taskName(category).taskAssignee(assignee).list();
	}

	/*public void completeTask(String processInstanceId, String assignee, Map<String, Object> data) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(assignee)
				.singleResult();
		logger.debug("task id : " + task.getId() + " task name : " + task.getName());
		taskService.complete(task.getId(), data);
	}*/

	public String startFlow(String username) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess",
				Collections.singletonMap("assignee", username));
		return processInstance.getId();
	}

	public void completeTask(String processInstanceId, String username) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(username).singleResult();
		taskService.complete(task.getId(), getData(username));
	}

	public Map<String,Object> getData(String username) {
		Map<String,Object> map = new HashMap<String,Object>();
		UserEntity user = userDao.findUserByUsername(username);
		UserEntity nextUser = null;
		if (user.getRole().equalsIgnoreCase("role_user")) {
			map.put("resubmit", false);
			map.put("super", false);
			map.put("head", false);
			map.put("hr", false);
			nextUser = userDao.findNextUserRole("role_superviser");
			map.put("assignee", nextUser.getUsername());
		} else if (user.getRole().equalsIgnoreCase("role_superviser")) {
			map.put("resubmit", false);
			map.put("super", true);
			map.put("head", false);
			map.put("hr", false);
			nextUser = userDao.findNextUserRole("role_deptHead");
			map.put("assignee", nextUser.getUsername());
		} else if (user.getRole().equalsIgnoreCase("role_deptHead")) {
			map.put("resubmit", false);
			map.put("super", false);
			map.put("head", true);
			map.put("hr", false);
			nextUser = userDao.findNextUserRole("role_hr");
			map.put("assignee", nextUser.getUsername());
		}else{
			return null;
		}
		return map;
		//return nextUser.getUsername();
	}

	public List<Leave> getLeaveApplications(String name) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(name).list();
		List<String> proIds = new ArrayList<String>();
		for(Task t : tasks)
			proIds.add(t.getProcessInstanceId());
		List<Object[]> objs = userDao.findAllAssignedApplication(proIds);
		List<Leave> leaves = new ArrayList<Leave>();
		for(Object[] o : objs)
			leaves.add(new Leave((String)o[0], (String)o[1], (String)o[3], (String)o[2]));
		return leaves;
	}

	public void saveApplication(LeaveApplicationEntity leaveApplicationEntity) {
		userDao.saveLeaveApplication(leaveApplicationEntity);
	}

	public long getUserId(String name) {
		
		return userDao.findUserByUsername(name).getUserId();
	}

	public int userCount() {
		return userDao.countAllUsers();
	}

	public UserEntity getUser(String name) {
		return userDao.findUserByUsername(name);
	}

	public void rejectApplication(String proId) {
		Task task = taskService.createTaskQuery().processInstanceId(proId).singleResult();
		taskService.deleteTask(task.getId(), "Not eligible");
	}

	public LeaveApplicationEntity getApplicationByProId(String proId) {
		return userDao.findLeaveApplicationByProId(proId);
	}

	public void applicationResubmit(String proId, String name,String applicant) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assignee", applicant);
		map.put("resubmit", true);
		Task task = taskService.createTaskQuery().processInstanceId(proId).taskAssignee(name).singleResult();
		taskService.complete(task.getId(),map);
	}

	public void updateLeaveApplication(LeaveApplicationEntity application) {
		userDao.updateApplication(application);
	}

	public List<LeaveApplicationEntity> getApplicationByStatus(String name) {
		return userDao.findAppByStatus(name);
	}

}
