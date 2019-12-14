package Controllers;

import BusinessLogic.MeetingServices;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.PollFinishedException;
import ErrorClasses.RoomReservationErrorException;
import Models.Meeting;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin (origins = "*", allowedHeaders = "*")
@RestController
public class MeetingController {
    @RequestMapping (value = "/api/addMeeting", method = RequestMethod.POST)
    public ResponseEntity addMeeting (HttpServletRequest req, @RequestBody String reqData) {
        Meeting meeting = null;
        try {
            JSONObject data = new JSONObject(reqData);

            meeting = MeetingServices.addMeeting(data);
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
        }
    }

    @RequestMapping(value = "/api/meeting/{id}/cancel", method = RequestMethod.POST)
    public ResponseEntity cancelMeeting (HttpServletRequest req, @PathVariable String id) {

        if(MeetingServices.cancelMeeting(id)) {
            return ResponseEntity.ok("deleted");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem id accessing DB");
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
