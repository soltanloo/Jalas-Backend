package BusinessLogic;

import DataManagers.MeetingDataHandler;
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
    public static long getCreationMeanTime(){
        return MeetingDataHandler.getCreationMeanTime();
    }
}
