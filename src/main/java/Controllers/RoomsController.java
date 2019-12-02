package Controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import BusinessLogic.RoomServices;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RoomsController {

    @RequestMapping (value = "/api/room", method = RequestMethod.GET)
    public ResponseEntity getRooms (HttpServletRequest req) {

        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");

        List<Integer> roomNumbers = RoomServices.fetchRooms(startTime, endTime);

        if (roomNumbers != null)
            return ResponseEntity.ok(roomNumbers);
        else
            return new ResponseEntity<>("Couldn't fetch rooms list from server!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping(value = "/api/reserveRoom", method = RequestMethod.POST)
    public ResponseEntity reserveRoom(HttpServletRequest req, @RequestBody String reqData) {
        try {
            JSONObject data = new JSONObject(reqData);
            if(RoomServices.reserveRoom(data)) {
                ResponseEntity.ok("Room reserved successfully!");
            }
            else {
                return new ResponseEntity<>("Couldn't reserve room from server!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Couldn't reserve room from server!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
