package com.canyapan.blog.taskmanager;

public class TaskDatabaseException extends Exception {
	private static final long serialVersionUID = 3482729788180558708L;

	public TaskDatabaseException(String message) {
		super(message);
	}
	
	public TaskDatabaseException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
