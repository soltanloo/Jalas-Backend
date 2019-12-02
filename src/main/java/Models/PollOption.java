package Models;

import java.util.ArrayList;

public class PollOption {
    private static int count = 0;
    private int id;
    private ArrayList<String> userList = new ArrayList<>();
    private String startTime;
    private String finishTime;


    public PollOption(String startTime, String finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.id = count++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
