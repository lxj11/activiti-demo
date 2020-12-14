package com.example.activiti.demo.query;

import java.util.HashMap;

public class GatewayTaskQuery {
	
	private String taskId;
	
	private HashMap<String, Object> variables;
	
	

	public GatewayTaskQuery(String taskId, HashMap<String, Object> variables) {
		super();
		this.taskId = taskId;
		this.variables = variables;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public HashMap<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(HashMap<String, Object> variables) {
		this.variables = variables;
	}
	
	
	

}
