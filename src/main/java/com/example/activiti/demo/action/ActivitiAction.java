package com.example.activiti.demo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activiti.demo.comment.Result;
import com.example.activiti.demo.query.GatewayTaskQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/activiti")
@Api(tags = "工作流API")
public class ActivitiAction {
	
	/**
	 * 部署流程定义
	 */
	@PostMapping(value = "deploy")
	@ApiOperation(value = "发布流程")
	public Result<String> create() {
		try {
			System.out.println("开始。。。");
			// 获取流程引擎
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			Deployment deploy = processEngine.getRepositoryService()
					.createDeployment()
					.name("请假申请")
					.addClasspathResource("bpmn/inclusiveGateway.bpmn")
					.addClasspathResource("bpmn/inclusiveGateway.png")
					.deploy();
			//获取仓库服务的实例
			System.out.println(deploy.getId() + "   " + deploy.getName());
			return new Result<String>("成功",200, "deployId: "+deploy.getId()+"  name: "+ deploy.getName());

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return new Result<String>("失败",500, e.toString());
		}
	}
	
	/**
	 * 启动流程实例
	 */
	@PostMapping(value = "startProcess")
	@ApiOperation(value = "启动流程实例")
	public Result<String> startProcess(String processKey) {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processKey);
		System.out.println("pid: "+ processInstance.getId() + "  name: " + processInstance.getName() + " deploymentId: " +processInstance.getDeploymentId());
		return new Result<String>("成功", 200, "pid: "+ processInstance.getId() + "  name: " + processInstance.getName() + " deploymentId: " +processInstance.getDeploymentId());
	}
	
	/**
	 * 查看任务
	 */
	@PostMapping(value = "queryMyTask")
	@ApiOperation(value = "查看任务")
	public Result<List<String>> queryMyTask() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> list = processEngine.getTaskService().createTaskQuery().list();
		List<String> taskList = new ArrayList<String>();
		for (Task task : list) {
			System.out.println("taskId: " + task.getId() + " taskName: " + task.getName());
			taskList.add("taskId: " + task.getId() + " taskName: " + task.getName()+ " processDefinitionId: "+task.getProcessDefinitionId());
		}
		return new Result<List<String>>("成功", 200, taskList);
	}
	
	/**
	 * 完成任务
	 */
	@PostMapping(value = "completeTask")
	@ApiOperation(value = "完成任务")
	public Result<String> completeTask(String taskId) {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务。。。");
		return new Result<String>("成功", 200, "完成任务。。。");
	}
	
	/**
	 * 查看流程定义
	 */
	@PostMapping(value = "queryProcessDefinition")
	@ApiOperation(value = "查看流程定义")
	public Result<List<String>> queryProcessDefinition() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
		List<String> processDefinitionList = new ArrayList<String>();
		for (ProcessDefinition processDefinition : list) {
			System.out.println("id: " + processDefinition.getId());
			System.out.println("name: " + processDefinition.getName());
			System.out.println("key: " + processDefinition.getKey());
			System.out.println("resourceName: " + processDefinition.getResourceName());
			processDefinitionList.add("id= " + processDefinition.getId() + ",name= " + processDefinition.getName()
			+ ",key= " + processDefinition.getKey() + ", resourceName=" + processDefinition.getResourceName());
		}
		return new Result<List<String>>("成功", 200, processDefinitionList);
	}

	/**
	 * 删除流程定义
	 */
	@PostMapping(value = "deleteProcessDefinition")
	@ApiOperation(value = "删除流程定义")
	public Result<String> deleteProcessDefinition(String deploymentId) {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
		return new Result<String>("成功", 200, "删除成功");
	}

	/**
	 * 查询历史任务
	 */
	@PostMapping(value = "queryHistoryTask")
	@ApiOperation(value = "查询历史任务")
	public Result<List<String>> queryHistoryTask() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().list();
		List<String> historicTaskInstanceList = new ArrayList<String>();
		
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println("任务ID： " + historicTaskInstance.getId());
			System.out.println("流程实例ID： " + historicTaskInstance.getProcessInstanceId());
			System.out.println("任务名字name： "+ historicTaskInstance.getName());
			historicTaskInstanceList.add("任务ID= " + historicTaskInstance.getId() + ", 流程实例ID = "+ historicTaskInstance.getProcessDefinitionId() 
			+ ", 任务名字name= "+ historicTaskInstance.getName());
		}
		return new Result<List<String>>("成功", 200, historicTaskInstanceList);
	}
	
	/**
	 * 完成任务
	 */
	@PostMapping(value = "completeGatewayTask")
	@ApiOperation(value = "完成网关任务")
	public Result<String> completeGatewayTask(@RequestBody GatewayTaskQuery gatewaytaskQuery) {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(gatewaytaskQuery.getTaskId(), gatewaytaskQuery.getVariables());
		System.out.println("完成任务。。。");
		return new Result<String>("成功", 200, "完成任务。。。");
	}
	
	

}
