package Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static int count = 0;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Integer> meetingIds;


    public List<Integer> getMeetingIds() {
        return meetingIds;
    }

    public void setMeetingIds(List<Integer> meetingIds) {
        this.meetingIds = meetingIds;
    }

    public void addMeeting(Integer meetingId) {
        this.meetingIds.add(meetingId);
    }

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

}
