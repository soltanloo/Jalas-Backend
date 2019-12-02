package DataManagers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataManager {

    public static void init () throws Exception {
        MeetingDataHandler.init();
        PollDataHandler.init();
        PollOptionDataHandler.init();
        DataManager.addSeedPolls();
    }

    private static void addSeedPolls () throws JSONException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        PollDataHandler.createSeedPolls((JSONArray) parser.parse(new FileReader("./../SeedRepository/PollSeed")));
    }

    public static void dropExistingTable (String tableName) {
        try {
            Connection con = DataBaseConnector.getConnection();

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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
