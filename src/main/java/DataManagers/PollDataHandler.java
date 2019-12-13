package DataManagers;

import Models.Poll;
import Models.PollOption;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

/*
    private int id;
    private String title;
    private ArrayList<PollOption> options;
 */

public class PollDataHandler {
    private static Connection con = null;

    private static final String COLUMNS = "(id, title, options, isOngoing)";

    public static void init() {
        try {
            DataManager.dropExistingTable("Poll");
            con = DataBaseConnector.getConnection();
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "Poll " +
                    "(id INTEGER PRIMARY KEY," +
                    "title TEXT , " +
                    "options TEXT, " +
                    "isOngoing INTEGER)";
            st.executeUpdate(sql);

            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addPoll(Poll poll) {
        String sql = "INSERT INTO Poll " + COLUMNS + " VALUES (?, ?, ?, ?)";

        try{
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);

            for (PollOption pollOption : poll.getOptions()) {
                PollOptionDataHandler.addOption(pollOption);
            }
            pollDomainToDB(poll, st);
            st.executeUpdate();
            st.close();
            con.close();
        }catch(SQLException se){
            se.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<Poll> getAllPols() {
        String sql = "SELECT * FROM Poll";

        ArrayList<Poll> polls = new ArrayList<>();
        try {
            con = DataBaseConnector.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                polls.add(pollDBtoDomain(rs));
            }

            stmt.close();
            rs.close();
            con.close();
            return polls;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Poll getPoll(int id) {
        String sql = "SELECT * FROM Poll WHERE id = ?";

        try {
            Poll poll = null;
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                poll = pollDBtoDomain(rs);
            }

            if (poll == null)
                return null;

            stmt.close();
            rs.close();
            con.close();
            return poll;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean setOngoingStatus(int id) {
        String sql = "UPDATE Poll SET isOngoing = ? where id = ?";

        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 0);
            stmt.setInt(2, id);

            stmt.executeUpdate();
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void pollDomainToDB(Poll poll,  PreparedStatement st) {
        try {
            st.setInt(1, poll.getId());
            st.setString(2, poll.getTitle());
            st.setString(3, DataHelpers.stringify(poll.getOptionsIDs()));
            if (poll.isOngoing())
                st.setInt(4, 1);
            else
                st.setInt(4, 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Poll pollDBtoDomain(ResultSet rs) {
        try {
            Poll poll = new Poll();
            poll.setId(rs.getInt("id"));
            poll.setTitle(rs.getString("title"));
            ArrayList<PollOption> pollOptions = new ArrayList<>();
            String userList = rs.getString("options");
            for(String id : DataHelpers.makeList(userList)) {
                pollOptions.add(PollOptionDataHandler.getPollOption(Integer.parseInt(id)));

            }
            poll.setOptions(pollOptions);
            poll.setOngoing(rs.getInt("isOngoing") == 1);
            return poll;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createSeedPolls(JSONArray jsonPolls) throws JSONException {
        for(int i = 0; i < jsonPolls.length(); i++) {
            JSONObject jPoll = jsonPolls.getJSONObject(i);
            Poll poll = new Poll();

            poll.setId(jPoll.getInt("id"));
            poll.setTitle(jPoll.getString("title"));

            JSONArray jOptions = jPoll.getJSONArray("options");
            ArrayList<PollOption> pollOptions = new ArrayList<>();
            for(int j = 0; j < jOptions.length(); j++) {
                PollOption pollOption = new PollOption(jOptions.getJSONObject(j).getString("startTime"), jOptions.getJSONObject(j).getString("finishTime"));
                pollOption.setId(jOptions.getJSONObject(j).getInt("id"));
                pollOption.setUserList(DataHelpers.makeList(jOptions.getJSONObject(j).getString("userList")));
                pollOptions.add(pollOption);
            }
            poll.setOptions(pollOptions);
            poll.setOngoing(jPoll.getInt("isOngoing") == 1);

            addPoll(poll);
        }
    }
}
