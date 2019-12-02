package BusinessLogic;

import Services.RoomReservationService;

import java.util.ArrayList;

public class RoomServices {
    public static ArrayList<Integer> fetchRooms(String startTime, String endTime) {
        return RoomReservationService.getFreeRooms(startTime, endTime);
    }
}
