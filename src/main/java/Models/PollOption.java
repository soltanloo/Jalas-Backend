package Models;

import ErrorClasses.DuplicateVoteException;

import java.util.ArrayList;

public class PollOption {
    private static int count = 0;
    private int id;
    private ArrayList<Integer> userList = new ArrayList<>();
    private ArrayList<Integer> userAgreeIfNeeded = new ArrayList<>();
    private String startTime;
    private String finishTime;


    public PollOption(String startTime, String finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.id = count++;
    }


    public ArrayList<Integer> getUserAgreeIfNeeded() {
        return userAgreeIfNeeded;
    }

    public void setUserAgreeIfNeeded(ArrayList<Integer> userAgreeIfNeeded) {
        this.userAgreeIfNeeded = userAgreeIfNeeded;
    }

    public void addUserAgreeIfNeeded(int userId) {
        this.userAgreeIfNeeded.add(userId);
    }

    public boolean hasUserAgreedIfNeeded(int userId) {
        for (Integer id : userAgreeIfNeeded)
            if (id == userId)
                return true;
        return false;
    }

    public void removeUserAgreeIfNeeded(int userId) {
        for (int i = 0; i < userAgreeIfNeeded.size(); i++)
            if (userAgreeIfNeeded.get(i) == userId)
                userAgreeIfNeeded.remove(i);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<Integer> userList) {
        this.userList = userList;
    }

    public boolean addVote(int userID) {
        if(userList.contains(userID))
            return false;
        this.userList.add(userID);
        return true;
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

    public void removeVotedUser(int userId) {
        for (int i = 0; i < userList.size(); i++)
            if (userList.get(i) == userId)
                userList.remove(i);
    }
}
