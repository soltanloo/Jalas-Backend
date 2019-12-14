package DataManagers;


//private int id;
//private String firstName;
//private String lastName;
//private String email;
//private List<Integer> createdPollIds;
//private List<Integer> invitedPollIds;
//private List<Integer> createdMeetingIds;
//private List<Integer> invitedMeetingIds;


import ErrorClasses.DataBaseErrorException;
import Models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class UserDataHandler {
    private static final String COLUMNS = "(id, firstName, lastName, email, createdPollIds, invitedPollIds, createdMeetingIds, invitedMeetingIds)";

    public static void init() {
        DataManager.dropExistingTable("User");
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "User " +
                    "(id INTEGER PRIMARY KEY," +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT, " +
                    "createdPollIds TEXT, " +
                    "invitedPollIds TEXT, " +
                    "createdMeetingIds TEXT, " +
                    "invitedMeetingIds TEXT)";
            st.executeUpdate(sql);

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }


    public static boolean addUser(User user) throws DataBaseErrorException {
        String sql = "INSERT INTO User " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);
            userDomainToDB(user, st);
            st.executeUpdate();
            st.close();
            DataBaseConnector.releaseConnection(con);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }


    public static User getUser(int id) throws DataBaseErrorException {
        String sql = "SELECT * FROM User WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            User user = null;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = userDBtoDomain(rs);
            }
            if (user == null)
                return null;

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static User getUserByEmail(String email) throws DataBaseErrorException {
        String sql = "SELECT * FROM User WHERE email = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            User user = null;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = userDBtoDomain(rs);
            }
            if (user == null)
                return null;

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    private static void updateList(String listName, String list, int userId) throws DataBaseErrorException {
        String sql = "UPDATE User SET ? = ? where id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, listName);
            st.setString(2, list);
            st.setInt(3, userId);
            st.executeUpdate();
            st.close();
            DataBaseConnector.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void updateCreatedPollIds(User user) throws DataBaseErrorException {
        updateList("createdPollIds", DataHelpers.stringify(user.getCreatedPollIds()), user.getId());
    }

    public static void updateInvitedPollIds(User user) throws DataBaseErrorException {
        updateList("invitedPollIds", DataHelpers.stringify(user.getInvitedPollIds()), user.getId());
    }

    public static void updateCreatedMeetingIds(User user) throws DataBaseErrorException {
        updateList("createdMeetingIds", DataHelpers.stringify(user.getCreatedPollIds()), user.getId());
    }

    public static void updateInvitedMeetingIds(User user) throws DataBaseErrorException {
        updateList("invitedMeetingIds", DataHelpers.stringify(user.getInvitedMeetingIds()), user.getId());
    }


    public static void userDomainToDB(User user, PreparedStatement st) throws DataBaseErrorException {
        try {
            st.setInt(1, user.getId());
            st.setString(2, user.getFirstName());
            st.setString(3, user.getLastName());
            st.setString(4, user.getEmail());
            st.setString(5, DataHelpers.stringify(user.getCreatedPollIds()));
            st.setString(6, DataHelpers.stringify(user.getInvitedPollIds()));
            st.setString(7, DataHelpers.stringify(user.getCreatedMeetingIds()));
            st.setString(8, DataHelpers.stringify(user.getInvitedMeetingIds()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseErrorException();
        }
    }

    public static User userDBtoDomain(ResultSet rs) throws DataBaseErrorException {
        try {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("firstName"));
            user.setFirstName(rs.getString("lastName"));
            user.setFirstName(rs.getString("email"));
            user.setCreatedPollIds(DataHelpers.makeList(rs.getString("createdPollIds")));
            user.setInvitedPollIds(DataHelpers.makeList(rs.getString("invitedPollIds")));
            user.setCreatedMeetingIds(DataHelpers.makeList(rs.getString("createdMeetingIds")));
            user.setInvitedMeetingIds(DataHelpers.makeList(rs.getString("invitedMeetingIds")));
            return user;
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataBaseErrorException();
        }
    }

    public static void createSeedUsers(JSONArray userJList) throws JSONException, DataBaseErrorException {
        for(int i = 0; i < userJList.length(); i++) {
            JSONObject jUser = userJList.getJSONObject(i);
            User user = new User();

            user.setId(jUser.getInt("id"));
            user.setFirstName(jUser.getString("firstName"));
            user.setLastName(jUser.getString("lastName"));
            user.setEmail(jUser.getString("email"));

            ArrayList<Integer> createdPollIds = new ArrayList<>();
//            JSONArray jcreatedPollIds = jUser.getJSONArray("createdPollIds");
//            for(int j = 0; j < jcreatedPollIds.length(); j++)
//                createdPollIds.add(jcreatedPollIds.getJSONObject(i).getInt("id"));
            user.setCreatedPollIds(createdPollIds);

            ArrayList<Integer> invitedPollIds = new ArrayList<>();
//            JSONArray jinvitedPollIds = jUser.getJSONArray("invitedPollIds");
//            for(int j = 0; j < jinvitedPollIds.length(); j++)
//                invitedPollIds.add(jinvitedPollIds.getJSONObject(i).getInt("id"));
            user.setInvitedPollIds(invitedPollIds);

            ArrayList<Integer> createdMeetingIds = new ArrayList<>();
//            JSONArray jcreatedMeetingIds = jUser.getJSONArray("createdMeetingIds");
//            for(int j = 0; j < jcreatedMeetingIds.length(); j++)
//                createdMeetingIds.add(jcreatedMeetingIds.getJSONObject(i).getInt("id"));
            user.setCreatedMeetingIds(createdMeetingIds);

            ArrayList<Integer> invitedMeetingIds = new ArrayList<>();
//            JSONArray jinvitedMeetingIds = jUser.getJSONArray("invitedMeetingIds");
//            for(int j = 0; j < jinvitedMeetingIds.length(); j++)
//                invitedMeetingIds.add(jinvitedMeetingIds.getJSONObject(i).getInt("id"));
            user.setInvitedMeetingIds(invitedMeetingIds);

            addUser(user);
        }
    }
}

//private int id;
//private String firstName;
//private String lastName;
//private String email;
//private List<Integer> createdPollIds;
//private List<Integer> invitedPollIds;
//private List<Integer> createdMeetingIds;
//private List<Integer> invitedMeetingIds;