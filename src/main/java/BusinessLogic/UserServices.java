package BusinessLogic;

import DataManagers.UserDataHandler;
import ErrorClasses.DataBaseErrorException;
import Models.User;
import Services.EmailService;

import java.util.ArrayList;

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

    public static User getUser(int id) throws DataBaseErrorException {
        return UserDataHandler.getUser(id);
    }

    public static String getUserEmail(int id) throws DataBaseErrorException {
        return UserDataHandler.getUserEmail(id);
    }

    public static void notifyNewOption(ArrayList<Integer> userIds, Integer pollId) throws DataBaseErrorException {
        for (Integer id : userIds) {
            String email = getUserEmail(id);
            String content = "New option for one of participating polls was added :\n";
            content += "http://localhost:8080/api/poll/" + pollId;
            EmailService.sendMail(email, content);
        }
    }

    public static void notifyNewVote(int userId, int pollId) throws DataBaseErrorException {
        String email = getUserEmail(userId);
        String content = "Your vote has been added to Poll with pollID " + pollId;
        content += "http://localhost:8080/api/poll/" + pollId;
        EmailService.sendMail(email, content);
    }
}
