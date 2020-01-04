package BusinessLogic;

import DataManagers.PollDataHandler;
import DataManagers.PollOptionDataHandler;
import DataManagers.UserDataHandler;
import ErrorClasses.*;
import Models.Poll;
import Models.PollOption;
import Models.User;
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

    public static void addVote(int userId, JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException, AccessViolationException, DuplicateVoteException, PollFinishedException {
        int pollID = data.getInt("pollId");
        int optionID = data.getInt("optionId");

        Poll poll = PollDataHandler.getPoll(pollID);
        PollOption option = PollOptionDataHandler.getPollOption(optionID);
        checkPreConstraintsForAddVote(userId, poll, option);

        boolean status = option.addVote(userId);
        if(!status)
            throw new DuplicateVoteException();

        removePreviousVote(userId, poll);
        PollOptionDataHandler.updateUserIDList(option);
        UserServices.notifyNewVote(userId, pollID);
    }

    public static void removePreviousVote(int userId, Poll poll) throws DataBaseErrorException {
        PollOption prevOption = poll.getUserVotedOption(userId);
        if (prevOption != null) {
            prevOption.removeVotedUser(userId);
            PollOptionDataHandler.updateUserIDList(prevOption);
        }
    }

    public static void checkPreConstraintsForAddVote(int userId, Poll poll, PollOption option) throws ObjectNotFoundInDBException, PollFinishedException, AccessViolationException {
        if (option == null || poll == null)
            throw new ObjectNotFoundInDBException();

        if(!poll.isOngoing())
            throw new PollFinishedException();
        if(!poll.doesContainOption(option.getId()))
            throw new AccessViolationException();
        if(!poll.isUserInvited(userId))
            throw new AccessViolationException();
    }

    public static void addParticipant(int userId, JSONObject data) throws JSONException, DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        String userEmail = data.getString("userEmail");
        int pollId = data.getInt("pollId");
        User owner = UserDataHandler.getUser(userId);
        User user = UserDataHandler.getUserByEmail(userEmail);
        Poll poll = PollDataHandler.getPoll(pollId);

        checkPreConstraintsForAddParticipants(owner, user, poll);

        poll.addInvitedUser(user.getId());
        PollDataHandler.updateInvitedIds(poll);

        UserServices.notifyAddedToPoll(userEmail, pollId);

        user.addInvitedPollId(poll.getId());
        UserDataHandler.updateInvitedPollIds(user);

    }

    public static void checkPreConstraintsForAddParticipants(User owner, User user, Poll poll) throws DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        if (owner == null || user == null || poll == null)
            throw new DataBaseErrorException();

        if (!owner.didCreatedPoll(poll.getId()))
            throw new AccessViolationException();

        if (!poll.isOngoing())
            throw new PollAlreadyClosedException();
    }

    public static void removeParticipant(int userId, JSONObject data) throws DataBaseErrorException, AccessViolationException, JSONException, UserWasNotInvitedException, PollAlreadyClosedException {
        String userEmail = data.getString("userEmail");
        int pollId = data.getInt("pollId");
        User owner = UserDataHandler.getUser(userId);
        User user = UserDataHandler.getUserByEmail(userEmail);
        Poll poll = PollDataHandler.getPoll(pollId);

        checkPreConstraintsForRemoveParticipant(owner, user, poll);

        poll.removeInvitedUser(user.getId());
        PollDataHandler.updateInvitedIds(poll);

        UserServices.notifyRemovedFromPoll(userEmail, pollId);
        user.removeInvitedPollId(poll.getId());
        UserDataHandler.updateInvitedPollIds(user);
    }

    public static void checkPreConstraintsForRemoveParticipant(User owner, User user, Poll poll) throws DataBaseErrorException, AccessViolationException, UserWasNotInvitedException, PollAlreadyClosedException {
        if (owner == null || user == null || poll == null)
            throw new DataBaseErrorException();

        if (!owner.didCreatedPoll(poll.getId()))
            throw new AccessViolationException();

        if(!poll.isUserInvited(user.getId()))
            throw new UserWasNotInvitedException();

        if (!poll.isOngoing())
            throw new PollAlreadyClosedException();
    }


    public static Poll createPoll(int userId, JSONObject data) throws JSONException, DataBaseErrorException, ObjectNotFoundInDBException {
        User user = UserDataHandler.getUser(userId);
        if (user == null) {
            throw new ObjectNotFoundInDBException();
        }

        Poll poll = createNewPoll(userId, data);
        addPollOptions(data, poll);

        if(!PollDataHandler.addPoll(poll))
            throw new DataBaseErrorException();
        user.addCreatedPollId(poll.getId());
        UserDataHandler.updateCreatedPollIds(user);

        UserServices.notifyNewPollCreated(user, poll.getId());
        return poll;
    }

    public static void addPollOptions(JSONObject data, Poll poll) throws JSONException {
        ArrayList<PollOption> options = new ArrayList<>();
        JSONArray jOptions = data.getJSONArray("options");
        for(int i = 0; i < jOptions.length(); i++) {
            JSONObject jOption = jOptions.getJSONObject(i);
            options.add(new PollOption(jOption.getString("startTime"), jOption.getString("finishTime")));
        }
        poll.setOptions(options);
    }

    public static Poll createNewPoll(int userId, JSONObject data) throws JSONException {
        Poll poll = new Poll();
        poll.setTitle(data.getString("title"));
        poll.setOngoing(true);
        poll.setOwnerId(userId);
        poll.setCreationTime(data.getInt("creationTime"));
        return poll;
    }

    public static Poll addOptionToPoll(int userId, JSONObject data) throws JSONException, DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        int pollId = data.getInt("pollId");
        User owner = UserDataHandler.getUser(userId);
        Poll poll = PollDataHandler.getPoll(pollId);

        checkPreConstraintsForPoll(owner, poll);

        PollOption option = new PollOption(data.getString("startTime"), data.getString("finishTime"));
        poll.addOption(option);
        PollOptionDataHandler.addOption(option);
        PollDataHandler.updateOptions(poll);
        UserServices.notifyNewOption(poll.getInvitedUserIds(), poll.getId());
        return poll;
    }

    public static void checkPreConstraintsForPoll(User owner, Poll poll) throws DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        if (owner == null || poll == null)
            throw new DataBaseErrorException();

        if (!owner.didCreatedPoll(poll.getId()))
            throw new AccessViolationException();

        if (!poll.isOngoing())
            throw new PollAlreadyClosedException();
    }

    public static Poll removeOptionFromPoll(int userId, JSONObject data) throws JSONException, DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        int pollId = data.getInt("pollId");
        int pollOptionId = data.getInt("optionId");
        User owner = UserDataHandler.getUser(userId);
        Poll poll = PollDataHandler.getPoll(pollId);

        checkPreConstraintsForRemoveOptionFromPoll(pollOptionId, owner, poll);

        UserServices.notifyDeletedOption(poll.getPollOptionUsers(pollOptionId), poll.getId());
        poll.removeOption(pollOptionId);
        PollOptionDataHandler.removeOption(pollOptionId);
        PollDataHandler.updateOptions(poll);

        return poll;
    }

    public static void checkPreConstraintsForRemoveOptionFromPoll(int pollOptionId, User owner, Poll poll) throws DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        if (owner == null || poll == null)
            throw new DataBaseErrorException();

        if (!owner.didCreatedPoll(poll.getId()))
            throw new AccessViolationException();

        if (!poll.doesContainOption(pollOptionId))
            throw new AccessViolationException();

        if (!poll.isOngoing())
            throw new PollAlreadyClosedException();
    }


    public static void unsetOngoingStatus(Poll poll) throws DataBaseErrorException {
        PollDataHandler.unsetOngoingStatus(poll.getId());
    }

    public static ArrayList<Poll> getUserPolls(int userId) throws DataBaseErrorException {
        User user = UserServices.getUser(userId);

        ArrayList<Poll> polls = new ArrayList<>();
        for (int pollId : user.getCreatedPollIds())
            polls.add(getPoll(pollId));

        for (int pollId : user.getInvitedPollIds())
            polls.add(getPoll(pollId));

        return polls;
    }

    public static void closePoll(int userId, int pollId) throws DataBaseErrorException, AccessViolationException, PollAlreadyClosedException {
        User owner = UserDataHandler.getUser(userId);
        Poll poll = PollDataHandler.getPoll(pollId);

        checkPreConstraintsForPoll(owner, poll);

        PollDataHandler.unsetOngoingStatus(poll.getId());

        UserServices.notifyPollClosed(poll.getInvitedUserIds(), poll.getId());
    }
}
