package edu.northeastern.numad22fa_team51_project.models;


import java.io.Serializable;

public class Task implements Serializable {

    private String title;
    private String creadtedBy;

    public Task(String title, String creadtedBy) {
        this.title = title;
        this.creadtedBy = creadtedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreadtedBy() {
        return creadtedBy;
    }

    public void setCreadtedBy(String creadtedBy) {
        this.creadtedBy = creadtedBy;
    }


}
