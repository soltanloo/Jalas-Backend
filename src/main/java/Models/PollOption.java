package Models;

import java.util.ArrayList;

public class PollOption {

    private ArrayList<String> userList = new ArrayList<>();
    private String time;


    public PollOption(String time) {
        this.time = time;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
