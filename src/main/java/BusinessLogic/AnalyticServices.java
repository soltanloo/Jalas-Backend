package BusinessLogic;

import DataManagers.MeetingDataHandler;

public class AnalyticServices {
    public static int getReseverdRoomsNum(){
        return MeetingDataHandler.getSetMeetingsNum();
    }
    public static int getCanceledMeetingsNum(){
        return MeetingDataHandler.getCancelledMeetingsNum();
    }
    public static long getCreationMeanTime(){
        return MeetingDataHandler.getCreationMeanTime();
    }
}
