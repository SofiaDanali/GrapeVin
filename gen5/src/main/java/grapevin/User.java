package grapevin;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private ArrayList<String> quizAnswers;

    public User() {
        this.username = null;
        this.password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getQuizAnswers() {
        return quizAnswers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {

    }
       
}
