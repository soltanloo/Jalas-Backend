package Controllers;

import BusinessLogic.AnalyticServices.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import static BusinessLogic.AnalyticServices.*;

@CrossOrigin (origins = "*", allowedHeaders = "*")
@RestController
public class AnalyticController {
    @RequestMapping (value = "/api/analytic", method = RequestMethod.GET)
    public ResponseEntity getAnalytics(HttpServletRequest req) {
        int reservedRoomsNum = getReseverdRoomsNum();
        int canceledMeetingsNum = getCanceledMeetingsNum();
//        long creationMeanTime = getCreationMeanTime();

        JSONObject data = new JSONObject();
        try {
            data.put("reservedRoomsNum", reservedRoomsNum);
            data.put("canceledMeetingsNum", canceledMeetingsNum);
//            data.put("creationMeanTime", creationMeanTime);
            return ResponseEntity.ok(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
