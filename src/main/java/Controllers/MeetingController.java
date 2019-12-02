package Controllers;

import BusinessLogic.MeetingServices;
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
    @RequestMapping (value = "/api/meeting", method = RequestMethod.POST)
    public ResponseEntity addMeeting (HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);

            Meeting meeting = MeetingServices.addMeeting(data);
            if(meeting != null) {
                ResponseEntity.ok(meeting);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in reserving room");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/api/meeting/{id}/cancel", method = RequestMethod.POST)
    public ResponseEntity cancelMeeting (HttpServletRequest req, @PathVariable String id) {

        if(MeetingServices.cancelMeeting(Integer.parseInt(id))) {
            return ResponseEntity.ok("deleted");
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
