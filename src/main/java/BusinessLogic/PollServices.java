package BusinessLogic;

import DataManagers.PollDataHandler;
import DataManagers.PollOptionDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.ObjectNotFoundInDBException;
import Models.Poll;
import Models.PollOption;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PollServices {

    public static ArrayList<Poll> getAllPolls() throws DataBaseErrorException {
        return PollDataHandler.getAllPols();
    }

    public static Poll getPoll(int id) throws DataBaseErrorException {
        return PollDataHandler.getPoll(id);
    }

    public static void addVote(JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException {
        int userID = data.getInt("userID");
        int optionID = data.getInt("optionID");

        PollOption option = PollOptionDataHandler.getPollOption(optionID);
        if (option == null)
            throw new ObjectNotFoundInDBException();

        option.addVote(userID);
        PollOptionDataHandler.updateUserIDList(option);
    }

    public static void unsetOngoingStatus(Poll poll) throws DataBaseErrorException {
        PollDataHandler.unsetOngoingStatus(poll.getId());
    }
}
