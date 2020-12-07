package com.example.activiti.demo.action;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activiti.demo.comment.Result;

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
					.addClasspathResource("bpmn/leave.bpmn")
					.addClasspathResource("bpmn/leave.png")
					.deploy();
			//获取仓库服务的实例
			System.out.println(deploy.getId() + "   " + deploy.getName());
			return new Result<String>("成功",200, "");

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return new Result<String>("失败",500, "");
		}
	}
	
	/**
	 * 启动流程实例
	 */
	@PostMapping(value = "startProcess")
	@ApiOperation(value = "启动流程实例")
	public Result<ProcessInstance> startProcess() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey("myProcess");
		System.out.println("pid: "+ processInstance.getId() + "  activityId: " + processInstance.getActivityId());
		return new Result<ProcessInstance>("成功", 200, processInstance);
	}
	
	/**
	 * 查看任务
	 */
	@PostMapping(value = "queryMyTask")
	@ApiOperation(value = "查看任务")
	public Result<List<Task>> queryMyTask() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> list = processEngine.getTaskService().createTaskQuery().list();
		for (Task task : list) {
			System.out.println("taskId: " + task.getId() + " taskName: " + task.getName());
		}
		return new Result<List<Task>>("成功", 200, list);
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
	public Result<List<ProcessDefinition>> queryProcessDefinition() {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println("id: " + processDefinition.getId());
			System.out.println("name: " + processDefinition.getName());
			System.out.println("key: " + processDefinition.getKey());
			System.out.println("resourceName: " + processDefinition.getResourceName());
		}
		return new Result<List<ProcessDefinition>>("成功", 200, list);
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
	public Result<String> queryHistoryTask(String deploymentId) {
		// 获取流程引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<HistoricTaskInstance> list = processEngine.getHistoryService().createHistoricTaskInstanceQuery().list();
		for (HistoricTaskInstance historicTaskInstance : list) {
			System.out.println("任务ID： " + historicTaskInstance.getId());
			System.out.println("流程实例ID： " + historicTaskInstance.getProcessInstanceId());
			System.out.println("************************");
		}
		return new Result<String>("成功", 200, "");
	}

}
