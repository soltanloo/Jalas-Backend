package Services;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;

import org.json.JSONArray;
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
            return null;
        }

        ArrayList<Integer> retRoomList = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(response.body());
            JSONArray roomsList = data.getJSONArray("availableRooms");
            for(int i = 0; i < roomsList.length(); i++) {
                retRoomList.add(roomsList.getInt(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retRoomList;
    }
}
