package BusinessLogic;

import DataManagers.MeetingDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.RoomReservationErrorException;
import Models.Meeting;
import Services.RoomReservationService;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MeetingServices {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

    public static Meeting addMeeting(JSONObject data) throws JSONException, RoomReservationErrorException, DataBaseErrorException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Meeting meeting = new Meeting();

        meeting.setRoomNumber(data.getInt("roomNumber"));
        meeting.setStartTime(data.getString("startTime"));
        meeting.setFinishTime(data.getString("finishTime"));
        meeting.setCreateTime(sdf.format(timestamp));

        if(MeetingDataHandler.addMeeting(meeting)) {
            if(RoomReservationService.reserveRoom(meeting.getRoomNumber(), "Juggernaut", meeting.getStartTime(), meeting.getFinishTime())) {
                MeetingDataHandler.setMeetingStatus(meeting);
                return meeting;
            }
            throw new RoomReservationErrorException();
        }
        else {
            throw new DataBaseErrorException();
        }
    }

    public static boolean cancelMeeting(String meetingId) {
        return MeetingDataHandler.cancelMeeting(Integer.parseInt(meetingId));
    }

    public static Meeting getMeeting(String id) throws DataBaseErrorException {
        return MeetingDataHandler.getMeeting(Integer.parseInt(id));
    }
}
