package Configs;

import DataManagers.MeetingDataHandler;
import Models.Meeting;
import Services.RoomReservationService;

import java.util.List;

public class PeriodRoomCheck implements Runnable {
    @Override
    public void run () {
        try {
            System.out.println("stalled meetings check");
            List<Meeting> stalledMeetings = MeetingDataHandler.getStalledMeetings();

            for (Meeting meeting : stalledMeetings) {
                if(RoomReservationService.reserveRoom(meeting.getRoomNumber(), "Juggernaut", meeting.getStartTime(), meeting.getFinishTime())) {
                    MeetingDataHandler.setMeetingStatus(meeting);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
