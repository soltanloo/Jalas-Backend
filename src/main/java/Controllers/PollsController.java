package Controllers;

import BusinessLogic.CommentServices;
import BusinessLogic.PollServices;
import ErrorClasses.*;
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
        String filterType = req.getHeader("filter-type");
        String userId = "";

        if (filterType.equals("local")) {
            userId = (String) req.getAttribute("userId");
            if (userId.equals(""))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");
        }

        ArrayList<Poll> polls;
        try {
            if (filterType.equals("global"))
                polls = PollServices.getAllPolls();
            else if (filterType.equals("local"))
                polls = PollServices.getUserPolls(Integer.parseInt(userId));
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown filter type");
            return ResponseEntity.ok(polls);
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
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            Poll poll = PollServices.createPoll(Integer.parseInt(userId), data);
            return ResponseEntity.ok(poll.getId());
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

    @RequestMapping(value = "/api/poll/addOption", method = RequestMethod.POST)
    public ResponseEntity addOption(HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            Poll poll = PollServices.addOptionToPoll(Integer.parseInt(userId), data);
            return ResponseEntity.ok(poll.getId());
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not this poll's owner");
        } catch (PollAlreadyClosedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is closed");
        }
    }

    @RequestMapping(value = "/api/poll/removeOption", method = RequestMethod.POST)
    public ResponseEntity removeOption(HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            Poll poll = PollServices.removeOptionFromPoll(Integer.parseInt(userId), data);
            return ResponseEntity.ok(poll.getId());
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not this poll's owner");
        } catch (PollAlreadyClosedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is closed");
        }
    }

    @RequestMapping(value = "/api/poll/addParticipant", method = RequestMethod.POST)
    public ResponseEntity addParticipant (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            PollServices.addParticipant(Integer.parseInt(userId), data);
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
        } catch (PollAlreadyClosedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is closed");
        }
    }

    @RequestMapping(value = "/api/poll/removeParticipant", method = RequestMethod.POST)
    public ResponseEntity removeParticipant (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try{
            JSONObject data = new JSONObject(reqData);
            PollServices.removeParticipant(Integer.parseInt(userId), data);
            return ResponseEntity.ok("Participant removed");
        } catch(JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sent owner ID does not match poll's owner ID");
        } catch (UserWasNotInvitedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This user was not invited");
        } catch (PollAlreadyClosedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is closed");
        }
    }

    @RequestMapping (value = "/api/vote", method = RequestMethod.POST)
    public ResponseEntity vote (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            PollServices.addVote(Integer.parseInt(userId), data);
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
        } catch (DuplicateVoteException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already voted for this option!");
        } catch (PollFinishedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll finished");
        }
    }

    @PostMapping(value = "/api/poll/comment")
    public ResponseEntity addComment (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            CommentServices.addComment(Integer.parseInt(userId), data);
            return ResponseEntity.ok("Comment added");
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing JSON");
        } catch (ObjectNotFoundInDBException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");
        } catch (NoCommentWithThisId e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment to reply not found");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (UserWasNotInvitedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not invited");
        }
    }

    @RequestMapping(value = "/api/poll/deleteComment", method = RequestMethod.POST)
    public ResponseEntity deleteComment (HttpServletRequest req, @RequestBody String reqData) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            JSONObject data = new JSONObject(reqData);
            CommentServices.deleteComment(Integer.parseInt(userId), data);
            return ResponseEntity.ok("comment successfully deleted");

        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem in parsing the JSON");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        } catch (NoCommentWithThisId noCommentWithThisId) {
            noCommentWithThisId.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid comment");
        } catch (ObjectNotFoundInDBException e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such comment");
        } catch (UserWasNotInvitedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not invited");
        } catch (NotTheOwnerException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this comment");
        }

    }

    @RequestMapping(value = "/api/poll/{id}/close", method = RequestMethod.GET)
    public ResponseEntity closePoll (HttpServletRequest req, @PathVariable String id) {
        String userId = (String) req.getAttribute("userId");
        if (userId.equals(""))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login first");

        try {
            PollServices.closePoll(Integer.parseInt(userId), Integer.parseInt(id));
            return ResponseEntity.ok("Poll Closed");
        } catch (AccessViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this comment");
        } catch (PollAlreadyClosedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is already closed");
        } catch (DataBaseErrorException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in accessing DB");
        }
    }
}
