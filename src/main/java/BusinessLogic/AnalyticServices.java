package BusinessLogic;

import DataManagers.MeetingDataHandler;

public class AnalyticServices {
    int getReseverdRoomsNum(){
        return MeetingDataHandler.getSetMeetingsNum();
    }
    int getCanceledMeetingsNum(){
        return MeetingDataHandler.getCancelledMeetingsNum();
    }
    long getCreationMeanTime(){
        return MeetingDataHandler.getCreationMeanTime();
    }
}
