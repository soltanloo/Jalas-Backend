package Controllers;

import BusinessLogic.AnalyticServices.*;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
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
    @RequestMapping (value = "/api/metrics", method = RequestMethod.GET)
    public ResponseEntity getMetrics(HttpServletRequest req) {
        String userId = (String) req.getAttribute("userId");
        if (userId == "")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");
        try {
            int reservedRoomsNum = getReseverdRoomsNum(Integer.parseInt(userId));
            int canceledMeetingsNum = getCanceledMeetingsNum(Integer.parseInt(userId));
            int meetingsNum = getAllMeetingsNum(Integer.parseInt(userId));
            int pollsNum = getAllPollsNum(Integer.parseInt(userId));
            int usersNum = getAllUsersNum(Integer.parseInt(userId));
            int commentsNum = getAllCommentsNum(Integer.parseInt(userId));

            JSONObject data = new JSONObject();
            data.put("reservedRoomsNum", reservedRoomsNum);
            data.put("canceledMeetingsNum", canceledMeetingsNum);
            data.put("meetingsNum", meetingsNum);
            data.put("pollsNum",pollsNum);
            data.put("usersNum",usersNum);
            data.put("commentsNum",commentsNum);
//            data.put("creationMeanTime", creationMeanTime);
            return ResponseEntity.ok(data.toString());
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @RequestMapping (value = "/api/performance", method = RequestMethod.GET)
    public ResponseEntity getPerformance(HttpServletRequest req) {
        String userId = (String) req.getAttribute("userId");
        if (userId == "")
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");
        try {
            int meetingsNum = getAllMeetingsNum(Integer.parseInt(userId));
            int pollsNum = getAllPollsNum(Integer.parseInt(userId));
            long pollCreationMeanTime = getCreationMeanTime(Integer.parseInt(userId));
            double meetingsPerPoll;
            if(pollsNum == 0)
                meetingsPerPoll = -1;
            else
                meetingsPerPoll = meetingsNum / pollsNum;
            JSONObject data = new JSONObject();
            data.put("meetingsNum", meetingsNum);
            data.put("pollsNum",pollsNum);
            data.put("creationMeanTime", pollCreationMeanTime);
            data.put("meetingsPerPoll", meetingsPerPoll);
            return ResponseEntity.ok(data.toString());
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
