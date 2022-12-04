package edu.northeastern.numad22fa_team51_project.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardSerializable implements Serializable {

    private String group_name = "";
    private String group_image = "";
    private String group_creadedBy = "";
    private ArrayList<String> group_assingedTo = new ArrayList<>();
    private String documentId = "";
    public ArrayList<Task> taskList = new ArrayList<>();

    public BoardSerializable(String group_name, String group_image, String group_creadedBy, ArrayList<String> group_assingedTo, String documentId) {
        this.group_name = group_name;
        this.group_image = group_image;
        this.group_creadedBy = group_creadedBy;
        this.group_assingedTo = group_assingedTo;
        this.documentId = documentId;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_image() {
        return group_image;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    public String getGroup_creadedBy() {
        return group_creadedBy;
    }

    public void setGroup_creadedBy(String group_creadedBy) {
        this.group_creadedBy = group_creadedBy;
    }

    public ArrayList<String> getGroup_assingedTo() {
        return group_assingedTo;
    }

    public void setGroup_assingedTo(ArrayList<String> group_assingedTo) {
        this.group_assingedTo = group_assingedTo;
    }
}
