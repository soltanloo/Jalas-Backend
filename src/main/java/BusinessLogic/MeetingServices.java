package BusinessLogic;

import DataManagers.MeetingDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.PollFinishedException;
import ErrorClasses.RoomReservationErrorException;
import Models.Meeting;
import Models.Poll;
import Services.EmailService;
import Services.RoomReservationService;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MeetingServices {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

    public static Meeting  addMeeting(JSONObject data) throws JSONException, RoomReservationErrorException, DataBaseErrorException, PollFinishedException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Poll poll = PollServices.getPoll(data.getInt("pollId"));

        if (!poll.isOngoing())
            throw new PollFinishedException();
        Meeting meeting = new Meeting();

        meeting.setRoomNumber(data.getInt("roomNumber"));
        meeting.setStartTime(data.getString("startTime"));
        meeting.setFinishTime(data.getString("finishTime"));
        meeting.setTitle(poll.getTitle());
        meeting.setOwnerId(poll.getOwnerId());
        meeting.setInvitedUserIds(poll.getInvitedUserIds());
        meeting.setCreateTime(sdf.format(timestamp));


        if(MeetingDataHandler.addMeeting(meeting)) {
            UserServices.addUserCreatedMeeting(poll.getOwnerId(), meeting.getId());
            PollServices.unsetOngoingStatus(poll);

            for (int userId : poll.getInvitedUserIds()) {
                UserServices.addUserInvitedMeeting(userId, meeting.getId());
            }
            String content = "New Meeting : \n" +
                    "api/meeting/" + meeting.getId();
            for(int userID : poll.getInvitedUserIds()) {
                EmailService.sendMail(UserServices.getUserEmail(userID), content);
            }

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
