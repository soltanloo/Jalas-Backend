package BusinessLogic;

import DataManagers.UserDataHandler;
import ErrorClasses.DataBaseErrorException;
import Models.User;

public class UserServices {
    public static void addUserCreatedPoll(int userId, int pollId) throws DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.addCreatedPollId(pollId);
        UserDataHandler.updateCreatedPollIds(user);
    }

    public static void addUserInvitedPoll(int userId, int pollId) throws DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.addInvitedPollId(pollId);
        UserDataHandler.updateInvitedPollIds(user);
    }

    public static void addUserCreatedMeeting(int userId, int meetingId) throws DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.addCreatedMeetingId(meetingId);
        UserDataHandler.updateCreatedMeetingIds(user);
    }

    public static void addUserInvitedMeeting(int userId, int meetingId) throws DataBaseErrorException {
        User user = UserDataHandler.getUser(userId);
        user.addInvitedMeetingId(meetingId);
        UserDataHandler.updateInvitedMeetingIds(user);
    }
}
