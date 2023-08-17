package com.example.taskmanagement.model;

public class TaskModel {
    String taskId, taskName, taskStatus, userID;
    public TaskModel(){}

    public TaskModel(String taskId, String taskName, String taskStatus, String userID) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.userID = userID;
    }

    public void setUserID(String userID){ this.userID = userID;}
    public String getUserID(){return userID;}
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
