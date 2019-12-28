package Controllers;

import BusinessLogic.CommentServices;
import BusinessLogic.RoomServices;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.NoCommentWithThisId;
import ErrorClasses.ObjectNotFoundInDBException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CommentController {

    @RequestMapping(value = "/api/deleteComment", method = RequestMethod.POST)
    public ResponseEntity deleteComment (HttpServletRequest req, @RequestBody String reqData) {

        try {
            JSONObject data = new JSONObject(reqData);
            CommentServices.deleteComment(data);
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
        }

    }
}
