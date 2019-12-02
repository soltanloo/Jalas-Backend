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
}
