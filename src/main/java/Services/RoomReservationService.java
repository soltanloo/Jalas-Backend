package Services;

import java.time.Duration;
import java.util.ArrayList;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RoomReservationService {
    private static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static ArrayList<Integer> getFreeRooms(String startTime, String endTime) {
        String uri = "http://213.233.176.40/available_rooms" + "?start=" + startTime + "&end=" + endTime;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .setHeader("User-Agent", "Jalas Juggernaut group client")
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if(response.statusCode() != 200) {
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return null;
        }

        ArrayList<Integer> retRoomList = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(response.body());
            JSONArray roomsList = data.getJSONArray("availableRooms");
            System.out.println(roomsList);
            for(int i = 0; i < roomsList.length(); i++) {
                retRoomList.add(roomsList.getInt(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retRoomList;
    }

    public static boolean reserveRoom(Integer roomNumber, String userName, String startTime, String endTime) {
        String uri = "http://213.233.176.40/rooms/" + roomNumber + "/reserve";
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormData(userName, startTime, endTime))
                .uri(URI.create(uri))
                .header("Content-Type", "application/json;charset=UTF-8")
                .setHeader("User-Agent", "Jalas Juggernaut group client")
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(response.statusCode() != 200) {
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return false;
        }
        else {
            return true;
        }
    }

    private static HttpRequest.BodyPublisher buildFormData(String userName, String startTime, String endTime) {
        JSONObject body = new JSONObject();
        try {
            body.put("username", userName);
            body.put("start", startTime);
            body.put("end", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(body.toString());
        return HttpRequest.BodyPublishers.ofString(body.toString());
    }
}
