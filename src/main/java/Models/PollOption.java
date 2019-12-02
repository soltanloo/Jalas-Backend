package Models;

import java.util.ArrayList;

public class PollOption {
    private static int count = 0;
    private int id;
    private ArrayList<String> userList = new ArrayList<>();
    private String timeOption;


    public PollOption(String timeOption) {
        this.timeOption = timeOption;
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

    public String getTimeOption() {
        return timeOption;
    }

    public void setTimeOption(String timeOption) {
        this.timeOption = timeOption;
    }
}
