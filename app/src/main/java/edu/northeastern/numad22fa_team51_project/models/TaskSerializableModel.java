package edu.northeastern.numad22fa_team51_project.models;


import java.io.Serializable;
import java.util.ArrayList;

public class TaskSerializableModel implements Serializable {

    private String board_id;
    private String card_name;
    private String card_notes;
    private String createdBy;
    private ArrayList<String> assignedTo;
    private String DueDate;

    public TaskSerializableModel(String board_id, String card_name, String card_notes, String createdBy, ArrayList<String> assignedTo, String dueDate) {
        this.board_id = board_id;
        this.card_name = card_name;
        this.card_notes = card_notes;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.DueDate = dueDate;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_notes() {
        return card_notes;
    }

    public void setCard_notes(String card_notes) {
        this.card_notes = card_notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<String> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(ArrayList<String> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }
}
