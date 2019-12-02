package BusinessLogic;

import DataManagers.MeetingDataHandler;
import Models.Meeting;
import Services.RoomReservationService;
import org.json.JSONException;
import org.json.JSONObject;

public class MeetingServices {
    public static Meeting addMeeting(JSONObject data) throws JSONException {
        Meeting meeting = new Meeting();

        meeting.setRoomNumber(data.getInt("roomNumber"));
        meeting.setStartTime(data.getString("startTime"));
        meeting.setFinishTime(data.getString("finishTime"));

        if(MeetingDataHandler.addMeeting(meeting)) {
//            if(RoomReservationService.reserveRoom(meeting.getRoomNumber(), "Juggernaut", meeting.getStartTime(), meeting.getFinishTime())) {
//
//            }

            return meeting;
        }
        else {
            return null;
        }
    }
}
