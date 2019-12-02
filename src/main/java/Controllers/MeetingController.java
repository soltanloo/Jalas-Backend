package Controllers;

import BusinessLogic.MeetingServices;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MeetingController {
    @RequestMapping (value = "/api/meeting", method = RequestMethod.POST)
    public ResponseEntity addMeeting (HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);

            int meetingID = MeetingServices.addMeeting(data);
            if(meetingID != -1) {
                ResponseEntity.ok(meetingID);
            }
            else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
