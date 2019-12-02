package Controllers;

import BusinessLogic.PollServices;
import Models.Poll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class PollsController {
    @RequestMapping (value = "/api/poll", method = RequestMethod.GET)
    public ResponseEntity getRooms (HttpServletRequest req) {

        ArrayList<Poll> polls = PollServices.getAllPolls();
        if (polls != null)
            return ResponseEntity.ok(polls);
        else
            return new ResponseEntity<>("Couldn't fetch polls list from server!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping (value = "/api/poll/{id}", method = RequestMethod.GET)
    public ResponseEntity getProject (HttpServletRequest req, @PathVariable String id) {

        Poll poll = PollServices.getPoll(Integer.parseInt(id));
        if (poll != null)
            return ResponseEntity.ok(poll);
        else
            return new ResponseEntity<>("Poll not found with this ID!", HttpStatus.NOT_FOUND);
    }
}
