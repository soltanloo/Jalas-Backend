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
    public ResponseEntity endorse (HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);

            if(MeetingServices.addMeeting(data)) {
                ResponseEntity.ok("Meeting added successfully!");
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
