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

import java.sql.*;
import java.util.ArrayList;

public class UserDataHandler {
    private static Connection con = null;

    private static final String COLUMNS = "(id, firstName, lastName, email, createdPollIds, invitedPollIds, createdMeetingIds, invitedMeetingIds)";

    public static void init() {
        try {
            DataManager.dropExistingTable("User");
            con = DataBaseConnector.getConnection();
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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean addUser(User user) throws DataBaseErrorException {
        String sql = "INSERT INTO User " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            userDomainToDB(user, st);
            st.executeUpdate();
            st.close();
            con.close();
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataBaseErrorException();
        }
    }


    public static User getUser(int id) throws DataBaseErrorException {
        String sql = "SELECT * FROM User WHERE id = ?";

        try {
            User user = null;
            con = DataBaseConnector.getConnection();
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
            con.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseErrorException();
        }
    }

    private static void updateList(String listName, String list, int userId) throws DataBaseErrorException {
        String sql = "UPDATE User SET ? = ? where id = ?";

        try {
            con = DataBaseConnector.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, listName);
            st.setString(2, list);
            st.setInt(3, userId);
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
}

//private int id;
//private String firstName;
//private String lastName;
//private String email;
//private List<Integer> createdPollIds;
//private List<Integer> invitedPollIds;
//private List<Integer> createdMeetingIds;
//private List<Integer> invitedMeetingIds;