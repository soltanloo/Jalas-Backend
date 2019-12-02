package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll {
    private static int count = 0;
    private int id;
    private String title;
    private ArrayList<PollOption> options;


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



}
