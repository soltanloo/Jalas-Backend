package BusinessLogic;

import DataManagers.PollDataHandler;
import Models.Poll;

import java.util.ArrayList;

public class PollServices {

    public static ArrayList<Poll> getAllPolls() {
        return PollDataHandler.getAllPols();
    }

    public static Poll getPoll(int id) {
        return PollDataHandler.getPoll(id);
    }
}
