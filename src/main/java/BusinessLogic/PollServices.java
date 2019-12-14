package BusinessLogic;

import DataManagers.PollDataHandler;
import DataManagers.PollOptionDataHandler;
import DataManagers.UserDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.ObjectNotFoundInDBException;
import Models.Poll;
import Models.PollOption;
import Models.User;
import Services.EmailService;
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

    public static void addParticipant(JSONObject data) throws JSONException, DataBaseErrorException {
        String userEmail = data.getString("userEmail");
        int pollId = data.getInt("pollId");
        int userId = data.getInt("userId");
        User user = UserDataHandler.getUser(userId);
        Poll poll = PollDataHandler.getPoll(pollId);
        poll.addInvitedUser(userId);
        String content = "/api/poll" + pollId;
        EmailService.sendMail(userEmail,content);
        UserDataHandler.updateInvitedPollIds(user);

    }
    public static void createPoll(JSONObject data) throws JSONException, DataBaseErrorException{
        int userID = data.getInt("userID");
        User user = UserDataHandler.getUser(userID);
        Poll poll = new Poll();
        poll.setTitle(data.getString("title"));
        poll.setOngoing(true);
        poll.setOwnerId(userID);
        if(PollDataHandler.addPoll(poll) == false)
            throw new DataBaseErrorException();
        UserDataHandler.updateCreatedPollIds(user);
    }


    public static void unsetOngoingStatus(Poll poll) throws DataBaseErrorException {
        PollDataHandler.unsetOngoingStatus(poll.getId());
    }
}
