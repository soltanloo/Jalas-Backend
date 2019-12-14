package Controllers;

import BusinessLogic.PollServices;
import ErrorClasses.AccessViolationException;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.ObjectNotFoundInDBException;
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
public class PollsController {
    @RequestMapping (value = "/api/poll", method = RequestMethod.GET)
    public ResponseEntity getPolls (HttpServletRequest req) {

        ArrayList<Poll> polls = null;
        try {
            polls = PollServices.getAllPolls();
            if (polls != null)
                return ResponseEntity.ok(polls);
            else
                return new ResponseEntity<>("Couldn't fetch polls list from server!", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }

    @RequestMapping (value = "/api/poll/{id}", method = RequestMethod.GET)
    public ResponseEntity getPoll (HttpServletRequest req, @PathVariable String id) {

        Poll poll = null;
        try {
            poll = PollServices.getPoll(Integer.parseInt(id));
            if (poll != null)
                return ResponseEntity.ok(poll);
            else
                return new ResponseEntity<>("Poll not found with this ID!", HttpStatus.NOT_FOUND);
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }

    @RequestMapping (value = "/api/poll", method = RequestMethod.POST)
    public ResponseEntity poll (HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);
            PollServices.createPoll(data);
            return ResponseEntity.ok("Poll created");
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (ObjectNotFoundInDBException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with this userID");
        }
    }

    @RequestMapping(value = "/api/poll/addParticipant", method = RequestMethod.POST)
    public ResponseEntity addParticipant (HttpServletRequest req, @RequestBody String reqData) {
        try{
            JSONObject data = new JSONObject(reqData);
            PollServices.addParticipant(data);
            return ResponseEntity.ok("Participant added");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sent owner ID does not match poll's owner ID");
        }
    }

    @RequestMapping (value = "/api/vote", method = RequestMethod.POST)
    public ResponseEntity vote (HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);
            PollServices.addVote(data);
            return ResponseEntity.ok("Vote added");
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (ObjectNotFoundInDBException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll option not found");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OptionID not in poll or user is not invited");
        }
    }
}
