package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll {
    private static int count = 0;
    private int id;
    private String title;
    private ArrayList<PollOption> options;
    private boolean isOngoing;
    private int ownerId;
    private ArrayList<Integer> invitedUserIds = new ArrayList<>();


    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<Integer> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(ArrayList<Integer> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    public ArrayList<PollOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<PollOption> options) {
        this.options = options;
    }

    public void addOption(PollOption option) {
        this.options.add(option);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Poll(){
        this.id = count++;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getOptionsIDs() {
        ArrayList<Integer> retList = new ArrayList<>();

        for(PollOption pollOption : this.options) {
            retList.add(pollOption.getId());
        }
        return retList;
    }

}
