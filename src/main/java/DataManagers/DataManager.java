package DataManagers;

import SeedsRepository.PollSeed;
import SeedsRepository.UserSeed;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {

    public static void init () throws Exception {
        DataBaseConnector.init();
        MeetingDataHandler.init();
        PollDataHandler.init();
        PollOptionDataHandler.init();
        UserDataHandler.init();
        CommentDataHandler.init();
        NotificationDataHandler.init();
        addSeedUsers();
        //DataManager.addSeedPolls();
    }

    private static void addSeedPolls () throws JSONException{
        JSONObject file = new JSONObject(PollSeed.pollSeed);
        PollDataHandler.createSeedPolls(file.getJSONArray("polls"));
    }

    private static void addSeedUsers () throws Exception {
        JSONObject file = new JSONObject(UserSeed.userList);
        UserDataHandler.createSeedUsers(file.getJSONArray("users"));
    }

    public static void dropExistingTable (String tableName) {
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement stmt = con.createStatement();
            String    sql  = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
            ResultSet rs   = stmt.executeQuery(sql);

            while (rs.next()) {
                if (tableName.equals(rs.getString("name"))) {
                    sql = "DROP TABLE " + tableName;
                    stmt.executeUpdate(sql);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }
}
