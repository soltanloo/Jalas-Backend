package BusinessLogic;

import DataManagers.CommentDataHandler;
import DataManagers.MeetingDataHandler;
import DataManagers.PollDataHandler;
import DataManagers.UserDataHandler;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import Models.User;

public class AnalyticServices {
    public static int getReseverdRoomsNum(int userId) throws DataBaseErrorException, AccessViolationException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return MeetingDataHandler.getSetMeetingsNum();
    }
    public static int getCanceledMeetingsNum(int userId) throws AccessViolationException, DataBaseErrorException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return MeetingDataHandler.getCancelledMeetingsNum();
    }

    public static int getAllPollsNum(int userId) throws DataBaseErrorException, AccessViolationException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return PollDataHandler.getNumOfRows();
    }

    public static int getAllCommentsNum(int userId) throws DataBaseErrorException, AccessViolationException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return CommentDataHandler.getNumOfRows();
    }

    public static int getAllUsersNum(int userId) throws DataBaseErrorException, AccessViolationException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return UserDataHandler.getNumOfRows();
    }


    public static int getAllMeetingsNum(int userId) throws DataBaseErrorException, AccessViolationException {
        User admin = UserDataHandler.getUser(userId);
        if(admin.getRole().equalsIgnoreCase("admin") == false)
            throw new AccessViolationException();
        return MeetingDataHandler.getNumOfRows();
    }

    public static long getCreationMeanTime(){
        return MeetingDataHandler.getCreationMeanTime();
    }

}
