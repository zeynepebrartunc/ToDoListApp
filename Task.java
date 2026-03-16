package com.example.todolistapp;

public class Task {
    private String name;
    private boolean done;

    public Task(String name, boolean done){
        this.name = name;
        this.done = done;
    }

    public String getName() { return name; }
    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }
}
