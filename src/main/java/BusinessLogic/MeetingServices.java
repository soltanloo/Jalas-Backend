package BusinessLogic;

import DataManagers.MeetingDataHandler;
import ErrorClasses.*;
import Models.Meeting;
import Models.Poll;
import Models.User;
import Services.EmailService;
import Services.RoomReservationService;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MeetingServices {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

    public static Meeting addMeeting(int userId, JSONObject data) throws JSONException, RoomReservationErrorException, DataBaseErrorException, PollFinishedException, NotTheOwnerException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Poll poll = PollServices.getPoll(data.getInt("pollId"));

        if (!poll.isOngoing())
            throw new PollFinishedException();
        if (poll.getOwnerId() != userId)
            throw new NotTheOwnerException();

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

            for (int invUserId : poll.getInvitedUserIds()) {
                UserServices.addUserInvitedMeeting(invUserId, meeting.getId());
            }
            String content = "New Meeting has been arranged!\n" +
                    "api/meeting/" + meeting.getId();
            for(int userID : poll.getInvitedUserIds()) {
                try {
                    EmailService.sendMail(UserServices.getUserEmail(userID), content);
                } catch (InvalidEmailAddressException e) {
                    e.printStackTrace();
                }
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

    public static void cancelMeeting(int userId, String meetingId) throws DataBaseErrorException, NotTheOwnerException {
        Meeting meeting = MeetingDataHandler.getMeeting(Integer.parseInt(meetingId));
        if(meeting.getOwnerId() != userId)
            throw new NotTheOwnerException();
        MeetingDataHandler.cancelMeeting(meeting.getId());
    }

    public static Meeting getMeeting(String id) throws DataBaseErrorException {
        return MeetingDataHandler.getMeeting(Integer.parseInt(id));
    }

    public static Meeting getMeeting(int id) throws DataBaseErrorException {
        return MeetingDataHandler.getMeeting(id);
    }

    public static ArrayList<Meeting> getAllMeetings() throws DataBaseErrorException {
        return MeetingDataHandler.getAllMeetings();
    }

    public static ArrayList<Meeting> getUserMeetings(int userId) throws DataBaseErrorException {
        User user = UserServices.getUser(userId);

        ArrayList<Meeting> meetings = new ArrayList<>();
        for (int meetingId : user.getCreatedMeetingIds())
            meetings.add(getMeeting(meetingId));
        for (int meetingId : user.getInvitedMeetingIds())
            meetings.add(getMeeting(meetingId));

        return meetings;
    }
}
