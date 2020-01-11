package DataManagers;


import ErrorClasses.DataBaseErrorException;
import Models.Poll;
import Models.User;

import java.sql.*;

/*
    private boolean notifyDeletedOptionOn = true;
    private boolean notifyNewVoteOn = true;
    private boolean notifyNewOptionOn = true;
    private boolean notifyNewPollCreatedOn = true;
    private boolean notifyAddedToPollOn = true;
    private boolean notifyRemovedFromPollOn = true;
    private boolean notifyPollClosedOn = true;
    private boolean notifyNewMeetingOn = true;
    private boolean notifyCanceledMeetingOn = true;
    private boolean notifyMentionInCommentOn = true;
 */
public class NotificationDataHandler {
    private static final String COLUMNS = "(userId, notifyDeletedOptionOn, notifyNewVoteOn, notifyNewOptionOn, notifyNewPollCreatedOn, " +
            "notifyAddedToPollOn, notifyRemovedFromPollOn, notifyPollClosedOn, notifyNewMeetingOn, notifyCanceledMeetingOn, " +
            "notifyMentionInCommentOn)";

    public static void init() {
        DataManager.dropExistingTable("NotifSettings");
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "NotifSettings " +
                    "(userId INTEGER PRIMARY KEY, " +
                    "notifyDeletedOptionOn INTEGER, " +
                    "notifyNewVoteOn INTEGER, " +
                    "notifyNewOptionOn INTEGER, " +
                    "notifyNewPollCreatedOn INTEGER, " +
                    "notifyAddedToPollOn INTEGER, " +
                    "notifyRemovedFromPollOn INTEGER, " +
                    "notifyPollClosedOn INTEGER, " +
                    "notifyNewMeetingOn INTEGER, " +
                    "notifyCanceledMeetingOn INTEGER, " +
                    "notifyMentionInCommentOn INTEGER)";
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static void addUserNotifSettings(User user) throws DataBaseErrorException {
        String sql = "INSERT INTO NotifSettings " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);

            notifDomainToDB(user, st);
            st.executeUpdate();
            st.close();
        }catch(SQLException se){
            se.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static void updateNotifSettings(User user) throws DataBaseErrorException {
        String sql = "REPLACE INTO NotifSettings " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);

            notifDomainToDB(user, st);
            st.executeUpdate();
            st.close();
        }catch(SQLException se){
            se.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static void setUserNotifSettings(User user) throws DataBaseErrorException {
        String sql = "SELECT * FROM NotifSettings WHERE userId = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            notifDBtoDomain(rs, user);

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void notifDomainToDB(User user, PreparedStatement st) throws SQLException {
        st.setInt(1, user.getId());
        st.setInt(2, user.isNotifyDeletedOptionOn() ? 1 : 0);
        st.setInt(3, user.isNotifyNewVoteOn() ? 1 : 0);
        st.setInt(4, user.isNotifyNewOptionOn() ? 1 : 0);
        st.setInt(5, user.isNotifyNewPollCreatedOn() ? 1 : 0);
        st.setInt(6, user.isNotifyAddedToPollOn() ? 1 : 0);
        st.setInt(7, user.isNotifyRemovedFromPollOn() ? 1 : 0);
        st.setInt(8, user.isNotifyPollClosedOn() ? 1 : 0);
        st.setInt(9, user.isNotifyNewMeetingOn() ? 1 : 0);
        st.setInt(10, user.isNotifyCanceledMeetingOn() ? 1 : 0);
        st.setInt(11, user.isNotifyMentionInCommentOn() ? 1 : 0);
    }

    public static void notifDBtoDomain(ResultSet rs, User user) throws SQLException {
        user.setNotifyDeletedOptionOn(rs.getInt("notifyDeletedOptionOn") == 1);
        user.setNotifyNewVoteOn(rs.getInt("notifyNewVoteOn") == 1);
        user.setNotifyNewOptionOn(rs.getInt("notifyNewOptionOn") == 1);
        user.setNotifyNewPollCreatedOn(rs.getInt("notifyNewPollCreatedOn") == 1);
        user.setNotifyAddedToPollOn(rs.getInt("notifyAddedToPollOn") == 1);
        user.setNotifyRemovedFromPollOn(rs.getInt("notifyRemovedFromPollOn") == 1);
        user.setNotifyPollClosedOn(rs.getInt("notifyPollClosedOn") == 1);
        user.setNotifyNewMeetingOn(rs.getInt("notifyNewMeetingOn") == 1);
        user.setNotifyCanceledMeetingOn(rs.getInt("notifyCanceledMeetingOn") == 1);
        user.setNotifyMentionInCommentOn(rs.getInt("notifyMentionInCommentOn") == 1);
    }
}

/*
    private boolean notifyDeletedOptionOn = true;
    private boolean notifyNewVoteOn = true;
    private boolean notifyNewOptionOn = true;
    private boolean notifyNewPollCreatedOn = true;
    private boolean notifyAddedToPollOn = true;
    private boolean notifyRemovedFromPollOn = true;
    private boolean notifyPollClosedOn = true;
    private boolean notifyNewMeetingOn = true;
    private boolean notifyCanceledMeetingOn = true;
    private boolean notifyMentionInCommentOn = true;
 */