package Controllers;

import BusinessLogic.NotificationServices;
import BusinessLogic.PollServices;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.PollAlreadyClosedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class NotificationController {
    @RequestMapping(value = "/api/notification/newOption", method = RequestMethod.POST)
    public ResponseEntity manageNewOption (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        boolean notifyNewOption = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
        try{
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
        boolean notifyNewVote = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyDeletedOption = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyNewPollCreated = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyAddedToPoll = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyRemovedFromPoll = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyPollClosedOn = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyNewMeetingOn = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyCanceledMeeting = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
        boolean notifyMentionInComment = (boolean) req.getAttribute("notifyOn");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        else
            try{
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
