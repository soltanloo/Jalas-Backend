package Controllers;

import BusinessLogic.MeetingServices;
import BusinessLogic.PollServices;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.NotTheOwnerException;
import ErrorClasses.PollFinishedException;
import ErrorClasses.RoomReservationErrorException;
import Models.Meeting;
import Models.Poll;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@CrossOrigin (origins = "*", allowedHeaders = "*")
@RestController
public class MeetingController {
    @RequestMapping(value = "/api/meeting", method = RequestMethod.GET)
    public ResponseEntity getMeetings (HttpServletRequest req) {
        String filterType = req.getHeader("filter-type");
        String userId = "";

        if (filterType.equals("local")) {
            userId = (String) req.getAttribute("userId");
            if (userId.equals(""))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");
        }

        ArrayList<Meeting> meetings;
        try {
            if (filterType.equals("global"))
                meetings = MeetingServices.getAllMeetings();
            else if (filterType.equals("local"))
                meetings = MeetingServices.getUserMeetings(Integer.parseInt(userId));
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown filter type");
            return ResponseEntity.ok(meetings);
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }

    @RequestMapping (value = "/api/addMeeting", method = RequestMethod.POST)
    public ResponseEntity addMeeting (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId == "")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        Meeting meeting = null;
        try {
            JSONObject data = new JSONObject(reqData);

            meeting = MeetingServices.addMeeting(Integer.parseInt(userId), data);
            return ResponseEntity.ok(meeting);

        } catch (RoomReservationErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in reserving room");
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing the JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (PollFinishedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll finished");
        } catch (NotTheOwnerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the poll's owner");
        }
    }

    @RequestMapping(value = "/api/meeting/{id}/cancel", method = RequestMethod.POST)
    public ResponseEntity cancelMeeting (HttpServletRequest req, @PathVariable String id) {

        String userId = (String) req.getAttribute("userId");
        if (userId == "")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            MeetingServices.cancelMeeting(Integer.parseInt(userId), id);
            return ResponseEntity.ok("deleted");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem id accessing DB");
        } catch (NotTheOwnerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the poll's owner");
        }
    }

    @RequestMapping(value = "/api/meeting/{id}", method = RequestMethod.GET)
    public ResponseEntity getMeeting (HttpServletRequest req, @PathVariable String id) {
        Meeting meeting = null;
        try {
            meeting = MeetingServices.getMeeting(id);

            if(meeting != null)
                return ResponseEntity.ok(meeting);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Meeting found with this ID");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
}
