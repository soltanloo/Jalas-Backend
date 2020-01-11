package Controllers;

import BusinessLogic.NotificationServices;
import BusinessLogic.PollServices;
import BusinessLogic.UserServices;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.PollAlreadyClosedException;
import Models.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
    @GetMapping("/api/getProfile")
    public ResponseEntity getUser(HttpServletRequest req) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            User user = UserServices.getUser(Integer.parseInt(userId));
            return ResponseEntity.ok(user);
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/newOption", method = RequestMethod.POST)
    public ResponseEntity manageNewOption (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyNewOption = data.getBoolean("notifyOn");
            NotificationServices.manageNewOption(Integer.parseInt(userId), notifyNewOption);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/newVote", method = RequestMethod.POST)
    public ResponseEntity manageNewVote (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyNewVote = data.getBoolean("notifyOn");
            NotificationServices.manageNewVote(Integer.parseInt(userId), notifyNewVote);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/deletedOption", method = RequestMethod.POST)
    public ResponseEntity manageDeletedOption (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyDeletedOption = data.getBoolean("notifyOn");
            NotificationServices.manageDeletedOption(Integer.parseInt(userId), notifyDeletedOption);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/newPollCreated", method = RequestMethod.POST)
    public ResponseEntity manageNewPollCreated (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyNewPollCreated = data.getBoolean("notifyOn");
            NotificationServices.manageNewPollCreation(Integer.parseInt(userId), notifyNewPollCreated);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/AddedToPoll", method = RequestMethod.POST)
    public ResponseEntity manageAddedToPoll (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyAddedToPoll = data.getBoolean("notifyOn");
            NotificationServices.manageAddedToPoll(Integer.parseInt(userId), notifyAddedToPoll);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }

    @RequestMapping(value = "/api/notification/RemovedFromPoll", method = RequestMethod.POST)
    public ResponseEntity manageRemovedFromPoll (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyRemovedFromPoll = data.getBoolean("notifyOn");
            NotificationServices.manageRemovedFromPoll(Integer.parseInt(userId), notifyRemovedFromPoll);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/pollClosed", method = RequestMethod.POST)
    public ResponseEntity managePollClosedOn (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyPollClosedOn = data.getBoolean("notifyOn");
            NotificationServices.managePollClosed(Integer.parseInt(userId), notifyPollClosedOn);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/newMeeting", method = RequestMethod.POST)
    public ResponseEntity manageNewMeetingOn (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyNewMeetingOn = data.getBoolean("notifyOn");
            NotificationServices.manageNewMeeting(Integer.parseInt(userId), notifyNewMeetingOn);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/cancelMeeting", method = RequestMethod.POST)
    public ResponseEntity manageCanceledMeeting (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyCanceledMeeting = data.getBoolean("notifyOn");
            NotificationServices.manageCanceledMeeting(Integer.parseInt(userId), notifyCanceledMeeting);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
    @RequestMapping(value = "/api/notification/mention", method = RequestMethod.POST)
    public ResponseEntity manageMentionInComment (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            boolean notifyMentionInComment = data.getBoolean("notifyOn");
            NotificationServices.manageMentionInComment(Integer.parseInt(userId), notifyMentionInComment);
            return ResponseEntity.ok("Notification Setting changed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
}
