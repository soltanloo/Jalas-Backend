package BusinessLogic;

import DataManagers.NotificationDataHandler;
import DataManagers.PollDataHandler;
import DataManagers.UserDataHandler;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.PollAlreadyClosedException;
import Models.Poll;
import Models.User;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationServices {
    public static void manageNewOption(int userId, boolean notifyNewOption) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyNewOptionOn(notifyNewOption);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageNewVote(int userId, boolean notifyNewVote) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyNewVoteOn(notifyNewVote);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageDeletedOption(int userId, boolean notifyDeletedVote) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyDeletedOptionOn(notifyDeletedVote);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageNewPollCreation(int userId, boolean notifyNewPollCreation) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyNewPollCreatedOn(notifyNewPollCreation);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageAddedToPoll(int userId, boolean notifyAddedToPoll) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyAddedToPollOn(notifyAddedToPoll);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageRemovedFromPoll(int userId, boolean notifyRemovedFromPoll) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyRemovedFromPollOn(notifyRemovedFromPoll);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void managePollClosed(int userId, boolean notifyManagePollClosed) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyPollClosedOn(notifyManagePollClosed);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageNewMeeting(int userId, boolean notifyNewMeeting) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyNewMeetingOn(notifyNewMeeting);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageCanceledMeeting(int userId, boolean notifyCanceledMeeting) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyCanceledMeetingOn(notifyCanceledMeeting);
        NotificationDataHandler.updateNotifSettings(user);
    }
    public static void manageMentionInComment(int userId, boolean notifyMentionInComment) throws JSONException, DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.setNotifyMentionInCommentOn(notifyMentionInComment);
        NotificationDataHandler.updateNotifSettings(user);
    }
}

