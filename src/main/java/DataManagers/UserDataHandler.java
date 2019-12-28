package DataManagers;


//private int id;
//private String firstName;
//private String lastName;
//private String email;
//private List<Integer> createdPollIds;
//private List<Integer> invitedPollIds;
//private List<Integer> createdMeetingIds;
//private List<Integer> invitedMeetingIds;
//private boolean isLoggedIn = false;
//private String password;
//private String token;


import ErrorClasses.DataBaseErrorException;
import Models.User;
import Services.MD5Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class UserDataHandler {
    private static final String COLUMNS = "(id, firstName, lastName, email, createdPollIds, invitedPollIds, createdMeetingIds, invitedMeetingIds, isLoggedIn, password, role)";

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
                    "email TEXT UNIQUE , " +
                    "createdPollIds TEXT, " +
                    "invitedPollIds TEXT, " +
                    "createdMeetingIds TEXT, " +
                    "invitedMeetingIds TEXT, " +
                    "isLoggedIn INTEGER, " +
                    "password TEXT, " +
                    "role TEXT)";
            st.executeUpdate(sql);

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }


    public static void addUser(User user) throws DataBaseErrorException {
        String sql = "INSERT INTO User " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);
            userDomainToDB(user, st);
            st.executeUpdate();
            st.close();
            DataBaseConnector.releaseConnection(con);
        } catch(SQLException e){
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

    public static int getUserIdByEmail(String email) throws DataBaseErrorException {
        String sql = "SELECT id FROM User WHERE email = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            String userId = rs.getString("id");

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return Integer.parseInt(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    private static void updateList(String sql, String list, int userId) throws DataBaseErrorException {
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, list);
            st.setInt(2, userId);
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
        String sql = "UPDATE User SET createdPollIds = ? WHERE id = ?";
        updateList(sql, DataHelpers.stringify(user.getCreatedPollIds()), user.getId());
    }

    public static void updateInvitedPollIds(User user) throws DataBaseErrorException {
        String sql = "UPDATE User SET invitedPollIds = ? WHERE id = ?";
        updateList(sql, DataHelpers.stringify(user.getInvitedPollIds()), user.getId());
    }

    public static void updateCreatedMeetingIds(User user) throws DataBaseErrorException {
        String sql = "UPDATE User SET createdMeetingIds = ? WHERE id = ?";
        updateList(sql, DataHelpers.stringify(user.getCreatedPollIds()), user.getId());
    }

    public static void updateInvitedMeetingIds(User user) throws DataBaseErrorException {
        String sql = "UPDATE User SET invitedMeetingIds = ? WHERE id = ?";
        updateList(sql, DataHelpers.stringify(user.getInvitedMeetingIds()), user.getId());
    }

    public static String getUserEmail(int id) throws DataBaseErrorException {
        String sql = "SELECT email FROM User WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String email = null;
            while (rs.next()) {
                email = rs.getString("email");
            }

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return email;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static String getUserName(int id) throws DataBaseErrorException {
        String sql = "SELECT firstName, lastName FROM User WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            String name = rs.getString("firstName") + " " + rs.getString("lastName");

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static boolean checkPasswordCorrectness (String email, String password) {
        String sql = "SELECT password FROM USER WHERE email = ?";
        Connection con = DataBaseConnector.getConnection();
        boolean result = false;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.getString(1).equals(password))
                result = true;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
        return result;
    }

    public static void userLogin (String email) {
        String sql = "UPDATE USER SET isLoggedIn = 1 WHERE email = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static int getNumOfRows() throws DataBaseErrorException {
        String sql = "SELECT COUNT(id) as num FROM User";
        Connection con = DataBaseConnector.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int numOfRows = rs.getInt("num");
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return numOfRows;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
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
            if (user.isLoggedIn())
                st.setInt(9, 1);
            else
                st.setInt(9, 0);
            st.setString(10, user.getPassword());
            st.setString(11, user.getRole());
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
            if (rs.getInt("isLoggedIn") == 1)
                user.setLoggedIn(true);
            else
                user.setLoggedIn(false);
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
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
            user.setRole(jUser.getString("role"));

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

            user.setLoggedIn(false);

            String password = MD5Service.changeToMd5(jUser.getString("password"));
            user.setPassword(password);

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