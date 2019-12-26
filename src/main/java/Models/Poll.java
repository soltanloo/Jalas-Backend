package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll {
    private static int count = 0;
    private int id;
    private String title;
    private ArrayList<PollOption> options = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private boolean isOngoing;
    private int ownerId;
    private ArrayList<Integer> invitedUserIds = new ArrayList<>();

    public Poll(){
        this.id = count++;
    }


    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

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

    public void addInvitedUser(int invitedUserId){this.invitedUserIds.add(invitedUserId);}

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

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getOptionsIDs() {
        ArrayList<Integer> retList = new ArrayList<>();

        for(PollOption pollOption : this.options)
            retList.add(pollOption.getId());

        return retList;
    }

    public ArrayList<Integer> getCommentIds() {
        ArrayList<Integer> retList = new ArrayList<>();

        for (Comment comment : this.comments)
            retList.add(comment.getId());

        return retList;
    }

    public boolean doesContaintOption(int optionId) {
        for (PollOption po : options) {
            if(po.getId() == optionId)
                return true;
        }
        return false;
    }

    public boolean isUserInvited(int userID) {
        if (userID == ownerId)
            return true;
        for (int id : invitedUserIds) {
            if(id == userID)
                return true;
        }
        return false;
    }

    public void removeInvitedUser(int userId) {
        for (int i = 0; i < invitedUserIds.size(); i++) {
            if(invitedUserIds.get(i) == userId) {
                invitedUserIds.remove(i);
                break;
            }
        }
    }

    public void removeOption(int optionId) {
        for (int i = 0; i < options.size(); i++)
            if (options.get(i).getId() == optionId) {
                options.remove(i);
                break;
            }
    }

    public boolean doesContaintComment(int commentId) {
        for (Comment comment : comments)
            if (comment.getId() == commentId)
                return true;
        return false;
    }
}
