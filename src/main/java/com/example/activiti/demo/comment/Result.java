package com.example.activiti.demo.comment;


public class Result<T> {
	
	private String message;
	
	private Integer code;
	
	private T data = null;

	public Result(String message, Integer code, T data) {
		super();
		this.message = message;
		this.code = code;
		this.data = data;
	}
	
	

	public T getData() {
		return data;
	}



	public void setData(T data) {
		this.data = data;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	


}
