package edu.northeastern.numad22fa_team51_project.models;

public class UserModel {
    private String user_email;
    private String user_id;
    private String user_name;
    private String user_passwd;

    public UserModel() {
    }

    public UserModel(String user_email, String user_id, String user_name, String user_passwd) {
        this.user_email = user_email;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_passwd = user_passwd;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_passwd() {
        return user_passwd;
    }

    public void setUser_passwd(String user_passwd) {
        this.user_passwd = user_passwd;
    }
}