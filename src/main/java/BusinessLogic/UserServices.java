package BusinessLogic;

import DataManagers.DataManager;
import DataManagers.UserDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.InvalidEmailAddressException;
import ErrorClasses.NoSuchUsernameException;
import ErrorClasses.WrongPasswordException;
import Models.User;
import Services.EmailService;
import Services.MD5Service;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void notifyDeletedOption(ArrayList<Integer> userIds, Integer pollId) throws DataBaseErrorException {
        String content = "An Option for one of the participating polls was Deleted :\n";
        content += "http://localhost:8080/api/poll/" + pollId;
        for (Integer id : userIds)
            EmailService.sendMail(getUserEmail(id), content);
    }

    public static void notifyNewVote(int userId, int pollId) throws DataBaseErrorException {
        String email = getUserEmail(userId);
        String content = "Your vote has been added to Poll with pollID " + pollId;
        content += "http://localhost:8080/api/poll/" + pollId;
        EmailService.sendMail(email, content);
    }

    public static void notifyNewPollCreated(User user, int pollId) {
        String content = "You have created a new poll with ID = " + pollId;
        content += "\nhttp://localhost:8080/api/poll/" + pollId;
        EmailService.sendMail(user.getEmail(), content);
    }

    public static User signIn (JSONObject data) throws NoSuchUsernameException, WrongPasswordException, JSONException {
        String email = data.getString("email");
        User user;
        try {
            user = UserDataHandler.getUserByEmail(email);
            if (user == null)
                throw new NoSuchUsernameException();
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            throw new NoSuchUsernameException();
        }

        String password = MD5Service.changeToMd5(data.getString("password"));
        if (isPasswordCorrect(email, password)) {
            userLogin(email);
        } else
            throw new WrongPasswordException();
        return user;
    }

    public static String getUserName(int userId) throws DataBaseErrorException {
        return UserDataHandler.getUserName(userId);
    }

    private static boolean isPasswordCorrect (String email, String password) {
        return UserDataHandler.checkPasswordCorrectness(email, password);
    }

    private static void userLogin (String email) {
        UserDataHandler.userLogin(email);
    }

    public static void notifyAddedToPoll(String userEmail, int pollId) {
        String content = "You have been added to a new poll!\n";
        content += "http://localhost:8080/api/poll/" + pollId;
        EmailService.sendMail(userEmail,content);
    }

    public static void notifyRemovedFromPoll(String userEmail, int pollId) {
        String content = "You have been removed from poll with ID : " + pollId;
        EmailService.sendMail(userEmail,content);
    }

    public static void notifyPollClosed(ArrayList<Integer> userIds, int pollId) throws DataBaseErrorException {
        String content = "One of the polls that you are invited has been closed :\n";
        content += "http://localhost:8080/api/poll/" + pollId;
        for (Integer id : userIds)
            EmailService.sendMail(getUserEmail(id), content);
    }

    public static void notifyNewMeeting(ArrayList<Integer> userIds, int meetingId) throws DataBaseErrorException {
        String content = "New Meeting has been arranged!\n" +
                "api/meeting/" + meetingId;
        for(int userID : userIds) {
            EmailService.sendMail(getUserEmail(userID), content);
        }
    }

    public static void notifyCanceledMeeting(ArrayList<Integer> userIds, int meetingId) throws DataBaseErrorException {
        String content = "Meeting has been canceled\n" +
                "api/meeting/" + meetingId;
        for(int userID : userIds) {
            EmailService.sendMail(getUserEmail(userID), content);
        }
    }

    public static void inviteUsersToMeeting(ArrayList<Integer> invitedUserIds, int meetingId) throws DataBaseErrorException {
        for (int invUserId : invitedUserIds) {
            addUserInvitedMeeting(invUserId, meetingId);
        }
        notifyNewMeeting(invitedUserIds, meetingId);
    }
}
