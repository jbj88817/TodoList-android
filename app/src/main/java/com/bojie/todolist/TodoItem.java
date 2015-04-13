package com.bojie.todolist;

/**
 * Created by bojiejiang on 4/13/15.
 */
public class TodoItem {

    private int id;
    private String body;
    private int priority;

    public TodoItem(String body, int priority) {
        this.body = body;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
