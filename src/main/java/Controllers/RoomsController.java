package Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import BusinessLogic.RoomServices;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin (origins = "*", allowedHeaders = "*")
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
}
