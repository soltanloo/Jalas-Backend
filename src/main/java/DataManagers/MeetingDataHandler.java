package DataManagers;

import ErrorClasses.DataBaseErrorException;
import Models.Meeting;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Timestamp;


//private int id;
//private int roomNumber;
//private String startTime;
//private String finishTime;
//private String createTime;
//private String setTime;
//private int ownerId;
//private ArrayList<Integer> invitedUserIds = new ArrayList<>();


public class MeetingDataHandler {
    private static final String COLUMNS = "(id, roomNumber, startTime, finishTime, status, createTime, setTime, ownerId, invitedUserIds, title)";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

    public static void init() {
        DataManager.dropExistingTable("Meeting");
        Connection con = DataBaseConnector.getConnection();
        try {
            Statement st = con.createStatement();

            String sql = "CREATE TABLE " +
                    "Meeting " +
                    "(id INTEGER PRIMARY KEY, " +
                    "roomNumber INTEGER, " +
                    "startTime TEXT, " +
                    "finishTime TEXT, " +
                    "status INTEGER, " +
                    "createTime TEXT, " +
                    "setTime TEXT, " +
                    "ownerId INTEGER, " +
                    "invitedUserIds TEXT, " +
                    "title TEXT)";
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }

    public static boolean addMeeting(Meeting meeting) {
        String sql = "INSERT INTO Meeting " + COLUMNS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DataBaseConnector.getConnection();
        try{
            PreparedStatement st = con.prepareStatement(sql);

            meetingDomainToDB(meeting, st);
            st.executeUpdate();
            st.close();
        }catch(SQLException se){
            se.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            return false;
        }
        DataBaseConnector.releaseConnection(con);
        return true;
    }

    public static Meeting getMeeting (Integer id) throws DataBaseErrorException {
        String sql = "SELECT * FROM Meeting WHERE id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            Meeting meeting = null;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meeting = meetingDBtoDomain(rs);
            }
            if (meeting == null)
                return null;

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return meeting;
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void setMeetingStatus(Meeting meeting) {
        String sql1 = "UPDATE Meeting SET status = ? where id = ?";
        String sql2 = "UPDATE Meeting SET setTime = ? where id = ?";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement st1 = con.prepareStatement(sql1);
            PreparedStatement st2 = con.prepareStatement(sql2);

            st1.setInt(1, Meeting.Status.SET.getLevelCode());
            st2.setString(1, sdf.format(timestamp));

            st1.setInt(2, meeting.getId());
            st2.setInt(2, meeting.getId());

            st1.executeUpdate();
            st2.executeUpdate();

            st1.close();
            st2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
    }
    public static int getSetMeetingsNum(){
        String sql = "SELECT * FROM Meeting WHERE status = ?";
        Connection con = DataBaseConnector.getConnection();
        int meetingsSet = 0;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String dateString = formatter.format(date);
            while (rs.next()) {
                Meeting meeting = meetingDBtoDomain(rs);

                if(meeting.getFinishTime().compareTo(dateString) == 1){
                    meetingsSet += 1;
                }
            }

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return meetingsSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
        return 0;
    }

    public static long getCreationMeanTime() {
        long meanTime = 0;
        String sql = "SELECT * FROM Meeting WHERE status = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, Meeting.Status.SET.getLevelCode());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Meeting meeting = meetingDBtoDomain(rs);
                java.sql.Timestamp create = java.sql.Timestamp.valueOf( meeting.getCreateTime() ) ;
                java.sql.Timestamp set = java.sql.Timestamp.valueOf( meeting.getSetTime() ) ;
                meanTime = create.getTime() - set.getTime();
            }

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return meanTime;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
        return 0;
    }

    public static int getCancelledMeetingsNum() {
        String sql = "SELECT * FROM Meeting WHERE status = ?";
        Connection con = DataBaseConnector.getConnection();
        int meetingsCancelled = 0;
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, -1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meetingsCancelled += 1;
            }

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return meetingsCancelled;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
        return 0;
    }

    public static ArrayList<Meeting> getStalledMeetings() {
        String sql = "SELECT * FROM Meeting WHERE status = ?";
        Connection con = DataBaseConnector.getConnection();
        ArrayList<Meeting> meetings = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, 0);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meetings.add(meetingDBtoDomain(rs));
            }

            stmt.close();
            rs.close();
            DataBaseConnector.releaseConnection(con);
            return meetings;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataBaseConnector.releaseConnection(con);
        return null;
    }

    public static void cancelMeeting(int id) throws DataBaseErrorException {
        String sql = "UPDATE Meeting SET status = ? where id = ?";
        Connection con = DataBaseConnector.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, -1);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            stmt.close();
            DataBaseConnector.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
            DataBaseConnector.releaseConnection(con);
            throw new DataBaseErrorException();
        }
    }

    public static void meetingDomainToDB(Meeting meeting,  PreparedStatement st) {
        try {
            st.setInt(1, meeting.getId());
            st.setInt(2, meeting.getRoomNumber());
            st.setString(3, meeting.getStartTime());
            st.setString(4, meeting.getFinishTime());
            st.setInt(5, meeting.getStatus().getLevelCode());
            st.setString(6, meeting.getCreateTime());
            st.setString(7, meeting.getSetTime());
            st.setInt(8, meeting.getOwnerId());
            st.setString(9, DataHelpers.stringify(meeting.getInvitedUserIds()));
            st.setString(10, meeting.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Meeting meetingDBtoDomain(ResultSet rs) {
        Meeting meeting = new Meeting();
        try {
            meeting.setId(rs.getInt("id"));
            meeting.setRoomNumber(rs.getInt("roomNumber"));
            meeting.setStartTime(rs.getString("startTime"));
            meeting.setFinishTime(rs.getString("finishTime"));
            meeting.setStatus(rs.getInt("status"));
            meeting.setCreateTime(rs.getString("createTime"));
            meeting.setSetTime(rs.getString("setTime"));
            meeting.setOwnerId(rs.getInt("ownerId"));
            meeting.setInvitedUserIds(DataHelpers.makeList(rs.getString("invitedUserIds")));
            meeting.setTitle(rs.getString("title"));
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return meeting;
    }

}
