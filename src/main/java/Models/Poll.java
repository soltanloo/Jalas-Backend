package Models;

import java.util.HashMap;
import java.util.List;

public class Poll {
    private static int count = 0;
    private int id;
    private String title;
    private HashMap<String, List<Vote>> votesList;
    private String chosenTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, List<Vote>> getVotesList() {
        return votesList;
    }

    public void setVotesList(HashMap<String, List<Vote>> votesList) {
        this.votesList = votesList;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public Poll(){
        this.id = count++;
    }

    public int getId(){
        return id;
    }



}
