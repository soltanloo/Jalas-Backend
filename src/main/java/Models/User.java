package Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static int count = 0;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Integer> createdPollIds = new ArrayList<>();
    private List<Integer> invitedPollIds = new ArrayList<>();
    private List<Integer> createdMeetingIds = new ArrayList<>();
    private List<Integer> invitedMeetingIds = new ArrayList<>();
    private boolean isLoggedIn = false;
    private String password;
    private String role;

    public User() {
        id = count;
        count++;
    }


    public String getRole() { return role;}

    public void setRole(String role) { this.role = role;}

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public boolean isLoggedIn () {
        return isLoggedIn;
    }

    public void setLoggedIn (boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public List<Integer> getCreatedPollIds() {
        return createdPollIds;
    }

    public void setCreatedPollIds(List<Integer> createdPollIds) {
        this.createdPollIds = createdPollIds;
    }

    public void addCreatedPollId(int pollId) {createdPollIds.add(pollId);}


    public List<Integer> getInvitedPollIds() {
        return invitedPollIds;
    }

    public void setInvitedPollIds(List<Integer> invitedPollIds) {
        this.invitedPollIds = invitedPollIds;
    }

    public void addInvitedPollId(int pollId) {invitedPollIds.add(pollId);}


    public List<Integer> getCreatedMeetingIds() {
        return createdMeetingIds;
    }

    public void setCreatedMeetingIds(List<Integer> createdMeetingIds) {
        this.createdMeetingIds = createdMeetingIds;
    }

    public void addCreatedMeetingId(int meetingId) {createdMeetingIds.add(meetingId);}


    public List<Integer> getInvitedMeetingIds() {
        return invitedMeetingIds;
    }

    public void setInvitedMeetingIds(List<Integer> meetingIds) {
        this.invitedMeetingIds = meetingIds;
    }

    public void addInvitedMeetingId(int meetingId) {invitedMeetingIds.add(meetingId);}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean didCreatedPoll(int pollId) {
        for (int i = 0; i < createdPollIds.size(); i++) {
            if (pollId == createdPollIds.get(i))
                return true;
        }
        return false;
    }

    public void removeInvitedPollId(int pollId) {
        for (int i = 0; i < invitedPollIds.size(); i++) {
            if(invitedPollIds.get(i) == pollId) {
                invitedPollIds.remove(i);
                break;
            }
        }
    }
}
