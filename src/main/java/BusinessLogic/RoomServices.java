package BusinessLogic;

import DataManagers.MeetingDataHandler;
import Models.Meeting;
import Services.RoomReservationService;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomServices {
    public static ArrayList<Integer> fetchRooms(String startTime, String endTime) {
        return RoomReservationService.getFreeRooms(startTime, endTime);
    }

    public static boolean reserveRoom(JSONObject data) throws JSONException {
        int roomId = data.getInt("roomId");
        int meetingId = data.getInt("meetingId");
        Meeting meeting = MeetingDataHandler.getMeeting(meetingId);

        if(RoomReservationService.reserveRoom(roomId, "Juggernaut", meeting.getStartTime(), meeting.getFinishTime())) {
            MeetingDataHandler.updateRoomNumber(roomId, meetingId);
            return true;
        }
        else {
            return false;
        }
    }
}
