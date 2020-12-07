package com.example.activiti.demo.action;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestAction {
	
	@GetMapping(value = "/helloworld")
	public String test1(@RequestParam(value = "name") String name) {
		return "hello " + name;
	}
	
	

}
