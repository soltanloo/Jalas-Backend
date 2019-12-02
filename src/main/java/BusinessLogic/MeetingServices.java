package BusinessLogic;

import DataManagers.MeetingDataHandler;
import Models.Meeting;
import org.json.JSONException;
import org.json.JSONObject;

public class MeetingServices {
    public static int addMeeting(JSONObject data) throws JSONException {
        Meeting meeting = new Meeting();
        if(!data.isNull("roomNumber")) {
            meeting.setRoomNumber(data.getInt("roomNumber"));
        }

        meeting.setStartTime(data.getString("startTime"));
        meeting.setFinishTime(data.getString("finishTime"));
        if(MeetingDataHandler.addMeeting(meeting)) {
            return meeting.getId();
        }
        else {
            return -1;
        }
    }
}
