package BusinessLogic;

import DataManagers.PollDataHandler;
import DataManagers.PollOptionDataHandler;
import DataManagers.UserDataHandler;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.DuplicateVoteException;
import ErrorClasses.ObjectNotFoundInDBException;
import Models.Poll;
import Models.PollOption;
import Models.User;
import Services.EmailService;
import org.json.JSONArray;
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

    public static void addVote(JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException, AccessViolationException, DuplicateVoteException {
        int userID = data.getInt("userId");
        int pollID = data.getInt("pollId");
        int optionID = data.getInt("optionId");

        Poll poll = PollDataHandler.getPoll(pollID);
        PollOption option = PollOptionDataHandler.getPollOption(optionID);
        if (option == null || poll == null)
            throw new ObjectNotFoundInDBException();

        if(!poll.doesContaintOption(option.getId()))
            throw new AccessViolationException();
        if(!poll.isUserInvited(userID))
            throw new AccessViolationException();

        boolean status = option.addVote(userID);
        if(status == false)
            throw new DuplicateVoteException();
        PollOptionDataHandler.updateUserIDList(option);
    }

    public static void addParticipant(JSONObject data) throws JSONException, DataBaseErrorException, AccessViolationException {
        String userEmail = data.getString("userEmail");
        int pollId = data.getInt("pollId");
        int ownerId = data.getInt("userId");
        User owner = UserDataHandler.getUser(ownerId);
        User user = UserDataHandler.getUserByEmail(userEmail);
        Poll poll = PollDataHandler.getPoll(pollId);

        if (owner == null || user == null || poll == null)
            throw new DataBaseErrorException();

        if (!owner.didCreatedPoll(poll.getId()))
            throw new AccessViolationException();

        poll.addInvitedUser(user.getId());
        PollDataHandler.updateInvitedIds(poll);

        String content = "/api/poll/" + pollId;
        EmailService.sendMail(userEmail,content);

        user.addInvitedPollId(poll.getId());
        UserDataHandler.updateInvitedPollIds(user);

    }
    public static Poll createPoll(JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException {
        int userID = data.getInt("userID");
        User user = UserDataHandler.getUser(userID);
        if (user == null) {
            throw new ObjectNotFoundInDBException();
        }
        Poll poll = new Poll();
        poll.setTitle(data.getString("title"));
        poll.setOngoing(true);
        poll.setOwnerId(userID);

        ArrayList<PollOption> options = new ArrayList<>();
        JSONArray jOptions = data.getJSONArray("options");
        for(int i = 0; i < jOptions.length(); i++) {
            JSONObject jOption = jOptions.getJSONObject(i);
            options.add(new PollOption(jOption.getString("startTime"), jOption.getString("finishTime")));
        }
        poll.setOptions(options);

        if(!PollDataHandler.addPoll(poll))
            throw new DataBaseErrorException();
        user.addCreatedPollId(poll.getId());
        UserDataHandler.updateCreatedPollIds(user);
        return poll;
    }


    public static void unsetOngoingStatus(Poll poll) throws DataBaseErrorException {
        PollDataHandler.unsetOngoingStatus(poll.getId());
    }
}
